package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.JqqdDao;
import com.naswork.model.Jqqd;
import com.naswork.service.JqqdService;

@Service("jqqdService")
public class JqqdServiceImpl implements JqqdService {
	@Resource
	private JqqdDao jqqdDao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return jqqdDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Jqqd record) {
		return jqqdDao.insert(record);
	}

	@Override
	public int insertSelective(Jqqd record) {
		return jqqdDao.insertSelective(record);
	}

	@Override
	public Jqqd selectByPrimaryKey(Integer id) {
		return jqqdDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Jqqd record) {
		return jqqdDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Jqqd record) {
		return jqqdDao.updateByPrimaryKey(record);
	}

	@Override
	public List<Jqqd> selectAll() {
		return jqqdDao.selectAll();
	}

}
