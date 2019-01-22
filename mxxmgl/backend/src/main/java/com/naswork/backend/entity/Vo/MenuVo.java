package com.naswork.backend.entity.Vo;

import com.naswork.backend.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @Program: MenuVo
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-07 20:38:18
 **/
@Data
public class MenuVo {

    private String menuCode;
    private String menuName;
    private List<Permission> permissions;

}
