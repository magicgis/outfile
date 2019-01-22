package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.BiOrgInfo;

public interface BiOrgInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BiOrgInfo record);

    int insertSelective(BiOrgInfo record);

    BiOrgInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiOrgInfo record);

    int updateByPrimaryKey(BiOrgInfo record);

	List<Integer> selectByParentId(@Param("id") Integer id);

	List<BiOrgInfo> getBiOrgInfoByParentId(@Param("parentId") Integer id);

	List<BiOrgInfo> getBiOrgInfoByLevel(@Param("level") Integer level);
}