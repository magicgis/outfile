package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjSrfxMonthlyMapper;
import com.naswork.model.CompanyEnum;
import com.naswork.model.TjSrfxMonthly;
import com.naswork.service.TjSrfxService;

/**
 * 
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 收入分析
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午1:20:55
 *
 */
@Service
public class TjSrfxServiceImpl implements TjSrfxService {
	@Resource
	TjSrfxMonthlyMapper tjSrfxMonthlyMapper;
	Comparator<TjSrfxMonthly> comByMonth = new Comparator<TjSrfxMonthly>() {
		@Override
		public int compare(TjSrfxMonthly o1, TjSrfxMonthly o2) {
			return o1.getRecordMonth() - o2.getRecordMonth();
		}
	};
	Comparator<TjSrfxMonthly> comByYear = new Comparator<TjSrfxMonthly>() {
		@Override
		public int compare(TjSrfxMonthly o1, TjSrfxMonthly o2) {
			return o1.getRecordYear() - o2.getRecordYear();
		}
	};

	/**
	 * @see com.naswork.service.TjSrfxService#getTotalIncomeMonthly(java.lang.Integer)
	 */
	@Override
	public Map<String, Object> getTotalIncomeMonthly(Integer year) {
		return getOneIncomeMonthly(year, CompanyEnum.LEVEL_TOTAL.val());
	}

	/**
	 * @see com.naswork.service.TjSrfxService#getTotalSumIncomeYearly()
	 */
	@Override
	public Map<String, Object> getTotalSumIncomeYearly() {
		return getOneSumIncomeYearly(CompanyEnum.LEVEL_TOTAL.val());
	}

	/**
	 * @see com.naswork.service.TjSrfxService#getAllSumIncomeYearly()
	 */
	@Override
	public Map<String, Object> getAllSumIncomeYearly() {
		Map<String, Object> res = new HashMap<>();
		int year = Calendar.getInstance().get(Calendar.YEAR);

		//获取数据
		List<TjSrfxMonthly> lxs = tjSrfxMonthlyMapper.getOneSumIncomeYearly(CompanyEnum.LEVEL_LXS.val());
		List<TjSrfxMonthly> jd = tjSrfxMonthlyMapper.getOneSumIncomeYearly(CompanyEnum.LEVEL_JD.val());
		List<TjSrfxMonthly> bgjd = tjSrfxMonthlyMapper.getOneSumIncomeYearly(CompanyEnum.LEVEL_BGJD.val());

		Float[] lxsList = new Float[year - 2010];
		Float[] jdList = new Float[year - 2010];
		Float[] bgjdList = new Float[year - 2010];
		String[] names = new String[year - 2010];
		//x坐标
		
		for (int k=0, i = 2011; i <= year; i++,k++) {
			names[k] = i + "年";
			lxsList[k]=0F;
			jdList[k]=0F;
			bgjdList[k]=0F;
		}

		//按照年份装载数据
		for (TjSrfxMonthly t : lxs) {
			if (t != null) {
				lxsList[year - t.getRecordYear()] = t.getIncome();
			}
		}
		for (TjSrfxMonthly t : jd) {
			if (t != null) {
				jdList[year - t.getRecordYear()] = t.getIncome();
			}
		}
		for (TjSrfxMonthly t : bgjd) {
			if (t != null) {
				bgjdList[year - t.getRecordYear()] = t.getIncome();
			}
		}

		List<Float[]> valueList = new ArrayList<>();
		valueList.add(lxsList);
		valueList.add(jdList);
		valueList.add(bgjdList);
		
		res.put("x", names);
		res.put("y", valueList);
		return res;
	}


	/**
	 * @see com.naswork.service.TjSrfxService#getOneIncomeMonthly(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public Map<String, Object> getOneIncomeMonthly(Integer year, Integer level) {
		Map<String, Integer> paramMap = new HashMap<>();
		Map<String, Object> res = new HashMap<>();
		paramMap.put("year", year);
		paramMap.put("level", level);
		List<TjSrfxMonthly> incomeMonthly = tjSrfxMonthlyMapper.getOneIncomeMonthly(paramMap);
		if (incomeMonthly != null) {
			int size = incomeMonthly.size();
			// 按月排序
			Collections.sort(incomeMonthly, comByMonth);
			// 封装数据
			String[] names = new String[size];
			Float[] values = new Float[size];
			for (int i = 0; i < size; i++) {
				TjSrfxMonthly tjSrfxMonthly = incomeMonthly.get(i);
				names[i] = (i + 1) + "月";
				if (tjSrfxMonthly != null) {
					values[i] = tjSrfxMonthly.getIncome();
				} else {
					values[i] = 0F;
				}
			}
			List<Float[]> list = new ArrayList<>();
			list.add(values);
			res.put("x", names);
			res.put("y", list);

		}
		return res;
	}

	/**
	 * @see com.naswork.service.TjSrfxService#getOneSumIncomeYearly(java.lang.Integer)
	 */
	@Override
	public Map<String, Object> getOneSumIncomeYearly(Integer level) {
		Map<String, Object> res = new HashMap<>();
 
		List<TjSrfxMonthly> oneIncomeYearly = tjSrfxMonthlyMapper.getOneSumIncomeYearly(level);
		if (oneIncomeYearly != null) {
			int size = oneIncomeYearly.size();
			// 按年排序
			Collections.sort(oneIncomeYearly, comByYear);
			// 封装数据
			String[] names = new String[size];
			Float[] values = new Float[size];
			for (int i = 0; i < size; i++) {
				TjSrfxMonthly tjSrfxMonthly = oneIncomeYearly.get(i);
				if (tjSrfxMonthly != null) {
					names[i] = tjSrfxMonthly.getRecordYear() + "年";
					values[i] = tjSrfxMonthly.getIncome();
				}
			} 
			List<Float[]> list = new ArrayList<>();
			list.add(values);
			res.put("x", names);
			res.put("y", list);
		}
		return res;
	}

}
