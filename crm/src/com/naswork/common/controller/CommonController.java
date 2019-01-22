/**
 * 
 */
package com.naswork.common.controller;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.service.FileService;
import com.naswork.utils.SpringUtils;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UploadFileVo;

/**
 * @since 2015-7-16 下午3:50:39
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {
	
	@Resource
	private FileService fileService;
	
	/**
	 * 资源不存在
	 */
	@RequestMapping(value = "/resource_not_found")
	public String resourceNotFound() {
		return "/common/not_found";
	}
	
	/**
	 * 导入方法
	 * @param request
	 * @return
	 * @since 2015年9月10日 下午4:34:05
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value="/import",method = RequestMethod.POST)
	public String importFile(HttpServletRequest request,@RequestParam("method")String methodName){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("Filedata");
		Map<String, String[]> paramap = multipartRequest.getParameterMap();
		ResultVo resultVo = null;
		try {
			String[] params = methodName.split("#");
			String springbeanid = params[0];
			String functionName = params[1];
			if(StringUtils.isNotEmpty(springbeanid) && StringUtils.isNotEmpty(functionName)){
				try {
					Object object = SpringUtils.getBean(springbeanid);
					Class[] parameterTypes = new Class[2];
					parameterTypes[0] = MultipartFile.class;
					parameterTypes[1] = Map.class;
					Method method = object.getClass().getMethod(functionName, parameterTypes);
					resultVo = (ResultVo) method.invoke(object, new Object[] {multipartFile,paramap});
				} catch (Exception e) {
					logger.error("执行方法时发生异常",e);
					e.printStackTrace();
				}
			}
			
		    request.setAttribute("result", resultVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return "/common/importResult";
	}

}
