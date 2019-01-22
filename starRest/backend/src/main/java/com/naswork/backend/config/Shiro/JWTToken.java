package com.naswork.backend.config.Shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Program: JWTToken
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-05 17:05:39
 **/

public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
