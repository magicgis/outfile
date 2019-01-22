<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>${title} </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function(){
	layout = $("#layout1").ligerLayout();
	$("#toolbar").ligerToolBar({
		items : [
		         {
						id:'search',
						icon : 'search',
						text : '收起搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
								var display = $("#searchdiv").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid.setGridHeight(PJ.getCenterHeight()-195);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-142);
								}
							});
						}
				 },
				 {
						id:'add',
						icon : 'search',
						text : '查询件号',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							if(ret==undefined){
								return;
							}
							var cageId = ret["cageId"];
							var iframeId="ideaIframe";
								PJ.topdialog(iframeId, '按件号nsn号查询 ', '<%=path%>/rfqstock/nsncenter/listByNsnPart?cageId='+cageId,
										undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
						}
				 }				 
		         ]
	});
	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/rfqstock/nsncenter/searchByCage',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-200,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "cageId",
		sortorder : "desc",
		colNames :["CAGE_ID","CAGE名称","类型","ADP_CNT_CT","商业类型","曾用名","联盟","SIZE", "CAO"],
		colModel :[
		           	PJ.grid_column("cageId", {key:true, sortable:true, width:50}),
		           	PJ.grid_column("cageName", {sortable:false,width:80}),
		           	PJ.grid_column("additionalInfo", {sortable:false,width:30, 
		        	   formatter:function(value, options, data){
		        		   var obj = JSON.parse(value.replace(/'/g, '"').replace(/u"/g,'"'));
		        		   return obj.type;
		        	   }}),
			       	PJ.grid_column("additionalInfo", {sortable:false,width:50, 
			        	   formatter:function(value, options, data){
			        		   var obj = JSON.parse(value.replace(/'/g, '"').replace(/u"/g,'"'));
			        		   return obj.adp_cnt_ct;
			        	   }}),
					PJ.grid_column("additionalInfo", {sortable:false,width:50, 
				        	   formatter:function(value, options, data){
				        		   var obj = JSON.parse(value.replace(/'/g, '"').replace(/u"/g,'"'));
				        		   return obj.type_of_business;
				        	   }}
					),
					PJ.grid_column("additionalInfo", {sortable:false,width:100, 
			        	   formatter:function(value, options, data){
			        		   var obj = JSON.parse(value.replace(/'/g, '"').replace(/u"/g,'"'));
			        		   return obj.former_name;
			        	   }}
					),
					PJ.grid_column("additionalInfo", {sortable:false,width:50, 
			        	   formatter:function(value, options, data){
			        		   var obj = JSON.parse(value.replace(/'/g, '"').replace(/u"/g,'"'));
			        		   return obj.affiliation;
			        	   }}
					),
					PJ.grid_column("additionalInfo", {sortable:false,width:50, 
			        	   formatter:function(value, options, data){
			        		   var obj = JSON.parse(value.replace(/'/g, '"').replace(/u"/g,'"'));
			        		   return obj.size;
			        	   }}
					),
					PJ.grid_column("additionalInfo", {sortable:false,width:50, 
			        	   formatter:function(value, options, data){
			        		   var obj = JSON.parse(value.replace(/'/g, '"').replace(/u"/g,'"'));
			        		   return obj.cao;
			        	   }}
					)
		           ]
	});
	
	//搜索
	$("#searchBtn").click(function(){
	 	var cageId  = $("#cageId").val();
		PJ.grid_search(grid,'<%=path%>/rfqstock/nsncenter/searchByCage?cageId='+cageId);
		
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
	<div position="center" title="${title}">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;display: unblock;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="3">
						<form:column>
							<form:left><p>CAGE ID：</p></form:left>
							<form:right><p><input id="cageId" class="text" type="text" name="cageId"/> </p></form:right>
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