package com.wu.qcc.writer.ExtendedBillerWriters;

import java.util.List;

import com.wu.qcc.interfaces.ExtenedBillerListInterface;
import com.wu.qcc.model.AccountDAO;

public class CommonBillerExtendedList implements ExtenedBillerListInterface {

	@Override
	public List<? extends AccountDAO> newList(List<? extends AccountDAO> items) {
		
		return items;
	}

}
