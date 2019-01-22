<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>任务处理默认页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var navtab;
	//页面参数（审批提交路径、任务信息路径、任务ID、待办事项、连线）
	var PARAM_URL_SUBMIT, PARAM_URL_INFO, PARAM_TASKID, PARAM_BUSINESSKEY, PARAM_JOBS, PARAM_OUTCOMES;
	$(function() {
		//初始化页面参数
		initParam();
		//初始化页面布局及样式
		initStyle();
		//初始化待办事项
		initJob();
		//初始化连线
		initOutCome();
	});

	//初始化页面参数
	function initParam() {
		var param = ${pageParam};
		PARAM_URL_SUBMIT = param.submitUrl;
		PARAM_URL_INFO = param.urlTaskInfo;
		PARAM_TASKID = param.taskId;
		PARAM_BUSINESSKEY = param.businessKey;
		PARAM_JOBS = param.jobs;
		PARAM_OUTCOMES = param.outcomes;
		
 		var url = "<%=path%>/workflow/viewFlowInfo?businessKey="+PARAM_BUSINESSKEY;
		$("#lcFrame").attr("src",url);
	}

	//初始化页面布局及样式
	function initStyle() {
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
		
		//设置待办事项区域高度
		$(".box-content-rwxx").css("height",(PJ.getCenterHeight()-260)+"px");
		
		//任务信息
		if(PARAM_URL_INFO && !PARAM_URL_INFO.startWith("/")){
			PARAM_URL_INFO = "/"+PARAM_URL_INFO;
		}
		$('#taskFrame').attr('src', basePath + PARAM_URL_INFO);
		$("#taskInfoUrl").val(PARAM_URL_INFO);//保存任务处理页面url
		
		//标签页对象
		navtab = top.liger.get("framecenter");
		
		$("#lcFrame").css("height",PJ.getCenterHeight());
	}

	//初始化待办事项
	function initJob() {
		if(PARAM_JOBS){
			if(PARAM_JOBS.length>0){
				for ( var i in PARAM_JOBS) {
					var job = PARAM_JOBS[i];
					var jobName = job.jobName;
					var status = job.status;
					var url = job.url;
					var cando = job.cando;
					if(status==1){
						$(".box-dbsx-content").append("<li class=\"green-wancheng\"><span class=\"yuan\"></span><div class=\"b-r-l\">"+jobName+"</div><div class=\"b-r-r\"><input type=\"button\" value=\"查看\" class=\"btn btn_green\" onclick=\"dealJob('" + job.jobName + "','" + job.url + "', '" + job.urlSubmit + "',false)\"></div></li>");
					}else{
						$(".box-dbsx-content").append("<li class=\"red-daiban\"><span class=\"yuan\"></span><div class=\"b-r-l\">"+jobName+"</div><div class=\"b-r-r\"><input type=\"button\" value=\"处理\" class=\"btn btn_green\" onclick=\"dealJob('" + job.jobName + "','" + job.url + "', '" + job.urlSubmit + "',true)\"></div></li>");
					}
				}
			}else{
				$(".box-dbsx-content").remove();
				$(".box-content-rwxx").append("<p>无待办内容</p>");
			}
		}else{
			$(".box-dbsx-content").remove();
			$(".box-content-rwxx").append("<p>无待办内容</p>");
		}
	}

	//初始化连线
	function initOutCome() {
		if(PARAM_OUTCOMES){
			if(PARAM_OUTCOMES.length>0){
				for ( var i in PARAM_OUTCOMES) {
					var outcome = PARAM_OUTCOMES[i];
					$("#outcomes").append("<input type='button' value='"+outcome.name+"' class='btn btn_"+outcome.color+"' style='margin-top:5px;' outcome='"+JSON.stringify(outcome)+"' onclick='finishTask(this);'/>");
				}
			}
		}
	}
	
	//处理待办事项
	function dealJob(jobName, url, urlSubmit,sx){
		var iframeId = 'dealJobFrame';
	 	PJ.openTopDialog(iframeId, jobName, basePath + url, {
	 		cancle:function(item,dialog){
	 			dialog.close();
	 			if(sx)
	 				navtab.reload(navtab.getSelectedTabItemID());
	 		},
	 		showMax:true,
	 		width:1000,
	 		height:500
	 	});
	}
	
	//任务提交
	function finishTask(outcome){
		outcome = $(outcome).attr("outcome");
		outcome = eval("("+outcome+")");
		//判断待办是否都已经完成
		if(outcome.checkJob == '1' && PARAM_JOBS){
			if(PARAM_JOBS.length>0){
				for ( var i in PARAM_JOBS) {
					var job = PARAM_JOBS[i];
					var jobName = job.jobName;
					var status = job.status;
					if(status==0){
						PJ.warn("提交任务前请完成待办中的 【"+jobName+"】事项!");
						return ;
					}					
				}
			}
		}
		PJ.confirm("确定 【" + outcome.name + "】?", function(yes){
			if(yes){
				var outcomeuser = outcome.outcomeUser;
				$('#outcome').val(outcome.name);
				$("#taskDescription").val(outcome.taskDescription);
				$("#beforeEvent").val(outcome.beforeEvent);
				$("#afterEvent").val(outcome.afterEvent);
				$('#taskId').val(PARAM_TASKID);
				
				/**
				"outcomeUser":{"type":"1","roleId":"1","params":[],"assignee":null}
				**/
				
				PJ.popupAssigneeWindow( 
					{
						roleid : outcomeuser.roleId,
						type : outcomeuser.type,
						assignee : outcomeuser.assignee
					},
					function(assigneedata){
						console.log(assigneedata);
						completeTask( assigneedata );
					}
				);
				
			}
		});
	}
	
	//任务提交
	function completeTask(assignee){
		$('#assignee').val(assignee);
		
		if(!PARAM_URL_SUBMIT.startWith("/")){
			PARAM_URL_SUBMIT = "/"+PARAM_URL_SUBMIT;
		}
		PJ.ajax({
			url: basePath + PARAM_URL_SUBMIT,
			data:$("#form").serialize(),
			loading:'提交经办意见中...',
			success:function(result){
				if(result.success){
					PJ.success(result.message, function(){
						top.closeCurrentTab();
						navtab.reload('home');
					});
				}else{
					PJ.error(result.message);
				}
			}
		});
	}
