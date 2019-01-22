package com.naswork.backend.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.naswork.backend.common.Result;
import com.naswork.backend.common.ResultCode;
import com.naswork.backend.entity.Project;
import com.naswork.backend.entity.Vo.ProjectVo;
import com.naswork.backend.mapper.ProjectMapper;
import com.naswork.backend.service.ProjectService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-09
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {



    @Override
    public Result getProjectListPage(HttpServletRequest request) {
        Project project = new Project();
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int pageRow = Integer.parseInt(request.getParameter("pageRow"));
//        int projectId = Integer.parseInt(request.getParameter("projectId"));
//        project.setId(projectId);
        Page<ProjectVo> page = new Page<>(pageNum,pageRow);
        page.setRecords(this.baseMapper.getProjectListPage(page,project));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public Result insertProject(ProjectVo projectVo) {
        Project project = new Project();
        project.setCreateTime(new Date());
        project.setCreateUser(projectVo.getCreateUser());
        project.setPlanDays(projectVo.getPlanDays());
        project.setProjectName(projectVo.getProjectName());
        project.setProjectDesc(projectVo.getProjectDesc());
        project.setProjectStatus("1");
        project.setProjectMoney(projectVo.getProjectMoney());
        project.setProjectCode(projectVo.getProjectCode());
        project.setProjectProgress("尚未填写");
        this.baseMapper.insert(project);
        return Result.requestBySuccess("success",project);
    }

    @Override
    public Result updateProject(ProjectVo projectVo) {
        Project project = new Project();
        project.setProjectName(projectVo.getProjectName());
        project.setProjectDesc(projectVo.getProjectDesc());
        project.setProjectStatus(projectVo.getProjectStatus());
        project.setPlanDays(projectVo.getPlanDays());
        project.setCreateUser(projectVo.getCreateUser());
        project.setId(projectVo.getProjectId());
        project.setProjectMoney(projectVo.getProjectMoney());
        project.setProjectCode(projectVo.getProjectCode());
        project.setProjectProgress(projectVo.getProjectProgress());
        this.baseMapper.updateById(project);
        return Result.requestBySuccess("success",project);
    }

    @Override
    public Result deleteProjectById(ProjectVo projectVo) {
        this.baseMapper.deleteProjectById(projectVo.getProjectId());
        return Result.requestBySuccess("success",projectVo);
    }

    @Override
    public Result getProjectListBySearch(ProjectVo projectVo) {
        Project project = new Project();
//        project.setPlanDays(projectVo.getPlanDays());
        project.setProjectName(projectVo.getProjectName());
        project.setProjectCode(projectVo.getProjectCode());
        project.setCreateUser(projectVo.getCreateUser());
        project.setProjectStatus(projectVo.getProjectStatus());
        int pageNum = projectVo.getPageNum();
        int pageRow = projectVo.getPageRow();
        Page<ProjectVo> page = new Page<>(pageNum,pageRow);
        page.setRecords(this.baseMapper.getProjectListPage(page,project));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public Result getUsers() {
        List<ProjectVo> projectVoList =   this.baseMapper.getUsers();
        return Result.requestBySuccess("success",projectVoList);
    }

    @Override
    public Result getProjectCodes() {
        Wrapper<Project> entity = new EntityWrapper<Project>();
//        entity.ne("project_status",0);
//        entity.ne("project_status",3);
        List<Project> projects = this.baseMapper.selectList(entity);
        return Result.requestBySuccess(ResultCode.SUCCESS.getDesc(),projects);
    }


}
