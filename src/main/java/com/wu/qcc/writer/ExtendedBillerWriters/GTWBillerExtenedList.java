package com.wu.qcc.writer.ExtendedBillerWriters;

import java.util.ArrayList;
import java.util.List;

import com.wu.qcc.interfaces.ExtenedBillerListInterface;
import com.wu.qcc.model.AccountDAO;
import com.wu.qcc.util.StatSingleton;

public class GTWBillerExtenedList implements ExtenedBillerListInterface {

	@Override
	public List<? extends AccountDAO> newList(List<? extends AccountDAO> items) {
		
		ArrayList<AccountDAO>  newlist = new ArrayList();
		
		for(AccountDAO acc:items)
		{
			String new_account = acc.getCustomData().get("new_account");
	         String old_account = acc.getCustomData().get("old_account");
	         String name = acc.getCustomData().get("name");
	         String convertFlag = "N";
	         String json;
	         
	         json = String.format("{\"account_num1\":\"%s\",\"account_num2\":\"%s\",\"name\":\"%s\",\"convertFlag\":\"%s\"}",
	                 new_account, old_account, name, convertFlag);
	         newlist.add(new AccountDAO(new_account, json));

	         convertFlag = "Y";
	         json = String.format("{\"account_num1\":\"%s\",\"account_num2\":\"%s\",\"name\":\"%s\",\"convertFlag\":\"%s\"}",
	                 old_account, new_account, name, convertFlag);
	         newlist.add(new AccountDAO(old_account, json));
	         
	         StatSingleton.getInstance().incrementModifiedReadCount();
	         
			
		}
		return newlist;
	}

}
