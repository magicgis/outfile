<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>发票明细</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [ {
				id:'add',
				icon : 'add',
				text : '新增',
				click: function(){
					var ret = PJ.grid_getSelectedData(grid);
					var clientInvoiceElementId=ret["id"];
					var id = ret["clientInvoiceId"];
					var clientOrderElementId = ret["clientOrderElementId"];
					var invoiceAmount=ret["invoiceAmount"];
					if(clientInvoiceElementId==""){
					var iframeId="addinvoiceelementiframe";
						PJ.topdialog(iframeId, '发票明细', '<%=path%>/finance/invoice/toAddInvoiceElement?id='+id+'&clientOrderElementId='+clientOrderElementId,
								function(item, dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
												url: '<%=path%>/finance/invoice/addInvoiceElement',
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
															dialog.close();
														} else {
															PJ.error(result.message);
															dialog.close();
														}		
												}
											});
								},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
					}else{
						PJ.warn('当前状态仅能做修改操作');
						return;
					}
				}
		 },
		 {
				id:'add',
				icon : 'add',
				text : '批量新增',
				click: function(){
					var iframeId="batchaddinvoiceelementiframe";
						PJ.topdialog(iframeId, '发票明细', '<%=path%>/finance/invoice/toBatchAddInvoiceElement?clientOrderId='+'${clientInvoice.clientOrderId}'+'&id='+'${clientInvoice.id}'+'&invoiceTerms='+'${clientInvoice.invoiceTerms}',
								function(item, dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
												url: '<%=path%>/finance/invoice/batchAddInvoiceElement',
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
															dialog.close();
														} else {
															PJ.error(result.message);
															dialog.close();
														}		
												}
											});
								},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
					
				}
		 },
					 {
							id:'add',
							icon : 'add',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["clientInvoiceId"];
								var clientInvoiceElementId=ret["id"];
								var clientOrderElementId = ret["clientOrderElementId"];
								var invoiceAmount=ret["invoiceAmount"];
								if(clientInvoiceElementId==""){
									PJ.warn('没有数据不能进行修改');
									return;
								}else{
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '修改明细', '<%=path%>/finance/invoice/toUpdateInvoiceElement?id='+id+'&clientInvoiceElementId='+clientInvoiceElementId+
											'&clientOrderElementId='+clientOrderElementId,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 $.ajax({
															url: '<%=path%>/finance/invoice/updateInvoiceElement',
															data: postData,
															type: "POST",
															loading: "正在处理...",
															success: function(result){
																	if(result.success){
																		PJ.success(result.message);
																		PJ.grid_reload(grid);
																		dialog.close();
																	} else {
																		PJ.error(result.message);
																		dialog.close();
																	}		
															}
														});
											},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
								}
							}
					 }
			         
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/finance/invoice/elementListData?clientOrderId='+'${clientInvoice.clientOrderId}'+'&id='+'${clientInvoice.id}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "inv.invoice_date",
			sortorder : "desc",
			colNames :["clientOrderElementId","clientInvoiceElementId","clientInvoiceId","件号","描述","单位","单价","开票数量","开票比例","备注","更新时间"],
			colModel :[
			           PJ.grid_column("clientOrderElementId", {key:true,hidden:true}),
			           PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("clientInvoiceId", {hidden:true}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:200,align:'left'}),
			           PJ.grid_column("quoteDescription", {sortable:true,width:200,align:'left'}),
			           PJ.grid_column("quoteUnit", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("clientOrderPrice",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("invoiceAmount",{sortable:true,width:80,align:'left',}),
			           PJ.grid_column("invoiceTerms",{sortable:true,width:100,align:'left',
			        	   formatter:function(value){
								if(value){
									return value+"%";
								}
								else{
									return value;
								}
							}}),
			           PJ.grid_column("remark",{sortable:true,width:170,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:180,align:'left'})
			           
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
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/finance/invoice/listData');
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/finance/invoice/listData');
			
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
		
		$("#invoicetNumber").blur(function(){
			var text = $("#invoicetNumber").val();
			$("#invoicetNumber").val(trim(text));
		});
		
		$("#orderNumber").blur(function(){
			var text = $("#orderNumber").val();
			$("#orderNumber").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="发票号：${clientInvoice.invoiceNumber}   订单号：${clientInvoice.orderNumber}    ">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 80px;display: none;">
				<form id="searchForm">
					<div class="search-box">
					
					</div>
				</form>
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>