package com.sms.entity.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordResponse {

    // id
    private Integer id;
    // 物品名
    private String goodsName;
    // 仓库
    private String storageName;
    // 分类
    private String goodsTypeName;
    // 管理员
    private String adminName;
    // 销售
    private String userName;
    // 客户
    private String customerName;
    // 数量
    private Integer count;
    // 操作时间
    private LocalDateTime createTime;
    // 备注
    private String remark;
}
