package com.naswork.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.TjnewjdrcRealtime;
import com.naswork.model.TjnewjdrcRealtimeKey;
@Component("tjnewjdrcRealtimeDao")
public interface TjnewjdrcRealtimeDao {
    int deleteByPrimaryKey(TjnewjdrcRealtimeKey key);

    int insert(TjnewjdrcRealtime record);

    int insertSelective(TjnewjdrcRealtime record);

    TjnewjdrcRealtime selectByPrimaryKey(TjnewjdrcRealtimeKey key);

    int updateByPrimaryKeySelective(TjnewjdrcRealtime record);

    int updateByPrimaryKey(TjnewjdrcRealtime record);
    /**
     *  根据系统时间当前参数获得实时人数 
     * @param curDate
     * @return
     */
    public List<TjnewjdrcRealtime> getNewjdrcRealTime(@Param("curDate") String curDate,@Param("morning") String morning,@Param("night")String night,@Param("id") Integer id);

    Integer getAllCount(@Param("num") Double num,@Param("startTime") String startTime,@Param("endTime") String endTime);

    Integer getAllCountByParentId(@Param("Id") Integer id,@Param("num") Double num,@Param("startTime") String startTime,@Param("endTime") String endTime);

    Integer getCountById(@Param("Id") Integer id,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<TjnewjdrcRealtime> getjqjdrcsspm(@Param("typeId") Integer typeId,@Param("Id") Integer Id,@Param("startTime") String startTime,@Param("endTime") String endTime);


}