package com.wu.qcc.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.wu.qcc.model.BranchRevisionDAO;
import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.model.RevisionStatus;
import com.wu.qcc.tasklet.InvalidateFileTasklet;
import com.wu.qcc.util.BranchRevisionUtil;
import com.wu.qcc.util.FlowSingleton;
import com.wu.qcc.util.StatSingleton;

public class CustomJobExecutionListener implements JobExecutionListener {
	
	private static final Logger log = LogManager.getLogger(CustomJobExecutionListener.class);
	public static long jobStartTime;
	@Autowired
	BranchRevisionDAO branchRevisionDao;
	@Autowired
	BranchRevisionUtil branchRevisionUtil;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		jobStartTime=System.currentTimeMillis();
		log.info("Started Job");

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		log.info("Checkig if file needs to be invlidated:-");
		CustomFlowVaribalePojo customObject = FlowSingleton.getInstance().getMap(jobExecution.getJobParameters().getString("billerPrefix"));
		
		
		
		if(jobExecution.getExitStatus().equals(ExitStatus.FAILED))
		{
			if(customObject.isBranchInsert())
				try {
					InvalidateFileTasklet.updateStatus(RevisionStatus.Failed, jobExecution.getJobParameters().getString("billerPrefix"),branchRevisionDao , branchRevisionUtil );
				} catch (Exception e1) {
					log.error("Error while making file invalidate DO it manually:-"+ customObject +" Exception:- "+e1);
				}
		}
		
		
		log.info("Time taken in Job execution:-"+(System.currentTimeMillis()-jobStartTime));
		log.info("-:STATS SUMMARY:-");
		log.info("Read Count Validatation:-"+StatSingleton.getInstance().getReadCountValidateStep());
		log.info("Read Count:-"+StatSingleton.getInstance().getReadCount());
		log.info("Modified Read Records Count:-"+StatSingleton.getInstance().getModifiedReadCount());
		log.info("Write Count:-"+StatSingleton.getInstance().getWriteCount());
		log.info("Read Error Count:-"+StatSingleton.getInstance().getErrorReadCount());
		log.info("Write Error Count:-"+StatSingleton.getInstance().getErrorWriteCount());
		log.info("Steps Exceution Times in Miliseconds(/60000 Minutes) :-"+StatSingleton.getInstance().getStepsDetailsMap());
		CustomFlowVaribalePojo customVaiables = FlowSingleton.getInstance().getMap(jobExecution.getJobParameters().getString("billerPrefix"));
		
		log.info("Custom FLow Variables:- "+customVaiables.toString());
		if(customVaiables.getExceptionList().size()>0)
			customVaiables.getExceptionList().parallelStream().forEach(e->log.error(e));
		
	}

}
