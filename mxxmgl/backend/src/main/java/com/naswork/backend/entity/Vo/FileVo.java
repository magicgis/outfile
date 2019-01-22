package com.naswork.backend.entity.Vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @Program: FileVo
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-16 17:17:03
 **/
@Data
public class FileVo {

    private Integer fileId;
    private String fileName;
    private Double fileSize;
    private String fileLocation;
    private String deleteStatus;
    private Integer downloadTimes;
    private Integer createUser;
    private Date createTime;

    private String userNickName;

    private String saveName;

    private int pageNum;
    private int pageRow;

    private Integer projectId;
    private String  projectCode;

}
