package com.naswork.service;

import com.naswork.model.TjnewxclvDaily;

import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 16:23
 * @Description:
 * @Modify_By:
 */
public interface TjnewxclvDailyService {

    /**
     * 景区每日接待人次(全市，景区)
     * @param yearId
     * @param monthId
     * @param typeId
     * @param Id
     * @return
     */
    List<TjnewxclvDaily> getxcjqmrjdrc(Integer yearId,Integer monthId,Integer typeId,Integer Id);


}
