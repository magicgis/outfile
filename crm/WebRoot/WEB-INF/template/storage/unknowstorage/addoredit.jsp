<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增客户询价</title>

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
		PJ.datepicker('deadline');
		PJ.datepicker('inquiryDate');
		PJ.datepicker('realDeadline');
		
		if('${unknowStorageDetail.certification }' != '' && '${unknowStorageDetail.certification }' != 'undefined'){
			if('${unknowStorageDetail.certification }' == '履历本'){
				var $option = $("<option></option>");
				$option.val("履历本").text("履历本");
				$("#certification").append($option);
				var $option2 = $("<option></option>");
				$option2.val("合格证").text("合格证");
				$("#certification").append($option2);
				var $option3 = $("<option></option>");
				$option3.val("无").text("无");
				$("#certification").append($option3);
			}else if('${unknowStorageDetail.certification }' == '合格证'){
				var $option = $("<option></option>");
				$option.val("合格证").text("合格证");
				$("#certification").append($option);
				var $option2 = $("<option></option>");
				$option2.val("履历本").text("履历本");
				$("#certification").append($option2);
				var $option3 = $("<option></option>");
				$option3.val("无").text("无");
				$("#certification").append($option3);
			}else if('${unknowStorageDetail.certification }' == '无'){
				var $option = $("<option></option>");
				$option.val("无").text("无");
				$("#certification").append($option);
				var $option2 = $("<option></option>");
				$option2.val("履历本").text("履历本");
				$("#certification").append($option2);
				var $option3 = $("<option></option>");
				$option3.val("合格证").text("合格证");
				$("#certification").append($option3);
			}
		}else{
			var $option1 = $("<option></option>");
			$option1.val("无").text("无");
			$("#certification").append($option1);
			var $option2 = $("<option></option>");
			$option2.val("履历本").text("履历本");
			$("#certification").append($option2);
			var $option3 = $("<option></option>");
			$option3.val("合格证").text("合格证");
			$("#certification").append($option3);
		}
	});
	
	function test(){
		var test = $("#test").val();
		var index = parseInt(test.indexOf("）"));
		test = test.substring(4,index);
		alert(test);
	}
	
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
			nodeName:"mixId,bizTypeId,airTypeId,sourceNumber,inquiryDate",
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
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="新增库存">
	<form id="addForm" action="">
	<input class="hide" name="id" id="id" value="${unknowStorageDetail.id}"/>
	<table>
		<tr>
			<td>件号</td>
			<td><input name="partNumber" id="partNumber" class="text" value="${unknowStorageDetail.partNumber }"/></td>
		</tr>
		<tr>
			<td>描述</td>
			<td><input name="description" id="description" class="text" class="tc" value="${unknowStorageDetail.description }"/></td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="amount" class="text" class="tc" value="${unknowStorageDetail.amount }"/></td>
		</tr>
		<tr>
			<td>已使用数量</td>
			<td><input name="useAmount" id="useAmount" class="text" class="tc" value="${unknowStorageDetail.useAmount }"/></td>
		</tr>
		<tr>
			<td>证书</td>
			<td>
				<select name="certification" id="certification">
				</select>
			</td>
		</tr>
		<tr>
			<td>SN号</td>
			<td><input name="sn" id="sn" class="text" class="tc" value="${unknowStorageDetail.sn }"/></td>
		</tr>
		<tr>
			<td>位置</td>
			<td><input name="location" id="location" class="text" class="tc" value="${unknowStorageDetail.location }"/></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark">${unknowStorageDetail.remark }</textarea></td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>