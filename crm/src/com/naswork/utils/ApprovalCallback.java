package com.naswork.utils;
import com.naswork.common.exception.ApprovalCallbackException;
import com.naswork.model.Approval;
public interface ApprovalCallback {
	public void onSucceed(Approval approval, int previousStatus)
		throws ApprovalCallbackException;
}
