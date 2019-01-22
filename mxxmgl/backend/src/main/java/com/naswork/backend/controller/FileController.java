package com.naswork.backend.controller;


import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Vo.FileVo;
import com.naswork.backend.service.FileService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auto
 * @since 2018-12-16
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/insertFile")
    @RequiresPermissions("file:add")
    @ResponseBody
    public Result insertFile(@RequestParam("file")MultipartFile file,HttpServletRequest request){
        return fileService.insertFile(file,request);
    }

    @GetMapping("/getFileListPage")
    @RequiresPermissions("file:list")
    @ResponseBody
    public Result getFileListPage(HttpServletRequest request){
        return fileService.getFileListPage(request);
    }

    @DeleteMapping("/deleteFileById")
    @RequiresPermissions("file:delete")
    @ResponseBody
    public Result deleteFileById(@RequestBody FileVo fileVo){
        return fileService.deleteFileById(fileVo);
    }

    @GetMapping("/downloadFile")
    @RequiresPermissions("file:list")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response){
        fileService.downloadFile(request,response);
    }

    @PostMapping("/downloadBatch")
    @RequiresPermissions("file:list")
    public void downloadBatch(@RequestBody List<Integer> ids , HttpServletRequest request, HttpServletResponse response){
        fileService.downloadBatch(ids,request,response);
    }

    @GetMapping("/getUsers")
    @RequiresPermissions("file:list")
    @ResponseBody
    public Result getUsers(HttpServletRequest request){
        return fileService.getUsers(request);
    }

    @PostMapping("/getFileListBySearch")
    @RequiresPermissions("file:list")
    @ResponseBody
    public Result getFileListBySearch(@RequestBody FileVo fileVo){
        return fileService.getFileListBySearch(fileVo);
    }



}

