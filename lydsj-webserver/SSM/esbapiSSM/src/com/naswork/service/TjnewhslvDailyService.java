package com.naswork.service;

import com.naswork.model.TjnewhslvDaily;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-08 17:48
 * @Description:
 * @Modify_By:
 */

public interface TjnewhslvDailyService {

    List<TjnewhslvDaily> gethsjqmrjdrc(Integer yearId,Integer monthId,Integer type_id,Integer id);

}
