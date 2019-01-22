package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.Tjqs3ajqsrfxV2Dao;
import com.naswork.model.Tjqs3ajqsrfxV2;
import com.naswork.service.Tjqs3ajqsrfxV2Service;

@Service("tjqs3ajqsrfxV2Service")
public class Tjqs3ajqsrfxV2ServiceImpl implements Tjqs3ajqsrfxV2Service {
	@Resource
	private Tjqs3ajqsrfxV2Dao tjqs3ajqsrfxV2Dao;

	@Override
	public List<Tjqs3ajqsrfxV2> getQs3ajqsrfxV2(String year) {
		return tjqs3ajqsrfxV2Dao.getQs3ajqsrfxV2(year);
	}

}
