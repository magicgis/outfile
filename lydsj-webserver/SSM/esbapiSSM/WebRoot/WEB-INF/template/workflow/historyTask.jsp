<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>我已办理的任务</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
$(function(){
	//初始化tab
	$("#div_tab").ligerTab({
		onAfterSelectTabItem : function(tabId) {
			if (tabId == "lct") {
				var navtab = $("#div_tab").ligerGetTabManager();
				navtab.reload(navtab.getSelectedTabItemID());
			}
		}
	});
	///布局
	$("#rwcllayout").ligerLayout({
		rightWidth : 300
	});
	
	var url = basePath + '/workflow/viewFlowInfo?&businessKey=${gyJbyj.businessKey}';
	$("#lcFrame").attr("src",url);
	
	var taskInfoUrl = '${gyJbyj.taskInfoUrl }';
	if(taskInfoUrl && taskInfoUrl!=''){
		$("#taskFrame").attr('src', basePath + taskInfoUrl);
	}
	
	$("#lcFrame").css("height",PJ.getCenterHeight());
})
</script>
</head>

<body>
	<div id="div_tab">
		<div tabid="taskInfo" title="任务信息" id="div_main">
			<div id="rwcllayout">
				<div position="center" title="任务信息">
					<iframe frameborder="0" id="taskFrame" src="" scrolling="auto" ></iframe>
				</div>
				<div position="right" title="我的处理">
					<!----------经办意见-------->
					<div class="box-content">
						<h3 class="title">
							<span>处理结果</span>
						</h3>
						<form:row>
							<form:column width="0">
								<form:left></form:left>
								<form:right><p>${task.outcome }</p></form:right>
							</form:column>
						</form:row>
						<h3 class="title">
							<span>经办意见</span>
						</h3>
						<form:row>
							<form:column width="0">
								<form:left></form:left>
								<form:right><p>${gyJbyj.jbyj }</p></form:right>
							</form:column>
						</form:row>
					</div>
				</div>
			</div>
		</div>
		<div tabid="lct" id="lct" title="流程图">
			<iframe frameborder="0" class="info" id="lcFrame" src="<%=path%>/flow/viewFlowInfo?businessKey="
				scrolling="auto" width="100%" height="100%"></iframe>
		</div>
	</div>
</body>
</html>
