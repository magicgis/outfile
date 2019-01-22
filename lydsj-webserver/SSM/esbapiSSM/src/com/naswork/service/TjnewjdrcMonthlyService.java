package com.naswork.service;

import java.util.List;

import com.naswork.model.TjnewjdrcMonthly;

public interface TjnewjdrcMonthlyService {

    /**
     * 根据year参数查询某年所有月接待人次-全市每月接待人次
     * @param year
     * @return
     */
    
    public List<TjnewjdrcMonthly>  getTjnewjdrcMonthly( Integer year,Integer id);
    /**
     * 根据year参数和id参数查询本县区和景区某年所有月接待人次-本县区和景区每月接待人次
     * @param year
     * @param id
     * @return
     */
      public List<TjnewjdrcMonthly>  getBxqjdrcMonthly( Integer year, Integer id);
      /**
       * 根据year参数和level参数查询所有3A景区某年所有月接待人次-全市3A景区每月接待人次
       * @param year
       * @param level
       * @return
       */
      public List<TjnewjdrcMonthly>  getQs3ajdrcMonthly( Integer year, Integer level);
      /**
       * 根据year参数和month参数查询各县区月接待人次-各县区每月接待人次
       * @param year
       * @param month
       * @return
       */
      public List<TjnewjdrcMonthly>  getGxqjdrcMonthly( Integer year, Integer month);
      /**
       * 根据year参数查询县区景区接待人次年度排名 
       * @param year
       * @param id
       * @return
       */
      public List<TjnewjdrcMonthly>  getJqjdrcndpm( Integer year, Integer id);
      /**
       * 根据year参数查询3a景区接待人次年度排名 
       * @param year
       * @return
       */
      public List<TjnewjdrcMonthly>  getJq3ajdrcndpm(Integer year);
      /**
       * 根据year、month、id参数查询本县某年某月景区接待人次年度排名
       * @param year
       * @param month
       * @param id
       * @return
       */
      public List<TjnewjdrcMonthly>  getBxjdrcndpm( Integer year, Integer month, Integer id);
      
      /**
       * 根据year、month、id参数查询3a景区接待人次月排名
       * @param year
       * @param month
       * @param level
       * @return
       */
      public List<TjnewjdrcMonthly>  getJq3ajdrcypm( Integer year, Integer month, Integer level);
      /**
       * 查询某县区所有3A景区接待人数之和，展示某年所有月
       * @param year
       * @param id
       * @return
       */
       public List<TjnewjdrcMonthly>  getMx3ajqjdrs( Integer year, Integer id);
}
