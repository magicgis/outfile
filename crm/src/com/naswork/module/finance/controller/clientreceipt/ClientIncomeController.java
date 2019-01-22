package com.naswork.module.finance.controller.clientreceipt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ClientReceipt;
import com.naswork.model.Income;
import com.naswork.model.IncomeDetail;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.ClientReceiptService;
import com.naswork.service.IncomeDetailService;
import com.naswork.service.IncomeService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping(value="/finance/clientincome")
public class ClientIncomeController extends BaseController{

	@Resource
	private IncomeService incomeService;
	@Resource
	private IncomeDetailService incomeDetailService;
	
	
	/**
	 * 收款管理
	 */
	@RequestMapping(value="/toIncome",method=RequestMethod.GET)
	public String toIncome() {
		return "/finance/clientincome/clientIncomeList";
	}
	
	/**
	 * 收款列表
	 */
	@RequestMapping(value="/income",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo income(HttpServletRequest request) {
		PageModel<Income> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		incomeService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Income income : page.getEntities()) {
//				income.setUncollected(income.getInvoiceSum()-income.getTotal());
				Map<String, Object> map = EntityUtil.entityToTableMap(income);
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
		Income income = incomeService.selectByPrimaryKey(id);
		request.setAttribute("income", income);
		return "/finance/clientincome/editClientIncome";
	}
	
	/**
	 * 保存修改
	 */
	@RequestMapping(value="/Edit",method=RequestMethod.POST)
	public @ResponseBody ResultVo Edit(HttpServletRequest request,@ModelAttribute Income income) {
		String message = "";
		boolean success = false;
		
		if (income.getId()!=null) {
			income.setUpdateTimestamp(new Date());
			incomeService.updateByPrimaryKeySelective(income);
			message = "更新成功！";
			success = true;
		}else {
			message = "更新失败！";
		}
		
		return new ResultVo(success, message);
		
		
	}
	
	/**
	 * 跳转明细
	 */
	@RequestMapping(value="/toIncomeDetail",method=RequestMethod.GET)
	public String toIncomeDetail(HttpServletRequest request){
		request.setAttribute("incomeId", getString(request, "id"));
		return "/finance/clientincome/incomeDetailList";
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
