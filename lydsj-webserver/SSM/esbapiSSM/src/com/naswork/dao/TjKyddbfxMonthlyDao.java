package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjKyddbfxMonthly;
import com.naswork.model.TjKyddbfxMonthlyKey;

public interface TjKyddbfxMonthlyDao {
    int deleteByPrimaryKey(TjKyddbfxMonthlyKey key);

    int insert(TjKyddbfxMonthly record);

    int insertSelective(TjKyddbfxMonthly record);

    TjKyddbfxMonthly selectByPrimaryKey(TjKyddbfxMonthlyKey key);

    int updateByPrimaryKeySelective(TjKyddbfxMonthly record);

    int updateByPrimaryKey(TjKyddbfxMonthly record);

    TjKyddbfxMonthly getList4FromMonth(@Param("id") Integer id, @Param("idStr") String idStr);
	
    TjKyddbfxMonthly getList3FromMonth(@Param("id") Integer id, @Param("idStr") String idStr);
	
    TjKyddbfxMonthly getList2FromMonth(@Param("id") Integer id, @Param("idStr") String idStr);
    /**
     * 查询所有某年所有数据的月 
     * @param year
     * @return
     */
    public List<Integer> getMonths(@Param("year") Integer year);
    /**
     * 获取某年 某月国际游客所占比 
     * @param year
     * @param month
     * @return
     */
    public Double getGuojiPercent(@Param("year") Integer year);
    /**
     * 获取某年某月国内游客所占比
     * @param year
     * @param month
     * @return
     */
    
    public Double getGuogneiPercent(@Param("year") Integer year);
    /**
     * 获取某年某月省内游客所占比
     * @param year
     * @param month
     * @return
     */
    
    public Double getShengneiPercent(@Param("year") Integer year);
}