<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改密码</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript"
	src="<%=path%>/resources/js/jquery.md5/jquery.md5.js"></script>
<script type="text/javascript">
	//子页面表单验证必须实现的验证方法
	function validate() {
		var v = $("#form").validate({
			rules : {
				oldpass : {
					required : true,
					minlength : 6,
					maxlength : 22
				},
				npass : {
					required : true,
					minlength : 6,
					maxlength : 22
				},
				ncpass : {
					required : true,
					minlength : 6,
					maxlength : 22,
					equalTo : "#npass"
				}
			},
			messages : {
				oldpass : {
					required : "密码不能为空",
					minlength : "密码长度最少为{0}位",
					maxlength : "密码长度不能超过{0}位"
				},
				npass : {
					required : "新密码不能为空",
					minlength : "密码长度最少为{0}位",
					maxlength : "密码长度不能超过{0}位"
				},
				ncpass : {
					required : "确认密码不能为空",
					minlength : "密码长度最少为{0}位",
					maxlength : "密码长度不能超过{0}位",
					equalTo : "两次输入密码不一致"
				}
			},
			errorPlacement : function(error, element) {
				var elem = $(element);
				var err = $(error);
				elem.attr("title", err.html())
			},
			success : function(element) {
				element.removeClass("error");
				element.removeAttr("title");
			}
		});
		if (v.form()) {
			return true;
		} else {
			v.defaultShowErrors();
			return false;
		}
	}

	//子页面必须提供表单数据方法
	function getFormData() {
		var oldpass = $.md5($("#oldpass").val());
		var npass = $.md5($("#npass").val());

		return "oldpass=" + oldpass + "&npass=" + npass;
	}
</script>
</head>

<body>
	<form id="form" class="table-box"
		style="border:none;padding-top: 10px;">
		<form:row columnNum="1">
			<form:column width="80">
				<form:left>旧密码</form:left>
				<form:right>
					<p>
						<input class="input" type="password" name="oldpass" id="oldpass" />
					</p>
				</form:right>
			</form:column>
		</form:row>
		<form:row columnNum="1">
			<form:column width="80">
				<form:left>新密码</form:left>
				<form:right>
					<p>
						<input class="input" type="password" name="npass" id="npass" />
					</p>
				</form:right>
			</form:column>
		</form:row>
		<form:row columnNum="1">
			<form:column width="80">
				<form:left>确认新密码</form:left>
				<form:right>
					<p>
						<input class="input" type="password" name="ncpass" id="ncpass" />
					</p>
				</form:right>
			</form:column>
		</form:row>
	</form>
</body>
</html>
