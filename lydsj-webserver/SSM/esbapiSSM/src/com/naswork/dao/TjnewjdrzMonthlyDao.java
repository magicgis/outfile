package com.naswork.dao;

import com.naswork.model.TjnewjdrzMonthly;
import com.naswork.model.TjnewjdrzMonthlyKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TjnewjdrzMonthlyDao {

  List<TjnewjdrzMonthly> getzsmyjdrs(@Param("yearId") Integer year_id,
                                     @Param("typeId") Integer type_id,
                                     @Param("Id") Integer id);

  List<TjnewjdrzMonthly> getzsmnjdrs(@Param("typeId") Integer typeId,
                                     @Param("Id") Integer Id);

  List<TjnewjdrzMonthly> getglbzsyjdrs(@Param("yearId") Integer year_id,
                                       @Param("monthId") Integer monthId);

  List<TjnewjdrzMonthly> getzsjdrsypm(@Param("yearId") Integer year_id,
                                      @Param("monthId") Integer monthId,
                                      @Param("typeId") Integer typeId,
                                      @Param("Id") Integer Id);

  List<TjnewjdrzMonthly> getzsjdrsndpm(@Param("yearId") Integer year_id,
                                       @Param("typeId") Integer type_id,
                                       @Param("Id") Integer Id);

  List<TjnewjdrzMonthly> getglbzsnjdrs(@Param("yearId") Integer year_id);

  List<TjnewjdrzMonthly> getzsyjdrstbfx(@Param("yearId") Integer year_id,
                                        @Param("typeId") Integer type_id,
                                        @Param("Id") Integer id);

  List<TjnewjdrzMonthly> getzsndjdrshbfx(@Param("typeId") Integer typeId,
                                         @Param("Id") Integer Id);
}