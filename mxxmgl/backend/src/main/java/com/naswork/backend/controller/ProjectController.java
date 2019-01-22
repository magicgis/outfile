package com.naswork.backend.controller;


import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Vo.ProjectVo;
import com.naswork.backend.service.ProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto
 * @since 2018-12-09
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/getProjectListPage")
    @RequiresPermissions("project:list")
    public Result getProjectListPage(HttpServletRequest request){
        return projectService.getProjectListPage(request);
    }

    @RequestMapping("/insertProject")
    @RequiresPermissions("project:add")
    public Result insertProject(@RequestBody ProjectVo projectVo){
       return projectService.insertProject(projectVo);
    }

    @RequestMapping("/updateProject")
    @RequiresPermissions("project:update")
    public Result updateProject(@RequestBody ProjectVo projectVo){
        return projectService.updateProject(projectVo);
    }

    @RequestMapping("/deleteProjectById")
    @RequiresPermissions("project:delete")
    public Result deleteProjectById(@RequestBody ProjectVo projectVo){
        return projectService.deleteProjectById(projectVo);
    }

    @RequestMapping("/getProjectListBySearch")
    @RequiresPermissions("project:list")
    public Result getProjectListBySearch(@RequestBody ProjectVo projectVo){
        return  projectService.getProjectListBySearch(projectVo);
    }

    @RequestMapping("/getUsers")
    @RequiresPermissions("project:list")
    public Result getUsers(){
        return projectService.getUsers();
    }

    @RequestMapping("/getProjectCodes")
    @RequiresPermissions("project:list")
    public Result getProjectCodes(){
        return projectService.getProjectCodes();
    }

}

