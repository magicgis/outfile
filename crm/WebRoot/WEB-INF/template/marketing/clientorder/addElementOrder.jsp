<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增客户订单明细</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   15px;   } 
</style> 
<script type="text/javascript">
	$(function(){
		PJ.datepicker('deadline');
		 
	});
	
	//获取表单数据
	function getFormData(){
		var $input = $("#addElementForm").find("input,textarea,select");
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
<div position="center" title="新增客户询价">
	<form id="addElementForm" action="">
	<input type="text" class="hide" value="${clientOrderElementVo.id }"/>
	<table>
		<tr>
			<td>订单号</td>
			<td>${orderNumber }</td>
		</tr>
		<tr>
			<td>件号</td>
			<td>${clientOrderElementVo.quotePartNumber }</td>
		</tr>
		<tr>
			<td>描述</td>
			<td>${clientOrderElementVo.quoteDescription }</td>
		</tr>
		<tr>
			<td>备注</td>
			<td>${clientOrderElementVo.quoteRemark }</td>
		</tr>
		<tr>
			<td>单位</td>
			<td>${clientOrderElementVo.quoteUnit }</td>
		</tr>
		<tr>
			<td>报价数量</td>
			<td>${clientOrderElementVo.inquiryAmount }</td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="amount" class="text" value="${clientOrderElementVo.inquiryAmount }"/></td>
		</tr>
		<tr>
			<td>报价</td>
			<td>${clientOrderElementVo.clientQuotePrice }</td>
		</tr>
		<tr>
			<td>单价</td>
			<td><input name="price" id="price" class="text" value="${clientOrderElementVo.clientQuotePrice }"/></td>
		</tr>
		<tr>
			<td>周期</td>
			<td><input name="leadTime" id="leadTime" class="text"/></td>
		</tr>
		<tr>
			<td>截至日期</td>
			<td><input name="deadline" id="deadline" class="tc"/></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>