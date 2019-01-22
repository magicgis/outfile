<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>选择任务处理人</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript">
		/**
		 * 无需选人,则判断是否赋值用户id和角色id，任务默认发送所有符合条件的人
		 */
		var SELECT_TYPE_NO = "0";
		/**
		 * 角色选人，弹窗选人
		 */
		var SELECT_TYPE_ROLE = "1";
		/**
		 * 所有人，弹窗列出所有人
		 */
		var SELECT_TYPE_ALL = "2";
		
		var selectGrid;
		var type="${type}",roleid="${roleid}";
		$(function(){
			
	    	$("#div_main").ligerLayout();
	    	
	    	var gridOpts = {
								rowNum: 1000,
								url: "<%=path%>/workflow/getAssigneerList?type=${type}&roleid=${roleid}",
								width : PJ.getCenterWidth(),
								height : PJ.getCenterHeight(),
								autoSrcoll:true,
						        multiselect : true,
								shrinkToFit:false,
								sortname : "roleName",
								sortorder : "desc",
								colNames : ["用户ID","用户名称","用户角色ID", "用户角色"],
								colModel : [
								            PJ.grid_column("userId", {key:true,hidden:true}),
					                        PJ.grid_column("userName", {sortable:true,width:250}),
					                        PJ.grid_column("roleId", {sortable:true,hidden:true}),
					                        PJ.grid_column("roleName", {sortable:true,width:150})
											],
					            viewrecords: true
					        };
	    	
	    	if( type === SELECT_TYPE_ROLE ) {
	    		gridOpts.grouping = true;
	    		gridOpts.groupingView = {
							              groupField : ['roleName'],
							              groupColumnShow : [false],
							              groupText : ['<b>职位：{0}</b>']
							            };
	    	}
	    	
	    	
			selectGrid = PJ.grid("selectGrid", gridOpts);
			
		});
		
		function getSelectRows(){
			 var s = PJ.grid_getMutlSelectedRowkey(selectGrid);
			 
			 var userIds = [];
			 for( var k in s ){
				 var ret = selectGrid.jqGrid('getRowData',s[k]);
				 userIds.push(ret.userId);
			 }
			 
			 return userIds;
		}
	</script>
  </head>
  
  <body style="overflow-x: hidden;">
	<div id="div_main">
		<div position="center" title="人员列表">
	 		<input type="hidden" name="" id=""/>
	    	<div>
	    		<div id="swjgToolbar"></div>
				<table id="selectGrid"></table>
	    	</div>
    	</div>
   	</div>
  </body>
</html>
