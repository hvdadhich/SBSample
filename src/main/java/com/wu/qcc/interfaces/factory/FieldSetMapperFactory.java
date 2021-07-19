package com.wu.qcc.interfaces.factory;

import java.util.HashMap;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.wu.qcc.mapper.AENAccountFieldSetMapper;
import com.wu.qcc.mapper.AZDAccountFieldSetMapper;
import com.wu.qcc.mapper.CommonFieldSetMapper;
import com.wu.qcc.mapper.GTWAccountFieldSetMapper;
import com.wu.qcc.model.AccountDAO;

import lombok.Data;

@Data
public class FieldSetMapperFactory implements InitializingBean,DisposableBean {

	HashMap<String,FieldSetMapper<AccountDAO>> fieldSetFactory = new HashMap<>();
	
	
public void addBiller(String prefix, FieldSetMapper<AccountDAO> billerList)
	
	{
		getFieldSetFactory().put(prefix, billerList);
		
	}
	
	public  FieldSetMapper<AccountDAO> getMapper(String prefix)
	{
		
		
		if(getFieldSetFactory().containsKey(prefix))
		{
			return getFieldSetFactory().get(prefix);
		}
		
		return getFieldSetFactory().get("COMMON");
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		getFieldSetFactory().put("GTW",  new GTWAccountFieldSetMapper());
		getFieldSetFactory().put("AZD", new AZDAccountFieldSetMapper());
		getFieldSetFactory().put("AEN", new AENAccountFieldSetMapper());
		getFieldSetFactory().put("COMMON", new CommonFieldSetMapper());
		
		
		
	}
	
}
