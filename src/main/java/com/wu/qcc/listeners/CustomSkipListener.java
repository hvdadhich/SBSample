package com.wu.qcc.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.SkipListener;

import com.wu.qcc.model.AccountDAO;
import com.wu.qcc.model.CustomFlowVaribalePojo;
import com.wu.qcc.util.FlowSingleton;

import lombok.Data;

@Data
public class CustomSkipListener implements SkipListener<AccountDAO, AccountDAO> {

	private Integer skipLimit;
	private String billerPrefix;
	
	private static final Logger log = LogManager.getLogger(CustomSkipListener.class);
	@Override
	public void onSkipInRead(Throwable t) {
		CustomFlowVaribalePojo singletonobj = FlowSingleton.getInstance().getMap(billerPrefix);
		
		if(singletonobj.getReadSkip()>=skipLimit)
		{
			singletonobj.setValidatedFailed(true);
			log.info("Got Skip in Read Exception :- "+t);
			log.info("Setting Failed Due to Exception As true");
			singletonobj.setFailedDueException(true);
			FlowSingleton.getInstance().putMap(billerPrefix, singletonobj);
		}
	}

	@Override
	public void onSkipInWrite(AccountDAO item, Throwable t) {
		//Implement if skip in write required.
		
	}

	@Override
	public void onSkipInProcess(AccountDAO item, Throwable t) {
		// TODO Auto-generated method stub
		
	}

}
