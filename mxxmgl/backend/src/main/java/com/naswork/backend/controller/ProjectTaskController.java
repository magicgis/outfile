package com.naswork.backend.controller;


import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Vo.ProjectPlanVo;
import com.naswork.backend.entity.Vo.ProjectTaskVo;
import com.naswork.backend.service.ProjectTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto
 * @since 2018-12-12
 */
@RestController
@RequestMapping("/projectTask")
public class ProjectTaskController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @RequestMapping(value = "/getTaskListPage")
    @RequiresPermissions("task:list")
    public Result getTaskListPage(HttpServletRequest request){
        return projectTaskService.getTaskListPage(request);
    }

    @RequestMapping(value = "/insertOrupdateTask")
    @RequiresPermissions("task:update")
    public Result insertOrupdateTask(@RequestBody ProjectTaskVo projectTaskVo){
        return projectTaskService.insertOrupdateTask(projectTaskVo);
    }

    @PutMapping(value = "/insertOrupdateTask")
    @RequiresPermissions("task:update")
    public Result insertOrupdate(@RequestBody ProjectTaskVo projectTaskVo){
        return projectTaskService.insertOrupdateTask(projectTaskVo);
    }

    @RequiresPermissions("task:delete")
    @DeleteMapping("/deleteProjectTaskById")
    public Result deleteProjectTaskById(@RequestBody ProjectTaskVo projectTaskVo){
       return projectTaskService.deleteProjectTaskById(projectTaskVo);
    }

    @RequestMapping(value = "/insertTaskBatch")
    @RequiresPermissions("task:add")
    public Result insertTaskBatch(@RequestParam("file")MultipartFile file,HttpServletRequest request){
        return projectTaskService.insertTaskBatch(file,request);
    }

    @RequestMapping(value = "/getTaskListBySearch")
    @RequiresPermissions("task:list")
    public Result getTaskListBySearch(@RequestBody ProjectTaskVo projectTaskVo){
        return projectTaskService.getTaskListBySearch(projectTaskVo);
    }

}

