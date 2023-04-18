package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.common.utils.QiniuUtils;
import com.sms.entity.Customer;
import com.sms.entity.Goods;
import com.sms.entity.Goodstype;
import com.sms.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
        goods.setCreatetime(LocalDateTime.now());
        return goodsService.save(goods)?Result.suc(goods):Result.fail();
    }

    @PostMapping("update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.suc(null):Result.fail();
    }

    @PostMapping("save/update")
    public Result saOrUp(@RequestBody Goods goods){
        // 检查是否存在同名公司
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getId,goods.getId());
        if( goodsService.list(wrapper).size() > 0){
            return Result.fail("已经存在同名公司");
        }
        // 非第一次
        if (goods.getId() != null){
            goodsService.updateById(goods);
        }else {
            // 第一次
            goods.setCreatetime(LocalDateTime.now());
            goodsService.save(goods);
        }
        return Result.suc(null);
    }

    @GetMapping("del/{id}")
    public Result del(@PathVariable String id){
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
        ArrayList date = (ArrayList) map.get("date");

        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if ( name != null && !"".equals(name)){
            wrapper.like(Goods::getName,name);
        }
        if ( goodsType != null && !"".equals(goodsType)){
            wrapper.like(Goods::getGoodstype,goodsType);
        }
        if (date != null && !date.isEmpty() && date.size() >= 2){
            wrapper.between(Goods::getCreatetime,
                    date.get(0),
                    date.get(1));
        }

        Page<Goods> res = goodsService.page(page, wrapper);

        return Result.suc(res.getTotal(),res.getRecords());


    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file){ //参数名需保存一致
        try {
            //获取文件原始名
            String originalFilename = file.getOriginalFilename();
            //找到.最后的位置
            int i = originalFilename.lastIndexOf(".");
            //从.开始截取后缀名
            String substring = originalFilename.substring(i);
            //使用UUID生成随机文件，避免上传同名文件时被覆盖
            String fileName = UUID.randomUUID().toString() + substring;
            //使用工具类上传
            QiniuUtils.upload2Qiniu(file.getBytes(),fileName);

            //返回结果包括文件名
            return Result.suc("http://rt9r53bla.hn-bkt.clouddn.com/"+fileName);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

    @PostMapping("/delete/img")
    public Result upload(@RequestParam("fileName") String fileName){ //参数名需保存一致
        try {

            //使用工具类上传
            QiniuUtils.deleteFileFromQiniu(fileName);

            //返回结果包括文件名
            return Result.suc(null);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

}
