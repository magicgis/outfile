package com.naswork.backend.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.naswork.backend.entity.Project;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.naswork.backend.entity.Vo.ProjectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2018-12-09
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    List<ProjectVo> getProjectListPage(Pagination pagination, @Param("project") Project project);

    void deleteProjectById(@Param("projectId") int projectId);

    List<ProjectVo> getUsers();

}
