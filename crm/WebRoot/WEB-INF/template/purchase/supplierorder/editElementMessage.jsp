<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改供应商订单明细</title>

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
			var $input = $("#editForm").find("input,textarea,select");
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
<div position="center" title="修改供应商订单明细">
	<form id="editForm" action="">
	<input type="text" class="hide" name="id" value="${addSupplierOrderElementVo.id }"/>
	<input type="text" class="hide" name="supplierOrderId" value="${addSupplierOrderElementVo.supplierOrderId }"/>
	<input type="text" class="hide" name="clientOrderElementId" value="${addSupplierOrderElementVo.clientOrderElementId }"/>
	<input type="text" class="hide" name="supplierQuoteElementId" value="${addSupplierOrderElementVo.supplierQuoteElementId }"/>
	<table>
		<tr>
			<td>订单号</td>
			<td>${addSupplierOrderElementVo.clientOrderNumber }</td>
		</tr>
		<tr>
			<td>供应商订单号</td>
			<td>${addSupplierOrderElementVo.supplierOrderNumber }</td>
		</tr>
		<tr>
			<td>件号</td>
			<td>${addSupplierOrderElementVo.quotePartNumber }</td>
		</tr>
		<tr>
			<td>描述</td>
			<td>${addSupplierOrderElementVo.quoteDescription }</td>
		</tr>
		<tr>
			<td>单位</td>
			<td>${addSupplierOrderElementVo.quoteUnit }</td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="amount" value="${addSupplierOrderElementVo.supplierOrderAmount }" /></td>
		</tr>
		<tr>
			<td>单价</td>
			<td><input name="price" id="price" value="${addSupplierOrderElementVo.supplierOrderPrice }"/></td>
		</tr>
		<tr>
			<td>周期</td>
			<td><input name="leadTime" id="leadTime"  value="${addSupplierOrderElementVo.supplierOrderLeadTime }"/></td>
		</tr>
		<tr>
			<td>截止日期</td>
			<td><input name="deadline" id="deadline" class="tc" value="<fmt:formatDate value="${addSupplierOrderElementVo.supplierOrderDeadline }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td><fmt:formatDate value="${addSupplierOrderElementVo.updateTimestamp }"  pattern="yyyy-MM-dd hh:mm:ss"/></td>
		</tr>
		
		
	</table>
	</form>
</div>	
</body>
</html>