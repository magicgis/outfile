package com.naswork.module.tjv2;

import com.naswork.model.TjnewhslvDaily;
import com.naswork.service.TjnewhslvDailyService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @Author: white
 * @Date: create in 2018-08-08 16:54
 * @Description: 特色旅游
 * @Modify_By:
 */
@RestController
@RequestMapping(value = "/v2")
public class TjnewhslvDailyController {

    @Resource
    private TjnewhslvDailyService tjNewHslvDailyService;

    /**
     *------------------------------------红色旅游部分-----------------------------
     * --------------------------------------------------------------------------
     */
    /**
     * @Author: Create by white
     * @Description: 景区每日接待人次
     * @Date: 2018-08-09 10:37
     * @Params: [yearId, monthId, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "/hsjqmrjdrc/{year_id}/{month_id}/{id_type}/{id}")
    public HashMap<String, Object> hsjqmrjdrc(@PathVariable("year_id") Integer yearId,
                                              @PathVariable("month_id") Integer monthId,
                                              @PathVariable("id_type") Integer id_type,
                                              @PathVariable(("id")) Integer id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        //日期的list
        List<String> dayStrList = new ArrayList<>();
        //每天对应的数值列表list
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        //从数据库中获取到数据
        List<TjnewhslvDaily> queryList = tjNewHslvDailyService.gethsjqmrjdrc(yearId, monthId, id_type, id);
        //遍历数据 并将其添加到列表中
        for (TjnewhslvDaily tjnewhslvDaily : queryList) {
            dayStrList.add(monthId + "月" + tjnewhslvDaily.getDay());
            valueList.add(tjnewhslvDaily.getSumofday());
        }
        resultList.add(valueList);
        //添加x对应列表到map
        resultMap.put("x", dayStrList);
        //添加y对应列表到map
        resultMap.put("y", resultList);
        return resultMap;
    }



}





