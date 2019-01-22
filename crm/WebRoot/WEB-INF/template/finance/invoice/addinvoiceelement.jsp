<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增发票明细</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
</style> 
<script type="text/javascript">
		function validate(){ 
			return validate2({
				nodeName:"invoiceNumber,invoiceDate,terms",
				form:"#addForm"
			});
		 } 
		
		function validate2(opt){
			var def = {nodeName:"",form: ""};
			for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
			var $items = $(def.form).find("select,input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
			$items.each(function(i){
				var name = $(this).attr("name");
				if(!name)return;
				for(var k in nodes){
					if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
					if(!flag){
		                $(this).addClass("input-error");
						tip = $(this).attr("data-tip");
						return false;
					}
					else $(this).removeClass("input-error");
				}
			});
			return flag;
			};

	$(function(){
		PJ.datepicker('invoiceDate');
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#addForm")[0].reset();
		 });
		 
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
<div position="center" title="新增发票明细">
	<form id="addForm" action="">
	<input name="clientInvoiceId" id="clientInvoiceId" value="${clientInvoice.id}" class="hide"/>
	<input name="clientOrderElementId" id="clientOrderElementId" value="${clientOrder.id}" class="hide"/>
	<input name="id" id="id" value="${clientInvoiceElement.id}" class="hide"/>


	<table>
		<tr>
			<td>发票号</td>
			<td>${clientInvoice.invoiceNumber}</td>
		</tr>
		<tr>
			<td>件号</td>
			<td>${clientOrder.quotePartNumber}</td>
		</tr>
		<tr>
			<td>描述</td>
			<td>${clientOrder.quoteDescription}</td>
		</tr>
		<tr>
			<td>单位</td>
			<td>${clientOrder.quoteUnit}</td>
		</tr>
		<tr>
			<td>单价</td>
			<td>${clientOrder.clientOrderPrice}</td>
		</tr>
		<tr>
			<td>订单数量</td>
			<td><fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${clientOrder.clientOrderAmount}" /></td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="amount"  value="${clientInvoiceElement.amount}" /></td>
		</tr>
		<tr>
			<td>开票比例</td>
			<td><input name="terms" type="text" id="terms" class="text" class="tc" value="<fmt:formatNumber  value="${clientInvoice.invoiceTerms}"  pattern="0" />"/>%</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark">${clientInvoiceElement.remark}</textarea></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>