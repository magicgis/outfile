package com.naswork.backend.mapper;

import com.naswork.backend.entity.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.naswork.backend.entity.Vo.Permission;
import com.naswork.backend.entity.Vo.RoleVo;
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
public interface RoleMapper extends BaseMapper<Role> {

    void insertRolePermission(@Param("roleId") int roleId, @Param("permissions") List<Integer> permissions);

    List<RoleVo> getAllRolesByRoleId(@Param("roleId") int roleId);

    void removeOldPermission(@Param("roleId") int roleId, @Param("permissions") List<Integer> permissions);
}
