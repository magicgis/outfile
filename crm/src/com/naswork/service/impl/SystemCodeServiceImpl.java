package com.naswork.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.naswork.dao.StockMarketSupplierMapDao;
import com.naswork.model.StockMarketSupplierMap;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import com.naswork.dao.SystemCodeDao;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.service.SystemCodeService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("systemCodeService")
public class SystemCodeServiceImpl implements SystemCodeService {

    @Resource
    private SystemCodeDao systemCodeDao;

    @Resource
    private StockMarketSupplierMapDao stockMarketSupplierMapDao;

    public int insertSelective(SystemCode record) {
        return systemCodeDao.insertSelective(record);
    }

    public SystemCode selectByPrimaryKey(Integer id) {
        return systemCodeDao.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(SystemCode record) {
        return systemCodeDao.updateByPrimaryKeySelective(record);
    }

    /*
     * 查询飞机类型
     */
    public List<SystemCode> findairType() {
        return systemCodeDao.findairType();
    }

    /*
     * 查询商业类型
     */
    public List<SystemCode> findbizType() {
        return systemCodeDao.findType("BIZ_TYPE");
    }

    /*
     * 查询询价状态
     */
    public List<SystemCode> findInquiryStatus() {
        return systemCodeDao.findType("INQUIRY_STATUS");
    }

    /*
     * 查询订单状态
     */
    public List<SystemCode> findOrderStatus() {
        return systemCodeDao.findType("ORDER_STATUS");
    }

    /*
     * 查询汇率
     */
    public List<SystemCode> findCurrency() {
        return systemCodeDao.findType("CURRENCY");
    }

    /*
     * 查询汇率
     */
    public List<ClientQuoteVo> findRate() {
        return systemCodeDao.findRate();
    }

    /*
     * 查询客户状态
     */
    public List<SystemCode> getClientStatus() {
        return systemCodeDao.findType("CLIENT_STATUS_ID");
    }

    /*
     * 查询客户等级
     */
    public List<SystemCode> getClientLevel() {
        return systemCodeDao.findType("CLIENT_LEVEL_ID");
    }

    /*
     * 按类型查询供应商信息
     */
    @Override
    public List<SystemCode> findSupplierByType(String type) {
        return systemCodeDao.findSupplierByType(type);
    }

    /*
     * 根据ID查询
     */
    public SystemCode findById(Integer id) {
        return systemCodeDao.findById(id);
    }

    /*
     * 发货方式
     */
    public List<SystemCode> shipWay() {
        return systemCodeDao.findType("SHIP_WAY");
    }

    /*
     * 交付方式
     */
    public List<SystemCode> delivery() {
        return systemCodeDao.findType("TERMS_OF_DELIVERY");
    }

    /*
     * 客户类型
     */
    public List<SystemCode> type() {
        return systemCodeDao.findType("CLIENT_TEMPLATE_TYPE");
    }

    /*
     * 发票模板
     */
    public List<SystemCode> invoiceTemplet() {
        return systemCodeDao.findType("INVOICE_TEMPLET");
    }

    /*
     * 机型类型
     */
    public void airPage(PageModel<SystemCode> page, String where,
                        GridSort sort) {
        page.put("where", where);
        if (sort != null) {
            sort.setName(ConvertUtil.toDBName(sort.getName()));
            String sotr = ConvertUtil.convertSort(sort);
            page.put("orderby", ConvertUtil.convertSort(sort));
        } else {
            page.put("orderby", null);
        }
        page.setEntities(systemCodeDao.airPage(page));
    }

    /**
     * stockmarket机型管理
     *
     * @param page
     * @param where
     * @param sort
     */
    public void airForStockMarketPage(PageModel<SystemCode> page, String where,
                                      GridSort sort) {
        page.put("where", where);
        if (sort != null) {
            sort.setName(ConvertUtil.toDBName(sort.getName()));
            String sotr = ConvertUtil.convertSort(sort);
            page.put("orderby", ConvertUtil.convertSort(sort));
        } else {
            page.put("orderby", null);
        }
        page.setEntities(systemCodeDao.airForStockMarketPage(page));
    }

    /*
     * 机型代码查询
     */
    public List<SystemCode> selectByCode(PageModel<String> page) {
        return systemCodeDao.selectByCode(page);
    }


    /*
     * 插入机型
     */
    @Override
    public void saveAir(SystemCode systemCode) {
        Integer maxId = systemCodeDao.getMaxId();
        systemCode.setType("AIR_TYPE");
        systemCode.setUpdateTimestamp(new Date());
        systemCode.setId(maxId + 1);
        systemCodeDao.insertSelective(systemCode);

    }

    /*
     * 插入stockmarket机型
     */
    @Override
    public void saveAirForStockMarket(SystemCode systemCode, String userId) {
        systemCode.setType("AIR_TYPE_FOR_STOCK_MARKET");
        systemCode.setUpdateTimestamp(new Date());
        systemCode.setId(systemCodeDao.getMaxId() + 1);
        systemCodeDao.insertSelective(systemCode);
        List<StockMarketSupplierMap> queryIdList = stockMarketSupplierMapDao.getSupplierIdByAirTypeId(1000145);
        for (StockMarketSupplierMap stock : queryIdList) {
            StockMarketSupplierMap stockMarketSupplierMap = new StockMarketSupplierMap();
            stockMarketSupplierMap.setSupplierId(stock.getSupplierId());
            stockMarketSupplierMap.setCreateUser(Integer.parseInt(userId));
            stockMarketSupplierMap.setUpdateTimestamp(new Date());
            stockMarketSupplierMap.setAirTypeId(systemCode.getId());
            stockMarketSupplierMapDao.insertSelective(stockMarketSupplierMap);
        }
    }
    /*
     * 商业类型类型
     */

    public void businessPage(PageModel<SystemCode> page, String where,
                             GridSort sort) {
        page.put("where", where);
        if (sort != null) {
            sort.setName(ConvertUtil.toDBName(sort.getName()));
            String sotr = ConvertUtil.convertSort(sort);
            page.put("orderby", ConvertUtil.convertSort(sort));
        } else {
            page.put("orderby", null);
        }
        page.setEntities(systemCodeDao.businessPage(page));
    }

    /*
     * 商业类型代码查询
     */
    public List<SystemCode> selectByBizCode(String code) {
        return systemCodeDao.selectByBizCode(code);
    }

    /*
     * 插入商业类型
     */
    public void saveBiz(SystemCode systemCode) {
        systemCode.setType("BIZ_TYPE");
        systemCode.setUpdateTimestamp(new Date());
        systemCode.setId(systemCodeDao.getMaxId() + 1);
        systemCodeDao.insertSelective(systemCode);
    }

    @Override
    public List<SystemCode> findType(String type) {
        return systemCodeDao.findType(type);
    }

    @Override
    public List<SystemCode> findTypeSort(String type) {
        return systemCodeDao.findTypeSort(type);
    }

    public List<SystemCode> findTypeSortWithCode(String type) {
        return systemCodeDao.findTypeSortWithCode(type);
    }
}
