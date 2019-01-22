package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.SupplierCommissionSaleDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.SupplierCommissionSale;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.service.SupplierCommissionSaleService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("supplierCommissionSaleService")
public class SupplierCommissionSaleServiceImpl implements
		SupplierCommissionSaleService {

	@Resource
	private SupplierCommissionSaleDao supplierCommissionSaleDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	
	@Override
	public void insertSelective(SupplierCommissionSale record) {
		supplierCommissionSaleDao.insertSelective(record);
	}

	@Override
	public SupplierCommissionSale selectByPrimaryKey(Integer id) {
		return supplierCommissionSaleDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(SupplierCommissionSale record) {
		supplierCommissionSaleDao.updateByPrimaryKeySelective(record);
		if (record.getSaleStatus().equals(0) || 0==record.getSaleStatus()) {
			SupplierCommissionSale supplierCommissionSale = selectByPrimaryKey(record.getId());
			if (supplierCommissionSale.getClientInquiryId() !=  null) {
				ClientInquiry clientInquiry = new ClientInquiry();
				clientInquiry.setId(supplierCommissionSale.getClientInquiryId());
				clientInquiry.setInquiryStatusId(34);
				clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
				List<Integer> list = supplierQuoteDao.getByClientInquiryId(clientInquiry.getId());
				SupplierQuote supplierQuote = new SupplierQuote();
				for (Integer integer : list) {
					supplierQuote.setId(integer);
					supplierQuote.setQuoteStatusId(91);
					supplierQuoteDao.updateByPrimaryKeySelective(supplierQuote);
				}
			}
		}
	}

	@Override
	public void listPage(PageModel<SupplierCommissionSale> page, String where,
			GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(supplierCommissionSaleDao.listPage(page));
	}
	
	public List<SupplierCommissionSale> getCrawlStockList(){
		return supplierCommissionSaleDao.getCrawlStockList();
	}
	
}
