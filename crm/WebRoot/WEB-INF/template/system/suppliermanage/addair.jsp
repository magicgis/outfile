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
 	
	var layout, grid;
	$(function() {
		layout = $("#layout_main").ligerLayout();
		
		
		

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ 
			       
			         ]
		});
		
		
		grid = PJ.grid("list", {
			rowNum: -1,
			url: "<%=path%>/supplierquote/airtypedata",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-70,
			autoSrcoll:true,
			shrinkToFit:true,
			multiselect:true,
			sortname : "",
			colNames : ["id", "机型"],
			colModel : [ PJ.grid_column("id", {sortable:true,width:100,key:true,hidden:true}),
			            PJ.grid_column("code", {sortable:true,width:100})
						]
		});
		
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		
	});
	
	
	
	 function getFormData(){
		 var postData = {};
		 var rowKey = grid.getGridParam("selarrrow");
		 if(rowKey!= ""){
			 var air =  PJ.grid_getMutlSelectedRowkey(grid);
			 ids = air.join(",");
			 if(ids.length>0){
					postData.ids = ids;
				}
		 }
		 if(rowKey== ""){
				PJ.warn("请选择需要操作的数据");
				return false;
		 }
		 postData.supplierId=${supplierId}
			return postData;
	 }	
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body style="padding:3px">
<input type="text" class="hide" name="conditionCode" id="conditionCode" value="">
<input type="text" class="hide" name="certificationCode" id="certificationCode" value="">
<input type="text" class="hide" name="supplierQuoteStatusValue" id="supplierQuoteStatusValue" value="">
<input type="text" class="hide" name="supplierInquiryId" id="supplierInquiryId" value="${supplierInquiryId}">
	<div id="layout_main">
		<div position="center" title="">
		
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
