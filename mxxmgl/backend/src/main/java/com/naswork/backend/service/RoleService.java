package com.naswork.backend.service;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Role;
import com.baomidou.mybatisplus.service.IService;
import com.naswork.backend.entity.Vo.Permission;
import com.naswork.backend.entity.Vo.RoleVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
public interface RoleService extends IService<Role> {

    Result getAllRoles(HttpServletRequest request);

    Result getListRoles(HttpServletRequest request);

    Result listAllPermission(HttpServletRequest request);

    Result addRole(RoleVo roleVo);

    Result updateRole(RoleVo roleVo);

    Result deleteRole(RoleVo roleVo);

}
