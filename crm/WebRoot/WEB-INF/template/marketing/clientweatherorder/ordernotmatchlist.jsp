<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>错误信息</title>

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
							text : '根据item关联',
							click: function(){
								var ids = getData()
								$.ajax({
									type: "POST",
									dataType: "json",
									url:'<%=path%>/sales/clientorder/connectByItem?ids='+ids,
									success:function(result){
										if(result.success){
											PJ.grid_reload(grid);
											PJ.showWarn(result.messahe);
										}else{
											PJ.showWarn(result.messahe);
										}
									}
								});
							}
					 },
					 {
							id:'edit',
							icon : 'edit',
							text : '不录入',
							click: function(){
								var ids = getData()
								$.ajax({
									type: "POST",
									dataType: "json",
									url:'<%=path%>/sales/clientorder/unCommit?ids='+ids,
									success:function(result){
										if(result.success){
											PJ.grid_reload(grid);
											PJ.showWarn(result.messahe);
										}else{
											PJ.showWarn(result.messahe);
										}
									}
								});
							}
					 }
				]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/sales/clientorder/orderNotMatch',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			multiselect:true,
			//shrinkToFit:false,
			colNames :["id","item","part_number","错误描述"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("error",{sortable:true,width:200,align:'left'})
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
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		$("#searchForm2").change(function(){
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/sales/clientinquiry/test',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						
					}
				}
			});
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	//获取表单数据
	function getData(){
		var postData = {};
		var ids =  PJ.grid_getMutlSelectedRowkeyNotValidate(grid);
		if (ids != ""){
			importPackageElementIds = ids.join(",");
			return importPackageElementIds;
		}else{
			return "";
		}
		
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
		<div position="center" title="错误信息">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>