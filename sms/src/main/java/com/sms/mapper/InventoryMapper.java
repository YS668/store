package com.sms.mapper;

import com.sms.entity.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 库存表 Mapper 接口
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

}
