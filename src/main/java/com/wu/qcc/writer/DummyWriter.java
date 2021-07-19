package com.wu.qcc.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.wu.qcc.model.AccountDAO;

public class DummyWriter implements ItemWriter<AccountDAO> {

	@Override
	public void write(List<? extends AccountDAO> items) throws Exception {
		
		
		
	}

}
