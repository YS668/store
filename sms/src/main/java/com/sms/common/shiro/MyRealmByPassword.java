package com.sms.common.shiro;


import com.sms.common.utils.JwtUtil;
import com.sms.entity.User;
import com.sms.service.UserService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * realm仅仅传递比较的信息
 * 不参与比较
 */
@Component
public class MyRealmByPassword extends AuthorizingRealm {

    @Resource
    private UserService userService;

    //获得自己定义的token
    //不重写会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getToken();
        String userNo = JwtUtil.getUserNo(token);
        User user = userService.getByNo(userNo);
        if (user == null){
            return null;
        }
        return new SimpleAuthenticationInfo(userNo,user.getPassword(),user.getMail());
    }
}
