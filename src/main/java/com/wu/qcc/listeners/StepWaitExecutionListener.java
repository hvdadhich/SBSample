package com.wu.qcc.listeners;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import lombok.Data;

@Data
public class StepWaitExecutionListener implements StepExecutionListener {

	private Long beforeStepWait=0L;
	
	private Long afterStepWait=0L;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {

		if(beforeStepWait>0)
			try {
				Thread.sleep(beforeStepWait*60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		if(afterStepWait>0)
			try {
				Thread.sleep(afterStepWait*60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return stepExecution.getExitStatus();
	}

}
