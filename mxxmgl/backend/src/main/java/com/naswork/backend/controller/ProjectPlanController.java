package com.naswork.backend.controller;


import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Vo.ProjectPlanVo;
import com.naswork.backend.service.ProjectPlanService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto
 * @since 2018-12-11
 */
@RestController
@RequestMapping("/projectPlan")
public class ProjectPlanController {

    @Autowired
    private ProjectPlanService projectPlanService;

    @RequestMapping("/getPlanListPage")
    @RequiresPermissions("plan:list")
    public Result getPlanListPage(HttpServletRequest request){
        return projectPlanService.getPlanListPage(request);
    }

    @RequestMapping("/insertPlan")
    @RequiresPermissions("plan:add")
    public Result insertPlan(@RequestBody ProjectPlanVo projectPlanVo){
        return projectPlanService.insertPlan(projectPlanVo);
    }

    @RequestMapping("/updatePlan")
    @RequiresPermissions("plan:update")
    public Result updatePlan(@RequestBody ProjectPlanVo projectPlanVo){
        return projectPlanService.updatePlan(projectPlanVo);
    }

    @RequestMapping(value="/insertPlanBatch")
    @RequiresPermissions("plan:add")
    public Result insertPlanBatch(@RequestParam("excel") MultipartFile file,HttpServletRequest request){
        return projectPlanService.insertPlanBatch(file,request);
    }

    @RequestMapping(value = "/deletePlanById")
    @RequiresPermissions("plan:delete")
    public Result deletePlanById(@RequestBody ProjectPlanVo projectPlanVo){
          return projectPlanService.deletePlanById(projectPlanVo);
    }

    @RequestMapping(value = "/getPlanListBySearch")
    @RequiresPermissions("plan:list")
    public Result getPlanListBySearch(@RequestBody ProjectPlanVo projectPlanVo){
        return projectPlanService.getPlanListBySearch(projectPlanVo);
    }

    @RequestMapping(value = "/checkPlanCode")
    @RequiresPermissions("plan:list")
    public Result checkPlanCode(HttpServletRequest request){
        return projectPlanService.checkPlanCode(request);
    }


}





