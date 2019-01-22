<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>添加人员 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [
					 {
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe5";
							PJ.topdialog(iframeId, '新增人员', '<%=path%>/xtgl/role/addPeople?id='+${id },
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
												url: '<%=path%>/xtgl/role/savePeople',
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
														} else {
															PJ.error(result.message);
														}		
												}
											});
									dialog.close();
									PJ.grid_reload(grid);}
								   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {	id:'delete',
						icon : 'delete',
						text : '删除',
						click: function(){
							 PJ.confirm("确定删除？", function(yes){
								 	var ret = PJ.grid_getSelectedData(grid);
								 	var userId = ret["userId"];
								 	var roleId = ret["roleId"];
									if(yes){
										$.ajax({
											url: '<%=path%>/xtgl/role/deletePeople?userId='+userId+'&roleId='+roleId,
											type: "POST",
											loading: "正在处理...",
											success: function(result){
													if(result.success){
														PJ.success(result.message);
														PJ.grid_reload(grid);
													} else {
														PJ.error(result.message);
													}		
											}
										});
									}
							    });
							 
					 	}
					 }
					 
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/xtgl/role/People?id='+${id },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/xtgl/user/EditPower',
			//sortname : "ci.inquiry_date",
			colNames :["userId","roleId","用户"],
			colModel :[PJ.grid_column("userId", {key:true,hidden:true}),
			           PJ.grid_column("roleId", {key:true,hidden:true}),
			           PJ.grid_column("userName", {sortable:true,width:100,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-182);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
	});
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>