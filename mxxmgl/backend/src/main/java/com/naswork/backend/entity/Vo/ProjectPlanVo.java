package com.naswork.backend.entity.Vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @Program: ProjectPlanVo
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-11 10:24:15
 **/
@Data
public class ProjectPlanVo {

    private Integer planId;
    private Integer projectId;
    private Date createTime;
    private String planDesc;
    private String planType;
    private String planDays;
    private String planCode;
    private String projectCode;
    private Integer createUser;
    private Integer planStatus;

    private String createUserNickName;
    private String taskMembers;

    private String userNickName;

    private int pageNum;
    private int pageRow;

}
