package com.wu.qcc.tasklet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.Data;

@Data
public class FailedAtStartTasklet implements Tasklet {
	private static final Logger log = LogManager.getLogger(FailedAtStartTasklet.class);
	private String billerPrefix;
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.error("Failed in start:- "+billerPrefix);
		
		//email alert
		return null;
	}

}
