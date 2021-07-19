package com.wu.qcc.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

import com.wu.qcc.model.AccountDAO;
import com.wu.qcc.model.BranchRevisionDAO;

public class GTWAccountFieldSetMapper implements FieldSetMapper<AccountDAO> {

	
	@Override
	public AccountDAO mapFieldSet(FieldSet fieldSet) throws BindException {
		
		
		String new_account=fieldSet.readString("new_account");
		
		String old_account=fieldSet.readString("old_account");
		
		String name=fieldSet.readString("name");
		
		AccountDAO account= new AccountDAO();
		
		
		account.getCustomData().put("old_account", old_account);
		account.getCustomData().put("new_account", new_account);
		account.getCustomData().put("name", name);
		
		
		return account;
	}

}
