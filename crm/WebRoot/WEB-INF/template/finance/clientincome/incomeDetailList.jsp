<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>收款明细 </title>

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
			url:'<%=path%>/sales/clientorder/incomeDetail?id='+${incomeId},
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/sales/clientorder/editIncomeDetail',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			sortname : "id.UPDATE_TIMESTAMP",
			colNames :["id","订单明细id","件号","单价","数量","总价","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("clientOrderElementId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("price", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:300,editable:true,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/sales/clientorder/incomeDetail?id='+${incomeId});
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/sales/clientorder/incomeDetail?id='+${incomeId});
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-182);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
	});
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="收款明细 ">
		<input type="text" name="isBlacklist" id="isBlacklist" class="hide"/>
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>