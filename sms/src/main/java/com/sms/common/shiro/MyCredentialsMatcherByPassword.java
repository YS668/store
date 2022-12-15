package com.sms.common.shiro;

import com.sms.common.utils.JwtUtil;
import com.sms.common.utils.SaltUtil;
import com.sms.entity.User;
import com.sms.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义密码验证器
 */
@Component
public class MyCredentialsMatcherByPassword extends SimpleCredentialsMatcher {

    @Resource
    private UserService userService;

    //比较密码
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken=(JwtToken) token;
        //拦截器时登录通过
        if (jwtToken.getPassword() == null){
            return true;
        }
        //用户名
        String userNo = String.valueOf(info.getPrincipals());
        //用户输入的密码
        String inPassword = new String(jwtToken.getPassword());
        //从数据库查询盐值
        User user = userService.getByNo(userNo);
        String salt = user.getSalt();
        //数据库的密码
        String dbPassword = (String) info.getCredentials();
        //进行密码比对
        Md5Hash md5Hash = new Md5Hash(inPassword, salt, SaltUtil.ITERATIONS);
        return md5Hash.toHex().equals(dbPassword);
    }
}
