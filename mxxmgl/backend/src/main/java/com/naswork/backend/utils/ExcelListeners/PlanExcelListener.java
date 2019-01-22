package com.naswork.backend.utils.ExcelListeners;

import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.naswork.backend.entity.ProjectPlan;
import com.naswork.backend.mapper.ProjectPlanMapper;
import com.naswork.backend.service.ProjectPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Program: PlanExcelListener
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-11 18:31:43
 **/

@Service
public class PlanExcelListener extends AnalysisEventListener {

    @Resource
    private ProjectPlanMapper projectPlanMapper;

    private List<Object> datas = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext analysisContext) {
           datas.add(object);
//           this.writeData(object);
    }

    public void  writeData(Object object){
            List<String> list = (List<String>)object;
            ProjectPlan projectPlan = new ProjectPlan();
//            System.out.println("第一项"+list.get(0));
//            System.out.println("第二项"+list.get(1));
//            System.out.println("第三项"+list.get(2));
//            System.out.println("第四项"+list.get(3));
//            System.out.println("第五项"+list.get(4));
            projectPlan.setPlanCode(list.get(0));
            projectPlan.setPlanType(list.get(1));
            projectPlan.setPlanDays(list.get(2));
            projectPlan.setTaskMembers(list.get(3));
            projectPlan.setPlanDesc(list.get(4));
            projectPlan.setCreateTime(new Date());
//            projectPlanMapper.insert(projectPlan);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}
