package com.naswork.module.testjbpm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Ask4leave;
import com.naswork.model.TestJbpm;
import com.naswork.service.TestJbpmService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

/**
 * 测试流程控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/testJbpm")
public class TestJbpmController extends BaseController  {
	@Resource
	protected TestJbpmService testJbpmService;

	//跳转列表页面
	@RequestMapping(value = "/listPage",method = RequestMethod.GET)
	public String listPage(HttpServletRequest request){
		return "/testjbpm/listPage";
	}
	
	//弹窗
	@RequestMapping(value = "/dataPage",method = RequestMethod.GET)
	public String dataPage(HttpServletRequest request){
		return "/testjbpm/dataPage";
	}
	
	//流程里审核里面
	@RequestMapping(value = "/proessListPage",method = RequestMethod.GET)
	public String proessListPage(HttpServletRequest request){
		return "/testjbpm/proessListPage";
	}
	
	/**
	 * 数据列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getNoteList", method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo getNoteList(HttpServletRequest request){
		UserVo user = ContextHolder.getCurrentUser();
		PageModel<TestJbpm> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		GridSort sort = getSort(request);
		testJbpmService.findPage(page, sort);
		
		jqgrid.setPage(page.getPageNo());
		jqgrid.setRecords(page.getRecordCount());
		jqgrid.setTotal(page.getPageCount());
		for (TestJbpm vo : page.getEntities()) {
			Map<String, Object> map = EntityUtil.entityToTableMap(vo);
			mapList.add(map);
		}

		jqgrid.setRows(mapList);
		
		return jqgrid;
	}
	
	/**
	 * 保存信息
	 * @param xm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResultVo saveNote(@ModelAttribute TestJbpm vo,HttpServletRequest request, HttpServletResponse response) {
		boolean success = true;
		String message = "保存成功！";
		try{
			if( vo.getId() == null){
			testJbpmService.insert(vo);
			}else{
			testJbpmService.updateByPrimaryKey(vo);
			}
		}catch(Exception e){
			success = false;
			message = "保存失败！";
		}
		
		return new ResultVo(success, message).setData(vo);
	}
	
	/**
	 * 发起审核审批
	 * @param xm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/submitReview", method = RequestMethod.POST)
	public @ResponseBody ResultVo submitReview(@ModelAttribute TestJbpm vo,@ModelAttribute(value="ids") String ids,HttpServletRequest request, HttpServletResponse response) {
		boolean success = true;
		String message = "提交成功！";
		try{
			testJbpmService.submitReview(vo,ids);
		}catch(Exception e){
			success = false;
			message = "提交失败！";
		}
		
		return new ResultVo(success, message).setData(vo);
	}
	
	
	/**
	 * 通过实体类获取页面传的时间值可以绑定，自动转化
	 * 
	 * @return
	 * @since 2016年5月7日 下午13:34
	 * @author giam<giam@naswork.com>
	 * @version v1.0
	 * @param binder
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    dateFormat.setLenient(false);  
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));  
	}
}
