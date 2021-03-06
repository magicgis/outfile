<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统外库存</title>

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
							var iframeId="addIdeaIframe";
							$.ajax({
								url:'<%=path%>/importpackage/checkUser',
								type:'post',
								success: function(result){
									if(result.success){
										PJ.topdialog(iframeId, '新增库存', '<%=path%>/storage/unknowstorage/toAdd',
												function(item, dialog){
														 var postData=top.window.frames[iframeId].getFormData();	
														 if(postData){
														 	$.ajax({
																url: '<%=path%>/storage/unknowstorage/saveAdd',
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
									} else {
										PJ.error(result.message);
										
									}		
								}
							});
						}
					},
					 {
							id:'modify',
							icon : 'modify',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="editIdeaIframe";
								$.ajax({
									url:'<%=path%>/importpackage/checkUser',
									type:'post',
									success: function(result){
										if(result.success){
											PJ.topdialog(iframeId, '修改库存', '<%=path%>/storage/unknowstorage/toEdit?id='+id,
													function(item, dialog){
															 var postData=top.window.frames[iframeId].getFormData();	
															 if(postData){
															 	$.ajax({
																	url: '<%=path%>/storage/unknowstorage/saveEdit',
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
															 
													},function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true,"修改");
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
			url:'<%=path%>/storage/unknowstorage/listData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "usd.update_timestamp",
			sortorder : "desc",
			colNames :["id","件号","描述","数量","已用数量","证书","位置","SN号","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("partNumber", {width:100,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("useAmount", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("certification", {sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("sn", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("updateTimestamp", {sortable:true,width:120,align:'left'})
			           ]
		});
		
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
				PJ.grid_search(grid,'<%=path%>/storage/unknowstorage/listData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/unknowstorage/listData');
			
		 });
		
		/* $("#list1").on("click", ".xmidData", function(e){ 
			xmid = e.currentTarget.id;
			alert(xmid);
	    }); */
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
	 	//所有输入框的前后空格去掉
		$("input").blur(function(){
			var text = this.value;
			var id = "#"+this.id;
			$(id).val(trim(text));
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
						<form:row columnNum="3">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" alias="usd.part_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>证书：</p></form:left>
								<form:right><p><input id="certification" class="text" type="text" alias="usd.certification" oper="cn"/> </p></form:right>
							</form:column>
							<form:column >
							<p style="padding-left:10px;">
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