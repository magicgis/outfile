package com.naswork.module.rfqstock.controller;

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
import com.naswork.model.rfqstock.nsncenter.CageInfo;
import com.naswork.model.rfqstock.nsncenter.StockInfo;
import com.naswork.service.rfqstock.NsnCenterService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;

@Controller
@RequestMapping("/rfqstock/nsncenter")
public class NsnCenterController extends BaseController {

	@Resource
	private NsnCenterService nsnCenterService;
	
	@RequestMapping(value = "/listByCage", method = RequestMethod.GET)
	public String listByCage(HttpServletRequest request){
		request.setAttribute("title", "按供应商查询");
		return "/rfqstock/nsncenter/listByCage";
	}

	@RequestMapping(value = "/listByNsnPart", method = RequestMethod.GET)
	public String listByNsnPart(HttpServletRequest request){
		request.setAttribute("title", "按件号查询");
		request.setAttribute("cageId", this.getString(request, "cageId"));
		return "/rfqstock/nsncenter/listByNsnPart";
	}
	
	@RequestMapping(value="/searchByCage", method=RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo searchByCage(HttpServletRequest request,
			HttpServletResponse response) {
		JQGridMapVo jqgrid = new JQGridMapVo();
		String cageId = this.getString(request, "cageId");
		if(!cageId.equals("")){
			PageModel<CageInfo> page=getPage(request);
			page.put("cageId", cageId);
			this.nsnCenterService.searchPageByCage(page, "", getSort(request));
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			for (CageInfo cageInfo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(cageInfo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());
		}
		
		return jqgrid;
	}

	@RequestMapping(value="/searchPageByNsnPart", method=RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo searchPageByNsnPart(HttpServletRequest request,
			HttpServletResponse response) {
		JQGridMapVo jqgrid = new JQGridMapVo();
		String cageId = this.getString(request, "cageId");
		String nsnId = this.getString(request, "nsnId");
		String partNum = this.getString(request, "partNum");
		String fuzzySearchForCage = this.getString(request, "fuzzySearchForCage");
		if(cageId.equals("") && nsnId.equals("") && partNum.equals("")){
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());			
		}
		else{
			PageModel<StockInfo> page=getPage(request);
			page.put("cageId", cageId);
			page.put("nsnId", nsnId);
			page.put("partNum", partNum);
			if(!fuzzySearchForCage.equals("")){
				page.put("fuzzySearchForCage", "1");
			}
			this.nsnCenterService.searchPageByNsnPart(page, "", getSort(request));
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			for (StockInfo stockInfo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(stockInfo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}
		
		return jqgrid;
	}

}
