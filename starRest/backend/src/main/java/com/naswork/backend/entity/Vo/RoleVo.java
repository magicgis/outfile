package com.naswork.backend.entity.Vo;

import com.naswork.backend.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @Program: RoleVo
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-07 20:31:55
 **/

@Data
public class RoleVo {

    private Integer roleId;

    private String roleName;

    private List<MenuVo> menus;

    private List<User> users;

    private List<Integer> permissions;

}





