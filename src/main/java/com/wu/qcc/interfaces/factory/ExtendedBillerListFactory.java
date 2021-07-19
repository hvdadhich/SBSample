package com.wu.qcc.interfaces.factory;

import java.util.HashMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.wu.qcc.interfaces.ExtenedBillerListInterface;
import com.wu.qcc.writer.ExtendedBillerWriters.CommonBillerExtendedList;
import com.wu.qcc.writer.ExtendedBillerWriters.GTWBillerExtenedList;

import lombok.Data;
@Data
public class ExtendedBillerListFactory implements InitializingBean,DisposableBean   {

	HashMap<String,ExtenedBillerListInterface> billerFactory = new HashMap();
	
	public void addBiller(String prefix, ExtenedBillerListInterface billerList)
	
	{
		getBillerFactory().put(prefix, billerList);
		
	}
	
	public ExtenedBillerListInterface getList(String prefix)
	{
		
		
		if(getBillerFactory().containsKey(prefix))
		{
			return getBillerFactory().get(prefix);
		}
		
		return getBillerFactory().get("COMMON");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		billerFactory.put("GTW", new GTWBillerExtenedList());
		billerFactory.put("COMMON", new CommonBillerExtendedList());
		
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
