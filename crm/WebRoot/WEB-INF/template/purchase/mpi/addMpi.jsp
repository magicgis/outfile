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
<div position="center" title="新增MPI">
	<form id="addForm" action="">
	<input class="hide" name="token" id="token" value="${token}"/>
	<table>
		<tr>
			<td>代码</td>
			<td><input name="code" id="code" class="text"/></td>
		</tr>
		<tr>
			<td>名称</td>
			<td><input name="name" id="name" class="text"/></td>
		</tr>
		<tr>
			<td>联系人</td>
			<td><input name="contact" id="contact" class="text"/></td>
		</tr>
		<tr>
			<td>地址</td>
			<td><textarea rows="10" cols="90" name="address"></textarea></td>
		</tr>
		<tr>
			<td>Tel</td>
			<td><input name=tel id="tel" class="text"/></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="email" id="email" class="text"/></td>
		</tr>
		<tr>
			<td>Fax</td>
			<td><input name="fax" id="fax" class="text"/></td>
		</tr>
		<tr>
			<td>AOG Desk Email</td>
			<td><input name="aogDeskEmail" id="aogDeskEmail" class="text"/></td>
		</tr>
		<tr>
			<td>AOG Hotline</td>
			<td><input name="aogHotline" id="aogHotline" class="text"/></td>
		</tr>
		<!-- <tr>
			<td><input name="deadline" id="test"/></td>
		</tr> -->
		
	</table>
	</form>
</div>	
</body>
</html>