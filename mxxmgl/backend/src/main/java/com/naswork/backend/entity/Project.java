package com.naswork.backend.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
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
 * @since 2018-12-09
 */
@Data
public class Project  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("project_name")
    private String projectName;
    @TableField("project_desc")
    private String projectDesc;
    @TableField("project_status")
    private String projectStatus;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("plan_days")
    private String planDays;
    @TableField("plan_finish_time")
    private Date planFinishTime;
    @TableField("real_finish_time")
    private Date realFinishTime;
    @TableField("create_user")
    private Integer createUser;

    @TableField("project_money")
    private Double projectMoney;
    @TableField("project_code")
    private String projectCode;

    @TableField("project_progress")
    private String projectProgress;

}
