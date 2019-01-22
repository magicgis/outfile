package com.naswork.backend.service;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.ProjectTask;
import com.baomidou.mybatisplus.service.IService;
import com.naswork.backend.entity.Vo.ProjectTaskVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-12
 */
public interface ProjectTaskService extends IService<ProjectTask> {

    Result getTaskListPage(HttpServletRequest request);

    Result insertOrupdateTask(ProjectTaskVo projectTaskVo);

    Result deleteProjectTaskById(ProjectTaskVo projectTaskVo);

    Result insertTaskBatch(MultipartFile file,HttpServletRequest request);

    Result getTaskListBySearch(ProjectTaskVo projectTaskVo);

}
