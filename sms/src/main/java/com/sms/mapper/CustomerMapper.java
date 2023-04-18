package com.sms.mapper;

import com.sms.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author sms
 * @since 2023-04-17
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
