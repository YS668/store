package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Goods;
import com.sms.entity.InRec;
import com.sms.entity.Inventory;
import com.sms.entity.response.InventoryResponse;
import com.sms.service.GoodsService;
import com.sms.service.InRecService;
import com.sms.service.InventoryService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 库存表 前端控制器
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    @Resource
    private GoodsService goodsService;

    // 入库
    @PostMapping("in/goods")
    public Result inGoods(@RequestBody Inventory inventory){

        Subject subject = SecurityUtils.getSubject();

        // 数量不对
        if (inventory.getCount() <= 0) {
            return Result.fail("入库数量小于等于0");
        }

        // 查询是否存在相同商品
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Inventory::getGoods, inventory.getGoods());
        wrapper.eq(Inventory::getGoodstype, inventory.getGoodstype());
        wrapper.eq(Inventory::getStorage, inventory.getStorage());

        Inventory data = inventoryService.getOne(wrapper);

        // 如果存在相同商品
        if (data != null){
            // 更新库存
            data.setCount(data.getCount() + inventory.getCount());
            inventoryService.updateById(data);
        }else {
            // 存入仓库
            inventory.setCreatetime(LocalDateTime.now());
            inventoryService.save(inventory);
        }

        return Result.suc(null);
    }

    // 出库
    @PostMapping("out/goods")
    public Result outGoods(@RequestBody Inventory inventory){

        Subject subject = SecurityUtils.getSubject();

        // 数量不对
        if (inventory.getCount() <= 0) {
            return Result.fail("出库数量小于等于0");
        }

        // 查询是否存在相同商品
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Inventory::getGoods, inventory.getGoods());
        wrapper.eq(Inventory::getGoodstype, inventory.getGoodstype());
        wrapper.eq(Inventory::getStorage, inventory.getStorage());

        Inventory data = inventoryService.getOne(wrapper);

        // 如果存在相同商品
        if (data != null){
            if (data.getCount() < inventory.getCount()){
                return Result.fail("库存不足");
            }
            // 更新库存
            data.setCount(data.getCount() - inventory.getCount());
            inventoryService.updateById(data);
        }else {
            // 存入仓库
            inventory.setCreatetime(LocalDateTime.now());
            inventoryService.save(inventory);
        }

        return Result.suc(null);
    }

    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam param) {

        Page<Inventory> page = new Page<>();

        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());

        HashMap map = param.getParam();
        String name = (String) map.get("name");
        String goodsType = (String) map.get("goodstype");
        String storage = (String) map.get("storage");

        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        if ( name != null && !"".equals(name)){
            LambdaQueryWrapper<Goods> goodsWrapper = new LambdaQueryWrapper<>();
            goodsWrapper.like(Goods::getName, name);
            // 可能的物品id
            List<Integer> collect = goodsService.list(goodsWrapper).stream().map(Goods::getId).collect(Collectors.toList());
            wrapper.in(Inventory::getGoods, collect);
        }
        if ( goodsType != null && !"".equals(goodsType)){
            wrapper.like(Inventory::getGoodstype,goodsType);
        }
        if ( storage != null && !"".equals(storage)){
            wrapper.like(Inventory::getStorage,storage);
        }

        Page<Inventory> res = inventoryService.page(page, wrapper);

        // 返回
        return Result.suc(res.getTotal(),res.getRecords().stream().map(this::toVo).collect(Collectors.toList()));
    }


    private InventoryResponse toVo(Inventory inventory){
        InventoryResponse vo = new InventoryResponse();
        vo.setId(inventory.getId());
        vo.setGoods(inventory.getGoods());
        Goods goods = goodsService.getById(inventory.getGoods());
        if (goods != null){
            vo.setGoodsName(goods.getName());
            vo.setImageUrl(goods.getImageUrl());
        }
        vo.setStorage(inventory.getStorage());
        vo.setGoodstype(inventory.getGoodstype());
        vo.setCount(inventory.getCount());
        vo.setRemark(inventory.getRemark());
        vo.setCreatetime(inventory.getCreatetime());
        return vo;
    }
}
