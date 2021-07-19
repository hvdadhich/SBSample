package com.wu.qcc.model;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomFlowVaribalePojo {

	private boolean processStart=false;
	
	private String fileName;
	
	private boolean branchInsert=false;
	
	private boolean revisionInsert=false;
	
	private Exception exception;
	
	private boolean failedDueException=false;
	
	private boolean validatedFailed=false;
	
	private Integer readSkip=0;
	
	private Integer writeSkip=0;
	
	private ArrayList<Exception> exceptionList = new ArrayList();
	
	
	
	
}
