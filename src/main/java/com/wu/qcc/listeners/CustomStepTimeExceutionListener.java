package com.wu.qcc.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.util.FlowSingleton;
import com.wu.qcc.util.StatSingleton;

import lombok.Data;

@Data
public class CustomStepTimeExceutionListener implements StepExecutionListener {
	private static final Logger log = LogManager.getLogger(CustomStepTimeExceutionListener.class);
	public static long stepStartTime;	
	String billerPrefix;
	@Override
	public void beforeStep(StepExecution stepExecution) {

		log.info("Started Step :- "+stepExecution.getStepName());
		stepStartTime=System.currentTimeMillis();
		StatSingleton.getInstance().putMap(stepExecution.getStepName(), stepStartTime);

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		log.info("Summary:-"+stepExecution.getSummary());
		log.info("Completed Step:- "+stepExecution.getStepName()+" Time Taken:- "+(System.currentTimeMillis()-stepStartTime));

		
		
		
		StatSingleton.getInstance().putMap(stepExecution.getStepName(), (System.currentTimeMillis()-StatSingleton.getInstance().getMap(stepExecution.getStepName())));
		

		log.info("Step Execution :-"+stepExecution.getExitStatus());
		
		
		
		return stepExecution.getExitStatus();
		
	}

}
