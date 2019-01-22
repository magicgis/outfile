package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.naswork.module.storage.controller.assetpackage.ArPriceByExcelTemplate;
import com.naswork.module.storage.controller.assetpackage.ArPricePartMappingVo;
import com.naswork.module.storage.controller.assetpackage.ScfseVo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jbpm.pvm.internal.query.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.ArPricePartMappingDao;
import com.naswork.dao.ClientContactDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ElementDao;
import com.naswork.dao.SupplierCommissionForStockmarketDao;
import com.naswork.dao.SupplierCommissionForStockmarketElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ArPricePartMapping;
import com.naswork.model.Client;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Element;
import com.naswork.model.SupplierCommissionForStockmarket;
import com.naswork.model.SupplierCommissionForStockmarketElement;
import com.naswork.model.SupplierCommissionSale;
import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.ClientContactService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientService;
import com.naswork.service.SupplierCommissionForStockmarketElementService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Service("supplierCommissionForStockmarketElementService")
public class SupplierCommissionForStockmarketElementServiceImpl implements
		SupplierCommissionForStockmarketElementService {

	@Resource
	private SupplierCommissionForStockmarketElementDao supplierCommissionForStockmarketElementDao;
	@Resource
	private ClientService clientService;
	@Resource
	private ClientContactDao clientContactDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ElementDao elementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private SupplierCommissionForStockmarketDao supplierCommissionForStockmarketDao;
	@Resource
	private ArPricePartMappingDao arPricePartMappingDao;
	
	@Override
	public void insertSelective(SupplierCommissionForStockmarketElement record) {
		ArPricePartMapping arPricePartMapping = arPricePartMappingDao.selectByPartNumber(record.getPartNumber().trim());
		if (record.getArPrice() != null && !"".equals(record.getArPrice())) {
			ArPricePartMapping arPricePartMappingInsert = new ArPricePartMapping();
			arPricePartMappingInsert.setPartNumber(record.getPartNumber().trim());
			arPricePartMappingInsert.setArPrice(String.valueOf(record.getArPrice()));
			arPricePartMappingInsert.setCreateUserId(new Integer(ContextHolder.getCurrentUser().getUserId()));
			arPricePartMappingDao.insertSelective(arPricePartMappingInsert);
			List<SupplierCommissionForStockmarketElement> list = supplierCommissionForStockmarketElementDao.selectByPartNumber(record.getPartNumber());
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setArPrice(record.getArPrice());
				supplierCommissionForStockmarketElementDao.updateByPrimaryKeySelective(list.get(i));
			}
		}else {
			if (arPricePartMapping != null && arPricePartMapping.getArPrice() != null) {
				record.setArPrice(arPricePartMapping.getArPrice());
			}
		}
		List<SupplierCommissionForStockmarketElement> altList = new ArrayList<SupplierCommissionForStockmarketElement>();
		if (record.getAlt() != null) {
			String[] alts = record.getAlt().split(",");
			if (alts.length > 0 && !"".equals(alts[0])) {
				for (int i = 0; i < alts.length; i++) {
					getAltList(alts, altList, record);
				}
			}
		}
		supplierCommissionForStockmarketElementDao.insertSelective(record);
		//另件号
		for (int i = 0; i < altList.size(); i++) {
			supplierCommissionForStockmarketElementDao.insertSelective(altList.get(i));
		}
	}

	@Override
	public SupplierCommissionForStockmarketElement selectByPrimaryKey(Integer id) {
		return supplierCommissionForStockmarketElementDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(SupplierCommissionForStockmarketElement record) {
		SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement = supplierCommissionForStockmarketElementDao.selectByPrimaryKey(record.getId());
		ArPricePartMapping arPricePartMapping = arPricePartMappingDao.selectByPartNumber(supplierCommissionForStockmarketElement.getPartNumber().trim());
		if (record.getArPrice() != null && !"".equals(record.getArPrice())) {
			ArPricePartMapping arPricePartMappingInsert = new ArPricePartMapping();
			arPricePartMappingInsert.setPartNumber(supplierCommissionForStockmarketElement.getPartNumber().trim());
			arPricePartMappingInsert.setArPrice(String.valueOf(record.getArPrice()));
			arPricePartMappingInsert.setCreateUserId(new Integer(ContextHolder.getCurrentUser().getUserId()));
			arPricePartMappingDao.insertSelective(arPricePartMappingInsert);
			List<SupplierCommissionForStockmarketElement> list = supplierCommissionForStockmarketElementDao.selectByPartNumber(supplierCommissionForStockmarketElement.getPartNumber());
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setArPrice(record.getArPrice());
				supplierCommissionForStockmarketElementDao.updateByPrimaryKeySelective(list.get(i));
			}
		}else {
			if (arPricePartMapping != null && arPricePartMapping.getArPrice() != null) {
				record.setArPrice(arPricePartMapping.getArPrice());
			}
		}
		supplierCommissionForStockmarketElementDao.updateByPrimaryKeySelective(record);
	}
	
	/*
     * excel上传
     */
	/**
	@Author: Modify by white
	@DateTime: 2018/10/11 10:36
	@Description: 修改上传数据
	*/
	/**
	@Author: Modify by white
	@DateTime: 2018/10/15 16:26
	@Description: 去除上传的ar评估价格
	*/
    public MessageVo uploadExcel(MultipartFile multipartFile, Integer id){
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
			List<SupplierCommissionForStockmarketElement> list = new ArrayList<SupplierCommissionForStockmarketElement>();
			List<SupplierCommissionForStockmarketElement> altList = new ArrayList<SupplierCommissionForStockmarketElement>();
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
	            	SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement = new SupplierCommissionForStockmarketElement();
	            	String partNumber = ""; 
	            	if (row.getCell(1) != null) {
	            		Cell oneCell = row.getCell(1);
			            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
							partNumber = oneCell.toString();
						}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						    partNumber = dataFormatter.formatCellValue(oneCell);
						}
					}
		            partNumber = partNumber.trim().replaceAll("\r|\t|\n", "").replaceAll("\\xc2|\\xa0", "").replaceAll(" ", "");
		            supplierCommissionForStockmarketElement.setPartNumber(partNumber);
		            if (row.getCell(0) != null) {
		            	try {
		            		Cell oneCell = row.getCell(0);
			            	HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						    String item = dataFormatter.formatCellValue(oneCell);
							supplierCommissionForStockmarketElement.setItem(new Integer(item));
						} catch (Exception e) {
							e.printStackTrace();
							return new MessageVo(false, (i+1)+"");
						}
					}
		            if (row.getCell(3) != null) {
						String desc = row.getCell(3).toString().trim();
						supplierCommissionForStockmarketElement.setDescription(desc);
					}
		            if (row.getCell(4) != null) {
						String sn = row.getCell(4).toString().trim();
						supplierCommissionForStockmarketElement.setSerialNumber(sn);
					}
		            if (row.getCell(5) != null) {
						String amount = row.getCell(5).toString().trim();
						supplierCommissionForStockmarketElement.setAmount(new Double(amount));
					}
		            if (row.getCell(6) != null) {
						String con = row.getCell(6).toString().trim();
						supplierCommissionForStockmarketElement.setCondition(con);
					}
					if(row.getCell(7) != null){
						String tsn = row.getCell(7).toString().trim();
						supplierCommissionForStockmarketElement.setTsn(tsn);
					}
					if(row.getCell(8) != null){
						String csn = row.getCell(8).toString().trim();
						supplierCommissionForStockmarketElement.setCsn(csn);
					}
					if(row.getCell(9) != null){
						String ata = row.getCell(9).toString().trim();
						supplierCommissionForStockmarketElement.setAta(ata);
					}
		            if (row.getCell(10) != null) {
						String dom = row.getCell(10).toString().trim();
						supplierCommissionForStockmarketElement.setDom(dom);
					}
		            if (row.getCell(11) != null) {
						String man = row.getCell(11).toString().trim();
						supplierCommissionForStockmarketElement.setManufacturer(man);
					}
					if(row.getCell(12) != null){
						String packagePrice = row.getCell(12).toString().trim();
						supplierCommissionForStockmarketElement.setPackagePrice(packagePrice);
					}
