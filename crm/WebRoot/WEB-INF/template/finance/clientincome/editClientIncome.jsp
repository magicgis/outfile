<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改收款</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
</style> 
<script type="text/javascript">
	$(function(){
		PJ.datepicker('receiveDate');
		
		 
	});
	
	//获取表单数据
	function getFormData(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};

		$input.each(function(index){
			if(!$(this).val()) {
				//PJ.tip("必填数据项没有填写完整");
				//flag = true;
				return;
			}
				
			postData[$(this).attr("name")] = $(this).val();
		});
		return postData;
	 }		
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="修改收款">
	<form id="addForm" action="">
	<input name="id" id="id" value="${income.id }" class="hide"/>
	<table>
		<tr>
			<td>收款时间</td>
			<td>${income }</td>
		</tr>
		<tr>
			<td>收款额</td>
			<td><input name="receiveSum" id="receiveSum" value="${clientReceipt.receiveSum }"/></td>
		</tr>
		<tr>
			<td>收款日期</td>
			<td><input name="receiveDate" type="text" id="receiveDate" class="text" class="tc" value="<fmt:formatDate value="${clientReceipt.receiveDate }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="5" cols="90" name="remark" id="remark">${clientReceipt.remark }</textarea></td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td><fmt:formatDate value="${clientReceipt.updateTimestamp }"  pattern="yyyy-MM-dd hh:mm:ss"/></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>