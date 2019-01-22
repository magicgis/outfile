<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新建</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript" src="<%=path%>/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor/ueditor.all.js"></script>
<script type="text/javascript">
	//子页面表单验证必须实现的验证方法
	function validate() {
		var v = $("#form").validate({
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
		return $("#form").serialize();
	}
	
	$(function() {
		var ue = UE.getEditor('content', {
			elementPathEnabled : false,
			wordCount : false,
			initialFrameWidth : '98%',
			initialFrameHeight : 320,
			initialContent : ''
		});
	});
</script>
</head>

<body>
	<form id="form">
		<div class="table-box">
			<h3 class="title">
				<span>填写以下内容</span>
			</h3>
			<form:row columnNum="1">
				<form:column>
					<form:left>
						<p>关键字</p>
					</form:left>
					<form:right>
						<p>
							<ef:editForm name="code" type="text" cssName="input" required="true"/>
						</p>
					</form:right>
				</form:column>
			</form:row>
			<form:row columnNum="1">
				<form:column>
					<form:left>
						<p>帮助标题</p>
					</form:left>
					<form:right>
						<p>
							<ef:editForm name="title" type="text" cssName="input" required="true"/>
						</p>
					</form:right>
				</form:column>
			</form:row>
			<form:row columnNum="1">
				<form:column>
					<form:left>
						<p>帮助内容</p>
					</form:left>
					<form:right>
						<p>
							<textarea id="content" name="content"></textarea>
						</p>
					</form:right>
				</form:column>
			</form:row>
		</div>
	</form>
</body>
</html>
