package com.naswork.dao;
import java.util.List;

import com.naswork.model.Approval;
import com.naswork.model.ApprovalDetail;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
public interface ApprovalDao {
	
	/**
	 * search & find the approval by approval type & key 
	 * @param approvalType
	 * @param associationKey
	 * @return
	 */
	Approval getApprovalByType(PageData pd);

	/**
	 * Get the approval by ID
	 * @param approvalId
	 * @return
	 */
	Approval getApprovalById(String approvalId);
	
	
	/**
	 * add approval into APPROVAL table
	 * @param approval
	 */
	int  addApproval(Approval approval);
	
	/**
	 * update an existing approval
	 * @param approval
	 */
	void updateApproval(Approval approval);
	
	/**
	 * add approval detail into APPROVAL DETAIL table
	 * @param approvalDetail
	 */
	void addApprovalDetail(ApprovalDetail approvalDetail);
	
	/**
	 * fetch the approval detail given an approvalid
	 * @param approvalId
	 * @return
	 */
	List<ApprovalDetail> fetchApprovalDetailPage(PageModel<ApprovalDetail> page);
	
}
