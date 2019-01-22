package com.naswork.common.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.naswork.common.constants.Constants;
import com.naswork.service.CacheService;
import com.naswork.service.ExportService;
import com.naswork.utils.UuidUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

/**
 * @since 2015-10-28 上午10:17:32
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Controller
public class BaseController {
	/**
	 * 日志
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private CacheService cacheService;
	@Resource
	protected ExportService exportService;
	
	/**
	 * 当前在线用户
	 * @param request
	 * @return
	 * @since 2015年8月20日 上午10:05:04
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public UserVo getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute(Constants.SESSION_USER);
		return null == obj ? null : (UserVo) obj;
	}
	
	/**
	 * 删除当前在线用户
	 * @param request
	 * @since 2015年8月20日 上午10:05:25
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	protected void removeCurrentUser(HttpServletRequest request) {
		request.getSession().removeAttribute(Constants.SESSION_USER);
	}
	
	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**
	 * 得到分页列表的信息 
	 */
	protected PageModel getPage(HttpServletRequest request){
		PageModel page = new PageModel(request);
		if(StringUtils.isNotEmpty(request.getParameter("page"))){
			page.setPageNo(Integer.parseInt(request.getParameter("page")));
		}
		if(StringUtils.isNotEmpty(request.getParameter("rows"))){
			page.setPageSize(Integer.parseInt(request.getParameter("rows")));
		}
		
		if(StringUtils.isNotEmpty(request.getParameter("exportModel"))){
			page.setPageSize(100000);
		}
		return page;
	}
	
	/**
	 * 从request中获取排序
	 * @return
	 * @since 2015年8月25日 下午5:10:44
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	protected GridSort getSort(HttpServletRequest request){
		if(StringUtils.isNotEmpty(request.getParameter("sidx"))){
			GridSort sort = new GridSort();
			sort.setName(request.getParameter("sidx"));
			sort.setOrder(request.getParameter("sord"));
			return sort;
		}
		return null;
	}
	
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		return request;
	}
	
	/**
	 * 从缓存中获取代码名称
	 * @param columnName
	 * @param value
	 * @return
	 * @since 2015年9月12日 上午11:48:59
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	protected String getDmMc(String columnName,String value) {
		if(StringUtils.isEmpty(value)){
			return "";
		}
		String mc = cacheService.getDmMc(columnName, value);
		return StringUtils.isNotBlank(mc)? mc : value;
	}
	
	public Object getDmObj(String columnName,String value) {
		if(StringUtils.isEmpty(value)){
			return null;
		}
		Object obj = cacheService.getDmObj(columnName, value);
		return obj;
	}
	
	
	/**
	 * 对数据中的代码进行转换
	 * @param data
	 * @param columnName
	 * @return
	 * @since 2015年9月12日 上午11:55:26
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	protected Map<String, Object> convertDM(Map<String, Object> data,String columnName) {
		for (String key : data.keySet()) {
			if(key.equals(columnName)){
				String mc = getDmMc(columnName,(String)data.get(key));
				data.put(key, mc);
			}
		}
		return data;
	}

	/**
	 * 得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	public static void logError(String errorMessage,Logger logger, Throwable t){
		// 获得当前类名
		String clazz = logger.getName();
		// 获得当前方法名
		logger.error("##############"+ clazz +"# logStart ###################");
		logger.error(errorMessage,t);
		logger.error("##############"+ clazz +"# logEnd ###################");
		
	}
	
	/**
	 * 获取字符串参
	 * @param request
	 * @param param
	 * @param def
	 * @return
	 */
	protected String getString(HttpServletRequest request, String param, String def) 
	{
		if (StringUtils.isEmpty(request.getParameter(param)))
		{
			return def;
		}
		else
		{
			return request.getParameter(param).trim();
		}
	}
	
	/**
	 * 获取字符串参
	 * @param request
	 * @param param
	 * @return
	 */
	public String getString(HttpServletRequest request, String param) 
	{
		return this.getString(request, param, "");
	}
}
