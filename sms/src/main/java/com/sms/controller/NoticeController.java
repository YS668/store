package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Notice;
import com.sms.entity.User;
import com.sms.service.NoticeService;
import com.sms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author sms
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Result all(){
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Notice::getTopic, Notice::getContent);
        return Result.suc(noticeService.list(wrapper));
    }

    //分页查询
    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();

        //分页
        Page<Notice> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap param = query.getParam();

        //查询条件：公告编码、发公告的人、主题、时间
        String code = (String) param.get("code");
        String userName = (String) param.get("userName");
        String topic = (String) param.get("topic");
        ArrayList date = (ArrayList) param.get("date");
        if (code != null && code.length() > 0){
            wrapper.eq(Notice::getCode,code);
        }
        if (userName != null && userName.length() > 0){
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper();
            userWrapper.like(User::getName, userName);
            List<Integer> collect = userService.list(userWrapper)
                    .stream().map(User::getId).collect(Collectors.toList());
            wrapper.in(Notice::getUid, collect);
        }
        if (topic != null && topic.length() > 0){
            wrapper.gt(Notice::getTopic,topic);
        }
        if (!date.isEmpty() && date.size() >= 2){
            wrapper.between(Notice::getCreateTime,
                    date.get(0),
                    date.get(1));
        }

        Page<Notice> result = noticeService.page(page, wrapper);

        return Result.suc(result.getTotal(),result.getRecords());
    }

    //删除
    @GetMapping("/del/{uid}/{code}")
    public Result del(@PathVariable String uid,@PathVariable String code){
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getCode,code);
        wrapper.eq(Notice::getUid,uid);
        noticeService.remove(wrapper);
        return Result.suc(null);
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Notice data){
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getCode,data.getCode());
        wrapper.eq(Notice::getUid,data.getUid());
        wrapper.eq(Notice::getId,data.getId());
        noticeService.update(data,wrapper);
        return Result.suc(null);
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Notice data){
        //秒时间戳就是编码
        data.setCode(String.valueOf(System.currentTimeMillis()/1000));
        data.setCreateTime(LocalDateTime.now());
        noticeService.save(data);
        return Result.suc(null);
    }
}
