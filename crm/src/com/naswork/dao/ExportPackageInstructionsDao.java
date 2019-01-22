package com.naswork.dao;

import java.util.List;

import com.naswork.model.ExportPackageInstructions;
import com.naswork.vo.PageModel;

public interface ExportPackageInstructionsDao {
    void deleteByPrimaryKey(Integer id);

    void insert(ExportPackageInstructions record);

    void insertSelective(ExportPackageInstructions record);

    ExportPackageInstructions selectByPrimaryKey(Integer id);
    
    List<ExportPackageInstructions> listDataPage(PageModel<ExportPackageInstructions> page);
    
    List<ExportPackageInstructions> flowLlistDataPage(PageModel<ExportPackageInstructions> page);

    void updateByPrimaryKeySelective(ExportPackageInstructions record);

    void updateByPrimaryKey(ExportPackageInstructions record);
    
    public ExportPackageInstructions findByNumber(String number);
    
}