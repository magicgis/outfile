<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增发票</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
   span
  { color:red;}
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
	
	$(function(){

		//校验code是否已存在
		$("#invoiceNumber").blur(function(){
			var code = $("#invoiceNumber").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/finance/invoice/testCode?code='+code,
				success:function(result){
					if(result.success){
						$("#msg").html("发票号已存在!");
					}else{
						$("#msg").html(" ");
					}
				}
			});
		});
	});
	
	//获取表单数据
	function getFormData(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};

		if($("#msg").html()=='发票号已存在!'){
			PJ.error('请输入正确的发票号');
			return false;
		}
		
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
<div position="center" title="新增发票">
	<form id="addForm" action="">
	<input name="clientOrderId" id="clientOrderId" value="${clientOrder.id}" class="hide"/>
	<input name="invoiceType" id="invoiceType" value="${clientInvoice.invoiceType}" class="hide"/>
	<input name="id" id="id" value="${clientInvoice.id}" class="hide"/>
		<input name="exportPackageId" id="exportPackageId" value="${exportPackageId}" class="hide"/>
	<table>
		<tr>
			<td>订单号</td>
			<td>${clientOrder.orderNumber}</td>
		</tr>
		<tr>
			<td>发票号</td>
			<td><input name="invoiceNumber" id="invoiceNumber" value="${clientInvoice.invoiceNumber}"/><span id="msg"></span></td>
		</tr>
		<tr>
			<td>开票日期</td>
			<td><input name="invoiceDate" type="text" id="invoiceDate" class="text" class="tc" value="<fmt:formatDate value="${clientInvoice.invoiceDate}"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>开票比例</td>
			<td><input name="terms" type="text" id="terms" class="text" class="tc" value="<fmt:formatNumber  value="${clientOrder.prepayRate}" pattern="0"/>"/>%</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark">${clientInvoice.remark}</textarea></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>