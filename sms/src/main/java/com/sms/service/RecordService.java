package com.sms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sms
 * @since 2022-11-11
 */
public interface RecordService extends IService<Record> {

    IPage listPage(Page<Record> page, QueryWrapper<Record> queryWrapper);
}
