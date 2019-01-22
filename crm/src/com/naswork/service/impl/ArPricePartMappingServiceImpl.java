package com.naswork.service.impl;

import com.naswork.dao.ArPricePartMappingDao;
import com.naswork.model.ArPricePartMapping;
import com.naswork.module.storage.controller.assetpackage.ArPricePartMappingVo;
import com.naswork.service.ArPricePartMappingService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-21 12:21
 * @Description:
 * @Modify_By:
 */
@Service("ArPricePartMappingServiceImpl")
public class ArPricePartMappingServiceImpl implements ArPricePartMappingService {


    @Resource
    private ArPricePartMappingDao arPricePartMappingDao;

    @Override
    public void getARPriceDataPage(PageModel<ArPricePartMappingVo> page, String where, GridSort sort) {
        page.put("where", where);
        if(sort!=null){
            sort.setName(ConvertUtil.toDBName(sort.getName()));
            String sort1=ConvertUtil.convertSort(sort);
            page.put("orderby", ConvertUtil.convertSort(sort));
        }else{
            page.put("orderby", null);
        }
        page.setEntities(arPricePartMappingDao.getARPriceDataPage(page));
    }

    @Override
    public List<ArPricePartMapping> getArPriceListById(int id) {
        return arPricePartMappingDao.getArPriceListById(id);
    }
}
