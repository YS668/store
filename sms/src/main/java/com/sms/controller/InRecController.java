package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sms.common.Result;
import com.sms.entity.Goods;
import com.sms.entity.InRec;
import com.sms.service.GoodsService;
import com.sms.service.InRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 入库表 前端控制器
 * </p>
 *
 * @author sms
 * @since 2023-04-17
 */
@RestController
@RequestMapping("/in/rec")
public class InRecController {

    @Autowired
    private InRecService inRecService;
    @Autowired
    private GoodsService goodsService;

    @PostMapping("save")
    public Result save(@RequestBody InRec inRec){
        Goods goods = goodsService.getById(inRec.getGoods());
        if (goods != null){
            inRec.setGoodname(goods.getName());
        }
        inRec.setCreatetime(LocalDateTime.now());
        return  inRecService.save(inRec) ? Result.suc(null):Result.fail();
    }



}
