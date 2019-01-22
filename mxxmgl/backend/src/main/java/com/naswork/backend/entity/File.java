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
 * @since 2018-12-16
 */
@Data
public class File  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("file_name")
    private String fileName;
    @TableField("file_size")
    private Double fileSize;
    @TableField("file_location")
    private String fileLocation;
    @TableField("delete_status")
    private Integer deleteStatus;
    @TableField("download_times")
    private Integer downloadTimes;
    @TableField("create_user")
    private Integer createUser;
    @TableField("create_time")
    private Date createTime;

    @TableField("save_name")
    private String saveName;

    @TableField("project_id")
    private Integer projectId;
}
