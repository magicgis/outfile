package com.naswork.backend.service.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Role;
import com.naswork.backend.entity.RoleUser;
import com.naswork.backend.entity.User;
import com.naswork.backend.entity.Vo.UserPermission;
import com.naswork.backend.entity.Vo.UserVo;
import com.naswork.backend.mapper.RoleUserMapper;
import com.naswork.backend.mapper.UserMapper;
import com.naswork.backend.service.MenuService;
import com.naswork.backend.service.RoleService;
import com.naswork.backend.service.RoleUserService;
import com.naswork.backend.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.naswork.backend.utils.jwt.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private RoleUserMapper roleUserMapper;

    /*
     * @Author: Create by white
     * @Datetime: 2018/12/5 18:37
     * @Descrition: getUserByUsername 根据用户名获取用户
     * @Params: [username]
     * @Return: com.naswork.backend.common.Result
     * @Throws:
     */
    @Override
    public Result getUserByUsername(String username) {
        User user = new User();
        user.setUserName(username);
        User selectOne =  this.baseMapper.selectOne(user);
        return Result.requestBySuccess(selectOne);
    }

    /*
     * @Author: Create by white
     * @Datetime: 2018/12/5 18:37
     * @Descrition: getInfoByUsername 获取用户权限通过用户名
     * @Params: [username]
     * @Return: com.naswork.backend.common.Result
     * @Throws:
     */
    @Override
    public Result getInfoByUsername(String username) {
        UserPermission userPermission = getUserPermission(username);
        return Result.requestBySuccess(userPermission);
    }

    /*
     * @Author: Create by white
     * @Datetime: 2018/12/5 18:37
     * @Descrition: login 用户登陆
     * @Params: [request]
     * @Return: com.naswork.backend.common.Result
     * @Throws:
     */
    @Override
    public Result login(JSONObject jsonObject) throws JSONException {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        logger.info("账号："+username);
        logger.info("密码："+password);
        try{
            User user = new User();
            user.setUserName(username);
            User selectUser = this.baseMapper.selectOne(user);
            System.out.println("打印获取到的用户密码"+selectUser.getPassword());
            if(!selectUser.getPassword().equals(password)){
                return Result.requestByError("error password","");
            }else{
                String token = JWTUtil.sign(username,password);
                return Result.requestBySuccess("success",token);
            }
        }catch (Exception e){
            return Result.requestByError("fail");
        }
    }

    @Override
    public Result login(HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        logger.info("账号："+username);
        logger.info("密码："+password);
        try{
            User user = new User();
            user.setUserName(username);
            User selectUser = this.baseMapper.selectOne(user);
            if(!selectUser.getPassword().equals(password)){
                return Result.requestByError("error password");
            }else{
                String token = JWTUtil.sign(username,password);
                return Result.requestBySuccess("success",token);
            }
        }catch (Exception e){
            return Result.requestByError("fail");
        }
    }

    @Override
    public Result getInfoByUsername(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(authorization);
        UserPermission userPermission = getUserPermission(username);
        return Result.requestBySuccess("获取成功",userPermission);
    }

    @Override
    public Result logout() {
        try{
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
        }catch (CompletionException e){
            return Result.requestByError(e.getMessage());
        }
        return Result.requestBySuccess("logout success");
    }

    @Override
    public Result getUserListPageBySearch(HttpServletRequest request) {
        return null;
    }

    @Override
    public Result<Page> getUserListPage(HttpServletRequest request) {
        User user = new User();
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int pageRow = Integer.parseInt(request.getParameter("pageRow"));
        Page<UserVo> page = new Page<>(pageNum,pageRow);
        page.setRecords(this.baseMapper.getUserListPage(page,user));
        return Result.requestBySuccess("success",page);
    }

    @Override
    public Result updatePassword(HttpServletRequest request) {
        return null;
    }

    @Override
    public Result insertUser(UserVo userVo) {
        User user = new User();
        user.setUserName(userVo.getUserName());
        user.setNickName(userVo.getNickName());
        user.setSex(userVo.getSex());
        user.setCreateTime(new Date());
        user.setEmail(userVo.getEmail());
        user.setPassword(userVo.getPassword());
        user.setDeleteStatus("1");
        this.baseMapper.insert(user);
        int userId = user.getId();
        RoleUser roleUser = new RoleUser();
        roleUser.setRoleId(userVo.getRoleId());
        roleUser.setUserId(userId);
        roleUserService.insert(roleUser);
        return Result.requestBySuccess("新增成功");
    }

    @Override
    public Result deleteUserById(HttpServletRequest request) {
        return null;
    }

    @Override
    public Result insertByExcel(MultipartFile file, HttpServletRequest request) {
        return null;
    }

    public  UserPermission getUserPermission(String username){
        UserPermission userPermission = new UserPermission();
        userPermission = userMapper.getUserRoleByUsername(username);
        userPermission.setUserName(username);
        List<String> menuList = (List<String>) menuService.getmenuListByUsername(username).getData();
        userPermission.setMenuList(menuList);
        List<String> permissionList = (List<String>) menuService.getpermissionListByUsername(username).getData();
        userPermission.setPermissionList(permissionList);
        return userPermission;
    }

    @Override
    public Result updateUser(UserVo userVo) {
        User user = new User();
        user.setUserName(userVo.getUserName());
        user.setNickName(userVo.getNickName());
        user.setPassword(userVo.getPassword());
        user.setEmail(userVo.getEmail());
        user.setId(userVo.getUserId());
        user.setSex(userVo.getSex());
        user.setDeleteStatus(userVo.getDeleteStatus());
        if(String.valueOf(userVo.getRoleId()) != "" && userVo.getRoleId() != null){
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(userVo.getUserId());
            roleUser.setRoleId(userVo.getRoleId());
            roleUserMapper.updateById(roleUser);
        }
        this.baseMapper.updateById(user);
        return Result.requestBySuccess("更新成功",user);
    }


    @Override
    public Result getUserListByRoleId(int roleId) {
        List<User> users = this.baseMapper.getUserListByRoleId(roleId);
        return Result.requestBySuccess("success",users);
    }



}










