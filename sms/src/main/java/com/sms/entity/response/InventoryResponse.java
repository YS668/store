package com.sms.entity.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    // "主键"
    private Integer id;

    // "货名id"
    private Integer  goods;

    // "货名"
    private String  goodsName;

    // "仓库id"
    private Integer storage;

    // "分类id"
    private Integer goodstype;

    // "数量"
    private Integer count;

    // "备注"
    private String remark;

    // "入库时间"
    private LocalDateTime createtime;

    // 图片
    private String imageUrl;

}