//		            if (row.getCell(12) != null) {
//						String ar = row.getCell(12).toString().trim();
//						supplierCommissionForStockmarketElement.setArPrice(new Double(ar));
//					}
		            supplierCommissionForStockmarketElement.setUpdateTimestamp(new Date());
		            supplierCommissionForStockmarketElement.setSupplierCommissionForStockmarketId(id);
		            if (row.getCell(2) != null) {
						String alts = row.getCell(2).toString().trim();
						String[] alt = alts.split(",");
						if (alt.length > 0 && !"".equals(alt[0])) {
							getAltList(alt,altList,supplierCommissionForStockmarketElement);
						}
						supplierCommissionForStockmarketElement.setAlt(alts);
					}
		            list.add(supplierCommissionForStockmarketElement);
	            }
	        }
			for (SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement : list) {
				ArPricePartMapping arPricePartMapping = arPricePartMappingDao.selectByPartNumber(supplierCommissionForStockmarketElement.getPartNumber().trim());
				if (supplierCommissionForStockmarketElement.getArPrice() != null && !"".equals(supplierCommissionForStockmarketElement.getArPrice())) {
					ArPricePartMapping arPricePartMappingInsert = new ArPricePartMapping();
					arPricePartMappingInsert.setPartNumber(supplierCommissionForStockmarketElement.getPartNumber().trim());
					arPricePartMappingInsert.setArPrice(String.valueOf(supplierCommissionForStockmarketElement.getArPrice()));
					arPricePartMappingInsert.setCreateUserId(new Integer(ContextHolder.getCurrentUser().getUserId()));
					arPricePartMappingDao.insertSelective(arPricePartMappingInsert);
					List<SupplierCommissionForStockmarketElement> elementList = supplierCommissionForStockmarketElementDao.selectByPartNumber(supplierCommissionForStockmarketElement.getPartNumber());
					for (int i = 0; i < elementList.size(); i++) {
						elementList.get(i).setArPrice(supplierCommissionForStockmarketElement.getArPrice());
						supplierCommissionForStockmarketElementDao.updateByPrimaryKeySelective(elementList.get(i));
					}
				}else {
					if (arPricePartMapping != null && arPricePartMapping.getArPrice() != null) {
						supplierCommissionForStockmarketElement.setArPrice(arPricePartMapping.getArPrice());
					}
				}
				supplierCommissionForStockmarketElementDao.insertSelective(supplierCommissionForStockmarketElement);
			}
			for (SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement : altList) {
				ArPricePartMapping arPricePartMapping = arPricePartMappingDao.selectByPartNumber(supplierCommissionForStockmarketElement.getPartNumber().trim());
				if (supplierCommissionForStockmarketElement.getArPrice() != null && !"".equals(supplierCommissionForStockmarketElement.getArPrice())) {
					ArPricePartMapping arPricePartMappingInsert = new ArPricePartMapping();
					arPricePartMappingInsert.setPartNumber(supplierCommissionForStockmarketElement.getPartNumber().trim());
					arPricePartMappingInsert.setArPrice(String.valueOf(supplierCommissionForStockmarketElement.getArPrice()));
					arPricePartMappingInsert.setCreateUserId(new Integer(ContextHolder.getCurrentUser().getUserId()));
					arPricePartMappingDao.insertSelective(arPricePartMappingInsert);
					List<SupplierCommissionForStockmarketElement> elementList = supplierCommissionForStockmarketElementDao.selectByPartNumber(supplierCommissionForStockmarketElement.getPartNumber());
					for (int i = 0; i < elementList.size(); i++) {
						elementList.get(i).setArPrice(supplierCommissionForStockmarketElement.getArPrice());
						supplierCommissionForStockmarketElementDao.updateByPrimaryKeySelective(elementList.get(i));
					}
				}else {
					if (arPricePartMapping != null && arPricePartMapping.getArPrice() != null) {
						supplierCommissionForStockmarketElement.setArPrice(arPricePartMapping.getArPrice());
					}
				}
				supplierCommissionForStockmarketElementDao.insertSelective(supplierCommissionForStockmarketElement);
			}
			return new MessageVo(true, (list.size()+altList.size())+"");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				fileStream.close();
			} catch (IOException e1) {
				e.printStackTrace();
				success=false;
				message="save unsuccessful!";
				return new MessageVo(success, message);
			}
			success=false;
			message="save unsuccessful!";
			return new MessageVo(success, message);
		}
    }
    
    public void listPage(PageModel<SupplierCommissionForStockmarketElement> page,GridSort sort){
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierCommissionForStockmarketElementDao.listPage(page));
	}
    
    /**
     * 生成爬虫询价单，并爬去相应的报价信息
     * @param id
     * @return
     */
    @Override
    public ResultVo AddInquiryAndCrawlElement(Integer id,Integer airType){
    	try {
			List<SupplierCommissionForStockmarketElement> list = supplierCommissionForStockmarketElementDao.getDistinctWithStockMarketId(id);
			Client client = clientService.findByCode("-1");
			List<ClientContact> ccList = clientContactDao.selectByClientId(client.getId());
			List<SystemCode> bizs = systemCodeDao.findType("BIZ_TYPE");
			List<SystemCode> airs = systemCodeDao.findType("AIR_TYPE");
			ClientInquiry clientInquiry = new ClientInquiry();
			clientInquiry.setClientId(client.getId());
			clientInquiry.setClientContactId(ccList.get(0).getId());
			clientInquiry.setBizTypeId(bizs.get(0).getId());
			clientInquiry.setAirTypeId(airType);
			clientInquiry.setInquiryDate(new Date());
			clientInquiry.setSourceNumber("资产包");
			clientInquiry.setInquiryStatusId(30);
			clientInquiry.setUpdateTimestamp(new Date());
			clientInquiryService.add(clientInquiry);
			for (int i = 0; i < list.size(); i++) {
				ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
				clientInquiryElement.setClientInquiryId(clientInquiry.getId());
				clientInquiryElement.setPartNumber(list.get(i).getPartNumber());
				clientInquiryElement.setUnit("EA");
				clientInquiryElement.setDescription(list.get(i).getDescription());
				clientInquiryElement.setItem(i+1);
				clientInquiryElement.setCsn(i+1);
				clientInquiryElement.setAmount(new Double(1));
				clientInquiryElement.setSource("资产包");
				List<Element> element = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
			 	Element element2 = new Element();
			 	if (element.size()==0) {
			 		byte[] p = clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()).getBytes();
	            	Byte[] pBytes = new Byte[p.length];
	            	for(int j=0;j<p.length;j++){
	            		pBytes[j] = Byte.valueOf(p[j]);
	            	}
	            	element2.setPartNumberCode(pBytes);
	            	element2.setUpdateTimestamp(new Date());
					elementDao.insert(element2);
					clientInquiryElement.setElementId(element2.getId());
				}else {
					clientInquiryElement.setElementId(element.get(0).getId());
				}
				clientInquiryElementDao.insertSelective(clientInquiryElement);
			}
			
			SupplierCommissionForStockmarket supplierCommissionForStockmarket = new SupplierCommissionForStockmarket();
			supplierCommissionForStockmarket.setId(id);
			supplierCommissionForStockmarket.setClientInquiryId(clientInquiry.getId());
			supplierCommissionForStockmarket.setCrawlStatus(1);
			supplierCommissionForStockmarketDao.updateByPrimaryKeySelective(supplierCommissionForStockmarket);
			return new ResultVo(true, "发起成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "发起异常！");
		}
    }
    
    public List<SupplierCommissionForStockmarketElement> selectByForeign(Integer id){
    	return supplierCommissionForStockmarketElementDao.selectByForeignKey(id);
    }
    
    public void getAltList(String[] alt,List<SupplierCommissionForStockmarketElement> list,SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement){
    	for (int j = 0; j < alt.length; j++) {
			if (alt[j] != null) {
				SupplierCommissionForStockmarketElement altElement = new SupplierCommissionForStockmarketElement();
				altElement.setPartNumber(alt[j].trim());
				altElement.setIsReplace(1);
				if (supplierCommissionForStockmarketElement.getArPrice() != null) {
					altElement.setArPrice(supplierCommissionForStockmarketElement.getArPrice());
				}
				if (supplierCommissionForStockmarketElement.getManufacturer()!= null) {
					altElement.setManufacturer(supplierCommissionForStockmarketElement.getManufacturer());
				}
				if (supplierCommissionForStockmarketElement.getDom() != null) {
					altElement.setDom(supplierCommissionForStockmarketElement.getDom());
				}
				if (supplierCommissionForStockmarketElement.getCondition() != null) {
					altElement.setCondition(supplierCommissionForStockmarketElement.getCondition());
				}
				if (supplierCommissionForStockmarketElement.getAmount() != null) {
					altElement.setAmount(supplierCommissionForStockmarketElement.getAmount());
				}
				if (supplierCommissionForStockmarketElement.getSerialNumber() != null) {
					altElement.setSerialNumber(supplierCommissionForStockmarketElement.getSerialNumber());
				}
				if (supplierCommissionForStockmarketElement.getDescription() != null) {
					altElement.setDescription(supplierCommissionForStockmarketElement.getDescription());
				}
				if (supplierCommissionForStockmarketElement.getItem()!= null) {
					altElement.setItem(supplierCommissionForStockmarketElement.getItem());
				}
				if (supplierCommissionForStockmarketElement.getSupplierCommissionForStockmarketId()!= null) {
					altElement.setSupplierCommissionForStockmarketId(supplierCommissionForStockmarketElement.getSupplierCommissionForStockmarketId());
				}
				list.add(altElement);
			}
		}
    }

	@Override
    public Double getCrawlPercent(Integer id){
    	return supplierCommissionForStockmarketElementDao.getCrawlPercent(id);
    }

	@Override
	public List<ScfseVo> getListData(PageModel<ScfseVo> pageModel) {
		return supplierCommissionForStockmarketElementDao.getListData(pageModel);
	}

	@Override
	public String getMainPricesByPartNumber(String partNum) {
		return supplierCommissionForStockmarketElementDao.getMainPricesByPartNumber(partNum);
	}

	@Override
	public List<String> getNewPricesByPartNumber(PageModel<String> page) {
		return supplierCommissionForStockmarketElementDao.getNewPricesByPartNumber(page);
	}

	@Override
	public 	List<String> getOldPricesByPartNumber(String partNum) {
		return supplierCommissionForStockmarketElementDao.getOldPricesByPartNumber(partNum);
	}

	@Override
	public String getMroRepairByPartNumber(String partNum) {
		return supplierCommissionForStockmarketElementDao.getMroRepairByPartNumber(partNum);
	}

	@Override
	public String getMroOverhaulByPartNumber(String partNum) {
		return supplierCommissionForStockmarketElementDao.getMroOverhaulByPartNumber(partNum);
	}

	/*
	 * @Author: Create by white
	 * @Datetime: 2018/10/12 11:05
	 * @Descrition: uploadArPriceExcel 用于通过模板上传Ar价格
	 * @Params: [multipartFile]
	 * @Return: com.naswork.module.marketing.controller.clientinquiry.MessageVo
	 * @Throws:
	 */
    @Override
    public MessageVo uploadArPriceExcel(MultipartFile multipartFile) {
		boolean success = false;
		String message = "保存成功！";
		InputStream fileStream = null;
		try{
			//获取excel的数据进行
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
			//获取第一个工作簿
			Sheet sheet = wb.getSheetAt(0);
			//定义行
			Row row;
			//循环遍历每一行 除了第一行表头之外
			for(int i = sheet.getFirstRowNum()+1;i<sheet.getPhysicalNumberOfRows();i++){
				ArPricePartMapping arPricePartMapping = new ArPricePartMapping();
				arPricePartMapping.setCreateUserId(Integer.parseInt(ContextHolder.getCurrentUser().getUserId()));
				row = sheet.getRow(i);
				//判断行的数据是否为空 不为空则将封装成对象
				if (row != null){
					//件号
					if(row.getCell(0)!= null && !"".equals(row.getCell(0).toString().trim())){
						arPricePartMapping.setPartNumber(row.getCell(0).toString().trim());
					}
					//描述
					if(row.getCell(1)!=null && !"".equals(row.getCell(1).toString().trim())){
						arPricePartMapping.setDescription(row.getCell(1).toString().trim());
					}
					//dom
					if(row.getCell(2)!=null && !"".equals(row.getCell(2).toString().trim())){
						arPricePartMapping.setDom(row.getCell(2).toString().trim());
					}
					//ar评估价
					if(row.getCell(3)!=null && !"".equals(row.getCell(3).toString().trim())){
						arPricePartMapping.setArPrice(row.getCell(3).toString().trim());
					}else{
						arPricePartMapping.setArPrice("0.0");
					}
					//ar销售价
					if(row.getCell(4)!=null && !"".equals(row.getCell(4).toString().trim())){
						arPricePartMapping.setArSalePrice(row.getCell(4).toString().trim());
					}else{
						arPricePartMapping.setArSalePrice("0.0");
					}
				}
				arPricePartMappingDao.insertSelective(arPricePartMapping);
			}
			int count = sheet.getPhysicalNumberOfRows()-1;
			return  new MessageVo(true,String.valueOf(count));
		}catch (Exception e){
			e.printStackTrace();
			return new MessageVo(false,"导入失败");
		}

    }


}
