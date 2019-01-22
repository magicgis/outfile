<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增收款</title>

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
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"receiveDate,receiveSum",
			form:"#addForm"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,select");var nodes = def.nodeName.split(",");var flag = true;var tip;
		$items.each(function(i){
			var name = $(this).attr("name");
			if(!name)return;
			for(var k in nodes){
				if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
				if(!flag){
	                $(this).addClass("input-error");
					tip = $(this).attr("data-tip");
					return false;
				}else $(this).removeClass("input-error");
			}
			if($("#clientId").val()==""||$("#clientId").text()=="请选择"){
				$("#clientId").addClass("input-error");
					tip = $("#clientId").attr("data-tip");
					flag=false;
					return false;
			}
			if($("#currencyId").val()==""||$("#currencyId").text()=="请选择"){
				$("#currencyId").addClass("input-error");
					tip = $("#currencyId").attr("data-tip");
					flag=false;
					return false;
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	}
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="新增收款">
	<form id="addForm" action="">
	<input name="exportPackageId" id="exportPackageId" value="${exportPackage.id }" class="hide"/>
	<table>
		<tr>
			<td>订单号</td>
			<td>${exportPackage.exportNumber }</td>
		</tr>
		<tr>
			<td>币种</td>
			<td>${systemCode.value }</td>
		</tr>
		<tr>
			<td>收款日期</td>
			<td><input name="receiveDate" id="receiveDate" class="text" class="tc" value="<fmt:formatDate value="${today }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>收款额</td>
			<td><input name="receiveSum" id="receiveSum" class="text"/></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark"></textarea></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>