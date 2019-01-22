package com.naswork.service;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.naswork.model.gy.GyExcel;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
public interface GyExcelService {

	public GyExcel findById(String excelFileId);
	
	public void add(GyExcel gyExcel);
	
	public void modify(GyExcel gyExcel);
	
	public void remove(String excelFileId);
	
	public void removeExcelByIds(String ids);
	
	public void searchPage(PageModel<GyExcel> page, String parameter, GridSort sort);
	
	public List<GyExcel> findByIds(String[] idarr);
	
	public GyExcel findLatestExcel(PageModel<GyExcel> page);
	
	public String generateExcel(String businessKey) throws FileNotFoundException, IOException;
}
