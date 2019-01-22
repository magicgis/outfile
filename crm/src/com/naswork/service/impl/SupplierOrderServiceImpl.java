package com.naswork.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.naswork.dao.ClientDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.dao.ImportPackagePaymentElementDao;
import com.naswork.dao.SeqKeyDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierDebtDao;
import com.naswork.dao.SupplierOrderDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.UserDao;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.SeqKey;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierDebt;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.Address;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.StorageOrderVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SystemCodeService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;

@Service("supplierOrderService")
public class SupplierOrderServiceImpl implements SupplierOrderService {

	@Resource
	private SupplierOrderDao supplierOrderDao;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource 
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private SeqKeyDao seqKeyDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ClientDao clientDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private ClientQuoteDao clientQuoteDao;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ImportPackagePaymentDao importPackagePaymentDao;
	@Resource
	private SupplierDebtDao supplierDebtDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ImportPackagePaymentElementDao importPackagePaymentElementDao;
	@Resource
	private UserDao userDao;
	
	
	
	public int insertSelective(SupplierOrder record) {
		try {
			ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(record.getClientOrderId());
			Double terms = clientOrder.getPrepayRate()*100;
			record.setTerms(terms.intValue());
			record.setOrderNumber(getOrderNumberSeq(record.getOrderDate()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		record.setOrderStatusId(1000090);
		return supplierOrderDao.insertSelective(record);
	}

	public SupplierOrder selectByPrimaryKey(Integer id) {
		return supplierOrderDao.selectByPrimaryKey(id);
	}
	
	public void updateByPrimaryKeySelective(SupplierOrder record) {
		
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getCurrencyId());
		record.setExchangeRate(exchangeRate.getRate());
		SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(record.getId());
		Integer statusId = supplierOrder.getOrderStatusId();
		supplierOrderDao.updateByPrimaryKeySelective(record);
		
		if (record.getOrderStatusId().equals(64)) {
			double total = 0.0;
			boolean ifCommit = false;
			List<ImportPackagePayment> list = importPackagePaymentDao.selectBySupplierOrderId(record.getId());
			List<SupplierOrderElement> elements = supplierOrderElementDao.selectBySupplierOrderIdAll(record.getId());
			List<ClientInquiryElement> inquiryList = clientInquiryElementDao.selectBySupplierOrderId(record.getId());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getPaymentStatusId().equals(231)) {
					total = total + list.get(i).getPaymentTotal();
					ifCommit = true;
				}
				
			}
			for (int i = 0; i < elements.size(); i++) {
				elements.get(i).setOrderStatusId(64);
				ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(elements.get(i).getClientOrderElementId());
				/*clientOrderElement.setElementStatusId(712);
				clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);*/
				supplierOrderElementDao.updateByPrimaryKeySelective(elements.get(i));
			}
			if (ifCommit) {
				SupplierDebt supplierDebt = new SupplierDebt();
				supplierDebt.setSupplierOrderId(record.getId());
				supplierDebt.setTotal(total);
				supplierDebtDao.insertSelective(supplierDebt);
			}
			
		}else if (statusId.equals(64) && !record.equals(64)) {
			List<SupplierOrderElement> elements = supplierOrderElementDao.selectBySupplierOrderIdAll(record.getId());
			for (int i = 0; i < elements.size(); i++) {
				elements.get(i).setOrderStatusId(60);
				supplierOrderElementDao.updateByPrimaryKeySelective(elements.get(i));
			}
		}
	}
	
