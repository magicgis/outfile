package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientDao;
import com.naswork.dao.StaticClientQuotePriceDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.StaticClientQuotePrice;
import com.naswork.model.SystemCode;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.GyExcelService;
import com.naswork.service.GyFjService;
import com.naswork.service.StaticClientQuotePriceService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Service("staticClientQuotePriceService")
public class StaticClientQuotePriceServiceImpl implements
		StaticClientQuotePriceService {

	@Resource
	private StaticClientQuotePriceDao staticClientQuotePriceDao;
	@Resource
	private ClientDao clientDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private GyFjService gyFjService;
	
	@Override
	public int insertSelective(StaticClientQuotePrice record) {
		return staticClientQuotePriceDao.insertSelective(record);
	}

	@Override
	public StaticClientQuotePrice selectByPrimaryKey(Integer id) {
		return staticClientQuotePriceDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StaticClientQuotePrice record) {
		return staticClientQuotePriceDao.updateByPrimaryKeySelective(record);
	}

	public void listPage(PageModel<StaticClientQuotePrice> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(staticClientQuotePriceDao.listPage(page));
    }
	
	public MessageVo uploadExcel(MultipartFile multipartFile){
		boolean success = false;
		String message = "保存成功！";
		InputStream fileStream = null;
		try {
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
			//定义行
		    Row row;
			List<StaticClientQuotePrice> elementList = new ArrayList<StaticClientQuotePrice>();
			int ab = sheet.getPhysicalNumberOfRows();
			HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			List<Integer> errorList = new ArrayList<Integer>();
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null) {
	            		StaticClientQuotePrice staticClientQuotePrice = new StaticClientQuotePrice();
	            	 	String code = null;
	            	 	if (row.getCell(0) != null) {
	            	 		Cell oneCell = row.getCell(0);
		            	 	if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	 		code = oneCell.toString().trim();
							}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								code = dataFormatter.formatCellValue(oneCell);
							}
		            	 	Client client = clientDao.findByCode(code);
		            	 	staticClientQuotePrice.setClientId(client.getId());
		            	 	//staticClientQuotePrice.setCurrencyId(client.getCurrencyId());
						}
	            	 	String partNumber = null;
	            	 	if (row.getCell(1) != null) {
	            	 		Cell oneCell = row.getCell(1);
		            	 	if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	 		partNumber = oneCell.toString().trim();
							}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								partNumber = dataFormatter.formatCellValue(oneCell);
							}
		            	 	staticClientQuotePrice.setPartNumber(partNumber);
						}
	            	 	Double price = null;
	            	 	if (row.getCell(2) != null) {
	            	 		Cell oneCell = row.getCell(2);
	            	 		if (dataFormatter.formatCellValue(oneCell) != null && !"".equals(dataFormatter.formatCellValue(oneCell))) {
	            	 			price = new Double(dataFormatter.formatCellValue(oneCell));
	            	 			staticClientQuotePrice.setPrice(price);;
							}
		            	 	
						}
	            	 	if (row.getCell(3) != null) {
	            	 		String currency = row.getCell(3).toString().trim();
	            	 		List<SystemCode> list = systemCodeDao.selectByValue(currency);
	            	 		if (list.size() > 0) {
	            	 			staticClientQuotePrice.setCurrencyId(list.get(0).getId());
							}
	            	 		
						}
	            	 	Integer year = null;
	            	 	if (row.getCell(4) != null) {
	            	 		Cell oneCell = row.getCell(4);
	            	 		if (dataFormatter.formatCellValue(oneCell) != null && !"".equals(dataFormatter.formatCellValue(oneCell))) {
								year = new Integer(dataFormatter.formatCellValue(oneCell));
							}
		            	 	staticClientQuotePrice.setYear(year);;
						}
	            	 	/*String currency = null;
	            	 	if (row.getCell(3) != null) {
	            	 		currency = row.getCell(3).toString().trim();
	            	 		List<SystemCode> systemCodes = systemCodeDao.selectByValue(currency);
	            	 		if (systemCodes.size() ==0) {
								systemCodes = systemCodeDao.selectByCurrencyCode(currency);
							}
	            	 		staticClientQuotePrice.setCurrencyId(systemCodes.get(0).getId());
						}*/
	            	 	
			            
			            //存起读取的数据等待保存进数据库
			            if (!elementList.contains(staticClientQuotePrice)) {
			            	elementList.add(staticClientQuotePrice);
						}
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (elementList.size()>0){
				//存档
				/*excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());*/
				for (StaticClientQuotePrice staticClientQuotePrice : elementList) {
					staticClientQuotePriceDao.insertSelective(staticClientQuotePrice);
				}
				success=true;
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				return messageVo;
			}
		} catch (Exception e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			e.printStackTrace();
			success = false;
			message="save unsuccessful!";
		}
		
		return null;
	}
	
	public ResultVo addWithPage(List<StaticClientQuotePrice> list) {
		try {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCode() != null && !"".equals(list.get(i).getCode())) {
					Client client = clientDao.findByCode(list.get(i).getCode().trim());
					list.get(i).setClientId(client.getId());
					list.get(i).setUpdateTimestamp(new Date());
					staticClientQuotePriceDao.insertSelective(list.get(i));
				}
			}
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增失败！");
		}
	}
	
	public int deleteByPrimaryKey(Integer id){
		return staticClientQuotePriceDao.deleteByPrimaryKey(id);
	}
	
	public List<Integer> getClients(){
		return staticClientQuotePriceDao.getClients();
	}
	
	public int findByClientId(Integer clientId){
		return staticClientQuotePriceDao.findByClientId(clientId);
	}
	
}
