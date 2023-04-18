package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.entity.*;
import com.sms.entity.request.ApplicationReq;
import com.sms.entity.response.ApplicationRes;
import com.sms.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 出库申请表 前端控制器
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private GoodstypeService goodstypeService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

    // 未审批
    private static Integer  NOT_APPROVE= 0;
    // 已通过
    private static Integer PASS = 1;
    // 未通过
    private static Integer NOT_PASS= 2;

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){

        //分页
        Page<Application> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();
        // 物品名
        String  name = (String) param.get("name");
        String  storage = (String) param.get("storage");
        String  goodstype = (String) param.get("goodstype");
        String  customerId = (String) param.get("customerId");
        String  approvalStatus = (String) param.get("approvalStatus");

        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        // 物品名
        if ( name != null && !"".equals(name)){
            LambdaQueryWrapper<Goods> goodWrapper = new LambdaQueryWrapper<>();
            goodWrapper.like(Goods::getName, name);
            // 符合条件的货品id
            List<Integer> collect = goodsService.list(goodWrapper)
                    .stream().map(Goods::getId).collect(Collectors.toList());
            // 库存id
            LambdaQueryWrapper<Inventory> inventoryWrapper = new LambdaQueryWrapper<>();
            inventoryWrapper.in(Inventory::getGoods, collect);
            List<Integer> collect1 = inventoryService.list(inventoryWrapper)
                    .stream().map(Inventory::getId).collect(Collectors.toList());
            wrapper.in(Application::getInventory,collect1);
        }
        // 仓库
        if ( storage != null && !"".equals(storage)){
            // 库存id
            LambdaQueryWrapper<Inventory> inventoryWrapper = new LambdaQueryWrapper<>();
            inventoryWrapper.in(Inventory::getStorage, storage);
            List<Integer> collect = inventoryService.list(inventoryWrapper)
                    .stream().map(Inventory::getId).collect(Collectors.toList());
            wrapper.in(Application::getInventory,collect);
        }
        // 类别
        if ( goodstype != null && !"".equals(goodstype)){
            // 库存id
            LambdaQueryWrapper<Inventory> inventoryWrapper = new LambdaQueryWrapper<>();
            inventoryWrapper.in(Inventory::getGoodstype, goodstype);
            List<Integer> collect = inventoryService.list(inventoryWrapper)
                    .stream().map(Inventory::getId).collect(Collectors.toList());
            wrapper.in(Application::getInventory,collect);
        }
        // 客户
        if ( customerId != null && !"".equals(customerId)){
            wrapper.eq(Application::getCustomerId,customerId);
        }
        // 审核状态
        if ( approvalStatus != null && !"".equals(approvalStatus)){
            wrapper.eq(Application::getApprovalStatus,approvalStatus);
        }

        Page<Application> res = applicationService.page(page, wrapper);

        return Result.suc(res.getTotal(), res.getRecords().stream().map(this::toVo).collect(Collectors.toList()));
    }

    @PostMapping("/save")
    public Result save(@RequestBody Application data){
        data.setApplyTime(LocalDateTime.now());
        data.setApprovalStatus(NOT_APPROVE);
        applicationService.save(data);
        return Result.suc();
    }

    // 审批通过
    @PostMapping("/pass")
    public Result pass(@RequestBody ApplicationReq req){
        Application appliaction = applicationService.getById(req.getId());
        appliaction.setApprovalTime(LocalDateTime.now());
        appliaction.setApproverId(req.getApproverId());
        appliaction.setResponse(req.getResponse());
        appliaction.setApprovalStatus(PASS);
        applicationService.save(appliaction);
        return Result.suc();
    }

    // 未审批
    @PostMapping("not/pass")
    public Result notPass(@RequestBody ApplicationReq req){
        Application appliaction = applicationService.getById(req.getId());
        appliaction.setApprovalTime(LocalDateTime.now());
        appliaction.setApproverId(req.getApproverId());
        appliaction.setResponse(req.getResponse());
        appliaction.setApprovalStatus(NOT_PASS);
        applicationService.save(appliaction);
        return Result.suc();
    }

    public ApplicationRes toVo(Application data){
        ApplicationRes vo = new ApplicationRes();
        vo.setId(data.getId());
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerId, data.getCustomerId());
        Customer customer = customerService.getOne(wrapper);
        if (customer != null){
            vo.setCustomerName(customer.getCustomerName());
        }
        User user = userService.getById(data.getUserId());
        if (user != null){
            vo.setUserName(user.getName());
        }
        Inventory inventory = inventoryService.getById(data.getInventory());
        if (inventory != null){
            Storage storage = storageService.getById(inventory.getStorage());
            if (storage != null){
                vo.setStorageName(storage.getName());
            }
            Goodstype goodstype = goodstypeService.getById(inventory.getGoodstype());
            if (goodstype != null){
                vo.setGoodsTypeName(goodstype.getName());
            }
        }
        vo.setCount(data.getCount());
        vo.setApplyTime(data.getApplyTime());
        vo.setApprovalStatus(data.getApprovalStatus());
        User approver = userService.getById(data.getApproverId());
        if (approver != null){
            vo.setApproverName(approver.getName());
        }
        vo.setApplyTime(data.getApplyTime());
        vo.setRemarks(data.getRemarks());
        vo.setResponse(data.getResponse());
        return vo;
    }

}
