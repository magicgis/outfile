package com.naswork.module.purchase.controller.mpi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.MpiMessage;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.MpiService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping(value="/purchase/mpi")
public class MpiController extends BaseController {

	@Resource
	private MpiService mpiService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping(value="/toList",method=RequestMethod.GET)
	public String toList(){
		return "/purchase/mpi/mpiManageList";
	}
	
	/**
	 * 列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<MpiMessage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		mpiService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (MpiMessage mpiMessage : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(mpiMessage);
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
	 * 新增页面
	 * @return
	 */
	@RequestMapping(value="/addMpi",method=RequestMethod.GET)
	public String addMpi(){
		return "/purchase/mpi/addMpi";
	}
	
	
	/**
	 * 保存新增数据
	 * @param request
	 * @param mpiMessage
	 * @return
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute MpiMessage mpiMessage){
		try {
			mpiService.insertSelective(mpiMessage);
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	/**
	 * 保存修改数据
	 * @param request
	 * @param mpiMessage
	 * @return
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute MpiMessage mpiMessage){
		try {
			mpiService.updateByPrimaryKeySelective(mpiMessage);
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false,"修改异常！");
		}
	}
	
	/**
	 * 保存修改数据
	 * @param request
	 * @param mpiMessage
	 * @return
	 */
	@RequestMapping(value="/deleteMpi",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteMpi(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			mpiService.deleteByPrimaryKey(id);
			return new ResultVo(true, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false,"删除异常！");
		}
	}
	
}
