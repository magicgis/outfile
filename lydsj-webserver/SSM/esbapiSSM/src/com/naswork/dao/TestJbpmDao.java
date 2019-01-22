package com.naswork.dao;

import java.util.List;

import com.naswork.model.Ask4leave;
import com.naswork.model.TestJbpm;
import com.naswork.vo.PageModel;

public interface TestJbpmDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TestJbpm record);

    int insertSelective(TestJbpm record);

    TestJbpm selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestJbpm record);

    int updateByPrimaryKey(TestJbpm record);
    
    /**
     * 查询分页
     * **/
    public List<TestJbpm> findPage(PageModel<TestJbpm> page);
}