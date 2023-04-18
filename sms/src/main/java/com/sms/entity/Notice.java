package com.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 公告表
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Notice对象", description="公告表")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "发公告的人")
    private String uid;

    @ApiModelProperty(value = "公告编码")
    private String code;

    @ApiModelProperty(value = "公告主题")
    private String topic;

    @ApiModelProperty(value = "公告内容")
    private String content;

    @ApiModelProperty(value = "额外字段")
    private String extra;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;


}
