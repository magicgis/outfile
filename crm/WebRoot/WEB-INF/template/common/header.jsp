<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>

<body>
	<div id='top-layout'>
		<div class="top">
			<div class="admin_logo">
				BetterAir客户关系管理系统
			</div>
			<div class="top-right fr">
				<div class="mt10 tr">
				<span class="pr">欢迎您：${user.userName}</span>
					<span><a href="javascript:void(0);" id="changePass">修改密码</a></span>
					<span><a href="javascript:void(0);" id="logout">注销</a></span></div>
			</div>
		</div>
</body>
</html>
