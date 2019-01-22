<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>帮助详情</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="http://resource.tax12366.com.cn/nasmis/css/common.css"
	rel="stylesheet" type="text/css" />
</head>

<body>
	<div class="news-box">
		<h1>${gyHelp.title }</h1>
		<p>${gyHelp.content }</p>
	</div>
</body>
</html>
