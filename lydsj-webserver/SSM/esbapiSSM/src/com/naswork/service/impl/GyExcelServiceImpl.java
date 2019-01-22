package com.naswork.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.naswork.dao.GyExcelDao;
import com.naswork.model.gy.GyExcel;
import com.naswork.service.GyExcelService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.ExcelGeneratorBase;
import com.naswork.utils.excel.ExcelGeneratorMapConstant;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

import javax.annotation.Resource;
@Service("gyExcelService")
public class GyExcelServiceImpl implements GyExcelService {
	@Resource
	private GyExcelDao gyExcelDao;
	
	@Override
	public GyExcel findById(String excelFileId) {
		
		return gyExcelDao.findById(excelFileId);
	}

	@Override
	public void add(GyExcel gyExcel) {		
		gyExcelDao.insert(gyExcel);		
	}

	@Override
	public void modify(GyExcel gyExcel) {
		gyExcelDao.update(gyExcel);

	}

	@Override
	public void remove(String excelFileId) {
		GyExcel excel = gyExcelDao.findById(excelFileId);
		if(excel!=null){
			gyExcelDao.delete(excelFileId);
			ExcelGeneratorBase.deleteFile(excel.getExcelFilePath());
		}
	}

	@Override
	public void searchPage(PageModel<GyExcel> page, String businessKey, GridSort sort) {
		String[] parameters = businessKey.split("\\.");
		page.put("ywTableName", parameters[0]);
		page.put("ywTablePkName", parameters[1]);
		boolean isNum = parameters[2].matches("[0-9]+");   
		if(isNum){
			page.put("ywId", parameters[2]);
		}else{
			String[] ids=parameters[2].split("-");
			if(ids[0].equals("0")){
			page.put("ywId", 0);
			}else{
				String Id = ids[0].replaceAll(",", "");
				page.put("ywId",Id);
			}
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(gyExcelDao.findPage(page));
	}

	@Override
	public GyExcel findLatestExcel(PageModel<GyExcel> page) {
		return gyExcelDao.findLatestExcel(page);
	}

	@Override
	public List<GyExcel> findByIds(String[] idarr) {
		StringBuffer idsb = new StringBuffer();
		for (String id : idarr) {
			idsb.append("'").append(id).append("',");
		}
		String ids = idsb.toString().replaceAll(",$", "");
		return gyExcelDao.findByIds(ids);
	}

	@Override
	public void removeExcelByIds(String ids) {
		String[] idarr = ids.split(",");
		for (String id : idarr) {
			remove(id);
		}
	}

	@Override
	public String generateExcel(String businessKey) throws FileNotFoundException, IOException {
		String[] parameters = businessKey.split("\\.");
		String excelTemplateName = parameters[3];
		String ywId = parameters[2];
		String currencyId="";
		if(parameters.length>4){
			 currencyId= parameters[4];
		}
		ExcelGeneratorBase generator = ExcelGeneratorMapConstant.EXCEL_GENERATOR_MAP.get(excelTemplateName);
		return generator.generate(ywId,currencyId);
	}

}
