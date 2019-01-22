package com.naswork.module.tjv2;

import com.naswork.common.constants.Constants;
import com.naswork.model.BiOrgInfo;
import com.naswork.model.Holiday;
import com.naswork.model.SubArea;
import com.naswork.model.TjnewjdrcRealtime;
import com.naswork.service.BiOrgInfoService;
import com.naswork.service.HolidayService;
import com.naswork.service.TjnewjdrcRealtimeService;
import com.naswork.utils.DateUtil;
import com.sun.istack.internal.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-11 21:14
 * @Description: 首页 实时指标数据（左边区域显示）
 * @Modify_By:zhangwenwen
 */

@RequestMapping(value = "/v2")
@RestController
@Component
public class TjnewsssjController {
    private static final Logger log = Logger.getLogger(TjnewsssjController.class);

    @Resource
    private BiOrgInfoService biOrgInfoService;

    @Resource
    private TjnewjdrcRealtimeService tjnewjdrcRealtimeService;

    @Resource
    private HolidayService holidayService;

    /**
     * @throws ParseException
     * @Author: Create by white
     * @Description: 实时指标数据（左边区域显示）
     * @Date: 2018-08-12 3:09
     * @Params: [id]
     * @Return: java.util.HashMap<java.lang.String       ,       java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/mapinfo/{id}")
    public HashMap<String, Object> mapinfo(@PathVariable("id") Integer id) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();

        //id
        resultMap.put("id", String.valueOf(id));
        BiOrgInfo biOrgInfo = biOrgInfoService.selectByPrimaryKey(id);
        if (biOrgInfo != null) {
            //level
            resultMap.put("level", biOrgInfo.getLevel());
            //areaName
            resultMap.put("areaName", biOrgInfo.getName());
            List<BigDecimal> realPosList = new ArrayList<>();
            BigDecimal realPosX = biOrgInfo.getRealposx();
            BigDecimal realPosY = biOrgInfo.getRealposy();
            realPosList.add(realPosX);
            realPosList.add(realPosY);
            //realPos
            resultMap.put("realPos", realPosList);
            //zoom
            resultMap.put("zoom", biOrgInfo.getZoom());
        }

        //构造infoData返回的map
        HashMap<String, Object> infoDataMap = new HashMap<>();
        //**************************处理日期开始******************************//
        //判断系统当前时间是否为节假日或者周末，节假日param=9.5,周末param=5.5,平时param=3.15,
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String dateStr = sdf.format(calendar.getTime());
        Date curDate = sdf.parse(dateStr);//当前时间
        Holiday holiday = holidayService.selectCurMonthHoliday();
        String week = getWeekOfDate(curDate);

        String startTime = null;
        String endTime = null;
        if(!DateUtil.compareDate()){
            startTime = Constants.START_TIME;
            endTime = Constants.END_TIME;
        }

