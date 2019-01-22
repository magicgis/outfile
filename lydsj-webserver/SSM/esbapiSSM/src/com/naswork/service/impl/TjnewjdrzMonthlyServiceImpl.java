package com.naswork.service.impl;

import com.naswork.dao.TjnewjdrzMonthlyDao;
import com.naswork.model.TjnewjdrzMonthly;
import com.naswork.service.TjnewjdrzMonthlyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-10 15:29
 * @Description:
 * @Modify_By:
 */
@Service(value = "TjnewjdrzMonthlyServiceImpl")
public class TjnewjdrzMonthlyServiceImpl implements TjnewjdrzMonthlyService {

    @Resource
    private TjnewjdrzMonthlyDao tjnewjdrzMonthlyDao;


    @Override
    public List<TjnewjdrzMonthly> getzsmyjdrs(Integer yearId, Integer id_type, Integer id) {
        return tjnewjdrzMonthlyDao.getzsmyjdrs(yearId,id_type,id);
    }

    @Override
    public List<TjnewjdrzMonthly> getzsmnjdrs(Integer type_id, Integer id) {
        return tjnewjdrzMonthlyDao.getzsmnjdrs(type_id,id);
    }

    @Override
    public List<TjnewjdrzMonthly> getglbzsyjdrs(Integer year_id, Integer month_id) {
        return tjnewjdrzMonthlyDao.getglbzsyjdrs(year_id,month_id);
    }


    @Override
    public List<TjnewjdrzMonthly> getzsjdrsypm(Integer year_id, Integer month_id, Integer type_id, Integer id) {
        return tjnewjdrzMonthlyDao.getzsjdrsypm(year_id,month_id,type_id,id);
    }

    @Override
    public List<TjnewjdrzMonthly> getzsjdrsndpm(Integer year_id, Integer type_id, Integer id) {
        return tjnewjdrzMonthlyDao.getzsjdrsndpm(year_id,type_id,id);
    }

    @Override
    public List<TjnewjdrzMonthly> getglbzsnjdrs(Integer year_id) {
        return tjnewjdrzMonthlyDao.getglbzsnjdrs(year_id);
    }

    @Override
    public List<TjnewjdrzMonthly> getzsyjdrstbfx(Integer year_id,Integer type_id,Integer id) {
        return tjnewjdrzMonthlyDao.getzsyjdrstbfx(year_id,type_id,id);
    }

    @Override
    public List<TjnewjdrzMonthly> getzsndjdrshbfx(Integer type_id,Integer id) {
        return tjnewjdrzMonthlyDao.getzsndjdrshbfx(type_id,id);
    }



}
