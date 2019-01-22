package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.Tjqs3ajqsrfxV2;

@Component("tjqs3ajqsrfxV2Dao")
public interface Tjqs3ajqsrfxV2Dao {

    /**
     * 根据年份参数获取3a景区该年所有月份的收入
     * @param year
     * @return
     */
    public List<Tjqs3ajqsrfxV2>getQs3ajqsrfxV2(@Param("year") String year);
}