package com.naswork.service;

import java.util.List;

import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SystemCodeService {
	
	int insertSelective(SystemCode record);

	SystemCode selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SystemCode record);
	
	List<SystemCode> findType(String type);
	
	public List<SystemCode> findTypeSort(String type);

	/*
	 * 查询飞机类型
	 */
	public List<SystemCode> findairType();
	
	/*
	 * 查询商业类型
	 */
	public List<SystemCode> findbizType();
	
	/*
	 * 查询状态
	 */
	public List<SystemCode> findInquiryStatus();
	
	/*
	 * 查询订单状态
	 */
	public List<SystemCode> findOrderStatus();
	
	/*
	 * 查询货币种类
	 */
	public List<SystemCode> findCurrency();
	
	/*
	 * 查询汇率
	 */
	public List<ClientQuoteVo> findRate();
	
	/*
	 * 查询客户状态
	 */
	public List<SystemCode> getClientStatus();
	
	/*
	 * 查询客户等级
	 */
	public List<SystemCode> getClientLevel();
	
	/*
	 * 按类型查询供应商信息
	 * **/
	public List<SystemCode> findSupplierByType(String type);
	
	/*
	 * 根据ID查询 
	 */
	public SystemCode findById(Integer id);
	
	/*
	 * 发货方式
	 */
	public List<SystemCode> shipWay();
	
	/*
	 * 交付方式
	 */
	public List<SystemCode> delivery();
	
	/*
	 * 客户类型
	 */
	public List<SystemCode> type();
	
	/*
	 * 发票模板
	 */
	public List<SystemCode> invoiceTemplet();
	
	/*
	 * 机型管理
	 */
	public void airPage(PageModel<SystemCode> page, String where,
			GridSort sort);
	
	/*
	 * stockmarket机型管理
	 */
	public void airForStockMarketPage(PageModel<SystemCode> page, String where,
			GridSort sort);
	
	/*
	 * 机型代码查询
	 */
	public List<SystemCode> selectByCode(PageModel<String> page);
	
	/*
	 * 插入机型
	 */
	public void saveAir(SystemCode systemCode);
	
	/*
	 * 插入stockmarket机型
	 */
	public void saveAirForStockMarket(SystemCode systemCode,String userId);
	
	
	/*
	 * 商业类型类型
	 */
	public void businessPage(PageModel<SystemCode> page, String where,
			GridSort sort);
	
	/*
	 * 商业类型代码查询
	 */
	public List<SystemCode> selectByBizCode(String code);
	
	/*
	 * 插入商业类型
	 */
	public void saveBiz(SystemCode systemCode);
	
	/**
	 * 根据类型搜索，根据code排序
	 * @param type
	 * @return
	 */
	public List<SystemCode> findTypeSortWithCode(String type);
	
}
