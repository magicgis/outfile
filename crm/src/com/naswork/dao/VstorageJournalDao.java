package com.naswork.dao;

import com.naswork.model.VstorageJournal;

public interface VstorageJournalDao {
    int insert(VstorageJournal record);

    int insertSelective(VstorageJournal record);
}