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
	var kaiguan=1;
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 300,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#submit").click(function(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			var url = '<%=path%>/market/clientweatherorder/uploadExcel?clientOrderId='+id;
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	//var message = decodeURI(data);
	            	//var a = decodeURI(data);
	            	//var da = jQuery.parseJSON(jQuery(data).text());
	            	var da = eval(data)[0];
	            	//var falg = data.flag;
	            	if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid2);
		            	$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid2.setGridHeight(PJ.getCenterHeight()-260);
		            	});
		            	var iframeId2 = "checkFrame";
		            	PJ.topdialog(iframeId2, '填写金额', '<%=path%>/market/clientweatherorder/toCheckTotal',
								function(item, dialog){
									var postData=top.window.frames[iframeId2].getFormData();
									var validate = top.window.frames[iframeId2].validate();
									if(validate){
										$.ajax({
											url: '<%=path%>/market/clientweatherorder/checkTotal?id='+id,
											data: postData,
											type: "POST",
											loading: "正在处理...",
											success: function(result){
													if(result.success){
														PJ.success(result.message);
														dialog.close();
													} else {
														PJ.error(result.message);
														dialog.close();
													}		
											}
										});
									}else{
										PJ.warn("请填写完数据");
									}
							},function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 250, 100, true,"确定");
	            	}else{
	            		$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid2.setGridHeight(PJ.getCenterHeight()-260);
		            	});
	            		if(da.message=='dotmatch'){
							iframeId = 'errorframe'
			            		PJ.topdialog(iframeId, '描述不匹配', '<%=path%>/sales/clientorder/toOrderNotMatch',
			            				function(item, dialog){
											 $.ajax({
													url: '<%=path%>/market/clientweatherorder/insertDate',
													data: '',
													type: "POST",
													loading: "正在处理...",
													success: function(result){
															if(result.success){
																PJ.success(result.message);
																PJ.grid_reload(grid2);
																dialog.close();
															} else {
																PJ.error(result.message);
															}		
													}
											});
										},function(item,dialog){
								            			$.ajax({
															url: '<%=path%>/sales/clientorder/deleteDate',
															type: "POST",
															loading: "正在处理...",
															success: function(result){
															}
														});
						            					dialog.close();
						            	 }, 1000, 500, true);
	            		}else{
	            		iframeId = 'errorframe'
	            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/sales/clientorder/toOrderNotMatch',
	            				undefined,function(item,dialog){
			            			$.ajax({
										url: '<%=path%>/sales/clientorder/checkFinish?id='+id,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
											if(result.success){
												$.ajax({
													url: '<%=path%>/sales/clientorder/deleteBackUp?id='+id,
													type: "POST",
													loading: "正在处理...",
													success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid2);
														}else{
															PJ.error(result.message);
														}
													}
												});
				            					dialog.close();
											}else{
												PJ.error(result.message);
											}
												
										}
									});
			            			}, 1000, 500, true);
	            		}
	            	}
	            	////////////////////////////////////////////////////
	            	/* if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.calculateGridHeight("#layout1"));
		            	});
	            	}
	            	else{
	            		PJ.error(da.message);
	            		$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.calculateGridHeight("#layout1"));
		            	});
	            	} */
	            },
	            error: function (data, status, e) { 
	            	PJ.warn("上传出现异常！");
	            }  
	        });  
			
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
									PJ.topdialog(iframeId, '销售-修改客户订单', '<%=path%>/market/clientweatherorder/clientorderEdit?id='+id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();
													 if(postData!=null){
														 $.ajax({
																url: '<%=path%>/market/clientweatherorder/edit',
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
						id:'add',
						icon : 'add',
						text : '发起合同评审',
						click: function(){
							if(kaiguan){
							kaiguan = 0;
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var clientId=ret["clientId"];
							var iframeId="orderApprovaliframe";
							var postData = {};
							postData.id = id;
								PJ.showLoading("处理中...");
									$.ajax({
										url: '<%=path%>/contractReview/orderReview?clientId='+clientId,
										data: postData,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													PJ.success(result.message);
													PJ.grid_reload(grid);
												} else {
													PJ.error(result.message);
													PJ.grid_reload(grid);
												}		
												setTimeout(function(){
													kaiguan = 1;
													PJ.hideLoading();
												}, 1000);
										}
								});
									
							}
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
							text : '新增',
							click: function(){
								var iframeId="idealframe12";
								var iframeId2="checkframe";
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
									PJ.topdialog(iframeId, '销售-新增客户预订单明细', '<%=path%>/market/clientweatherorder/addOrder?clientOrderId='+id,
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												 $.ajax({
														url: '<%=path%>/market/clientweatherorder/addOrderElement?clientOrderId='+id,
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	PJ.success(result.message);
																	PJ.grid_reload(grid2);
																	PJ.topdialog(iframeId2, '填写金额', '<%=path%>/market/clientweatherorder/toCheckTotal',
																			function(item, dialog){
																				var postData=top.window.frames[iframeId2].getFormData();
																				var validate = top.window.frames[iframeId2].validate();
																				if(validate){
																					$.ajax({
																						url: '<%=path%>/market/clientweatherorder/checkTotal?id='+id,
																						data: postData,
																						type: "POST",
																						loading: "正在处理...",
																						success: function(result){
																								if(result.success){
																									PJ.success(result.message);
																									dialog.close();
																								} else {
																									PJ.error(result.message);
																									dialog.close();
																								}		
																						}
																					});
																				}else{
																					PJ.warn("请填写完数据");
																				}
																		},function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 250, 100, true,"确定");
																	dialog.close();
																} else {
																	PJ.error(result.message);
																	dialog.close();
																}		
														}
													});
												 PJ.grid_reload(grid2);
											},function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
							}
						},
						{	id:'delete',
							icon : 'delete',
							text : '客户取消订单',
							click: function(){
								 PJ.confirm("确定作废？", function(yes){
									 	/* var ret = PJ.grid_getSelectedData(grid2);
									 	var id = ret["id"]; */
									 	var ids = getSelectData();
										if(yes){
											$.ajax({
												url: '<%=path%>/market/clientweatherorder/cancelOrderElement?ids='+ids,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid2);
														} else {
															PJ.error(result.message);
														}		
												}
											});
										}
								    });
								 
						 	}
						 },
						 {
							id:'search2',
							icon : 'search2',
							text : '上传客户订单明细',
							click: function(){
								$("#uploadBox").toggle(function(){
									var display = $("#uploadBox").css("display");
									if(display=="block"){
										$("#toolbar2 > div[toolbarid='search2'] > span").html("上传客户订单明细");
										grid2.setGridHeight(PJ.getCenterHeight()-300);
									}else{
										$("#toolbar2 > div[toolbarid='search2'] > span").html("上传客户订单明细");
										grid2.setGridHeight(PJ.getCenterHeight()-260);
									}
								});
							}
					     }
		 
		 ]
		});
		
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/market/clientweatherorder/listData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:false,
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			sortname : "cwo.update_timestamp",
			sortorder : "desc",
			useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
			colNames :["id","报价Id","客户ID","订单号","客户订单号","订单日期","紧急程度","预付","发货","帐期","验货","账期","证书","币种","汇率","状态","备注","更新时间"],
			colModel :[PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("clientQuoteId", {hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("orderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("sourceOrderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("orderDate", {sortable:true,width:80,sortname : "co.order_number",align:'left'}),
			           PJ.grid_column("urgentLevelValue", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("prepayRate", {sortable:true,width:60,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           
			           PJ.grid_column("shipPayRate", {sortable:true,width:60,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           PJ.grid_column("shipPayPeriod",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("receivePayRate", {sortable:true,width:60,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           PJ.grid_column("receivePayPeriod",{sortable:true,width:60 ,align:'left'}),
			           PJ.grid_column("certification",{sortable:true,width:120 ,align:'left'}),
			           PJ.grid_column("currencyValue",{sortable:true,width:60 ,align:'left'}),
			           PJ.grid_column("exchangeRate", {sortable:true,width:50,
			        	   formatter: function(value){
								if (value) {
									return value.substr(0, 4);
								} else {
									return '';							
								}
							},align:'left'}),
			           PJ.grid_column("orderStatusValue",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:220,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:110,align:'left'})
			           ]
		});
		
			grid2 = PJ.grid("list2", {
				rowNum: 20,
				width : PJ.getCenterBottomWidth(),
				height : PJ.getCenterBottomHeight(),
				autoSrcoll:true,
				shrinkToFit:true,
				inLineEdit: true,
				multiselect:true,
				onSelectRow:function(rowid,result){
					var ret = PJ.grid_getSelectedData(grid2);
					var certificationCode= ret["certificationCode"];
					var certificationId= ret["certificationId"];
					var ret = PJ.grid_getSelectedData(grid);
					var orderStatusId = ret["orderStatusId"];
					var orderStatusValue = ret["orderStatusValue"];
					$("#certificationCode").val(certificationCode);
					var postData = {};
					postData.id = rowid;
					//证书
				 	$.ajax({
						type: "POST",
						data: postData,
						url:'<%=path%>/supplierquote/findcert?certificationId='+certificationId+'&type='+"onlineedit",
						success:function(result){
							if(certificationId==""){
								cert = result.message;
								}else{
									cert =certificationId+":"+certificationCode+";"+ result.message;
								}
						}
					}); 
					
					$.ajax({
						type: "POST",
						data: postData,
						url:'<%=path%>/purchase/supplierorder/findorderStatus?orderStatusId='+orderStatusId,
						success:function(result){
							if(result.success){
								os =orderStatusId+":"+orderStatusValue+";"+ result.message;
							}
						}
					}); 
				},
				pager: "#pager2",
				editurl:'<%=path%>/market/clientweatherorder/editElement',
				aftersavefunc:function(rowid,result){
					PJ.grid_reload(grid2);
				},
				sortname : "cwoe.price",
				sortorder : "asc",
				colNames :["id","spId","item","件号","另件号","单位","描述","证书id","证书","状态","当前处理人","数量","单价","佣金","银行费用","总价","备注","周期","截止日期",
				           "更新日期","供应商","单价","供应商","单价"],
				colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
				           PJ.grid_column("spId", {editable:true,hidden:true}),
				           PJ.grid_column("item",{sortable:true,width:40,align:'left'}),
				           PJ.grid_column("inquiryPartNumber",{sortable:true,width:130,align:'left',editable:true}),
				           PJ.grid_column("alterPartNumber",{sortable:true,width:130,align:'left'}),
				           PJ.grid_column("unit",{sortable:true,width:40,align:'left',editable:true}),
				           PJ.grid_column("inquiryDescription",{sortable:true,width:210,align:'left',editable:true}),
				           PJ.grid_column("certificationId",{sortable:true,width:210,align:'left',hidden:true}),
				           PJ.grid_column("certificationCode", {sortable:true,width:120,editable:true,
								edittype:"select",formatter:"",
								editoptions:{value:
									function(){
									return cert;
									
								}
									/* "50:OEM COC-原厂合格证;51:FAA 8130-3-FAA 8130-3;52:EASA Form One-EASA Form One;53:Vendor COC-Vendor COC;54:Other-Other" */}
							,align:'left'}),
						  PJ.grid_column("spzt",{
							sortable:true,
							formatter : spztFormatter
												}),/**/
					       PJ.grid_column("userName",{sortable:true,width:80,editable:true,align:'left'}),							
				           PJ.grid_column("clientOrderAmount",{sortable:true,width:60,editable:true,align:'left'}),
				           PJ.grid_column("clientOrderPrice",{sortable:true,width:80,editable:true,align:'left'}),
				           PJ.grid_column("fixedCost", {width:80,align:'left',editable:true}),
				       	   PJ.grid_column("bankCharges", {width:70,align:'left'}),
				           PJ.grid_column("totalprice",{sortable:true,width:80,align:'left'}),
				           PJ.grid_column("remark",{sortable:true,width:180,align:'left',editable:true}),
				           PJ.grid_column("clientOrderLeadTime",{sortable:true,width:60,editable:true,align:'left'}),
				           PJ.grid_column("clientOrderDeadline",{sortable:true,width:120,editable:true,
				        	   formatter: OrderFormatter,
				        	   editoptions:{ 
				        		   dataInit:function(el){ 
				        		     $(el).click(function(){ 
				        		       WdatePicker(); 
				        		     }); 
				        		   } 
				        	   } 
				           ,align:'left'}),
				           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'}),
				           PJ.grid_column("supplierCode",{sortable:true,width:80,align:'left'}),
				           PJ.grid_column("supplierQuotePrice",{sortable:true,width:80,align:'left'}),
				           PJ.grid_column("realSupplierCode",{sortable:true,width:80,align:'left'}),
				           PJ.grid_column("realSupplierQuotePrice",{sortable:true,width:80,align:'left'}),
			           ]
			});
			 grid2.jqGrid('setGroupHeaders', { 
				 	useColSpanStyle: true, 
				 	groupHeaders:[ 
				 		{startColumnName: 'id', numberOfColumns: 20, titleText: '<div align="center"><span>客户预订单</span></div>'},
				 		{startColumnName: 'supplierCode', numberOfColumns: 2, titleText: '<div align="center"><span>销售预报价</span></div>'},
				 		{startColumnName: 'realSupplierCode', numberOfColumns: 2, titleText: '<div align="center"><span>采购订单</span></div>'},
				 	] 
				});
			
			function OrderFormatter(cellvalue, options, rowObject){
				var amount = rowObject["clientOrderAmount"];
				
				switch (amount) {
					case '':
						return "无订单";
						break;
						
					default: 
						return cellvalue;
						break; 
				}
			}
			
			function CountFormatter(cellvalue, options, rowObject){
				switch (cellvalue) {
				case 0:
					return '0';
					break;
					
				case '':
					return "无订单";
					break;
					
				default: 
					return cellvalue;
					break; 
				}
			}
			
	
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			PJ.grid_search(grid2,'<%=path%>/market/clientweatherorder/elementList?id='+id);
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
		    	PJ.grid_search(grid,'<%=path%>/market/clientweatherorder/listData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/market/clientweatherorder/listData');
			
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
	
	function spztFormatter(cellvalue, options, rowObject){
		var id = rowObject["spId"];
		
		switch (cellvalue) {
			case 234:
				return "未发起审批"
				break;
			case 233: 
				return PJ.addTabLink("审批不通过","审批不通过","/workflow/viewFlowInfo?businessKey=ORDER_APPROVAL.ID."+id,"red")
				break;
			case 232: 
				return PJ.addTabLink("审批中","审批中","/workflow/viewFlowInfo?businessKey=ORDER_APPROVAL.ID."+id,"blue")
				break;
			case 235: 
				return PJ.addTabLink("审批完成","审批完成","/workflow/viewFlowInfo?businessKey=ORDER_APPROVAL.ID."+id,"green")
				break;
			case 231: 
				return "审核取消"
				break;
			default: 
				return cellvalue;
				break; 
			}
	}
	
	function trim(str){
		return $.trim(str);
	}
	
	function getSelectData(){
		var postData = {};
		var ids =  PJ.grid_getMutlSelectedRowkeyNotValidate(grid2);
		if (ids != ""){
			ids = ids.join(",");
			return ids;
		}else{
			return "";
		}
		
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
<style>
	#cb_list2{
		margin:0
	}
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
								<form:right><p><input id="searchForm2" class="text" type="text" name="orderNumber" alias="cwo.order_number or cwo.source_number" oper="cn"/> </p></form:right>
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
						</form:row>
						<form:row columnNum="5">	
							<form:column>
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="cwo.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="cwo.order_date" oper="lt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> </p></form:left>
								<form:right><p> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> </p></form:left>
								<form:right><p> </p></form:right>
							</form:column>
							<form:column>
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
			 	<input type="hidden" name="id" id="id" value="${id}"/>
		   		
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