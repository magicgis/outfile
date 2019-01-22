<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>商品库cage code管理</title>

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
$(function(){
	//校验code是否已存在
	$("#cageCode").blur(function(){
		var code = $("#cageCode").val();
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/stock/search/findCageCode?code='+code,
			success:function(result){
				if(result.success){
					$("#msg").html("cage code已存在!");
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
			nodeName:"cageCode,manName,msnFlag",
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
<div position="center" title="商品库cage code管理">
	<form id="addForm" action="">
	<table>
	<input name="msn" id="msn" class="hide" value="${tManufactory.msn}"/>
		<tr>
			<td>cage code</td>
			<td><input name="cageCode" id="cageCode" class="text" value="${tManufactory.cageCode}" <c:if test="${not empty tManufactory.cageCode}">disabled="disabled"</c:if>/><span id="msg"></span></td>
		</tr>
		<tr>
			<td>man name</td>
			<td><input id="manName" name="manName" class="text"  value="${tManufactory.manName}"/>
			</td>
		</tr>
		<tr>
			<td>msn flag</td>
			<td><input id="msnFlag" name="msnFlag" class="text"  value="${tManufactory.msnFlag}"/>
			</td>
		</tr>
		<tr>
			<td>生产能力</td>
			<td><select id="capMan" name="capMan" class="text">
				<c:if test="${ empty tManufactory.capMan || tManufactory.capMan=='1'}">
				<option value="1">是</option>
				<option value="0">否</option>
				</c:if>
				<c:if test="${ tManufactory.capMan=='0'}">
				<option value="0">否</option>
				<option value="1">是</option>
				</c:if>
				</select>	
			</td>
		</tr>
		<tr>
			<td>检测能力</td>
			<td><select id="capInspection" name="capInspection" class="text">
				<c:if test="${ empty tManufactory.capInspection || tManufactory.capInspection=='1'}">
				<option value="1">是</option>
				<option value="0">否</option>
				</c:if>
				<c:if test="${ tManufactory.capInspection=='0'}">
				<option value="0">否</option>
				<option value="1">是</option>
				</c:if>
				</select>	
			</td>
		</tr>
		<tr>
			<td>维修能力</td>
			<td><select id="capRepair" name="capRepair" class="text">
				<c:if test="${ empty tManufactory.capRepair || tManufactory.capRepair=='1'}">
				<option value="1">是</option>
				<option value="0">否</option>
				</c:if>
				<c:if test="${ tManufactory.capInspection=='0'}">
				<option value="0">否</option>
				<option value="1">是</option>
				</c:if>
				</select>	
			</td>
		</tr>
		<tr>
			<td>改装能力</td>
			<td><select id="capModification" name="capModification" class="text">
				<c:if test="${ empty tManufactory.capModification || tManufactory.capModification=='1'}">
				<option value="1">是</option>
				<option value="0">否</option>
				</c:if>
				<c:if test="${ tManufactory.capInspection=='0'}">
				<option value="0">否</option>
				<option value="1">是</option>
				</c:if>
				</select>	
			</td>
		</tr>
		<tr>
			<td>翻修能力</td>
			<td><select id="capOverhaul" name="capOverhaul" class="text">
				<c:if test="${ empty tManufactory.capOverhaul || tManufactory.capOverhaul=='1'}">
				<option value="1">是</option>
				<option value="0">否</option>
				</c:if>
				<c:if test="${ tManufactory.capInspection=='0'}">
				<option value="0">否</option>
				<option value="1">是</option>
				</c:if>
				</select>	
			</td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>