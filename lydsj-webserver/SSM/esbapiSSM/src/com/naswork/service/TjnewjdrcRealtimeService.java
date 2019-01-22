package com.naswork.service;

import java.util.Date;
import java.util.List;

import com.naswork.model.TjnewjdrcRealtime;

public interface TjnewjdrcRealtimeService {
    /**
     *  根据系统时间当前参数获得实时人数 
     * @param curDate
     * @return
     */
    public  List<TjnewjdrcRealtime> getNewjdrcRealTime(String curDate,String morning,String night,Integer id);

    /**
     * 获取全部subcount的综合
     * @return
     */
    Integer getAllCount(Double num,String startTime,String endTime);

    /**
     * 通过父id获取count
     * @param id
     * @return
     */
    Integer getAllCountByParentId(Integer id,Double num,String startTime,String endTime);

    /**
     * 通过id获取某个id值下面的count
     * @param id
     * @return
     */
    Integer getCountById(Integer id,String startTime,String endTime);

    /**
     *热门景点实时监测(TOP-N)
     * @param typeId
     * @param id
     * @return
     */
    List<TjnewjdrcRealtime> getjqjdrcsspm(Integer typeId, Integer id, String startTime,String endTime);
}
