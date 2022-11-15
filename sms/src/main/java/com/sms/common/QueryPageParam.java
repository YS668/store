package com.sms.common;

import lombok.Data;

import java.util.HashMap;

/**
 * 分页查询条件
 */
@Data
public class QueryPageParam {

    //默认参数
    private static int PAGE_SIZE = 20;
    private static int PAGE_NUM = 1;

    //接受的参数,避免传入空值
    private int pageSize = PAGE_SIZE;
    private int pageNum = PAGE_NUM;

    //查询条件
    //初始化，避免出现空指针异常
    private HashMap param = new HashMap();
}
