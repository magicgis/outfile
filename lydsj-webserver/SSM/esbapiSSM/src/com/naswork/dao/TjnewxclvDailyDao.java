package com.naswork.dao;

import com.naswork.model.TjnewxclvDaily;
import com.naswork.model.TjnewxclvDailyKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TjnewxclvDailyDao {

    List<TjnewxclvDaily> getxcjqmrjdrc(@Param("yearId") Integer year_id,
                                       @Param("monthId") Integer month_id,
                                       @Param("typeId") Integer type_id,
                                       @Param("Id") Integer Id);

}