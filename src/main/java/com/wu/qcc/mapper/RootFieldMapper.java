package com.wu.qcc.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

import com.wu.qcc.interfaces.factory.FieldSetMapperFactory;
import com.wu.qcc.model.AccountDAO;

import lombok.Data;
@Data
public class RootFieldMapper implements FieldSetMapper<AccountDAO> {
	
	@Autowired
	FieldSetMapperFactory fieldSetFactory;
	
	private String billerPrefix;

	@Override
	public AccountDAO mapFieldSet(FieldSet fieldSet) throws BindException {
		
		FieldSetMapper<AccountDAO> mapper = fieldSetFactory.getMapper(billerPrefix);		
	
		AccountDAO accoutdao=mapper.mapFieldSet(fieldSet);
		
		return accoutdao;
		
	}

}
