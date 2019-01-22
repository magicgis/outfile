package com.naswork.backend.entity.Vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @Program: ProjectTaskVo
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-11 10:24:25
 **/
@Data
public class ProjectTaskVo {

    private Integer TaskId;
    private Date finishTime;
    private Integer planId;
    private String planCode;
    private Date createTime;
    private Date updateTime;
    private String taskOutcome;
    private Integer createUser;

    private String finishUser;
    private String finishDays;
    private String taskCode;
    private String createUserNickName;

    private int pageNum;
    private int pageRow;


}
