/**
 * 
 */
package com.naswork.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.model.gy.GyHelp;
import com.naswork.service.GyHelpService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

/**
 * @since 2016年5月7日 下午3:11:49
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Controller
@RequestMapping(value = "/help")
public class HelpController extends BaseController {
	@Resource
	private GyHelpService gyHelpService;

	/**
	 * 系统帮助管理
	 * 
	 * @return
	 * @since 2016年5月6日 下午12:00:56
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String helplist() {
		return "/help/list";
	}

	/**
	 * 系统帮助列表数据数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @since 2016年5月6日 下午12:01:47
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/list/data", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo helplistdata(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<GyHelp> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		gyHelpService.searchPage(page, request.getParameter("searchString"),
				getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (GyHelp help : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(help);
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
				exportService.exportGridToXls("帮助列表",
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
	 * 新增页面
	 * 
	 * @return
	 * @since 2016年5月6日 下午2:35:41
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addHelp() {
		return "/help/add";
	}

	/**
	 * 保存帮助
	 * 
	 * @param gyHelp
	 * @param request
	 * @return
	 * @throws Exception
	 * @since 2016年5月6日 下午2:43:09
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo addHelpFn(@ModelAttribute GyHelp gyHelp, HttpServletRequest request)
			throws Exception {
		boolean success = false;
		String message = "";
		try {
			gyHelpService.add(gyHelp);
			success = true;
			message = "保存成功！";
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "保存失败!";
		}
		return new ResultVo(success, message);
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 * @since 2016年5月6日 下午2:35:47
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
	public String modifyHelp(@PathVariable("id") String id,
			HttpServletRequest request) {
		GyHelp gyHelp = gyHelpService.findById(id);
		if (gyHelp != null) {
			request.setAttribute("gyHelp", JsonUtils.toJson(gyHelp));
		}
		return "/help/modify";
	}

	/**
	 * 修改帮助
	 * 
	 * @param gyHelp
	 * @param request
	 * @return
	 * @throws Exception
	 * @since 2016年5月6日 下午2:44:56
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo modifyHelpFn(@ModelAttribute GyHelp gyHelp,
			HttpServletRequest request) throws Exception {
		boolean success = false;
		String message = "";
		try {
			gyHelpService.modify(gyHelp);
			success = true;
			message = "修改成功！";
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "修改失败!";
		}
		return new ResultVo(success, message);
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 * @since 2016年5月6日 下午5:07:08
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo removeHelpFn(@RequestParam String id, HttpServletRequest request)
			throws Exception {
		boolean success = false;
		String message = "";
		try {
			gyHelpService.remove(id);
			success = true;
			message = "删除成功！";
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "删除失败!";
		}
		return new ResultVo(success, message);
	}

	/**
	 * 查看帮助
	 * 
	 * @return
	 * @since 2016年5月7日 上午9:25:06
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/view/{key}", method = RequestMethod.GET)
	public String viewHelp(@PathVariable String key, HttpServletRequest request) {
		GyHelp gyHelp = gyHelpService.findByCode(key);
		if (gyHelp != null) {
			request.setAttribute("gyHelp", gyHelp);
		}
		return "/help/detail";
	}
}
