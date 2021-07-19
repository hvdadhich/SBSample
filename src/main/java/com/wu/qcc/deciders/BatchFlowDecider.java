package com.wu.qcc.deciders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.util.FlowSingleton;

import lombok.Data;
@Data
public class BatchFlowDecider implements JobExecutionDecider {

	private static final Logger log = LogManager.getLogger(BatchFlowDecider.class);
	
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		
		
		String billerPrefix=jobExecution.getJobParameters().getString("billerPrefix");
		
		CustomFlowVaribalePojo customObject = FlowSingleton.getInstance().getMap(billerPrefix);
		
		if(!customObject.isProcessStart())
		{
			log.info("Process is not Started Exiting");
			return new FlowExecutionStatus("NOTSTARTED");
		}
		
		if(customObject.isFailedDueException())
		{
			log.error("Flow as got Exception, will invalidate the load");
			if(customObject.isBranchInsert() || customObject.isValidatedFailed())
				return new FlowExecutionStatus("INVALIDATE");
			else
				return new FlowExecutionStatus("NOTSTARTED");
		}
			
		
		if(stepExecution.getExitStatus().equals(ExitStatus.FAILED)||stepExecution.getExitStatus().equals(ExitStatus.STOPPED))
		{
			log.error("Flow as got Exception, will invalidate the load");
			log.error(stepExecution.getFailureExceptions());
			if(customObject.isBranchInsert() || customObject.isValidatedFailed())
				return new FlowExecutionStatus("INVALIDATE");			
				
		}
		
		
		return new FlowExecutionStatus("VALIDATED");
	}

}
