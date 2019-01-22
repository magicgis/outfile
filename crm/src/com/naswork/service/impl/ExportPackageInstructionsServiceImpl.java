package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ExportPackageInstructionsDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.model.ExportPackageInstructions;
import com.naswork.model.ImportPackageElement;
import com.naswork.service.ExportPackageInstructionsService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("exportPackageInstructionsServiceImpl")
public class ExportPackageInstructionsServiceImpl implements ExportPackageInstructionsService {

	@Resource
	private ExportPackageInstructionsDao exportPackageInstructionsDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	
	@Override
	public void deleteByPrimaryKey(Integer id) {
		exportPackageInstructionsDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(ExportPackageInstructions record) {
		exportPackageInstructionsDao.insert(record);
	}

	@Override
	public void insertSelective(ExportPackageInstructions record) {
		exportPackageInstructionsDao.insertSelective(record);
	}

	@Override
	public ExportPackageInstructions selectByPrimaryKey(Integer id) {
		return exportPackageInstructionsDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(ExportPackageInstructions record) {
		exportPackageInstructionsDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void updateByPrimaryKey(ExportPackageInstructions record) {
		exportPackageInstructionsDao.updateByPrimaryKey(record);
	}

	@Override
	public void listDataPage(PageModel<ExportPackageInstructions> page, String where, GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(exportPackageInstructionsDao.listDataPage(page));
		
	}

	@Override
	public ExportPackageInstructions findByNumber(String number) {
		return exportPackageInstructionsDao.findByNumber(number);
	}

	@Override
	public void flowLlistDataPage(PageModel<ExportPackageInstructions> page, String where, GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(exportPackageInstructionsDao.flowLlistDataPage(page));
	}
	
	public List<ImportPackageElement> getByInstructionsId(Integer id){
		return importPackageElementDao.getByInstructionsId(id);
	}
	
}
