package com.naswork.service;

import com.naswork.model.TjnewxclvMonthly;

import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 16:05
 * @Description:
 * @Modify_By:
 */
public interface TjnewxclvMonthlyService {

    /**
     * 全市景区每月接待人次
     * @param yearId
     * @param typeId
     * @param Id
     * @return
     */
    List<TjnewxclvMonthly> getxcjqmyjdrc(Integer yearId,Integer typeId,Integer Id);

    /**
     * 景区每年接待人次(全市，县区，景区)
     * @param typeId
     * @param Id
     * @return
     */
    List<TjnewxclvMonthly> getxcjqmnjdrc(Integer typeId,Integer Id);

    /**
     * 各县区景区月接待人次(全市页面)
     * @param yearId
     * @param monthId
     * @return
     */
    List<TjnewxclvMonthly> getxcgxqjqyjdrc(Integer yearId,Integer monthId);

    /**
     * 景区接待人次月排名(全市，县区)
     * @param yearId
     * @param typeId
     * @param id
     * @return
     */
    List<TjnewxclvMonthly> getxcjqjdrcypm(Integer yearId,Integer monthId,Integer typeId,Integer id);

    /**
     * 景区接待人次年度排名(全市，县区)
     * @param yearId
     * @param id_type
     * @param id
     * @return
     */
    List<TjnewxclvMonthly> getxcjqjdrcndpm(Integer yearId,Integer id_type,Integer id);

    /**
     * 景区月接待人次同比分析
     * @param yearId
     * @param id_type
     * @param id
     * @return
     */
    List<TjnewxclvMonthly> getxcjqyjdrctbfx(Integer yearId,Integer id_type,Integer id);
}
