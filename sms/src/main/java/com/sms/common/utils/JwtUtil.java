package com.sms.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;
import java.io.UnsupportedEncodingException;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.*;

public class JwtUtil {
    // 过期时间 7 天  604800
    private static final long EXPIRE_TIME = 60*60*24*7;
    //密钥
    private static final String SECRET  = "1sf12sds21ie1inecs078j";

    //生成token
    public static String createJWT(String userNo,String mail){
        try{
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //附带用户名信息
            return JWT.create()
                    .withClaim("userNo",userNo)
                    .withClaim("mail",mail)
                    //到期时间
                    .withExpiresAt(date)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(algorithm);
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }

    /**
     * 校验 token 是否正确
     */
    public static boolean parseJWT(String token)  {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 查看token是否超时
     */
    public static boolean isTokenExpired(String token) {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
            Date expiresAt = verify.getExpiresAt();
            return expiresAt.after(new Date());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     */
    public static String getUserNo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userNo").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getMail(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("mail").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


}
