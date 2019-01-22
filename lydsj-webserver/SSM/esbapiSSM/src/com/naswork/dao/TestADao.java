package com.naswork.dao;

import java.util.List;

import com.naswork.model.TestA;

public interface TestADao {
    int deleteByPrimaryKey(Integer id);

    int insert(TestA record);

    int insertSelective(TestA record);

    TestA selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestA record);

    int updateByPrimaryKey(TestA record);

	List<TestA> selectAll();
}