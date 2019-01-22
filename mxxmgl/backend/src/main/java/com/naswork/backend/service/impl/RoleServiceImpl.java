package com.naswork.backend.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.naswork.backend.common.Result;
import com.naswork.backend.entity.Menu;
import com.naswork.backend.entity.Role;
import com.naswork.backend.entity.User;
import com.naswork.backend.entity.Vo.MenuVo;
import com.naswork.backend.entity.Vo.Permission;
import com.naswork.backend.entity.Vo.RoleVo;
import com.naswork.backend.mapper.MenuMapper;
import com.naswork.backend.mapper.RoleMapper;
import com.naswork.backend.service.MenuService;
import com.naswork.backend.service.RoleMenuService;
import com.naswork.backend.service.RoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.naswork.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2018-12-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public Result getAllRoles(HttpServletRequest request) {
        Wrapper<Role> enity = new EntityWrapper<>();
        List<Role> roleList = this.baseMapper.selectList(enity);
        return Result.requestBySuccess(roleList);
    }

    @Override
    public Result getListRoles(HttpServletRequest request) {
        List<RoleVo> roleVoList = new ArrayList<>();
        Wrapper<Role> enity = new EntityWrapper<>();
        List<Role> roleList = this.baseMapper.selectList(enity);
        //循环遍历拼接roleList
        for(Role role : roleList){
            RoleVo roleVo = new RoleVo();
            roleVo.setRoleId(role.getId());
            roleVo.setRoleName(role.getRoleName());
            //查找返回menus
            List<MenuVo> menus = getMenus(role.getId());
            //查找返回users
            List<User> users = getUsers(role.getId());
            roleVo.setMenus(menus);
            roleVo.setUsers(users);
            roleVoList.add(roleVo);
        }

        return Result.requestBySuccess("查询成功",roleVoList);
    }


    private List<MenuVo> getMenus(int roleId){
        List<MenuVo> menuVos = new ArrayList<>();
        String status = "";
        List<Menu> menus = (ArrayList<Menu>)menuService.getMenuListByRoleId(roleId,status).getData();
        menus.forEach(menu ->{
            MenuVo menuVo = new MenuVo();
            menuVo.setMenuCode(menu.getMenuCode());
            menuVo.setMenuName(menu.getMenuName());
            List<Permission> permissions = new ArrayList<>();
            String menuCode = menu.getMenuCode();
            System.out.println("打印得到的menucode   "+menuCode+"------------");
            List<Menu> menuDetail = (ArrayList<Menu>)menuService.getMenuListByRoleId(roleId,menuCode).getData();
            menuDetail.forEach(menu1 -> {
                int i = menuDetail.indexOf(menu1);
                Permission permission = new Permission();
                permission.setPermissionId(menu1.getId());
                permission.setPermissionName(menu1.getPermissionName());
                permission.setRequiredPerm(i+1);
                permissions.add(permission);
                menuVo.setPermissions(permissions);
            });
            menuVos.add(menuVo);
        });
        return menuVos;
    }

    private List<User> getUsers(int roleId){
        List<User> users = (ArrayList<User>)userService.getUserListByRoleId(roleId).getData();
        return users;
    }


    @Override
    public Result listAllPermission(HttpServletRequest request) {
        List<MenuVo> menuVos = new ArrayList<>();
        String status = "";
        List<Menu> menus = (ArrayList<Menu>)menuService.getMenuListByRoleId(0,status).getData();
        menus.forEach(menu -> {
            MenuVo menuVo = new MenuVo();
            menuVo.setMenuName(menu.getMenuName());
            menuVo.setMenuCode(menu.getMenuCode());
            List<Permission> permissionList = new ArrayList<>();
            String menuCode = menu.getMenuCode().trim();
            List<Menu> menu2 = (ArrayList<Menu>)menuService.getMenuListByRoleId(0,menuCode).getData();
            menu2.forEach(menu1 -> {
                Integer i = menu2.indexOf(menu1);
                Permission permission = new Permission();
                permission.setPermissionName(menu1.getPermissionName());
                permission.setPermissionId(menu1.getId());
                permission.setRequiredPerm(i+1);
                permissionList.add(permission);
            });
            menuVo.setPermissions(permissionList);
            menuVos.add(menuVo);
        });
        return Result.requestBySuccess("success",menuVos);
    }

    @Override
    public Result addRole(RoleVo roleVo) {
        Role role = new Role();
        role.setRoleName(roleVo.getRoleName());
        this.baseMapper.insert(role);
        System.out.println("打印获取到的角色id"+role.getId());
        this.baseMapper.insertRolePermission(role.getId(),roleVo.getPermissions());
        return Result.requestBySuccess("success",role);
    }

    @Override
    public Result updateRole(RoleVo roleVo) {
        int roleId = roleVo.getRoleId();
        List<Integer> newPerms = roleVo.getPermissions();
        List<RoleVo> roleVos = this.baseMapper.getAllRolesByRoleId(roleId);
//        Set<Integer> oldPerms = (Set<Integer>)roleVos.get(0).getPermissions();
        List<Integer> oldPerms = roleVos.get(0).getPermissions();
        //修改角色名称
        Role role = this.baseMapper.selectById(roleVo.getRoleId());
        role.setRoleName(roleVo.getRoleName());
        this.baseMapper.updateById(role);
        //添加新权限
        saveNewPermission(roleVo.getRoleId(),newPerms,oldPerms);
        //移除旧的不再拥有的权限
        deleteOldPermission(roleVo.getRoleId(),newPerms,oldPerms);
        return Result.requestBySuccess("update success","");
    }

    private void saveNewPermission(int roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms){
        List<Integer> waitInsert = new ArrayList<>();
        for (Integer newPerm : newPerms) {
            if (!oldPerms.contains(newPerm)) {
                waitInsert.add(newPerm);
            }
        }
        if (waitInsert.size() > 0) {
            this.baseMapper.insertRolePermission(roleId, waitInsert);
        }
    }

    private void deleteOldPermission(int roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms){
        List<Integer> waitRemove = new ArrayList<>();
        for (Integer oldPerm : oldPerms) {
            if (!newPerms.contains(oldPerm)) {
                waitRemove.add(oldPerm);
            }
        }
        if (waitRemove.size() > 0) {
            this.baseMapper.removeOldPermission(roleId, waitRemove);
        }
    }

    @Override
    public Result deleteRole(RoleVo roleVo) {
        System.out.println("打印得到的roleId:"+roleVo.getRoleId());
//      int roleId = Integer.parseInt(request.getParameter("roleId"));
        int roleId = roleVo.getRoleId();
        roleMenuService.deleteByRoleId(roleId);
        this.baseMapper.deleteById(roleId);
        return Result.requestBySuccess("success","");
    }
}








