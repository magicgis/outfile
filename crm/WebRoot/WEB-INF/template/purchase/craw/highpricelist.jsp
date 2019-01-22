<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商订单明细管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />

<script type="text/javascript">
var layout, grid;

$(function(){
	layout = $("#layout1").ligerLayout();
	
	var shipway;
	var destination;
	var os;
	var orderStatusValue;
	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/supplierquote/highPrice',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-112,
		autoSrcoll:true,
		shrinkToFit:true,
		sortname : "sq.quote_date",
		sortorder : "desc",
		colNames :["id","件号","另件号","报价单号","价格","币种","报价日期","更新时间"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("partNumber", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("alterPartNumber", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("quoteNumber", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("price", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("currencyValue", {sortable:true,width:30,editable:true,align:'left'}),
		           PJ.grid_column("quoteDate", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("updateTimestamp", {sortable:true,width:140,align:'left'})
		           ]
	});
	
	function CountFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '0';
			break;
			
		
			
		default: 
			return cellvalue;
			break; 
		}
	}
	
	function taxFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '否';
			break;
		case 1:
			return '是';
			break;
			
		default: 
			return cellvalue;
			break; 
		}
	}
	
	//改变窗口大小自适应
	$(window).resize(function() {
		grid.setGridWidth(PJ.getCenterWidth());
		grid.setGridHeight(PJ.getCenterHeight()-112);
	});
	
});

</script>
</head>

<body>
<div id="layout1">
		<div position="center" title="爬虫报价">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>

</body>
</html>