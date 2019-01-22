package com.naswork.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierCommissionForStockmarketDao;
import com.naswork.dao.SupplierDao;
import com.naswork.model.Client;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Element;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierCommissionForStockmarket;
import com.naswork.model.SupplierCommissionSale;
import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.model.SystemCode;
import com.naswork.service.SupplierCommissionForStockmarketService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

@Service("supplierCommissionForStockmarketService")
public class SupplierCommissionForStockmarketServiceImpl implements
		SupplierCommissionForStockmarketService {

	@Resource
	private SupplierCommissionForStockmarketDao supplierCommissionForStockmarketDao;
	@Resource
	private SupplierDao supplierDao;
	
	@Override
	public void insertSelective(SupplierCommissionForStockmarket record) {
		Supplier supplier = supplierDao.selectByPrimaryKey(record.getSupplierId());
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(getDateStr(record.getCreateDate()));
		stringBuffer.append("-");
		stringBuffer.append(supplier.getCode());
		record.setNumber(stringBuffer.toString());
		supplierCommissionForStockmarketDao.insertSelective(record);
	}

	@Override
	public SupplierCommissionForStockmarket selectByPrimaryKey(Integer id) {
		return supplierCommissionForStockmarketDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(
			SupplierCommissionForStockmarket record) {
		supplierCommissionForStockmarketDao.updateByPrimaryKeySelective(record);
	}
	
	public void listPage(PageModel<SupplierCommissionForStockmarket> page,GridSort sort){
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierCommissionForStockmarketDao.listPage(page));
	}
	
	
    public String getDateStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		StringBuffer buffer = new StringBuffer();
		buffer.append(getYear(calendar));
		buffer.append(getMonth(calendar));
		buffer.append(getDay(calendar));
		buffer.append(calendar.get(Calendar.HOUR));
		buffer.append(calendar.get(Calendar.MINUTE));
		buffer.append(calendar.get(Calendar.SECOND));
		return buffer.toString();
	}
    
    private String getYear(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		return StringUtils.leftPad(Integer.toString(year), 4, '0');
	}

	private String getMonth(Calendar calendar) {
		int month = calendar.get(Calendar.MONTH) + 1;
		return Integer.toHexString(month).toUpperCase();
	}

	private String getDay(Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return StringUtils.leftPad(Integer.toString(day), 2, '0');
	}
	
	public List<SupplierCommissionForStockmarket> getCrawlStockList(){
		return supplierCommissionForStockmarketDao.getCrawlStockList();
	}

}
