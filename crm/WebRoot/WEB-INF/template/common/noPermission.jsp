<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>没有权限访问</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
</head>

<body>
	<div style="margin: auto;width: 500px;height: 500px;margin-top: 100px;text-align: center">
		<i class="fa fa-times-circle-o" style="height: 200px;line-height: 200px;font-size: 200px;color: red"></i>
		<p>Sorry！你没有权限进行访问该页面，请联系管理员</p>
	</div>
</body>
</html>
