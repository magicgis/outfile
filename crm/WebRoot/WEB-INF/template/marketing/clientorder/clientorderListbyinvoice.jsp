<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>销售-客户订单管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout,layout2;
	var grid,grid2;
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 300,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [
					{
							id:'add',
							icon : 'add',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe1";
									PJ.topdialog(iframeId, '销售-修改客户订单', '<%=path%>/sales/clientorder/clientorderEdit?id='+id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();
													 if(postData!=null){
														 $.ajax({
																url: '<%=path%>/sales/clientorder/edit',
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
											},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
								
							}
					},
					{
							id:'view',
							icon : 'view',
							text : '明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="idealframe2";
									PJ.topdialog(iframeId, '销售-客户订单明细管理 ', '<%=path%>/sales/clientorder/element?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
							}
					},
					{
						id:'add',
						icon : 'add',
						text : '新增形式发票',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var iframeId="invoiceIframe";
								PJ.topdialog(iframeId, '新增形式发票', '<%=path%>/finance/invoice/toAddInvoice?id='+id+'&type='+'0',
										function(item, dialog){
												 var postData=top.window.frames[iframeId].getFormData();
												 var isNull=top.window.frames[iframeId].validate();
												 if(isNull&&postData){
													 $.ajax({
															url: '<%=path%>/finance/invoice/addInvoice',
															data: postData,
															type: "POST",
															loading: "正在处理...",
															success: function(result){
																	if(result.success){
																		PJ.success(result.message);
																		PJ.grid_reload(grid2);
																		dialog.close();
																	} else {
																		PJ.error(result.message);
																		dialog.close();
																	}		
															}
													});
												 }
										},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
							
						}
				},
				{
					id:'add',
					icon : 'add',
					text : '新增预付款发票',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"];
						var iframeId="invoiceIframe";
							PJ.topdialog(iframeId, '新增预付款发票', '<%=path%>/finance/invoice/toAddInvoice?id='+id+'&type='+'1',
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();
											 var isNull=top.window.frames[iframeId].validate();
											 if(isNull&&postData){
												 $.ajax({
														url: '<%=path%>/finance/invoice/addInvoice',
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	PJ.success(result.message);
																	PJ.grid_reload(grid2);
																	dialog.close();
																} else {
																	PJ.error(result.message);
																	dialog.close();
																}		
														}
												});
											 }
									},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
						
					}
			},
			{
				id:'add',
				icon : 'add',
				text : '新增发货发票',
				click: function(){
					var ret = PJ.grid_getSelectedData(grid);
					var id = ret["id"];
					var iframeId="invoiceIframe";
					PJ.topdialog(iframeId, '新增发货发票', '<%=path%>/finance/invoice/toAddExportInvoice?id='+id+'&type='+'2',
							function(item, dialog){
									 var postData=top.window.frames[iframeId].getFormData();
									 var isNull=top.window.frames[iframeId].validate();
									 if(isNull&&postData){
										 $.ajax({
												url: '<%=path%>/finance/invoice/addExportInvoice?type='+2,
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid2);
															dialog.close();
														} else {
															PJ.error(result.message);
															dialog.close();
														}		
												}
										});
									 }
							},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
				}
			},
			{
				icon : 'view',
				text : '预利润表',
				click : function(){
					var ret = PJ.grid_getSelectedData(grid);
					var clientquoteid = ret["clientQuoteId"];
					var orderNumber = ret["orderNumber"];
					var id = ret["id"];
					var iframeId = 'clientquoteFrame';
				 	PJ.topdialog(iframeId, '财务 - 预利润表', 
				 			'<%=path%>/clientquote/weatherProfitStatementByOrder?clientquoteid='+clientquoteid+'&orderNumber='+orderNumber+'&id='+id,
								function(item, dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
											    url: '<%=path%>/clientquote/save',
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
								},function(item,dialog){dialog.close();}, 1000, 500, true);
					}
					},{
						icon : 'view',
						text : '利润表',
						click : function(){
							var ret = PJ.grid_getSelectedData(grid);
							var clientquoteid = ret["clientQuoteId"];
							var orderNumber = ret["orderNumber"];
							var id = ret["id"];
							var iframeId = 'clientquoteFrame';
						 	PJ.topdialog(iframeId, '财务 - 利润表', 
						 			'<%=path%>/clientquote/profitStatementByOrder?clientquoteid='+clientquoteid+'&orderNumber='+orderNumber+'&id='+id,
										function(item, dialog){
												 var postData=top.window.frames[iframeId].getFormData();	 							
												 $.ajax({
													    url: '<%=path%>/clientquote/save',
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
										},function(item,dialog){dialog.close();}, 1000, 500, true);
							}
							},
				{
					id:'add',
					icon : 'add',
					text : '新增采购申请单',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"];
						var iframeId="idealframe21";
						var postData = {};
						postData.id = id;
						$.ajax({
							url: '<%=path%>/sales/clientorder/addPurchaseApply',
							data: postData,
							type: "POST",
							loading: "正在处理...",
							success: function(result){
									if(result.success){
										var ret = PJ.grid_getSelectedData(grid);
							 		    var id = ret["id"];
										//根据具体业务提供相关的title
										var title = '采购申请单';
										//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
										//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
										var businessKey = 'purchase_application_form.id.'+id+'.PurchaseApplicationFormExcel';
											PJ.excelDiaglog({
												data:'businessKey='+businessKey,
												title: title,
												add:true,
												remove:true,
												download:true
											});
									} else {
										PJ.error(result.message);
									}		
							}
					});
					}
				},  <%--{
					id:'add',
					icon : 'add',
					text : '发起合同评审',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"];
						var iframeId="orderApprovaliframe";
						var postData = {};
						postData.id = id;
						$.ajax({
							url: '<%=path%>/sales/clientorder/orderReview',
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
									}		
							}
					});
					}
				}, --%>
					 {
						id:'view',
						icon : 'view',
						text : '文件管理',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var iframeId="ideaIframe3";
							PJ.topdialog(iframeId, '文件管理 ', '<%=path%>/sales/clientorder/fileUpload?id='+id,
									function(item, dialog){
									dialog.close();
									},function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
						}
				 	},
				 	{
						id:'down',
						icon : 'down',
						text : '下载',
						click: function(){
							    var ret = PJ.grid_getSelectedData(grid);
					 		    var id = ret["clientId"];
								//根据具体业务提供相关的title
								var title = 'excel管理';
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var businessKey = 'Pending_PO_Status.id.'+id+'.Pending_PO_StatusExcel';
									PJ.excelDiaglog({
										data:'businessKey='+businessKey,
										title: title,
										add:true,
										remove:true,
										download:true
									});
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
		
	$("#toolbar2").ligerToolBar({
			
			items :	[ 
				{
				id:'add',
				icon : 'add',
				text : '明细',
				click: function(){
					var ret = PJ.grid_getSelectedData(grid2);
					var id = ret["id"];
					var clientOrderId = ret["clientOrderId"];
					var iframeId="invoiceelementiframe";
						PJ.topdialog(iframeId, '发票明细', '<%=path%>/finance/invoice/invoiceElementList?id='+id+'&clientOrderId='+clientOrderId,
								undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
					
				}
		 },
					 {
							id:'add',
							icon : 'add',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid2);
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
																		PJ.grid_reload(grid2);
																		dialog.close();
																	} else {
																		PJ.error(result.message);
																		dialog.close();
																	}		
															}
														});
													 }
											},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '收款管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid2);
								var id = ret["id"];
								var clientOrderId = ret["clientOrderId"];
								var invoiceType = ret["invoiceType"];
								var iframeId="ideaIframe3";
								if(invoiceType!="形式发票"){
									PJ.topdialog(iframeId, '收款管理 ', '<%=path%>/sales/clientorder/toIncome?id='+id+'&clientOrderId='+clientOrderId,
											function(item, dialog){
											dialog.close();
											PJ.grid_reload(grid2);
											},function(item,dialog){PJ.hideLoading();dialog.close();PJ.grid_reload(grid2);}, 1000, 500, true);
								}else{
									PJ.warn("形式发票不能新增收款！");
								}
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '收款完成',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid2);
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
																				PJ.grid_reload(grid2);
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
								    var ret = PJ.grid_getSelectedData(grid2);
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
					 }
		 
		 ]
		});
		
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/sales/clientorder/listData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:false,
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			sortname : "co.update_timestamp",
			sortorder : "desc",
			useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
			colNames :["id","报价Id","客户ID","订单号","客户订单号","订单日期","总价","紧急程度","预付","发货","帐期","验货","账期","发货项目比","发货金额比","运费","证书","币种","汇率","状态","创建人","备注","更新时间"],
			colModel :[PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("clientQuoteId", {hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("orderNumber", {sortable:true,width:130,align:'left'  
			           }),
			           PJ.grid_column("sourceOrderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("orderDate", {sortable:true,width:60,sortname : "co.order_number",align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("urgentLevelValue", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("prepayRate", {sortable:true,width:40,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           
			           PJ.grid_column("shipPayRate", {sortable:true,width:40,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           PJ.grid_column("shipPayPeriod",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("receivePayRate", {sortable:true,width:40,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           PJ.grid_column("receivePayPeriod",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("exportAmountPercent",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("exportTotalPercent",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("freight",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("certification",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("currencyValue",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("exchangeRate", {sortable:true,width:30,
			        	   formatter: function(value){
								if (value) {
									return value.substr(0, 3);
								} else {
									return '';							
								}
							},align:'left'}),
			           PJ.grid_column("orderStatusValue",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("userName",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:100,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 10,
			width : PJ.getCenterBottomWidth(),
			height : PJ.getCenterBottomHeight(),
			autoSrcoll:true,
			shrinkToFit:true,
			pager: "#pager2",
			sortname : "inv.invoice_date",
			sortorder : "desc",
			colNames :["id","clientOrderId","clientId","客户","发票号","订单号","发票类型","开票日期","开票金额","开票比例","备注","收款总金额","未收金额","收款状态","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientOrderId", {hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("invoiceNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:140,align:'left'}),
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
			           PJ.grid_column("clientInvoicePrice",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("invoiceTerms",{sortable:true,width:120,align:'left',
			        	   formatter:function(value){
								if(value){
									return value+"%";
								}
								else{
									return value;
								}
							}}),
			           PJ.grid_column("remark",{sortable:true,width:170,align:'left'}),
			           PJ.grid_column("totalPrice",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("uncollected",{sortable:true,width:120,align:'left',
			        	   formatter:function(value){
			        		   if(value){
			        			   return value.toFixed(4);
			        		   }else{
			        			   return value;
			        		   }}}),
			           PJ.grid_column("invoiceStatusId",{sortable:true,width:120,align:'left',
			        	   formatter:function(value){
								if(value=="1"){
									return "收款完成";
								}
								else{
									return "进行中";
								}
							}}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:140,align:'left'})
			           
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var orderNumber = ret["orderNumber"];
			PJ.grid_search(grid2,'<%=path%>/finance/invoice/orderInvoicelistData?orderNumber='+orderNumber);
		};

		
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
						$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
						$("#searchForm4").append($option);
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
		    	PJ.grid_search(grid,'<%=path%>/sales/clientorder/listData');
		    }  
		});
		
		/* $("#list1").on("click", ".xmidData", function(e){ 
			xmid = e.currentTarget.id;
			alert(xmid);
	    }) */;
		
		//搜索
		 $("#searchBtn").click(function(){
			 var partNumber = $("#partNumber").val()
			 PJ.grid_search(grid,'<%=path%>/sales/clientorder/listData?partNumber='+partNumber);
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
			$(window).resize(function() {
				GridResize();
			});
			
			function GridResize() {
				grid.setGridWidth(PJ.getCenterWidth());
				grid2.setGridWidth(PJ.getCenterBottomWidth());
				grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
				grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
			}
			grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
			grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));

		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
	});
	
	/* function cbspztFormatter(cellvalue, options, rowObject){
		var id = rowObject["id"];
		
		switch (cellvalue) {
			case 234:
				return "未发起审批"
				break;
			case 233: 
				return PJ.addTabLink("审批不通过","审批不通过","/workflow/viewFlowInfo?businessKey=CLIENT_ORDER_ELEMENT.ID."+id,"red")
				break;
			case 232: 
				return PJ.addTabLink("审批中","审批中","/workflow/viewFlowInfo?businessKey=CLIENT_ORDER_ELEMENT.ID."+id,"blue")
				break;
			case 235: 
				return PJ.addTabLink("审批完成","审批完成","/workflow/viewFlowInfo?businessKey=CLIENT_ORDER_ELEMENT.ID."+id,"green")
				break;
			default: 
				return cellvalue;
				break; 
			}
	} */
	
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
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="orderNumber" alias="co.order_number or co.source_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" name="partNumber"/> </p></form:right>
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
								<form:left>机型：</form:left>
								<form:right><p>
													<select id="searchForm3" name="airTypeValue" alias="at.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							
							<form:column>
								<form:left>商业类型：</form:left>
								<form:right><p>
													<select id="searchForm4" name="bizTypeCode" alias="bt.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							
						</form:row>
						<form:row columnNum="5">	
							<form:column>
								<form:left>状态：</form:left>
								<form:right><p>
													<select id="searchForm5" name="inquiryStatusCode" alias="sta.code" oper="eq">
							            			<option value="">全部</option>
							            			<option value="CANCEL">作废</option>
							            			<option value="NO_INQUIRY">未询价</option>
							            			<option value="QUOTED">已报</option>
							            			<option value="REFUSE">拒报</option>
							            			<option value="SEND_INQUIRY">已询价</option>
							            			<option value="SUPPLIER_QUOTE">供应商已报</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="co.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="co.order_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							
							<form:column >
							<p style="padding-left:60px;">
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
		<div position="centerbottom" >
				<div id="toolbar2"></div>
					
		<div id="uploadBox" style="display: none;">
	<form id="form" name="form">
			<form:row columnNum="2">
				<form:column width="120">
					<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
					<form:right>
						<p><input type="file" value="" id="file" name="file"/>&nbsp;
						   <input type="button" id="submit" value="上传"/>
						</p>
					</form:right>
				</form:column>
			</form:row>
	            
	 </form>
	</div>
		<table id="list2" style=""></table>
		<div id="pager2"></div>
  </div>
	</div>
</body>
</html>