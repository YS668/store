package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Goods;
import com.sms.entity.Goodstype;
import com.sms.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sms
 * @since 2022-11-11
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @GetMapping("/all")
    public Result all(){
        List<Goods> list = goodsService.list();
        return Result.suc(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods)?Result.suc(null):Result.fail();
    }

    @PostMapping("update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.suc(null):Result.fail();
    }

    @GetMapping("del")
    public Result del(@RequestParam String id){
        return goodsService.removeById(id)?Result.suc(null):Result.fail();
    }

    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam param){

        Page<Goods> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());

        HashMap map = param.getParam();
        String name = (String) map.get("name");
        String goodsType = (String) map.get("goodstype");
        String storage = (String) map.get("storage");

        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if ( name != null && !"".equals(name)){
            wrapper.like(Goods::getName,name);
        }
        if ( goodsType != null && !"".equals(goodsType)){
            wrapper.like(Goods::getGoodstype,goodsType);
        }
        if ( storage != null && !"".equals(storage)){
            wrapper.like(Goods::getStorage,storage);
        }

        Page<Goods> res = goodsService.page(page, wrapper);

        return Result.suc(res.getTotal(),res.getRecords());


    }

}
