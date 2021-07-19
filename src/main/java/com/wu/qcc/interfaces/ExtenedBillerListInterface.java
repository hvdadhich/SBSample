package com.wu.qcc.interfaces;

import java.util.List;

import com.wu.qcc.model.AccountDAO;

public interface ExtenedBillerListInterface {
	public List<? extends AccountDAO> newList(List<? extends AccountDAO> items);
}
