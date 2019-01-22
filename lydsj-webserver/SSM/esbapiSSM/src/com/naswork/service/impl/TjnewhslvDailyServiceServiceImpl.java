package com.naswork.service.impl;

import com.naswork.dao.TjnewhslvDailyDao;
import com.naswork.model.TjnewhslvDaily;
import com.naswork.service.TjnewhslvDailyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-08 17:49
 * @Description:
 * @Modify_By:
 */
@Service(value = "TjnewhslvDailyServiceServiceImpl")
public class TjnewhslvDailyServiceServiceImpl implements TjnewhslvDailyService {

    @Resource
    private TjnewhslvDailyDao tjnewhslvDailyMapper;

    @Override
    public List<TjnewhslvDaily> gethsjqmrjdrc(Integer yearId, Integer monthId, Integer type_id, Integer id) {
        return tjnewhslvDailyMapper.gethsjqmrjdrc(yearId,monthId,type_id,id);
    }




}
