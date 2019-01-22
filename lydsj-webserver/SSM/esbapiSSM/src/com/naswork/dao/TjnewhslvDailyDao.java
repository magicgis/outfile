package com.naswork.dao;

import com.naswork.model.TjnewhslvDaily;
import com.naswork.model.TjnewhslvDailyKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TjnewhslvDailyDao {
    int deleteByPrimaryKey(TjnewhslvDailyKey key);

    int insert(TjnewhslvDaily record);

    int insertSelective(TjnewhslvDaily record);

    TjnewhslvDaily selectByPrimaryKey(TjnewhslvDailyKey key);

    int updateByPrimaryKeySelective(TjnewhslvDaily record);

    int updateByPrimaryKey(TjnewhslvDaily record);

    List<TjnewhslvDaily> gethsjqmrjdrc(@Param("yearId") Integer yearId,
                                       @Param("monthId") Integer monthId,
                                       @Param("typeId") Integer typeId,
                                       @Param("id") Integer id);



}