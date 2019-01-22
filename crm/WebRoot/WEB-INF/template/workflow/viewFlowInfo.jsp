<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<title>流程信息</title>
<script type="text/javascript">
	$(function(){
		var tabHeight = parseInt($('#div_tab', parent.window.document).css('height').replace('px', ''));
		$('#lcFrame', parent.window.document).css({'height' : tabHeight});
	});
</script>
</head>
<body style="overflow: auto;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" id="tableId">
		<tr>
			<td>
				<h2>&nbsp;&nbsp;流程实例属性</h2>
			</td>
		</tr>
		<tr>
			<td>
				<table width="98%" border="0" cellpadding="0" cellspacing="0" class="table_blue">
					<tr>
						<th>流程名称</th>
						<th>版本号</th>
						<th>当前处理人/候选人</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th>启动人</th>
						<th>状态</th>
					</tr>
					<tr>
						<td>${definition.name}</td>
						<td>${definition.version}</td>
						<td>${task.assignee}</td>
						<td>
							<fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>${startUser}</td>
						<td>${instance.state}</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<h2>&nbsp;&nbsp;工作项列表</h2>
			</td>
		</tr>
		<tr>
			<td>
				<table width="98%" border="0" cellpadding="0" cellspacing="0"
					class="table_blue">
					<tr>
						<th width="15%">时间</th>
						<th width="12%">参与人</th>
						<th>结果</th>
						<th>经办意见</th>
					</tr>
					<c:forEach var="comment" items="${comments }">
						<tr>
							<td>${comment.time }</td>
							<td title="${comment.userId }">${comment.userName }</td>
							<td>${comment.outcome }</td>
							<td>${comment.message }</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<h2>&nbsp;&nbsp;流程动态监控图</h2>
			</td>
		</tr>
		<tr>
			<td>
				<iframe src="<%=path %>/workflow/viewFlowChartByBusinessKey?businessKey=${businessKey}" id="viewIframe"
					scrolling="no" frameborder="0"></iframe><br />
			</td>
		</tr>
	</table>
	
</body>
</html>