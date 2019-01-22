<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户询价明细管理 </title>

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
							id:'edit',
							icon : 'edit',
							text : '取消发送标注',
							click: function(){
								var postData = getData()
								if(postData.length > 0){
									$.ajax({
										url: '<%=path%>/purchase/supplierinquiry/cancelEmailSend',
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
								}
							}
					 }
							 
			         ]
		});
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/purchase/supplierinquiry/crawlEmailSupplierList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:false,
			sortname : "pae.update_datetime",
			sortorder : "desc",
			multiselect:true,
			colNames :["id","supplierId","件号","供应商","取消标志"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("supplierId", {hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("cancel", {sortable:true,width:170,align:'left',
			        		formatter:statusFormatter
			           }),
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
		
		function getData(){
			var postData = {};
			var partNumbers = new Array();
			var supplierIds = new Array();
			var key =  PJ.grid_getMutlSelectedRowkey(grid);
			for(var k=0; k<key.length; k++) {
		         var curRowData = jQuery("#list1").jqGrid('getRowData', key[k]);
		         partNumbers.push(curRowData['partNumber']);
		         supplierIds.push(curRowData['supplierId']);
			}
				
			postData.partNumbers = partNumbers.join(",");
			postData.supplierIds = supplierIds.join(",");
			return postData;
		}
		
		function statusFormatter(cellvalue, options, rowObject){
			var cancel = rowObject["cancel"];
			
			switch (cancel) {
				case 0:
					return "";
					break;
					
				case 1:
					return "是";
					break;
					
				default: 
					return cellvalue;
					break; 
			}
		}
		
	});
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
<input type="text" name="isBlacklist" id="isBlacklist" class="hide"/>
	<div id="layout1">
		<div position="center" title="询价单号   ${quoteNumber } ">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>