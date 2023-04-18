package com.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Customer;
import com.sms.entity.Goods;
import com.sms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 出库申请表 前端控制器
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/customer")
public class CustomerComtroller {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    public Result all(){
        List<Customer> list = customerService.list();
        return Result.suc(list);
    }

    @PostMapping("save")
    public Result save(@RequestBody Customer customer){
        customer.setRegist(LocalDateTime.now());
        return customerService.save(customer)?Result.suc(customer):Result.fail();
    }

    @PostMapping("update")
    public Result update(@RequestBody Customer customer){
        return customerService.updateById(customer)?Result.suc(null):Result.fail();
    }

    @PostMapping("save/update")
    public Result saOrUp(@RequestBody Customer customer){
        // 检查是否存在同名公司
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerName,customer.getCustomerName());
        if( customerService.list(wrapper).size() > 0){
            return Result.fail("已经存在同名公司");
        }
        // 非第一次
        if (customer.getCustomerId() != null){
            customerService.updateById(customer);
        }else {
            // 第一次
            customer.setRegist(LocalDateTime.now());
            customerService.save(customer);
        }
        return Result.suc(null);
    }

    @GetMapping("del/{id}")
    public Result del(@PathVariable String id){
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerId,id);
        return customerService.remove(wrapper) ? Result.suc(null):Result.fail();
    }

    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam param){

        Page<Customer> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());

        HashMap map = param.getParam();
        String customerName = (String) map.get("customerName");
        String contactName = (String) map.get("contactName");
        String phoneNumber = (String) map.get("phoneNumber");

        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();

        if ( customerName != null && !"".equals(customerName)){
            wrapper.like(Customer::getCustomerName,customerName);
        }
        if ( contactName != null && !"".equals(contactName)){
            wrapper.like(Customer::getContactName,contactName);
        }
        if ( phoneNumber != null && !"".equals(phoneNumber)){
            wrapper.like(Customer::getPhoneNumber,phoneNumber);
        }

        Page<Customer> res = customerService.page(page, wrapper);
        return Result.suc(res.getTotal(),res.getRecords());
    }
}
