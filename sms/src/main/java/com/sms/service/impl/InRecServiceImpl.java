package com.sms.service.impl;

import com.sms.entity.InRec;
import com.sms.mapper.InRecMapper;
import com.sms.service.InRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 * 入库表 服务实现类
 * </p>
 *
 * @author sms
 * @since 2023-04-17
 */
@Service
public class InRecServiceImpl extends ServiceImpl<InRecMapper, InRec> implements InRecService {



}
