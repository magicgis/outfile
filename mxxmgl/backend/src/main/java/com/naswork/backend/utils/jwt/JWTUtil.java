package com.naswork.backend.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.naswork.backend.common.Constants;

import java.util.Date;

/**
 * @Program: JWTUtil
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-05 15:51:37
 **/

public class JWTUtil {

    /*
     * @Author: Create by white
     * @Datetime: 2018/15/5 16:33
     * @Descrition: verify  检验前端传过来的token是否正确
     * @Params: [token, username, password]
     * @Return: boolean
     * @Throws:
     */
    public static boolean verify(String token,String username,String password){
        try{
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withClaim("username",username)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    /*
     * @Author: Create by white
     * @Datetime: 2018/12/5 16:35
     * @Descrition: getUsername 从token获取到username
     * @Params: [token]
     * @Return: java.lang.String
     * @Throws:
     */
    public static String getUsername(String token){
        try{
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("username").asString();
        }catch (Exception e){
            return null;
        }
    }

    /*
     * @Author: Create by white
     * @Datetime: 2018/12/5 16:38
     * @Descrition: sign  生成签名
     * @Params: [username, password]
     * @Return: java.lang.String
     * @Throws:
     */
    public static String sign(String username,String password){

        try{
            Date date = new Date(System.currentTimeMillis()+ Constants.EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(password);
            return JWT.create()
                    .withClaim("username",username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            return null;
        }
    }

}
