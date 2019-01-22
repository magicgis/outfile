<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>流程部署</title>
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import> 
	<script type="text/javascript"> 
	var ask4levaeGrid;
    $(function (){
    	
    	$("#div_main").ligerLayout();
    	
    	//操作工具栏
		$("#toolbar").ligerToolBar({
			items : [
		         	{
						icon : 'add',
						text : '新增',
						click : function() {
							editNote("add");
						}
					},{
						icon : 'edit',
						text : '编辑',
						click : function() {
							editNote("edit");
						}
					},{
						icon : 'edit',
						text : '提交审批',
						click : function() {
							

							var selectRow = PJ.grid_getSelectedData(grid);;

							if( !selectRow ) return;
							PJ.popupAssigneeWindow( 
								{
									type:"1",
									roleid: "0"
								},
								function(assigneedata){
									PJ.ajax({
										url:'<%=path%>/testJbpm/submitReview?ids='+assigneedata+'&id='+selectRow.id,
										data:'',
										success:function(result){
											if(result.success){
												PJ.tip(result.message);
											}else{
												PJ.error(result.message);
											}
										}
									});
								}
							);
						}
					},/* {
						icon : 'delete',
						text : '删除',
						click: function(){
							
						}
					},  */{
						icon : 'refresh',
						text : '刷新',
						click: function(){
							PJ.grid_reload(grid);
						}
					}
					]
		});
    	
		grid = PJ.grid("grid", {
			url: '<%=path%>/testJbpm/getNoteList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 142,
			pager: '#Page',
			colNames : ["ID", "信息", "备注"],
			colModel : [PJ.grid_column("id", {key:true}),
					PJ.grid_column("message", {}),
					PJ.grid_column("remark", {})]
		});
		
		function editNote(type){
			
			var selectRow={id:""},id;
			if( type === "edit" ) {
				selectRow = PJ.grid_getSelectedData(grid);
				
			}
			
			if( !selectRow.id && type === "edit"  ) return;
			
			var url = "<%=path%>/testJbpm/dataPage?type="+type+"&id="+selectRow.id;
			
			var iframeId = 'dataFrame';
			var saveCallback = function(item,dialog){
				var postData = {};
				postData = top.window.frames[iframeId].getFormData();
				
				PJ.ajax({
					loading:'保存中...',
					url: "<%=path%>/testJbpm/save?id="+selectRow.id,
					data: postData,
					success:function(result){
						if(result.success){
							PJ.success(result.message, function(){
								PJ.grid_reload(grid);
								dialog.close();
							})
						}else{
							PJ.error(result.message);
						}
					}
				});
			}
			var cancleCallback = function(item,dialog){PJ.grid_reload(grid);dialog.close();}
		
			var callbackOpts = {
					save: saveCallback,
					cancle: cancleCallback,
					saveTitle: "保存",
					cancelTitle: "取消",
					width: 1000,
					height: 560,
					showMax: true
			};
			
			PJ.openTopDialog(iframeId, "编辑", url, callbackOpts);
		}
		
    });
    </script> 
</head>
<body>
	<div id="div_main">
		<div position="center" title="列表">
			<div id="toolbar"></div>
		    <table id="grid"></table>
		    <div id="Page"></div>
		</div>
	</div>
</body>
</html>