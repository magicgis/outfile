package com.naswork.backend.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
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
public class RoleMenu  {

    private static final long serialVersionUID = 1L;

    @TableField("menu_id")
    private Integer menuId;
    @TableField("role_id")
    private Integer roleId;



}
