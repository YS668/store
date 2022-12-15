package com.sms.common.shiro;

import com.sms.common.utils.RedisUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.stereotype.Component;

/**
 * 自定义验证码验证器
 */
@Component
public class MyCredentialsMatcherByCode extends SimpleCredentialsMatcher {

    //比较验证码
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        //拦截器时登录通过
        if (jwtToken.getPassword() == null){
            return true;
        }
        //邮箱
        String mail = String.valueOf(info.getPrincipals());
        //输入的验证码
        String inCode = String.valueOf(jwtToken.getPassword());
        //发送的验证
        String code = RedisUtil.getCode(mail);
        return inCode.equals(code);
    }
}
