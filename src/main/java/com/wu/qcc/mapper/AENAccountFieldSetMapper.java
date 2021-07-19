package com.wu.qcc.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.wu.qcc.model.AccountDAO;

public class AENAccountFieldSetMapper implements FieldSetMapper<AccountDAO> {

	@Override
	public AccountDAO mapFieldSet(FieldSet fieldSet) throws BindException {
		
		String account = fieldSet.readString("account").trim();
		String jurisdiction = fieldSet.readString("jurisdiction").trim();
		
		if(jurisdiction.equals("IPL")) {
			jurisdiction="XCSCNORTH IA";

		} else if(jurisdiction.equals("WPL")) {
			jurisdiction="XWPLPAYMENT IA";
		}
		else {
			jurisdiction=" ";
		}
		
		String dnac_flag="N";

		DateFormat df = new SimpleDateFormat("yyyyMMdd000000");
		Calendar calObj = Calendar.getInstance();
		String update_date=df.format(calObj.getTime()).toString();
		String jSon = String.format("{\"account\":\"%s\",\"jurisdiction\":\"%s\",\"dnac_flag\":\"%s\",\"update_date\":\"%s\"}",  account, jurisdiction, dnac_flag,update_date );

		AccountDAO accountDao = new AccountDAO(account, jSon);
		
		return accountDao;
		
		
	}

}
