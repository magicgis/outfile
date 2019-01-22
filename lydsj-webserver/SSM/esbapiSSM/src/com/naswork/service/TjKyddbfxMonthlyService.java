package com.naswork.service;

import java.util.List;

import com.naswork.model.TjKyddbfxMonthly;
import com.naswork.model.TjKyddbfxMonthlyKey;

public interface TjKyddbfxMonthlyService {
	int deleteByPrimaryKey(TjKyddbfxMonthlyKey key);

	int insert(TjKyddbfxMonthly record);

	int insertSelective(TjKyddbfxMonthly record);

	TjKyddbfxMonthly selectByPrimaryKey(TjKyddbfxMonthlyKey key);

	int updateByPrimaryKeySelective(TjKyddbfxMonthly record);

	int updateByPrimaryKey(TjKyddbfxMonthly record);

	TjKyddbfxMonthly getList4FromMonth(Integer id, String idStr);

	TjKyddbfxMonthly getList3FromMonth(Integer id, String idStr);

	TjKyddbfxMonthly getList2FromMonth(Integer id, String idStr);
    /**
     * 查询所有某年所有数据的月 
     * @param year
     * @return
     */
    public List<Integer> getMonths(Integer year);
    /**
     * 获取某年 某月国际游客所占比 
     * @param year
     * @param month
     * @return
     */
    public Double getGuojiPercent(Integer year);
    /**
     * 获取某年某月国内游客所占比
     * @param year
     * @param month
     * @return
     */
    
    public Double getGuogneiPercent( Integer year);
    /**
     * 获取某年某月省内游客所占比
     * @param year
     * @param month
     * @return
     */
    
    public Double getShengneiPercent(Integer year);
}
