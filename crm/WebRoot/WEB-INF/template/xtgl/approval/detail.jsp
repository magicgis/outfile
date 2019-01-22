<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title> </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function(){
	layout = $("#layout1").ligerLayout();
	$("#toolbar").ligerToolBar({
		items : [ {
			icon : '导出excel',
			text : '导出excel',
			click : function(){
				PJ.grid_export("list");
			}
		}]});
	grid = PJ.grid("list1", {
		rowNum: 10,
		url:'<%=path%>/xtgl/approval/detailPage/data?approvalId='+${approvalId},
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-80,
		autoSrcoll:true,
		shrinkToFit:true,
		sortname : "updateDatetime",
		sortorder : "desc",
		colNames :["流水号","时间","更新人","状态","意见"],
		colModel :[
		           	PJ.grid_column("approvalDetailId", {key:true, sortable:true, hidden:true}),
		           	PJ.grid_column("updateDatetime", {sortable:false,width:200}),
		           	PJ.grid_column("updaterName", {sortable:false,width:100}),
		           	PJ.grid_column("approvalStatus", {sortable:false,width:50,
			        	   formatter:function(value, options, data){
			        		   if(value==0){
			        			   return "草稿";
			        		   }else if(value==1){
			        			   return "发起中";
			        		   }else if(value==2){
			        			   return "审批中";
			        		   }else if(value==3){
			        			   return "审批通过";
			        		   }else if(value==4){
			        			   return "审批否决";
			        		   }else if(value==5){
			        			   return "取消";
			        		   }
			        	   }		           				           	
		           	}),
		           	PJ.grid_column("comment", {sortable:false,width:500})
		           ]
	});
	
});
</script>
</head>

<body>
<div id="layout1">
	<div position="center" title="${title}">
	<div id="toolbar"></div>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>
</body>
</html>