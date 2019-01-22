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
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/detail/partInLoaction?location='+'${location}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-152,
			autoSrcoll:true,
			//shrinkToFit:false,
			colNames :["id","件号","描述","数量","位置","入库单号","供应商"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importPackageNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:120,align:'left'})
			           ]
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
		 
		 $("#searchForm2").keydown(function() {
	         setTimeout(validatea,1000);
	     });
		 
		 function validatea(){
				var number = $("#searchForm2").val();
				var index = parseInt(number.indexOf(")"));
				if(index > 0){
					number = number.substring(3,index);
				}
				var start = number.substring(0,3);
				if(parseInt(start.indexOf("03"))>-1){
					var length = number.length;
					if(length==17){
						var end=start.substring(2,3);
						var importPackageId = number.substring(3,10);
						var clientOrderElementId = number.substring(10,17);
						var postData = {};
						postData.importPackageId = importPackageId;
						postData.clientOrderElementId = clientOrderElementId;
						if(end!=0){
							var sequence = end;
							postData.sequence = sequence;
						}
						$.ajax({
							type: "POST",
							dataType: "json",
							data: postData,
							url:'<%=path%>/storage/detail/checkAndRemoveRecord?location='+'${location}',
							success:function(result){
								if(result.success){
									PJ.success(result.message);
								}else{
									PJ.warn(result.message);
								}
								PJ.grid_reload(grid);
							}
						});
					}else if(length==18){
						var importPackageId = number.substring(4,11);
						var clientOrderElementId = number.substring(11,18);
						var sequence = number.substring(2,4);
						var postData = {};
						postData.importPackageId = importPackageId;
						postData.clientOrderElementId = clientOrderElementId;
						postData.sequence = sequence;
						$.ajax({
							type: "POST",
							dataType: "json",
							data: postData,
							url:'<%=path%>/storage/detail/checkAndRemoveRecord?location='+'${location}',
							success:function(result){
								if(result.success){
									PJ.success(result.message);
								}else{
									PJ.warn(result.message);
								}
								PJ.grid_reload(grid);
							}
						});
					}else if(length==19){
						var importPackageId = number.substring(3,10);
						var clientOrderElementId = number.substring(10,17);
						var sequence = number.substring(17,19);
						var postData = {};
						postData.importPackageId = importPackageId;
						postData.clientOrderElementId = clientOrderElementId;
						postData.sequence = sequence;
						$.ajax({
							type: "POST",
							dataType: "json",
							data: postData,
							url:'<%=path%>/storage/detail/checkAndRemoveRecord?location='+'${location}',
							success:function(result){
								if(result.success){
									PJ.success(result.message);
								}else{
									PJ.warn(result.message);
								}
								PJ.grid_reload(grid);
							}
						});
					}
				}
				$("#searchForm2").val("");
		 }
		 
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
		
		//获取表单数据
		function getFormData2(){
			var $input = $("#searchForm").find("input,textarea,select");
			var postData = {};
				$input.each(function(index){
					if(!$(this).val()) {
						//PJ.tip("必填数据项没有填写完整");
						//flag = true;
						return;
					}
						
					postData[$(this).attr("name")] = $(this).val();
				});
				if($("#taxReturn").is(':checked')==true){
					postData.taxReturn=1;
				}else{
					postData.taxReturn=0;
				}
				return postData;
		}
		
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
			<div id="searchdiv" style="width: 100%;height: 40px;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="2">
							<form:column>
								<form:left><p>件号条码：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="exportNumber"/> </p></form:right>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>