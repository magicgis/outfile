package com.naswork.backend.service.impl;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.naswork.backend.common.Result;
import com.naswork.backend.common.ResultCode;
import com.naswork.backend.entity.Project;
import com.naswork.backend.entity.ProjectPlan;
import com.naswork.backend.entity.User;
import com.naswork.backend.entity.Vo.ProjectPlanVo;
import com.naswork.backend.mapper.ProjectMapper;
import com.naswork.backend.mapper.ProjectPlanMapper;
import com.naswork.backend.mapper.UserMapper;
import com.naswork.backend.service.ProjectPlanService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.naswork.backend.utils.ExcelListeners.PlanExcelListener;
import com.naswork.backend.utils.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-11
 */
@Service
public class ProjectPlanServiceImpl extends ServiceImpl<ProjectPlanMapper, ProjectPlan> implements ProjectPlanService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Result getPlanListPage(HttpServletRequest request) {
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int pageRow = Integer.parseInt(request.getParameter("pageRow"));
        ProjectPlan projectPlan = new ProjectPlan();
        Page<ProjectPlanVo> page =  new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageRow);
        page.setRecords(this.baseMapper.getPlanListPage(page,projectPlan));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public Result insertPlan(ProjectPlanVo projectPlanVo) {
        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setCreateTime(new Date());
        projectPlan.setCreateUser(projectPlanVo.getCreateUser());
        projectPlan.setPlanType(projectPlanVo.getPlanType());
        projectPlan.setPlanDays(projectPlanVo.getPlanDays());
        projectPlan.setPlanDesc(projectPlanVo.getPlanDesc());
        projectPlan.setProjectId(projectPlanVo.getProjectId());
        projectPlan.setPlanCode(projectPlanVo.getPlanCode());
        projectPlan.setPlanStatus(1);
        projectPlan.setTaskMembers(projectPlanVo.getTaskMembers());
        this.baseMapper.insert(projectPlan);
        return Result.requestBySuccess(ResultCode.SUCCESS.getDesc(),projectPlan);
    }

    @Override
    public Result updatePlan(ProjectPlanVo projectPlanVo) {
        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setCreateUser(projectPlanVo.getCreateUser());
        projectPlan.setPlanType(projectPlanVo.getPlanType());
        projectPlan.setPlanDays(projectPlanVo.getPlanDays());
        projectPlan.setPlanDesc(projectPlanVo.getPlanDesc());
        projectPlan.setProjectId(projectPlanVo.getProjectId());
        projectPlan.setPlanStatus(projectPlanVo.getPlanStatus());
        projectPlan.setTaskMembers(projectPlanVo.getTaskMembers());
        this.baseMapper.updateById(projectPlan);
        return Result.requestBySuccess(ResultCode.SUCCESS.getDesc(),projectPlan);
    }

    @Override
    public Result insertPlanBatch(MultipartFile file,HttpServletRequest request) {
       User user = getUserByUserName(request);
       int userId = user.getId();
        try {
            InputStream inputStream = file.getInputStream();
            String projectCode = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            int projectId ;
            Wrapper<Project> entity = new  EntityWrapper<Project>();
            entity.eq("project_code",projectCode);
            List<Project> projectList = projectMapper.selectList(entity);
            if(projectList.size()>0){
                projectId = projectList.get(0).getId();
            }else{
                return Result.requestByError("error","项目编号不存在");
            }
            PlanExcelListener planExcelListener = new PlanExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX,null,planExcelListener);
            excelReader.read();
            List<Object> datas = planExcelListener.getDatas();
            datas.remove(0);
            List<ProjectPlan> projectPlans = new ArrayList<>();
            datas.forEach(data->{
                List<String> list = (List<String>)data;
                ProjectPlan projectPlan = new ProjectPlan();
                projectPlan.setPlanCode(list.get(0));
                projectPlan.setPlanType(list.get(1));
                projectPlan.setPlanDays(list.get(2));
                projectPlan.setTaskMembers(list.get(3));
                projectPlan.setPlanDesc(list.get(4));
                projectPlan.setCreateTime(new Date());
                projectPlan.setCreateUser(userId);
                projectPlan.setPlanStatus(1);
                projectPlan.setProjectId(projectId);
                projectPlans.add(projectPlan);
            });
            this.insertBatch(projectPlans);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.requestByError("server throw exception","");
        }
       return Result.requestBySuccess(ResultCode.SUCCESS.getDesc(),"");
    }

    public User getUserByUserName(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        Wrapper<User> userWrapper = new  EntityWrapper<User>();
        userWrapper.eq("user_name",username);
        List<User> userList= userMapper.selectList(userWrapper);
        User user = null;
        if(userList.size()>0){
            user = userList.get(0);
        }
        return user;
    }

    @Override
    public Result deletePlanById(ProjectPlanVo projectPlanVo) {
        Wrapper<ProjectPlan> entity = new EntityWrapper<>();
        entity.eq("id",projectPlanVo.getPlanId());
        this.baseMapper.delete(entity);
        return Result.requestBySuccess("success",projectPlanVo);
    }

    @Override
    public Result getPlanListBySearch(ProjectPlanVo projectPlanVo) {
        int pageNum = projectPlanVo.getPageNum();
        int pageRow = projectPlanVo.getPageRow();
        ProjectPlan projectPlan = new ProjectPlan();

        projectPlan.setPlanCode(projectPlanVo.getPlanCode());
        projectPlan.setTaskMembers(projectPlanVo.getTaskMembers());
        projectPlan.setPlanDesc(projectPlanVo.getPlanDesc());
        projectPlan.setPlanStatus(projectPlanVo.getPlanStatus());

        Page<ProjectPlanVo> page =  new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageRow);
        page.setRecords(this.baseMapper.getPlanListPage(page,projectPlan));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public Result checkPlanCode(HttpServletRequest request) {
        String planCode = request.getParameter("planCode").trim();
        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setPlanCode(planCode);
        projectPlan = this.baseMapper.selectOne(projectPlan);
        if(projectPlan == null || "".equals(projectPlan)){
            return Result.requestByError("计划编号不存在，请重新输入","");
        }else{
            return Result.requestBySuccess("success",projectPlan);
        }
    }

}



