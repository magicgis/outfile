package com.naswork.dao;

import com.naswork.model.SeqKey;
import com.naswork.model.SeqKeyKey;

public interface SeqKeyDao {
    int deleteByPrimaryKey(SeqKeyKey key);

    int insert(SeqKey record);

    int insertSelective(SeqKey record);

    SeqKey selectByPrimaryKey(SeqKeyKey key);

    int updateByPrimaryKeySelective(SeqKey record);

    int updateByPrimaryKey(SeqKey record);
    
    public SeqKey findMaxSeq(SeqKey seqKey);
}