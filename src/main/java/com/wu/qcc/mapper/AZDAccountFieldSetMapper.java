package com.wu.qcc.mapper;

import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.wu.qcc.model.AccountDAO;

public class AZDAccountFieldSetMapper implements FieldSetMapper<AccountDAO> {

	@Override
	public AccountDAO mapFieldSet(FieldSet fieldSet) throws BindException {
		
		String account = fieldSet.readString("account").trim().replaceAll("[^a-zA-Z0-9]+", "");
		String lastName = fieldSet.readString("lastName").trim();
		
		if(lastName.length()<3)			
		throw new FlatFileParseException("Biller Prefix :- AZD "+ "Culprit Record:-",fieldSet.toString());
		
		String firstName = fieldSet.readString("firstName").trim();
		String json = String.format("{\"lastName\":\"%s\",\"firstName\":\"%s\"}", lastName, firstName);

		AccountDAO accountDao = new AccountDAO(account, json);
		
		return accountDao;
		
		
	}

}
