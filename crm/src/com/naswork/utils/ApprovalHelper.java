package com.naswork.utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.naswork.common.controller.BaseController;
import com.naswork.common.exception.ApprovalCallbackException;
import com.naswork.common.exception.ApprovalNotExistException;
import com.naswork.common.exception.DuplicateApprovalException;
import com.naswork.common.exception.InvalidStatusException;
import com.naswork.common.exception.NoPrivilegeException;
import com.naswork.model.Approval;
import com.naswork.model.ApprovalDetail;
import com.naswork.service.ApprovalService;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;

@Service("approvalHelper")
public class ApprovalHelper {
	@Resource
	private ApprovalService approvalService;
	
	public ResultVo createApprovalRequest(
			BaseController controller,
			HttpServletRequest request,
			HttpServletResponse response,
			ApprovalCallback callback){
		String comment = controller.getString(request, "comment");
		String send = controller.getString(request, "send");
		boolean sendFlag = false;
		if(send.equals("1")){
			sendFlag = true;
		}
		Approval approval = new Approval();
		approval.setApprovalName(controller.getString(request, "approvalName"));
		approval.setApprovalType(controller.getString(request, "approvalType"));
		approval.setAssociationKey(controller.getString(request, "associationKey"));
		approval.setApproverRoleId(controller.getString(request, "approverRoleId"));
		approval.setApproverRoleName(((RoleVo)controller.getDmObj("ROLE", controller.getString(request, "approverRoleId"))).getRoleName());
		approval.setRequesterId(controller.getCurrentUser(request).getUserId());
		approval.setRequesterName(controller.getCurrentUser(request).getUserName());
		
		ApprovalDetail approvalDetail = new ApprovalDetail();
		approvalDetail.setComment(comment);
		approvalDetail.setUpdaterId(controller.getCurrentUser(request).getUserId());
		approvalDetail.setUpdaterName(controller.getCurrentUser(request).getUserName());

		boolean success = false;
		String message = null;
		try{
			this.approvalService.draftApproval(approval, approvalDetail, sendFlag, callback);
			message = "成功生成审批请求";
			success = true;
		}catch(DuplicateApprovalException e){
			message="该记录的相关审批记录已存在";
		}catch(ApprovalCallbackException e){
			message="业务更新失败:"+e.getCallbackErrorMsg();
		}catch(Exception e){
			message = "未知错误，请联系维护人员";
		}
		ResultVo result = new ResultVo(success, message);
		return result;
		
	}
	
	public ResultVo sendApprovalRequest(			
			BaseController controller,
			HttpServletRequest request,
			HttpServletResponse response,
			ApprovalCallback callback){
		String approvalId = controller.getString(request, "approvalId");
		String comment = controller.getString(request, "comment");
		
		Approval approval = new Approval();
		ApprovalDetail approvalDetail = new ApprovalDetail();
		approval.setApprovalId(approvalId);
		approvalDetail.setComment(comment);
		approvalDetail.setUpdaterId(controller.getCurrentUser(request).getUserId());
		approvalDetail.setUpdaterName(controller.getCurrentUser(request).getUserName());
		boolean success = false;
		String message = null;
		try{
			this.approvalService.sendApproval(approval, approvalDetail, callback);
			message = "成功发送审批请求";
			success = true;
		}catch(NoPrivilegeException e){
			message="您没有权限发送审批请求";
		} catch (ApprovalNotExistException e) {
			message ="所请求的审批不存在";
		} catch (InvalidStatusException e) {
			message = "该状态下不可发送";
		}catch(ApprovalCallbackException e){
			message="业务更新失败:"+e.getCallbackErrorMsg();
		}catch(Exception e){
			message = "未知错误，请联系维护人员";
		}
		ResultVo result = new ResultVo(success, message);
		return result;
		
	}
	
	public ResultVo receiveApprovalRequest(
			BaseController controller,
			HttpServletRequest request,
			HttpServletResponse response,
			ApprovalCallback callback){
		String approvalId = controller.getString(request, "approvalId");
		
		Approval approval = new Approval();
		ApprovalDetail approvalDetail = new ApprovalDetail();
		approval.setApprovalId(approvalId);
		approvalDetail.setUpdaterId(controller.getCurrentUser(request).getUserId());
		approvalDetail.setUpdaterName(controller.getCurrentUser(request).getUserName());
		boolean success = false;
		String message = null;
		try{
			this.approvalService.receiveApproval(controller.getCurrentUser(request), approval, approvalDetail, callback);
			message = "成功接收审批请求";
			success = true;
		}catch(NoPrivilegeException e){
			message="您没有权限接收审批请求";
		} catch (ApprovalNotExistException e) {
			message ="所请求的审批不存在";
		} catch (InvalidStatusException e) {
			message = "该状态下不可接受请求";
		} catch(ApprovalCallbackException e){
			message="业务更新失败:"+e.getCallbackErrorMsg();
		} catch(Exception e){
			message = "未知错误，请联系维护人员";
		}
		ResultVo result = new ResultVo(success, message);
		return result;
		
	}
	
