package com.naswork.service;

import com.naswork.model.TjnewjdrzDaily;
import com.naswork.model.TjnewjdrzMonthly;

import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 15:29
 * @Description:
 * @Modify_By:
 */
public interface TjnewjdrzMonthlyService {

    /**
     * 全市住宿每月接待人数
     * @param yearId
     * @param id_type
     * @param id
     * @return
     */
    List<TjnewjdrzMonthly> getzsmyjdrs(Integer yearId,Integer id_type,Integer id);

    /**
     * 全市住宿每年接待人数
     * @param type_id
     * @param id
     * @return
     */
    List<TjnewjdrzMonthly> getzsmnjdrs(Integer type_id,Integer id);

    /**
     * 各类别住宿月接待人数
     * @param year_id
     * @param month_id
     * @return
     */
    List<TjnewjdrzMonthly> getglbzsyjdrs(Integer year_id,Integer month_id);

    /**
     * 住宿接待人数月排名
     * @param year_id
     * @param month_id
     * @param type_id
     * @param id
     * @return
     */
    List<TjnewjdrzMonthly> getzsjdrsypm(Integer year_id,Integer month_id,Integer type_id,Integer id);

    /**
     *住宿接待人数年度排名
     * @param year_id
     * @param type_id
     * @param id
     * @return
     */
    List<TjnewjdrzMonthly> getzsjdrsndpm(Integer year_id,Integer type_id,Integer id);

    /**
     * 各类别住宿年接待人数
     * @param year_id
     * @return
     */
    List<TjnewjdrzMonthly> getglbzsnjdrs(Integer year_id);

    /**
     * 住宿月接待人数同比分析
     * @param year_id
     * @return
     */
    List<TjnewjdrzMonthly> getzsyjdrstbfx(Integer year_id,Integer type_id,Integer id);

    /**
     *住宿年度接待人数环比分析
     * @return
     */
    List<TjnewjdrzMonthly> getzsndjdrshbfx(Integer type_id,Integer id);

}
