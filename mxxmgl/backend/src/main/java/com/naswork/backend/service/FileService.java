package com.naswork.backend.service;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.File;
import com.baomidou.mybatisplus.service.IService;
import com.naswork.backend.entity.Vo.FileVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-16
 */
public interface FileService extends IService<File> {

    Result insertFile(MultipartFile file,HttpServletRequest request);

    Result getFileListPage(HttpServletRequest request);

    Result deleteFileById(FileVo fileVo);

    ResponseEntity<byte[]> downloadFile(HttpServletRequest request);

    void downloadFile(HttpServletRequest request, HttpServletResponse response);

    Result getUsers(HttpServletRequest request);

    Result getFileListBySearch(FileVo fileVo);

    HttpServletResponse downloadBatchTest(List<Integer> ids, HttpServletRequest request, HttpServletResponse response);

    void downloadBatch(List<Integer> ids, HttpServletRequest request, HttpServletResponse response);

}