	public ResultVo approveApprovalRequest(
			BaseController controller,
			HttpServletRequest request,
			HttpServletResponse response,
			ApprovalCallback callback){
		String approvalId = controller.getString(request, "approvalId");
		String comment = controller.getString(request, "comment");
		
		Approval approval = new Approval();
		ApprovalDetail approvalDetail = new ApprovalDetail();
		approval.setApprovalId(approvalId);
		approvalDetail.setComment(comment);
		approvalDetail.setUpdaterId(controller.getCurrentUser(request).getUserId());
		approvalDetail.setUpdaterName(controller.getCurrentUser(request).getUserName());
		boolean success = false;
		String message = null;
		try{
			this.approvalService.approveApproval(controller.getCurrentUser(request), approval, approvalDetail, callback);
			message = "成功批准审批请求";
			success = true;
		}catch(NoPrivilegeException e){
			message="您没有权限批准审批请求";
		} catch (ApprovalNotExistException e) {
			message ="所请求的审批不存在";
		} catch (InvalidStatusException e) {
			message = "该状态下不可批准请求";
		} catch(ApprovalCallbackException e){
			message="业务更新失败:"+e.getCallbackErrorMsg();
		} catch(Exception e){
			message = "未知错误，请联系维护人员";
		}
		ResultVo result = new ResultVo(success, message);
		return result;
				
	}
	
	public ResultVo rejectApprovalRequest(
			BaseController controller,
			HttpServletRequest request,
			HttpServletResponse response,
			ApprovalCallback callback){
		String approvalId = controller.getString(request, "approvalId");
		String comment = controller.getString(request, "comment");
		
		Approval approval = new Approval();
		ApprovalDetail approvalDetail = new ApprovalDetail();
		approval.setApprovalId(approvalId);
		approvalDetail.setComment(comment);
		approvalDetail.setUpdaterId(controller.getCurrentUser(request).getUserId());
		approvalDetail.setUpdaterName(controller.getCurrentUser(request).getUserName());
		boolean success = false;
		String message = null;
		try{
			this.approvalService.rejectApproval(controller.getCurrentUser(request), approval, approvalDetail, callback);
			message = "成功拒绝审批请求";
			success = true;
		}catch(NoPrivilegeException e){
			message="您没有权限拒绝审批请求";
		} catch (ApprovalNotExistException e) {
			message ="所请求的审批不存在";
		} catch (InvalidStatusException e) {
			message = "该状态下不可拒绝请求";
		} catch(ApprovalCallbackException e){
			message="业务更新失败:"+e.getCallbackErrorMsg();
		} catch(Exception e){
			message = "未知错误，请联系维护人员";
		}
		ResultVo result = new ResultVo(success, message);
		return result;
	}
		
	public ResultVo cancelApprovalRequest(
			BaseController controller,
			HttpServletRequest request,
			HttpServletResponse response,
			ApprovalCallback callback){
		String approvalId = controller.getString(request, "approvalId");
		String comment = controller.getString(request, "comment");
		
		Approval approval = new Approval();
		ApprovalDetail approvalDetail = new ApprovalDetail();
		approval.setApprovalId(approvalId);
		approvalDetail.setComment(comment);
		approvalDetail.setUpdaterId(controller.getCurrentUser(request).getUserId());
		approvalDetail.setUpdaterName(controller.getCurrentUser(request).getUserName());
		boolean success = false;
		String message = null;
		try{
			this.approvalService.cancelApproval(controller.getCurrentUser(request), approval, approvalDetail, callback);
			message = "成功取消审批请求";
			success = true;
		}catch(NoPrivilegeException e){
			message="您没有权限取消审批请求";
		} catch (ApprovalNotExistException e) {
			message ="所请求的审批不存在";
		} catch (InvalidStatusException e) {
			message = "该状态下不可取消请求";
		} catch(ApprovalCallbackException e){
			message="业务更新失败:"+e.getCallbackErrorMsg();
		} catch(Exception e){
			message = "未知错误，请联系维护人员";
		}
		ResultVo result = new ResultVo(success, message);
		return result;
	}
	
	public ResultVo restoreApprovalRequest(
			BaseController controller,
			HttpServletRequest request,
			HttpServletResponse response,
			ApprovalCallback callback){
		String approvalId = controller.getString(request, "approvalId");
		String comment = controller.getString(request, "comment");
		
		Approval approval = new Approval();
		ApprovalDetail approvalDetail = new ApprovalDetail();
		approval.setApprovalId(approvalId);
		approvalDetail.setComment(comment);
		approvalDetail.setUpdaterId(controller.getCurrentUser(request).getUserId());
		approvalDetail.setUpdaterName(controller.getCurrentUser(request).getUserName());
		boolean success = false;
		String message = null;
		try{
			this.approvalService.restoreApprovalToDraft(controller.getCurrentUser(request), approval, approvalDetail, callback);
			message = "成功将审批请求改为草稿状态";
			success = true;
		}catch(NoPrivilegeException e){
			message="您没有权限将审批请求改为草稿状态";
		} catch (ApprovalNotExistException e) {
			message ="所请求的审批不存在";
		} catch (InvalidStatusException e) {
			message = "该状态下不可改为草稿状态";
		} catch(ApprovalCallbackException e){
			message="业务更新失败:"+e.getCallbackErrorMsg();
		} catch(Exception e){
			message = "未知错误，请联系维护人员";
		}
		ResultVo result = new ResultVo(success, message);
		return result;
	}
}
