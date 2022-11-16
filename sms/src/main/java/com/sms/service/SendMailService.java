package com.sms.service;

import com.sms.common.Result;
import com.sms.entity.MailRequest;

public interface SendMailService {

    /**
     * 发送邮件
     */
    Result sendSimpleMail(MailRequest mailRequest);

}
