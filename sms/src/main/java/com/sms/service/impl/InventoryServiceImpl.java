package com.sms.service.impl;

import com.sms.entity.Inventory;
import com.sms.mapper.InventoryMapper;
import com.sms.service.InventoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存表 服务实现类
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

}
