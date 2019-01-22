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
		$("#ask4leaveToolbar").ligerToolBar({
			items : [
		         	{
						icon : 'add',
						text : '新增假条',
						click : function() {
							editNote("add");
						}
					},{
						icon : 'edit',
						text : '编辑假条',
						click : function() {
							editNote("edit");
						}
					},{
						icon : 'edit',
						text : '提交审批',
						click : function() {
							

							var selectRow = PJ.grid_getSelectedData(ask4levaeGrid);;

							if( !selectRow ) return;
							PJ.popupAssigneeWindow( 
								{
									type:"1",
									roleid: "0"
								},
								function(assigneedata){
									PJ.ajax({
										url:'<%=path%>/ask4leave/submitReview?ids='+assigneedata+'&id='+selectRow.id,
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
					},{
						icon : 'delete',
						text : '删除假条',
						click: function(){
							
						}
					}, {
						icon : 'refresh',
						text : '刷新',
						click: function(){
							PJ.grid_reload(ask4levaeGrid);
						}
					}
					]
		});
    	
		ask4levaeGrid = PJ.grid("ask4levaeGrid", {
			url: '<%=path%>/ask4leave/getNoteList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 142,
			pager: '#ask4levaePage',
			colNames : ["请假条ID", "请假人姓名", "所属部门", "部门职位", "请假类别代码", "请假类别", "创建时间", "审批状态"],
			colModel : [PJ.grid_column("id", {key:true}),
					PJ.grid_column("qjrXm", {}),
					PJ.grid_column("ssbm", {}),
					PJ.grid_column("bmzw", {}),
					PJ.grid_column("qjlbDm", {}),
					PJ.grid_column("qjlbMc", {}),
					PJ.grid_column("cjsj", {}),
					PJ.grid_column("spzt", {})]
		});
		
		function editNote(type){
			
			var selectRow={id:""},id;
			if( type === "edit" ) {
				selectRow = PJ.grid_getSelectedData(ask4levaeGrid);
				
			}
			
			if( !selectRow.id && type === "edit"  ) return;
			
			var url = "<%=path%>/ask4leave/notePage?type="+type+"&id="+selectRow.id;
			
			var iframeId = 'noteFrame';
			var saveCallback = function(item,dialog){
				var postData = {};
				postData = top.window.frames[iframeId].getFormData();
				
				PJ.ajax({
					loading:'发起流程中...',
					url: "<%=path%>/ask4leave/saveNote",
					data: postData,
					success:function(result){
						if(result.success){
							PJ.success(result.message, function(){
								PJ.grid_reload(ask4levaeGrid);
								dialog.close();
							})
						}else{
							PJ.error(result.message);
						}
					}
				});
			}
			var cancleCallback = function(item,dialog){PJ.grid_reload(ask4levaeGrid);dialog.close();}
		
			var callbackOpts = {
					save: saveCallback,
					cancle: cancleCallback,
					saveTitle: "保存",
					cancelTitle: "取消",
					width: 1000,
					height: 560,
					showMax: true
			};
			
			PJ.openTopDialog(iframeId, "编辑假条", url, callbackOpts);
		}
		
    });
    </script> 
</head>
<body>
	<div id="div_main">
		<div position="center" title="请假单列表">
			<div id="ask4leaveToolbar"></div>
		    <table id="ask4levaeGrid"></table>
		    <div id="ask4levaePage"></div>
		</div>
	</div>
</body>
</html>