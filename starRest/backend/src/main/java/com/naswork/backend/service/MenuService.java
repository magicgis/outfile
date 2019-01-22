package com.naswork.backend.service;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Menu;
import com.baomidou.mybatisplus.service.IService;
import com.sun.org.apache.regexp.internal.RE;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
public interface MenuService extends IService<Menu> {

    Result getmenuListByUsername(String username);

    Result getpermissionListByUsername(String username);

    Result getMenuListByRoleId(int roleId, String status);

}
