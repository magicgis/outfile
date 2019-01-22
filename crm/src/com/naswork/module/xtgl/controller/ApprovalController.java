package com.naswork.module.xtgl.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ApprovalDetail;
import com.naswork.service.ApprovalService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.utils.ApprovalHelper;
@Controller
@RequestMapping("/xtgl/approval")
public class ApprovalController extends BaseController {

	@Resource
	private ApprovalService approvalService;
	
	@Resource
	private ApprovalHelper approvalHelper;
	
	@RequestMapping(value = "/detailPage",method = RequestMethod.GET)
	public String detailPage(HttpServletRequest request){
		String approvalId = this.getString(request, "approvalId");
		request.setAttribute("approvalId", approvalId);		
		return "/xtgl/approval/detail";
	}
	
	@RequestMapping(value = "/detailPage/data", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo detailPageData(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<ApprovalDetail> page = new PageModel<ApprovalDetail>();
		String approvalId = this.getString(request, "approvalId");
		page.put("approvalId", approvalId);
		this.approvalService.fetchApprovalDetail(page, this.getSort(request));
		JQGridMapVo jqgrid = new JQGridMapVo();
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ApprovalDetail ad : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(ad);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());
		}

		// 导出
		if (StringUtils.isNotEmpty(request.getParameter("exportModel"))) {
			try {
				exportService.exportGridToXls("审核详细列表",
						request.getParameter("exportModel"), jqgrid.getRows(),
						response);
				return null;
			} catch (Exception e) {
				logger.warn("导出数据出错!", e);
			}
		}
		return jqgrid;
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createApprovalRequest", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo createApprovalRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.approvalHelper.createApprovalRequest(this, request, response, null);
	}
	
	@RequestMapping(value = "/sendApprovalRequest", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo sendApprovalRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.approvalHelper.sendApprovalRequest(this, request, response, null);
	}

	@RequestMapping(value = "/receiveApprovalRequest", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo receiveApprovalRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.approvalHelper.receiveApprovalRequest(this, request, response, null);
	}
	
	@RequestMapping(value = "/approveApprovalRequest", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo approveApprovalRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.approvalHelper.approveApprovalRequest(this, request, response, null);
	}

	@RequestMapping(value = "/rejectApprovalRequest", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo rejectApprovalRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.approvalHelper.rejectApprovalRequest(this, request, response, null);
	}

	@RequestMapping(value = "/cancelApprovalRequest", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo cancelApprovalRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.approvalHelper.cancelApprovalRequest(this, request, response, null);
	}

	@RequestMapping(value = "/restoreApprovalRequest", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo restoreApprovalRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.approvalHelper.restoreApprovalRequest(this, request, response, null);
	}

}
