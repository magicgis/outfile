<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>入库管理</title>

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
		
		
		
		$("#toolbar").ligerToolBar({
			items : [ /*  {
				icon : 'view',
				text : '明细',
				click : function(){
					mx();
						}
					}, */
					 {
					icon : 'process',
					text : '修改',
					click : function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"]; 
						var importNumber = ret["importNumber"]; 
						var importDate = ret["importDate"]; 
						var remark = ret["remark"]; 
						var exchangeRate = ret["exchangeRate"]; 
						var currencyId = ret["currencyId"]; 
						var currencyValue = ret["currencyValue"];
						var updateTimestamp = ret["updateTimestamp"];
						var supplierCode = ret["supplierCode"];
						var iframeId="importpackageFrame";
						PJ.topdialog(iframeId, '财务 - 修改入库单', 
					 			'<%=path%>/importpackage/updateimportpackage?id='+id+'&importNumber='+importNumber
					 					+'&importDate='+importDate+'&remark='+remark+'&exchangeRate='+exchangeRate+'&supplierCode='+supplierCode
					 					+'&currencyId='+currencyId+'&currencyValue='+currencyValue+'&updateTimestamp='+updateTimestamp,
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 $.ajax({
												 url: '<%=path%>/importpackage/updateimportpackagedate',
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
									},function(item,dialog){dialog.close();}, 1000, 500, true,'修改');
			  			}
					},
					
					 {
						icon : 'add',
						text : '新增',
						click : function(){
							var iframeId="importpackageFrame";
							PJ.topdialog(iframeId, '财务 - 新增入库单', 
						 			'<%=path%>/importpackage/addimportpackage',
										function(item, dialog){
												 var postData=top.window.frames[iframeId].getFormData();
												 $.ajax({
													 url: '<%=path%>/importpackage/addimportpackagedate',
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
										},function(item,dialog){dialog.close();}, 1000, 500, true,'新增');
				  			}
						},
						
							{
								icon : 'view',
								text : 'Excel管理',
								click : function(){
									Excel();
										}
									},
									{
										id:'add',
										icon : 'add',
										text : '新增付款',
										click: function(){
											var ret = PJ.grid_getSelectedData(grid);
											var id = ret["id"];
											var supplierId = ret["supplierId"];
											var iframeId="paymentIframe";
											PJ.topdialog(iframeId, '新增收款', '<%=path%>/importpackage/toAddPayment',
													function(item,dialog){
														 var postData=top.window.frames[iframeId].getFormData();
														 postData.importPackageId = id;
														 postData.supplierId = supplierId;
														 $.ajax({
																url: '<%=path%>/importpackage/addPayment',
																data: postData,
																type: "POST",
																loading: "正在处理...",
																success: function(result){
																		if(result.success){
																			PJ.success(result.message);
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
									 {
											id:'view',
											icon : 'view',
											text : '付款管理',
											click: function(){
												var ret = PJ.grid_getSelectedData(grid);
												var supplierId = ret["supplierId"];
												var iframeId="paymentIframe";
												PJ.topdialog(iframeId, '付款管理', '<%=path%>/importpackage/toAddPaymentList?supplierId='+supplierId,
													   undefined,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true);
											}
									 },
									 {
											id:'add',
											icon : 'add',
											text : '入库完成',
											click: function(){
												var ret = PJ.grid_getSelectedData(grid);
												var id = ret["id"];
												var supplierId=ret["supplierId"];
												$.ajax({
													url: '<%=path%>/importpackage/importComplete?id='+id+'&supplierId='+supplierId,
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
														
											}
									  },
										  {
											id:'view',
											icon : 'view',
											text : '异常件附件下载',
											click: function(){
												var ret = PJ.grid_getSelectedData(grid);
												var id = ret["id"];
												var iframeId="uploadframe";
												PJ.topdialog(iframeId, '异常件附件下载 ', '<%=path%>/importpackage/partfile?id='+id+'&type='+'importpackage',
														undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
											}
									 }, 
									 {
											id:'edit',
											icon : 'edit',
											text : '修改库位',
											click: function(){
												var iframeId="changeloactionframe";
												PJ.topdialog(iframeId, '修改库位', '<%=path%>/importpackage/toChangeLocation',
														function(item,dialog){
															var postData=top.window.frames[iframeId].getFormData();
															PJ.ajax({
																url: '<%=path%>/importpackage/saveChangeLoaction',
																type: "POST",
																loading: "正在处理...",
																data: postData,
																success: function(result){
																		if(result.success){
																			PJ.success(result.message);
																		} else {
																			PJ.error(result.message);
																		}		
																}
															});
														},function(item,dialog){dialog.close();}, 1000, 500, true);
											}
									 },
									 {
											id:'edit',
											icon : 'edit',
											text : '批量修改标签',
											click: function(){
												var iframeId="changeloactionframe";
												PJ.topdialog(iframeId, '批量打印标签', '<%=path%>/importpackage/toPrintLotLabel',
														undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
											}
									 },
									 {
											id:'down',
											icon : 'down',
											text : '批量下载',
											click: function(){
													var iframeId="lotsimportiframe";
													PJ.topdialog(iframeId, '选择入库单', '<%=path%>/importpackage/toSearchImport',
														undefined,function(item,dialog){dialog.close();}, 1200, 700, true);
											}
										
									 }, 
									 {
											id:'add',
											icon : 'add',
											text : '文件管理',
											click: function(){
												var ret = PJ.grid_getSelectedData(grid);
												var id = ret["id"];
												var iframeId="uploadframe";
												PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/importpackage/file?id='+id,
														undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
											}
									 },
									 {	 	icon : 'logout',
										 	text : '导出时寿命件excel',
										 	click : function() {
										 		PJ.ajax({
													url: '<%=path%>/importpackage/checkRecord',
													type: "POST",
													loading: "正在处理...",
													success: function(result){
															if(result.success){
																PJ.warn(result.message);
															} else {
																PJ.grid_export("list");
															}		
													}
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
			}   ]
		});
		
		$("#toolbar2").ligerToolBar({
			items : [  {
				icon : 'add',
				text : '新增',
				click : function(){
					var ret = PJ.grid_getSelectedData(grid);
					var id = ret["id"];
					var importNumber = ret["importNumber"];
					var supplierId = ret["supplierId"];
					/* var originalNumber=$("#originalNumber").val();
					var certificationDate=$("#certificationDate").val(); */
					var url = '<%=path%>/importpackage/addimportpackageelement?importNumber='+importNumber+'&id='+id
 					+'&supplierId='+supplierId;
 					var iframeId="importpackageelementFrame";
 					var btnArr=[];
 					
 					var addbtnCancel={text:"新增",onclick:function(item,dialog){
 						 var postData=top.window.frames[iframeId].getFormData();
 						$.ajax({
 							type: "POST",
 							dataType: "json",
 							data:postData,
 							url:'<%=path%>/importpackage/checkShelfLife',
 							success:function(result){
 								if(!result.success){
 									PJ.warn(result.message);
 								}else{
 									var nullAble=top.window.frames[iframeId].validate();
 									var life=top.window.frames[iframeId].validateLife();
 									 if(nullAble){
 										 if(life){
 											$.ajax({
 	 											 url: '<%=path%>/importpackage/addimportpackageelementdate?supplierOrderElementId='+
 	 													 postData.supplierOrderElementId+'&importPackageId='+id+'&soeid='+postData.soeid,
 	 												data: postData,
 	 												type: "POST",
 	 												loading: "正在处理...",
 	 												success: function(result){
 	 													
 	 													 if(!isNaN(result.message)){		 
 	 															if(result.success){		 
 	 																var originalNumber=$("#originalNumber").val();		 
 	 																var certificationDate=$("#certificationDate").val();		 
 	 																var url = '<%=path%>/importpackage/addStock?importNumber='+importNumber+'&elementId='+ postData.elementId		 
 	 																+'&supplierOrderElementId='+postData.supplierOrderElementId+'&clientId='+postData.clientId+'&id='+id+'&message='+result.message;		 
 	 																var iframeId="stockFrame";		 
 	 																var btnArr=[];		 
 	 																			 							 
 	 																var addbtnCancel={text:"新增",onclick:function(item,dialog){		 
 	 																		 var postData=top.window.frames[iframeId].getFormData();
 	 																		 /* var inspectionDate = $("#inspectionDate").val();
 	 																		 var manufactureDate = $("#manufactureDate").val();
 	 																		 var soeid = $("#soeid").val();
 	 																		 var postData = {};
 	 																		 postData.inspectionDate = inspectionDate;
 	 																		 postData.manufactureDate = manufactureDate;
 	 																		 postData.soeid = soeid; */
 	 																		 $.ajax({
 	 																				type: "POST",
 	 																				dataType: "json",
 	 																				data:postData,
 	 																				url:'<%=path%>/importpackage/checkShelfLife',
 	 																				success:function(result){
 	 																					if(!result.success){
 	 																						PJ.warn(result.message);
 	 																					}else{
 	 																						var nullAble=top.window.frames[iframeId].validate();		 
 	 																	 					 if(nullAble){		 
 	 																							$.ajax({		 
 	 																								url: '<%=path%>/importpackage/addimportpackageelementdate?supplierOrderElementId='+		 
 	 																									0+'&importPackageId='+id,		 
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
 	 																					}
 	 																				}
 	 																			});
 	 																				 
 	 																					 					}};		 
 	 																			 var printCancel1={text:"件号条形码打印",onclick:function(item,dialog){
 	 																			 		var postData=top.window.frames[iframeId].getFormData();
 	 																			 		/* var inspectionDate = $("#inspectionDate").val();
 	 																					 var manufactureDate = $("#manufactureDate").val();
 	 																					 var soeid = $("#soeid").val();
 	 																					 var postData = {};
 	 																					 postData.inspectionDate = inspectionDate;
 	 																					 postData.manufactureDate = manufactureDate;
 	 																					 postData.soeid = soeid; */
 	 																					 $.ajax({
 	 																							type: "POST",
 	 																							dataType: "json",
 	 																							data:postData,
 	 																							url:'<%=path%>/importpackage/checkShelfLife',
 	 																							success:function(result){
 	 																								if(!result.success){
 	 																									PJ.warn(result.message);
 	 																								}else{
 	 																									//top.window.frames[iframeId].onfous();	 
 	 																								 	var elementId=postData.elementId;
 	 																							 		var soeid=postData.soeid;
 	 																							 		var printAmount;
 	 																											if (typeof(postData.printAmount) == "undefined") { 
 	 																														printAmount=0; 
 	 																														}else{
 	 																															printAmount=postData.printAmount;
 	 																														}
 	 																													var amount=postData.amount;
 	 																													var inspectionDate=postData.inspectionDate;
 	 																													var manufactureDate=postData.manufactureDate;
 	 																													var printPartNumber=postData.printPartNumber;
 	 																													var printDescription=postData.printDescription;
 	 																													var resume=postData.completeComplianceCertificate;
 	 																													var complianceCertificate=postData.complianceCertificate;
 	 																													var unit=postData.unit;
 	 																													var shelfLife=postData.shelfLife;
 	 																													var location=postData.location;
 	 																													var serialNumber=postData.serialNumber;
 	 																													var sourceNumber=postData.sourceNumber;
 	 																													var $a = $("<a href='partiframes?idate="+inspectionDate+"&id=+"+soeid+"&pamount=+"+printAmount+"&sl=+"+location+"&resume=+"+resume
 	 																															+"&complianceCertificate="+complianceCertificate+"&unit="+unit+"&shelfLife="+shelfLife+"&sourceNumber="+sourceNumber
 	 																															+"&amount=+"+amount+"&ipid=+"+id+"&mdate=+"+manufactureDate+"&sn=+"+encodeURIComponent(serialNumber)+
 	 																															"&pdesc=+"+encodeURIComponent(printDescription)+"&ppart=+"+encodeURIComponent(printPartNumber)+"'></a>");
 	 																													$a.printPage();
 	 																							 						$a.trigger("click");
 	 																							 						if(window.closed){
 	 																														top.window.frames[iframeId].onfous();
 	 																													}
 	 																								}
 	 																							}
 	 																					});
 	 																			 						
 	 																			 				
 	 																			 					}};	 
 	 																					 							 
 	 																				 					var printCancel2={text:"箱子条形码打印",onclick:function(item,dialog){		 
 	 																										 var postData=top.window.frames[iframeId].getFormData();	 		 
 	 																					 						var location=postData.location;		 
 	 																						 						var clientCode=postData.clientCode;		
 	 																						 						var clientOrderElementId=postData.clientOrderElementId;
 	 																										var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"&clientOrderElementId=+"+clientOrderElementId+"'></a>");
 	 																												$a.printPage();		 
 	 																											$a.trigger("click");		 
 	 														 												}};		 
 	 																					 							 
 	 														 							 					var closeCancel={text:"关闭",onclick:function(item,dialog){		 
 	 																				 						PJ.grid_reload(grid2);dialog.close();		 
 	 																				 					}};		 
 	 																				 							 
 	 																				 					btnArr.push(closeCancel);		 
 	 																				 					btnArr.push(printCancel1);		 
 	 																				 					btnArr.push(printCancel2);		 
 	 																					 					btnArr.push(addbtnCancel);		 
 	 																						 							 
 	 																					 					var opts = {		 
 	 																						 							buttons: btnArr,		 
 	 																				 							width: 1000, 		 
 	 																						 							height: 500,		 
 	 																				 							showMax: true		 
 	 																			 					};		 
 	 																				 							 
 	 																				 					PJ.openTopDialog(iframeId, "财务 - 新增入库单明细", url, opts);		 
 	 																									PJ.success("新增成功，跳转新增剩余"+result.message+"件库存");		 
 	 																										PJ.grid_reload(grid2);		 
 	 																									dialog.close();		 
 	 																								} else {		 
 	 																										PJ.error(result.message);		 
 	 																										dialog.close();		 
 	 																										}			 
 	 																							}else{
 	 																if(result.success){		
 	 																	PJ.success("新增成功");		
 	 																	dialog.close();
 	 																}else{
 	 														PJ.error(result.message);
 	 																}
 	 														PJ.grid_reload(grid2);
 	 														 }
 	 												}
 	 											});
 										 }else{
 											 PJ.warn("时寿件必须填上生产日期与过期日期！");
 										 }
 									}
 								}
 							}
 						});
						 
 					}};
 					var printCancel1={text:"件号条形码打印",onclick:function(item,dialog){
 						 var postData=top.window.frames[iframeId].getFormData();
 						/* var inspectionDate = $("#inspectionDate").val();
 						 var manufactureDate = $("#manufactureDate").val();
 						 var soeid = $("#soeid").val();
 						 var postData = {};
 						 postData.inspectionDate = inspectionDate;
 						 postData.manufactureDate = manufactureDate;
 						 postData.soeid = soeid; */
 						 $.ajax({
 								type: "POST",
 								dataType: "json",
 								data:postData,
 								url:'<%=path%>/importpackage/checkShelfLife',
 								success:function(result){
 									if(!result.success){
 										PJ.warn(result.message);
 									}else{
 										//top.window.frames[iframeId].onfous();	 
 				 						var elementId=postData.elementId;
 				 						var soeid=postData.soeid;
 				 						var printAmount;
 										if (typeof(postData.printAmount) == "undefined") { 
 											printAmount=0; 
 											}else{
 												printAmount=postData.printAmount;
 											}
 										var amount=postData.amount;
 										var inspectionDate=postData.inspectionDate;
 										var manufactureDate=postData.manufactureDate;
 										var printPartNumber=postData.printPartNumber;
 										var resume=postData.completeComplianceCertificate;
 										var complianceCertificate=postData.complianceCertificate;
 										printPartNumber=printPartNumber.replace(/'/g,"%27");
 										var printDescription=postData.printDescription;
 										var location=postData.location;
 										var unit=postData.unit;
 										var shelfLife=postData.shelfLife;
 										var serialNumber=postData.serialNumber;
 										var sourceNumber=postData.sourceNumber;
 										var $a = $("<a href='partiframes?idate="+inspectionDate+"&id=+"+soeid+"&pamount=+"+printAmount+"&sl=+"+location+"&resume=+"+resume
 												+"&complianceCertificate="+complianceCertificate+"&unit="+unit+"&shelfLife="+shelfLife+"&sourceNumber="+sourceNumber
 												+"&amount=+"+amount+"&ipid=+"+id+"&mdate=+"+manufactureDate+"&sn=+"+encodeURIComponent(serialNumber)+
 												"&pdesc=+"+encodeURIComponent(printDescription)+"&ppart=+"+encodeURIComponent(printPartNumber)+"'></a>");

 										$a.printPage();
 				 						$a.trigger("click");
 				 						if(window.closed){
 											top.window.frames[iframeId].onfous();
 										}
 									}
 								}
 						});
 				
 					}};
 					
 					var printCancel2={text:"箱子条形码打印",onclick:function(item,dialog){
 						 var postData=top.window.frames[iframeId].getFormData();	
 						
  						var location=postData.location;
  						var clientCode=postData.clientCode;
  						var clientOrderElementId=postData.clientOrderElementId;
						var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"&clientOrderElementId=+"+clientOrderElementId+"'></a>");
 						$a.printPage();
 						$a.trigger("click");
 						if(window.closed){
							top.window.frames[iframeId].onfous();
						}
 						
 					}};
 					
 					var closeCancel={text:"关闭",onclick:function(item,dialog){
 						PJ.grid_reload(grid2);dialog.close();
 					}};
 					
 					btnArr.push(closeCancel);
 					btnArr.push(printCancel1);
 					btnArr.push(printCancel2);
 					btnArr.push(addbtnCancel);
 					
 					var opts = {
 							buttons: btnArr,
 							width: 1000, 
 							height: 500,
 							showMax: true
 					};
 					
 					PJ.openTopDialog(iframeId, "新增入库单明细", url, opts);
					
		  			}
				},
				{
					icon : 'process',
					text : '修改',
					click : function(){
						var ret = PJ.grid_getSelectedData(grid);
						var ret2 = PJ.grid_getSelectedData(grid2);
						var importpackid=ret["id"];
						var id = ret2["id"]; 
						var clientId = ret2["clientId"]; 
						var supplierId = ret["supplierId"]; 
						var clientOrderNumber = ret2["clientOrderNumber"]; 
						var url ='<%=path%>/importpackage/editimportpackageelement?id='+id
			 			+'&supplierId='+supplierId+'&clientId='+clientId+'&clientOrderNumber='+clientOrderNumber;
						var iframeId = 'importpackageelementFrame';
						var btnArr=[];
						var editCancel={text:"修改",onclick:function(item,dialog){
							 var postData=top.window.frames[iframeId].getFormData();	 							
							 var nullAble=top.window.frames[iframeId].validate();
							 var life=top.window.frames[iframeId].validateLife();
								if(nullAble){
									if(life){
										$.ajax({
										    url: '<%=path%>/importpackage/editimportpackageelementdate?importPackageId='+id,
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
									}else{
										PJ.warn("时寿件必须填上生产日期与过期日期！")
									}
								}
						}};
						
						var printCancel1={text:"件号条形码打印",onclick:function(item,dialog){
	 						var postData=top.window.frames[iframeId].getFormData();
	 						/* var inspectionDate = $("#inspectionDate").val();
	 						 var manufactureDate = $("#manufactureDate").val();
	 						 var soeid = $("#soeid").val();
	 						 var postData = {};
	 						 postData.inspectionDate = inspectionDate;
	 						 postData.manufactureDate = manufactureDate;
	 						 postData.soeid = soeid; */
	 						 $.ajax({
	 								type: "POST",
	 								dataType: "json",
	 								data:postData,
	 								url:'<%=path%>/importpackage/checkShelfLife',
	 								success:function(result){
	 									if(!result.success){
	 										PJ.warn(result.message);
	 									}else{
	 										//top.window.frames[iframeId].onfous();	 
	 				 						var elementId=postData.elementId;
	 				 						var soeid=postData.soeid;
	 				 						var printAmount;
	 										if (typeof(postData.printAmount) == "undefined") { 
	 											printAmount=0; 
	 											}else{
	 												printAmount=postData.printAmount;
	 											}
	 										var amount=postData.amount;
	 										var inspectionDate=postData.inspectionDate;
	 										var manufactureDate=postData.manufactureDate;
	 										var printPartNumber=postData.printPartNumber;
	 										var type="update";
	 										var ipeId=postData.id;
	 										var resume=postData.completeComplianceCertificate;
	 										var complianceCertificate=postData.complianceCertificate;
	 										printPartNumber=printPartNumber.replace(/'/g,"%27");
	 										var printDescription=postData.printDescription;
	 										var location=postData.location;
	 										var serialNumber=postData.serialNumber;
	 										var unit=postData.unit;
	 										var shelfLife=postData.shelfLife;
	 										var sourceNumber=postData.sourceNumber;
	 										var $a = $("<a href='partiframes?idate="+inspectionDate+"&id="+soeid+"&pamount="+printAmount+"&sl="+location+"&resume="+resume
	 												+"&complianceCertificate="+complianceCertificate+"&unit="+unit+"&shelfLife="+shelfLife+"&sourceNumber="+sourceNumber
	 												+"&amount="+amount+"&ipid="+importpackid+"&mdate="+manufactureDate+"&sn="+encodeURIComponent(serialNumber)+
	 												"&type="+type+"&ipeId="+ipeId+
	 												"&pdesc="+encodeURIComponent(printDescription)+"&ppart="+encodeURIComponent(printPartNumber)+"'></a>");
	 										$a.printPage();
	 				 						$a.trigger("click");
	 				 						
	 										 var supplierQuoteElementId=ret2["supplierQuoteElementId"];
	 										 <%-- $.ajax({
	 											    url: '<%=path%>/importpackage/storage?supplierQuoteElementId='+supplierQuoteElementId,
	 												data: '',
	 												type: "POST",
	 												loading: "正在处理...",
	 												success: function(result){
	 														if(result.success){
	 															var message=result.message;
	 															var obj = eval(message);
	 															 var importPackageId = obj[0].importPackageId;
	 													 		 var supplierOrderElementId=obj[0].supplierOrderElementId;
	 													 		 var storageAmount=obj[0].storageAmount;
	 													 		 var partNumber=obj[0].partNumber;
	 													 		 var importPackageElementId=obj[0].importPackageElementId;
	 													 		 var unit=postData.unit;
	 													 		 var type="update";
	 															 var $a = $("<a href='<%=path%>/importpackage/partiframes?id="+supplierOrderElementId+"&ipid=+"+importPackageId+"&unit="+unit+
	 																	"&complianceCertificate="+complianceCertificate+"&ipeId=+"+importPackageElementId+"&type=+"+type+"&amount=+"+storageAmount+"&ppart=+"+partNumber+"'></a>");
	 															$a.printPage();
	 															$a.trigger("click");
	 														} 	
	 												}
	 											}); --%>
	 										
	 				 						
	 				 						if(window.closed){
	 											top.window.frames[iframeId].onfous();
	 										}
	 									}
	 								}
	 						});
	 				
	 					}};
						
						var printCancel2={text:"箱子条形码打印",onclick:function(item,dialog){
	 						 var postData=top.window.frames[iframeId].getFormData();	
	 						
	  						var location=postData.location;
	  						var clientCode=postData.clientCode;
	  						var clientOrderElementId=postData.clientOrderElementId;
							var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"&clientOrderElementId=+"+clientOrderElementId+"'></a>");
	 						$a.printPage();
	 						$a.trigger("click");
	 						if(window.closed){
								top.window.frames[iframeId].onfous();
							}
	 						
	 					}};
	 					
	 					var closeCancel={text:"关闭",onclick:function(item,dialog){
	 						PJ.grid_reload(grid2);dialog.close();
	 					}};
	 					
	 					btnArr.push(closeCancel);
	 					btnArr.push(printCancel1);
	 					btnArr.push(printCancel2);
	 					btnArr.push(editCancel);
	 					
	 					var opts = {
	 							buttons: btnArr,
	 							width: 1000, 
	 							height: 500,
	 							showMax: true
	 					};
						
	 					PJ.openTopDialog(iframeId, "修改入库单明细", url, opts);
	 					PJ.grid_reload(grid2);
						dialog.close();
	 					
						<%-- var ret = PJ.grid_getSelectedData(grid);
						var ret2 = PJ.grid_getSelectedData(grid2);
						var id = ret2["id"]; 
						var clientId = ret2["clientId"]; 
						var supplierId = ret["supplierId"]; 
						var clientOrderNumber = ret2["clientOrderNumber"]; 
						var iframeId = 'importpackageelementFrame';
							 	PJ.topdialog(iframeId, '财务 - 修改入库单明细','<%=path%>/importpackage/editimportpackageelement?id='+id
							 			+'&supplierId='+supplierId+'&clientId='+clientId+'&clientOrderNumber='+clientOrderNumber,
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
											 $.ajax({
												    url: '<%=path%>/importpackage/editimportpackageelementdate?importPackageId='+id,
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
									},function(item,dialog){dialog.close();}, 1000, 500, true,'修改');--%>
			  			} 
					} ,
					{
						icon : 'process',
						text : '入库转库存',
						click : function(){
							var ret = PJ.grid_getSelectedData(grid);
							var ret2 = PJ.grid_getSelectedData(grid2);
							var id = ret2["id"]; 
							var supplierId = ret["supplierId"]; 
							var iframeId = 'importpackageelementFrame';
								 	PJ.topdialog(iframeId, '财务 - 修改入库单明细','<%=path%>/importpackage/toalterstoragepage?id='+id
								 			+'&supplierId='+supplierId,
										function(item, dialog){
												 var postData=top.window.frames[iframeId].getFormData();	 							
												 var nullAble=top.window.frames[iframeId].validate();
													if(nullAble){
												 $.ajax({
													    url: '<%=path%>/importpackage/alterstoragea?importPackageId='+id,
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
										},function(item,dialog){dialog.close();}, 1000, 500, true,'修改');
				  			}
						} ,	{
							icon : 'process',
							text : '退货',
							click : function(){
								var ret = PJ.grid_getSelectedData(grid2);
								var id = ret["id"]; 
								var supplierOrderElementId = ret["supplierOrderElementId"]; 
								
								var iframeId = 'returngoodsFrame';
									 	PJ.topdialog(iframeId, '退货','<%=path%>/importpackage/returnGoodsPage?id='+id
									 			+'&supplierId='+'${supplierId}',
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 var nullAble=top.window.frames[iframeId].validate();
														if(nullAble){
													 $.ajax({
														    url: '<%=path%>/importpackage/returnGoods?importpackageelementid='+id+'&supplierOrderElementId='+supplierOrderElementId,
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
											},function(item,dialog){dialog.close();}, 1000, 500, true,'退货');
					  			}
							} ,
				{
					icon : 'view',
					text : '手动生成合格证',
					click : function(){
						Excel2();
							}
						},
						{
							icon : 'view',
							text : '合格证下载',
							click :
							function Excel(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"]; 
									//根据具体业务提供相关的title
									var title = 'excel管理';
									//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
									//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
									var ipid=id
									var businessKey = 'import_package_element.id.'+ipid+'.CertificationExcel'
									PJ.excelDiaglog({
										data:'businessKey='+businessKey,
										title: title,
										add:true,
										remove:true,
										download:true
									});
							}
						},{
							id:'up',
							icon : 'up',
							text : '异常件附件上传',
							click: function(){
								var ret2 = PJ.grid_getSelectedData(grid2);
								var ret = PJ.grid_getSelectedData(grid);
								var importNumber = ret["importNumber"];
								var partNumber=ret2['partNumber'];
								var id=ret2['id'];
								$("#fjName").val(partNumber);
								$("#folderName").val(importNumber);
								var businessKey={};
								businessKey['businessKey'] ='abnormal_part'+'.id.'+id,
								PJ.uploadAttachment(null,businessKey,function(result){
									if(result && result.id){
										PJ.success("上传成功", function(){
											if(parent.afterUpload){
												parent.afterUpload(result);
											}
										})
									}
								}, {});
							}
						},{
							id:'up',
							icon : 'up',
							text : '到货附件上传',
							click: function(){
								$.ajax({
									url:'<%=path%>/importpackage/checkUser',
									type: "POST",
									success: function(result){
										if(result.success){
											var ret = PJ.grid_getSelectedData(grid2);
											var id = ret["id"];
											var iframeId="uploadIdeaIframe";
											PJ.topdialog(iframeId, ' 附件管理 ', '<%=path%>/importpackage/importFile?id='+id,
													undefined,function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
										} else {
											PJ.error(result.message);
										}		
									}
								})
							}
						}
						]
		});

		grid = PJ.grid("list", {
			rowNum: 20,
			autowidth:true,
			url:'<%=path%>/importpackage/importpackagedate',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:true,
			autowidth:true,
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			pager: "#pager1",
			sortname : "ip.update_timestamp",
			sortorder: "desc",
			colNames : ["id","供应商id","币种id", "入库单号","供应商","入库日期","币种", "汇率","物流方式","运输单号","费用币种","入库费","运费","状态","重量","备注","操作人","更新时间"],
			colModel : [PJ.grid_column("id", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierId", {sortable:true,hidden:true}),
			            PJ.grid_column("currencyId", {sortable:true,hidden:true}),
			            PJ.grid_column("importNumber", {sortable:true,align:'left'}),
			            PJ.grid_column("supplierCode", {sortable:true,width:80,align:'left'}),
			            PJ.grid_column("importDate", {sortable:true,width:80,align:'left'}),
			            PJ.grid_column("currencyValue", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("exchangeRate", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("logisticsWayValue", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("logisticsNo", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("feeCurrencyValue", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("importFee", {sortable:true,width:80,align:'left'/* ,
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }	
						 */}),
						PJ.grid_column("freight", {sortable:true,width:80,align:'left'/* ,
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }	
						 */}),
						PJ.grid_column("importStatus", {sortable:true,width:60,align:'left',
							formatter: StatusFormatter	
						}),
						PJ.grid_column("weight", {sortable:true,width:50,align:'left',
							formatter: function(value){
								if(value){
									return value + "kg";
								}
								
						}}),
						PJ.grid_column("remark", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("createUserName", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:180,align:'left'}),
						],
						
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 10,
			url:'<%=path%>/importpackage/importpackageelementdate',
			autowidth:true,
			width : PJ.getCenterBottomWidth(),
			height : PJ.getCenterBottomHeight(),
			pager: "#pager2",
			autoSrcoll:true,
			shrinkToFit:false,
			autowidth:true,
		/* 	gridComplete:function(){
				var obj = $("#list").jqGrid("getRowData");
				jQuery(obj).each(function(){
					$("#originalNumber").val(this.originalNumber);
					$("#certificationDate").val(this.certificationDate);
					
			    });
			}, */
			sortname : "ip.import_date",
			colNames : ["id","supplierOrderElementId","supplierQuoteElementId","importpackageid", "订单序号","件号","描述","状态","证书","证书编号","符合性证明","是否完成符合性证明", "单位","数量","单价"
			            , "位置","重量","退税","溯源号","序列号","批次号","生产日期","检验日期","时寿件","过期日期","剩余寿命", "订单号","客户订单号","询价单号","更新时间","客户id","客户订单号"],
			colModel : [PJ.grid_column("id", {key:true,sortable:true,hidden:true}),
			            PJ.grid_column("supplierOrderElementId", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierQuoteElementId", {sortable:true,hidden:true}),
			            PJ.grid_column("importPackageId", {sortable:true,hidden:true}),
			            PJ.grid_column("item", {sortable:true,width:60}),
			            PJ.grid_column("partNumber", {sortable:true}),
			            PJ.grid_column("description", {sortable:true}),
			            PJ.grid_column("conditionCode", {sortable:true,width:60}),
			            PJ.grid_column("certificationCode", {sortable:true,width:80}),
			            PJ.grid_column("certificationNumber", {sortable:true,width:80}),
			            PJ.grid_column("complianceCertificateValue", {sortable:true,width:80}),
			            PJ.grid_column("completeComplianceCertificateValue", {sortable:true,width:120}),
						PJ.grid_column("unit", {sortable:true,width:50}),
						PJ.grid_column("amount", {sortable:true,width:50}),
						PJ.grid_column("price", {sortable:true,width:70}),
			            PJ.grid_column("location", {sortable:true,width:70}),
			           /*  PJ.grid_column("spzt", {sortable:true,
							formatter : cbspztFormatter}), */
			            PJ.grid_column("boxWeight", {sortable:true,width:60,formatter:function(value){
							if(value){
								return value+"g";
							}
						}}),
						PJ.grid_column("taxReturnValue", {sortable:true,width:40}),
			            PJ.grid_column("originalNumber", {sortable:true}),
			            PJ.grid_column("serialNumber", {sortable:true,width:100}),
			            PJ.grid_column("batchNumber", {sortable:true,width:100}),
			            PJ.grid_column("manufactureDate", {sortable:true,width:80}),
			            PJ.grid_column("inspectionDate", {sortable:true,width:80}),
			            PJ.grid_column("hasLife", {sortable:true,width:60,
			            	formatter:function(value){
								if(value == '1'){
									return "是";
								}else{
									return "否";
								}
							}	
			            }),
			            PJ.grid_column("expireDate", {sortable:true,width:80}),
			            PJ.grid_column("restLife", {sortable:true,width:60,
			            	formatter:function(value){
								if(value){
									return value+"%";
								}
							}
			            }),
						PJ.grid_column("orderNumber", {sortable:true,width:100}),
						PJ.grid_column("sourceOrderNumber", {sortable:true,width:100}),
						PJ.grid_column("quoteNumber", {sortable:true,width:100}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:140}),
						PJ.grid_column("clientId", {sortable:true,width:180,hidden:true}),
						PJ.grid_column("clientOrderNumber", {sortable:true,width:140}),
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
		    	PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var orderNumber = $("#orderNumber").val();
			 if(orderNumber != ""){
				 PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate?orderNumber='+orderNumber);
			 }else{
				 PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate');
			 }
			 
		 });
		 
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		 function StatusFormatter(cellvalue, options, rowObject){
				switch (cellvalue) {
				case 0:
					return '未完成';
					break;
					
				case 1:
					return '完成';
					break;
					
				case 2:
					return '预入库';
					break;
					
				default: 
					return cellvalue;
					break; 
				}
			}
		 
			function cbspztFormatter(cellvalue, options, rowObject){
				var id = rowObject["id"];
				
				switch (cellvalue) {
					case 232: 
						return PJ.addTabLink("审批中","审批中","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_ELEMENT.ID."+id,"blue")
						break;
					case 235: 
						return PJ.addTabLink("审批完成","审批完成","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_ELEMENT.ID."+id,"green")
						break;
					default: 
						return cellvalue;
						break; 
					}
			}
		 
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

		 
	
		$("#test4_dropdown").hide();
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/importpackage/uploadExcel?editType='+ getFormData().toString();
			//批量新增来函案源
			PJ.showLoading("上传中....");
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	var da = eval(data)[0];
	            	if(da.flag==true){
	            		PJ.hideLoading();
		            	PJ.success("新增成功！");
		            	PJ.grid_reload(grid);
		            	PJ.grid_reload(grid2);
	            	}else{
	            		PJ.hideLoading();
	            		PJ.warn(da.message);
	            	}
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            }  
	        });
			
		 });
		
		function getFormData(){
			 var postData = {};
			 postData.data = $("#form").serialize();
			 return postData;
		};
	});
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/importpackage/importpackageelementdate?id='+id);
		
	};
	
	function mx(){
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		var importNumber = ret["importNumber"];
		var supplierId = ret["supplierId"];
		var supplierCode = ret["supplierCode"];
		var iframeId = 'clientquoteFrame';
	 	PJ.topdialog(iframeId, '采购 - 入库单明细管理', 
	 			'<%=path%>/importpackage/importpackageelement?id='+id+'&supplierCode='+supplierCode+'&importNumber='+importNumber+'&supplierId='+supplierId,
	 			
	 			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
	}
	
	function Excel(){
 		var ret = PJ.grid_getSelectedData(grid);
 		var id = ret["id"];
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = 'import_package.id.'+id+'.ImportpackageExcel'
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
			});
	}
	
	function Excel2(){
 	/* 	var ret = PJ.grid_getSelectedData(grid2);
 		var id = ret["id"];
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = 'import_package_element.id.'+id+'.CertificationExcel'
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
			}); */
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		var importNumber = ret["importNumber"];
		var supplierId = ret["supplierId"];
		var supplierCode = ret["supplierCode"];
		var iframeId = 'clientquoteFrame';
	 	PJ.topdialog(iframeId, '生成合格证', 
	 			'<%=path%>/importpackage/partCertification?id='+id+'&supplierCode='+supplierCode+'&importNumber='+importNumber+'&supplierId='+supplierId,
	 			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
	}
	
	function trim(str){
		return $.trim(str);
	}
	
	
</script>
</head>

<body style="padding:3px">
	<div id="layout1">
		<div position="center">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
							<form:row columnNum="5">
							<form:column>
							     <form:left><p>入库单号：</p></form:left>
							     <form:right><p><input  id="searchForm2" type="text" style="width:150px" prefix="" name="ip.import_number" class="text" oper="cn" alias="ip.import_number" value=""/></p></form:right>
							   	</form:column>
							<form:column>
							      <form:left>供应商</form:left>
							   	<form:right><p><select style="width:150px" id="supplier_code" name="supplier_code" alias="s.id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
								<form:column>
							          <form:left>入库日期：</form:left>
							  <form:right><p><input id="quotedatestart" style="width:150px" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'quotedateend\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="quotedatestart" alias="ip.import_date" oper="gt"/> 
							 </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left> 
							<form:right><p>
							<input id="quotedateend" style="width:150px" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'quotedatestart\')}',dateFmt:'yyyy-MM-dd'})" name="quotedateend" alias="ip.import_date" oper="lt"/> 
							</p></form:right>
							</form:column>
							<form:column>
							<form:left><p style="line-height: 30px;">运单号</p></form:left>
							<form:right>
								<p>
									<input type="text" class="text" id="shipNumber" name="shipNumber" alias="ip.logistics_no" oper="cn"/>
								</p>
							</form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column >
							<form:left><p style="line-height: 30px;">新增商品库excel</p></form:left>
							<form:right>
								<p><input type="file" value="" id="file" name="file"/>&nbsp;
								</p>
							</form:right>
							</form:column>
							<form:column >
							<form:left><p style="line-height: 30px;"><input type="button" id="submit" value="上传"/></p></form:left>
							<form:right>
							</form:right>
							</form:column>
							<form:column>
							<form:left><p style="line-height: 30px;">订单号</p></form:left>
							<form:right>
								<p>
									 <input type="text" class="text" id="orderNumber" name="orderNumber"/>
								</p>
							</form:right>
							</form:column>
							<form:column >
							<form:left></form:left>
							<form:right>
							</form:right>
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
				<div position="centerbottom" >
			<div id="toolbar2"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
				
				</form>
			</div>
			<table id="list2"></table>
			<div id="pager2"></div>
		
		</div>
	</div>
	 <div class="hide" id="tmpBox">
    </div>
	<div class="table-box" style="display: none;width:330px" id="uploadDiv" >
	<input type="text" name="folderName" id="folderName" class="hide" value="${importNumber}"/>
   			<form:row>
    			<form:column>
    				<form:left>附件名称：</form:left>
    				<form:right><input type="text" name="fjName" id="fjName" class="input" style="width: 150px"/></form:right>
    			</form:column>
    		</form:row>
    		<form:row>
    			<form:column>
    				<form:left>附件文件：</form:left>
    				<form:right><input type="file" name="file" id="uploadFile"style="width: 150px" /></form:right>
    			</form:column>
    		</form:row>
   		</div>
</body>
</html>
