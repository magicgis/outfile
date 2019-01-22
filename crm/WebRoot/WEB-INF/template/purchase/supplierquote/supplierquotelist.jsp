<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商报价管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
	var layout,layout2;
	var grid,grid2;
		
		
	$(function() {
		
		$("#submit").click(function(){
			var ret = PJ.grid_getSelectedData(grid);
			var Id = ret["id"];
			var url = '<%=path%>/supplierquote/uploadExcel?id='+Id;
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
		            	closeoropen();
		            	PJ.grid_reload(grid2);
		            	var iframeId2 = "checkFrame";
		            	PJ.topdialog(iframeId2, '填写金额', '<%=path%>/market/clientweatherorder/toCheckTotal',
								function(item, dialog){
									var postData=top.window.frames[iframeId2].getFormData();
									var validate = top.window.frames[iframeId2].validate();
									if(validate){
										$.ajax({
											url: '<%=path%>/supplierquote/checkTotalByQuote?id='+Id,
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
							},function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 250, 110, true,"确定");
	            	}
	            	else{
	            		iframeId = 'errorframe'
	                		PJ.topdialog(iframeId, '错误信息', '<%=path%>/supplierquote/toUnknow',
	                				undefined,function(item,dialog){
	    		            			$.ajax({
	    									url: '<%=path%>/supplierquote/deleteData',
	    									type: "POST",
	    									loading: "正在处理...",
	    									success: function(result){
	    									}
	    								});
	                					dialog.close();}, 1000, 500, true);
	            		closeoropen();
	    	           
	            	}
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            	closeoropen();
	            }  
	        });  
		});	
	 	
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 380,
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
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findcid',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#client_code").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findairid',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#air_type_value").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
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
						$("#biz_type_code").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		$("#toolbar").ligerToolBar({
			items : [  {
				icon : 'view',
				text : '明细',
				click : function(){
					mx();
						}
					},
					 {
					icon : 'process',
					text : '修改',
					click : function(){
						var ret = PJ.grid_getSelectedData(grid);
						var clientInquiryQuoteNumber = ret["clientInquiryQuoteNumber"];
						var supplierInquiryQuoteNumber = ret["supplierQuoteQuoteNumber"]; 
						var currencyId = ret["currencyId"];  
						var currencyValue = ret["currencyValue"];  
						var exchangeRate = ret["exchangeRate"]; 
						var quoteDate = ret["quoteDate"]; 
						var updateTimestamp = ret["updateTimestamp"]; 
						var sourceNumber = ret["sourceNumber"]; 
						var id = ret["id"]; 
						var iframeId="supplierquoteFrame";
						PJ.topdialog(iframeId, '采购 - 修改供应商报价', 
					 			'<%=path%>/supplierquote/editsupplierquotedate?clientInquiryQuoteNumber='+clientInquiryQuoteNumber
					    		+'&supplierInquiryQuoteNumber='+supplierInquiryQuoteNumber+'&currencyId='+currencyId+'&quoteDate='+quoteDate
					    		+'&exchangeRate='+exchangeRate+'&updateTimestamp='+updateTimestamp+'&Id='+id+'&currencyValue='+currencyValue+'&sourceNumber='+sourceNumber,
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 $.ajax({
												 url: '<%=path%>/supplierquote/editsupplierquote',
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
						id:'add',
						icon : 'add',
						text : '文件管理',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var clientInquiryId=ret['clientInquiryId'];
							var iframeId="uploadframe";
							PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/supplierquote/partfile?id='+id+'&type='+clientInquiryId,
									undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
						}
				 },
				 {
						id:'add',
						icon : 'add',
						text : '供应商预报价',
						click: function(){
							
							var iframeId="airtypeiframe";
							PJ.topdialog(iframeId, ' 供应商预报价 ', '<%=path%>/supplierquote/weatherQuote',
									function(item, dialog){
								 var postData=top.window.frames[iframeId].getFormData();	 
								 if(postData){
								 $.ajax({
									 url: '<%=path%>/supplierquote/weatherQuoteData',
										data: postData,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													var iframeId="weatherquoteiframe";
													PJ.topdialog(iframeId, ' 预报价 ', '<%=path%>/supplierquote/weatherQuotePage?id='+result.message,
															undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
													dialog.close();
												} else {
													PJ.error(result.message);
													dialog.close();
												}		
										}
									});
								 }
						},function(item,dialog){dialog.close();}, 1000, 500, true,"查找");
						}
				 }, {
						id:'add',
						icon : 'add',
						text : '新增供应商报价',
						click: function(){
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '新增供应商报价', '<%=path%>/supplierquote/supplierList',
										undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
						}
					 }, 
			          {
					id:'search1',
					icon : 'search',
					text : '收起搜索',
					click: function(){
						$("#searchdiv").toggle(function(){
						var display = $("#searchdiv").css("display");
						if(display=="block"){
							$("#toolbar > div[toolbarid='search1'] > span").html("收起搜索");
							grid.setGridHeight(PJ.getCenterHeight()-202);
						}else{
							$("#toolbar > div[toolbarid='search1'] > span").html("展开搜索");
							grid.setGridHeight(PJ.getCenterHeight()-102);
						}
					});
				}
			}   ]
		});
		
	$("#toolbar2").ligerToolBar({
			items :	[ 
			 {
				id:'add',
				icon : 'add',
				text : '新增',
				click: function(){
					var ret = PJ.grid_getSelectedData(grid);
					var Id = ret["id"];
					var supplierInquiryId = ret["supplierInquiryId"];
					var currencyValue = ret["currencyValue"]; 
					var iframeId="ideaIframe5";
					PJ.topdialog(iframeId, '新增供应商报价明细', '<%=path%>/supplierquote/toAddElement?id='+Id+'&supplierInquiryId='+supplierInquiryId,
							function(item,dialog){
								 var postData=top.window.frames[iframeId].getFormData();	 							
								 $.ajax({
										url: '<%=path%>/supplierquote/addElement?id='+Id,
										data: postData,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													PJ.success(result.message);
													dialog.close();
													PJ.grid_reload(grid2);
													var iframeId2 = "checkFrame";
									            	PJ.topdialog(iframeId2, '填写金额', '<%=path%>/market/clientweatherorder/toCheckTotal',
															function(item, dialog){
																var postData=top.window.frames[iframeId2].getFormData();
																var validate = top.window.frames[iframeId2].validate();
																if(validate){
																	$.ajax({
																		url: '<%=path%>/supplierquote/checkTotalByQuote?id='+Id,
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
														},function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 250, 110, true,"确定");
												} else {
													PJ.error(result.message);
													dialog.close();
												}		
										}
									});
							PJ.grid_reload(grid2);}
						   ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
				}
			 },
			 {
				icon : 'process',
				text : '修改',
				click : function(){
					var ret = PJ.grid_getSelectedData(grid2);
					var iframeId = 'supplierquoteelementFrame';
					var clientInquiryQuoteNumber = ret["clientInquiryQuoteNumber"];
				/* 	var quoteAmount = ret["quoteAmount"];
					var quoteUnit = ret["quoteUnit"];
					var price = ret["price"];
					var quoteDescription = ret["quoteDescription"];
					var updateTimestamp = ret["updateTimestamp"];
					var quoteRemark = ret["quoteRemark"];
					var leadTime = ret["leadTime"];
					var conditionId = ret["conditionId"];
					var certificationId = ret["certificationId"]; 
					var supplierQuoteStatusId = ret["supplierQuoteStatusId"]; 
					var conditionValue = ret["conditionValue"];
					var certificationValue = ret["certificationValue"]; 
					var supplierQuoteStatusValue = ret["supplierQuoteStatusValue"];
					var conditionCode = ret["conditionCode"];
					var certificationCode = ret["certificationCode"]; 
					var updateTimestamp = ret["updateTimestamp"];  */
					var supplierQuoteStatusValue = ret["supplierQuoteStatusValue"];
					var conditionCode = ret["conditionCode"];
					var certificationCode = ret["certificationCode"]; 
					var conditionValue = ret["conditionValue"];
					var certificationValue = ret["certificationValue"]; 
					var id = ret["id"]; 
						 	PJ.topdialog(iframeId, '采购 - 修改供应商报价明细','<%=path%>/supplierquote/editsupplierquoteelementdate?id='+id
						 			+'&supplierInquiryQuoteNumber='+'${supplierInquiryQuoteNumber}'+'&clientInquiryQuoteNumber='+clientInquiryQuoteNumber
						 			+'&supplierQuoteStatusValue='+supplierQuoteStatusValue+'&conditionCode='+conditionCode+'&certificationCode='+certificationCode
						 			+'&conditionValue='+conditionValue+'&certificationValue='+certificationValue,
								function(item, dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
											    url: '<%=path%>/supplierquote/editsupplierquoteelement?id='+id,
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
										 onSelect();
								},function(item,dialog){onSelect();dialog.close();}, 1000, 500, true,'修改');
		  			}
				},
				{
					id:'search2',
					icon : 'search',
					text : '展开文件上传',
					click: function(){
						closeoropen();
					}
				},{
					id:'up',
					icon : 'up',
					text : '件号附件上传',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid2);
						var ret2 = PJ.grid_getSelectedData(grid);
						var supplierCode=ret2["supplierCode"];
						$("#supplierCode").val(supplierCode);
						var quotePartNumber=ret['quotePartNumber'];
						var clientInquiryId=ret['clientInquiryId'];
						$("#fjName").val(quotePartNumber);
						var businessKey={};
						businessKey['businessKey'] ='client_quote_view'+'.id.'+clientInquiryId,
						PJ.uploadAttachment(null,businessKey,function(result){
							if(result && result.id){
								PJ.success("上传成功", function(){
									if(parent.afterUpload){
										parent.afterUpload(result);
									}
									PJ.grid_reload(grid2);
								})
							}
						}, {});
					}
				}]
		});

		grid = PJ.grid("list", {
			rowNum: 20,
			autowidth:true,
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:true,
			autowidth:true,
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			pager: "#pager1",
			sortname : "sq.quote_status_id,sq.update_timestamp",
			sortorder : "desc",
			colNames : ["clientInquiryId","供应商询价id", "币种id","id","供应商","询价单号","报价单号","供应商报价单号","报价日期","有效日期",  "币种", "汇率","银行费","提货换单费","附件","状态","操作人","更新时间"],
			colModel : [PJ.grid_column("clientInquiryId", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierInquiryId", {sortable:true,hidden:true}),
			            PJ.grid_column("currencyId", {sortable:true,hidden:true}),
			            PJ.grid_column("id", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierCode", {sortable:true,width:80,align:'left'}),
			            PJ.grid_column("clientInquiryQuoteNumber", {align:'left'}),
			            PJ.grid_column("supplierQuoteQuoteNumber", {width:150,align:'left'}),
			            PJ.grid_column("sourceNumber", {width:150,align:'left'}),
			            PJ.grid_column("quoteDate", {sortable:true,width:150,align:'left'}),
			            PJ.grid_column("validity", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("currencyValue", {width:100,align:'left'}),
						PJ.grid_column("exchangeRate", {width:80,align:'left'}),
						PJ.grid_column("counterFee", {width:80,align:'left',
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}	
						}),
						PJ.grid_column("feeForExchangeBill", {width:80,align:'left',
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}	
						}),
						PJ.grid_column("haveAttachment", {width:40,align:'left',
							formatter: function(value){
								if (value==1) {
									return "有";
								}else {
									return "";
								}
							}	
						}),
						PJ.grid_column("quoteStatusValue", {width:80,align:'left'}),
						PJ.grid_column("userName", {width:80,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:130,align:'left'}),
						],
						
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 10,
			pager: "#pager2",
			width : PJ.getCenterBottomWidth(),
			height : PJ.getCenterBottomHeight(),
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
			},
			onSelectRow:function(rowid,result){
				var ret = PJ.grid_getSelectedData(grid2);
				var conditionCode = ret["conditionCode"];
				var certificationCode= ret["certificationCode"];
				var supplierQuoteStatusValue = ret["supplierQuoteStatusValue"];
				var conditionId = ret["conditionId"];
				var certificationId= ret["certificationId"];
				var supplierQuoteStatusId = ret["supplierQuoteStatusId"];
				var conditionValue = ret["conditionValue"];
				$("#conditionCode").val(conditionCode);
				$("#certificationCode").val(certificationCode);
				$("#supplierQuoteStatusValue").val(supplierQuoteStatusValue);
				var postData = {};
				postData.id = rowid;
				//证书
			 	$.ajax({
					type: "POST",
					data: postData,
					url:'<%=path%>/supplierquote/findcert?certificationId='+certificationId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							cert =certificationId+":"+certificationCode+";"+ result.message;
						}
					}
				}); 
				
			 	//状态
			 	$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/supplierquote/findcond?conditionId='+conditionId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							cond =conditionId+":"+conditionCode+";"+ result.message;
						}
					}
				}); 
			 	
			 	//报价状态
			 	$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/supplierquote/findsqstauts?supplierQuoteStatusId='+supplierQuoteStatusId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							status =supplierQuoteStatusId+":"+supplierQuoteStatusValue+";"+ result.message;
						}
					}
				}); 
				
			},
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/supplierquote/updatesupplierquoteelement',
			sortname : "cie.item",
			sortorder : "asc",
			colNames : [ "clientInquiryId","id","状态id","证书id","状态id","状态value","证书value","状态value",
			             "询价id","序号","CSN", "主件号","件号", "描述", "单位","数量","MOQ", "单价","年内最低报价","周期",
			             "状态", "证书", "报价状态","有效期","HAZMAT FEE","提货换单费","银行费用","杂费","Available Qty","Location","备注","Core Charge","Warranty","SN","TagSrc","TagDate","Trace","更新时间"],
			colModel : [PJ.grid_column("clientInquiryId", {sortable:true,hidden:true}),
			            PJ.grid_column("id", {sortable:true,hidden:true,editable:true,key:true}),
			            PJ.grid_column("conditionId", {sortable:true,hidden:true}),
			            PJ.grid_column("certificationId", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierQuoteStatusId", {sortable:true,hidden:true}),
			            PJ.grid_column("conditionValue", {sortable:true,hidden:true}),
			            PJ.grid_column("certificationValue", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierQuoteStatusValue", {sortable:true,hidden:true}),
			            PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,hidden:true}),
			            PJ.grid_column("item", {sortable:true,width:30,key:true,align:'left'}),
			            PJ.grid_column("csn", {sortable:true,width:30,key:true,align:'left'}),
			            PJ.grid_column("mainPartNumber",{sortable:true,width:120,align:'left'}),
			            PJ.grid_column("quotePartNumber", {sortable:true,width:120,align:'left'}),
						PJ.grid_column("quoteDescription", {sortable:true,width:150,editable:true,align:'left'}),
						PJ.grid_column("quoteUnit", {sortable:true,width:30,editable:true,align:'left'}),
						PJ.grid_column("quoteAmount", {sortable:true,width:40,editable:true,align:'left'}),
						PJ.grid_column("moq", {sortable:true,width:50,editable:true,align:'left'}),
						PJ.grid_column("price", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("latestPrice", {sortable:true,width:90,editable:true,align:'left'}),
						PJ.grid_column("leadTime", {sortable:true,width:40,editable:true,align:'left'}),
						PJ.grid_column("conditionCode", {sortable:true,width:50,editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								return cond;
							}/* "40:FN-全新;41:NE-新品;42:NS-库存品;43:SV-可用件;44:OH-大修件;45:AR-拆机件;46:RE-修理" */}
						,align:'left'}),
						PJ.grid_column("certificationCode", {sortable:true,width:100,editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								return cert;
								
							}
								/* "50:OEM COC-原厂合格证;51:FAA 8130-3-FAA 8130-3;52:EASA Form One-EASA Form One;53:Vendor COC-Vendor COC;54:Other-Other" */}
						,align:'left'}),
						PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:50,editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								return status;
							}
								/* "70:有效;71:失效;72:错误" */}
						,align:'left'}),
						PJ.grid_column("validity", {sortable:true,width:80,align:'left',editable:false,
				        	   editoptions:{ 
				        		   dataInit:function(el){ 
				        		     $(el).click(function(){ 
				        		       WdatePicker(); 
				        		     }); 
				        		   } 
				        	   }}),
				       	PJ.grid_column("quoteHazmatFee", {sortable:true,width:70,editable:true,align:'left',
				       		formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}
				       	}),
				       	PJ.grid_column("quoteFeeForExchangeBill", {sortable:true,width:60,editable:true,align:'left',
				       		formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}	
				       	}),
						PJ.grid_column("quoteBankCost", {sortable:true,width:70,editable:true,align:'left',
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}	
						}),
						PJ.grid_column("quoteOtherFee", {sortable:true,width:70,editable:true,align:'left',
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}	
						}),
				       	PJ.grid_column("availableQty", {sortable:true,width:70,editable:true,align:'left'}),
						PJ.grid_column("location", {sortable:true,width:70,editable:true,align:'left'}),
						PJ.grid_column("quoteRemark", {sortable:true,width:150,editable:true,align:'left'}),
						PJ.grid_column("coreCharge", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("warranty", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("serialNumber", {sortable:true,width:100,editable:true,align:'left'}),
						PJ.grid_column("tagSrc", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("tagDate", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("trace", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:200,align:'left'})
						]
		});
		
		//搜索条件是日期类型的加入日期控件
		PJ.datepickerAll();
		
		//右上角的帮助按扭float:right
		$("#toolbar > div[toolbarid='help']").addClass("fr");
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/supplierquote/supplierquotedate');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/supplierquote/supplierquotedate');
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
		
		$("#test4_dropdown").hide();
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
	});
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var Id = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/supplierquote/querysupplierquoteelement?Id='+Id);
	};
	
	function mx(){
		var ret = PJ.grid_getSelectedData(grid);
		var Id = ret["id"];
		var supplierCode=ret["supplierCode"];
		var supplierInquiryId = ret["supplierInquiryId"];
		 var supplierInquiryQuoteNumber = ret["supplierQuoteQuoteNumber"]; 
		 var clientInquiryQuoteNumber = ret["clientInquiryQuoteNumber"]; 
		 var currencyValue = ret["currencyValue"]; 
		var iframeId = 'clientquoteFrame';
	 	PJ.topdialog(iframeId, '采购 - 供应商报价明细', 
	 			'<%=path%>/supplierquote/supplierquoteelementlist?Id='+Id+
	 					'&supplierInquiryQuoteNumber='+supplierInquiryQuoteNumber+
	 					'&currencyValue='+currencyValue+'&clientInquiryQuoteNumber='+clientInquiryQuoteNumber+
	 					'&supplierInquiryId='+supplierInquiryId+'&supplierCode='+supplierCode,
	 			
	 					undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
	}
	
	function closeoropen(){
		$("#uploadBox").toggle(function(){
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				$("#toolbar2 > div[toolbarid='search2'] > span").html("收起文件上传");
				grid2.setGridHeight(PJ.getCenterHeight()-122);
			}else{
				$("#toolbar2 > div[toolbarid='search2'] > span").html("展开文件上传");
				grid2.setGridHeight(PJ.getCenterHeight()-82);
			}
		});
		}
	
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

