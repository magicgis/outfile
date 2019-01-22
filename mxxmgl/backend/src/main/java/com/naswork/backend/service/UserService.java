package com.naswork.backend.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.naswork.backend.common.Result;
import com.naswork.backend.entity.User;
import com.baomidou.mybatisplus.service.IService;
import com.naswork.backend.entity.Vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
public interface UserService extends IService<User> {

    Result getUserByUsername(String username);

    Result getInfoByUsername(String username);

    Result login(JSONObject jsonObject)  throws JSONException;

    Result login(HttpServletRequest httpServletRequest);

    Result getInfoByUsername(HttpServletRequest request);

    Result logout();

    Result getUserListPageBySearch(HttpServletRequest request);

    Result<Page> getUserListPage(HttpServletRequest request);

    Result updatePassword(HttpServletRequest request);

    Result insertUser(UserVo userVo);

    Result deleteUserById(HttpServletRequest request);

    Result insertByExcel(MultipartFile file, HttpServletRequest request);

    Result updateUser(UserVo userVo);

    Result getUserListByRoleId(int roleId);
}
