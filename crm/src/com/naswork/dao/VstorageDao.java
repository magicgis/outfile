package com.naswork.dao;

import com.naswork.model.Vstorage;

public interface VstorageDao {
    int insert(Vstorage record);

    int insertSelective(Vstorage record);
}