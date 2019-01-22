package com.naswork.module.system.controller.exchangerate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.Client;
import com.naswork.service.ExchangeRateService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;
import com.naswork.model.ExchangeRate;

@Controller
@RequestMapping(value="/system/exchangerate")
public class ExchangeRateController extends BaseController{
	
	@Resource
	private ExchangeRateService exchangeRateService;
	
	
	/**
	 * 汇率管理 
	 */
	@RequestMapping(value="/toExchangeRate",method=RequestMethod.GET)
	public String toExchangeRate() {
		return "/system/exchangerate/list";
	}
	
	
	/**
	 * 汇率列表
	 */
	@RequestMapping(value="/ExchangeRate",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo ExchangeRate(HttpServletRequest request) {
		PageModel<ExchangeRateVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		
		exchangeRateService.listPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExchangeRateVo exchangeRateVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(exchangeRateVo);
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
	 * 修改
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ExchangeRateVo exchangeRateVo = exchangeRateService.findById(id);
		request.setAttribute("exchangeRateVo", exchangeRateVo);
		return "/system/exchangerate/editMessage";
	}
	
	/**
	 * 保存修改
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute ExchangeRate exchangeRate) {
		String message = "";
		boolean success = false;
		
		if (exchangeRate.getCurrencyId()!=null) {
			exchangeRate.setUpdateTimestamp(new Date());
			exchangeRateService.updateByPrimaryKeySelective(exchangeRate);
			message = "修改成功！";
			success = true;
		}else {
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
	}

}
