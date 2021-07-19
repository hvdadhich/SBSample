package com.wu.qcc.interfaces.factory;

import java.util.HashMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.wu.qcc.Exception.BillerException;
import com.wu.qcc.LoadTypes.FullLoad;
import com.wu.qcc.interfaces.LoadTypeInterface;

import lombok.Data;

@Data
public class LoadTypeFactory implements InitializingBean,DisposableBean {

	HashMap<String,LoadTypeInterface> billerLoadFactory = new HashMap<>(); 
	
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		getBillerLoadFactory().put("GTW", new FullLoad());
		getBillerLoadFactory().put("AZD", new FullLoad());
		getBillerLoadFactory().put("AEN", new FullLoad());
		
	}
	
	
	boolean isFullLoad(String billerPrefix,String filePath) throws BillerException
	{
		
		return getType(billerPrefix).isFullLoad(filePath);
		
	}
	
	public void addBillerLoad(String prefix,LoadTypeInterface type)
	{
		
	billerLoadFactory.put(prefix, type);
	}
	

	public LoadTypeInterface getType(String prefix) throws BillerException
	{
		
		if(billerLoadFactory.containsKey(prefix))
			return billerLoadFactory.get(prefix);
		else
			throw new BillerException("No Type Defined in Load");
		
		
		
	}
	
	
}
