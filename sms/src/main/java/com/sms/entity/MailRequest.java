package com.sms.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送邮件请求体
 */
@Data
public class MailRequest implements Serializable {

    /**
     * 邮件内容
     */
    public static final String SEND_MSG = "欢迎来到仓库管理系统，你本次登录的验证码5分钟内有效，验证码为：";

    /**
     * 邮件主题
     */
    public static final String SUBJECT = "仓库管理系统";

    /**
     * 用户不存在
     */
    public static final String USER_NOT = "该邮箱不是系统用户";

    /**
     * 接收人
     */
    private String sendTo;

    /**
     *验证码
     */
    private String code;

    /**
     * 附件路径
     */
    private String filePath;

}
