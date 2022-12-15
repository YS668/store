package com.sms.service.impl;

import com.sms.entity.User;
import com.sms.mapper.UserMapper;
import com.sms.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sms
 * @since 2022-11-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getByNo(String username) {
        return userMapper.getByNo(username);
    }

    @Override
    public User getByMail(String Mail) {
        return userMapper.getByMail(Mail);
    }
}
