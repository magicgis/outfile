package com.naswork.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jbpm.api.task.Task;

import com.naswork.model.Jbpm4Task;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface Jbpm4TaskDao {
    int deleteByPrimaryKey(BigDecimal dbid);
    
    int deleteByParticipation(BigDecimal dbid);
    
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
    
    void updateMessage(Jbpm4Task record);
    
    void updateTaskMessage(Jbpm4Task record);
    
    void updateJbpm4HistActinst(Jbpm4Task record);

    int updateByPrimaryKey(Jbpm4Task record);
    
    List<Jbpm4Task> selectByAssigneePage(PageModel<Jbpm4Task> page);
    
    List<Jbpm4Task> groupSubTaskData(String  ywTableId,String userId);
    
    List<Jbpm4Task> myFinishedTaskDataPage(PageModel<Jbpm4Task> page);
    
    List<Jbpm4Task> myFinishedListsUBData(String assignee,String  ywTableId,String activityName,String name);
    
    List<Jbpm4Task> groupTaskData(PageModel<Jbpm4Task> page);
    
    List<Jbpm4Task>  findYwTableElementId(Jbpm4Task jbpm4Task);
    
    List<Jbpm4Task>  findByYwTableId(Jbpm4Task jbpm4Task);
    
    List<Jbpm4Task> findOnlyYwTableElementId(Integer ywTableElementId);
    
    List<Jbpm4Task> selectByAssigneeAndYwId(PageModel<Jbpm4Task> page);
    
    List<Jbpm4Task>  selectWeatherOrder(Jbpm4Task record);
    
    List<Jbpm4Task>  findByYwTableElementId(Integer ywTableElementId);
    
    Jbpm4Task  selectDbversion(Jbpm4Task record);
    
    List<Jbpm4Task> selectByTaskName(Jbpm4Task record );
    
    String findMessage(String message);
    
    public List<Jbpm4Task> getListById(String id);
    
    public List<Jbpm4Task> getListByIdForHis(String id);
    
    
}