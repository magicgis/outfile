package com.naswork.dao;

import java.math.BigDecimal;
import java.util.List;

import org.jbpm.api.task.Task;

import com.naswork.model.Jbpm4Task;
import com.naswork.vo.PageModel;

public interface Jbpm4TaskDao {
    int deleteByPrimaryKey(BigDecimal dbid);
    
    int deleteHIStTask(String execution);
    
    int deleteHistProcinst(String id);
    
    int deleteHistActinst(String execution);
    
    int deleteExecution(String id);

    int insert(Jbpm4Task record);

    int insertSelective(Jbpm4Task record);

    Jbpm4Task selectByPrimaryKey(BigDecimal dbid);
    
    Jbpm4Task  selectByExecutionId(String executionId);

    int updateByPrimaryKeySelective(Jbpm4Task record);
    
    void updateByExecutionId(Jbpm4Task record);
    
    void updateJbpm4HistTask(Jbpm4Task record);
    
    void updateJbpm4HistActinst(Jbpm4Task record);
    
    void updateJbpm4Execution(Jbpm4Task record);

    int updateByPrimaryKey(Jbpm4Task record);
    
    List<Jbpm4Task> selectByAssignee(PageModel<Jbpm4Task> page);
    
    List<Jbpm4Task>  findYwTableElementId(Jbpm4Task jbpm4Task);
    
    List<Jbpm4Task> selectByAssigneeAndYwId(PageModel<Jbpm4Task> page);
    
    List<Jbpm4Task>  selectWeatherOrder(Jbpm4Task record);
    
    Jbpm4Task  selectDbversion(Jbpm4Task record);
}