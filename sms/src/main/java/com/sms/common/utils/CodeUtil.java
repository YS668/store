package com.sms.common.utils;

import java.util.Random;

/**
 * 验证码工具类
 */
public class CodeUtil {

    private static final int SIX = 6;

    //生成6位验证码
    public static final String getCode(){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < SIX; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
