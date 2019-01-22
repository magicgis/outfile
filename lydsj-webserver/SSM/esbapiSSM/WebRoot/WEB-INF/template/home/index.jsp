<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>我的主页</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
	<script type="text/javascript">
		$(function() {
			layout = $("#layout1").ligerLayout({
				rightWidth:500,
				isRightCollapse:true,
				onRightToggle:function(){
					resize();
				}
			});
			
			$(window).resize(function() {
				resize();
			});
			resize();
		});
		
		function reloadPersonalTask()
		{
			window.frames["centeriframe"].reloadPersonalTask();
		}
		
		function resize(){
			var centerwidth=PJ.getCenterWidth();
			var centerheight = PJ.getCenterHeight()-30;
			$("#centeriframe").css("height",centerheight);
			$("#centeriframe").css("width",centerwidth);
			var rightWidth = $("#layout1 .l-layout-right").width();
			var rightHeight = $("#layout1 .l-layout-right").height()-30;
			$("#rightiframe").css("width",rightWidth);
			$("#rightiframe").css("height",rightHeight);
		}
	</script>
</head>
<body style="padding:2px">
	<div id="layout1">
		<div position="center" title="个人任务">
			<iframe id="centeriframe" name="centeriframe" frameborder="0" src="<%=path%>/home/personalTask" scrolling="auto"></iframe>
		</div>
		<div position="right" title="组任务">
			<iframe id="rightiframe" name="rightiframe" frameborder="0" src="<%=path%>/home/groupTask" scrolling="auto"></iframe>
		</div>
	</div>
</body>
</html>