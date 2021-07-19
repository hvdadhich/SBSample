package com.wu.qcc.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.wu.qcc.model.AccountDAO;

public class CommonFieldSetMapper implements FieldSetMapper<AccountDAO> {

	@Override
	public AccountDAO mapFieldSet(FieldSet fieldSet) throws BindException {
		String account = fieldSet.readString("account");
		
		AccountDAO accountDao = new AccountDAO(account, "");
		
		return accountDao;
	}

}
