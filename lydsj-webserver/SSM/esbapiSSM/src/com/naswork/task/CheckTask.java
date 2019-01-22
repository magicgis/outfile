package com.naswork.task;

import com.naswork.model.TjnewjdrcRealtime;
import com.naswork.service.TjnewjdrcRealtimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Program: CheckTask
 * @Description:
 * @Author: White
 * @DateTime: 2018-09-15 14:26:28
 **/
@Component
@Lazy(false)
public class CheckTask {

    private static Logger log = LoggerFactory.getLogger(CheckTask.class);

    @Resource
    private TjnewjdrcRealtimeService tjnewjdrcRealtimeService;
    /*
     * @Author: Create by white
     * @Datetime: 2018/9/15 13:50
     * @Descrition: checkGetAllCountByParentId
     * @Params: []
     * @Return: void
     * @Throws:
     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void  checkGetAllCountByParentId(){
//        log.info("每五秒测试getAllCountByParentId查询语句");
//        Integer [] array  = {1001,1002,1003,1004,1005,1006,1007};
//        int length = array.length;
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//        for (int i = 0;i<length-1;i++){
//            Integer number = tjnewjdrcRealtimeService.getAllCountByParentId(array[i],3.15);
//            if(number == null){
//                log.info("在"+sf.format(new Date())+" getAllCountByParentId()查询语句id为"+array[i]+"的时候获取不到数据");
//            }
//        }
//    }

}
