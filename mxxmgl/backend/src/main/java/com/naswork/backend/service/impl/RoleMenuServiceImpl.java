package com.naswork.backend.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.naswork.backend.common.Result;
import com.naswork.backend.entity.RoleMenu;
import com.naswork.backend.mapper.RoleMenuMapper;
import com.naswork.backend.service.RoleMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public Result deleteByRoleId(int roleId) {
        this.baseMapper.deleteByRoleId(roleId);
        return Result.requestBySuccess("success","");
    }
}
