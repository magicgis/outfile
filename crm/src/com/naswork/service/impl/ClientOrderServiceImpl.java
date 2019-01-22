package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientorder.orderReview;
import com.naswork.module.statistics.controller.StatisticsVo;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ExchangeRate;
import com.naswork.service.ClientOrderService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("clientOrderService")
public class ClientOrderServiceImpl implements ClientOrderService {
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	
	@Override
	public void add(ClientOrder clientOrder) {
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(clientOrder.getCurrencyId());
    	clientOrder.setExchangeRate(exchangeRate.getRate());
		clientOrderDao.insert(clientOrder);
	}
	
	public ClientOrder selectByPrimaryKey(Integer id){
		return clientOrderDao.selectByPrimaryKey(id);
	}
	
	/*
     * 列表页面数据
     */
    public void listPage(PageModel<ClientOrderVo> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientOrderDao.listPage(page));
    }
    
    /*
     * 根据id查询
     */
    public ClientOrderVo findById(Integer id){
    	return clientOrderDao.findById(id);
    }
    
    /*
     * 更新
     */
    public void updateByPrimaryKeySelective(ClientOrder clientOrder){
    	if (clientOrder.getPrepayRate()!=null && clientOrder.getReceivePayRate()!=null && clientOrder.getShipPayRate()!=null) {
    		clientOrder.setPrepayRate(clientOrder.getPrepayRate()/100);
        	clientOrder.setReceivePayRate(clientOrder.getReceivePayRate()/100);
        	clientOrder.setShipPayRate(clientOrder.getShipPayRate()/100);
        	ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(clientOrder.getCurrencyId());
        	clientOrder.setExchangeRate(exchangeRate.getRate());
		}
    	clientOrderDao.updateByPrimaryKeySelective(clientOrder);
			if (null!=clientOrder.getOrderStatusId()&&(clientOrder.getOrderStatusId().equals(64) || clientOrder.getOrderStatusId().equals(1000093))) {
			List<ClientOrderElement> list = clientOrderElementDao.selectByCLientOrderId(clientOrder.getId());
			List<ClientInquiryElement> inquiryList = clientInquiryElementDao.selectByClientOrderId(clientOrder.getId());
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setOrderStatusId(64);
				list.get(i).setElementStatusId(711);
				clientOrderElementDao.updateByPrimaryKeySelective(list.get(i));
			}
			/*for (int i = 0; i < inquiryList.size(); i++) {
				inquiryList.get(i).setElementStatusId(711);
				clientInquiryElementDao.updateByPrimaryKeySelective(inquiryList.get(i));
			}*/
		}
    }
    
    
    /*
     * 根据单号找Id
     */
    public Integer findIdByOrderNumber(String orderNumber){
    	return clientOrderDao.findIdByOrderNumber(orderNumber);
    }

	@Override
	public ClientOrderElementVo findByCoIdAndCqeId(Integer coId, Integer cqeId) {
		return clientOrderDao.findByCoIdAndCqeId(coId, cqeId);
	}

	@Override
	public void dueReminlistPage(PageModel<ClientOrderVo> page, String where, GridSort sort) {
		if(null!=where&&!"".equals(where)){
		page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientOrderDao.dueReminlistPage(page)); 
	}

	@Override
	public ClientOrder lookOrderByImportPacksge(ClientQuote cq, Integer clientQuoteId, Integer currencyId,
			Double exchangeRate) {
		ClientOrder clientOrder=clientOrderDao.findOrderByImportPacksge(clientQuoteId);
		if(null==clientOrder){
			clientOrder=new ClientOrder();
			clientOrder.setClientQuoteId(clientQuoteId);
			clientOrder.setCurrencyId(currencyId);
			clientOrder.setExchangeRate(exchangeRate);
			Calendar calendar = Calendar.getInstance();
			clientOrder.setOrderDate(calendar.getTime());
			String storageSourceNumber = getStorageSourceNumber(calendar
					.get(Calendar.YEAR));
			clientOrder.setSourceNumber(storageSourceNumber);
			clientOrder.setSeq(1);
			clientOrder.setOrderNumber(storageSourceNumber);
			clientOrder.setTerms(100);
			clientOrder.setOrderStatusId(63);
			clientOrder.setRemark("STORAGE");
			clientOrderDao.insertSelective(clientOrder);
		}
		return clientOrder;
	}
	
	private String getStorageSourceNumber(int year) {
		return "ORD-KC-" + year;
	}
    
    /*
     * excel上传
     */
    /*public MessageVo UploadExcel(MultipartFile multipartFile, Integer id){
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
			//记录错误行数
			List<Integer> list = new ArrayList<Integer>();
			List<ClientInquiryElement> elementList = new ArrayList<ClientInquiryElement>();
			
			//错误行数集合
			StringBuffer lines=new StringBuffer();
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            Double a = new Double(row.getCell(0).toString());
	            Integer item = a.intValue();
	            String partNumber = row.getCell(1).toString();
	            String description = row.getCell(2).toString();
	            String unit = row.getCell(3).toString();
	            Double amount = new Double(row.getCell(4).toString());
	            String remark = row.getCell(5).toString();
			}
		}catch (Exception e) {
			
		}
    }*/
	
    
    /*
     * 查询未发货数量
     */
    public List<ClientOrderVo> getImportAmount(Integer clientId){
    	List<ClientOrderVo> importElement =  clientOrderDao.getImportAmount(clientId);
    	List<ClientOrderVo> orderElement = clientOrderDao.getOrderAmount(clientId);
    	for (int i = 0; i < orderElement.size(); i++) {
			for (int j = 0; j < importElement.size(); j++) {
				if (orderElement.get(i).getId().equals(importElement.get(j).getId())) {
					if (orderElement.get(i).getOrderAmount().equals(importElement.get(j).getOrderAmount())) {
						orderElement.remove(i);
						i=0;
					}else {
						BigDecimal b1 = new BigDecimal(Double.toString(orderElement.get(i).getOrderAmount()));
						BigDecimal b2 = new BigDecimal(Double.toString(importElement.get(j).getOrderAmount()));
						Double amount = b1.subtract(b2).doubleValue();
						orderElement.get(i).setOrderAmount(amount);
					}
					break;
				}
			}
		}
    	return orderElement;
    }
    
    /*
     * 获取订单Id
     */
    public List<ClientOrder> getOrderId(Integer clientId){
    	return clientOrderDao.getOrderId(clientId);
    }
    
    public int statisticsPage(PageModel<StatisticsVo> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		List<StatisticsVo> list = clientOrderDao.statistics(page);
		/*List<StatisticsVo> quoteList = clientOrderDao.getQuote();
		List<StatisticsVo> orderList = clientOrderDao.getOrder();
		List<StatisticsVo> exportList = clientOrderDao.getExport();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < quoteList.size(); j++) {
				if (list.get(i).getClientId().equals(quoteList.get(j).getClientId()) &&
						list.get(i).getAirTypeId().equals(quoteList.get(j).getAirTypeId()) &&
							list.get(i).getBizTypeId().equals(quoteList.get(j).getBizTypeId())) {
					list.get(i).setClientQuoteCount(quoteList.get(j).getClientQuoteCount());
					list.get(i).setClientQuoteSum(quoteList.get(j).getClientQuoteSum());
				}
			}
			
			for (int j = 0; j < orderList.size(); j++) {
				if (list.get(i).getClientId().equals(orderList.get(j).getClientId()) &&
						list.get(i).getAirTypeId().equals(orderList.get(j).getAirTypeId()) &&
							list.get(i).getBizTypeId().equals(orderList.get(j).getBizTypeId())) {
					list.get(i).setClientOrderCount(orderList.get(j).getClientOrderCount());
					list.get(i).setClientOrderSum(orderList.get(j).getClientOrderSum());
				}
			}
			
			for (int j = 0; j < exportList.size(); j++) {
				if (list.get(i).getClientId().equals(exportList.get(j).getClientId()) &&
						list.get(i).getAirTypeId().equals(exportList.get(j).getAirTypeId()) &&
							list.get(i).getBizTypeId().equals(exportList.get(j).getBizTypeId())) {
					list.get(i).setClientExportCount(exportList.get(j).getClientExportCount());
					list.get(i).setClientExportSum(exportList.get(j).getClientExportSum());
				}
			}
		}*/
		double inquiryCount = 0.0;
		double quoteCount = 0.0;
		double quoteSum = 0.0;
		double orderCount = 0.0;
		double orderSum = 0.0;
		double exportCount = 0.0;
		double exportSum = 0.0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getClientQuoteCount()!=null) {
				BigDecimal   b1   =   new   BigDecimal(Double.toString(quoteCount));   
                BigDecimal   b2   =   new   BigDecimal(Double.toString(list.get(i).getClientQuoteCount()));   
                quoteCount = b1.add(b2).doubleValue();   
				quoteCount += list.get(i).getClientQuoteCount();
			}
			if (list.get(i).getClientInquiryCount()!=null) {
				inquiryCount += list.get(i).getClientInquiryCount();			
			}
			if (list.get(i).getClientQuoteSum()!=null) {
				quoteSum += list.get(i).getClientQuoteSum();
			}
			if (list.get(i).getClientOrderCount()!=null) {
				orderCount += list.get(i).getClientOrderCount();
			}
			if (list.get(i).getClientOrderSum()!=null) {
				orderSum += list.get(i).getClientOrderSum();
			}
			if (list.get(i).getClientExportCount()!=null) {
				exportCount += list.get(i).getClientExportCount();
			}
			if (list.get(i).getClientExportSum()!=null) {
				exportSum += list.get(i).getClientExportSum();
			}
		}
		StatisticsVo statisticsVo = new StatisticsVo();
		statisticsVo.setBizTypeCode("总计：");
		statisticsVo.setClientInquiryCount(inquiryCount);
		statisticsVo.setClientQuoteCount(quoteCount);
		statisticsVo.setClientQuoteSum(quoteSum);
		statisticsVo.setClientOrderCount(orderCount);
		statisticsVo.setClientOrderSum(orderSum);
		statisticsVo.setClientExportCount(exportCount);
		statisticsVo.setClientExportSum(exportSum);
		list.add(statisticsVo);
		page.setEntities(list);
		return list.size();
    }
    
    /*
     *获取出库信息 
     */
    public ClientOrder getExportMessage(Integer clientOrderId){
    	return clientOrderDao.getExportMessage(clientOrderId);
    }
    
    /**
     * 根据明细Id获取clientOrder
     */
    public ClientOrder getClientOrder(Integer clientOrderElementId){
    	return clientOrderDao.getClientOrder(clientOrderElementId);
    }

	@Override
	public List<orderReview> orderReviewData(Integer clientOrderId) {
		return clientOrderDao.orderReviewData(clientOrderId);
	}
	
	public List<ClientOrder> selectByClientInquiryId(Integer clientInquiryId){
		return clientOrderDao.selectByClientInquiryId(clientInquiryId);
	}

	@Override
	public Integer selectSupplierId(Integer clientOrderElementId) {
		return clientOrderDao.selectSupplierId(clientOrderElementId);
	}

	@Override
	public Double getOrderPrice(Integer clientQuoteId) {
		return clientOrderDao.getOrderPrice(clientQuoteId);
	}
	
	public Double getIncomeTotalByOrderId(Integer id){
		return clientOrderDao.getIncomeTotalByOrderId(id);
	}
	
	public List<Date> getExportDates(Integer id){
		return clientOrderDao.getExportDates(id);
	}
}
