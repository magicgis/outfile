package com.naswork.module.home.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.service.FlowService;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController{

	@Autowired
	private FlowService flowService;
	
	/**
	 * 我的主页
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request)
	{
		return "/home/index";
	}
	
	/**
	 * 个人任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/personalTask")
	public String personalTask(HttpServletRequest request)
	{
		return "/home/personalTask";
	}
	
	/**
	 * 组任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/groupTask")
	public String groupTask(HttpServletRequest request)
	{
		return "/home/groupTask";
	}
	
	/**
	 * 组任务数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/groupTaskData", method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo groupTaskData(HttpServletRequest request)
	{
		PageModel page = getPage(request);
		Map<String, String> map = new HashMap<String, String>();
		
		flowService.findGroupTaskListPage(page, map, getSort(request));
		
		JQGridMapVo vo = new JQGridMapVo();
		vo.setRows(page.getEntities());
		vo.setPage(page.getPageNo());
		vo.setTotal(page.getPageCount());
		vo.setRecords(page.getRecordCount());
		
		return vo;
	}
	
	/**
	 * 拾取任务
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/takeTask", method=RequestMethod.POST)
	public @ResponseBody ResultVo takeTask(HttpServletRequest request)
	{
		String taskIds = getString(request, "taskIds");
		String message = "";
		boolean result = false;
		try {
			flowService.claimTask(taskIds);
			result = true;
			message = "拾取任务成功！";
		} catch (Exception e) {
			e.printStackTrace();
			message = "拾取任务失败！";
		}
		return new ResultVo(result, message);
	}
}
