package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.Tj3ajqtsblfxV2MonthlyDao;
import com.naswork.model.Tj3ajqtsblfxV2Monthly;
import com.naswork.service.Tj3ajqtsblfxV2MonthlyService;

@Service("tj3ajqtsblfxV2MonthlyService")
public class Tj3ajqtsblfxV2MonthlyServiceImpl implements Tj3ajqtsblfxV2MonthlyService {
	
	@Resource
	private Tj3ajqtsblfxV2MonthlyDao tj3ajqtsblfxV2MonthlyDao;
	@Override
	public List<Tj3ajqtsblfxV2Monthly> getJqtsblfx3a(String year) {
		return tj3ajqtsblfxV2MonthlyDao.getJqtsblfx3a(year);
	}

}
