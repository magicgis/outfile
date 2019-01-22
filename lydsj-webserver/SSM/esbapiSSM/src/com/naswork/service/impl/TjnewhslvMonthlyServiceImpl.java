package com.naswork.service.impl;

import com.naswork.dao.TjnewhslvMonthlyDao;
import com.naswork.model.TjnewhslvDaily;
import com.naswork.model.TjnewhslvMonthly;
import com.naswork.service.TjnewhslvMonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-08 19:16
 * @Description:
 * @Modify_By:
 */
@Service("TjnewhslvMonthlyServiceImpl")
public class TjnewhslvMonthlyServiceImpl implements TjnewhslvMonthlyService {

    @Resource
    private TjnewhslvMonthlyDao tjnewhslvMonthlyMapper;

    /**
     * @Author: Create by white
     * @Description:
     * @Date: 2018-08-08 19:43
     * @Params: [yearId]
     * @Return: java.util.List<java.lang.Integer>
     * @Throws:
     */
    @Override
    public List<TjnewhslvMonthly> getSubscriberCountOfMonths(Integer yearId,Integer typeId,Integer id) {
        return tjnewhslvMonthlyMapper.getSubscriberCountOfMonths(yearId,typeId,id);
    }

    @Override
    public List<TjnewhslvMonthly> gethsjqmnjdrc(Integer typeId, Integer id) {
        return tjnewhslvMonthlyMapper.gethsjqmnjdrc(typeId,id);
    }

    @Override
    public List<TjnewhslvMonthly> gethsgxqjqyjdrc(Integer yearId, Integer monthId) {
        return tjnewhslvMonthlyMapper.gethsgxqjqyjdrc(yearId,monthId);
    }

    @Override
    public List<TjnewhslvMonthly> gethsjqjdrcypm(Integer yearId, Integer monthId, Integer typeId, Integer id) {
        return tjnewhslvMonthlyMapper.gethsjqjdrcypm(yearId,monthId,typeId,id);
    }

    @Override
    public List<TjnewhslvMonthly> gethsjqjdrcndpm(Integer yearId, Integer typeId, Integer id) {
        return tjnewhslvMonthlyMapper.gethsjqjdrcndpm(yearId,typeId,id);
    }

    @Override
    public List<TjnewhslvMonthly> gethsjqyjdrctbfx(Integer yearId, Integer typeId, Integer id) {
        return tjnewhslvMonthlyMapper.gethsjqyjdrctbfx(yearId,typeId,id);
    }

}
