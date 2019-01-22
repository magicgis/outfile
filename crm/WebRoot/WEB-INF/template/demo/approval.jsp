<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>审批示例</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript">
		$(function(){
			$("#viewapprovaldetail").click(function(){
				var iframeId="approvalDetail";
				PJ.topdialog(iframeId, '审批历史列表', '<%=path%>/xtgl/approval/detailPage?approvalId=4',
						undefined,function(item,dialog){dialog.close();}, 1000, 500, true);

			});
		});

		$(function(){
			$("#addapproval").click(function(){
				var approvalName = "订单审批请求";
				var comment = "请尽快审批";
				var send = "1";
				var associationKey="3";
				var queryString = "approvalType=order&approverRoleId=1";
				queryString = queryString +	"&associationKey="+associationKey;
				queryString = queryString +	"&approvalName="+approvalName;
				queryString = queryString + "&comment="+comment;
				queryString = queryString + "&send="+send;
				$.ajax({
					url: '<%=path%>/xtgl/approval/createApprovalRequest?'+queryString,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});

			});
		});

		$(function(){
			$("#sendapproval").click(function(){
				var approvalId = "1"
				var comment = "请尽快审批send";
				var queryString = "approvalId=" + approvalId + "&comment="+comment;
				$.ajax({
					url: '<%=path%>/xtgl/approval/sendApprovalRequest?'+queryString,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});

			});
		});

		$(function(){
			$("#cancelapproval").click(function(){
				var approvalId = "1"
				var comment = "cancel the request";
				var queryString = "approvalId=" + approvalId + "&comment="+comment;
				$.ajax({
					url: '<%=path%>/xtgl/approval/cancelApprovalRequest?'+queryString,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});

			});
		});
		
		$(function(){
			$("#restoreapproval").click(function(){
				var approvalId = "4"
				var comment = "restore the request";
				var queryString = "approvalId=" + approvalId + "&comment="+comment;
				$.ajax({
					url: '<%=path%>/xtgl/approval/restoreApprovalRequest?'+queryString,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});

			});
		});
		
		$(function(){
			$("#receiveapproval").click(function(){
				var approvalId = "4"
				var queryString = "approvalId=" + approvalId;
				$.ajax({
					url: '<%=path%>/xtgl/approval/receiveApprovalRequest?'+queryString,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});

			});
		});
		$(function(){
			$("#approveapproval").click(function(){
				var approvalId = "4"
				var comment = "your request is approved";
				var queryString = "approvalId=" + approvalId + "&comment="+comment;
				$.ajax({
					url: '<%=path%>/xtgl/approval/approveApprovalRequest?'+queryString,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});

			});
		});
		$(function(){
			$("#rejectapproval").click(function(){
				var approvalId = "4"
				var comment = "your request is rejected";
				var queryString = "approvalId=" + approvalId + "&comment="+comment;
				$.ajax({
					url: '<%=path%>/xtgl/approval/rejectApprovalRequest?'+queryString,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});

			});
		});
		
	</script>
  </head>
  
  <body style="margin: 20px;">
    <h1>申请者</h1>
    <h2>1、新增审批</h2>
    <input type="button" value="新增审批" id="addapproval"/>
    <h2>2、发送审批</h2>
    <input type="button" value="发送审批" id="sendapproval"/>
    <h2>3、取消审批</h2>
    <input type="button" value="取消审批" id="cancelapproval"/>
    <h2>4、将审批改为草稿</h2>
    <input type="button" value="恢复审批" id="restoreapproval"/>
    <h1>审批者</h1>
    <h2>1、接收审批</h2>
    <input type="button" value="接收审批" id="receiveapproval"/>
    <h2>2、批准</h2>
    <input type="button" value="批准审批" id="approveapproval"/>
    <h2>3、否决</h2>
    <input type="button" value="否决审批" id="rejectapproval"/>
    <h1>其他</h1>
    <h2>查看审批详细</h2>
    <input type="button" value="查看审批详细" id="viewapprovaldetail"/>
    
    <br/>

  </body>
</html>
