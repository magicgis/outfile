package com.naswork.module.zwwcontroller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naswork.common.constants.Constants;
import com.naswork.common.controller.BaseController;
import com.naswork.service.UserService;
import com.naswork.vo.UserVo;
/**
 * 
 * @author zhangwenwen
 *post请求登录
 */
@Controller
public class NewLoginController extends BaseController {

	@Resource
	private UserService userService;
	
	@RequestMapping(value = "/v1/login",method=RequestMethod.POST)
	@ResponseBody
	public HashMap<String, String> doLogin(HttpSession session,RedirectAttributes redirectAttributes,
			 @RequestBody HashMap<String, String> map) {
		UserVo userVo=userService.getLoginUser(map.get("name"), map.get("password"));
		HashMap<String, String> hashMap=new HashMap<String, String>();
		if (userVo!=null) {
			session.setAttribute(Constants.SESSION_USER, userVo);
			hashMap.put("flag", "success");
			return hashMap;
		}
		redirectAttributes.addFlashAttribute("message","用户名或密码错误!");
		hashMap.put("flag", "fail");
		return hashMap;
	}
}
