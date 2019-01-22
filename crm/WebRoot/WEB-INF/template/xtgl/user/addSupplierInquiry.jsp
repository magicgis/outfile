<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增供应商询价 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
	.l-layout { width: 53%; margin: 0; padding: 0}
	th{bgcolor:#f5f5f6;}
</style>
<script type="text/javascript">
var layout,layout2, grid,grid2;

//存放多选事件的值
var postData = {};

//询价明细index
var indexElement = 0;

//供应商index
var indexSupplier = 0;

$(function(){
	layout = $("#layout1").ligerLayout();
	
	
	grid2 = PJ.grid("list2", {
		rowNum: ${count2},
		url:'<%=path%>/purchase/supplierinquiry/suppliers',
		width : PJ.getCenterWidth()-120,
		height : PJ.getCenterHeight()-100,
		multiselect:true,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "ci.inquiry_date",
		colNames :["id","代码","供应商"],
		colModel :[PJ.grid_column("sid", {key:true,hidden:true}),
		           PJ.grid_column("code", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("name", {sortable:true,width:400,align:'left'})
		           ]
	});
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/suppliers');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/suppliers');
		
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
			grid2.setGridHeight(PJ.getCenterHeight()-242);
		}else{
			grid.setGridHeight(PJ.getCenterHeight()-142);
			grid2.setGridHeight(PJ.getCenterHeight()-142);
		}
	});
	
});

//获取表单数据
function getData(){
	var postData = {};
	var suppliers =  PJ.grid_getMutlSelectedRowkey(grid2);
	supplierIds = suppliers.join(",");
	if(supplierIds.length>0){
		postData.supplierIds = supplierIds;
	}
	return postData;
 }	
</script>
<style>
	#cb_list2{
		margin:0
	}
</style>
</head>

<body>
<div id="layout1" style="float: left">
	<div position="center" title="">
		<div id="toolbar"></div>
		<table id="list1"></table>
	</div>
</div>

<div id="layout1" style="float: right;width: 47%; margin: 0; padding: 10px">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;">
				<form id="searchForm">
					<div class="search-box">
						<table>
							<tr>
								<td>关键字：</td>
								<td><input id="searchForm2" class="text" type="text" name="main" alias="s. NAME or s. CODE or sc.VALUE" oper="cn"/></td>
								<td><input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/></td>
								<td><input class="btn btn_blue" type="button" value="重置" id="resetBtn"/></td>
							</tr>
						</table>
					</div>
				</form>
		</div>			
		<table id="list2"></table>
</div>

</body>
</html>