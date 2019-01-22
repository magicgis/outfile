package com.naswork.service.impl;

import com.naswork.dao.TjnewxclvDailyDao;
import com.naswork.model.TjnewxclvDaily;
import com.naswork.service.TjnewxclvDailyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 16:24
 * @Description:
 * @Modify_By:
 */
@Service(value = "TjnewxclvDailyServiceImpl")
public class TjnewxclvDailyServiceImpl implements TjnewxclvDailyService {

    @Resource
    private TjnewxclvDailyDao tjnewxclvDailyDao;


    @Override
    public List<TjnewxclvDaily> getxcjqmrjdrc(Integer yearId, Integer monthId, Integer typeId, Integer Id) {
        return tjnewxclvDailyDao.getxcjqmrjdrc(yearId,monthId,typeId,Id);
    }
}
