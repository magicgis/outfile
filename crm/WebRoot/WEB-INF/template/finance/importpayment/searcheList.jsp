<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>查询 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>

</style>
<script type="text/javascript">
var layout, grid;

//存放多选事件的值
var postData = {};

//询价明细index
var indexElement = 0;

//供应商index
var indexSupplier = 0;

$(function(){
	layout = $("#layout1").ligerLayout();
	

	
	grid = PJ.grid("list1", {
		rowNum: 1000,
		url:'',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-142,
		autoSrcoll:true,
		//shrinkToFit:false,
		colNames :["id","供应商id","供应商","订单号","出库单号"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("supplierId", {hidden:true}),
		           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left'}),
		           PJ.grid_column("supplierOrderNumber", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("importPackageNumber", {sortable:true,width:80,align:'left'})
		           ]
	});
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/seach');
	    }  
	});
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/seach');
		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	//改变窗口大小自适应
	$(window).resize(function() {
		grid.setGridWidth(PJ.getCenterWidth());
		grid2.setGridWidth(PJ.getCenterWidth());
		var display = $("#searchdiv").css("display");
		if(display=="block"){
			grid.setGridHeight(PJ.getCenterHeight()-242);
		}else{
			grid.setGridHeight(PJ.getCenterHeight()-142);
		}
	});
	
});

//获取表单数据
function getData(){
	var ret = PJ.grid_getSelectedData(grid);
	var id = ret["id"];
	return id;
 }	
</script>
</head>

<body>
<div id="layout1" style="float: left">
	<div position="center" title="">
			<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="3">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="Number" class="text" type="text" name="Number" alias="so.ORDER_NUMBER" oper="cn"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>入库号：</p></form:left>
								<form:right><p><input id="Number" class="text" type="text" name="Number" alias="ip.IMPORT_NUMBER" oper="cn"/></p></form:right>
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
		<table id="list1"></table>
	</div>
</div>

</body>
</html>