package com.sms.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRes {

    // 申请ID
    private Integer id;

    // 客户名称
    private String customerName;

    // 销售名称
    private String userName;

    // 库存id
    private Integer inventory;

    // 仓库名称
    private String storageName;

    // 类别
    private String goodsTypeName;

    // 数量
    private Integer count;

    // 申请时间
    private LocalDateTime applyTime;

    // 审批状态，0-待审批，1-已通过，2-未通过")
    private Integer approvalStatus;

    // 审批人名称
    private String approverName;

    // 审批时间
    private LocalDateTime approvalTime;

    // 备注信息
    private String remarks;

    // 反馈信息
    private String response;
}
