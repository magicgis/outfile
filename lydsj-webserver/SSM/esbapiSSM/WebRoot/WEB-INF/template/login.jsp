<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/reset.css" />
<script src="<%=path%>/resources/js/jquery/jquery-1.11.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/resources/js/jquery.md5/jquery.md5.js"></script>
<script src="<%=path%>/resources/js/PJ.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		if(PJ.inIframe()){
			window.top.location.href='<%=path%>/loginExpired';
		}
		var $loginForm = $("#loginForm");
		var $username = $("#username");
		var $password = $("#password");
		var $enPassword = $("#enPassword");

		// 表单验证
		$loginForm.submit(function() {
			if($username.val()==""){
				$("#message").html("用户名不能为空!");
				$("#tipdiv").show();
				return false;
			}
			if($password.val()==""){
				$("#message").html("密码不能为空!");
				$("#tipdiv").show();
				return false;
			}
			
			$enPassword.val($.md5($password.val()));
			$("#loginBtn").val("登录中...").attr('disabled',"true");
			return true;
		});
		var message = '${message }';
		if(message!=""){
			$("#tipdiv").show();
		}
	});
</script>
</head>

<body style="overflow: hidden;">
<div id="tipdiv" class="tips_w" style="display: none;"><img src="<%=path%>/resources/images/yuan_gantan.png"><span id="message">${message}</span></div>
<div class="login-box">
	<h2 class="tc"></h2>
	<h3 class="tc login-title"></h3>
	<form id="loginForm" action="<%=path%>/login" method="post">
	<input type="hidden" id="enPassword" name="enPassword" />
		<div class="login-box-form">
			<div class="login-user fl"><input type="text" name="username" class="input2"  value=""/></div>
			<div class="login-lock fl"><input type="password" class="input2" id="password" autocomplete="off" value=""/></div>
			<div class="login-btn-box fl"><input type="submit" class="btn btn-orange" value="登 录" autocomplete="off" id="loginBtn"/></div>
			<div class="tc login-footer"></div>
		</div>
	</form>
</div>
</body>
</html>
