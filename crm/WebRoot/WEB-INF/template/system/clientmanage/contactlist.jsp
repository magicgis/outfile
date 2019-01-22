<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-客户联系人管理</title>

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
			items : [{
						id:'add',
						icon : 'add',
						text : '新增联系人',
						click: function(){
							var iframeId="ideaIframe12";
								PJ.topdialog(iframeId, '系统配置-新增联系人', '<%=path%>/system/clientmanage/toAddContact?id='+${clientId},
										function(item, dialog){
											var postData=top.window.frames[iframeId].getFormData();
											var nullAble=top.window.frames[iframeId].validate();
											if(nullAble){
												$.ajax({
													url: '<%=path%>/system/clientmanage/saveContact',
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
			url:'<%=path%>/system/clientmanage/ContactsList?clientId='+${clientId},
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/system/clientmanage/saveEditContact',
			colNames :["id","联系人名字","邮编","地址","快递地址","电话","传真","邮箱","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("name", {sortable:true,width:150,editable:true,align:'left'}),
			           PJ.grid_column("postCode", {sortable:true,width:100,editable:true,align:'left'}),
			           PJ.grid_column("address",{sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("shipAddress", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("phone",{sortable:true,width:130,editable:true,align:'left'}),
			           PJ.grid_column("fax",{sortable:true,width:130,editable:true,align:'left'}),
			           PJ.grid_column("email",{sortable:true,width:150,editable:true,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:150,editable:true,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'})
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
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="销售-客户询价管理">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>