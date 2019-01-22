package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.TjnewjdrcMonthly;

@Component("tjnewjdrcMonthlyDao")
public interface TjnewjdrcMonthlyDao {
	
    /**
     * 根据year参数查询某年所有月接待人次-全市每月接待人次
     * @param year
     * @return
     */
    
    public List<TjnewjdrcMonthly>  getTjnewjdrcMonthly(@Param("year") Integer year,@Param("id") Integer id);
    
  /**
   * 根据year参数和id参数查询本县区和景区某年所有月接待人次-本县区和景区每月接待人次
   * @param year
   * @param id
   * @return
   */
    public List<TjnewjdrcMonthly>  getBxqjdrcMonthly(@Param("year") Integer year,@Param("id") Integer id);
    
    /**
     * 根据year参数和level参数查询所有3A景区某年所有月接待人次-全市3A景区每月接待人次
     * @param year
     * @param level
     * @return
     */
    public List<TjnewjdrcMonthly>  getQs3ajdrcMonthly(@Param("year") Integer year,@Param("level") Integer level);
    /**
     * 根据year参数和month参数查询各县区月接待人次-各县区每月接待人次
     * @param year
     * @param month
     * @return
     */
    public List<TjnewjdrcMonthly>  getGxqjdrcMonthly(@Param("year") Integer year,@Param("month") Integer month);

    /**
     * 根据year参数查询县区景区接待人次年度排名 
     * @param year
     * @param id
     * @return
     */
    public List<TjnewjdrcMonthly>  getJqjdrcndpm(@Param("year") Integer year,@Param("id") Integer id);
    /**
     * 根据year参数查询3a景区接待人次年度排名 
     * @param year
     * @return
     */
    public List<TjnewjdrcMonthly>  getJq3ajdrcndpm(@Param("year") Integer year);
    
    /**
     * 根据year、month、id参数查询本县某年某月景区接待人次年度排名
     * @param year
     * @param month
     * @param id
     * @return
     */
    public List<TjnewjdrcMonthly>  getBxjdrcndpm(@Param("year") Integer year,@Param("month") Integer month,@Param("id") Integer id);
    /**
     * 根据year、month、id参数查询3a景区接待人次月排名
     * @param year
     * @param month
     * @param level
     * @return
     */
    public List<TjnewjdrcMonthly>  getJq3ajdrcypm(@Param("year") Integer year,@Param("month") Integer month,@Param("level") Integer level);

   /**
    * 查询某县区所有3A景区接待人数之和，展示某年所有月
    * @param year
    * @param id
    * @return
    */
    public List<TjnewjdrcMonthly>  getMx3ajqjdrs(@Param("year") Integer year,@Param("id") Integer id);


}