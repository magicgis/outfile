package com.naswork.backend.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Data
public class Menu  {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @TableField("menu_code")
    private String menuCode;
    @TableField("menu_name")
    private String menuName;
    @TableField("permission_code")
    private String permissionCode;
    @TableField("permission_name")
    private String permissionName;



}
