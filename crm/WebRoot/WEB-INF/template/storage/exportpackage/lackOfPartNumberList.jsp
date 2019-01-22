<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>缺少件清单</title>

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
					]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exportpackage/lack?exportPackageId='+${exportPackageId},
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			//shrinkToFit:false,
			//sortname : "",
			colNames :["id","订单号","件号","位置","单位","数量","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("orderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:250,align:'left'})
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
		<div position="center" title="缺少件清单">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>