        //**************************处理日期结束******************************//
        if (holiday == null) {    //系统当前月没有节假日的情况下周末传5.5，平时传3.15
            if (week.equals("星期六") || week.equals("星期日")) {
                switch (biOrgInfo.getLevel()) {
                    case 1:
                        //全市
                        Integer data1 = tjnewjdrcRealtimeService.getAllCount(5.5,startTime,endTime);;
                        infoDataMap.put("data", data1);
                        break;
                    case 2:
                        //县区
                        Integer data2 = tjnewjdrcRealtimeService.getAllCountByParentId(id, 5.5,startTime,endTime);
                        infoDataMap.put("data", data2);
                        break;
                    //景区
                    case 3:
                        Integer data3 =  tjnewjdrcRealtimeService.getCountById(id,startTime,endTime);
                        infoDataMap.put("data", data3);
                        break;
                    default:
                        break;
                }
            } else {
                switch (biOrgInfo.getLevel()) {
                    case 1:
                        //全市
                        Integer data1 = tjnewjdrcRealtimeService.getAllCount(3.15,startTime,endTime);
                        infoDataMap.put("data", data1);
                        break;
                    case 2:
                        //县区
                        Integer data2 = tjnewjdrcRealtimeService.getAllCountByParentId(id, 3.15,startTime,endTime);
                        infoDataMap.put("data", data2);
                        break;
                    //景区
                    case 3:
                        Integer data3 = tjnewjdrcRealtimeService.getCountById(id,startTime,endTime);
                        infoDataMap.put("data", data3);
                        break;
                    default:
                        break;
                }
            }
        } else {
            //**************************处理日期结束******************************//
            Date holidayStartDate = holiday.getHolidayStartDate();//节假日开始时间
            Date holidayEndDate = holiday.getHolidayEndDate();//节假日结束时间
            int compareStart = curDate.compareTo(holidayStartDate);
            int compareEnd = curDate.compareTo(holidayEndDate);
            //**************************处理日期结束******************************//
            if (compareStart >= 0 && compareEnd <= 0) {    //如果系统当前时间属于节假日
                switch (biOrgInfo.getLevel()) {
                    case 1:
                        //全市
                        Integer data1 = tjnewjdrcRealtimeService.getAllCount(9.5,startTime,endTime);
                        infoDataMap.put("data", data1);
                        break;
                    case 2:
                        //县区
                        Integer data2 = tjnewjdrcRealtimeService.getAllCountByParentId(id, 9.5,startTime,endTime);
                        infoDataMap.put("data", data2);
                        break;
                    //景区
                    case 3:
                        Integer data3 = tjnewjdrcRealtimeService.getCountById(id,startTime,endTime);
                        infoDataMap.put("data", data3);
                        break;
                    default:
                        break;
                }
            } else if (week.equals("星期六") || week.equals("星期日")) {    //如果系统当前时间属于周末
                switch (biOrgInfo.getLevel()) {
                    case 1:
                        //全市
                        Integer data1 = tjnewjdrcRealtimeService.getAllCount(5.5,startTime,endTime);
                        infoDataMap.put("data", data1);
                        break;
                    case 2:
                        //县区
                        Integer data2 = tjnewjdrcRealtimeService.getAllCountByParentId(id, 5.5,startTime,endTime);
                        infoDataMap.put("data", data2);
                        break;
                    //景区
                    case 3:
                        Integer data3 = tjnewjdrcRealtimeService.getCountById(id,startTime,endTime);
                        infoDataMap.put("data", data3);
                        break;
                    default:
                        break;
                }
            } else {//如果系统当前时间属于平时

                switch (biOrgInfo.getLevel()) {
                    case 1:
                        //全市
                        Integer data1 = tjnewjdrcRealtimeService.getAllCount(3.15,startTime,endTime);
                        infoDataMap.put("data", data1);
                        break;
                    case 2:
                        //县区
                        Integer data2 = tjnewjdrcRealtimeService.getAllCountByParentId(id, 3.15,startTime,endTime);
                        infoDataMap.put("data", data2);
                        break;
                    //景区
                    case 3:
                        Integer data3 = tjnewjdrcRealtimeService.getCountById(id,startTime,endTime);
                        infoDataMap.put("data", data3);
                        break;
                    default:
                        break;
                }

            }
        }
        infoDataMap.put("name", "实时旅游接待人数");
        //infoData
        resultMap.put("infoData", infoDataMap);

