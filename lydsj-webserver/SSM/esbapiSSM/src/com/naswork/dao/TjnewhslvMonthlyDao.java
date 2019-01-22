package com.naswork.dao;

import com.naswork.model.TjnewhslvDaily;
import com.naswork.model.TjnewhslvMonthly;
import com.naswork.model.TjnewhslvMonthlyKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

public interface TjnewhslvMonthlyDao {
    int deleteByPrimaryKey(TjnewhslvMonthlyKey key);

    int insert(TjnewhslvMonthly record);

    int insertSelective(TjnewhslvMonthly record);

    TjnewhslvMonthly selectByPrimaryKey(TjnewhslvMonthlyKey key);

    int updateByPrimaryKeySelective(TjnewhslvMonthly record);

    int updateByPrimaryKey(TjnewhslvMonthly record);

    List<TjnewhslvMonthly> getSubscriberCountOfMonths(@Param("yearId") Integer yearId,@Param("typeId") Integer typeId,@Param("Id") Integer id);

    List<TjnewhslvMonthly> gethsjqmnjdrc(@Param("typeId") Integer typeId,
                                       @Param("id") Integer id);

    List<TjnewhslvMonthly> gethsgxqjqyjdrc(@Param("yearId") Integer yearId,@Param("monthId") Integer monthId);

    List<TjnewhslvMonthly> gethsjqjdrcypm(@Param("yearId") Integer yearId,@Param("monthId") Integer monthId,@Param("typeId") Integer typeId,@Param("Id") Integer id);

    List<TjnewhslvMonthly> gethsjqjdrcndpm(@Param("yearId") Integer yearId,@Param("typeId") Integer typeId,@Param("id") Integer id);

    List<TjnewhslvMonthly> gethsjqyjdrctbfx(@Param("yearId") Integer yearId,@Param("typeId") Integer typeId,@Param("id") Integer id);

}