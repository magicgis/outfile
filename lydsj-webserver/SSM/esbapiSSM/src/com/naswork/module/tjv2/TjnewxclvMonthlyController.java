package com.naswork.module.tjv2;

import com.naswork.model.TjnewjdrzMonthly;
import com.naswork.model.TjnewxclvMonthly;
import com.naswork.service.TjnewxclvMonthlyService;
import com.naswork.vo.RankVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 16:03
 * @Description: 特色旅游--乡村部分
 * @Modify_By:
 */
@RequestMapping(value = "/v2")
@RestController
public class TjnewxclvMonthlyController {

    @Resource
    private TjnewxclvMonthlyService tjnewxclvMonthlyService;

    /**
     * @Author: Create by white
     * @Description: 全市景区每月接待人次
     * @Date: 2018-08-10 15:53
     * @Params: [year_id, type_id, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/xcjqmyjdrc/{year_id}/{id_type}/{id}")
    public HashMap<String,Object> xcjqmyjdrc(@PathVariable("year_id") Integer year_id,
                                             @PathVariable("id_type") Integer type_id,
                                             @PathVariable("id") Integer id){
        HashMap<String,Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewxclvMonthly> queryList = tjnewxclvMonthlyService.getxcjqmyjdrc(year_id,type_id,id);
        for(TjnewxclvMonthly tjnewxclvMonthly:queryList){
            keyList.add(tjnewxclvMonthly.getRecordmonth()+"月");
            valueList.add(tjnewxclvMonthly.getSubscribercount());
        }
        resultList.add(valueList);
        resultMap.put("x",keyList);
        resultMap.put("y",resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description:  景区每年接待人次(全市，县区，景区)
     * @Date: 2018-08-10 16:59
     * @Params: [type_id, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/xcjqmnjdrc/{id_type}/{id}")
    public HashMap<String,Object> xcjqmnjdrc(@PathVariable("id_type") Integer type_id,
                                             @PathVariable("id") Integer id){

        HashMap<String,Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewxclvMonthly> queryList = tjnewxclvMonthlyService.getxcjqmnjdrc(type_id,id);
        for(TjnewxclvMonthly tjnewxclvMonthly : queryList){
            keyList.add(tjnewxclvMonthly.getRecordyear()+"年");
            valueList.add(tjnewxclvMonthly.getSubscribercount());
        }
        resultList.add(valueList);
        resultMap.put("x",keyList);
        resultMap.put("y",resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 各县区景区月接待人次(全市页面)
     * @Date: 2018-08-10 17:26
     * @Params: [year_id, month_id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/xcgxqjqyjdrc/{year_id}/{month_id}")
    public HashMap<String,Object> xcgxqjqyjdrc(@PathVariable("year_id") Integer year_id,
                                               @PathVariable("month_id") Integer month_id){
        HashMap<String,Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewxclvMonthly> queryList = tjnewxclvMonthlyService.getxcgxqjqyjdrc(year_id,month_id);
        for(TjnewxclvMonthly tjnewxclvMonthly : queryList){
            switch (tjnewxclvMonthly.getId()){
                case 1001:
                    keyList.add("梅县区");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                case 1002:
                    keyList.add("梅江区");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                case 1003:
                    keyList.add("平远县");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                case 1004:
                    keyList.add("丰顺县");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                case 1005:
                    keyList.add("兴宁市");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                case 1006:
                    keyList.add("大埔县");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                case 1007:
                    keyList.add("五华县");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                case 1008:
                    keyList.add("蕉岭县");
                    valueList.add(tjnewxclvMonthly.getSubscribercount());
                    break;
                default:
                    break;
            }
        }
        resultList.add(valueList);
        resultMap.put("x",keyList);
        resultMap.put("y",resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 景区接待人次月排名(全市，县区)
     * @Date: 2018-08-10 17:36
     * @Params: [year_id, month_id, type_id, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/xcjqjdrcypm/{year_id}/{month_id}/{id_type}/{id}")
    public HashMap<String,Object> xcjqjdrcypm(@PathVariable("year_id") Integer year_id,
                                              @PathVariable("month_id") Integer month_id,
                                              @PathVariable("id_type") Integer type_id,
                                              @PathVariable("id") Integer id){
        HashMap<String,Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Object>> resultList = new ArrayList<>();
        List<TjnewxclvMonthly> queryList = tjnewxclvMonthlyService.getxcjqjdrcypm(year_id,month_id,type_id,id);
        for(int i = 0;i<queryList.size();i++){
            List<Object> valueList = new ArrayList<>();
            valueList.add(i+1);
            valueList.add(queryList.get(i).getSpotName());
            valueList.add(queryList.get(i).getSubscribercount());
            resultList.add(valueList);
        }
        keyList.add("排名");
        keyList.add("景点");
        keyList.add("人次");
        resultMap.put("data",resultList);
        resultMap.put("title",keyList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 景区接待人次年度排名(全市，县区)
     * @Date: 2018-08-10 19:19
     * @Params: [year_id, month_id, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/xcjqjdrcndpm/{year_id}/{id_type}/{id}")
    public HashMap<String,Object> xcjqjdrcndpm(@PathVariable("year_id") Integer year_id,
                                               @PathVariable("id_type") Integer month_id,
                                               @PathVariable("id") Integer id){
        HashMap<String,Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Object>> resultList = new ArrayList<>();
        List<TjnewxclvMonthly> queryList = tjnewxclvMonthlyService.getxcjqjdrcndpm(year_id,month_id,id);
        for(int i = 0;i<queryList.size();i++){
            List<Object> valueList = new ArrayList<>();
            valueList.add(i+1);
            valueList.add(queryList.get(i).getSpotName());
            valueList.add(queryList.get(i).getValueofcount());
            resultList.add(valueList);
        }
        keyList.add("排名");
        keyList.add("景点");
        keyList.add("人次");
        resultMap.put("data",resultList);
        resultMap.put("title",keyList);
        return resultMap;
    }
    /**
     * @Author: Create by white
     * @Description: 景区月接待人次同比分析
     * @Date: 2018-08-10 19:42
     * @Params: [year_id, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/xcjqyjdrctbfx/{year_id}/{id_type}/{id}")
    public HashMap<String,Object> xcjqyjdrctbfx(@PathVariable("year_id") Integer year_id,
                                                @PathVariable("id_type") Integer id_type,
                                                @PathVariable("id") Integer id){
        HashMap<String,Object> resultMap  = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Float>> resultList = new ArrayList<>();
        List<Float> valueList = new ArrayList<>();
        List<TjnewxclvMonthly> queryList = tjnewxclvMonthlyService.getxcjqyjdrctbfx(year_id,id_type,id);
        for(TjnewxclvMonthly tjnewxclvMonthly :queryList){
            keyList.add(tjnewxclvMonthly.getRecordmonth()+"月");
            valueList.add(tjnewxclvMonthly.getTrend()==null?-1:tjnewxclvMonthly.getTrend());
        }
        resultList.add(valueList);
        resultMap.put("x",keyList);
        resultMap.put("y",resultList);
        return resultMap;
    }


}
