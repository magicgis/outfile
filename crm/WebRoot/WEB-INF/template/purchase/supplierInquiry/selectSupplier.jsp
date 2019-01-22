<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>选择供应商</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
</style> 
<script type="text/javascript">
	
	//获取表单数据
	function getFormData(){
		var check=$("input[type='checkbox']:checked");//.prop("checked");
		var suppliers="";
		$.each(check,function(i){
			suppliers=suppliers+$(this).val()+",";
		});
		return suppliers;
	}
	
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="选择供应商">
	<form id="addForm" action="">
	<table>
		<tr>
			<td><input type="checkbox" value="1003"/>1003</td>
			<td><input type="checkbox" value="1005"/>1005</td>
			<td><input type="checkbox" value="1006"/>1006</td>
			<td><input type="checkbox" value="1077"/>1077</td>
			<td><input type="checkbox" value="6012"/>6012</td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>