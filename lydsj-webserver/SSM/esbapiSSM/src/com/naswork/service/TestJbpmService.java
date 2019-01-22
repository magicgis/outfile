package com.naswork.service;

import com.naswork.model.Ask4leave;
import com.naswork.model.TestJbpm;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface TestJbpmService {
	  int deleteByPrimaryKey(Integer id);

	    int insert(TestJbpm record);

	    int insertSelective(TestJbpm record);

	    TestJbpm selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(TestJbpm record);

	    int updateByPrimaryKey(TestJbpm record);
	    
	    public void findPage(PageModel<TestJbpm> page, GridSort sort);

		public void submitReview(TestJbpm vo,String ids);
		
		public void changeSpzt(String businessKey,
				String taskId, String outcome, String assignee, String comment);
}
