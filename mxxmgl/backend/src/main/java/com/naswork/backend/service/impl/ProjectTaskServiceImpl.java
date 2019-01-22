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
import com.naswork.backend.entity.ProjectTask;
import com.naswork.backend.entity.User;
import com.naswork.backend.entity.Vo.ProjectPlanVo;
import com.naswork.backend.entity.Vo.ProjectTaskVo;
import com.naswork.backend.mapper.ProjectPlanMapper;
import com.naswork.backend.mapper.ProjectTaskMapper;
import com.naswork.backend.service.ProjectPlanService;
import com.naswork.backend.service.ProjectTaskService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.naswork.backend.utils.ExcelListeners.PlanExcelListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-12
 */
@Service
public class ProjectTaskServiceImpl extends ServiceImpl<ProjectTaskMapper, ProjectTask> implements ProjectTaskService {

    @Autowired
    private ProjectPlanServiceImpl projectPlanService;

    @Autowired
    private ProjectPlanMapper projectPlanMapper;

    @Override
    public Result getTaskListPage(HttpServletRequest request) {
        ProjectTask projectTask = new ProjectTask();
        int pageRow = Integer.parseInt(request.getParameter("pageRow"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        Page<ProjectTaskVo> page = new Page<>();
        page.setSize(pageRow);
        page.setCurrent(pageNum);
        page.setRecords(this.baseMapper.getTaskListPage(page,projectTask));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public Result insertOrupdateTask(ProjectTaskVo projectTaskVo) {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setTaskCode(projectTaskVo.getTaskCode());
        projectTask.setFinishDays(projectTaskVo.getFinishDays());
        projectTask.setTaskOutcome(projectTaskVo.getTaskOutcome());
        projectTask.setPlanId(projectTaskVo.getPlanId());
        projectTask.setFinishUser(projectTaskVo.getFinishUser());
        projectTask.setFinishTime(projectTaskVo.getFinishTime());

        if("".equals(projectTaskVo.getTaskId()) || projectTaskVo.getTaskId() == null){
            projectTask.setCreateUser(projectTaskVo.getCreateUser());
            projectTask.setCreateTime(new Date());
            this.baseMapper.insert(projectTask);
            return Result.requestBySuccess("insert success","");
        }else{
            projectTask.setPlanId(projectTaskVo.getPlanId());
            this.baseMapper.updateById(projectTask);
            return Result.requestBySuccess("update success","");
        }

    }

    @Override
    public Result deleteProjectTaskById(ProjectTaskVo projectTaskVo) {
        Wrapper<ProjectTask> entity = new EntityWrapper<>();
        entity.eq("id",projectTaskVo.getTaskId());
        this.baseMapper.delete(entity);
        return Result.requestBySuccess("success",projectTaskVo);
    }

    @Override
    public Result insertTaskBatch(MultipartFile file, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        User user = projectPlanService.getUserByUserName(request);
        int userId = user.getId();
        try {
            InputStream inputStream = file.getInputStream();
            String planCode = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            int planId ;
            Wrapper<ProjectPlan> entity = new  EntityWrapper<ProjectPlan>();
            entity.eq("plan_code",planCode);
            List<ProjectPlan> projectPlanListList = projectPlanMapper.selectList(entity);
            if(projectPlanListList.size()>0){
                planId = projectPlanListList.get(0).getId();
            }else{
                return Result.requestByError("error","计划编号不存在");
            }
            PlanExcelListener planExcelListener = new PlanExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX,null,planExcelListener);
            excelReader.read();
            List<Object> datas = planExcelListener.getDatas();
            datas.remove(0);
            List<ProjectTask> projectTasks = new ArrayList<>();
            datas.forEach(data->{
                List<String> list = (List<String>)data;
                ProjectTask projectTask = new ProjectTask();
                projectTask.setPlanId(planId);
                projectTask.setCreateUser(userId);
                projectTask.setCreateTime(new Date());
                projectTask.setTaskCode(list.get(0));
                projectTask.setTaskOutcome(list.get(1));
                projectTask.setFinishDays(list.get(2));
                projectTask.setFinishUser(list.get(3));
                try {
                    projectTask.setFinishTime(sdf.parse(list.get(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                projectTasks.add(projectTask);
            });
            this.insertBatch(projectTasks);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.requestByError("server throw exception","");
        }
        return Result.requestBySuccess(ResultCode.SUCCESS.getDesc(),"");
    }

    @Override
    public Result getTaskListBySearch(ProjectTaskVo projectTaskVo) {
        int pageNum = projectTaskVo.getPageNum();
        int pageRow = projectTaskVo.getPageRow();

        ProjectTask projectTask = new ProjectTask();
        projectTask.setFinishUser(projectTaskVo.getFinishUser());
        projectTask.setTaskCode(projectTaskVo.getTaskCode());
        projectTask.setTaskOutcome(projectTaskVo.getTaskOutcome());
        projectTask.setFinishDays(projectTaskVo.getFinishDays());
        Page<ProjectTaskVo> page =  new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageRow);
        page.setRecords(this.baseMapper.getTaskListPage(page,projectTask));
        return Result.requestBySuccess("success",page);
    }


}
