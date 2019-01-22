<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-机型管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-top;
  padding-top:2px;
 } */
</style>

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
								var iframeId="ideaIframe1";
									PJ.topdialog(iframeId, '新增类型', '<%=path%>/system/parttype/toAddElement',
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
													$.ajax({
														url: '<%=path%>/system/parttype/saveAir',
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	PJ.success(result.message);
																	PJ.grid_reload(grid);
																	dialog.close();
																} else {
																	PJ.error(result.message);
																	dialog.close();
																}		
														}
													});
												}else{
													PJ.warn("数据还没有填写完整！");
												}
											},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
							}
						 } ,{
								id:'add',
								icon : 'add',
								text : '可询价供应商',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId="airSupplierframe";
										PJ.topdialog(iframeId, '系统配置-可询价供应商', '<%=path%>/system/parttype/airSupplier?id='+id,
												undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
									
								}
						 },
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/system/parttype/elementList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/system/airmanage/saveEdit',
			colNames :["id","代码","值","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("code", {sortable:true,width:130,align:'left',editable:true}),
			           PJ.grid_column("value", {sortable:true,width:130,align:'left',editable:true}),
			           PJ.grid_column("remark", {sortable:true,width:130,align:'left',editable:true}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		
	});
	
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