package com.naswork.module.storage.controller.onpassagestorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.PurchaseApplicationForm;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.service.OnPassageStroageService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping(value="/storage/onpassagestorage")
public class OnPassageStorageController extends BaseController {

	@Resource
	private OnPassageStroageService onPassageStroageService;
	@Resource
	private UserService userService;
	
	/**
	 * 跳转在途库存页面
	 * @return
	 */
	@RequestMapping(value="/toListPage",method=RequestMethod.GET)
	public String toListPage() {
		return "/storage/onpassagestorage/onpassagestorageList";
	}
	
	
	/**
	 * 在途库存列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listPage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listPage(HttpServletRequest request) {
		PageModel<OnPassageStorage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = request.getParameter("searchString");
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		GridSort sort = getSort(request);
		
		onPassageStroageService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OnPassageStorage onPassageStroage: page.getEntities()) {
				if (!onPassageStroage.getAmount().equals(0.0)) {
					Map<String, Object> map = EntityUtil.entityToTableMap(onPassageStroage);
					mapList.add(map);
				}
				
			}
			jqgrid.setRows(mapList);
			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return jqgrid;
	}
	
	
}
