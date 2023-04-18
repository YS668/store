package com.sms.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author sms
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Customer对象", description="客户表")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户")
    private Integer customerId;

    @ApiModelProperty(value = "客户名字")
    private String customerName;

    @ApiModelProperty(value = "联系人名字")
    private String contactName;

    @ApiModelProperty(value = "联系电话")
    private String phoneNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "邮件地址")
    private String emailAddress;

    private LocalDateTime regist;

    @ApiModelProperty(value = "备注")
    private String comments;


}
