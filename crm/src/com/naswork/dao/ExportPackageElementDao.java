package com.naswork.dao;

import java.util.List;

import com.naswork.model.ExportPackageElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.vo.PageModel;

public interface ExportPackageElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ExportPackageElement record);

    int insertSelective(ExportPackageElement record);
    
    Double  selectByIpeId(Integer id);

    ExportPackageElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExportPackageElement record);

    int updateByPrimaryKey(ExportPackageElement record);
    
    public List<ExportPackageElementVo> listPage(PageModel<ExportPackageElementVo> page);
    
    public List<ExportPackageElementVo> Elements(Integer exportPackageId);
    
    public List<ExportPackageElementVo> addElementPage(PageModel<ExportPackageElementVo> page);
    
    List<ExportPackageElementVo> exportPackageInstructionsPage(PageModel<ExportPackageElementVo> page);
    
    Double BoxWeight(ImportPackageElement parame);
    
    public void updateByExportPackageId(ExportPackageElement exportPackageElement);
    
    public List<Integer> findByExportId(Integer exportPackageId);
    
    public List<ExportPackageElementVo> getClientOrderId(Integer exportPackageId);
    
    public List<ExportPackageElementVo> getCountWithLocation(Integer exportPackageId);
    
    public List<ImportPackageElement> getElementByLocation(String location);
    
    public List<ImportPackageElement> getLackPage(PageModel<ImportPackageElement> page);
    
    List<ExportPackageElementVo> findClientOrderNumber(PageModel<ExportPackageElementVo> page);
    
    ExportPackageElementVo findEpeAmount(Integer  exportPackageId,Integer clientOrderId);
    
    List<ExportPackageElementVo> findByEpidAndCoid(String  exportPackageId,String clientOrderId);
    
    public List<ExportPackageElementVo> getImportElement(PageModel<ExportPackageElementVo> page);
    
    public ExportPackageElement getImportPackageElementId(PageModel<ExportPackageElement> page);
    
    public int getCLientOrderElementCount(Integer clientOrderId);
    
    public int getExportPackageElementCount(Integer clientOrderId);
    
    public List<ExportPackageElementVo> selectByInstructionsId(Integer instructionsId);
    
    List<ExportPackageElementVo> findBoxWeight(Integer id);
    		
    public List<ExportPackageElement> selectByExportId(Integer export);
    
    public List<ExportPackageElement> getByExportElementByOrderId(Integer clientOrderElementId,Integer importPackageId);
    
    public Double getTotalAmountByOrderId(Integer clientOrderId);
    
}