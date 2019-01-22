<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-竞争对手管理</title>

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
									PJ.topdialog(iframeId, '新增竞争对手', '<%=path%>/system/competitormanage/toAdd',
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
													$.ajax({
														url: '<%=path%>/system/competitormanage/saveCompetitor',
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
						 },
						 {
								id:'modify',
								icon : 'modify',
								text : '修改',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId="ideaIframe2";
										PJ.topdialog(iframeId, '修改竞争对手', '<%=path%>/system/competitormanage/toEdit?id='+id,
												function(item, dialog){
														 var postData=top.window.frames[iframeId].getFormData();	 							
														 $.ajax({
																url: '<%=path%>/system/competitormanage/saveEdit',
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
												},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
									
								}
						 },
						 {
								id:'view',
								icon : 'view',
								text : '已绑定供应商',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId="ideaIframe3";
									PJ.topdialog(iframeId, '已绑定供应商列表', '<%=path%>/system/competitormanage/toMapSupplierAndCompetitorList?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
									
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
											grid.setGridHeight(PJ.getCenterHeight()-152);
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
			url:'<%=path%>/system/competitormanage/competitorManage',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			colNames :["id","代码","名称","币种","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("code", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("name", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("currencyValue", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/system/competitormanage/competitorManage');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/system/competitormanage/competitorManage');
			
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
				<form:row columnNum="3">
					<form:column>
						<form:left><p>代码</p></form:left>
						<form:right><p><input id="code" name="code" class="text" alias="c.code" oper="cn"/></p></form:right>
					</form:column>
					<form:column>
						<form:left><p>名称</p></form:left>
						<form:right><p><input id="name" name="name" class="text" alias="c.name" oper="cn"/></p></form:right>
					</form:column>
					
					<form:column >
							<p style="padding-left:200px;">
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