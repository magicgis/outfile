package com.naswork.backend.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.naswork.backend.entity.File;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.naswork.backend.entity.Vo.FileVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2018-12-16
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

    List<FileVo> getFileListPage(Pagination pagination, @Param("file") File file);

    List<FileVo> getUsers();

    void updateTimes(@Param("ids") List<Integer> ids);
}
