package com.naswork.module.tjv2;

import com.naswork.model.TjnewxclvDaily;
import com.naswork.service.TjnewxclvDailyService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 16:22
 * @Description: 特色旅游---乡村旅游部分（每天）
 * @Modify_By:
 */

@RestController
@RequestMapping(value = "/v2")
public class TjnewxclvDailyController {

    @Resource
    private TjnewxclvDailyService tjnewxclvDailyService;

    /**
     * @Author: Create by white
     * @Description:  景区每日接待人次(全市，景区)
     * @Date: 2018-08-10 16:42
     * @Params: [year_id, month_id, id_type, id]
     * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
     * @Throws:
     */
    @RequestMapping(value = "xcjqmrjdrc/{year_id}/{month_id}/{id_type}/{id}")
    public HashMap<String,Object> xcjqmrjdrc(@PathVariable("year_id") Integer year_id,
                                             @PathVariable("month_id") Integer month_id,
                                             @PathVariable("id_type") Integer id_type,
                                             @PathVariable("id") Integer id){
        HashMap<String,Object> resultMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<List<Integer>> resultList= new ArrayList<>();
        List<Integer> valueList= new ArrayList<>();
        List<TjnewxclvDaily> queryList =  tjnewxclvDailyService.getxcjqmrjdrc(year_id,month_id,id_type,id);
        for(TjnewxclvDaily tjnewxclvDaily : queryList){
            keyList.add(tjnewxclvDaily.getDayofmonth()+"日");
            valueList.add(tjnewxclvDaily.getSubscribercount());
        }
        resultList.add(valueList);
        resultMap.put("x",keyList);
        resultMap.put("y",resultList);
        return resultMap;
    }

}
