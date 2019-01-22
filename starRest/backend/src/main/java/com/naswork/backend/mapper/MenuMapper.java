package com.naswork.backend.mapper;

import com.naswork.backend.entity.Menu;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getmenuListByUsername(String username);

    List<String> getpermissionListByUsername(String username);

    List<Menu> getMenuListByRoleId(@Param("roleId") int roleId, @Param("status") String status);

}
