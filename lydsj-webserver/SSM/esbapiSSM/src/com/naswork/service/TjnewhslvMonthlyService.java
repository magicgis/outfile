package com.naswork.service;

import com.naswork.model.TjnewhslvDaily;
import com.naswork.model.TjnewhslvMonthly;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-08 19:15
 * @Description:
 * @Modify_By:
 */

public interface TjnewhslvMonthlyService {

    /**
     * 根据年份获取数据
     * @param yearId
     * @return
     */
    List<TjnewhslvMonthly> getSubscriberCountOfMonths(Integer yearId,Integer type_id,Integer id);

    /**
     * 根据id,level获取数据
     * @param typeId
     * @param id
     * @return
     */
    List<TjnewhslvMonthly> gethsjqmnjdrc(Integer typeId, Integer id);

    /**
     * 根据年份和月份获取 各县区景区月接待人次
     * @param yearId
     * @param monthId
     * @return
     */
    List<TjnewhslvMonthly> gethsgxqjqyjdrc(Integer yearId,Integer monthId);

    /**
     * 全市景区接待人次月排名
     * @param yearId
     * @param monthId
     * @param typeId
     * @param id
     * @return
     */
    List<TjnewhslvMonthly> gethsjqjdrcypm(Integer yearId,Integer monthId,Integer typeId,Integer id);

    /**
     * 全市景区接待人次年度排名
     * @param yearId
     * @param typeId
     * @param id
     * @return
     */
    List<TjnewhslvMonthly> gethsjqjdrcndpm(Integer yearId,Integer typeId,Integer id);

    /**
     * 景区月接待人次同比分析
     * @param yearId
     * @param typeId
     * @param id
     * @return
     */
    List<TjnewhslvMonthly> gethsjqyjdrctbfx(Integer yearId,Integer typeId,Integer id);
}


