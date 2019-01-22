<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>绑定供应商</title>

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
								text : '绑定供应商',
								click: function(){
									var iframeId="supplierIframe";
										PJ.topdialog(iframeId, '绑定供应商', '<%=path%>/system/competitormanage/toAddMapSupplierAndCompetitor?id='+${competitorId},
												function(item, dialog){
													var postData=top.window.frames[iframeId].getFormData();
													$.ajax({
														url: '<%=path%>/system/competitormanage/saveSupplierAndCompetitorMapper?id='+${competitorId},
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
												},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
								}
						 },
						 {
								id:'delete',
								icon : 'delete',
								text : '删除',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									PJ.confirm("确定删除？", function(yes){
										$.ajax({
											url: '<%=path%>/system/competitormanage/delSupplierAndCompetitorMapper?id='+id,
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
									});
								}
						 }
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/system/competitormanage/mapSupplierAndCompetitorList?id='+${competitorId},
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			colNames :["id","客户","供应商"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:130,align:'left'})
			           ]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
		
	});
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;display: none;">
		<form id="searchForm" action="">
			<div class="search-box">
			</div>
			
		</form>
		</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>