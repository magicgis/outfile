package com.naswork.backend.service.impl;

import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Menu;
import com.naswork.backend.mapper.MenuMapper;
import com.naswork.backend.service.MenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Result getmenuListByUsername(String username) {
        List<String> menuList = menuMapper.getmenuListByUsername(username);
        return Result.requestBySuccess(menuList);
    }

    @Override
    public Result getpermissionListByUsername(String username) {
        List<String> permissionList = menuMapper.getpermissionListByUsername(username);
        return Result.requestBySuccess(permissionList);
    }

    @Override
    public Result getMenuListByRoleId(int roleId,String status) {
        List<Menu> menus = this.baseMapper.getMenuListByRoleId(roleId,status);
        return Result.requestBySuccess("success",menus);
    }
}
