package com.wu.qcc.model;

import org.springframework.context.annotation.Bean;

import lombok.Data;

@Data

public class BranchRevisionDAO {

	private Long branch;
	private Long revision;
	private int branchIndex;
	private int revisionIndex;
	private Long fileID;
	
	
	
}