</script>
</head>

<body>
	<div id="div_tab">
		<div tabid="taskInfo" title="任务处理" id="div_main">
			<div id="rwcllayout">
				<div position="center" title="任务信息">
					<iframe frameborder="0" id="taskFrame" src=""
						scrolling="auto"></iframe>
				</div>
				<div position="right" title="任务处理">
					<!----------待办事项-------->
					<div class="box-content box-content-rwxx">
						<h3 class="title">
							<span>待办事项</span>
						</h3>
						<ul class="box-dbsx-content" >
						</ul>
					</div>

					<!----------经办意见-------->
					<div class="box-content box-content-jbyj">
						<h3 class="title">
							<span>经办意见</span>
						</h3>
						<p style="margin-bottom: 5px;">
						<form id="form">
							<input type="hidden" name="taskId" id="taskId"/>
							<input type="hidden" name="outcome" id="outcome"/>
							<input type="hidden" name="assignee" id="assignee"/>
							<input type="hidden" name="taskDescription" id="taskDescription"/>
							<input type="hidden" name="beforeEvent" id="beforeEvent"/>
							<input type="hidden" name="afterEvent" id="afterEvent"/>
							<input type="hidden" name="taskInfoUrl" id="taskInfoUrl"/>
							<textarea id="comment" name="comment" clos="20" rows="5" class="textbox" placeHolder="请填写经办意见"></textarea>
						</form>
						</p>
						<p id="outcomes">
						</p>
					</div>
					<!----------经办意见 end-------->
				</div>
			</div>
		</div>
		<div tabid="lct" id="lct" title="流程图">
			<iframe frameborder="0" class="info" id="lcFrame" src=""
				scrolling="auto" width="100%" height="100%"></iframe>
		</div>
	</div>
</body>
</html>
