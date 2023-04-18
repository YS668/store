package com.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.*;
import com.sms.entity.response.RecordResponse;
import com.sms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sms
 * @since 2022-11-11
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private InRecService inRecService;
    @Autowired
    private OutRecService outRecService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodstypeService goodstypeService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/page/in")
    public Result inPage(@RequestBody QueryPageParam param){

        Page<InRec> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());

        HashMap map = param.getParam();
        String name = (String) map.get("name");
        String goodsType = (String) map.get("goodstype");
        String storage = (String) map.get("storage");
        ArrayList date = (ArrayList) map.get("date");

        LambdaQueryWrapper<InRec> wrapper = new LambdaQueryWrapper<>();
        if ( name != null && !"".equals(name)){
            wrapper.like(InRec::getGoodname,name);
        }
        if ( goodsType != null && !"".equals(goodsType)){
            wrapper.like(InRec::getGoodstype,goodsType);
        }
        if ( storage != null && !"".equals(storage)){
            wrapper.like(InRec::getStorage,storage);
        }
        if (!date.isEmpty() && date.size() >= 2){
            wrapper.between(InRec::getCreatetime,
                    date.get(0),
                    date.get(1));
        }

        Page<InRec> res = inRecService.page(page, wrapper);


        return Result.suc(res.getTotal(),res.getRecords().stream().map(this::toVo).collect(Collectors.toList()));
    }

    @PostMapping("/page/out")
    public Result outPage(@RequestBody QueryPageParam param){
        Page<OutRec> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());

        HashMap map = param.getParam();
        String name = (String) map.get("name");
        String goodsType = (String) map.get("goodstype");
        String storage = (String) map.get("storage");
        ArrayList date = (ArrayList) map.get("date");

        LambdaQueryWrapper<OutRec> wrapper = new LambdaQueryWrapper<>();
        if ( name != null && !"".equals(name)){
            wrapper.like(OutRec::getGoodname,name);
        }
        if ( goodsType != null && !"".equals(goodsType)){
            wrapper.like(OutRec::getGoodstype,goodsType);
        }
        if ( storage != null && !"".equals(storage)){
            wrapper.like(OutRec::getStorage,storage);
        }
        if (!date.isEmpty() && date.size() >= 2){
            wrapper.between(OutRec::getCreatetime,
                    date.get(0),
                    date.get(1));
        }

        Page<OutRec> res = outRecService.page(page, wrapper);


        return Result.suc(res.getTotal(),res.getRecords().stream().map(this::toVo).collect(Collectors.toList()));
    }


    public RecordResponse toVo(InRec data){
        RecordResponse vo = new RecordResponse();
        vo.setId(data.getId());
        vo.setGoodsName(data.getGoodname());
        Storage storage = storageService.getById(data.getStorage());
        if (storage != null){
            vo.setStorageName(storage.getName());
        }
        Goodstype goodstype = goodstypeService.getById(data.getGoodstype());
        if (goodstype != null){
            vo.setGoodsTypeName(goodstype.getName());
        }
        User admin = userService.getById(data.getAdminId());
        if (admin != null){
            vo.setAdminName(admin.getName());
        }
        vo.setCount(data.getCount());
        vo.setCreateTime(data.getCreatetime());
        vo.setRemark(data.getRemark());
        return vo;
    }

    public RecordResponse toVo(OutRec data){
        RecordResponse vo = new RecordResponse();
        vo.setId(data.getId());
        vo.setGoodsName(data.getGoodname());
        Storage storage = storageService.getById(data.getStorage());
        if (storage != null){
            vo.setStorageName(storage.getName());
        }
        Goodstype goodstype = goodstypeService.getById(data.getGoodstype());
        if (goodstype != null){
            vo.setGoodsTypeName(goodstype.getName());
        }
        User admin = userService.getById(data.getAdminId());
        if (admin != null){
            vo.setAdminName(admin.getName());
        }
        User user = userService.getById(data.getUserid());
        if (user != null){
            vo.setUserName(user.getName());
        }
        Customer customer = customerService.getById(data.getCustomerid());
        if (customer != null){
            vo.setCustomerName(customer.getCustomerName());
        }
        vo.setCount(data.getCount());
        vo.setCreateTime(data.getCreatetime());
        vo.setRemark(data.getRemark());
        return vo;
    }
}
