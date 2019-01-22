package com.naswork.dao;

import java.util.List;

import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.vo.PageModel;

public interface SystemCodeDao {
	
	int deleteByPrimaryKey(Integer id);

    int insert(SystemCode record);

    int insertSelective(SystemCode record);

    SystemCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemCode record);

    int updateByPrimaryKey(SystemCode record);
	
	public SystemCode selectById(Integer id);
	
	/*
	 * 查询飞机类型
	 */
	public List<SystemCode> findairType();
	
	/*
	 * 查询商业类型
	 */
	public List<SystemCode> findType(String type);
	
	/*
	 * 主键查询
	 */
	public SystemCode findById(Integer id);
	
	/*
	 * 查询汇率
	 */
	public List<ClientQuoteVo> findRate();
	
	/*
	按类型查询供应商信息
	*/
	public List<SystemCode> findSupplierByType(String type);
	
	/*
	 * 机型
	 */
	public List<SystemCode> airPage(PageModel<SystemCode> page);
	
	/*
	 * 机型代码查询
	 */
	public List<SystemCode> selectByCode(PageModel<String> page);
	
	/*
	 * 获取最大ID
	 */
	public Integer getMaxId();
	
	/*
	 * 商业类型
	 */
	public List<SystemCode> businessPage(PageModel<SystemCode> page);
	
	/*
	 * 商业类型代码查询
	 */
	public List<SystemCode> selectByBizCode(String code);
	
	/*
	 * 根据出库指令查询
	 */
	public Integer findByExportPackageInstructionsNumber(String exportPackageInstructionsNumber);
	
	/*
	 * 根据类型代码查询
	 */
	public SystemCode findByAirCode(Integer airCode);
	
	public List<SystemCode> selectByValue(String value);
	
	public List<SystemCode> selectByAllCode(String code);
	
	public List<SystemCode> getByCode(String code);
	
	public List<SystemCode> findTypeSort(String type);
	
	public List<SystemCode> findTypeSortWithCode(String type);
	
	public List<SystemCode> airForStockMarketPage(PageModel<SystemCode> page);
}