package com.naswork.module.tjv2;

import com.naswork.model.TjnewhslvDaily;
import com.naswork.model.TjnewhslvMonthly;
import com.naswork.service.TjnewhslvMonthlyService;
import com.naswork.vo.RankVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: white
 * @Date: create in 2018-08-08 19:17
 * @Description: 特色旅游
 * @Modify_By:
 */
@RestController
@RequestMapping("/v2")
public class TjnewhslvMonthlyController {

    @Resource
    private TjnewhslvMonthlyService tjNewHslvMonthlyService;

    /**
     * -----------------------------------红色旅游部分----------------------------
     * -------------------------------------------------------------------------
     */
    /**
     * @Author: Create by white
     * @Description: 景区每月接待人次
     * @Date: 2018-08-09 9:57
     * @Params: [year_id, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping("/hsjqmyjdrc/{year_id}/{id_type}/{id}")
    public HashMap<String, Object> hsjqmyjdrc(@PathVariable Integer year_id, @PathVariable Integer id_type, @PathVariable Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        List<TjnewhslvMonthly> queryList = tjNewHslvMonthlyService.getSubscriberCountOfMonths(year_id, id_type, id);
        List<String> monthStrList = new ArrayList<>();
        List<List<Integer>> listList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        for (TjnewhslvMonthly tjnewhslvMonthly : queryList) {
            monthStrList.add(tjnewhslvMonthly.getRecordmonth() + "月");
            countList.add(tjnewhslvMonthly.getSumofmonth());
        }
        listList.add(countList);
        map.put("x", monthStrList);
        map.put("y", listList);
        return map;
    }

    /**
     * @Author: Create by white
     * @Description: 景区每年接待人次
     * @Date: 2018-08-09 14:00
     * @Params: [typeId, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/hsjqmnjdrc/{id_type}/{id}")
    public HashMap<String, Object> hsjqmnjdrc(@PathVariable("id_type") Integer typeId,
                                              @PathVariable("id") Integer id) {

        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> yearStrList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewhslvMonthly> queryList = tjNewHslvMonthlyService.gethsjqmnjdrc(typeId, id);
        for (TjnewhslvMonthly tjnewhslvMonthly : queryList) {
            yearStrList.add(tjnewhslvMonthly.getRecordyear() + "年");
            valueList.add(tjnewhslvMonthly.getSumofyear());
        }
        resultList.add(valueList);
        resultMap.put("y", resultList);
        resultMap.put("x", yearStrList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 各县区景区月接待人次
     * @Date: 2018-08-09 16:06
     * @Params: [year_id, month_id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/hsgxqjqyjdrc/{year_id}/{month_id}")
    public HashMap<String, Object> hsgxqjqyjdrc(@PathVariable("year_id") Integer year_id,
                                                @PathVariable("month_id") Integer month_id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> countyStrList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewhslvMonthly> queryList = tjNewHslvMonthlyService.gethsgxqjqyjdrc(year_id, month_id);
        for (TjnewhslvMonthly tjnewhslvMonthly : queryList) {
            switch (tjnewhslvMonthly.getId()) {
                case 1001:
                    countyStrList.add("梅县区");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                case 1002:
                    countyStrList.add("梅江区");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                case 1003:
                    countyStrList.add("平远县");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                case 1004:
                    countyStrList.add("丰顺县");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                case 1005:
                    countyStrList.add("兴宁市");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                case 1006:
                    countyStrList.add("大埔县");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                case 1007:
                    countyStrList.add("五华县");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                case 1008:
                    countyStrList.add("蕉岭县");
                    valueList.add(tjnewhslvMonthly.getSumofcountymonth());
                    break;
                default:
                    break;
            }
        }
        resultList.add(valueList);
        resultMap.put("x", countyStrList);
        resultMap.put("y", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 全市景区接待人次月排名
     * @Date: 2018-08-10 10:31
     * @Params: [year_id, month_id, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/hsjqjdrcypm/{year_id}/{month_id}/{id_type}/{id}")
    public HashMap<String, Object> hsjqjdrcypm(@PathVariable("year_id") Integer year_id,
                                               @PathVariable("month_id") Integer month_id,
                                               @PathVariable("id_type") Integer id_type,
                                               @PathVariable("id") Integer id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> titleList = new ArrayList<>();
        titleList.add("排名");
        titleList.add("景点");
        titleList.add("人次");
        List<List<Object>> resultList = new ArrayList<>();
        List<TjnewhslvMonthly> queryList = tjNewHslvMonthlyService.gethsjqjdrcypm(year_id, month_id, id_type, id);
        for (int i = 0;i<queryList.size();i++){
            List<Object> valueList = new ArrayList<>();
            valueList.add(i+1);
            valueList.add(queryList.get(i).getSpotName());
            valueList.add(queryList.get(i).getSubscribercount());
            resultList.add(valueList);
        }
        resultMap.put("title", titleList);
        resultMap.put("data", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 全市景区接待人次年度排名
     * @Date: 2018-08-10 10:49
     * @Params: [year_id, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "hsjqjdrcndpm/{year_id}/{id_type}/{id}")
    public HashMap<String, Object> hsjqjdrcndpm(@PathVariable("year_id") Integer year_id,
                                                @PathVariable("id_type") Integer id_type,
                                                @PathVariable("id") Integer id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> titleStrList = new ArrayList<>();
        List<List<Object>> resultList = new ArrayList<>();
        List<TjnewhslvMonthly> queryList = tjNewHslvMonthlyService.gethsjqjdrcndpm(year_id, id_type, id);
        titleStrList.add("排名");
        titleStrList.add("景点");
        titleStrList.add("人次");
        for (int i = 0; i < queryList.size(); i++) {
            List<Object> valueList = new ArrayList<>();
            valueList.add(i+1);
            valueList.add(queryList.get(i).getSpotName());
            valueList.add(queryList.get(i).getSumofmonth());
            resultList.add(valueList);
        }
        resultMap.put("title", titleStrList);
        resultMap.put("data", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description:   景区月接待人次同比分析(景区页面)
     * @Date: 2018-08-10 17:14
     * @Params: [year_id, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "hsjqyjdrctbfx/{year_id}/{id_type}/{id}")
    public HashMap<String,Object> hsjqyjdrctbfx(@PathVariable("year_id") Integer year_id,
                                                @PathVariable("id_type") Integer id_type,
                                                @PathVariable("id") Integer id){
        HashMap<String,Object> resultMap = new HashMap<>();
        List<String> stringList = new ArrayList<>();
        List<List<Float>> resultList = new ArrayList<>();
        List<Float> valueList = new ArrayList<>();
        List<TjnewhslvMonthly> queryList = tjNewHslvMonthlyService.gethsjqyjdrctbfx(year_id,id_type,id);
        for(TjnewhslvMonthly tjnewhslvMonthly : queryList){
            stringList.add(tjnewhslvMonthly.getRecordmonth()+"月");
            valueList.add(tjnewhslvMonthly.getTrend()==null?0:tjnewhslvMonthly.getTrend());
        }
        resultList.add(valueList);
        resultMap.put("x",stringList);
        resultMap.put("y",resultList);
        return resultMap;
    }

}
