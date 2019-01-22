<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>库存流水</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
	var layout, grid;
	$(function() {
		
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
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchclient',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].client_code).text(obj[i].client_code);
						$("#client_code").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		
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
							grid.setGridHeight(PJ.getCenterHeight()-222);
						}else{
							$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
							grid.setGridHeight(PJ.getCenterHeight()-122);
						}
					});
				}
			},{	
				  id:'add',
				  icon : 'add',
					text : '打印条码',
					click : function() {
						 var ret = PJ.grid_getSelectedData(grid);
				 		    var importPackageId = ret["importPackageId"];
				 		    var supplierOrderElementId=ret["supplierOrderElementId"];
				 		   var amount=ret["amount"];
				 		  var partNumber=ret["partNumber"];
						var $a = $("<a href='<%=path%>/importpackage/partiframes?id="+supplierOrderElementId+"&importpackid=+"
								+importPackageId+"&amount=+"+amount+"&printPartNumber=+"+partNumber+"'></a>");
						$a.printPage();
						$a.trigger("click");
						
						
							}
				  }   ]
		});
		
		$("#exact").change(function(){
			if($("#exact").prop("checked")){
				$("#check").val("eq");
			}else if(!$("#exact").prop("checked")){
				$("#check").val("cn");
			}
	});

		grid = PJ.grid("list", {
			rowNum: 20,
			autowidth:true,
			width : PJ.getCenterWidth(),
			autoSrcoll:true,
			shrinkToFit:false,
			autowidth:true,
			gridComplete:function(){
				var obj = $("#list").jqGrid("getRowData");
				if(obj.length!="0"){
				var total = obj[0].total;
				/* alert(obj[total-1].totalBasePrice); */
				/* jQuery(obj).each(function(){ */
					  $("#price").val(obj[total-1].totalBasePrice);
					 $("#amount").val(obj[total-1].totalAmount); 
				 /*  }); */
			}else{
				 $("#price").val(0);
				 $("#amount").val(0); 
			}
			},
			height : PJ.getCenterHeight()-122,
			sortname : "storage_date",
			colNames : ["","importPackageId","supplierOrderElementId","客户","供应商","件号", "描述","单位","数量","入库单号", "入库日期","出库单号","出库日期","人民币价格","位置","订单号","运输单号","物流方式","","",""],
			colModel : [ PJ.grid_column("id", {sortable:true,hidden:true}),
			             PJ.grid_column("importPackageId", {hidden:true}),
				           PJ.grid_column("supplierOrderElementId", {hidden:true}),
			            PJ.grid_column("clientCode", {sortable:true,width:80}),
			            PJ.grid_column("supplierCode", {sortable:true,width:80}),
			            PJ.grid_column("partNumber", {sortable:true}),
			            PJ.grid_column("description", {sortable:true,align:'left'}),
			            PJ.grid_column("unit", {sortable:true,width:80,align:'left'}),
			            PJ.grid_column("amount", {sortable:true,width:80,align:'left'}),
			            PJ.grid_column("importNumber", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("importDate", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("exportNumber", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("exportDate", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("basePrice", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("location", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("orderNumber", {sortable:true,width:220,align:'left'}),
						PJ.grid_column("logisticsNo", {sortable:true,width:180,align:'left'}),
						PJ.grid_column("logisticsWayValue", {sortable:true,width:180,align:'left'}),
						PJ.grid_column("totalBasePrice", {sortable:true,width:80,align:'left',hidden:true}),
						PJ.grid_column("totalAmount", {sortable:true,width:180,align:'left',hidden:true}),
						PJ.grid_column("total", {sortable:true,width:180,align:'left',hidden:true}),
						],
						
		});
		
		//搜索条件是日期类型的加入日期控件
		PJ.datepickerAll();
		
		//右上角的帮助按扭float:right
		$("#toolbar > div[toolbarid='help']").addClass("fr");
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/importpackage/storageflowlistdate?part_number='+$("#partNumber").val()+'&check='+$("#check").val());
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/importpackage/storageflowlistdate?part_number='+$("#partNumber").val()+'&check='+$("#check").val());
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
				grid.setGridHeight(PJ.getCenterHeight()-262);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		$("#test4_dropdown").hide();
		
		 $("#partNumber").blur(function(){
				var text = $("#partNumber").val();
				$("#partNumber").val(trim(text));
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
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
</head>

<body style="padding:3px">
	<div id="layout1">
		<div position="center" title="">
		库存数量：<input type="text" name="amount" id="amount" style="border:0px;background-color:transparent;" readonly="readonly" value=""/>
		库存金额:<input type="text" name="price" id="price" style="border:0px;background-color:transparent;" readonly="readonly"/>
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 80px;display: none;">
				<form id="searchForm">
					<div class="search-box">
							<form:row columnNum="5">
							
							<form:column>
							      <form:left>件号</form:left>
							   	<form:right><p>
							            	<input  type="text" style="width:150px" prefix="" id="partNumber" name="partNumber" class="text" value=""/>
							           </p>
								</form:right>
							</form:column>
							<form:column>
							<form:left>精确查询</form:left>
							<form:right><input type="checkbox" name="exact" id="exact"/></form:right>
							<input type="text" name="check" id="check" class="hide"/>
						</form:column>
							<form:column>
							      <form:left>客户</form:left>
							   	<form:right><p><select style="width:150px" id="client_code" name="client_code" alias="client_code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							<form:column>
							      <form:left>供应商</form:left>
							   	<form:right><p><select style="width:150px" id="supplier_code" name="supplier_code" alias="supplier_id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							
							<form:column>
							      <form:left>位置</form:left>
							   	<form:right><p>
							            	<input id="searchForm2" type="text" style="width:150px" prefix="" name="location" class="text" oper="eq" alias="location" value=""/>
							           </p>
								</form:right>
							</form:column>
							<form:column>
							      <form:left>帐型</form:left>
							   	<form:right><p>
							            	<input id="searchForm3" type="text" style="width:150px" prefix="" name="storageType" class="text" oper="eq" alias="storage_type" value=""/>
							           </p>
								</form:right>
							</form:column>
							<form:column>
							          <form:left>操作日期：</form:left>
							  <form:right><p><input id="storageDateStart" style="width:150px" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'storageDateStart\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="storageDate" alias="storage_date" oper="gt"/> 
							 </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left> 
							<form:right><p>
							<input id="storageDateEnd" style="width:150px" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'storageDateEnd\')}',dateFmt:'yyyy-MM-dd'})" name="storageDate" alias="storage_date" oper="lt"/> 
							</p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							
							<form:column >
					<form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>
			<table id="list"></table>
			<div id="pager1"></div>
		
		</div>
	</div>
</body>
</html>
