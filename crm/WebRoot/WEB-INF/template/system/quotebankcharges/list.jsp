

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-报价银行费用管理</title>

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
									PJ.topdialog(iframeId, '新增银行费用', '<%=path%>/system/quoteBankCharges/toAdd',
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
													$.ajax({
														url: '<%=path%>/system/quoteBankCharges/savequoteBankCharges',
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
						 } 
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/system/quoteBankCharges/quoteBankChargesManage',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/system/quoteBankCharges/update',
			colNames :["id","客户","银行费用"],
			colModel :[PJ.grid_column("clientId", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("code", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("bankCharges", {sortable:true,width:130,align:'left',editable:true})
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