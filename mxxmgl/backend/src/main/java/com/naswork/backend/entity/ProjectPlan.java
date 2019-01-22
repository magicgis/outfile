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
 * @since 2018-12-11
 */
@TableName("project_plan")
@Data
public class ProjectPlan  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("project_id")
    private Integer projectId;
    @TableField("create_time")
    private Date createTime;
    @TableField("plan_desc")
    private String planDesc;
    @TableField("plan_type")
    private String planType;
    @TableField("plan_days")
    private String planDays;
    @TableField("plan_code")
    private String planCode;
    @TableField("create_user")
    private Integer createUser;
    @TableField("plan_status")
    private Integer planStatus;

    @TableField("task_members")
    private String taskMembers;

}
