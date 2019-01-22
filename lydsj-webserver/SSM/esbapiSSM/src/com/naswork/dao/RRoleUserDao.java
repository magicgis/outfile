package com.naswork.dao;

import com.naswork.model.RRoleUser;

public interface RRoleUserDao {
    int insert(RRoleUser record);

    int insertSelective(RRoleUser record);
    
    public void deletePeople(RRoleUser rRoleUser);
}