package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Goodstype;
import com.sms.entity.Storage;
import com.sms.service.GoodstypeService;
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
@RequestMapping("/goodstype")
public class GoodstypeController {

    @Resource
    private GoodstypeService goodstypeService;

    @GetMapping("/all")
    public Result all(){
        List<Goodstype> list = goodstypeService.list();
        return Result.suc(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Goodstype goodstype){
        return goodstypeService.save(goodstype)?Result.suc(null):Result.fail();
    }

    @PostMapping("update")
    public Result update(@RequestBody Goodstype goodstype){
        return goodstypeService.updateById(goodstype)?Result.suc(null):Result.fail();
    }

    @GetMapping("del")
    public Result del(@RequestParam String id){
        return goodstypeService.removeById(id)?Result.suc(null):Result.fail();
    }

    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam param){

        Page<Goodstype> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());

        HashMap map = param.getParam();
        String name = (String) map.get("name");

        LambdaQueryWrapper<Goodstype> wrapper = new LambdaQueryWrapper<>();
        if ( name != null && !"".equals(name)){
            wrapper.like(Goodstype::getName,name);
        }

        Page<Goodstype> res = goodstypeService.page(page, wrapper);

        return Result.suc(res.getTotal(),res.getRecords());


    }
}
