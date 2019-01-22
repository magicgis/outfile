package com.naswork.module.tjv2;

import com.naswork.service.TjnewjdrzDailyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: white
 * @Date: create in 2018-08-10 15:28
 * @Description: 酒店分析
 * @Modify_By:
 */
@RestController
@RequestMapping(value = "/v2")
public class TjnewjdrzDailyController {

    @Resource
    private TjnewjdrzDailyService tjnewjdrzDailyService;




}
