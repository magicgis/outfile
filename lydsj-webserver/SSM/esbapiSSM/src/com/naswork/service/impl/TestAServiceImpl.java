package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TestADao;
import com.naswork.model.TestA;
import com.naswork.service.TestAService;

@Service("testAService")
public class TestAServiceImpl implements TestAService {
	@Resource
	private TestADao testADao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return testADao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(TestA record) {
		return testADao.insert(record);
	}

	@Override
	public int insertSelective(TestA record) {
		return testADao.insertSelective(record);
	}

	@Override
	public TestA selectByPrimaryKey(Integer id) {
		return testADao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(TestA record) {
		return testADao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TestA record) {
		return testADao.updateByPrimaryKey(record);
	}

	@Override
	public List<TestA> selectAll() {
		return testADao.selectAll();
	}

}
