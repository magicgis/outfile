package com.naswork.module.tj;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.BiOrgInfo;
import com.naswork.model.BigScreen;
import com.naswork.model.InfoData;
import com.naswork.model.SubArea;
import com.naswork.model.TjJdrsRealtimeCur;
import com.naswork.service.BiOrgInfoService;
import com.naswork.service.TjJdrsMonthlyService;
import com.naswork.service.TjJdrsRealtimeCurService;
import com.naswork.service.TjLmlyrsDailyService;
import com.naswork.service.TjLmlyrsMonthlyService;

/*大屏数据*/
@RestController
@RequestMapping("/v1/mapinfo")
public class BigScreenController extends BaseController {
	@Resource
	private BiOrgInfoService biOrgInfoService;
	@Resource
	private TjLmlyrsMonthlyService tjLmlyrsMonthlyService;
	@Resource
	private TjLmlyrsDailyService tjLmlyrsDailyService;
	@Resource
	private TjJdrsMonthlyService tjJdrsMonthlyService;
	@Resource
	private TjJdrsRealtimeCurService tjJdrsRealtimeCurService;
	
	@RequestMapping("/{id}")
	public BigScreen getBigScreen(@PathVariable Integer id){
		BigScreen vo = new BigScreen();//id
		String idStr = String.valueOf(id);
		vo.setId(idStr);
		BiOrgInfo bi = biOrgInfoService.selectByPrimaryKey(id);
		if(bi!=null){
			vo.setLevel(bi.getLevel());//level
			vo.setAreaName(bi.getName());//areaName
			BigDecimal bdx = bi.getRealposx();
			BigDecimal bdy = bi.getRealposy();
			List<BigDecimal> realPos = new ArrayList<>();
			realPos.add(bdx);
			realPos.add(bdy);
			vo.setRealPos(realPos);//realPos
			vo.setZoom(bi.getZoom());//zoom
		}
		// info data part
		List<InfoData> infoData = new ArrayList<>();
		//zww统计本年进入梅州总人数，id6作为本年累计入梅人数对象
		Date curDate = new Date();
		String[] strs = MyTools.getDateArr(curDate);
		int year=Integer.parseInt(strs[0]);
		Integer totalLmNum=tjLmlyrsMonthlyService.getTotalLmNum(id, year);
		BigDecimal bd = new BigDecimal("-1");
		//本月进入梅州人数

		String identifier = strs[0]+strs[1]+"00000000";
		InfoData id1 = new InfoData();
		id1.setName("本月/本年进入梅州人数");
		Integer num = tjLmlyrsMonthlyService.CurMonthNum(id, identifier);
		if (num==null) {
			num=0;
		}
		id1.setData(num+"/"+totalLmNum);
		id1.setAlarm(-1);
		
		id1.setTrend(bd);
		infoData.add(id1);
		//zww本年累计旅游接待人次，id5作为本年累计接待人数对象
		Integer totalJdNum=tjJdrsMonthlyService.getTotalJdNum(id, year);
		//本月旅游接待人次

		identifier = strs[0]+strs[1]+"00000000";
		InfoData id3 = new InfoData();
		id3.setName("本月/本年旅游接待人次");
		num = tjJdrsMonthlyService.getMonthNum(id, identifier);
		if(num == null) num = 0;
		id3.setData(num+"/"+totalJdNum);
		bd = new BigDecimal("-1");
		id3.setTrend(bd);
		/*
		 * Integer maxCap = bi.getCapacity();
		double ratio = 1.0*num/maxCap;
		int alarmNum = -1;
		if(ratio >= 1 && ratio <= 1.2) alarmNum = 1;
		else if(ratio > 1.2) alarmNum = 2;
		id3.setAlarm(alarmNum);
		*/
		id3.setAlarm(-1);//TODO 注释掉原有预警逻辑
		infoData.add(id3);
		
		//昨日进入梅州人数
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -2);
		strs = MyTools.getDateArr(cd.getTime());
		identifier = strs[0]+strs[1]+strs[2]+"000000";
		InfoData id2 = new InfoData();
		id2.setName("昨日进入梅州游客");
		//实际上SQL中没用到identifier
		num = tjLmlyrsDailyService.LastDayNum(id, identifier);
		if(num == null) num = 0;
		id2.setData(num+"");
		id2.setAlarm(-1);
		//实际上SQL中没用到identifier
		bd = tjLmlyrsDailyService.lastDayTrend(id, identifier);
		if(bd == null) bd = new BigDecimal("0.00");
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		id2.setTrend(bd);
		infoData.add(id2);
		//实时接待人数
		InfoData id4 = new InfoData();
		id4.setName("实时接待人数");
		TjJdrsRealtimeCur jdrsCur = tjJdrsRealtimeCurService.selectCur(id);
		num = jdrsCur.getSubscribercount();
		Float trend = jdrsCur.getTrend();
		if(trend == null) trend = 0.00f;
		id4.setData(num+"");
		bd = new BigDecimal(trend);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		id4.setTrend(bd);
		
		/*
		ratio = 1.0*num/maxCap;
		if(ratio >= 1 && ratio <= 1.2) alarmNum = 1;
		else if(ratio > 1.2) alarmNum = 2;
		id4.setAlarm(alarmNum);
		*/
		id4.setAlarm(-1);//TODO 注释掉原有预警逻辑
		infoData.add(id4);
		// sub area
		List<Integer> subIds = biOrgInfoService.selectByParentId(id);
		List<SubArea> subList = new ArrayList<>();
		for(Integer tmpid: subIds){
			SubArea tmps = new SubArea();
			BiOrgInfo tmpb = biOrgInfoService.selectByPrimaryKey(tmpid);
			tmps.setId(tmpid); //id
			List<BigDecimal> tmpPos = new ArrayList<>();
			tmpPos.add(tmpb.getDisposx());
			tmpPos.add(tmpb.getDisposy());
			tmps.setDisPos(tmpPos); //disPos
			tmps.setIndexCode(tmpb.getIndexcode());//index code
			tmps.setName(tmpb.getName());//name
			TjJdrsRealtimeCur tmpC = tjJdrsRealtimeCurService.selectCur(tmpid);
			if(tmpC == null){
				tmps.setNum(0);
				tmps.setAlarm(-1);
			}
			else{
				Integer tmpNum = tmpC.getSubscribercount(); 
				tmps.setNum(tmpNum);
				Integer maxNum = tmpb.getCapacity();
				double tmpr = 1.0*tmpNum/maxNum;
				int tmpa = -1;
				if(tmpr>=1 && tmpr<=1.2) tmpa = 1;
				else if(tmpr > 1.2) tmpa = 2;
				tmps.setAlarm(tmpa);
			}
			subList.add(tmps);
		}
		vo.setInfoData(infoData);
		vo.setSubArea(subList);
		return vo;
	}
		
		
}
