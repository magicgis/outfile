package com.naswork.dao;

import com.naswork.model.TjNewJdrcRealtime;
import com.naswork.model.TjNewJdrcRealtimeKey;
import com.naswork.vo.PageModel;
import org.jbpm.pvm.internal.query.Page;

import java.util.List;

public interface TjNewJdrcRealtimeDao {
    int deleteByPrimaryKey(TjNewJdrcRealtimeKey key);

    int insert(TjNewJdrcRealtime record);

    int insertSelective(TjNewJdrcRealtime record);

    TjNewJdrcRealtime selectByPrimaryKey(TjNewJdrcRealtimeKey key);

    int updateByPrimaryKeySelective(TjNewJdrcRealtime record);

    int updateByPrimaryKey(TjNewJdrcRealtime record);

    List<TjNewJdrcRealtime> getList(PageModel<TjNewJdrcRealtime> page);

    List<TjNewJdrcRealtime> getSpotNameList(PageModel<TjNewJdrcRealtime> pageModel);
}