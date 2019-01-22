package com.naswork.backend.config.Shiro;

import com.naswork.backend.utils.jwt.JWTUtil;
import org.apache.shiro.realm.AuthorizingRealm;
import com.naswork.backend.entity.User;
import com.naswork.backend.entity.Vo.UserPermission;
import com.naswork.backend.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;



/**
 * @Program: UserRealm
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-05 17:07:36
 **/

@Service
public class UserRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.toString());
        User user = (User) userService.getUserByUsername(username).getData();
        UserPermission userPermission = (UserPermission) userService.getInfoByUsername(username).getData();
        logger.info("用户权限："+userPermission.getPermissionList());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        simpleAuthorizationInfo.addRole(user.getRole());
//        Set<String> permission = new HashSet<>(Arrays.asList(userPermission.getPermission().split(",")));
        simpleAuthorizationInfo.addRole(userPermission.getRoleName());
        simpleAuthorizationInfo.addStringPermissions((Collection<String>) userPermission.getPermissionList());
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = (User) userService.getUserByUsername(username).getData();
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }
        if (! JWTUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}



