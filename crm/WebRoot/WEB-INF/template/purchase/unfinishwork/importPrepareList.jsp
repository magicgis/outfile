<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商发货清单 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />

<script type="text/javascript">
var layout, grid;

$(function(){
	layout = $("#layout1").ligerLayout();
	
	$("#toolbar").ligerToolBar({
		items : [  
					{
						id:'search',
						icon : 'search',
						text : '展开搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
								var display = $("#searchdiv").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid1.setGridHeight(PJ.getCenterHeight()-142);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid1.setGridHeight(PJ.getCenterHeight()-102);
								}
							});
						}
				 	}
				]
	});
	
	$("#toolbar2").ligerToolBar({
		items : [
					{	icon : 'logout',
						text : '导出excel',
						click : function() {
								PJ.grid_export("list2");
							}
					}
		         ]
	});
	
	/* $("#exact").change(function(){
		if($("#exact").prop("checked")){
			$("#partNumber").attr("oper","eq");
		}else if(!$("#exact").prop("checked")){
			$("#partNumber").attr("oper","cn");
		}
	}); */
	
	grid1 = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/purchase/supplierorder/ImportPrepare',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-142,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "soe.update_timestamp",
		sortorder : "desc",
		colNames :["id","供应商订单号","件号","数量","运输方式","运输单号","目的地","更新日期"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("orderNumber", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("amount", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("shipWayValue", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("awb", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("destination", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("updateTimestamp", {sortable:true,width:150,align:'left'}),
		           ]
	});
	
	
	
	//改变窗口大小自适应
	$(window).resize(function() {
		grid.setGridWidth(PJ.getCenterWidth());
		var display = $("#searchdiv").css("display");
		if(display=="block"){
			grid.setGridHeight(PJ.getCenterHeight()-242);
		}else{
			grid.setGridHeight(PJ.getCenterHeight()-182);
		}
	});
	
	//客户代码来源
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/sales/clientinquiry/clientCode',
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
	
	//机型
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/sales/clientinquiry/airType',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
					$("#searchForm3").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
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
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid1,'<%=path%>/purchase/supplierorder/ImportPrepare');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid1,'<%=path%>/purchase/supplierorder/ImportPrepare');
		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
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
			<form id="searchForm" action="">
				<div id="searchdiv">
						<form:row columnNum="4">
							<form:column>
								<form:left><p>供应商订单号</p></form:left>
								<form:right>
											<p>
												<input name="orderNumber" id="orderNumber" alias="so.ORDER_NUMBER"/>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>订单日期</p></form:left>
								<form:right><p><input id="orderStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'orderEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="orderStart" alias="so.ORDER_DATE" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="orderEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderStart\')}',dateFmt:'yyyy-MM-dd'})" name="orderEnd" alias="so.ORDER_DATE" oper="lt"/></form:right>
							</form:column>
							<form:column>
								<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
								</p>
							</form:column>
						</form:row>
						
				</div>
			</form>
				
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
</div>

</body>
</html>