package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Storage;
import com.sms.service.StorageService;
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
@RequestMapping("/storage")
public class StorageController {

    @Resource
    private StorageService storageService;

    @GetMapping("/all")
    public Result all(){
        List<Storage> list = storageService.list();
        return Result.suc(list);
    }

    @GetMapping("del")
    public Result del(@RequestParam String id){
        return storageService.removeById(id)?Result.suc(null):Result.fail();
    }

    @PostMapping("save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage)?Result.suc(null):Result.fail();
    }

    @PostMapping("update")
    public Result update(@RequestBody Storage storage){
        return storageService.updateById(storage)?Result.suc(null):Result.fail();
    }

    @PostMapping("page")
    public Result listPage(@RequestBody QueryPageParam param){
        Page<Storage> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());

        HashMap map = param.getParam();
        String name = (String) map.get("name");

        LambdaQueryWrapper<Storage> wrapper = new LambdaQueryWrapper<>();
        if ( name != null && !"".equals(name)){
            wrapper.like(Storage::getName,name);
        }

        Page<Storage> res = storageService.page(page, wrapper);

        return Result.suc(res.getTotal(),res.getRecords());
    }


}
