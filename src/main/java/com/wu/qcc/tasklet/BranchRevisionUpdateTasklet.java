package com.wu.qcc.tasklet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.couchbase.client.core.message.kv.subdoc.multi.Lookup;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.subdoc.DocumentFragment;
import com.wu.qcc.model.BranchRevisionDAO;
import com.wu.qcc.model.Revision;
import com.wu.qcc.model.RevisionStatus;
import com.wu.qcc.repository.MetadataRepository;
import com.wu.qcc.util.BranchRevisionUtil;

import lombok.Data;

@Data
public class BranchRevisionUpdateTasklet implements Tasklet {

	private static final Logger log = LogManager.getLogger(BranchRevisionUpdateTasklet.class);

	private String billerPrefix;

	@Autowired
	private BranchRevisionDAO branchRevisionDao;

	private static final SimpleDateFormat DefaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	@Autowired
	private MetadataRepository metaDatarepo;

	@Autowired
	private BranchRevisionUtil branchRevisionUtil;
	
	//EXCEPTION HANDLING IS PENDING
	//retry listener pending.
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		
		List<Revision> revisionList=branchRevisionUtil.getAllRevision(branchRevisionDao.getBranchIndex(),getBillerPrefix());
		 int revisionIndex = -1;
		 Revision revision = null;
		for(int j = 0; j < revisionList.size() && revisionIndex < 0; j++) {
            revision = revisionList.get(j);
            if(revision.getFileId() ==  branchRevisionDao.getFileID()) {
                revisionIndex = j;

            }
            
            
        }
		
		revision.setStatus(RevisionStatus.Valid.toString());
		
		branchRevisionUtil.updateMetaDocument(getBillerPrefix(), branchRevisionDao.getBranchIndex(), revisionIndex, revision);
		
		//on success
		metaDatarepo.getCouchbaseOperations().getCouchbaseBucket().mutateIn(branchRevisionUtil.getBillerMetaDataName(getBillerPrefix()))
        .upsert("currentBranch", branchRevisionDao.getBranch()).execute();
		
		
		//email alert
		
		
	return null;
	}


	
}
