package com.naswork.backend.controller;


import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Vo.Permission;
import com.naswork.backend.entity.Vo.RoleVo;
import com.naswork.backend.entity.Vo.UserVo;
import com.naswork.backend.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/getAllRoles")
    @RequiresPermissions("user:list")
    public Result getAllRoles(HttpServletRequest request){
        return roleService.getAllRoles(request);
    }

    @GetMapping("/getListRoles")
    @RequiresPermissions("user:list")
    public Result getListRoles(HttpServletRequest request){
        return roleService.getListRoles(request);
    }

    @GetMapping("/listAllPermission")
    @RequiresPermissions("user:list")
    public Result listAllPermission(HttpServletRequest request){
        return roleService.listAllPermission(request);
    }

    @PostMapping("/addRole")
    @RequiresPermissions("role:add")
    public Result addRole(@RequestBody RoleVo roleVo){
        return roleService.addRole(roleVo);
    }

    @PostMapping("/updateRole")
    @RequiresPermissions("role:update")
    public Result updateRole(@RequestBody RoleVo roleVo){
        return roleService.updateRole(roleVo);
    }

    @PostMapping("/deleteRole")
    @RequiresPermissions("role:delete")
    public Result deleteRole(@RequestBody RoleVo roleVo){
        return roleService.deleteRole(roleVo);
    }

}




