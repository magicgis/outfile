<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>收款管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	
	function validate(){ 
		return validate2({
			nodeName:"",
			form:"#searchForm"
		});
	 } 
	
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("select,input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
		$items.each(function(i){
			var name = $(this).attr("name");
			if(!name)return;
			for(var k in nodes){
				if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
				if(!flag){
	                $(this).addClass("input-error");
					tip = $(this).attr("data-tip");
					return false;
				}
				else $(this).removeClass("input-error");
			}
			 if($("#searchForm1").val()==""||$("#searchForm1").text()=="全部"){
					$("#searchForm1").addClass("input-error");
						tip = $("#searchForm1").attr("data-tip");
						flag=false;
						return false;
				}
			
		});
		return flag;
		};
	
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [ {
				id:'add',
				icon : 'add',
				text : '明细',
				click: function(){
					var ret = PJ.grid_getSelectedData(grid);
					var id = ret["id"];
					var clientOrderId = ret["clientOrderId"];
					var iframeId="invoiceelementiframe";
						PJ.topdialog(iframeId, '发票明细', '<%=path%>/finance/invoice/invoiceElementList?id='+id+'&clientOrderId='+clientOrderId,
								undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
					
				}
		 },
					 {
							id:'add',
							icon : 'add',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '修改发票单', '<%=path%>/finance/invoice/toUpdateInvoice?id='+id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();
													 var isNull=top.window.frames[iframeId].validate();
													 if(isNull){
													 $.ajax({
															url: '<%=path%>/finance/invoice/updateInvoice',
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
													 }
											},function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true,"修改");
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '收款完成',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var invoiceStatusId = ret["invoiceStatusId"];
								var invoiceType = ret["invoiceType"];
								if(invoiceType!="形式发票"){
								if(invoiceStatusId=='进行中'){
								var clientInvoicePrice = ret["clientInvoicePrice"];
								var totalPrice = ret["totalPrice"];
								PJ.confirm('开票金额:'+clientInvoicePrice+" "+'收款金额:'+totalPrice+',是否确认完成', function (yes){
									if(yes){
															 $.ajax({
																	url: '<%=path%>/finance/invoice/updateInvoiceStatus?id='+id,
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
														 
									}
								});
								}else{
									PJ.warn("收款已完成！");
								}
							}else{
								PJ.warn("形式发票没有收款！");
							}
								
							}
					 },
					 {
						    id:'down',
							icon : 'down',
							text : '下载',
							click: function(){
								    var ret = PJ.grid_getSelectedData(grid);
						 		    var id = ret["id"];
						 		    var clientId = ret["clientId"];
									//根据具体业务提供相关的title
									var title = 'excel管理';
									//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
									//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
									var businessKey = 'client_invoice.id.'+id+'.ClientInvoiceExcel.'+clientId;
									PJ.excelDiaglog({
										data:'businessKey='+businessKey,
										title: title,
										add:true,
										remove:true,
										download:true
									});
							}
					 },{
							id:'down',
							icon : 'down',
							text : '批量下载',
							click: function(){
								 
										PJ.showLoading("处理中...");
										var ids = PJ.grid_getMutlSelectedRowkey(grid);
										 for(var i=0, len=ids.length; i<len; i++) {
											  (function(a) {
												setTimeout(function(){
															var id= ids[a];
															var data = grid.jqGrid("getRowData",id);
															var clientId = data.clientId;
															var businessKey = 'client_invoice.id.'+id+'.ClientInvoiceExcel.'+clientId;
															var data='businessKey='+businessKey;
															PJ.ajax({
																url:'<%=path%>/excelfile/generate?businessKey='+data,
																beforeSend : function() {
																	PJ.loading = false;
																},
																complete : function() {
																	PJ.loading = false;
																}
															});
												},i*2000);
											  })(i)
										}
										 setTimeout(function(){
												PJ.hideLoading();
											 var businessKey = 'businessKey='+ids+'.client_invoice.id';
												var iframeId="downIframe";
												PJ.topdialog(iframeId, '客户发票批量下载', '<%=path%>/excelfile/list?data='+businessKey+'&batch='+'yes',
													undefined,function(item,dialog){dialog.close();	PJ.grid_reload(grid);}, 1000, 500, true,"关闭");
															},len*2000+1000);
										
										
							}
					 },
					 {
						    id:'logout',
							icon : 'logout',
							text : '导出',
							click: function(){
								PJ.grid_export("list1");
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '收款管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var clientOrderId = ret["clientOrderId"];
								var invoiceType = ret["invoiceType"];
								var iframeId="ideaIframe3";
								if(invoiceType!="形式发票"){
									PJ.topdialog(iframeId, '收款管理 ', '<%=path%>/sales/clientorder/toIncome?id='+id+'&clientOrderId='+clientOrderId,
											function(item, dialog){
											dialog.close();
											PJ.grid_reload(grid);
											},function(item,dialog){PJ.hideLoading();dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
								}else{
									PJ.warn("形式发票不能新增收款！");
								}
							}
					 }, {
							id:'delete',
							icon : 'delete',
							text : '删除',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								 $.ajax({
										url: '<%=path%>/finance/invoice/deleteInvoice?id='+id,
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
							}
					 },
				<%-- 	 {
							id:'add',
							icon : 'add',
							text : '收款完成',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var invoiceStatusId = ret["invoiceStatusId"];
								var invoiceType = ret["invoiceType"];
								if(invoiceType!="形式发票"){
								if(invoiceStatusId=='进行中'){
								var clientInvoicePrice = ret["clientInvoicePrice"];
								var totalPrice = ret["totalPrice"];
								PJ.confirm('开票金额:'+clientInvoicePrice+" "+'收款金额:'+totalPrice+',是否确认完成', function (yes){
									if(yes){
															 $.ajax({
																	url: '<%=path%>/finance/invoice/updateInvoiceStatus?id='+id,
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
									}
								});
								}else{
									PJ.warn("收款已完成！");
								}
							}else{
								PJ.warn("形式发票没有收款！");
							}
								
							}
					 }, --%>
			         {
							id:'search',
							icon : 'search',
							text : '展开搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid.setGridHeight(PJ.getCenterHeight()-192);
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
			rowNum: 20,
			url:'<%=path%>/finance/invoice/receivablesInvoiceListData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			shrinkToFit:true,
			multiselect:true,
			sortname : "inv.update_timestamp",
			sortorder : "desc",
			colNames :["id","clientOrderId","clientId","客户","发票号","订单号","发票类型","开票日期","开票金额","开票比例","备注","收款总金额","未收金额","收款状态","出库号","出库指令号","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientOrderId", {hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("invoiceNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("invoiceType",{sortable:true,width:80,align:'left',
			        	   formatter:function(value){
								if(value==0){
									return "形式发票";
								}
								else if(value==1){
									return "预付款发票";
								}
								else if(value==2){
									return "发货发票";
								}else if(value==3){
									return "尾款发票";
								}
							}}),
			           PJ.grid_column("invoiceDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("clientInvoicePrice",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("invoiceTerms",{sortable:true,width:80,align:'left',
			        	   formatter:function(value){
								if(value){
									return value+"%";
								}
								else{
									return value;
								}
							}}),
			           PJ.grid_column("remark",{sortable:true,width:170,align:'left'}),
			           PJ.grid_column("totalPrice",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("uncollected",{sortable:true,width:100,align:'left',
			        	   formatter:function(value){
			        		   if(value){
			        			   return value.toFixed(2);
			        		   }else{
			        			   return value;
			        		   }
			        		   
			        	   }}),
			           PJ.grid_column("invoiceStatusId",{sortable:true,width:70,align:'left',
			        	   formatter:function(value){
								if(value=="1"){
									return "收款完成";
								}
								else{
									return "进行中";
								}
							}}),
					   PJ.grid_column("exportNumber", {sortable:true,width:120,align:'left'}),
					   PJ.grid_column("exportPackageInstructionsNumber",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:140,align:'left'})
			           
			           ]
		});
		
		//客户代码来源
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
		    	PJ.grid_search(grid,'<%=path%>/finance/invoice/receivablesInvoiceListData?exportNumber='+$("#exportNumber").val()+'&exportPackageInstructionsNumber='+$("#exportPackageInstructionsNumber").val());
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 //if(validate()){
			 PJ.grid_search(grid,'<%=path%>/finance/invoice/receivablesInvoiceListData?exportNumber='+$("#exportNumber").val()+'&exportPackageInstructionsNumber='+$("#exportPackageInstructionsNumber").val());
			// }
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
		$("#exportNumber").blur(function(){
			var text = $("#exportNumber").val();
			$("#exportNumber").val(trim(text));
		});
		
		$("#exportPackageInstructionsNumber").blur(function(){
			var text = $("#exportPackageInstructionsNumber").val();
			$("#exportPackageInstructionsNumber").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 80px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>发票号：</p></form:left>
								<form:right><p><input id="invoicetNumber" class="text" type="text" name="invoicetNumber" alias="inv.invoice_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="orderNumber" class="text" type="text" name="orderNumber" alias="co.order_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="c.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>	
							<form:column>
								<form:left><p>开票日期：</p></form:left>
								<form:right><p><input id="receiptStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'receiptEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="receiptStart" alias="inv.invoice_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="receiptEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'receiptStart\')}',dateFmt:'yyyy-MM-dd'})" name="receiptEnd" alias="inv.invoice_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>状态：</form:left>
								<form:right><p>
													<select id="searchForm5" name="invoiceStatusCode" alias="inv.invoice_status_id" oper="eq">
							            			<option value="">全部</option>
							            			<option value="0">进行中</option>
							            			<option value="1">收款已完成</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>类型：</form:left>
								<form:right><p>
													<select id="searchForm6" name="invoiceType" alias="inv.invoice_type" oper="eq">
							            			<option value="">全部</option>
							            			<option value="2">发货发票</option>
							            			<option value="3">尾款发票</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>出库单号：</p></form:left>
								<form:right><p><input id="exportNumber" class="text" type="text" name="exportNumber" /> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>出库指令单号：</p></form:left>
								<form:right><p><input id="exportPackageInstructionsNumber" class="text" type="text" name="exportPackageInstructionsNumber" /> </p></form:right>
							</form:column>
							
							<form:column >
							<form:right>
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
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