package com.naswork.service.impl;

import com.naswork.dao.TjnewxclvMonthlyDao;
import com.naswork.model.TjnewxclvMonthly;
import com.naswork.service.TjnewxclvMonthlyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 16:05
 * @Description:
 * @Modify_By:
 */
@Service(value = "TjnewxclvMonthlyServiceImpl")
public class TjnewxclvMonthlyServiceImpl implements TjnewxclvMonthlyService {

   @Resource
   private TjnewxclvMonthlyDao tjnewxclvMonthlyDao;


    @Override
    public List<TjnewxclvMonthly> getxcjqmyjdrc(Integer yearId, Integer typeId, Integer Id) {
        return tjnewxclvMonthlyDao.getxcjqmyjdrc(yearId,typeId,Id);
    }

    @Override
    public List<TjnewxclvMonthly> getxcjqmnjdrc(Integer typeId, Integer Id) {
        return tjnewxclvMonthlyDao.getxcjqmnjdrc(typeId,Id);
    }

    @Override
    public List<TjnewxclvMonthly> getxcgxqjqyjdrc(Integer yearId, Integer monthId) {
        return tjnewxclvMonthlyDao.getxcgxqjqyjdrc(yearId,monthId);
    }

    @Override
    public List<TjnewxclvMonthly> getxcjqjdrcypm(Integer yearId, Integer monthId, Integer typeId, Integer id) {
        return tjnewxclvMonthlyDao.getxcjqjdrcypm(yearId,monthId,typeId,id);
    }

    @Override
    public List<TjnewxclvMonthly> getxcjqjdrcndpm(Integer yearId, Integer id_type, Integer id) {
        return tjnewxclvMonthlyDao.getxcjqjdrcndpm(yearId,id_type,id);
    }

    @Override
    public List<TjnewxclvMonthly> getxcjqyjdrctbfx(Integer yearId, Integer id_type, Integer id) {
        return tjnewxclvMonthlyDao.getxcjqyjdrctbfx(yearId,id_type,id);
    }


}
