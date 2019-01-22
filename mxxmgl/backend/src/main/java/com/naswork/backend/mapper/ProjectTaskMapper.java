package com.naswork.backend.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.naswork.backend.entity.ProjectTask;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.naswork.backend.entity.Vo.ProjectTaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2018-12-12
 */
@Mapper
public interface ProjectTaskMapper extends BaseMapper<ProjectTask> {

    List<ProjectTaskVo> getTaskListPage(Pagination pagination, @Param("projectTask") ProjectTask projectTask);

}
