<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function() {
	
	//获取数据列表
	//数据区的工具栏
	$("#toolbar").ligerToolBar({
		items:[
				
		       ]
	});
	
	layout = $("#layout1").ligerLayout();

	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/xtgl/user/userdatalist',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-72,
		autoSrcoll:true,
		shrinkToFit:true,
		inLineEdit: true,
		editurl:'<%=path%>/xtgl/user/editUser',
		//caption:"案件列表",
		sortname : "userId",
		colNames : [ "用户编号", "用户姓名"],
		colModel : [ 	PJ.grid_column("userId", {sortable:true,hidden:true}),
						PJ.grid_column("userName", {width:150,align:'left'})
					
						]
				
	});
	grid.jqGrid('setFrozenColumns');
		//改变窗口大小自适应

	
});
//获取表单数据
function getFormData(){
	  var postData = {}; 
	 var ret = PJ.grid_getSelectedData(grid);
	 var userId = ret["userId"];
	 postData.userId=userId;
	 return postData;
 }
</script>
</head>
<body>
	<div id="layout1">
		<div position="center">
			<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>

</body>
</html>