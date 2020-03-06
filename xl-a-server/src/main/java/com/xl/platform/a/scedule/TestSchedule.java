/*
 * 合肥币秀科技有限公司
 * Copyright (C) 2019 All Rights Reserved.
 */
package com.xl.platform.a.scedule;

import com.xl.platform.common.scedule.AbstractSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: cms
 * @description:
 * @create: 2020/01/28
 * @des: 测试
 **/
@Component
@Slf4j
public class TestSchedule extends AbstractSchedule {

    /**
     * 获取cron表达式
     *
     * @return
     */
    @Override
    protected String getCron() {
        return "0/5 * * * * ?";
    }

    /**
     * 具体执行的定时任务
     */
    @Override
    public void doSchedule() {
        log.info("测试：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}


