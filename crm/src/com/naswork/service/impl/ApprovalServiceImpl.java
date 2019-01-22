package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.model.Approval;
import com.naswork.model.ApprovalDetail;
import com.naswork.service.ApprovalService;
import com.naswork.utils.ApprovalCallback;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;
import com.naswork.dao.ApprovalDao;
import com.naswork.common.exception.ApprovalCallbackException;
import com.naswork.common.exception.ApprovalNotExistException;
import com.naswork.common.exception.DuplicateApprovalException;
import com.naswork.common.exception.InvalidStatusException;
import com.naswork.common.exception.NoPrivilegeException;
import com.naswork.common.constants.Constants;

@Service("approvalService")
public class ApprovalServiceImpl implements ApprovalService {

	@Resource
	private ApprovalDao approvalDao;
	
	@Override
	public void draftApproval(Approval approval, ApprovalDetail detail, boolean send, ApprovalCallback callback) 
			throws DuplicateApprovalException,ApprovalCallbackException {
		//1. check if approval has been raised before
		PageData pd = new PageData();
		pd.put("approvalType", approval.getApprovalType());
		pd.put("associationKey", approval.getAssociationKey());
		Approval existingApproval = this.approvalDao.getApprovalByType(pd);
		if(existingApproval!=null){
			throw new DuplicateApprovalException();
		}
				
		//2. add an approval
		if(send){
			approval.setApprovalStatus(Constants.APPROVAL_STATUS_SENT);
			detail.setApprovalStatus(Constants.APPROVAL_STATUS_SENT);
		}else{
			approval.setApprovalStatus(Constants.APPROVAL_STATUS_DRAFT);
			detail.setApprovalStatus(Constants.APPROVAL_STATUS_DRAFT);
		}
		this.approvalDao.addApproval(approval);
		
		//3. add an approval detail
		detail.setApprovalId(approval.getApprovalId());
		this.approvalDao.addApprovalDetail(detail);
		
		if(callback!=null){
			callback.onSucceed(approval, Constants.APPROVAL_STATUS_NONE);			
		}
		
	}

	@Override
	public void sendApproval(Approval approval, ApprovalDetail detail, ApprovalCallback callback)  
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException{
		//1. verification
		//1.1 check if approval exists or not
		Approval existing = this.approvalDao.getApprovalById(approval.getApprovalId());
		if(existing==null){
			throw new ApprovalNotExistException();			
		}
		//1.2 check if previlege user
		if(!existing.getRequesterId().equals(detail.getUpdaterId())){
			throw new NoPrivilegeException();
		}
		//1.3 check status of the approval
		int previousStatus=existing.getApprovalStatus();
		
		if(existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_DRAFT){
			throw new InvalidStatusException();
		}
		
		//2. update appproval
		existing.setApprovalStatus(Constants.APPROVAL_STATUS_SENT);
		this.approvalDao.updateApproval(existing);
		
		//3. add new record into detail table
		detail.setApprovalStatus(Constants.APPROVAL_STATUS_SENT);
		detail.setApprovalId(existing.getApprovalId());
		this.approvalDao.addApprovalDetail(detail);

		if(callback!=null){
			callback.onSucceed(existing, previousStatus);
		}

	}

