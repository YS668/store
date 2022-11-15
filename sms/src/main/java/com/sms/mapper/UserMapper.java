package com.sms.mapper;

import com.sms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sms
 * @since 2022-11-10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
