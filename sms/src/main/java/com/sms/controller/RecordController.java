package com.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.common.QueryPageParam;
import com.sms.common.Result;
import com.sms.entity.Goods;
import com.sms.entity.Record;
import com.sms.service.GoodsService;
import com.sms.service.RecordService;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
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
@RequestMapping("/record")
public class RecordController {

    @Resource
    private RecordService recordService;

    @Resource
    private GoodsService goodsService;

    @GetMapping("/all")
    public Result all(){
        List<Record> list = recordService.list();
        return Result.suc(list);
    }

    @PostMapping("/page")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String goodstype = (String)param.get("goodstype");
        String storage = (String)param.get("storage");
        String roleId = (String)param.get("roleId");
        String userId = (String)param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        if("2".equals(roleId)){
            // queryWrapper.eq(Record::getUserid,userId);
            queryWrapper.apply(" a.userId= "+userId);
        }

        if( name != null && !"null".equals(name)){
            queryWrapper.like("b.name",name);
        }
        if( goodstype != null && !"".equals(goodstype)){
            queryWrapper.eq("d.id",goodstype);
        }
        if( storage != null && !"".equals(storage)){
            queryWrapper.eq("c.id",storage);
        }

        IPage result = recordService.listPage(page,queryWrapper);
        return Result.suc(result.getTotal(),result.getRecords());
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        Goods goods = goodsService.getById(record.getGoods());
        int temp = record.getCount();
        int old = goods.getCount();

        //出库
        if("2".equals(record.getAction())){
            //判断是否有足够的数量出库
            if (temp > old){
                return Result.fail("出库失败，出库的最大数量为"+old);
            }
            temp = -temp;
            record.setCount(temp);
        }

        int num = goods.getCount() +temp;
        goods.setCount(num);
        goodsService.updateById(goods);

        return recordService.save(record)?Result.suc(null):Result.fail();
    }

}
