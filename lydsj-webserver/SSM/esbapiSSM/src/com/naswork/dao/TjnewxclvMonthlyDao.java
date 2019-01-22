package com.naswork.dao;

import com.naswork.model.TjnewxclvMonthly;
import com.naswork.model.TjnewxclvMonthlyKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TjnewxclvMonthlyDao {

    List<TjnewxclvMonthly> getxcjqmyjdrc(@Param("yearId") Integer yearId,
                                         @Param("typeId") Integer typeId,
                                         @Param("Id") Integer Id);
    List<TjnewxclvMonthly> getxcjqmnjdrc(@Param("typeId") Integer typeId,
                                         @Param("Id") Integer Id);

    List<TjnewxclvMonthly> getxcgxqjqyjdrc(@Param("yearId") Integer yearId,
                                           @Param("monthId") Integer monthId);

    List<TjnewxclvMonthly> getxcjqjdrcypm(@Param("yearId") Integer yearId,
                                          @Param("monthId") Integer monthId,
                                          @Param("typeId") Integer typeId,
                                          @Param("Id") Integer Id);

    List<TjnewxclvMonthly> getxcjqjdrcndpm(@Param("yearId") Integer yearId,
                                           @Param("typeId") Integer typeId,
                                           @Param("Id") Integer Id);

    List<TjnewxclvMonthly> getxcjqyjdrctbfx(@Param("yearId") Integer yearId,
                                            @Param("typeId") Integer typeId,
                                            @Param("Id") Integer Id);
}