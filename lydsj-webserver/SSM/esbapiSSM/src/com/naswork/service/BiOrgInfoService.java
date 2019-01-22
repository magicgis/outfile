package com.naswork.service;

import java.util.List;

import com.naswork.model.BiOrgInfo;

public interface BiOrgInfoService {
	int deleteByPrimaryKey(Integer id);

	int insert(BiOrgInfo record);

	int insertSelective(BiOrgInfo record);

	BiOrgInfo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(BiOrgInfo record);

	int updateByPrimaryKey(BiOrgInfo record);

	List<Integer> selectByParentId(Integer id);

	List<BiOrgInfo> getBiOrgInfoByParentId(Integer id);

	List<BiOrgInfo> getBiOrgInfoByLevel(Integer level);

}
