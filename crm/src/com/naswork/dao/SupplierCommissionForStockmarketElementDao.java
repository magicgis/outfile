package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierCommissionForStockmarketElement;
import com.naswork.module.storage.controller.assetpackage.ScfseVo;
import com.naswork.vo.PageModel;

import org.apache.ibatis.annotations.Param;

public interface SupplierCommissionForStockmarketElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierCommissionForStockmarketElement record);

    int insertSelective(SupplierCommissionForStockmarketElement record);

    SupplierCommissionForStockmarketElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierCommissionForStockmarketElement record);

    int updateByPrimaryKey(SupplierCommissionForStockmarketElement record);
    
    public List<SupplierCommissionForStockmarketElement> listPage(PageModel<SupplierCommissionForStockmarketElement> page);
    
    public List<SupplierCommissionForStockmarketElement> selectByForeignKey(Integer id);
    
    public List<SupplierCommissionForStockmarketElement> getDistinctWithStockMarketId(Integer id);
    
    public List<SupplierCommissionForStockmarketElement> selectByPartNumber(String partNumber);
    
    public List<SupplierCommissionForStockmarketElement> getNotReplaceRecord(Integer id);
    
    public Double getCrawlPercent(Integer id);

    List<ScfseVo> getListData(PageModel<ScfseVo> pageModel);

    String getMainPricesByPartNumber(@Param("partNum") String partNum);

	List<String> getOldPricesByPartNumber(@Param("partNum") String partNum);

    List<String> getNewPricesByPartNumber(PageModel<String> page);

    String getMroRepairByPartNumber(@Param("partNum") String partNum);

    String getMroOverhaulByPartNumber(@Param("partNum") String partNum);

}