	/*
	 * 生成订单号
	 */
	public String getOrderNumberSeq(Date date) throws SQLException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ORD-");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		String yearKey = Integer.toString(year % 100);
		buffer.append(StringUtils.leftPad(yearKey, 2, '0'));
		SeqKey seqKey = new SeqKey();
		seqKey.setKeyName(yearKey);
		seqKey.setSchemaName("crm");
		seqKey.setTableName("supplier_order");
		SeqKey seqKey2 = seqKeyDao.findMaxSeq(seqKey);
		Integer seqNumber=null;
		if (seqKey2!=null) {
			seqNumber = seqKey2.getSeq().intValue();
			seqNumber++;
		}
		if(seqKey2==null){
			seqKey.setUpdateTimestamp(new Date());
			seqKey.setSeq(1001);
			seqNumber=1001;
			seqKeyDao.insertSelective(seqKey);
			buffer.append(StringUtils.leftPad(Integer.toString(seqNumber), 6, '0'));
			return buffer.toString();
		}
		if (seqNumber != seqKey2.getSeq()) {
			seqKey.setSeq(seqNumber);
			seqKeyDao.updateByPrimaryKeySelective(seqKey);
			buffer.append(StringUtils.leftPad(Integer.toString(seqNumber), 6, '0'));
		}
		
