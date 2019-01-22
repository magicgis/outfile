<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商询价单统计 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function(){
	layout = $("#layout1").ligerLayout();
	$("#toolbar").ligerToolBar({
		items : [	 {
		    id:'logout',
			icon : 'logout',
			text : '导出',
			click: function(){
				PJ.grid_export("list1");
			}
	 },
		         {
						id:'search',
						icon : 'search',
						text : '展开搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
								var display = $("#searchdiv").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid.setGridHeight(PJ.getCenterHeight()-202);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-102);
								}
							});
						}
				 }
		         ]
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
					$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
					$("#air_type_value").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
	});
	
	//商业类型
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/sales/clientinquiry/bizType',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
					$("#biz_type_code").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
	});
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/supplierquote/findsid',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].code);
					$("#supplier_code").append($option);
				}
			}else{
					PJ.showWarn(result.msg);
			}
		}
	}); 
	
	grid = PJ.grid("list1", {
		rowNum: -1,
		url:'',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-202,
		autoSrcoll:true,
		shrinkToFit:false,
		sortname : "ci.update_timestamp",
		sortorder : "desc",
		colNames :["id","供应商","类型","商业类型","询价单数量","报价单数量","报价单金额","订单数量","订单金额","出库单数量","出库单金额"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("supplierCode", {sortable:true,width:110,align:'left'}),
		           PJ.grid_column("airTypeCode", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("bizTypeCode", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("supplierInquiryCount", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("supplierQuoteCount",{sortable:true,width:140,align:'left'}),
		           PJ.grid_column("supplierQuoteSum", {sortable:true,width:170,align:'left'}),
		           PJ.grid_column("supplierOrderCount",{sortable:true,width:150,align:'left'}),
		           PJ.grid_column("supplierOrderSum",{sortable:true,width:170,align:'left'}),
		           PJ.grid_column("supplierImportCount",{sortable:true,width:150,align:'left'}),
		           PJ.grid_column("supplierImportSum",{sortable:true,width:170,align:'left'})
		           ]
	});
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid,'<%=path%>/supplierstatistic/listdate?sid='+$("#supplier_code").val()+'&airId='+$("#air_type_value").val()
					+'&bizId='+$("#biz_type_code").val()+'&start='+$("#quotedatestart").val()+'&end='+$("#quotedateend").val());
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/supplierstatistic/listdate?sid='+$("#supplier_code").val()+'&airId='+$("#air_type_value").val()
				+'&bizId='+$("#biz_type_code").val()+'&start='+$("#quotedatestart").val()+'&end='+$("#quotedateend").val());
		
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
	
});


</script>
</head>

<body>
<div id="layout1">
	<div position="center">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 100px;display: block;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="5">
					<form:column>
							      <form:left>供应商</form:left>
							   <form:right><p><select  id="supplier_code" name="supplier_code" >
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							<form:column>
							      <form:left>类型</form:left>
							   <form:right><p><select  id="air_type_value" name="air_type_value" >
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>商业类型</form:left>
							   <form:right><p><select id="biz_type_code" name="biz_type_code">
							            	<option value="">全部</option>
							            	</select>
							          </p>
								</form:right>
							</form:column>
						
					</form:row>
					<form:row columnNum="5">	
							<form:column>
								<form:left><p>日期：</p></form:left>
								<form:right><p><input id="quotedatestart"  class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'quotedateend\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="quotedatestart" /> 
							 </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left> 
							<form:right><p>
							<input id="quotedateend"  class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'quotedatestart\')}',dateFmt:'yyyy-MM-dd'})" name="quotedateend" /> 
							</p></form:right>
							</form:column>
								<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							
						<form:column ><form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
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