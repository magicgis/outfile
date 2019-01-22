<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商订单明细列表</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>

<script type="text/javascript">
 	//-- Set Attribute
 	
	var layout, grid;
	$(function() {
		layout = $("#layout_main").ligerLayout();

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ 
			]
		});
		
		$("#exact").change(function(){
			if($("#exact").prop("checked")){
				$("#searchForm2").attr("oper","eq");
			}else if(!$("#exact").prop("checked")){
				$("#searchForm2").attr("oper","cn");
			}
	});
		
		grid = PJ.grid("list", {
			rowNum: 20,
			url: "<%=path%>/importpackage/supplierorderelementdate?Id="+"${supplierId}",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 170,
			autoSrcoll:true,
			shrinkToFit:false,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var clientOrderElementId = ret["clientOrderElementId"];
				var supplierOrderImportAmount=ret["supplierOrderImportAmount"];
				var clientOrderImportAmount=ret["clientOrderImportAmount"];
				//$("#searchForm1").val(ret["clientOrderAmount"]);
				 //供应商订单数量
			 	$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/clientquote/supplierOrderAmount?clientOrderElementId='+clientOrderElementId,
					success:function(result){
						var obj = eval(result.message)[0];
						if(result.success){
							
								var orderAmount =obj[0].orderAmount;
								var ipAmount=obj[0].importAmount;
								if(""!=supplierOrderImportAmount&&""!=clientOrderImportAmount){
									if(parseFloat(supplierOrderImportAmount)>parseFloat(clientOrderImportAmount)){
										ipAmount=supplierOrderImportAmount;
									}
								}
								$("#orderAmount").val(orderAmount);
								$("#surplusAmount").val(orderAmount-ipAmount);
								$("#ipAmount").val(ipAmount);
						}else{
								PJ.showWarn(result.msg);
						}
					}
				}); 
			},
			sortname : "",
			colNames : [ "key","id","clientOrderELementId","elementid","clientCode","客户id","conid","cerid","供应商订单号", "订单号", "件号","订单件号", "描述", "订单描述","退税","周期", "截止日期","单位",
			             "客户订单数量","供应商订单数量", "入库数量","客户订单入库数量","供应商订单入库数量","是否主订单","主订单","单价","总价","更新时间","入库","退货","在途库存状态","shelf life"],
			colModel : [PJ.grid_column("key", {sortable:true,width:150,key:true,hidden:true}), 
			            PJ.grid_column("id", {sortable:true,width:150,hidden:true}),
			            PJ.grid_column("clientOrderElementId", {sortable:true,width:150,hidden:true}),
			            PJ.grid_column("elementId", {sortable:true,width:150,hidden:true}),
			            PJ.grid_column("clientCode", {sortable:true,width:150,hidden:true}),
			            PJ.grid_column("conditionId", {sortable:true,width:150,hidden:true}),
			            PJ.grid_column("certificationId", {sortable:true,width:150,hidden:true}),
			            PJ.grid_column("clientId", {sortable:true,width:150,hidden:true}),
			            PJ.grid_column("supplierOrderNumber", {sortable:true,width:150,key:true,align:'left'}),
			            PJ.grid_column("clientOrderNumber", {sortable:true,width:160,align:'left'}),
						PJ.grid_column("quotePartNumber", {sortable:true,width:110,align:'left'}),
						PJ.grid_column("inquiryPartNumber", {sortable:true,width:110,align:'left',hidden:true}),
						PJ.grid_column("quoteDescription", {sortable:true,width:180,align:'left'}),
						PJ.grid_column("orderDescription", {sortable:true,width:180,align:'left',hidden:true}),
						PJ.grid_column("taxReturnValue", {sortable:true,width:40,align:'left'}),
						PJ.grid_column("supplierorderleadtime", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("supplierOrderDeadline", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("quoteUnit", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("clientOrderAmount", {sortable:true,width:70,hidden:false}),
						PJ.grid_column("supplierOrderAmount", {sortable:true,width:70,align:'left'}),
						PJ.grid_column("importAmount", {sortable:true,width:70,align:'left'}),
						PJ.grid_column("clientOrderImportAmount", {sortable:true,width:120,align:'left'}),
						PJ.grid_column("supplierOrderImportAmount", {sortable:true,width:130,align:'left'}),
						PJ.grid_column("orderType", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("mainOrder", {sortable:true,width:120,align:'left'}),
						PJ.grid_column("supplierOrderPrice", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("supplierOrderTotalPrice", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("rk", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("th", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("onPassageStatus", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("shelfLife", {})
						]
		});
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid.setGridHeight(PJ.getCenterHeight()-162);
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/importpackage/supplierorderelementdate?Id='+"${supplierId}");
		    }  
		}); 
		
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/importpackage/supplierorderelementdate?Id='+"${supplierId}");
		 });
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 $("#searchForm2").blur(function(){
				var text = $("#searchForm2").val();
				$("#searchForm2").val(trim(text));
			});
		 $("#searchForm1").blur(function(){
				var text = $("#searchForm1").val();
				$("#searchForm1").val(trim(text));
			});
	});
	//获取表单数据
	 function getFormData(){
			var $input = $("#searchForm").find("input,textarea,select");
			var postData = {};
			var ret = PJ.grid_getSelectedData(grid);
			var supplierOrderNumber = ret["supplierOrderNumber"];
			var id = ret["id"];
			var conditionId = ret["conditionId"];
			var certificationId = ret["certificationId"];
			var quotePartNumber = ret["quotePartNumber"];
			var quoteDescription = ret["quoteDescription"];
			var orderDescription=ret["orderDescription"];
			var quoteUnit = ret["quoteUnit"];
			var supplierOrderAmount = ret["supplierOrderAmount"];
			var clientOrderAmount = ret["clientOrderAmount"];
			var importAmount = ret["importAmount"];
			var supplierOrderPrice = ret["supplierOrderPrice"];
			var th = ret["th"];
			var clientId = ret["clientId"];
			var clientOrderNumber = ret["clientOrderNumber"];
			var elementId = ret["elementId"];
			var clientCode=ret["clientCode"];
			var elementId=ret["elementId"];
			var clientOrderElementId=ret["clientOrderElementId"];
			var onPassageStatus=ret["onPassageStatus"];
			var supplierOrderImportAmount=ret["supplierOrderImportAmount"];
			var clientOrderImportAmount=ret["clientOrderImportAmount"];
			var shelfLife=ret["shelfLife"];
			postData.onPassageStatus = onPassageStatus;
			postData.clientOrderElementId=clientOrderElementId;
			postData.clientOrderAmount=clientOrderAmount;
			postData.clientCode=clientCode;
			postData.elementId=elementId;
			postData.clientOrderNumber=clientOrderNumber;
			postData.clientId=clientId;
			postData.th=th;
			postData.id=id;
			postData.conditionId=conditionId;
			postData.certificationId=certificationId;
			postData.quotePartNumber=quotePartNumber;
			postData.quoteDescription=quoteDescription;
			postData.orderDescription=orderDescription;
			postData.quoteUnit=quoteUnit;
			postData.supplierOrderAmount=supplierOrderAmount;
			postData.shelfLife=shelfLife;
			if(""!=supplierOrderImportAmount&&""!=clientOrderImportAmount){
				if(parseFloat(supplierOrderImportAmount)>parseFloat(clientOrderImportAmount)){
					postData.importAmount=supplierOrderImportAmount;
				}else{
					postData.importAmount=importAmount;
				}
			}else{
			postData.importAmount=importAmount;
			}
			postData.countImportAmount=$("#ipAmount").val();
			postData.clientOrderImportAmount=clientOrderImportAmount;
			postData.supplierOrderPrice=supplierOrderPrice;
			postData.supplierOrderNumber=supplierOrderNumber;
			postData.surplusAmount=$("#surplusAmount").val();
			postData.orderAmount=$("#orderAmount").val();
			return postData;
	 }		
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
/* th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
}  */
 
</style>
</head>

<body style="padding:3px">
	<div id="layout_main">
		<div position="center" title="">
		<form id="searchForm">
		<input type="text" class="hide" name="client" value="1">
			<input type="text" class="hide" name="surplusAmount" id="surplusAmount" value="">
			<input type="text" class="hide" name="orderAmount" id="orderAmount" value="">
			<input type="text" class="hide" name="ipAmount" id="ipAmount" value="">
					<div class="search-box">
						<form:row columnNum="4">
						<form:column>
							       <form:left><p>供应商订单号：</p></form:left>
							   	<form:right><p><input id="searchForm1"  type="text" style="width:150px" prefix="" name="so.order_number" class="text" oper="cn" alias="so.order_number" value=""/></p></form:right>
							       	</form:column>
							<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="searchForm2"  type="text" style="width:150px" prefix="" name="sqe.part_number" class="text" oper="cn" alias="sqe.part_number" value=""/></p></form:right>
							       </form:column>
						<form:column>
							<form:left>精确查询</form:left>
							<form:right><input type="checkbox" name="exact" id="exact"/></form:right>
						</form:column>
						<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p>
						</form:column>
					</form:row>
							</div></form>
		<div id="toolbar"></div>
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
