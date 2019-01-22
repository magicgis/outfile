package com.naswork.backend.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.naswork.backend.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.naswork.backend.entity.Vo.UserPermission;
import com.naswork.backend.entity.Vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    UserPermission getUserRoleByUsername(String username);

    List<UserVo> getUserListPage(Pagination pagination, @PathParam("user") User user);

    List<User> getUserListByRoleId(int roleId);

}
