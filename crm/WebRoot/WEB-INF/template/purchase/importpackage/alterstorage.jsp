<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>入库转库存</title>

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
		nodeName:"amount,location",
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
		PJ.datepicker('certificationDate');
		
		//位置
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/importpackage/locationList?id='+"${importPackageElement.locationId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].location);
						$("#location").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
	 	
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
			var amount =postData.amount;
			if(null!=amount&&""!=amount&&typeof(amount) != "undefined"){
				amount=amount.replace(/,/g,"");
				postData.amount=amount;
			}
			return postData;
	 }		
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="">
	<form id="addForm" action="">
	<input type="text" class="hide" name="supplierOrderElementId" id="supplierOrderElementId" value="">
	<input type="text" class="hide" name="id" id="id" value="${importPackageElement.id}">
	<input type="text" class="hide" name="type" id="type" value="${type}">
	<table>
	<tr>
			<td>入库单号</td>
			<td>${importPackageElement.importNumber}</td>
		</tr>
		<tr>
		<td>供应商订单号 </td>
			<td>${importPackageElement.orderNumber}</td>
		</tr>
		<tr>
			<td>件号</td>
			<td>${importPackageElement.partNumber}</td>
		</tr>
		<tr>
			<td>描述</td>
			<td>${importPackageElement.description}</td>
		</tr>
		<tr>
			<td>单位</td>
			<td>${importPackageElement.unit}</td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="supplierOrderAmount"  class="tc" value="<fmt:formatNumber type="number" maxFractionDigits="3" value="${importPackageElement.amount}"/>"/></td>
		</tr>
		<tr>
			<td>单价</td>
			<td>${importPackageElement.price}</td>
		</tr>
		<tr>
			<td>状态</td>
			<td>${importPackageElement.conditionCode}</td>
		</tr>
		<tr>
			<td>证书</td>
			<td>${importPackageElement.certificationCode}</td>
		</tr>
		<tr>
			<td>位置</td>
			<td><input name="location" id="location" class="tc" value="${importPackageElement.location}"/>
			<%-- <select id="location" name="location" >
		<c:if test="${not empty importPackageElement.lLocation}"><option value="${importPackageElement.lLocation}" >${importPackageElement.lLocation} </option></c:if>
			</select> --%>
			</td>
		</tr>
		<tr>
			<td>溯源号</td>
			<td>${importPackageElement.originalNumber}</td>
		</tr>
		<tr>
			<td>系列号</td>
			<td>${importPackageElement.serialNumber}</td>
		</tr>
		<tr>
			<td>合格证日期</td>
			<td><fmt:formatDate value="${importPackageElement.certificationDate}"  pattern="yyyy-MM-dd"/></td>
				</tr>
		<tr>
			<td>备注</td>
			<td><textarea id="remark" name="remark" rows="8" cols="50">${importPackageElement.remark}</textarea> </td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>