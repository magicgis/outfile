package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjnewstationRealtimeDao;
import com.naswork.model.TjnewstationRealtime;
import com.naswork.service.TjnewstationRealtimeService;
@Service("tjnewstationRealtimeService")
public class TjnewstationRealtimeServiceImpl implements TjnewstationRealtimeService {
	@Resource
	private TjnewstationRealtimeDao tjnewstationRealtimeDao;
	@Override
	public List<TjnewstationRealtime> getStationCountRealtime() {
		return tjnewstationRealtimeDao.getStationCountRealtime();
	}

}
