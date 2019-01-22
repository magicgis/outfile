<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>组任务</title>
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>  
	<script type="text/javascript">
		var task_grid, times=0;
		$(function(){
			$("#div_main").ligerLayout();
			
			//工具栏
    		$("#task_toolbar").ligerToolBar({
    			items : [{
    				icon : 'refresh',
    				text : '刷新',
    				click : function() {
    					PJ.grid_reload(task_grid);
    				}
    			} ,{
    				icon : 'right',
    				text : '接收任务',
    				click : function(rowid, status, e) {
    					var ids = PJ.grid_getMutlSelectedRowkey(task_grid);
    					if (ids)
    					{
	    					PJ.ajax({
	    						url: '<%=path%>/home/takeTask',
	    						data:'taskIds=' + ids,
	    						success:function(result){
	    							if(result.success){
	    								PJ.tip(result.message,function(){
	    									PJ.grid_reload(task_grid);
	    									window.parent.reloadPersonalTask();
	    								});
	    							}else{
	    								PJ.error(result.message);
	    							}
	    						}
	    					});
    					}
    				}
    			} ]
    		});
			
    		task_grid = PJ.grid("task_grid", {
				url: '<%=path%>/home/groupTaskData',
				width : PJ.getCenterWidth(),
				height : PJ.getCenterHeight() - 112,
    			pager: '#task_page',
    			multiselect:true,
    			colNames : ["任务ID","所属模块", "任务名称"],
    			colModel : [PJ.grid_column("id", {key:true}),
    			            PJ.grid_column("deploymentName", {}),
    						PJ.grid_column("name", {})],
				gridComplete:function(){
					var num = task_grid.jqGrid('getRowData').length;
					if(num > 0)
					{
						parent.layout.setRightCollapse(false);
					}
				}
    		});
			
    		//jqGrid 自适应
    		$(window).resize(function() {
    			resize();
    		});
    		
    		$("#task_page_left").css("display","none");
    		$("#task_page_right").css("display","none");
		});
		
		function resize(){
			task_grid.setGridWidth(PJ.getCenterWidth());
			task_grid.setGridHeight(PJ.getCenterHeight() - 112);
		}
	</script>
</head>
<body style="overflow: hidden;">
	<div id="div_main">
		<div position="center">
			<div id="task_toolbar"></div>
		    <table id="task_grid"></table>
		    <div id="task_page"></div>
		</div>
	</div>
</body>
</html>