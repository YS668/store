package com.sms.common.utils;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component //项目运行时就注入Spring容器
public class RedisUtil {

    @Resource
    private  RedisTemplate<String,String> redis;

    //赋值一个静态的redisTemplate
    public static RedisTemplate redisTemplate;

    @PostConstruct //此注解表示构造时赋值
    public void redisTemplate() {
        redisTemplate = this.redis;
    }

    /**
     * 验证码过期
     */
    public static final String CODE_EXPIRE = "验证码已过期";

    /**
     * 验证码错误
     */
    public static final String CODE_WRONG = "验证码错误，请重新输入";

    public static void setCode(String mail,String Code){
        redisTemplate.opsForValue().set(mail, Code,3000L, TimeUnit.SECONDS);
    }

    public static String getCode(String mail){
        return (String) redisTemplate.opsForValue().get(mail);
    }

}
