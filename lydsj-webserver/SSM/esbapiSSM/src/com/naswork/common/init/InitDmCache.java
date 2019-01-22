/**
 * 
 */
package com.naswork.common.init;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.naswork.dao.RoleDao;
//import com.naswork.common.constants.TableMapConstant;
import com.naswork.service.CacheService;
import com.naswork.utils.excel.ExcelGeneratorMapConstant;
import com.naswork.vo.PageData;
import com.naswork.vo.RoleVo;

/**
 * 初始化代码
 * @since 2015年9月12日 上午11:57:19
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Component("initDmCache")
@Lazy(false)
public class InitDmCache {
	/**
	 * log
	 */
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private CacheService cacheService;
	
	@Resource
	private RoleDao roleDao;
	@Resource
	private ExcelGeneratorMapConstant excelGeneratorMapConstant;
	
	/**
	 * 初始化Excel生成器
	 * @since 2016年3月18日 下午12:08:20
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@PostConstruct
	public void initExcelGeneatorMap(){
		excelGeneratorMapConstant.init();
	}

	@PostConstruct
	private void initRoleCache(){
		List<RoleVo> allRoles = roleDao.findAllRoles(new PageData());
		Map<String, Object> data = new HashMap<String, Object>();
		for(RoleVo role: allRoles){
			data.put(role.getRoleId(), role);
		}
		cacheService.initCache("ROLE_CACHE", data);
		
		Map<String, Object> allRoleData = new HashMap<String, Object>();
		allRoleData.put("allRoles", allRoles);
		cacheService.initCache("ALL_ROLE_CACHE", allRoleData);
	}
	
}
