<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>未收货清单 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />

<script type="text/javascript">
var layout, grid;

$(function(){
	layout = $("#layout1").ligerLayout();
	
	$("#toolbar").ligerToolBar({
		items : [{
				 	id:'add',
					icon : 'add',
					text : '新增付款',
					click: function(){
						var iframeId="addPaymentIframe";
						
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"];
						PJ.topdialog(iframeId, '新增付款单', '<%=path%>/finance/importpackagepayment/toAddPayment?id='+id,
								 function(item,dialog){
							 	 var postData=top.window.frames[iframeId].getFormData();
								 $.ajax({
										url: '<%=path%>/finance/importpackagepayment/savePayment?postData='+postData,
										data: postData,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													PJ.success(result.message);
													PJ.grid_reload(grid);
												} else {
													PJ.error(result.message);
												}		
										}
									});
								 PJ.grid_reload(grid);
								 dialog.close();}													 
								 ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
							}
				 },
				 {	icon : 'logout',
						text : '导出excel',
						click : function() {
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
									grid.setGridHeight(PJ.getCenterHeight()-172);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-72);
								}
							});
						}
				}
		        ]
	});
	
	grid = PJ.grid("list1", {
		rowNum: 1000,
		url:'<%=path%>/finance/importpackagepayment/paymentWarnList',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-82,
		autoSrcoll:true,
		shrinkToFit:true,
		colNames :["id","类型","供应商","订单号","入库单号","订单日期","入库时间","预付","发货后","验收","账期"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("paymentType", {sortable:true,width:40,align:'left'}),
		           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left'}),
		           PJ.grid_column("supplierOrderNumber", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("importPackageNumber", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("orderDate", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("importDate",{sortable:true,width:70,align:'left'}),
		           PJ.grid_column("prepayRate", {sortable:true,width:80,align:'left',
		        	   formatter: function(value){
							if (value) {
								return value+"%";
							} else {
								return '0'+"%";							
							}
						}}),
		           PJ.grid_column("shipPayRate", {sortable:true,width:80,align:'left',
		        	   formatter: function(value){
							if (value) {
								return value+"%";
							} else {
								return '0'+"%";							
							}
						}}),
		           PJ.grid_column("receivePayRate", {sortable:true,width:80,align:'left',
		        	   formatter: function(value){
							if (value) {
								return value+"%";
							} else {
								return '0'+"%";							
							}
						}}),
		           PJ.grid_column("receivePayPeriod", {sortable:true,width:80,align:'left'})
		           ]
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
	    	PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/paymentWarnList?paymentType='+paymentType);
	    }  
	}); 
	
	//搜索
	 $("#searchBtn").click(function(){
		 var paymentType = $("#paymentType").val()
		 PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/paymentWarnList?paymentType='+paymentType);
		
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
			grid.setGridHeight(PJ.getCenterHeight()-182);
		}else{
			grid.setGridHeight(PJ.getCenterHeight()-82);
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

function OrderFormatter(cellvalue, options, rowObject){
	var orderAmount = rowObject["supplierOrderAmount"];
	var importAmount = rowObject["importAmount"];
	var amount = orderAmount.parseInt()-importAmount.parseInt();
	return amount;
}

</script>
</head>

<body>
<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" alias="so.order_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>入库单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" alias="ip.import_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>供应商：</form:left>
								<form:right><p>
													<select id="searchForm5" name="inquiryStatusCode" alias="s. CODE" oper="eq">
													<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>入库日期：</p></form:left>
								<form:right><p><input id="orderStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'orderEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="orderStart" alias="ip.import_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="orderEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="orderEnd" alias="ip.import_date" oper="lt"/> </p></form:right>
							</form:column>
							
						</form:row>
						<form:row columnNum="5">	
							<form:column>
								<form:left><p>付款类型</p></form:left>
								<form:right><p>
										<select id="paymentType" class="text" type="text">
												<option value="0">全部</option>
												<option value="1">预付</option>
												<option value="2">发货付</option>
												<option value="3">验货付</option>
										</select>
								</p></form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
							<p style="padding-left:90px;">
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