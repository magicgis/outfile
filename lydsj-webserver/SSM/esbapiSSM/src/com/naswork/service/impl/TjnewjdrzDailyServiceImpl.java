package com.naswork.service.impl;

import com.naswork.dao.TjnewjdrzDailyDao;
import com.naswork.service.TjnewjdrzDailyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: white
 * @Date: create in 2018-08-10 15:37
 * @Description:
 * @Modify_By:
 */
@Service(value = "TjnewjdrzDailyServiceImpl")
public class TjnewjdrzDailyServiceImpl implements TjnewjdrzDailyService {

    @Resource
    private TjnewjdrzDailyDao tjnewjdrzDailyDao;



}
