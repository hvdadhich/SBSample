package com.wu.qcc.util;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.wu.qcc.model.CustomFlowVaribalePojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StatSingleton {
	

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private static StatSingleton Instance;
	@Setter(AccessLevel.NONE)
	private AtomicLong readCountValidateStep= new AtomicLong(0);
	@Setter(AccessLevel.NONE)
	private AtomicLong readCount= new AtomicLong(0);
	@Setter(AccessLevel.NONE)
	private AtomicLong writeCount= new AtomicLong(0);
	@Setter(AccessLevel.NONE)
	private AtomicLong errorReadCount= new AtomicLong(0);
	@Setter(AccessLevel.NONE)
	private AtomicLong errorWriteCount= new AtomicLong(0);
	
	@Setter(AccessLevel.NONE)
	private HashMap<String,Long> stepsDetailsMap = new HashMap<String, Long>();
	
	@Setter(AccessLevel.NONE)
	private AtomicLong modifiedReadCount= new AtomicLong(0);	
	
	
	private StatSingleton()
	{
			
	}
	
	public static StatSingleton getInstance()
	{
		if(Instance==null)
		{
			if(Instance==null)
			{
				Instance= new StatSingleton();
			}
		}
		return Instance;
	}
	
	public void putMap(String key, Long value)
	{
		stepsDetailsMap.put(key, value);
	}
	
	
	public Long getMap (String key)
	{
		return stepsDetailsMap.get(key);
	}
	
	public void clearMap(String key)
	{
		stepsDetailsMap.remove(key);	
	}
	
	
	public void incrementReadCount()
	{
		readCount.incrementAndGet();
	}
	
	public void incrementReadValidateCount()
	{
		readCountValidateStep.incrementAndGet();
	}
	
	public void incrementWriteCount()
	{
		writeCount.incrementAndGet();
	}
	
	public void incrementErrorReadCount()
	{
		errorReadCount.incrementAndGet();
	}
	
	public void incrementErrorWriteCount()
	{
		errorWriteCount.incrementAndGet();
	}
	
	public void incrementModifiedReadCount()
	{
		modifiedReadCount.incrementAndGet();
	}
	
	

}