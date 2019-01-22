package com.naswork.module.white;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Program: ScheduledController
 * @Description:
 * @Author: White
 * @DateTime: 2018-09-15 10:09:41
 **/
@Component
@Lazy(false)
public class ScheduledController {

    private static Logger logger = LoggerFactory.getLogger(ScheduledController.class);

    /*
     * @Author: Create by white
     * @Datetime: 2018/9/15 14:20
     * @Descrition: Scheduled 测试定时任务 每三秒执行一次
     * @Params: []
     * @Return: void
     * @Throws:
     */

//    @Scheduled(cron = "0/3 * * * * ?")
//    public  void Scheduled(){
//        logger.info("设置3秒钟打印一次");
//    }

}



