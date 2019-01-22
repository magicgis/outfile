package com.naswork.backend.entity;

import com.baomidou.mybatisplus.annotations.TableId;
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
public class RoleUser  {

    private static final long serialVersionUID = 1L;

    @TableId("user_id")
    private Integer userId;
    @TableField("role_id")
    private Integer roleId;


}
