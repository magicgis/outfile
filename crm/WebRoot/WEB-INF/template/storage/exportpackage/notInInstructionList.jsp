<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>出库询价</title>

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
							text : '修改库位',
							click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var location = ret["location"];
									var id = ret["id"];
									var clientCode = ret["clientCode"];
									var clientOrderElementId = ret["clientOrderElementId"];
									var iframeId = "locationIframeId";
									PJ.topdialog(iframeId, '位置列表 ', '<%=path%>/storage/detail/toChangeLocation?clientId='+${clientId}+'&location='+location+'&clientCode='+clientCode+'&clientOrderElementId='+clientOrderElementId,
											function(item,dialog){
												var postData = top.window.frames[iframeId].getFormData()
												$.ajax({
													type: "POST",
													dataType: "json",
													date:postData,
													url:'<%=path%>/storage/exportpackage/changeLocation?id='+id,
													success:function(result){
														if(result.success){
															PJ.success(result.message)
														}else{
															PJ.warn(result.message)
														}
													}
												});
												dialog.close();
											},function(item,dialog){dialog.close();}, 700, 150, true);
							}
						}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/detail/notInInstructions?id=${id}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "a.location",
			sortorder : "desc",
			colNames :["id","客户代码","客户订单明细Id","位置","件号","数量","客户订单号","入库单号"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientCode", {hidden:true}),
			           PJ.grid_column("clientOrderElementId", {hidden:true}),
			           PJ.grid_column("location", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("storageAmount", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:110,align:'left'}),
			           PJ.grid_column("importNumber", {sortable:true,width:110,align:'left'}),
			           ]
		});
		
		//客户代码来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exportpackage/clientCode',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm1").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportList');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportList');
			
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
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		$("#searchForm3").blur(function(){
			var text = $("#searchForm3").val();
			$("#searchForm3").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 85px;display: none;">
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>