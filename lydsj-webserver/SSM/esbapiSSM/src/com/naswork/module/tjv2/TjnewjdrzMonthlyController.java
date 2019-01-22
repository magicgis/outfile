package com.naswork.module.tjv2;

import com.naswork.model.TjnewjdrzMonthly;
import com.naswork.service.TjnewjdrzMonthlyService;
import com.naswork.vo.RankVo;
import org.omg.CORBA.OBJ_ADAPTER;
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
 * @Date: create in 2018-08-10 15:27
 * @Description: 酒店分析
 * @Modify_By:
 */
@RequestMapping(value = "/v2")
@RestController
public class TjnewjdrzMonthlyController {

    @Resource
    private TjnewjdrzMonthlyService tjnewjdrzMonthlyService;

    /**
     * @Author: Create by white
     * @Description: 住宿每月接待人数
     * @Date: 2018-08-10 21:16
     * @Params: [yearId, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/zsmyjdrs/{year_id}/{id_type}/{id}")
    public HashMap<String, Object> zsmyjdrs(@PathVariable("year_id") Integer yearId,
                                            @PathVariable("id_type") Integer id_type,
                                            @PathVariable("id") Integer id) {
        if (id_type == 2) {
            id_type = 4;
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getzsmyjdrs(yearId, id_type, id);
        for (TjnewjdrzMonthly tjnewjdrzMonthly : queryList) {
            keyList.add(tjnewjdrzMonthly.getRecordmonth() + "月");
            valueList.add(tjnewjdrzMonthly.getSubscribercount());
        }
        resultList.add(valueList);
        resultMap.put("x", keyList);
        resultMap.put("y", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 全市住宿每年接待人数
     * @Date: 2018-08-10 21:26
     * @Params: [id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/zsmnjdrs/{id_type}/{id}")
    public HashMap<String, Object> zsmnjdrs(@PathVariable("id_type") Integer id_type,
                                            @PathVariable("id") Integer id) {
        if (id_type == 2) {
            id_type = 4;
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getzsmnjdrs(id_type, id);
        for (TjnewjdrzMonthly tjnewjdrzMonthly : queryList) {
            keyList.add(tjnewjdrzMonthly.getRecordyear() + "年");
            valueList.add(tjnewjdrzMonthly.getSubscribercount());
        }
        resultList.add(valueList);
        resultMap.put("x", keyList);
        resultMap.put("y", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 各类别住宿月接待人数
     * @Date: 2018-08-11 10:04
     * @Params: [year_id, month_id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/glbzsyjdrs/{year_id}/{month_id}")
    public HashMap<String, Object> glbzsyjdrs(@PathVariable("year_id") Integer year_id,
                                              @PathVariable("month_id") Integer month_id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getglbzsyjdrs(year_id, month_id);
        for (TjnewjdrzMonthly tjnewjdrzMonthly : queryList) {
            switch (tjnewjdrzMonthly.getId()) {
                case 2001:
                    keyList.add("酒店");
                    valueList.add(tjnewjdrzMonthly.getSubscribercount());
                    break;
                case 2002:
                    keyList.add("民宿");
                    valueList.add(tjnewjdrzMonthly.getSubscribercount());
                    break;
                case 2003:
                    keyList.add("农家乐");
                    valueList.add(tjnewjdrzMonthly.getSubscribercount());
                    break;
                default:
                    break;
            }
        }
        resultList.add(valueList);
        resultMap.put("x", keyList);
        resultMap.put("y", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 全市住宿接待人数月排名
     * @Date: 2018-08-11 10:38
     * @Params: [year_id, month_id, type_id, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/zsjdrsypm/{year_id}/{month_id}/{id_type}/{id}")
    public HashMap<String, Object> zsjdrsypm(@PathVariable("year_id") Integer year_id,
                                             @PathVariable("month_id") Integer month_id,
                                             @PathVariable("id_type") Integer type_id,
                                             @PathVariable("id") Integer id) {
        if(type_id==2){
            type_id = 2;
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = Arrays.asList("排名", "名称", "人数");
        List<List<Object>> resultList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getzsjdrsypm(year_id, month_id, type_id, id);
        for (int i = 0; i < queryList.size(); i++) {
            List<Object> valueList = new ArrayList<>();
            valueList.add(i + 1);
            valueList.add(queryList.get(i).getSpotName());
            valueList.add(queryList.get(i).getSubscribercount());
            resultList.add(valueList);
        }
        resultMap.put("title", keyList);
        resultMap.put("data", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 住宿接待人数年度排名
     * @Date: 2018-08-11 11:46
     * @Params: [year_id, type_id, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/zsjdrsndpm/{year_id}/{id_type}/{id}")
    public HashMap<String, Object> zsjdrsndpm(@PathVariable("year_id") Integer year_id,
                                              @PathVariable("id_type") Integer type_id,
                                              @PathVariable("id") Integer id) {
        if (type_id == 2) {
            type_id = 4;
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = null;
        List<List<Object>> resultList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getzsjdrsndpm(year_id, type_id, id);
        if (queryList == null) {
            List<String> stringList = new ArrayList<>();
            int[][] arrays = {};
            resultMap.put("title", stringList);
            resultMap.put("data", arrays);
        } else {
            keyList = Arrays.asList("排名", "名称", "人数");
            resultMap.put("title", keyList);
            for (int i = 0; i < queryList.size(); i++) {
                List<Object> valueList = new ArrayList<>();
                valueList.add(i + 1);
                valueList.add(queryList.get(i).getSpotName());
                valueList.add(queryList.get(i).getValueofcount());
                resultList.add(valueList);
            }
            resultMap.put("data", resultList);
        }
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 各类别住宿年接待人数
     * @Date: 2018-08-11 12:05
     * @Params: [year_id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/glbzsnjdrs/{year_id}")
    public HashMap<String, Object> glbzsnjdrs(@PathVariable("year_id") Integer year_id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getglbzsnjdrs(year_id);
        for (TjnewjdrzMonthly tjnewjdrzMonthly : queryList) {
            switch (tjnewjdrzMonthly.getId()) {
                case 2001:
                    keyList.add("酒店");
                    valueList.add(tjnewjdrzMonthly.getValueofcount());
                    break;
                case 2002:
                    keyList.add("民宿");
                    valueList.add(tjnewjdrzMonthly.getValueofcount());
                    break;
                case 2003:
                    keyList.add("农家乐");
                    valueList.add(tjnewjdrzMonthly.getValueofcount());
                    break;
                default:
                    break;
            }
        }
        resultList.add(valueList);
        resultMap.put("x", keyList);
        resultMap.put("y", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 住宿月接待人数同比分析
     * @Date: 2018-08-11 14:29
     * @Params: [year_id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/zsyjdrstbfx/{year_id}/{type_id}/{id}")
    public HashMap<String, Object> zsyjdrstbfx(@PathVariable("year_id") Integer year_id,
                                               @PathVariable("type_id") Integer type_id,
                                               @PathVariable("id") Integer id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Float>> resultList = new ArrayList<>();
        List<Float> valueList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getzsyjdrstbfx(year_id, type_id, id);
        for (TjnewjdrzMonthly tjnewjdrzMonthly : queryList) {
            keyList.add(tjnewjdrzMonthly.getRecordmonth() + "月");
            valueList.add(tjnewjdrzMonthly.getTrend()==null?-1:tjnewjdrzMonthly.getTrend());
        }
        resultList.add(valueList);
        resultMap.put("x", keyList);
        resultMap.put("y", resultList);
        return resultMap;
    }

    /**
     * @Author: Create by white
     * @Description: 住宿年度接待人数环比分析
     * @Date: 2018-08-11 14:40
     * @Params: []
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/zsndjdrshbfx/{type_id}/{id}")
    public HashMap<String, Object> zsndjdrshbfx(@PathVariable("type_id") Integer type_id,
                                                @PathVariable("id") Integer id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Float>> resultList = new ArrayList<>();
        List<Float> valueList = new ArrayList<>();
        List<TjnewjdrzMonthly> queryList = tjnewjdrzMonthlyService.getzsndjdrshbfx(type_id, id);
        for (TjnewjdrzMonthly tjnewjdrzMonthly : queryList) {
            keyList.add(tjnewjdrzMonthly.getRecordyear() + "年");
            valueList.add(tjnewjdrzMonthly.getTrend() == null ? -1 : tjnewjdrzMonthly.getTrend());
        }
        resultList.add(valueList);
        resultMap.put("x", keyList);
        resultMap.put("y", resultList);
        return resultMap;
    }

}
