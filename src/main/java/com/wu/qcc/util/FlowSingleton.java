package com.wu.qcc.util;

import java.util.HashMap;

import com.wu.qcc.model.CustomFlowVaribalePojo;

public class FlowSingleton {

	private static FlowSingleton Instance;
	private HashMap<String,CustomFlowVaribalePojo> singletonMap = new HashMap<String, CustomFlowVaribalePojo>();
	
	private FlowSingleton()
	{
			
	}
	
	public static FlowSingleton getInstance()
	{
		if(Instance==null)
		{
			if(Instance==null)
			{
				Instance= new FlowSingleton();
			}
		}
		return Instance;
	}
	
	
	public void putMap(String key, CustomFlowVaribalePojo value)
	{
		singletonMap.put(key, value);
	}
	
	
	public CustomFlowVaribalePojo getMap (String key)
	{
		return singletonMap.get(key);
	}
	
	public void clearMap(String key)
	{
   singletonMap.remove(key);	
	}

}