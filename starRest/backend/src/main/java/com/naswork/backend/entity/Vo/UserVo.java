package com.naswork.backend.entity.Vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @Program: UserVo
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-07 13:28:41
 **/
@Data
public class UserVo {


    private Integer userId;
    private String userName;
    private String password;
    private String email;
    private String sex;
    private Date createTime;

    private Date updateTime;
    private String nickName;

    private String roleName;
    private Integer roleId;
//    private String
    private String deleteStatus;

}
