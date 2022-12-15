package com.sms.service;

import com.sms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sms
 * @since 2022-11-10
 */
public interface UserService extends IService<User> {

    User getByNo(String username);

    User getByMail(String Mail);
}
