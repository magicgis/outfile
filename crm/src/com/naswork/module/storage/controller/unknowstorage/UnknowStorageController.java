package com.naswork.module.storage.controller.unknowstorage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.UnknowStorageDetail;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.UnknowStorageDetailService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/storage/unknowstorage")
public class UnknowStorageController extends BaseController {
	
	@Resource
	private UnknowStorageDetailService unknowStorageDetailService;
	
	/**
	 * 列表列表页面
	 * @return
	 */
	@RequestMapping(value="/toList",method=RequestMethod.GET)
	public String toList(){
		return "/storage/unknowstorage/List";
	}
	
	
	/**
	 * 显示列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<UnknowStorageDetail> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		unknowStorageDetailService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (UnknowStorageDetail unknowStorageDetail : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(unknowStorageDetail);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");

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
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd(){
		return "/storage/unknowstorage/addoredit";
	}
	
	/**
	 * 保存新增
	 * @param request
	 * @param unknowStorageDetail
	 * @return
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute UnknowStorageDetail unknowStorageDetail){
		try {
			unknowStorageDetailService.insertSelective(unknowStorageDetail);
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	/**
	 * 修改页面
	 * @return
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request){
		Integer id = new Integer(getString(request, "id"));
		UnknowStorageDetail unknowStorageDetail = unknowStorageDetailService.selectByPrimaryKey(id);
		request.setAttribute("unknowStorageDetail", unknowStorageDetail);
		return "/storage/unknowstorage/addoredit";
	}
	
	/**
	 * 保存修改
	 * @param request
	 * @param unknowStorageDetail
	 * @return
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute UnknowStorageDetail unknowStorageDetail){
		try {
			unknowStorageDetail.setUpdateTimestamp(new Date());
			unknowStorageDetailService.updateByPrimaryKeySelective(unknowStorageDetail);
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
	
}
