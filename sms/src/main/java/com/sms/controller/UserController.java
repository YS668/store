package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Menu;
import com.sms.entity.User;
import com.sms.service.MenuService;
import com.sms.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2022-11-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    //查询所有
    @GetMapping("/all")
    public Result getAll(){
        List<User> list = userService.list();
        return Result.suc(list);
    }

    //唯一账号
    @GetMapping("findByNo")
    public Result findByNo(@RequestParam String no){
        List<User> res = userService.lambdaQuery().eq(User::getNo, no).list();
        return res.size()>0 ? Result.suc(res):Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user)?Result.suc(null):Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateById(user)?Result.suc(null):Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public Result saveOrMod(@RequestBody User user){
        return userService.saveOrUpdate(user)?Result.suc(null):Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result delete(@RequestParam("id") String id){
        return userService.removeById(id)?Result.suc(null):Result.fail();
    }

    //分页查询（模糊匹配）
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        //查询条件
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");

        if (name != null && !"".equals(name)){
            wrapper.like(User::getName,name);
        }
        if (sex != null && !"".equals(sex)){
            wrapper.eq(User::getSex,sex);
        }
        if (roleId != null){
            wrapper.eq(User::getRoleId,roleId);
        }

        Page<User> result = userService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword())
                .list();
        if (list.size() > 0 ){
            User us = (User) list.get(0);
            List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuright, us.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user",us);
            res.put("menu",menuList);
            return Result.suc(res);
        }

        return Result.fail();
    }
}
