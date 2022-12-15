package com.sms.common.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.Random;

/**
 * 验证码工具类
 */
public class SaltUtil {

    //加密次数
    public static final int ITERATIONS= 18;

    /**
     * 注册登录都是通过md5+salt的方式
     * 这是函数是用来生产随机salt（邮箱验证码）
     * @param n
     * @return
     */
    public static String getSalt(int n){
        String s="0123456789";
        for(char a='A';a<='Z';a++)
            s+=a;
        for(char a='a';a<='z';a++)
            s+=a;
        String res="";
        for(int i=0;i<n;i++){
            res+=s.charAt(new Random().nextInt(s.length()-1));
        }
        return res;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            String salt = getSalt(10);
            System.out.println(salt);
            Md5Hash md5Hash = new Md5Hash("123",salt,SaltUtil.ITERATIONS);
            System.out.println(md5Hash.toHex());
        }
    }
}
