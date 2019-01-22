/**
 * 
 */
package com.naswork.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.naswork.common.constants.Constants;
import com.naswork.vo.UserVo;

/**
 * @since 2016年4月26日 上午9:05:34
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ContextHolder {
	/**
	 * 存放request
	 */
	private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();
	/**
	 * 存放response
	 */
	private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();

	public static HttpServletRequest getRequest() {
		return requestLocal.get();
	}

	public static void setRequest(HttpServletRequest request) {
		requestLocal.set(request);
	}

	public static HttpServletResponse getResponse() {
		return responseLocal.get();
	}

	public static void setResponse(HttpServletResponse response) {
		responseLocal.set(response);
	}

	public static HttpSession getSession() {
		return (requestLocal.get()).getSession();
	}
	
	public static UserVo getCurrentUser() {
		HttpSession session = getSession();
		Object obj = session.getAttribute(Constants.SESSION_USER);
		return null == obj ? null : (UserVo) obj;
	}
}