        //构造subArea返回的List<Map>集合
        List<BiOrgInfo> biOrgInfoList = biOrgInfoService.getBiOrgInfoByParentId(id);
        List<SubArea> subAreas = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        for (BiOrgInfo biOrgInfo1 : biOrgInfoList) {
            SubArea subArea = new SubArea();
            subArea.setId(biOrgInfo1.getId());
            List<BigDecimal> disPosList = new ArrayList<>();
            disPosList.add(biOrgInfo1.getDisposx());
            disPosList.add(biOrgInfo1.getDisposy());
            subArea.setDisPos(disPosList);
            subArea.setIndexCode(biOrgInfo1.getIndexcode());
            subArea.setName(biOrgInfo1.getName());
            subArea.setAlarm(-1);
            Integer data4 = 0;
            switch (biOrgInfo.getLevel()) {
                case 1:
                    //全市
                    if (holiday == null) {        //系统当前月没有节假日的情况下直接传3.15
                        if (week.equals("星期六") || week.equals("星期日")) {
                            data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 5.5,startTime,endTime);
                            if (data4 == null) {
                                log.info("在"+sf.format(new Date())+" mapinfo接口中getAllCountByParentId获取不到数据null");
//                                for (int i = 0; i <= 10; i++) {
//                                    data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 5.5);
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                            }
                        } else {
                            data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 3.15,startTime,endTime);
                            if (data4 == null) {
                                log.info("在"+sf.format(new Date())+" mapinfo接口中getAllCountByParentId获取不到数据null");
//                                for (int i = 0; i <= 10; i++) {
//                                    data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 3.15);
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                            }
                        }
                    } else {
                        Date holidayStartDate = holiday.getHolidayStartDate();//节假日开始时间
                        Date holidayEndDate = holiday.getHolidayEndDate();//节假日结束时间
                        int compareStart = curDate.compareTo(holidayStartDate);
                        int compareEnd = curDate.compareTo(holidayEndDate);

                        if (compareStart >= 0 && compareEnd <= 0) {    //如果系统当前时间属于节假日
                            data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 9.5,startTime,endTime);
                            if (data4 == null) {
                                log.info("在"+sf.format(new Date())+" mapinfo接口中getAllCountByParentId获取不到数据null");
//                                for (int i = 0; i <= 10; i++) {
//                                    data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 9.5);
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                            }
                        } else if (week.equals("星期六") || week.equals("星期日")) {    //如果系统当前时间属于周末
                            data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 5.5,startTime,endTime);
                            if (data4 == null) {
                                log.info("在"+sf.format(new Date())+" mapinfo接口中getAllCountByParentId获取不到数据null");
//                                for (int i = 0; i <= 10; i++) {
//                                    data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 5.5);
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                            }
                        } else {        //如果系统当前时间属于平时
                            data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 3.15,startTime,endTime);
                            if (data4 == null) {
                                log.info("在"+sf.format(new Date())+" mapinfo接口中getAllCountByParentId获取不到数据null");
//                                for (int i = 0; i <= 10; i++) {
//                                    data4 = tjnewjdrcRealtimeService.getAllCountByParentId(biOrgInfo1.getId(), 3.15);
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                            }
                        }
                    }
                    subArea.setNum(data4 == null ? 0 : data4);
                    break;

                //县区
                case 2:
                    Integer   data5 = tjnewjdrcRealtimeService.getCountById(biOrgInfo1.getId(),startTime,endTime);
                    if (data5 == null) {
                        for (int i = 0; i <= 10; i++) {
                            data5 = tjnewjdrcRealtimeService.getCountById(biOrgInfo1.getId(),startTime,endTime);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    subArea.setNum(data5);
                    break;
                //景区
                case 3:
                    subArea.setNum(0);
                    break;
                default:
                    break;
            }
            subAreas.add(subArea);
        }
        //subArea
        resultMap.put("subArea", subAreas);
        //scenes
        if (biOrgInfo.getLevel() == 1) {
            List<HashMap<String, Object>> scenesList = new ArrayList<>();
            Integer level = 3;
            List<BiOrgInfo> biOrgInfoList1 = biOrgInfoService.getBiOrgInfoByLevel(level);
            for (BiOrgInfo biOrgInfo1 : biOrgInfoList1) {
                HashMap<String, Object> scenesMap = new HashMap<>();
                scenesMap.put("id", biOrgInfo1.getId());
                List<BigDecimal> disPosList = new ArrayList<>();
                disPosList.add(biOrgInfo1.getDisposx());
                disPosList.add(biOrgInfo1.getDisposy());
                scenesMap.put("disPos", disPosList);
                scenesMap.put("name", biOrgInfo1.getName());
                scenesList.add(scenesMap);
            }
            resultMap.put("scenes", scenesList);
        }
