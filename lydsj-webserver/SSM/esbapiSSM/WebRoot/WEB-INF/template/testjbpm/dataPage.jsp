<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>测试流程</title>
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import> 
	<script type="text/javascript"> 

    $(function (){
		PJ.datepickerAll();
    });
    
	//-- 提交的表单数据
	function getFormData(){
		var postData ={};
		var $valItems = $("#noteInfo").find("input,textarea,radio:checked");
		
		$valItems.each(function(i){
			if($(this).attr("disabled") || $(this).attr("readonly")) return;//-- 禁用输入框不取值
			if( !$(this).attr("name") )return;//-- name为空也不取值
			
			if($(this).is("span")) postData[$(this).attr("name")] = $(this).text();//-- 特殊处理span标签
			else postData[$(this).attr("name")] = $(this).val();
		});
		return postData;
	}
    </script> 
</head>
<body>
	<div>
		<div>
			<table id="noteInfo">
				<tbody>
					<tr>
						<td>信息</td>
						<td><input type="text" name="message" /></td>
						<td>备注</td>
						<td><input type="text" name="remark" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>