package com.naswork.backend.entity.Vo;

import lombok.Data;

import java.util.List;

/**
 * @Program: UserPermission
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-05 17:40:23
 **/

@Data
public class UserPermission {


    private List<String> menuList;

    private Integer roleId;

    private String userName;

    private String roleName;

    private List<String> permissionList;

    private Integer userId;

}



