package com.naswork.backend.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.naswork.backend.common.Result;
import com.naswork.backend.entity.File;
import com.naswork.backend.entity.Project;
import com.naswork.backend.entity.User;
import com.naswork.backend.entity.Vo.FileVo;
import com.naswork.backend.entity.Vo.ProjectVo;
import com.naswork.backend.mapper.FileMapper;
import com.naswork.backend.service.FileService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.naswork.backend.utils.ZipUtil.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-16
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private ProjectPlanServiceImpl projectPlanService;

    @Value("${file.location}")
    private String fileLocation;

    @Override
    public Result insertFile(MultipartFile file,HttpServletRequest request) {
        Integer projectId  = Integer.parseInt(request.getParameter("projectId"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String dateStr = sdf.format(date);
        String uploadFileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."))
                +"-"
                +dateStr
                +"."
                +file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        boolean flag = saveFileToLocal(file,uploadFileName);
        if(flag){
            File fileData = new File();
            User user = projectPlanService.getUserByUserName(request);
            int user_id = user.getId();
            fileData.setCreateUser(user_id);
            fileData.setCreateTime(new Date());
            fileData.setDownloadTimes(0);
            fileData.setDeleteStatus(1);
            fileData.setSaveName(uploadFileName);
            fileData.setProjectId(projectId);
            try{
                String fileName = file.getOriginalFilename();
                logger.info("------------------------------开始读取文件");
//                String fileLocation = "D:\\pms\\"+uploadFileName;
//                String fileLocation = "/usr/local/pms/"+uploadFileName;
                String location = fileLocation+uploadFileName;
                long fileSize = file.getSize();
                fileData.setFileName(fileName);
                fileData.setFileSize(new Double(fileSize));
                fileData.setFileLocation(location);
                this.baseMapper.insert(fileData);
            }catch (Exception e){
                e.printStackTrace();
                return Result.requestByError("server throw exception","");
            }
            return Result.requestBySuccess("success","");
        }else{
            return Result.requestByError("server throw exception","");
        }

    }

    @Override
    public Result getFileListPage(HttpServletRequest request) {
        int pageRow = Integer.parseInt(request.getParameter("pageRow"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Integer projectId = null;
        if(!StringUtils.isBlank(request.getParameter("projectId"))){
            projectId = Integer.parseInt(request.getParameter("projectId"));
        }
        File file = new File();
        file.setProjectId(projectId);
        Page<FileVo> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageRow);
        page.setRecords(this.baseMapper.getFileListPage(page,file));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public Result deleteFileById(FileVo fileVo) {
        File file = new File();
        file.setId(fileVo.getFileId());
        file.setDeleteStatus(0);
        this.baseMapper.updateById(file);
        return Result.requestBySuccess("success","");
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request) {
        String fileId = request.getParameter("id");
        File file = new File();
        file.setId(Integer.parseInt(fileId));
        File resultFile = this.baseMapper.selectOne(file);
        String filelocation = resultFile.getFileLocation().trim();
        String fileName = resultFile.getFileName();
        logger.info("------------------------------开始读文件");
        java.io.File fileObject = new  java.io.File(filelocation);
        logger.info("----------文件信息----------"+fileObject);
        try{
            // 为防止中文文件名的乱码显示
            String agent = request.getHeader("User-Agent");
            if (agent.contains("MSIE")) {
                // IE浏览器
                fileName = URLEncoder.encode(fileName, "utf-8");
                fileName = fileName.replace("+", "");
            } else if (agent.contains("Firefox")) {
                //火狐浏览器
                BASE64Encoder base64Encoder = new BASE64Encoder();
                fileName = "=?utf-8?B?" + base64Encoder.encode(fileName.getBytes("utf-8")) + "?=";
            } else {
                //其它浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            // 编码后文件名中的空格被替换为“+”号，所以此处将替换编码后文件名中“+”号为UTF-8中空格的“%20”编码
            fileName = fileName.replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(fileObject),headers, HttpStatus.OK);
            return responseEntity;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveFileToLocal(MultipartFile file,String uploadFileName){
        OutputStream os = null;
        InputStream inputStream = null;
//        String path  = "D:\\pms\\";
//        String path = "/usr/local/pms";
         String path = fileLocation;
        try{
            byte[] bs = new byte[1024];
            int len;
            java.io.File file1 = new java.io.File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            os = new FileOutputStream(file1.getPath() + java.io.File.separator + uploadFileName);
            // 开始读取
             inputStream = file.getInputStream();
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                os.close();
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  true;
    }

    @Override
    public void downloadFile(HttpServletRequest request, HttpServletResponse response){
        String fileId = request.getParameter("id");
        File file = new File();
        file.setId(Integer.parseInt(fileId));
        File resultFile = this.baseMapper.selectOne(file);
        String filelocation = resultFile.getFileLocation().trim();
        String fileName = resultFile.getFileName();
        int downloadTimes = resultFile.getDownloadTimes()+1;
        file.setDownloadTimes(downloadTimes);
        this.baseMapper.updateById(file);
        java.io.File fileObject = new  java.io.File(filelocation);
        try{
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(filelocation);
            //创建输出流
            OutputStream out = response.getOutputStream();
            //创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区当中
            while((len=in.read(buffer))>0){
                //输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            //关闭文件输入流
            in.close();
            //关闭输出流
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Result getUsers(HttpServletRequest request) {
        List<FileVo> fileVos =   this.baseMapper.getUsers();
        return Result.requestBySuccess("success",fileVos);
    }

    @Override
    public Result getFileListBySearch(FileVo fileVo) {
        File file = new File();
        file.setCreateUser(fileVo.getCreateUser());
        file.setFileName(fileVo.getFileName());
        int pageNum = fileVo.getPageNum();
        int pageRow = fileVo.getPageRow();
        Page<FileVo> page = new Page<>(pageNum,pageRow);
        page.setRecords(this.baseMapper.getFileListPage(page,file));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public HttpServletResponse downloadBatchTest(List<Integer> ids,HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取得到的ids"+ids);
        //从数据库查出所有的文件
        List<File> files = new ArrayList<>();
        Wrapper<File> entity = new EntityWrapper<>();
        entity.in("id",ids);
        files = this.baseMapper.selectList(entity);
        List<java.io.File> fileList = new ArrayList<>();
        files.forEach(file -> {
            java.io.File file1 = new java.io.File(file.getFileLocation());
            fileList.add(file1);
        });
        //windows系统
//        String path = "D:\\pms\\";
        //linux系统
//        String path = "/usr/local/pms";
        String path = fileLocation;
        String zipFileName = path+"tempFile.zip";
        java.io.File file  = new java.io.File(zipFileName);
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            response.reset();
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            ZipUtil.zipFile(fileList, zipOut);
            zipOut.close();
            fous.close();
            return ZipUtil.downloadZip(file,response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void downloadBatch(List<Integer> ids, HttpServletRequest request, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String dateStr = sdf.format(date);

        System.out.println("获取得到的ids"+ids);
        //从数据库查出所有的文件
        List<File> files = new ArrayList<>();
        Wrapper<File> entity = new EntityWrapper<>();
        entity.in("id",ids);
        files = this.baseMapper.selectList(entity);
        List<java.io.File> fileList = new ArrayList<>();
        files.forEach(file -> {
            java.io.File file1 = new java.io.File(file.getFileLocation());
            fileList.add(file1);
        });
        //        windows系统
//        String path = "D:\\pms\\";
//        linux系统
//        String path = "/usr/local/pms";
        String path = fileLocation;
        String zipFileName = dateStr+"_tempFile.zip";
        String zipFileLocation = path+zipFileName;
        if(ZipFile(fileList,zipFileLocation)){
            this.baseMapper.updateTimes(ids);
            try{
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
                //读取要下载的文件，保存到文件输入流
                FileInputStream in = new FileInputStream(zipFileLocation);
                //创建输出流
                OutputStream out = response.getOutputStream();
                //创建缓冲区
                byte buffer[] = new byte[1024];
                int len = 0;
                //循环将输入流中的内容读取到缓冲区当中
                while((len=in.read(buffer))>0){
                    //输出缓冲区的内容到浏览器，实现文件下载
                    out.write(buffer, 0, len);
                }
                //关闭文件输入流
                in.close();
                //关闭输出流
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

//    将文件保存到压缩包
    private static boolean  ZipFile(List<java.io.File> files,String zipFileLocation){
//        windows系统
//        String path = "D:\\pms\\";
//        linux系统
//        String path = "/usr/local/pms";
//        String zipFileName = path+"tempFile.zip";
        java.io.File file  = new java.io.File(zipFileLocation);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            ZipUtil.zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }





}




