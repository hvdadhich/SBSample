package com.wu.qcc.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.IncorrectLineLengthException;
import org.springframework.beans.factory.annotation.Value;

import com.wu.qcc.model.AccountDAO;
import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.util.FlowSingleton;
import com.wu.qcc.util.StatSingleton;

import lombok.Data;

@Data
public class InputReadListener implements ItemReadListener<AccountDAO> {

	private static final Logger log = LogManager.getLogger(InputReadListener.class);
	private String billerPrefix;

	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@Override
	public void beforeRead() {
		

	}

	@Override
	public void afterRead(AccountDAO item) {

		if (stepExecution.getStepName().contains("validate"))
			StatSingleton.getInstance().incrementReadValidateCount();
		
		if (stepExecution.getStepName().contains("billerExecution"))
			StatSingleton.getInstance().incrementReadCount();

	}

	@Override
	public void onReadError(Exception ex) {
		// TODO Auto-generated method stub

		if (stepExecution.getStepName().contains("validate")) {
			CustomFlowVaribalePojo singleton = FlowSingleton.getInstance().getMap(billerPrefix);
			log.error("Inside Read Listener Exception:- " + ex.toString());
			if (ex.getClass() == FlatFileParseException.class) {
				singleton.setReadSkip(singleton.getReadSkip() + 1);
				singleton.getExceptionList().add(ex);
				StatSingleton.getInstance().incrementErrorReadCount();
			}

		}
		
		
		
		
		

	}

}
