/**
 * 
 */
package com.naswork.module.xtgl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naswork.common.constants.Constants;
import com.naswork.common.controller.BaseController;
import com.naswork.model.Menu;
import com.naswork.service.UserService;
import com.naswork.utils.MD5;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

/**
 * 登录及注销相关
 * @since 2015年8月14日 上午11:29:25
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Controller("loginController")
public class LoginController extends BaseController{

	@Resource
	private UserService userService;

	/**
	 * 登录页面
	 * 
	 * @return
	 * @since 2015年8月14日 上午11:32:03
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String loginPage(HttpSession session) {
		if(session.getAttribute(Constants.SESSION_USER)!=null){
			return "redirect:/index";
		}
		return "/login";
	}
	
	/**
	 * shiro验证后的结果获取,分开两个方法防F5刷新
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @since 2015年8月19日 下午3:50:58
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 * @throws Exception 
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public String loginFn(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception{
		String loginName = StringUtils.defaultIfEmpty(request.getParameter("username"),"");
		String password = StringUtils.defaultIfEmpty(request.getParameter("enPassword"),"");
		
		ResultVo resultVo;
		resultVo = userService.login(loginName, password, request);
		if(resultVo.isSuccess()){
			return "redirect:/index";
		}
		redirectAttributes.addFlashAttribute("message", resultVo.getMessage());
		return "redirect:/login";
	}
	
	/**
	 * shiro验证后的结果获取,分开两个方法防F5刷新
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @since 2015年8月19日 下午3:50:58
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 * @throws Exception 
	 */
	@RequestMapping(value = "/nologin",method = RequestMethod.GET)
	public String nologin(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception{
		String loginName = StringUtils.defaultIfEmpty(request.getParameter("username"),"");
		String password = StringUtils.defaultIfEmpty(request.getParameter("enPassword"),"");
		
		ResultVo resultVo;
		resultVo = userService.login(loginName, password, request);
		if(resultVo.isSuccess()){
			return "redirect:/index";
		}
		redirectAttributes.addFlashAttribute("message", resultVo.getMessage());
		return "redirect:/login";
	}

	/**
	 * 主页面
	 * 
	 * @param request
	 * @return
	 * @since 2015年8月14日 下午2:04:59
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request) {
		UserVo user = this.getCurrentUser(request);
		request.setAttribute("user", user);
		List<Menu> menus = user.getRootmenus();
		request.setAttribute("rootmenus", menus);
		return "/index";
	}

	/**
	 * 注销
	 * @param request
	 * @return
	 * @since 2015年8月20日 下午1:10:50
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value="/logout",method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
		removeCurrentUser(request);
		return "redirect:/login";
	}
	
	/**
	 * 欢迎页面
	 * 
	 * @param request
	 * @return
	 * @since 2015年8月14日 下午2:08:17
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome(HttpServletRequest request) {

		return "/welcome";
	}
	
	/**
	 * 用于在拦截器后提示超时
	 * @param redirectAttributes
	 * @return
	 * @since 2015年8月20日 下午1:46:28
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value="/loginExpired",method = RequestMethod.GET)
	public String loginExpired(RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("message","登录已经超时,请重新登录!");
		return "redirect:/login";
	}
	
	/**
	 * 切换身份页面
	 * @return
	 * @since 2015年9月16日 下午3:57:38
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/qhsf",method=RequestMethod.GET)
	public String qhsfpage(){
		return "/sf";
	}
	
	/**
	 * 用户菜单
	 * @param request
	 * @param id
	 * @return
	 * @since 2015年10月8日 上午11:02:12
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/userSubMenus/{id}",method = RequestMethod.POST)
	public @ResponseBody List<LigerTreeVo> userSubMenus(HttpServletRequest request,@PathVariable String id){
		UserVo user = getCurrentUser(request);
		return user.getSubmenus().get(id);
	} 
	
	/**
	 * 修改密码页面
	 * 
	 */
	@RequestMapping(value = "/updatepass",method = RequestMethod.GET)
	public String updatepassPage(HttpSession session) {
		return "/updatepassword";
	}
	
	/**
     * 保存修改密码
     * @param request
     * @return
    */
	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo updatepassword(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		ResultVo vo=null;
		String oldpassword=request.getParameter("oldpass");//旧密码
		String newpassword=request.getParameter("npass");//新密码
		try {
			 vo=userService.updatePasswByLoginName(this.getCurrentUser(request).getLoginName(), newpassword,oldpassword);
			
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "修改密码失败" ;
		   vo=new ResultVo(success, message);
		}
		return vo;
	}
}
