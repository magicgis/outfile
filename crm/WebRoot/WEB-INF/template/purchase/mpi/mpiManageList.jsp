<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-供应商订单管理</title>

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
								var iframeId="ideaIframe";
								PJ.topdialog(iframeId, '采购-修改供应商订单', '<%=path%>/purchase/mpi/addMpi',
										function(item, dialog){
												 var postData=top.window.frames[iframeId].getFormData();	
												 if(postData){
												 	$.ajax({
														url: '<%=path%>/purchase/mpi/saveAdd',
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	PJ.success(result.message);
																	dialog.close();
																	PJ.grid_reload(grid);
																} else {
																	PJ.error(result.message);
																}		
														}
													});
												 }
												 
										},function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true,"新增");
								
							}
					 },
					 {
							id:'delete',
							icon : 'delete',
							text : '删除',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret['id'];
							 	$.ajax({
									url: '<%=path%>/purchase/mpi/deleteMpi?id='+id,
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
					 },
			         {
							id:'search',
							icon : 'search',
							text : '展开搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid.setGridHeight(PJ.getCenterHeight()-162);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-102);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/purchase/mpi/listData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "mm.update_timestamp",
			sortorder : "desc",
			inLineEdit: true,
			editurl:'<%=path%>/purchase/mpi/saveEdit',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			colNames :["id","代码","名称","联系人","地址","电话","邮箱","Fax","AOG Hotline","AOG Desk Email"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("code", {sortable:true,width:50,align:'left',editable:true}),
			           PJ.grid_column("name", {sortable:true,width:60,align:'left',editable:true}),
			           PJ.grid_column("contact", {sortable:true,width:50,align:'left',editable:true}),
			           PJ.grid_column("address", {sortable:true,width:130,align:'left',editable:true}),
			           PJ.grid_column("tel", {sortable:true,width:40,editable:true,align:'left'}),
			           PJ.grid_column("email", {sortable:true,width:60,align:'left',editable:true}),
			           PJ.grid_column("fax",{sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("aogHotline", {sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("aogDeskEmail", {sortable:true,width:60,align:'left',editable:true})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	var partNumber = $("#partNumber").val()
				PJ.grid_search(grid,'<%=path%>/purchase/mpi/listData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var partNumber = $("#partNumber").val()
			 PJ.grid_search(grid,'/purchase/mpi/listData');
			
		 });
		
		/* $("#list1").on("click", ".xmidData", function(e){ 
			xmid = e.currentTarget.id;
			alert(xmid);
	    }); */
		
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
		
		function getData(options){
			var a = options;
		}
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	function getData(options){
		var a = options;
	}
	
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:20px;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
/*  th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
} */
</style>
</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>代码：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" alias="mm.CODE" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>名称：</form:left>
								<form:right><p><input id="searchForm12" class="text" type="text" alias="mm.NAME" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<p style="padding-left:30px;">
									<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
									<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
								</p>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>