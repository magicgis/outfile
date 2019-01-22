package com.naswork.module.purchase.controller.deploymentsendbackmessage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.SendBackFlowMessage;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.SendBackFlowMessageService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

/**
 * @author: Tanoy
 * @date:2018年8月23日 下午5:30:37
 * @version :1.0
 * 
 */

@Controller
@RequestMapping(value="/purchase/deploymentSendBack")
public class DeploymentSendBackController extends BaseController {
	
	@Resource
	private SendBackFlowMessageService sendBackFlowMessageService;
	@Resource
	private UserService userService;

	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月23日 下午5:35:22
	 * @Description:跳转列表页面
	 * @return
	 */
	@RequestMapping(value="/toListData",method=RequestMethod.GET)
	public String toListData(){
		return "/purchase/sendbackdeployment/list";
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月23日 下午5:35:43
	 * @Description:流程退回列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SendBackFlowMessage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员")) {
			page.put("userId", userVo.getUserId());
		}
		sendBackFlowMessageService.getSendBackListPage(page, where, sort);;
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SendBackFlowMessage sendBackFlowMessage : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(sendBackFlowMessage);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月23日 下午5:35:43
	 * @Description:获取流程退回数量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getListDataCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getListDataCount(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SendBackFlowMessage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员")) {
			page.put("userId", userVo.getUserId());
		}
		sendBackFlowMessageService.getSendBackListPage(page, where, sort);;
		return new ResultVo(true, page.getEntities().size()+"");
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月23日 下午5:49:36
	 * @Description:修改阅读状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/changeReadStatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo changeReadStatus(HttpServletRequest request){
		try {
			String ids = getString(request, "ids");
			String[] editIds = ids.split(",");
			for (int i = 0; i < editIds.length; i++) {
				SendBackFlowMessage sendBackFlowMessage = new SendBackFlowMessage();
				sendBackFlowMessage.setRead(1);
				sendBackFlowMessage.setId(new Integer(editIds[i]));
				sendBackFlowMessageService.updateByPrimaryKeySelective(sendBackFlowMessage);
			}
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
	
}
