package com.naswork.backend.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.naswork.backend.entity.ProjectPlan;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.naswork.backend.entity.Vo.ProjectPlanVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2018-12-11
 */
@Mapper
public interface ProjectPlanMapper extends BaseMapper<ProjectPlan> {

    List<ProjectPlanVo> getPlanListPage(Pagination pagination,@Param("projectPlan") ProjectPlan projectPlan);

}
