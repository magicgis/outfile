package com.naswork.service;
import java.util.List;

import com.naswork.common.exception.DuplicateApprovalException;
import com.naswork.common.exception.NoPrivilegeException;
import com.naswork.common.exception.ApprovalCallbackException;
import com.naswork.common.exception.ApprovalNotExistException;
import com.naswork.common.exception.InvalidStatusException;
import com.naswork.model.Approval;
import com.naswork.model.ApprovalDetail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;
import com.naswork.utils.ApprovalCallback;
public interface ApprovalService {

	/**
	 * User to raise an approval request.
	 * The service will
	 * 1) check if the associated key exists or not. if yes, then no need to continue
	 * 2) add a record in APPROVAL table and a record in APPROVAL_DETAIL
	 * @param approval
	 */
	void draftApproval(Approval approval, ApprovalDetail detail, boolean send, ApprovalCallback callback) 
			throws DuplicateApprovalException,ApprovalCallbackException;
	
	/**
	 * User can send the approval for approve
	 * 1) check if the associated key exists or not. if yes, then no need to continue
	 * 2) change status to "SENT"
	 * 3) add a record in APPROVAL table and a record in APPROVAL_DETAIL
	 * @param approval
	 */
	void sendApproval(Approval approval, ApprovalDetail detail, ApprovalCallback callback) 
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException;
	
	/**
	 * Get the approval by id
	 * @param approvalId
	 * @return
	 */
	Approval getApproval(String approvalId);
	
	/**
	 * The approver can receive the request
	 * change the status of an approval to "RECEIVED"
	 * add an record into approval detail table
	 * @param approval
	 */
	void receiveApproval(UserVo user,Approval approval, ApprovalDetail detail, ApprovalCallback callback)
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException;
	
	/**
	 * The approver can approve the request
	 * change the status of the approval to "APPROVED"
	 * add an record into approval detail table
	 * @param approval
	 */
	void approveApproval(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback)
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException;
	
	/**
	 * The approver can reject the request
	 * change the status of the approval to "reject"
	 * add an record into approval detail table
	 * @param approval
	 */
	void rejectApproval(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback)			
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException;
	
	/**
	 * The requester can cancel an approval
	 * change the status to "cancel"
	 * add an record into approval detail table
	 * @param approval
	 * @param detail
	 */
	void cancelApproval(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback)
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException;
	
	/**
	 * The requester can restore the approval back to draft status
	 * change the status to "draft"
	 * change the approver id back to default
	 * add an record into approval detail table
	 * @param approval
	 * @param detail
	 */
	void restoreApprovalToDraft(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback)
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException;
	
	/**
	 * Search the approval records by requester id
	 * @param requesterId
	 * @return
	 */
	List<Approval> searchApprovalByRequesterId(String requesterId);
	
	/**
	 * Search the approval records by requester id & status
	 * @param requesterId
	 * @return
	 */
	List<Approval> searchApprovalByRequesterIdAndStatus(String requesterId, int status);

	/**
	 * Search the approval records by approver id
	 * @param requesterId
	 * @return
	 */
	List<Approval> searchApprovalByApproverId(String approverId);

	/**
	 * Search the approval records by approver id & status
	 * @param requesterId
	 * @param status
	 * @return
	 */
	List<Approval> searchApprovalByApproverIdAndStatus(String approverId, int status);
	
	/**
	 * Search the approval records by approver id
	 * @param requesterId
	 * @return
	 */
	List<Approval> searchApprovalByApproverRoleId(String roleId);

	/**
	 * Search the approval records by approver id & status
	 * @param requesterId
	 * @param status
	 * @return
	 */
	List<Approval> searchApprovalByApproverRoleIdAndStatus(String roleId, int status);
	
	/**
	 * fetch all approval detail records by approval id
	 * @param page the page with parameter approvalId
	 * @param sort the sort
	 */
	public void fetchApprovalDetail(PageModel<ApprovalDetail> page, GridSort sort);
}