<body style="padding:3px">
<input type="text" class="hide" name="conditionCode" id="conditionCode" value="">
<input type="text" class="hide" name="certificationCode" id="certificationCode" value="">
<input type="text" class="hide" name="supplierQuoteStatusValue" id="supplierQuoteStatusValue" value="">
	<div id="layout1">
		<div position="center">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: block;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
							       <form:left><p>询价单号：</p></form:left>
							    <form:right><p><input id="searchForm2" type="text"  prefix="" name="quoteNumber" class="text" oper="cn" alias="ci.quote_number or si.quote_number" value=""/></p></form:right>
							   	</form:column>
							<form:column>
							<form:left>客户：</form:left>
								<form:right><p><select  id="client_code" name="client_code" alias="c.id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							          </p>
								</form:right>
							</form:column>
								<form:column>
							      <form:left>供应商</form:left>
							   <form:right><p><select  id="supplier_code" name="supplier_code" alias="s.id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
						<form:column>
								<form:left>机型</form:left>
							   <form:right><p><select  id="air_type_value" name="air_type_value" alias="at.id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
								<form:column>
								<form:left>商业类型</form:left>
								<form:right><p><select  id="biz_type_code" name="biz_type_code" alias="bt.id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
								</form:row>
							<form:row columnNum="5">	
							<form:column>
								<form:left><p>报价日期：</p></form:left>
								<form:right><p><input id="quotedatestart"  class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'quotedateend\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="quotedatestart" alias="sq.quote_date" oper="gt"/>
								 </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left> 
							<form:right><p><input id="quotedateend"  class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'quotedatestart\')}',dateFmt:'yyyy-MM-dd'})" name="quotedateend" alias="sq.quote_date" oper="lt"/> 
							</p></form:right>
							</form:column>
						<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							
						<form:column ><form:right>
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
		<div position="centerbottom">
		<div id="toolbar2"></div>
			<div id="uploadBox"style="display: none;">
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
			<table id="list2"></table>
			<div id="pager2"></div>
		</div>
	</div>
	<div class="table-box" style="display: none;width:330px" id="uploadDiv" >
	<input type="text" name="supplierCode" id="supplierCode" class="hide" value=""/>
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
