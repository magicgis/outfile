<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>明细列表</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
 	//-- Set Attribute
 	var ayclUuid = "${ayclUuid}";
 	
	var layout, grid;
	$(function() {
		layout = $("#layout_main").ligerLayout();

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ 
			{
				icon : 'add',
			    text : '新增',
				click : function(){
			    btnFunction.viewWs();
			}
			},        
		 	{
				icon : 'process',
				text : '修改',
				click : function(){
					btnFunction.viewWs();
				}
			}]
		});
		grid = PJ.grid("list", {
			rowNum: 20,
			url: "<%=path%>/clientquote/clientquoteelementdate?clientinquiryquotenumber="+"${client_inquiry_quote_number}",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 182,
			onSelectRow:function(rowid,status,e){},
			shrinkToFit:true,
			sortname : "",
			colNames : [ "序号", "件号", "描述","数量", "单位",
			             "供应商", "件号", "备注","单位", "数量", "单价",
			             "数量","单价", "总价","利润率","备注","更新时间"],
			colModel : [ PJ.grid_column("item", {sortable:true,width:150,align:'left'}),
			             PJ.grid_column("inquiry_part_number", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("inquiry_description", {sortable:true,width:350,align:'left'}),
						PJ.grid_column("inquiry_amount", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("inquiry_unit", {sortable:true,width:150,align:'left'}),
						
						PJ.grid_column("supplier_code", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("inquiry_part_number", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("quote_remark", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("inquiry_unit", {sortable:true,width:350,align:'left'}),
						PJ.grid_column("supplier_quote_amount", {sortable:true,width:400,align:'left'}),
						PJ.grid_column("supplier_quote_price", {sortable:true,width:400,align:'left'}),
						
						PJ.grid_column("client_quote_amount", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("client_quote_price", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("countprice", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("profit_margin", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("client_quote_remark", {sortable:true,width:350,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:400,align:'left'})
						]
		});
		
		 grid.jqGrid('setGroupHeaders', { 
		 	useColSpanStyle: true, 
		 	groupHeaders:[ 
		 		{startColumnName: 'item', numberOfColumns: 5, titleText: '<div align="center"><span>客户询价</span></div>'},
		 		{startColumnName: 'supplier_code', numberOfColumns: 6, titleText: '<div align="center"><span>供应商询价</span></div>'},
		 		{startColumnName: 'client_quote_amount', numberOfColumns: 6, titleText: '<div align="center"><span>客户报价</span></div>'}
		 	] 
		}); 
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid.setGridHeight(PJ.getCenterHeight()-142);
		});
		
	});
	
	
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
</head>

<body style="padding:3px">
<input type="text" class="hide" name="client_inquiry_quote_number" value="${client_inquiry_quote_number}">
	<div id="layout_main">
		<div position="center" title="询价单号:  ${client_inquiry_quote_number}">
		<div id="toolbar"></div>
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
