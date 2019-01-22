package com.naswork.backend.service;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.RoleMenu;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
public interface RoleMenuService extends IService<RoleMenu> {

    Result deleteByRoleId(int roleId);

}
