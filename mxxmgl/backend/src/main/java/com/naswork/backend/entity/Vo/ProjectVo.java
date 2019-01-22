package com.naswork.backend.entity.Vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @Program: ProjectVo
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-09 23:46:57
 **/
@Data
public class ProjectVo {


    private Integer projectId;
    private String projectName;
    private String projectDesc;
    private String projectStatus;
    private Date createTime;
    private Date updateTime;
    private String planDays;
    private Date planFinishTime;
    private Date realFinishTime;
    private Integer createUser;

    private String userNickName;

    private Integer pageNum;
    private Integer pageRow;

    private Double projectMoney;

    private String projectCode;

    private String projectProgress;
}
