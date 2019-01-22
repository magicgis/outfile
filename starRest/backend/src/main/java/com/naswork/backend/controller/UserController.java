package com.naswork.backend.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.naswork.backend.common.Result;
import com.naswork.backend.entity.User;
import com.naswork.backend.entity.Vo.UserVo;
import com.naswork.backend.service.UserService;
import com.naswork.backend.utils.CommonUtil;
import org.apache.shiro.authz.annotation.Logical;
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
 * @since 2018-12-05
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Result login(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"username,password");
        return userService.login(requestJson);
   }

//    @GetMapping(value = "/getInfoByUsername")
////    public Result getInfoByUsername(@RequestParam("username") String username){
////        return userService.getInfoByUsername(username);
////    }

    @GetMapping(value = "/getInfoByUsername")
    @RequiresPermissions("user:list")
    public Result getInfoByUsername(HttpServletRequest request){
        return userService.getInfoByUsername(request);
    }

    @PostMapping(value = "/logout")
    public Result logout(){
        return userService.logout();
    }

    @RequiresPermissions("user:list")
    @GetMapping("/getUserListPageBySearch")
    public Result getUserListPageBySearch(HttpServletRequest request){
        return userService.getUserListPageBySearch(request);
    }

    @RequiresPermissions("user:list")
    @GetMapping("/getUserListPage")
    public Result getUserListPage(HttpServletRequest request){
       return  userService.getUserListPage(request);
   }

    @RequiresPermissions("user:update")
    @PostMapping("/updatePassword")
    public Result updatePassword(HttpServletRequest request){
        return userService.updatePassword(request);
    }

    @PostMapping("/insertUser")
    @RequiresPermissions("user:add")
    public Result insertUser(@RequestBody UserVo userVo){
        return userService.insertUser(userVo);
    }

    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody UserVo userVo){
        return userService.updateUser(userVo);
    }

    @DeleteMapping("/deleteUserById")
    @RequiresPermissions("user:delete")
    public Result deleteUserById(HttpServletRequest request){
        return userService.deleteUserById(request);
    }

    @PostMapping("/insertByExcel")
    @RequiresPermissions(value = "user:add",logical = Logical.OR)
    public Result insertByExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request){

        Wrapper<User> entity = new EntityWrapper<User>();
        entity.eq("user_name","white");
        userService.selectList(entity);

        return userService.insertByExcel(file,request);
    }

}










