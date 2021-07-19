package com.wu.qcc.tasklet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.couchbase.client.core.message.kv.subdoc.multi.Lookup;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonLongDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.subdoc.DocumentFragment;
import com.wu.qcc.Exception.BillerException;
import com.wu.qcc.interfaces.factory.LoadTypeFactory;
import com.wu.qcc.model.BillerMetaData;
import com.wu.qcc.model.Branch;
import com.wu.qcc.model.BranchRevisionDAO;
import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.model.Revision;
import com.wu.qcc.model.RevisionBuilder;
import com.wu.qcc.model.RevisionStatus;
import com.wu.qcc.repository.MetadataRepository;
import com.wu.qcc.util.BranchRevisionUtil;
import com.wu.qcc.util.FlowSingleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Data;

@Data
public class BranchRevisionFetchTasklet implements Tasklet {

	private static final Logger log = LogManager.getLogger(BranchRevisionFetchTasklet.class);

	private String filePath;

	private String billerPrefix;

	private static final SimpleDateFormat DefaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Autowired
	private MetadataRepository metaDatarepo;

	@Autowired
	private BranchRevisionDAO branchRevisionDao;

	@Autowired
	private BranchRevisionUtil branchRevisionUtil;

	@Autowired
	private LoadTypeFactory loadFactory;

	//EXCEPTION HANDLING IS PENDING.
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Long branchno;
		Long revisionno;
		CustomFlowVaribalePojo cfvp = new CustomFlowVaribalePojo();
		cfvp.setProcessStart(true);
		FlowSingleton.getInstance().putMap(billerPrefix, cfvp);

		Optional<BillerMetaData> optionalMetadata;

		try {
			optionalMetadata = metaDatarepo
					.findById(branchRevisionUtil.getBillerMetaDataName(billerPrefix.toLowerCase()));
			BillerMetaData metadata;

			// add Exception Scenerio
			if (!optionalMetadata.isPresent()) {
				metadata = new BillerMetaData();
				metadata.setType("biller");
				metadata.setBranchSequence(-1);
				metadata.setCreatedOn(DefaultFormat.format(new Date()));
				metadata.setId(branchRevisionUtil.getBillerMetaDataName(billerPrefix.toLowerCase()));
				metadata.setPrefix(billerPrefix);
				metadata.setName(billerPrefix);
				metadata.setCurrentBranch(-1);
				metadata.setBranches(Collections.emptyList());
				metaDatarepo.save(metadata);

			}
		} catch (Exception e) {
			log.error("Failed in CB while fetch/inital insert");
			log.error(e);
			cfvp.setFailedDueException(true);
			cfvp.setException(e);
			FlowSingleton.getInstance().putMap(billerPrefix, cfvp);
			throw (e);
		}

		boolean fullLoad;

		try {
			fullLoad = loadFactory.getType(billerPrefix).isFullLoad(getFilePath());
		} catch (BillerException e) {
			cfvp.setFailedDueException(true);
			cfvp.setException(e);
			FlowSingleton.getInstance().putMap(billerPrefix, cfvp);
			return RepeatStatus.FINISHED;
		}

		if (fullLoad)
//		if(isFullLoad())
		{
			Branch branch = new Branch().setRevisionSequence(-1);

			Long branchId = metaDatarepo.getCouchbaseOperations().getCouchbaseBucket()
					.mutateIn(branchRevisionUtil.getBillerMetaDataName(billerPrefix.toLowerCase()))
					.counter("branchSequence", 1).execute().content(0, Long.class);

			
			branch.setBranchId(branchId.intValue());
			branch.setLastUpdateOn(DefaultFormat.format(new Date()));

			JsonObject content = JsonObject.create().put("branchId", branch.getBranchId()).put("revisionSequence", -1)
					.put("lastUpdateOn", branch.getLastUpdateOn()).put("revisions", Collections.emptyList());

			metaDatarepo.getCouchbaseOperations().getCouchbaseBucket()
					.mutateIn(branchRevisionUtil.getBillerMetaDataName(getBillerPrefix().toLowerCase()))
					.arrayPrepend("branches", content).execute();

			branchno = branchId;

		} else {
			if (!optionalMetadata.isPresent()) {
				log.error("Doing Increemntal before Full load");
				cfvp.setFailedDueException(true);
				cfvp.setException(new Exception("Doing incremental before full load"));
				FlowSingleton.getInstance().putMap(billerPrefix, cfvp);
//				throw(new Exception("Doing incremental before full load"));
				return RepeatStatus.FINISHED;
			}

			branchno = (long) optionalMetadata.get().getCurrentBranch();

			if (branchno < 0) {
				log.error("Doing Incremntal before Full load");
				cfvp.setFailedDueException(true);
				cfvp.setException(new Exception("Doing incremental before full load"));
				
				return RepeatStatus.FINISHED;
			}

		}

		int branchindex = branchRevisionUtil.getIndexOfId(Integer.parseInt(branchno.toString()),
				branchRevisionUtil.getAllBranches(getBillerPrefix()));

		branchRevisionDao.setFileID(branchRevisionUtil.getNextFileId());

		Revision revision = new RevisionBuilder().setBranchId(branchno).setFileId(branchRevisionDao.getFileID())
				.setStatus(RevisionStatus.Pending.toString()).createRevision();

		Long revisionId = metaDatarepo.getCouchbaseOperations().getCouchbaseBucket()
				.mutateIn(branchRevisionUtil.getBillerMetaDataName(getBillerPrefix().toLowerCase()))
				.counter(String.format("branches[%s].revisionSequence", branchindex), 1, true).execute()
				.content(0, Long.class);

		revision.setRevisionId(revisionId.longValue());
		revision.setLastUpdatedOn(DefaultFormat.format(new Date()));
		revision.setLastUpdateBy("system");

		JsonObject content = JsonObject.create().put("branchId", revision.getBranchId())
				.put("revisionId", revision.getRevisionId()).put("status", revision.getStatus())
				.put("lastUpdatedOn", revision.getLastUpdatedOn()).put("lastUpdateBy", revision.getLastUpdateBy())
				.put("mamfileId", revision.getFileId());

		DocumentFragment fragment = metaDatarepo.getCouchbaseOperations().getCouchbaseBucket()
				.mutateIn(branchRevisionUtil.getBillerMetaDataName(getBillerPrefix().toLowerCase()))
				.arrayPrepend(String.format("branches[%s].revisions", branchindex), content, true).execute();

		branchRevisionDao.setBranch(branchno);
		branchRevisionDao.setBranchIndex(branchindex);
		branchRevisionDao.setRevision(revisionId);

		cfvp.setBranchInsert(true);
		cfvp.setRevisionInsert(true);
		FlowSingleton.getInstance().putMap(billerPrefix, cfvp);

		return RepeatStatus.FINISHED;

//		return null;

	}

}
