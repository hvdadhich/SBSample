package com.wu.qcc.listeners;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;

import com.wu.qcc.model.AccountDAO;
import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.util.FlowSingleton;
import com.wu.qcc.util.StatSingleton;

import lombok.Data;

@Data
public class CustomWriteListener implements ItemWriteListener<AccountDAO> {

	@Value("#{stepExecution}")
	private StepExecution stepExecution;
	
	
	private static final Logger log = LogManager.getLogger(CustomWriteListener.class);
	private String billerPrefix;
	
	@Override
	public void beforeWrite(List<? extends AccountDAO> items) {
	log.debug("Going to Write  records :- "+items.size());
		
	}

	@Override
	public void afterWrite(List<? extends AccountDAO> items) {
		log.debug("Written  records :- "+items.size());
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends AccountDAO> items) {
		
		log.error("Inside write Listener Exception Occured:- " + exception.toString());
				StatSingleton.getInstance().incrementErrorWriteCount();
				

		
	}

}
