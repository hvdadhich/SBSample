package com.wu.qcc.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

import com.wu.qcc.model.BranchRevisionDAO;
import com.wu.qcc.model.RevisionStatus;
import com.wu.qcc.tasklet.BranchRevisionUpdateTasklet;
import com.wu.qcc.tasklet.InvalidateFileTasklet;
import com.wu.qcc.util.BranchRevisionUtil;

import lombok.Data;
@Data
public class CustomRetryListener implements RetryListener {
	private static final Logger log = LogManager.getLogger(CustomRetryListener.class);
	private String billerPrefix;
	 
	
	@Autowired
	BranchRevisionDAO branchRevisionDao;
	@Autowired
	BranchRevisionUtil branchRevisionUtil;
	
	@Override
	public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {

		log.error("Custom Retry Listener:- Excption got:- "+context.getLastThrowable());
		
		return false;
	}

	@Override
	public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
			Throwable throwable) {
//		log.error("Retry Listener Excausted invlidating file.:- "+context.getLastThrowable());
//	try {
//		InvalidateFileTasklet.updateStatus(RevisionStatus.Failed, billerPrefix, branchRevisionDao, branchRevisionUtil);
//	} catch (Exception e) {
//		log.error("Error While updating status, Please update Manually:- "+e);
//	}
//		
//		log.error("Retry Listener Excausted:- "+context.getLastThrowable());
	}

	@Override
	public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
			Throwable throwable) {
		// TODO Auto-generated method stub

	}

}
