package com.naswork.backend.service;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Project;
import com.baomidou.mybatisplus.service.IService;
import com.naswork.backend.entity.Vo.ProjectVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-09
 */
public interface ProjectService extends IService<Project> {

    Result getProjectListPage(HttpServletRequest request);

    Result insertProject(ProjectVo projectVo);

    Result updateProject(ProjectVo projectVo);

    Result deleteProjectById(ProjectVo projectVo);

    Result getProjectListBySearch(ProjectVo projectVo);

    Result getUsers();

    Result getProjectCodes();

}
