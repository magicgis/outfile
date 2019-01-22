package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjnewjdrcDailyDao;
import com.naswork.model.TjnewjdrcDaily;
import com.naswork.service.TjnewjdrcDailyService;

@Service("tjnewjdrcDailyService")
public class TjnewjdrcDailyServiceImpl implements TjnewjdrcDailyService {
	@Resource
	private TjnewjdrcDailyDao tjnewjdrcDailyDao;
	@Override
	public List<TjnewjdrcDaily> getmrijdrcDaily(Integer year, Integer month, Integer id) {
		return tjnewjdrcDailyDao.getmrijdrcDaily(year, month, id);
	}
	@Override
	public List<TjnewjdrcDaily> getQs3amrijdrcDaily(Integer year, Integer month, Integer level) {
		return tjnewjdrcDailyDao.getQs3amrijdrcDaily(year, month, level);
	}

}
