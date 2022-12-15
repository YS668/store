package com.sms.common.shiro;

import com.sms.common.utils.JwtUtil;
import com.sms.entity.User;
import com.sms.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class MyRealmByCode extends AuthorizingRealm {

    @Resource
    public UserService userService;

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
        String mail = JwtUtil.getMail(token);
        User user = userService.getByMail(mail);
        if (user == null){
            return null;
        }
        return new SimpleAuthenticationInfo(mail,mail,getName());
    }
}
