package com.naswork.backend.mapper;

import com.naswork.backend.entity.RoleMenu;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    void deleteByRoleId(@Param("roleId") int roleId);

}
