package com.sms.service.impl;

import com.sms.entity.Application;
import com.sms.mapper.ApplicationMapper;
import com.sms.service.ApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 出库申请表 服务实现类
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

}
