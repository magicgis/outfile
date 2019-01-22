package com.naswork.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjnewjdrcRealtimeDao;
import com.naswork.model.TjnewjdrcRealtime;
import com.naswork.service.TjnewjdrcRealtimeService;

@Service("tjnewjdrcRealtimeService")
public class TjnewjdrcRealtimeServiceImpl implements TjnewjdrcRealtimeService {
	@Resource
	private TjnewjdrcRealtimeDao tjnewjdrcRealtimeDao;
	@Override
	public  List<TjnewjdrcRealtime> getNewjdrcRealTime(String curDate,String morning,String night,Integer id) {
		return tjnewjdrcRealtimeDao.getNewjdrcRealTime(curDate, morning,night,id);
	}

	@Override
	public Integer getAllCount(Double num, String startTime,String endTime) {
		return tjnewjdrcRealtimeDao.getAllCount(num,startTime,endTime);
	}

	@Override
	public Integer getAllCountByParentId(Integer id,Double num, String startTime,String endTime) {
		return tjnewjdrcRealtimeDao.getAllCountByParentId(id,num,startTime,endTime);
	}

	@Override
	public Integer getCountById(Integer id, String startTime,String endTime) {
		return tjnewjdrcRealtimeDao.getCountById(id,startTime,endTime);
	}

	@Override
	public List<TjnewjdrcRealtime> getjqjdrcsspm(Integer typeId, Integer id, String startTime,String endTime) {
		return tjnewjdrcRealtimeDao.getjqjdrcsspm(typeId,id,startTime,endTime);
	}

}
