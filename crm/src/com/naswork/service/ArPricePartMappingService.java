package com.naswork.service;

import com.naswork.model.ArPricePartMapping;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.module.storage.controller.assetpackage.ArPricePartMappingVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-21 12:20
 * @Description: AR 预测价列表部分
 * @Modify_By:
 */
public interface ArPricePartMappingService {

    void getARPriceDataPage(PageModel<ArPricePartMappingVo> page, String where, GridSort sort);

    List<ArPricePartMapping> getArPriceListById(int id);

}
