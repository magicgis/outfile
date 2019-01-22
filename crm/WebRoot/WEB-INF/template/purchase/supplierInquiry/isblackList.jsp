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
	
		
		
		grid = PJ.grid("list1", {
			rowNum: -1,
			url:'<%=path%>/sales/clientinquiry/isblacklistData?partNumber='+'${partNumber}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-112,
			autoSrcoll:true,
			multiselect:true,
			//shrinkToFit:false,
			sortname : "ci.inquiry_date",
			colNames :["bsn","件号","描述","cage code","厂商","是否黑名单"],
			colModel :[PJ.grid_column("bsn", {key:true,hidden:true}),
			           PJ.grid_column("partNum", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("partName", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("cageCode", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("manName", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("isBlacklist",{sortable:true,width:90,align:'left',
							  formatter: function(value){
									if (value==1||value=="是") {
										return "是";
									} else if(value==0||value=="否"){
										return "否";							
									}else {
										return "";
									}
								}
			           })
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
	
	function getData(){
		var postData = {};
		var parts =  PJ.grid_getMutlSelectedRowkey(grid);
		blacklist = parts.join(",");
		if(blacklist.length>0 ){
			postData.blacklist = blacklist;
		}
		return postData;
	 }	
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>

</head>

<body>
	<div id="layout1">
		<div position="center" title="询价单号   ${quoteNumber } ">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>