package com.sms.controller;


import com.sms.common.Result;
import com.sms.entity.Goods;
import com.sms.entity.InRec;
import com.sms.entity.OutRec;
import com.sms.service.GoodsService;
import com.sms.service.InRecService;
import com.sms.service.OutRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 出库表 前端控制器
 * </p>
 *
 * @author sms
 * @since 2023-04-17
 */
@RestController
@RequestMapping("/out/rec")
public class OutRecController {

    @Autowired
    private OutRecService outRecService;
    @Autowired
    private GoodsService goodsService;

    @PostMapping("save")
    public Result save(@RequestBody OutRec outRec){
        Goods goods = goodsService.getById(outRec.getGoods());
        if (goods != null){
            outRec.setGoodname(goods.getName());
        }
        outRec.setCreatetime(LocalDateTime.now());
        return  outRecService.save(outRec) ? Result.suc(null):Result.fail();
    }

}
