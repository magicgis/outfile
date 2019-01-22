package com.naswork.dao;

import java.util.List;

import com.naswork.model.gy.GyExcel;
import com.naswork.vo.PageModel;

public interface GyExcelDao {

	public GyExcel findById(String excelFileId);
	
	public int insert(GyExcel gyExcel);
	
	public int update(GyExcel gyExcel);
	
	public int delete(String excelFileId);
	
	List<GyExcel> findPage(PageModel<GyExcel> page);
	
	List<GyExcel> findByIds(String ids);
	
	public GyExcel findLatestExcel(PageModel<GyExcel> page);
}
