package com.naswork.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientInvoiceDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ExportPackageElementDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.IncomeDao;
import com.naswork.dao.IncomeDetailDao;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.IncomeDetail;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.IncomeDetailService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("incomeDetailService")
public class IncomeDetailServiceImpl implements IncomeDetailService {
	@Resource
	private IncomeDetailDao incomeDetailDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ClientInvoiceDao clientInvoiceDao;
	@Resource
	private IncomeDao incomeDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	@Resource
	private ExportPackageElementDao exportPackageElementDao;
	
	public void insertSelective(List<ClientInvoiceExcelVo> list,Integer incomeId,Integer clientInvoiceId){
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDataItem().equals("on")) {
				ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(list.get(i).getClientOrderElementId());
				Double total = clientOrderElement.getPrice()*list.get(i).getInvoiceAmount()*list.get(i).getElementTerms()*0.01;
				IncomeDetail incomeDetail = new IncomeDetail();
				incomeDetail.setIncomeId(incomeId);
				incomeDetail.setClientOrderElementId(list.get(i).getClientOrderElementId());
				incomeDetail.setAmount(list.get(i).getInvoiceAmount());
				incomeDetail.setTotal(total);
				incomeDetail.setUpdateTimestamp(new Date());
				incomeDetailDao.insertSelective(incomeDetail);
				//确认状态
				ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement.getClientQuoteElementId());
				ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
				IncomeDetail incomeDetail2 = incomeDetailDao.getTotalByClientOrderElementId(clientOrderElement.getId());
				ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
				DecimalFormat df = new DecimalFormat("#.00");
				Double prepayTotal = new Double(df.format(clientOrderElement.getAmount() * clientOrderElement.getPrice() * clientOrder.getPrepayRate()));
				int importCount = importPackageElementDao.getImportCountByCoeId(clientOrderElement.getId());
				Double elementTotal = clientOrderElement.getAmount() * clientOrderElement.getPrice();
				//Double totalIncome = getIncomeTotal(clientOrder.getId());
				if (!"".equals(clientOrderElement.getElementStatusId()) && clientOrderElement.getElementStatusId()!=null) {
					if (clientOrderElement.getElementStatusId().equals(707) || clientOrderElement.getElementStatusId()==707 ||
							clientOrderElement.getElementStatusId().equals(709) || clientOrderElement.getElementStatusId()==709 ||
									clientOrderElement.getElementStatusId().equals(705) || clientOrderElement.getElementStatusId()==705){
						if (clientOrder.getPrepayRate() > 0 && prepayTotal.equals(incomeDetail2.getTotal()) && importCount > 0) {
							clientOrderElement.setElementStatusId(708);
							clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);
						}else if (elementTotal.equals(incomeDetail2.getTotal())) {
							clientOrderElement.setElementStatusId(710);
							clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);
						}
					}
				}
				checkIncome(incomeDetail);
			}
		}
		Double incomeTotal = incomeDao.getIncomeTotal(clientInvoiceId);
		Double clientInvoiceTotal = incomeDao.getInvoiceTotal(clientInvoiceId);
		if (incomeTotal.equals(clientInvoiceTotal)) {
			ClientInvoice clientInvoice = clientInvoiceDao.selectByPrimaryKey(clientInvoiceId);
			clientInvoice.setInvoiceStatusId(1);
			clientInvoiceDao.updateByPrimaryKeySelective(clientInvoice);
		}
		
	}
	
	public Double getIncomeTotal(Integer clientOrderId){
		Double completeTotal = (incomeDao.getCompleteTotal(clientOrderId) != null) ? (incomeDao.getCompleteTotal(clientOrderId)):0;
		Double unCompleteTotal = (incomeDao.getUnCompleteTotal(clientOrderId) != null) ? (incomeDao.getUnCompleteTotal(clientOrderId)) : 0;
		return completeTotal+unCompleteTotal;
	}

	public IncomeDetail selectByPrimaryKey(Integer id){
		return incomeDetailDao.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(IncomeDetail record){
		ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(record.getClientOrderElementId());
		int terms = incomeDetailDao.getTerms(record.getClientOrderElementId());
		Double amount = record.getTotal()/clientOrderElement.getPrice()/terms*100;
		record.setAmount(amount);
		return incomeDetailDao.updateByPrimaryKeySelective(record);
	}
	
	public void getByInvoiceIdPage(PageModel<IncomeDetail> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(incomeDetailDao.getByIncomeIdPage(page));
	}
	
	public void checkIncome(IncomeDetail incomeDetail) {
		ClientOrderElement clientOrderElement = clientOrderElementDao.findByIncomeDetailId(incomeDetail.getId());
		Double incomeTotal = getIncomeTotal(clientOrderElement.getClientOrderId());
		Double orderTotal = incomeDetailDao.getOrderTotal(clientOrderElement.getClientOrderId());
		Double exportAmount = (exportPackageElementDao.getTotalAmountByOrderId(clientOrderElement.getClientOrderId()) != null) ? exportPackageElementDao.getTotalAmountByOrderId(clientOrderElement.getClientOrderId()) : 0;
		Double orderAmount = clientOrderElementDao.getTotalAmount(clientOrderElement.getClientOrderId());
		if (incomeTotal.equals(orderTotal) && orderAmount.equals(exportAmount)) {
			ClientOrder clientOrder = new ClientOrder();
			clientOrder.setId(clientOrderElement.getClientOrderId());
			clientOrder.setOrderStatusId(62);
			clientOrderDao.updateByPrimaryKeySelective(clientOrder);
		}
				
	}
	
	public IncomeDetail getTotalByClientOrderElementId(Integer clientOrderElementId){
		return incomeDetailDao.getTotalByClientOrderElementId(clientOrderElementId);
	}

}
