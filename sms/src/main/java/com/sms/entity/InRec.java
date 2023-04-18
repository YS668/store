package com.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 入库表
 * </p>
 *
 * @author sms
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="InRec对象", description="入库表")
public class InRec implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "货品id")
    private Integer goods;

    @ApiModelProperty(value = "货品名称")
    @TableField("goodName")
    private String goodname;

    @ApiModelProperty(value = "货品类别id")
    @TableField("goodsType")
    private Integer goodstype;

    @ApiModelProperty(value = "仓库id")
    @TableField("storage")
    private Integer storage;

    @ApiModelProperty(value = "管理人id")
    private Integer adminId;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "备注")
    private String remark;


}