	@Override
	public Approval getApproval(String approvalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveApproval(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback) 
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException
	{
		//1. verification
		//1.1 check if approval exists or not
		Approval existing = this.approvalDao.getApprovalById(approval.getApprovalId());
		if(existing==null){
			throw new ApprovalNotExistException();			
		}
		//1.2 check if privilege user
		if(!user.getRoleIdList().contains(existing.getApproverRoleId())){
			throw new NoPrivilegeException();
		}
		//1.3 check status of the approval
		int previousStatus=existing.getApprovalStatus();
		if(existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_SENT){
			throw new InvalidStatusException();
		}
		
		//2. update approval
		existing.setApprovalStatus(Constants.APPROVAL_STATUS_RECEIVED);
		existing.setApproverId(user.getUserId());
		existing.setApproverName(user.getUserName());
		this.approvalDao.updateApproval(existing);
		
		//3. add new record into detail table
		detail.setApprovalStatus(Constants.APPROVAL_STATUS_RECEIVED);
		detail.setApprovalId(existing.getApprovalId());
		if(detail.getComment()==null){
			detail.setComment("");
		}
		this.approvalDao.addApprovalDetail(detail);

		if(callback!=null){
			callback.onSucceed(existing, previousStatus);
		}
	}

	@Override
	public void approveApproval(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback) 
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException	
	{
		//1. verification
		//1.1 check if approval exists or not
		Approval existing = this.approvalDao.getApprovalById(approval.getApprovalId());
		if(existing==null){
			throw new ApprovalNotExistException();			
		}
		//1.2 check if privilege user
		if(!user.getUserId().equals(existing.getApproverId())){
			throw new NoPrivilegeException();
		}
		//1.3 check status of the approval
		int previousStatus=existing.getApprovalStatus();
		if(existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_SENT &&
				existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_RECEIVED &&
				existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_REJECTED){
			throw new InvalidStatusException();
		}
		
		//2. update approval
		existing.setApprovalStatus(Constants.APPROVAL_STATUS_APPROVED);
		existing.setApproverId(user.getUserId());
		existing.setApproverName(user.getUserName());
		this.approvalDao.updateApproval(existing);
		
		//3. add new record into detail table
		detail.setApprovalStatus(Constants.APPROVAL_STATUS_APPROVED);
		detail.setApprovalId(existing.getApprovalId());
		this.approvalDao.addApprovalDetail(detail);

		if(callback!=null){
			callback.onSucceed(existing, previousStatus);
		}
		
	}

	@Override
	public void rejectApproval(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback) 
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException
	{
		//1. verification
		//1.1 check if approval exists or not
		Approval existing = this.approvalDao.getApprovalById(approval.getApprovalId());
		if(existing==null){
			throw new ApprovalNotExistException();			
		}
		//1.2 check if privilege user
		if(!user.getUserId().equals(existing.getApproverId())){
			throw new NoPrivilegeException();
		}
		//1.3 check status of the approval
		int previousStatus=existing.getApprovalStatus();
		if(existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_SENT &&
				existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_RECEIVED &&
				existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_APPROVED){
			throw new InvalidStatusException();
		}
		
		//2. update approval
		existing.setApprovalStatus(Constants.APPROVAL_STATUS_REJECTED);
		existing.setApproverId(user.getUserId());
		existing.setApproverName(user.getUserName());
		this.approvalDao.updateApproval(existing);
		
		//3. add new record into detail table
		detail.setApprovalStatus(Constants.APPROVAL_STATUS_REJECTED);
		detail.setApprovalId(existing.getApprovalId());
		this.approvalDao.addApprovalDetail(detail);

		if(callback!=null){
			callback.onSucceed(existing, previousStatus);
		}
	}

	@Override
	public void cancelApproval(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback) 
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException
	{
		//1. verification
		//1.1 check if approval exists or not
		Approval existing = this.approvalDao.getApprovalById(approval.getApprovalId());
		if(existing==null){
			throw new ApprovalNotExistException();			
		}
		//1.2 check if privilege user
		if(!user.getUserId().equals(existing.getRequesterId())){
			throw new NoPrivilegeException();
		}
		//1.3 check status of the approval
		int previousStatus=existing.getApprovalStatus();
		if(existing.getApprovalStatus()==Constants.APPROVAL_STATUS_DRAFT ||
				existing.getApprovalStatus()==Constants.APPROVAL_STATUS_CANCELLED){
			throw new InvalidStatusException();
		}
		
		//2. update approval
		existing.setApprovalStatus(Constants.APPROVAL_STATUS_CANCELLED);
		this.approvalDao.updateApproval(existing);
		
		//3. add new record into detail table
		detail.setApprovalStatus(Constants.APPROVAL_STATUS_CANCELLED);
		detail.setApprovalId(existing.getApprovalId());
		this.approvalDao.addApprovalDetail(detail);

		if(callback!=null){
			callback.onSucceed(existing, previousStatus);
		}
	}

	@Override
	public void restoreApprovalToDraft(UserVo user, Approval approval, ApprovalDetail detail, ApprovalCallback callback)
			throws NoPrivilegeException,ApprovalNotExistException,InvalidStatusException,ApprovalCallbackException
	{
		//1. verification
		//1.1 check if approval exists or not
		Approval existing = this.approvalDao.getApprovalById(approval.getApprovalId());
		if(existing==null){
			throw new ApprovalNotExistException();			
		}
		//1.2 check if privilege user
		if(!user.getUserId().equals(existing.getRequesterId())){
			throw new NoPrivilegeException();
		}
		//1.3 check status of the approval
		int previousStatus=existing.getApprovalStatus();
		if(existing.getApprovalStatus()!=Constants.APPROVAL_STATUS_CANCELLED){
			throw new InvalidStatusException();
		}
		
		//2. update approval
		existing.setApprovalStatus(Constants.APPROVAL_STATUS_DRAFT);
		this.approvalDao.updateApproval(existing);
		
		//3. add new record into detail table
		detail.setApprovalStatus(Constants.APPROVAL_STATUS_DRAFT);
		detail.setApprovalId(existing.getApprovalId());
		this.approvalDao.addApprovalDetail(detail);

		if(callback!=null){
			callback.onSucceed(existing, previousStatus);
		}
	}

	@Override
	public List<Approval> searchApprovalByRequesterId(String requesterId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Approval> searchApprovalByRequesterIdAndStatus(String requesterId, int status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Approval> searchApprovalByApproverId(String approverId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Approval> searchApprovalByApproverIdAndStatus(String approverId, int status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Approval> searchApprovalByApproverRoleId(String roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Approval> searchApprovalByApproverRoleIdAndStatus(String roleId, int status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fetchApprovalDetail(PageModel<ApprovalDetail> page, GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(this.approvalDao.fetchApprovalDetailPage(page));
	}

}
