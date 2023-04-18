package com.sms.mapper;

import com.sms.entity.Application;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 出库申请表 Mapper 接口
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {

}
