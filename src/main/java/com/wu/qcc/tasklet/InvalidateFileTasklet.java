package com.wu.qcc.tasklet;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.wu.qcc.model.BranchRevisionDAO;
import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.model.Revision;
import com.wu.qcc.model.RevisionStatus;
import com.wu.qcc.repository.MetadataRepository;
import com.wu.qcc.util.BranchRevisionUtil;
import com.wu.qcc.util.FlowSingleton;

import lombok.Data;

@Data
public class InvalidateFileTasklet implements Tasklet {
	private static final Logger log = LogManager.getLogger(InvalidateFileTasklet.class);
	private String billerPrefix;
	@Autowired
	BranchRevisionDAO branchRevisionDao;
	@Autowired
	BranchRevisionUtil branchRevisionUtil;

	private static final SimpleDateFormat DefaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Autowired
	private MetadataRepository metaDatarepo;
//EXCEPTION HANDLING IS PENDING

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		updateStatus(RevisionStatus.Failed,getBillerPrefix(),branchRevisionDao,branchRevisionUtil);

		// email alert

		return RepeatStatus.FINISHED;

	}
//make status method.
	public static void updateStatus(RevisionStatus rs,String billerPrefix,BranchRevisionDAO branchRevisionDao,BranchRevisionUtil branchRevisionUtil) throws Exception {
		CustomFlowVaribalePojo customObject = FlowSingleton.getInstance().getMap(billerPrefix);

		if (customObject.isBranchInsert()) {
			List<Revision> revisionList = branchRevisionUtil.getAllRevision(branchRevisionDao.getBranchIndex(),
					billerPrefix);
			int revisionIndex = -1;
			Revision revision = null;
			for (int j = 0; j < revisionList.size() && revisionIndex < 0; j++) {
				revision = revisionList.get(j);
				if (revision.getFileId() == branchRevisionDao.getFileID()) {
					revisionIndex = j;

				}

			}

			revision.setStatus(rs.toString());

			branchRevisionUtil.updateMetaDocument(billerPrefix, branchRevisionDao.getBranchIndex(), revisionIndex,
					revision);
		}

	}
}
