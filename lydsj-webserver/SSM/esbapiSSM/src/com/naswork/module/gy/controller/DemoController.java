/**
 * 
 */
package com.naswork.module.gy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.vo.DropDownVo;

/**
 * @since 2016年5月7日 下午2:25:20
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {
	/**
	 * 表单demo
	 * @return
	 * @since 2016年5月6日 上午9:52:47
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/formdemo", method = RequestMethod.GET)
	public String formdemo() {
		return "/demo/formdemo";
	}
	
	/**
	 * 上传附件demo
	 * @return
	 * @since 2016年5月7日 下午2:27:44
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/uploadAttachmentDemo", method = RequestMethod.GET)
	public String uploadAttachment(){
		return "/demo/uploadAttachmentDemo";
	}

	@RequestMapping(value = "/approvalDemo", method = RequestMethod.GET)
	public String approvalDemo(){
		return "/demo/approval";
	}

	/**
	 * 上传附件demo
	 * @return
	 * @since 2016年5月7日 下午2:27:44
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/excelMgmtDemo", method = RequestMethod.GET)
	public String excelMgmt(){
		return "/demo/excelMgmtDemo";
	}

	/**
	 * 查询条件
	 * @return
	 * @since 2016年5月10日 下午9:05:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/searchFormDemo", method = RequestMethod.GET)
	public String searchFormDemo() {
		return "/demo/searchFormDemo";
	}
	
	/**
	 * 条件内容
	 * @param request
	 * @return
	 * @since 2016年5月10日 下午9:05:32
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/typesjson",method = RequestMethod.POST)
	public @ResponseBody List<DropDownVo> typesjson(HttpServletRequest request){
		List<DropDownVo> dropdownlist =  new ArrayList<DropDownVo>();
		for (int i = 0; i < 10; i++) {
			dropdownlist.add(new DropDownVo(String.valueOf((i+1)),"选择"+i+1));
		}
		
		return dropdownlist;
	}
	
	/**
	 * 弹出窗口选人或者税务机关
	 * @return
	 * @since 2016年5月11日 下午4:31:33
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/openDialogSelectDemo", method = RequestMethod.GET)
	public String openDialogSelectDemo() {
		return "/demo/openDialogSelectDemo";
	} 
	
	
	/**
	 * 查询条件
	 * @return
	 * @since 2016年5月10日 下午9:05:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/selectDemo", method = RequestMethod.GET)
	public String selectDemo() {
		return "/demo/selectDemo";
	}
	
	/**
	 * 选择案件demo
	 * @return
	 * @since 2016年5月12日 下午2:28:09
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/selectCase", method = RequestMethod.GET)
	public String selectCase() {
		return "/demo/selectCase";
	}
	
	/**
	 * 文书示例
	 * @return
	 * @since 2016年5月7日 下午2:27:44
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/wsDemo", method = RequestMethod.GET)
	public String wsDemo(){
		return "/demo/wsDemo";
	}
	
	@RequestMapping(value = "/wsDemoPage", method = RequestMethod.GET)
	public String wsDemoPage(HttpServletRequest request){
		
		return "/demo/demo-"+request.getParameter("id").toString();
	}
}
