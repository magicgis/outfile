<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增供应商询价 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
	.l-layout { width: 53%; margin: 0; padding: 0}
	th{bgcolor:#f5f5f6;}
</style>
<script type="text/javascript">
var layout,layout2, grid,grid2;

//存放多选事件的值
var postData = {};

//询价明细index
var indexElement = 0;

//供应商index
var indexSupplier = 0;

$(function(){
	layout = $("#layout1").ligerLayout();
	//供应商归类
	 $.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/suppliermanage/classifyList?id='+'${data.id}',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option=$("<option></option>");
					$option.val(obj[i].id).text(obj[i].value);
					$("#supplierClassify").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
	});

	
	grid = PJ.grid("list1", {
		rowNum: ${count},
		url:'<%=path%>/sales/clientinquiry/elementData?id='+${id },
		width : PJ.getCenterWidth()-1,
		height : PJ.getCenterHeight()-75,
		multiselect:true,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "cie.item",
		onSelectRow:function(rowid,status,e){
			var parts =  PJ.grid_getMutlSelectedRowkey(grid);
			if(parts.length == undefined){
				var select = "0个已勾选";
				$(".l-layout-header").html(select);
			}else{
				var select = parts.length+"个已勾选";
				$(".l-layout-header").html(select);
			}
		},
		colNames :["id","CiId","序号","件号","另件号","描述","单位","数量","黑名单","备注"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("clientInquiryId", {hidden:true}),
		           PJ.grid_column("item", {sortable:true,width:40,align:'left'}),
		           PJ.grid_column("partNumber", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("alterPartNumber", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("description", {sortable:true,width:125,align:'left'}),
		           PJ.grid_column("unit", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("amount",{sortable:true,width:60,align:'left'}), 
		           PJ.grid_column("isBlacklist",{sortable:true,width:60,align:'left',
						  formatter: function(value){
								if (value==1||value=="是") {
									return "是";
								} else if(value==0||value=="否"){
									return "否";							
								}else {
									return "";
								}
							}
		           }),
		           PJ.grid_column("remark", {sortable:true,width:80,align:'left'})
		           ]
	});
	
	grid2 = PJ.grid("list2", {
		rowNum: ${count2},
		url:'<%=path%>/purchase/supplierinquiry/suppliers',
		width : PJ.getCenterWidth()-120,
		height : PJ.getCenterHeight()-130,
		multiselect:true,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "ci.inquiry_date",
		colNames :["id","代码","供应商"],
		colModel :[PJ.grid_column("sid", {key:true,hidden:true}),
		           PJ.grid_column("code", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("name", {sortable:true,width:400,align:'left'})
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
					$("#searchForm3").append($option);
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
	    	var ability = $("#ability").val();
			if(ability != ""){
				PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/suppliers?ability='+ability);
			}else{
				PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/suppliers');
			}
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		var ability = $("#ability").val();
		if(ability != ""){
			PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/suppliers?ability='+ability);
		}else{
			PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/suppliers');
		}
		
		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	//改变窗口大小自适应
	$(window).resize(function() {
		grid.setGridWidth(PJ.getCenterWidth());
		grid2.setGridWidth(PJ.getCenterWidth());
		var display = $("#searchdiv").css("display");
		if(display=="block"){
			grid.setGridHeight(PJ.getCenterHeight()-242);
			grid2.setGridHeight(PJ.getCenterHeight()-242);
		}else{
			grid.setGridHeight(PJ.getCenterHeight()-142);
			grid2.setGridHeight(PJ.getCenterHeight()-142);
		}
	});
	
});

//获取表单数据
function getData(){
	var postData = {};
	var parts =  PJ.grid_getMutlSelectedRowkey(grid);
	var suppliers =  PJ.grid_getMutlSelectedRowkey(grid2);
	orderElementIds = parts.join(",");
	supplierIds = suppliers.join(",");
	if(orderElementIds.length>0 && supplierIds.length>0){
		postData.orderElementIds = orderElementIds;
		postData.supplierIds = supplierIds;
	}
	return postData;
 }	
</script>
<style>
	#cb_list1{
		margin:0
	}
	#cb_list2{
		margin:0
	}
</style>
</head>

<body>
<div id="layout1" style="float: left">
	<div position="center" id="sel" title="0个已勾选">
		<div id="toolbar"></div>
		<table id="list1"></table>
	</div>
</div>

<div id="layout1" style="float: right;width: 47%; margin: 0; padding: 10px">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 80px;">
				<form id="searchForm">
					<div class="search-box">
						<table>
							<tr>
								<td>关键字：</td>
								<td colspan="2"><input id="searchForm2" class="text" type="text" style="" name="main" alias="s. NAME or s. CODE or sc.VALUE" oper="cn"/></td>
								<td>能力：</td>
								<td colspan="2"><select id="ability" name="ability" >
							            			<option value="">全部</option>
							            			<option value="1">维修</option>
							            			<option value="2">库存</option>
							            			<option value="3">供应</option>
							            			<option value="4">交换</option>
							            		</select>
							    </td>
							</tr>
							<tr>
								<td>类型：</td>
								<td><select id="searchForm3" name="airTypeValue" alias="sar.air_id" oper="eq">
							            			<option value="">全部</option>
							            			</select></td>
							    <td>供应商归类：</td>
								<td><select id="supplierClassify" name="supplierClassify" alias="sc.id" oper="eq">
							            			<option value="">全部</option>
							            			</select></td>
								<td><input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/></td>
								<td><input class="btn btn_blue" type="button" value="重置" id="resetBtn"/></td>
							</tr>
						</table>
					</div>
				</form>
		</div>			
		<table id="list2"></table>
</div>

</body>
</html>