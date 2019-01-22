package com.naswork.service;

import com.naswork.model.TjNewJdrcRealtime;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-22 16:36
 * @Description:
 * @Modify_By:
 */
public interface TjNewJdrcRealtimeService {

    void getList(PageModel<TjNewJdrcRealtime> page, String where, GridSort sort);

    List<TjNewJdrcRealtime> getSpotNameList(PageModel<TjNewJdrcRealtime> pageModel);
}
