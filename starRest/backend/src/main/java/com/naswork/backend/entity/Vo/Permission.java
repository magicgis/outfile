package com.naswork.backend.entity.Vo;

import lombok.Data;

/**
 * @Program: Permission
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-07 20:40:47
 **/

@Data
public class Permission {

    private Integer permissionId;

    private String permissionName;

    private Integer requiredPerm;

}
