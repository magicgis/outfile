package com.naswork.service;

import java.util.List;

import com.naswork.model.Dasi;

public interface DasiService {

	public void insertSelective(Dasi record);

	public Dasi selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(Dasi record);
	
	public List<Dasi> getCompleteList();
	
	public void sendDasiEmail();
	
}
