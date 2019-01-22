package com.naswork.module.white;

import com.naswork.common.controller.BaseController;
import com.naswork.model.Client;
import com.naswork.model.TjNewJdrcRealtime;
import com.naswork.service.TjNewJdrcRealtimeService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.*;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: white
 * @Date: create in 2018-08-22 15:39
 * @Description: 本人用于编写demo用
 * @Modify_By:
 */
@RequestMapping("/white/demo")
@Controller
public class WhiteController extends BaseController {

    @Resource
    private TjNewJdrcRealtimeService tjNewJdrcRealtimeService;
    @Resource
    private UserService userService;

    /**
     * @Author: Create by white
     * @Description: 跳转到列表页面
     * @Date: 2018-08-23 10:11
     * @Params: []
     * @Return: java.lang.String
     * @Throws:
     */
    @RequestMapping(value = "/toList", method = RequestMethod.GET)
    public String toList() {
        return "/white/demo/list";
    }

    /**
     * @Author: Create by white
     * @Description:  获取列表数据
     * @Date: 2018-08-23 10:12
     * @Params: [request]
     * @Return: com.naswork.vo.JQGridMapVo
     * @Throws:
     */
    @RequestMapping(value = "/getList",method = RequestMethod.POST)
    @ResponseBody
    public JQGridMapVo getList(HttpServletRequest request) {
        PageModel<TjNewJdrcRealtime> page = getPage(request);
        JQGridMapVo jqgrid = new JQGridMapVo();
        UserVo userVo = getCurrentUser(request);
        GridSort sort = getSort(request);
        String where = request.getParameter("searchString");

        tjNewJdrcRealtimeService.getList(page, where, sort);

        if (page.getEntities().size() > 0) {
            jqgrid.setPage(page.getPageNo());
            jqgrid.setRecords(page.getRecordCount());
            jqgrid.setTotal(page.getPageCount());
            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            for (TjNewJdrcRealtime tjNewJdrcRealtime: page.getEntities()) {
                Map<String, Object> map = EntityUtil.entityToTableMap(tjNewJdrcRealtime);
                mapList.add(map);
            }
            jqgrid.setRows(mapList);
            String exportModel = getString(request, "exportModel");
        } else {
            jqgrid.setRecords(0);
            jqgrid.setTotal(0);
            jqgrid.setRows(new ArrayList<Map<String, Object>>());
        }
        return jqgrid;
    }


    /**
     * @Author: Create by white
     * @Description: 获取景点名称列表
     * @Date: 2018-08-23 17:42
     * @Params: [request]
     * @Return: com.naswork.vo.ResultVo
     * @Throws:
     */
    @RequestMapping(value="/spotnameList",method=RequestMethod.POST)
    @ResponseBody
    public ResultVo spotnameList(HttpServletRequest request) {
        boolean success = false;
        String msg = "";
        UserVo userVo=getCurrentUser(request);
        RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
        PageModel<TjNewJdrcRealtime> page = getPage(request);
        List<TjNewJdrcRealtime> list=tjNewJdrcRealtimeService.getSpotNameList(page);
        success = true;
        JSONArray json = new JSONArray();
        json.add(list);
        msg =json.toString();
        System.out.println(msg);
        return new ResultVo(success, msg);
    }






}
