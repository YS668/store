package com.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sms.common.Result;
import com.sms.common.utils.CodeUtil;
import com.sms.common.utils.RedisUtil;
import com.sms.entity.MailRequest;
import com.sms.entity.User;
import com.sms.service.SendMailService;
import com.sms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 发送邮件
 */
@Service
public class SendMailServiceImpl implements SendMailService {

    //邮件工具类
    @Autowired
    private JavaMailSender javaMailSender;

    @Resource
    private UserService userService;

    //发件人
    @Value("${spring.mail.username}")
    private String sendMailer;

    //查看用户是否存在
    public boolean checkMail(MailRequest mailRequest) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMail,mailRequest.getSendTo());
        List<User> list = userService.list(wrapper);
        return list.size() > 0;
    }


    @Override
    public Result sendSimpleMail(MailRequest mailRequest) {
        //判断用户是否存在
        if (!checkMail(mailRequest)){
            return Result.fail(MailRequest.USER_NOT);
        }
        //随机验证码
        String code = CodeUtil.getCode();
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(sendMailer);
        //邮件收件人(可多人)
        message.setTo(mailRequest.getSendTo());
        //邮件主题
        message.setSubject(MailRequest.SUBJECT);
        //邮件内容
        message.setText(mailRequest.SEND_MSG + code);
        //邮件发送时间
        message.setSentDate(new Date());
        //发送
        int i = 10/0;
        javaMailSender.send(message);
        //缓存验证码
        RedisUtil.setCode(mailRequest.getSendTo(),code);
        return Result.suc(null);
    }
}
