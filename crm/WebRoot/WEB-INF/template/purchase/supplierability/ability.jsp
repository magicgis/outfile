<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-供应商订单管理</title>

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
							id:'search',
							icon : 'search',
							text : '展开搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid.setGridHeight(PJ.getCenterHeight()-162);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-102);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: '',
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "s.code",
			sortorder : "desc",
			colNames :["cage code","供应商","件号"],
			colModel :[PJ.grid_column("cageCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:130,align:'left'})
			           ]
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
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm4").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//供应商代码
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/purchase/supplierinquiry/getSuppliers',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm5").append($option);
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
		    	if($("#cageCode").val() != "" && $("#partNumber").val() == ""){
					 PJ.grid_search(grid,'<%=path%>/market/partsinformation/supplierAbilityByCage');
				 }else if($("#cageCode").val() == "" && $("#partNumber").val() != ""){
					 PJ.grid_search(grid,'<%=path%>/market/partsinformation/supplierAbilityByPartNumber');
				 }else if($("#cageCode").val() != "" && $("#partNumber").val() != ""){
					 PJ.warn("cagecode和partNumber不能同时搜索！");
				 }else if($("#cageCode").val() == "" && $("#partNumber").val() == "" && $("#supplier").val() != ""){
					 PJ.grid_search(grid,'<%=path%>/market/partsinformation/supplierAbilityBySupplier');
				 }
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 if($("#cageCode").val() != "" && $("#partNumber").val() == ""){
				 PJ.grid_search(grid,'<%=path%>/market/partsinformation/supplierAbilityByCage');
			 }else if($("#cageCode").val() == "" && $("#partNumber").val() != ""){
				 PJ.grid_search(grid,'<%=path%>/market/partsinformation/supplierAbilityByPartNumber');
			 }else if($("#cageCode").val() != "" && $("#partNumber").val() != ""){
				 PJ.warn("cagecode和partNumber不能同时搜索！");
			 }else if($("#cageCode").val() == "" && $("#partNumber").val() == "" && $("#supplier").val() != ""){
				 PJ.grid_search(grid,'<%=path%>/market/partsinformation/supplierAbilityBySupplier');
			 }
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
	
	function trim(str){
		return $.trim(str);
	}
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:20px;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
/*  th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
} */
</style>
</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>cagecode：</p></form:left>
								<form:right><p><input id="cageCode" class="text" type="text" alias="tm.cage_code" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>供应商：</p></form:left>
								<form:right><p><input id="supplier" class="text" type="text" alias="s.code" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" alias="tp.part_num" oper="cn"/> </p></form:right>
							</form:column>
							<form:column >
							<p style="padding-left:30px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
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