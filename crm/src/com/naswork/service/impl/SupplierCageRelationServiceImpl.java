package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.SupplierCageRelationDao;
import com.naswork.dao.SupplierDao;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAnnualOffer;
import com.naswork.model.SupplierCageRelationKey;
import com.naswork.model.TManufactory;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.partsinformation.SupplierAbilityVo;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.service.SupplierCageRelationService;
import com.naswork.service.TManufactoryService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("supplierCageRelationService")
public class SupplierCageRelationServiceImpl implements
		SupplierCageRelationService {

	@Resource
	private SupplierCageRelationDao supplierCageRelationDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private TManufactoryService tManufactoryService;
	
	@Override
	public int deleteByPrimaryKey(SupplierCageRelationKey key) {
		return supplierCageRelationDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(SupplierCageRelationKey record) {
		return supplierCageRelationDao.insert(record);
	}

	@Override
	public int insertSelective(SupplierCageRelationKey record) {
		return supplierCageRelationDao.insertSelective(record);
	}
	
	public MessageVo factoryExcel(MultipartFile multipartFile, Integer id){
		boolean success = false;
		String message = "保存成功！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		try {
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
		    //定义行
		    Row row;
		    int line = 2;
		    Supplier supplier = supplierDao.selectByPrimaryKey(id);
		    List<SupplierCageRelationKey> list = new ArrayList<SupplierCageRelationKey>();
		    List<Integer> unKonw = new ArrayList<Integer>();
		    for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null) {
	            	Cell oneCell = row.getCell(0);
		            String cageCode = ""; 
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	cageCode = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						cageCode = dataFormatter.formatCellValue(oneCell);
					}
		            List<TManufactory> list2 = tManufactoryService.getMsnByCageCode(cageCode);
		            if (list2.size() > 0) {
		            	SupplierCageRelationKey supplierCageRelationKey = new SupplierCageRelationKey();
			            supplierCageRelationKey.setMsn(list2.get(0).getMsn());
			            supplierCageRelationKey.setSupplierId(supplier.getId());
			            list.add(supplierCageRelationKey);
					}else {
						unKonw.add(line);
					}
	            }
	            line++;
		    }
		    if (unKonw.size()>0) {
		    	StringBuffer lines = new StringBuffer();
		    	lines.append("Line ");
				for (Integer a : unKonw) {
					lines.append(a).append(",");
				}
				lines.deleteCharAt(lines.length()-1);
				lines.append(" cage_code undefine!");
				String result = lines.toString();
				messageVo.setFlag(success);
				messageVo.setMessage(result);
				return messageVo;
			}else {
				for (SupplierCageRelationKey supplierCageRelationKey : list) {
					int count = supplierCageRelationDao.selectByPrimaryKey(supplierCageRelationKey);
					if (count==0) {
						supplierCageRelationDao.insertSelective(supplierCageRelationKey);
					}
				}
			    success=true;
				message="save successful!";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			message="save unsuccessful!";
		}
		messageVo.setFlag(success);
		messageVo.setMessage(message);
		
		return messageVo;
	}

	public void listPage(PageModel<FactoryVo> page){
		page.setEntities(supplierCageRelationDao.listPage(page));
	}
	
	public void add(String msn,Integer supplierId){
		//List<TManufactory> list = tManufactoryService.getMsnByCageCode(cageCode);
		SupplierCageRelationKey supplierCageRelationKey = new SupplierCageRelationKey();
        supplierCageRelationKey.setMsn(msn);
        supplierCageRelationKey.setSupplierId(supplierId);
        int count = supplierCageRelationDao.selectByPrimaryKey(supplierCageRelationKey);
        if (count==0) {
        	supplierCageRelationDao.insertSelective(supplierCageRelationKey);
        }
	}
	
	public void listByAbilityPage(PageModel<SupplierAbilityVo> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(supplierCageRelationDao.listByAbilityPage(page));
	}
	
	public List<Integer> selectByMsn(String msn){
		return supplierCageRelationDao.selectByMsn(msn);
	}
	
	
}
