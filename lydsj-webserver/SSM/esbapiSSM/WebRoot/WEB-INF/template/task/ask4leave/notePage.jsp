<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>流程部署</title>
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
		<div>请假条</div>
		<div>
			<input type="text" name="id" />
			<table id="noteInfo">
				<tbody>
					<tr>
						<td>姓名</td>
						<td><input type="text" name="qjrXm" /></td>
						<td>部门</td>
						<td><input type="text" name="ssbm" /></td>
						<td>职务</td>
						<td><input type="text" name="bmzw" /></td>
					</tr>
					<tr>
						<td>请假类别</td>
						<td>
							<div><input type="radio" name="qjlbDm" value="0" />&nbsp;事假</div>
							<div><input type="radio" name="qjlbDm" value="1" />&nbsp;病假</div>
							<div><input type="radio" name="qjlbDm" value="2" />&nbsp;婚假</div>
							<div><input type="radio" name="qjlbDm" value="3" />&nbsp;丧假</div>
							<div><input type="radio" name="qjlbDm" value="4" />&nbsp;公假</div>
							<div><input type="radio" name="qjlbDm" value="5" />&nbsp;工伤</div>
							<div><input type="radio" name="qjlbDm" value="6" />&nbsp;产假</div>
							<div><input type="radio" name="qjlbDm" value="7" />&nbsp;其他</div>
						</td>
					</tr>
					<tr>
						<td>请假事由</td>
						<td>
							<textarea name="qjly"></textarea>
						</td>
					</tr>
					<tr>
						<td>请假时间</td>
						<td>
							<div><input type="text" name="kssj" value="" elemType=date />~<input type="text" name="jssj" value="" elemType=date /></div>
						</td>
						<td>总天数</td>
						<td>
							<input type="text" name="zts" />
						</td>
					</tr>
					<tr>
						<td>管理层意见</td>
						<td>
							<textarea name="glcyj"></textarea>
						</td>
					</tr>
					<tr>
						<td>请假人签名</td>
						<td><input type="text" name="qjrqm" /></td>
						<td>管理层签名</td>
						<td><input type="text" name="glcqm" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>