		return buffer.toString();
	}
	
    /*
     * 新增供应商订单明细列表
     */
    /*public void AddSupplierOrderPage(PageModel<AddSupplierOrderElementVo> page){
    	page.setEntities(supplierOrderElementDao.AddSupplierOrderPage(page));
    }*/
	
    /*
     * 新增供应商订单明细列表-客户订单模块
     */
    public void ClientOrderPage(PageModel<ClientOrderElementVo> page){
    	page.setEntities(supplierOrderElementDao.ClientOrderPage(page));
    }
    
    /*
     * 获取
     */
    
    /*
     * 新增供应商订单明细列表
     */
    public List<Map<String, Object>> AddSupplierOrderPage(Integer id,String userId) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		PageModel<AddSupplierOrderElementVo> page = new PageModel<AddSupplierOrderElementVo>();

		page.put("id", id);
		
		RoleVo roleVo = userDao.getPower(new Integer(userId));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userId);
		}

		List<AddSupplierOrderElementVo> list = supplierOrderElementDao.AddSupplierOrderPage(page);

		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < list.size(); i++) {

			AddSupplierOrderElementVo supplierOrderElement = list.get(i);

			Map<String, Object> map = new HashMap<String, Object>();

			String[] cell = {
					supplierOrderElement.getSupplierOrderNumber(),
					supplierOrderElement.getSupplierCode(),
					supplierOrderElement.getOrderDate() != null ? sdf.format(supplierOrderElement.getOrderDate()) : "",
					String.valueOf(supplierOrderElement.getSupplierOrderAmount()),
					String.valueOf(supplierOrderElement.getSupplierOrderPrice()),
					String.valueOf(supplierOrderElement.getSupplierOrderAmount()*supplierOrderElement.getSupplierOrderPrice()),
					supplierOrderElement.getLeadTime(),
					supplierOrderElement.getDeadline() != null ? sdf.format(supplierOrderElement.getDeadline()) : "" };

			map.put("cell", cell);
			map.put("id", i);
			mapList.add(map);

		}

		return mapList;
	}
    
    
    /*
     * 供应商管理明细
     */
    public void SupplierOrderElement(PageModel<AddSupplierOrderElementVo> page) {
		page.setEntities(supplierOrderElementDao.SupplierOrderElementPage(page));
	}
    
    /*
     * 供应商管理明细
     */
    public void SupplierOrderElementForSelect(PageModel<AddSupplierOrderElementVo> page) {
		page.setEntities(supplierOrderElementDao.SupplierOrderElement(new Integer(page.get("id").toString())));
	}
    
    /*
     * 供应商列表
     */
    public void SupplierListPage(PageModel<SupplierListVo> page){
    	page.setEntities(supplierOrderElementDao.SupplierListPage(page));
    }
    
    /*
     * 获取供应商数据
     */
    public List<SupplierListVo> SupplierListMessage(PageModel<SupplierListVo> page){
    	return supplierOrderElementDao.SupplierListPage(page);
    }
    
    /*
     * 获取elementId
     */
    public Integer getElementId(Integer clientQuoteElementId){
    	return supplierOrderElementDao.getElementId(clientQuoteElementId);
    }
    
    public Integer getSqeElementId(Integer clientQuoteElementId){
    	return supplierOrderElementDao.getSqeElementId(clientQuoteElementId);
    }
    
    /*
     * 获取客户订单信息
     */
    public ClientOrderElementVo findById(Integer id){
    	return supplierOrderElementDao.findById(id);
    }
    
    /*
     * 供应商订单管理
     */
    public void SupplierOrderManagePage(PageModel<SupplierOrderManageVo> page,String where,GridSort sort){
    	page.put("where", where);
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(supplierOrderDao.SupplierOrderManagePage(page));
    }
    
    /*
     * 根据id查订单
     */
    public SupplierOrderManageVo findMessageById(Integer id) {
    	PageModel<SupplierOrderManageVo> page = new PageModel<SupplierOrderManageVo>();
    	StringBuffer where = new StringBuffer();
    	where.append("so.id = ").append(id.toString());
    	page.put("where", where.toString());
    	return supplierOrderDao.SupplierOrderManagePage(page).get(0);
	}
    
    /*
     * 根据ID查询
     */
    public SupplierOrderManageVo findByOrderId(Integer id){
    	return supplierOrderDao.findById(id);
    }
    
    
    /*
     * 计算比较比率
     */
    public BigDecimal caculatePrice(BigDecimal price, BigDecimal baseER,
			BigDecimal exchangeRate) {
		price = price.multiply(baseER).divide(exchangeRate, 2,
				BigDecimal.ROUND_HALF_UP);
		return price;
	}
    
    /*
     * 根据客户订单ID查询
     */
    public SupplierOrder findByClientOrderId(SupplierOrder supplierOrder){
    	return supplierOrderDao.findByClientOrderId(supplierOrder);
    }
    
    /*
     * 根据订单ID查询
     */
    public List<AddSupplierOrderElementVo> findBySupplierOrderId(PageModel<AddSupplierOrderElementVo> page){
    	return supplierOrderElementDao.SupplierOrderElementPage(page);
    }
    
    public List<AddSupplierOrderElementVo> Elements(Integer id) {
		return supplierOrderElementDao.SupplierOrderElement(id);
   	}
    
    /*
     * 未完成工作
     */
    public void unFinishWorkPage(PageModel<AddSupplierOrderElementVo> page,String where,GridSort sort){
    	StringBuffer condition = new StringBuffer();
    	if (where!=null && !"".equals(where)) {
			condition.append(" and ").append(where);
			page.put("where", condition.toString());
		}
    	
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(supplierOrderElementDao.unFinishWorkPage(page));
    }


	@Override
	public void dueRemindSupplierOrderManagePage(PageModel<SupplierOrderManageVo> page, String where, GridSort sort) {
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
		page.setEntities(supplierOrderDao.dueRemindSupplierOrderManagePage(page));
	}


	@Override
	public SupplierOrder lookSupplierOrder(ClientOrder clientOrder, Integer clientOrderId, Integer supplierid,
			String supplierCode, Integer currencyId, Double exchangeRate) {
		List<SupplierOrder> supplierOrder=supplierOrderDao.findSupplierOrder( clientOrderId, supplierid, 63);
		
		SupplierOrder record=new SupplierOrder();
		if(null==supplierOrder||supplierOrder.isEmpty()){
			record.setClientOrderId(clientOrderId);
			record.setSupplierId(supplierid);
			record.setCurrencyId(currencyId);
			record.setExchangeRate(exchangeRate);
			record.setOrderNumber(getSupplierOrderNumber(clientOrder.getOrderNumber(),supplierCode));
			record.setTerms(100);
			record.setOrderStatusId(63);
			record.setOrderDate(new Date());
			record.setUpdateTimestamp(new Date());
			record.setRemark("STORAGE");
			supplierOrderDao.insertSelective(record);
		}else{
			record=supplierOrder.get(0);
		}
		return record;
	}
	
	private String getSupplierOrderNumber(String orderNumber,
			String supplierCode) {
		return orderNumber + "-" + supplierCode;
	}
	
	public double getMinPrice(PageModel<SupplierListVo> page){
		return supplierOrderElementDao.getMinPrice(page);
	}
	
    /**
     * 未下订单
     */
    public void NoOrderPage(PageModel<ClientOrder> page,String where,GridSort sort){
    	if (!"".equals(where) && null!=where) {
    		page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierOrderDao.NoOrderPage(page));
    }
    
    /**
     * 未订货明细
     */
    public void NoOrderELementPage(PageModel<ClientOrderElement> page){
    	page.setEntities(supplierOrderDao.NoOrderELementPage(page));
    }

    /**
     * 新增询价单
     */
    public ClientInquiry creatClientInquiry(Integer supplierId,Integer repair){
    	Supplier supplier = supplierDao.selectByPrimaryKey(supplierId);
    	ClientInquiry clientInquiry = new ClientInquiry();
    	List<SystemCode> airTypes = systemCodeService.findType("AIR_TYPE");
    	List<SystemCode> bizTypes = systemCodeService.findType("BIZ_TYPE");
    	Client client = clientDao.selectByPrimaryKey(65);
    	int maxSep=clientInquiryDao.findMaxSeq();
		Integer nextSeq=new Integer(maxSep+1);
		clientInquiry.setQuoteNumberSeq(nextSeq);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	StringBuffer sourceNumber = new StringBuffer();
    	StringBuffer quoteNumber=new StringBuffer();
    	sourceNumber.append("KC-").append(sdf.format(new Date())).append("-").append(supplier.getCode());
    	if (repair == 1) {
    		for (int i = 0; i < bizTypes.size(); i++) {
				if (bizTypes.get(i).getCode().equals("4")) {
					clientInquiry.setBizTypeId(bizTypes.get(i).getId());
				}
			}
    		
		}else {
			for (int i = 0; i < bizTypes.size(); i++) {
				if (bizTypes.get(i).getCode().equals("1")) {
					clientInquiry.setBizTypeId(bizTypes.get(i).getId());
				}
			}
		}
    	clientInquiry.setAirTypeId(airTypes.get(0).getId());
    	clientInquiry.setClientId(65);
    	clientInquiry.setClientContactId(17);
    	clientInquiry.setInquiryDate(new Date());
    	clientInquiry.setInquiryDate(new Date());
    	clientInquiry.setSourceNumber(sourceNumber.toString());
    	quoteNumber.append(client.getCode());
		quoteNumber.append(airTypes.get(0).getCode());
		quoteNumber.append(bizTypes.get(0).getCode());
		quoteNumber.append(clientInquiryService.getDateStr((Date)clientInquiry.getInquiryDate()));
		quoteNumber.append(StringUtils.leftPad(nextSeq.toString(), 2, '0'));
		clientInquiry.setQuoteNumber(quoteNumber.toString());
		clientInquiry.setQuoteNumberSeq(nextSeq);
		clientInquiry.setInquiryStatusId(30);
		clientInquiry.setUpdateTimestamp(new Date());
    	clientInquiryDao.insertSelective(clientInquiry);
    	return clientInquiry;
    }
    
    /**
     * 新增供应商询价单
     */
    public SupplierInquiry addSupplierInquiry(ClientInquiry clientInquiry,Integer supplierId) {
		SupplierInquiry supplierInquiry = new SupplierInquiry();
		supplierInquiry.setSupplierId(supplierId);
		supplierInquiry.setClientInquiryId(clientInquiry.getId());
		supplierInquiry.setQuoteNumber(supplierInquiryService.getQuoteNumberSeq(clientInquiry.getInquiryDate(), supplierId));
		supplierInquiry.setInquiryDate(new Date());
		supplierInquiry.setDeadline(new Date());
		supplierInquiry.setUpdateTimestamp(new Date());
		supplierInquiryService.insertSelective(supplierInquiry);
		return supplierInquiry;
	}
    
    /**
     * 新增供应商报价单
     */
    public SupplierQuote addSupplierQuote(SupplierInquiry supplierInquiry,Integer userId) {
		SupplierQuote supplierQuote = new SupplierQuote();
		Supplier supplier = supplierDao.selectByPrimaryKey(supplierInquiry.getSupplierId());
		SystemCode currency = systemCodeService.selectByPrimaryKey(supplier.getCurrencyId());
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(currency.getId());
		supplierQuote.setQuoteNumber(supplierInquiry.getQuoteNumber());
		supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
		supplierQuote.setCurrencyId(currency.getId());
		supplierQuote.setExchangeRate(exchangeRate.getRate());
		supplierQuote.setQuoteDate(new Date());
		supplierQuote.setUpdateTimestamp(new Date());
		supplierQuote.setQuoteStatusId(70);
		supplierQuote.setCreateUser(userId);
		supplierQuoteDao.insertSelective(supplierQuote);
		return supplierQuote;
	}
    
    /**
     * 新增客户报价
     */
    public ClientQuote addClientQuote(ClientInquiry clientInquiry) {
		ClientQuote clientQuote = new ClientQuote();
		Client client = clientDao.selectByPrimaryKey(clientInquiry.getClientId());
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(client.getCurrencyId());
		List<SystemCode> list = systemCodeService.findType("TERMS_OF_DELIVERY");
		clientQuote.setClientInquiryId(clientInquiry.getId());
		clientQuote.setSeq(1);
		clientQuote.setQuoteNumber(clientInquiry.getQuoteNumber());
		clientQuote.setQuoteDate(new Date());
		clientQuote.setCurrencyId(client.getCurrencyId());
		clientQuote.setExchangeRate(exchangeRate.getRate());
		clientQuote.setProfitMargin(BigDecimal.ONE.doubleValue());
		clientQuote.setPrepayRate(client.getPrepayRate());
		clientQuote.setShipPayPeriod(client.getShipPayPeriod());
		clientQuote.setShipPayRate(client.getShipPayRate());
		clientQuote.setReceivePayPeriod(client.getReceivePayPeriod());
		clientQuote.setReceivePayRate(client.getReceivePayRate());
		clientQuote.setTermsOfDelivery(list.get(0).getId());
		clientQuoteDao.insertSelective(clientQuote);
		return clientQuote;
	}
    
    /**
     * 新增客户订单
     */
    public ClientOrder addClientOrder(ClientQuote clientQuote,Integer userId,Integer supplierId) {
    	Supplier supplier = supplierDao.selectByPrimaryKey(supplierId);
		ClientOrder clientOrder = new ClientOrder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		StringBuffer sourceNumber = new StringBuffer();
		sourceNumber.append("KC-").append(sdf.format(new Date())).append("-").append(supplier.getCode());
		clientOrder.setClientQuoteId(clientQuote.getId());
		clientOrder.setCurrencyId(clientQuote.getCurrencyId());
		clientOrder.setExchangeRate(clientQuote.getExchangeRate());
		clientOrder.setSourceNumber(sourceNumber.toString());
		clientOrder.setOrderNumber("ORD-"+clientQuote.getQuoteNumber());
		Integer seq=clientQuoteService.findseq(clientQuote.getId());
		int maxSeq;
		if(seq==null){
			maxSeq=0;
		}else{
			maxSeq=seq;
		}
		clientOrder.setSeq(++maxSeq);
		clientOrder.setOrderDate(new Date());
		clientOrder.setOrderStatusId(63);
		clientOrder.setUpdateTimestamp(new Date());
		clientOrder.setPrepayRate(clientQuote.getPrepayRate());
		clientOrder.setShipPayPeriod(clientQuote.getShipPayPeriod());
		clientOrder.setShipPayRate(clientQuote.getShipPayRate());
		clientOrder.setReceivePayPeriod(clientQuote.getReceivePayPeriod());
		clientOrder.setReceivePayRate(clientQuote.getReceivePayRate());
		clientOrder.setCreateUserId(userId);
		clientOrder.setPurchaseApply(0);
		clientOrder.setComplete(1);
		clientOrderDao.insertSelective(clientOrder);
		return clientOrder;
	}
    
    /**
     * 新增库存订单
     */
    public void addStorageOrder(Integer userId,Integer supplierId,Integer repair) {
		ClientInquiry clientInquiry = creatClientInquiry(supplierId,repair);
		SupplierInquiry supplierInquiry = addSupplierInquiry(clientInquiry,supplierId);
		SupplierQuote supplierQuote = addSupplierQuote(supplierInquiry,userId);
		ClientQuote clientQuote = addClientQuote(clientInquiry);
		ClientOrder clientOrder = addClientOrder(clientQuote, userId,supplierId);
	}
    
    /**
     * 新增供应商订单
     * @throws SQLException 
     */
   /* public SupplierOrder addSupplierOrder(ClientOrder clientOrder) throws SQLException {
		SupplierOrder supplierOrder = new SupplierOrder();
		Supplier supplier = supplierDao.selectByPrimaryKey(1000002);
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
		supplierOrder.setClientOrderId(clientOrder.getId());
		supplierOrder.setSupplierId(supplier.getId());
		supplierOrder.setCurrencyId(supplier.getCurrencyId());
		supplierOrder.setExchangeRate(exchangeRate.getRate());
		supplierOrder.setOrderDate(new Date());
		supplierOrder.setOrderNumber(getOrderNumberSeq(supplierOrder.getOrderDate()));
		Double terms = clientOrder.getPrepayRate()*100;
		supplierOrder.setTerms(terms.intValue());
		supplierOrder.setOrderStatusId(63);
		supplierOrder.setUpdateTimestamp(new Date());
		supplierOrderDao.insertSelective(supplierOrder);
		return supplierOrder;
	}*/
    
    
	@Override
	public List<SupplierInquiryStatistic> supplierOrderStat(SupplierInquiryStatistic supplierInquiryStatistic) {
		return supplierOrderDao.supplierOrderStat( supplierInquiryStatistic);
	}
	
	public StorageOrderVo getIds(Integer clientOrderId){
		return supplierOrderDao.getIds(clientOrderId);
	}


	@Override
	public ClientOrderElementVo findByClientOrderElementId(Integer clientOrderElementId) {
		return supplierOrderElementDao.findByClientOrderElementId(clientOrderElementId);
	}
	
	public SupplierOrderManageVo getPaymentPercent(Integer supplierOrderId){
		return supplierOrderDao.getPaymentPercent(supplierOrderId);
	}

	@Override
	public List<ClientOrderElementVo> ClientOrderData(ClientQuote clientQuote) {
		return supplierOrderElementDao.ClientOrderData(clientQuote);
	}

	@Override
	public List<SupplierListVo> SupplierListData(Integer sqeElementId,Integer cieElementId,Integer ciieElementId) {
		return supplierOrderElementDao.SupplierListData(sqeElementId,cieElementId,ciieElementId);
	}
	
	public SupplierOrder selectBySupplierOrderElementId(Integer supplierOrderElementId){
		return supplierOrderDao.selectBySupplierOrderElementId(supplierOrderElementId);
	}

	@Override
	public void NoOrderELementDataPage(PageModel<ClientOrderElement> page) {
		page.setEntities(supplierOrderDao.NoOrderELementDataPage(page));
	}

	@Override
	public List<ClientOrderElementVo> CompletedSupplierWeatherOrderData(Integer clientOrderId) {
		return supplierOrderElementDao.CompletedSupplierWeatherOrderData(clientOrderId);
	}
	
	public Address getAddress(Integer id){
		return supplierOrderDao.getAddress(id);
	}
	
	public Integer getDestination(Integer id){
		return supplierOrderDao.getDestination(id);
	}
	
	public SupplierOrder selectByOrderNumber(String orderNumber){
		return supplierOrderDao.selectByOrderNumber(orderNumber);
	}
}