//        log.info(resultMap.toString());
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 热门景点实时监测(TOP - N)
     * @Date: 2018-08-13 9:16
     * @Params: [typeId, id]
     * @Return: java.util.List<java.util.HashMap   <   java.lang.String   ,   java.lang.Object>>
     * @Throws:
     */
    @RequestMapping(value = "/jqjdrcsspm/{id_type}/{id}")
    public List<HashMap<String, Object>> jqjdrcsspm(@PathVariable("id_type") Integer typeId,
                                                    @PathVariable("id") Integer id) throws Exception{
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        List<TjnewjdrcRealtime> queryList = new ArrayList<>();
        String startTime = null;
        String endTime = null;
        if(!DateUtil.compareDate()){
            startTime =  Constants.START_TIME;
            endTime =Constants.END_TIME;
        }
        queryList = tjnewjdrcRealtimeService.getjqjdrcsspm(typeId, id,startTime,endTime);
        for (TjnewjdrcRealtime tjnewjdrcRealtime : queryList) {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", tjnewjdrcRealtime.getId());
            resultMap.put("name", tjnewjdrcRealtime.getName());
            resultMap.put("num", tjnewjdrcRealtime.getSubscribercount());
            resultMap.put("level", tjnewjdrcRealtime.getLevel());
            resultList.add(resultMap);
        }
        return resultList;
    }

    @RequestMapping(value = "/ssjdrc/{id_type}/{id}")
    public HashMap<String, Object> ssjdrc(@PathVariable("id_type") Integer type_id,
                                          @PathVariable("id") Integer id) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        //**************************处理日期开始******************************//
        //判断系统当前时间是否为节假日或者周末，节假日param=9.5,周末param=5.5,平时param=3.15,
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String dateStr = sdf.format(calendar.getTime());
        Date curDate = sdf.parse(dateStr);//当前时间
        Holiday holiday = holidayService.selectCurMonthHoliday();
        String week = getWeekOfDate(curDate);

        String startTime = null;
        String endTime = null;
        if(!DateUtil.compareDate()){
            startTime = Constants.START_TIME;
            endTime = Constants.END_TIME;
        }
        //**************************处理日期结束******************************//
        Integer ssjdrc = 0;
        if (holiday == null) {        //系统当前月没有节假日的情况下周末传5.5，平时直接传3.15
            if (week.equals("星期六") || week.equals("星期日")) {
                ssjdrc = tjnewjdrcRealtimeService.getAllCount(5.5,startTime,endTime);
            } else {
                ssjdrc = tjnewjdrcRealtimeService.getAllCount(3.15,startTime,endTime);
            }

        } else {
            //**************************处理日期开始******************************//
            Date holidayStartDate = holiday.getHolidayStartDate();//节假日开始时间
            Date holidayEndDate = holiday.getHolidayEndDate();//节假日结束时间
            int compareStart = curDate.compareTo(holidayStartDate);
            int compareEnd = curDate.compareTo(holidayEndDate);
            //**************************处理日期结束******************************//
            if (compareStart >= 0 && compareEnd <= 0) {
                //如果系统当前时间属于节假日
                ssjdrc = tjnewjdrcRealtimeService.getAllCount(9.5,startTime,endTime);
            } else if (week.equals("星期六") || week.equals("星期日")) {//如果系统当前时间属于周末
                ssjdrc = tjnewjdrcRealtimeService.getAllCount(5.5,startTime,endTime);
            } else {//如果系统当前时间属于平时
                ssjdrc = tjnewjdrcRealtimeService.getAllCount(3.15,startTime,endTime);
            }
        }
        resultMap.put("data", ssjdrc);
//        log.info(resultMap.toString());
        return resultMap;
    }


    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }


}
