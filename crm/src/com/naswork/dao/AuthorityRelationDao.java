package com.naswork.dao;

import java.util.List;

import com.naswork.model.AuthorityRelation;
import com.naswork.module.xtgl.controller.PowerVo;

public interface AuthorityRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthorityRelation record);

    int insertSelective(PowerVo record);

    AuthorityRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthorityRelation record);

    int updateByPrimaryKey(AuthorityRelation record);
    
    void updateBySupplierId(AuthorityRelation record);
    
    public void deletePower(Integer id);
    
    public List<Integer> getUserId(Integer supplierId);
    
    public int checkPower(PowerVo powerVo);
    
    List<AuthorityRelation> selectBySupplierId(Integer supplierId);
    
    List<AuthorityRelation> selectByClientId(Integer clientId);
    
    public Integer getCassieUserId(Integer supplierId);
    
    public List<Integer> getUserIdByClient(Integer clientId);
    
    public Integer checkPowerBySupplier(Integer userId,Integer supplierId);
    
    public Integer checkPowerByClient(Integer userId,Integer clientId);
}