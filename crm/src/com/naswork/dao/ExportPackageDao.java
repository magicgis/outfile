package com.naswork.dao;

import java.util.List;

import com.naswork.model.ExportPackage;
import com.naswork.module.storage.controller.exportpackage.ExportPackageVo;
import com.naswork.vo.PageModel;

public interface ExportPackageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ExportPackage record);

    int insertSelective(ExportPackage record);

    ExportPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExportPackage record);

    int updateByPrimaryKey(ExportPackage record);
    
    public List<ExportPackageVo> listPage(PageModel<ExportPackageVo> page);
    
    public Integer getMaxSeq(ExportPackage exportPackage);
    
    ExportPackage findByCidAndexportDate(ExportPackageVo exportPackageVo);
    
    List<ExportPackage> findStorageExport(ExportPackage record);
    
    public List<ExportPackage> getByIpeId(Integer importPackageElementId);
    
    public ExportPackage getToTalAmount(Integer exportPackageId);
}