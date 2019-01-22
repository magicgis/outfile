package com.naswork.backend.service;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.ProjectPlan;
import com.baomidou.mybatisplus.service.IService;
import com.naswork.backend.entity.Vo.ProjectPlanVo;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-11
 */
public interface ProjectPlanService extends IService<ProjectPlan> {

    Result getPlanListPage(HttpServletRequest request);

    Result insertPlan(ProjectPlanVo projectPlanVo);

    Result updatePlan(ProjectPlanVo projectPlanVo);

    Result insertPlanBatch(MultipartFile file,HttpServletRequest request);

    Result deletePlanById(ProjectPlanVo projectPlanVo);

    Result getPlanListBySearch(ProjectPlanVo projectPlanVo);

    Result checkPlanCode(HttpServletRequest request);

}
