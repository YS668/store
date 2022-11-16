package com.sms.controller;

import com.sms.common.Result;
import com.sms.entity.MailRequest;
import com.sms.service.SendMailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mail")
public class SendMailController {

    @Resource
    private SendMailService sendMailService;

    @PostMapping("/send")
    public Result sendSimpleMail(@RequestBody MailRequest mailRequest){
        return sendMailService.sendSimpleMail(mailRequest);
    }
}
