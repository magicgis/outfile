package com.naswork.backend.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * @since 2018-12-12
 */
@TableName("project_task")
@Data
public class ProjectTask  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("finish_time")
    private Date finishTime;
    @TableField("plan_id")
    private Integer planId;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("task_outcome")
    private String taskOutcome;
    @TableField("create_user")
    private Integer createUser;
    @TableField("task_code")
    private String taskCode;
    @TableField("finish_user")
    private String finishUser;
    @TableField("finish_days")
    private String finishDays;

}
