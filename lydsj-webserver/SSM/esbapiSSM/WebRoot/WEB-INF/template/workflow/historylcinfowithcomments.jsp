<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>流程办理信息</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
</head>

<body>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" >
		<tr>
			<td>
				<h3 class="h3title"><span>流程实例属性</span></h3>
			</td>
		</tr>
		<tr>
			<td>
				<table width="98%" border="0" cellpadding="0" cellspacing="0" class="table_blue">
					<tr>
						<th width="80">流程名称</th>
						<th width="80">流程关键字</th>
						<th width="80">版本号</th>
						<th width="80">开始时间</th>
						<th width="80">结束时间</th>
						<th width="70">启动人</th>
						<th width="40">状态</th>
					</tr>
					<tr>
						<td>${deployment.name }</td>
						<td>${processDefinition.key }</td>
						<td>${processDefinition.version }</td>
						<td>
							<fmt:formatDate value="${processInstance.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td><fmt:formatDate value="${processInstance.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td title="${starter.userId }">${starter.userName }</td>
						<td>
							<c:choose>
								<c:when test="${processInstance.endTime==null }">
									运行中
								</c:when>
								<c:when test="${processInstance.endTime!=null }">
									停止
								</c:when>
							</c:choose>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<h3 class="h3title"><span>工作项列表</span></h3>
			</td>
		</tr>
		<tr>
			<td>
				<table width="98%" border="0" cellpadding="0" cellspacing="0"
					class="table_blue">
					<tr>
						<th width="15%">时间</th>
						<th width="12%">参与人</th>
						<th width="12%">任务名称</th>
						<th width="12%">处理结果</th>
						<th>经办意见</th>
					</tr>
					<c:forEach var="comment" items="${comments }">
						<tr>
							<td><fmt:formatDate value="${comment.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td title="${comment.userId }">${comment.userName }</td>
							<td>${comment.taskName }</td>
							<td>${comment.outcome }</td>
							<td>${comment.jbyj }</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<h3 class="h3title"><span>流程动态监控图</span></h3>
			</td>
		</tr>
		<tr>
			<td><iframe src="<%=path %>/workflow/viewlct?businessKey=${businessKey}&history=${history}"
					scrolling="no" frameborder="0"></iframe><br />
			</td>
		</tr>
	</table>
</body>
</html>
