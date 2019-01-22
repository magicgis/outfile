/**
 * 
 */
package com.naswork.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.naswork.common.constants.Constants;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

/**
 * 检查用户是否存在
 * 
 * @since 2015-8-5 下午3:01:43
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class UserInterceptor extends HandlerInterceptorAdapter {
	private static final String loginUrl = "/login";// 登录页面
	private static final String loginExpiredUrl = "/loginExpired";// 登录超时
	private List<String> uncheckUrls;// 不检查的url

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		/*try {
			// 获得请求路径的uri
			String uri = request.getRequestURI();
			// 判断路径是登出还是登录验证，是这两者之一的话执行Controller中定义的方法
			for (String url : uncheckUrls) {
				if (uri.contains(url))
					return true;
			}

			HttpSession session = request.getSession();
			UserVo user = (UserVo) session
					.getAttribute(Constants.SESSION_USER);
			if (user != null)
				return true;
			
			String str1 = request.getHeader("X-Requested-With");
			if ((str1 != null) && (str1.equalsIgnoreCase("XMLHttpRequest"))) {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().print(JsonUtils.toJson(new ResultVo(false, "登录已经超时,请重新登录!")));
				return false;
			}
			if (request.getMethod().equalsIgnoreCase("GET")) {
				response.sendRedirect(request.getContextPath() + loginExpiredUrl);
			} else {
				response.sendRedirect(request.getContextPath() + loginUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return true;
	}

	public List<String> getUncheckUrls() {
		return uncheckUrls;
	}

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}
}
