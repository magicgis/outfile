<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>出库明细管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	var supplierids,conditions,certifications,currencys;
	$(function() {
		var data =[];
		PJ.datepicker('validity');
		$(document).ready(function(){ 
			$("#search").focus();
		});
		
		layout1 = $("#layout1").ligerLayout({
			centerBottomHeight: 370,
			onEndResize:function(e){
			GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [
						{
							id:'add',
							icon : 'add',
							text : '新增询价',
							click: function(){
								var iframeId="ideaIframe1";
									PJ.topdialog(iframeId, '销售-新增客户询价', '<%=path%>/sales/clientinquiry/addMessage',
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
													PJ.ajax({
														url: '<%=path%>/sales/clientinquiry/add',
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	//PJ.success(result.message);
																	dialog.close();
																	var id = result.message.split(',')[0]
																	var quoteNumber = result.message.split(',')[1]
																	$("#searchForm3").val(trim(quoteNumber))
																	PJ.topdialog(iframeId, '新增询价明细', '<%=path%>/sales/clientinquiry/addelementafteradd?id='+id,
																			function(item, dialog){
																				var postData=top.window.frames[iframeId].getFormData();
																				 PJ.ajax({
																						url: '<%=path%>/sales/clientinquiry/addElement?id='+id,
																						data: postData,
																						type: "POST",
																						loading: "正在处理...",
																						success: function(result){
																								if(result.success){ 
																									PJ.success(result.message);
																									PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistData');
																									dialog.close();
																								} else {
																									PJ.error(result.message);
																								}		
																						}
																					});
																				PJ.grid_reload(grid2);
																				dialog.close();
																			},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
																}else if(result.message==null){
																	dialog.close();
																}
																else if(!result.success){
																	PJ.error(result.message);
																	dialog.close();
																}		
														}
													});
												}else{
													PJ.warn("数据还没有填写完整！");
												}
											},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
							}
						 },
						 {
								id:'add',
								icon : 'add',
								text : '新增客户报价',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid1);
									var id = ret["clientInquiryId"];
									var iframeId="ideaIframe4";
									var zt = ret["inquiryStatusValue"];
									PJ.topdialog(iframeId, '销售-新增客户报价 ', '<%=path%>/sales/clientinquiry/addquote?id='+id,
										 function(item, dialog){
											 var nullAble=top.window.frames[iframeId].validate();
											 if(nullAble){
												 var postData=top.window.frames[iframeId].getFormData();
												 if(postData!=null){
													 PJ.showLoading("新增中....");
													 PJ.ajax({
															url: '<%=path%>/sales/clientinquiry/saveQuote',
															data: postData,
															type: "POST",
															success: function(result){
																	PJ.hideLoading();
																	if(result.success){
																		PJ.success(result.message);
																	} else {
																		PJ.error(result.message);
																	}		
															}
														});
												 }
											 }else{
													PJ.warn("数据还没有填写完整");
											 }
											 dialog.close();
										 },function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
								}
						 },
						 {
								id:'add',
								icon : 'add',
								text : '新增客户询价',
								click: function(){
									var iframeId="addIframe";
									PJ.topdialog(iframeId, '销售-新增客户询价 ', '<%=path%>/sales/clientinquiry/inquiryTable',
										 function(item, dialog){
											 var nullAble=top.window.frames[iframeId].validate();
											 if(nullAble){
												 var postData=top.window.frames[iframeId].getFormData();
												 if(postData!=null){
													 PJ.showLoading("新增中....");
													 PJ.ajax({
															url: '<%=path%>/sales/clientinquiry/addInquiry',
															data: postData,
															type: "POST",
															success: function(result){
																	PJ.hideLoading();
																	if(result.success){
																		PJ.success(result.message.split(",")[0]);
																		$("#searchForm3").val(trim(result.message.split(",")[1]))
																		PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistData');
																		dialog.close();
																	} else {
																		PJ.error(result.message);
																	}		
															}
														});
												 }
											 }else{
													PJ.warn("数据还没有填写完整");
											 }
										 },function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
								}
						 },
						 {
								icon : 'edit',
								text : '修改报价',
								click : function(){
									edit();
								}
						 }, 
						 {
								icon : 'view',
								text : '报价单下载',
								click : function(){
									var ret = PJ.grid_getSelectedData(grid1);
									var client_inquiry_id = ret["clientInquiryId"];
									var clientTemplateType = ret["clientTemplateType"];
									var bizTypeId= ret["bizTypeId"];
									var currencyId= ret["currencyId"];
									var quoteNumber= ret["quoteNumber"];
									var iframeId = 'clientquoteFrame';
									var count = ""
									PJ.ajax({
										url: '<%=path%>/clientquote/checkQuote?clientInquiryId='+client_inquiry_id,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													PJ.topdialog(iframeId, '多份报价单下载', 
												 			'<%=path%>/clientquote/quotesListPage?client_inquiry_id='+client_inquiry_id+'&clientTemplateType='
												 					+clientTemplateType+'&bizTypeId='+bizTypeId+'&currencyId='+result.message,
												 			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
												}else{
													PJ.warn("询价单:"+quoteNumber+"未生成报价单!")
												}
										}
									});
									
								 	
								}
						},
						{
							id:'search',
							icon : 'search',
							text : '收起搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid1.setGridHeight(PJ.getCenterHeight()-242);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid1.setGridHeight(PJ.getCenterHeight()-102);
									}
								});
							}
						}
					]
		});
		$("#toolbar2").ligerToolBar({
			items : [
							{
								id:'search',
								icon : 'search',
								text : '新增报价',
								click: function(){
									$("#uploadBox").toggle(function(){
										var display = $("#uploadBox").css("display");
										if(display=="block"){
											$("#toolbar > div[toolbarid='uploadBox'] > span").html("新增报价");
											grid2.setGridHeight(PJ.getCenterHeight()-222);
										}else{
											$("#toolbar > div[toolbarid='uploadBox'] > span").html("新增报价");
											grid2.setGridHeight(PJ.getCenterHeight()-102);
										}
									});
								}
							},
							{
								id:'down',
								icon : 'down',
								text : '件号附件下载',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid1);
									var clientInquiryId = ret["clientInquiryId"];
									var clientInquiryElementId = ret["id"];
									var iframeId="uploadframe";
									PJ.topdialog(iframeId, ' 件号附件下载 ', '<%=path%>/clientquote/partfileHis?id='+clientInquiryId+'&type='+"marketing"+"&clientInquiryElementId="+clientInquiryElementId,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								}
							}
			        ]
		});
		
		grid1 = PJ.grid("list1", {
			rowNum: 500,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "cie.id",
			sortorder : "desc",
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			editurl:'<%=path%>/sales/clientinquiry/addAimPrice',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid1);
			},
			inLineEdit: true,
			colNames :["row","id","客户id","询价id","序号","供应商","件号","描述","另件号","数量","moq","报价状态","单价","单位","报价证书","交期","询价单号","客户询价单号","询价日期","售前状态","目标价","创建人","备注","状态","类型","CSN","银行费用","佣金","利润率","bizTypeId","clientTemplateType","报价明细ID","报价ID","供应商报价明细ID","currencyId"],
			colModel :[PJ.grid_column("row", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("id", {hidden:true,editable:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("clientInquiryId", {hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("supplierAndPrice", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("alterPartNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("moq", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("quoteCond", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("price", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("quoteCert", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("leadTime", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("sourceNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("inquiryDate", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("elementStatusValue", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("aimPrice", {sortable:true,width:50,align:'left',editable:true}),
			           PJ.grid_column("userName", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("typeCode", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("bankCost", {hidden:true}),
			           PJ.grid_column("fixedCost", {hidden:true}),
			           PJ.grid_column("profitMargin", {hidden:true}),
			           PJ.grid_column("bizTypeId", {hidden:true}),
			           PJ.grid_column("clientTemplateType", {hidden:true}),
			           PJ.grid_column("clientQuoteElementId", {hidden:true}),
			           PJ.grid_column("clientQuoteId", {hidden:true}),
			           PJ.grid_column("supplierQuoteElementId", {hidden:true}),
			           PJ.grid_column("currencyId", {hidden:true})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum:-1,
			url: '',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 240,
			pager: "#pager2",
			onSelectRow:function(rowid,status,e){
				var curRowData = jQuery("#list2").jqGrid('getRowData', rowid);
				if(curRowData.isHisOrder == "1"){
					$("#list2").jqGrid("setSelection", rowid,false);
				}else{
					var ret1 = PJ.grid_getSelectedData(grid1);
					var ret = PJ.grid_getSelectedData(grid2);
					var supplierQuoteStatusValue=ret["supplierQuoteStatusValue"];
					if(supplierQuoteStatusValue=="失效"){
						PJ.error("价格失效不能使用");
					}
					var iframeId = 'clientquoteFrame';
					var compareRate = ret["compareRate"];
					var averagePrice = ret["averagePrice"];
					var lowestPrice = ret["lowestPrice"];
					var profitMargin = ret["profitMargin"];
					
					var quoteAmount = ret["quoteAmount"];
					if (quoteAmount == ""){
						quoteAmount = ret1["amount"]
					}
					$("#amount").val(quoteAmount);
					var quoteRemark = ret["quoteRemark"];
					var quotePrice = ret["quotePrice"];
					var profitMargin = ret["profitMargin"];
					var supplierQuoteElementId = ret["id"];
					var location= ret["location"];
					var freight=ret["freight"];
					var counterFee=ret["counterFee"];
					var hazmatFee=ret["hazmatFee"];
					var feeForExchangeBill = ret["feeForExchangeBill"];
					var otherFee = ret["otherFee"];
					var price=ret["price"];
					var fixedCost=$("#fixedCost").val();
					var bankCharges=$("#bankCharges").val();
					var bankChargesFee = 0;
					var moq = ret["moq"];
					if(moq != ""){
						$("#moq").val(moq);
						if(parseFloat(moq) > parseFloat(quoteAmount)){
							quoteAmount = moq;
						}
					}else{
						$("#moq").val("");
					}
					if(""==bankCharges){
						bankCharges=0;
					}else if(parseFloat(bankCharges)>=1) {
						bankChargesFee = bankCharges
						bankCharges = 0
					}
					if(""==freight){
						freight=0;
					}
					if(""==counterFee){
						counterFee=0;
					}else{
						counterFee = counterFee.replace('$','')
					}
					if(""==feeForExchangeBill){
						feeForExchangeBill=0;
					}else{
						feeForExchangeBill = feeForExchangeBill.replace('$','')
					}
					if(""==hazmatFee){
						hazmatFee=0;
					}else{
						hazmatFee = hazmatFee.replace('$','')
					}
					if(""==otherFee){
						otherFee=0;
					}else{
						otherFee = otherFee.replace('$','')
					}
					var fixedCostFee = 0;
					if(null==fixedCost||""==fixedCost){
						fixedCost=0;
					}else if(parseFloat(fixedCost)>=1) {
						fixedCostFee = fixedCost
						fixedCost = 0
					}
					
					var profitmargin=ret["profitmargin"];
					counterFee = counterFee/quoteAmount
					feeForExchangeBill = feeForExchangeBill/quoteAmount
					otherFee = otherFee/quoteAmount
					profitmargin=1-(1/parseFloat(profitmargin));
					quotePrice=(((parseFloat(price))/(1-parseFloat(profitmargin))+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
					//profitMargin=((1-price/((parseFloat(quotePrice)-parseFloat(hazmatFee)-parseFloat(feeForExchangeBill)-parseFloat(otherFee)-parseFloat(counterFee)-parseFloat(bankChargesFee)-parseFloat(fixedCostFee)-parseFloat(quotePrice)*(parseFloat(fixedCost)+parseFloat(bankCharges)))))*100).toFixed(1)
					profitMargin=((quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
									(quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)

					var relativeRate = ret["relativeRate"];
					$("#relativeRate").val(relativeRate);
					$("#location").val(location);
					$("#remark").val(quoteRemark);
					$("#price").val(quotePrice);
					$("#supplier_quote_price").val(price);
					$("#profitMargin").val(profitMargin);
					$("#compareRate").val(compareRate+'%');
					$("#averagePrice").val(averagePrice);
					$("#lowestPrice").val(lowestPrice);
					$("#supplierQuoteElementId").val(supplierQuoteElementId);
					$("#freight").val(freight);
					$("#counterFee").val(counterFee);
					$("#feeForExchangeBill").val(feeForExchangeBill);
					$("#hazmatFee").val(hazmatFee);
				}
			},
			gridComplete:function(){
				var supplierQuoteElementId=$("#supplierQuoteElementId").val();
				var ids = jQuery("#list2").jqGrid('getDataIDs');
				if(ids){
					for(var i=0; i<ids.length; i++){
						var rowData = jQuery("#list2").jqGrid('getRowData', ids[i]);
						var agent = rowData.isAgentValue;
						if("是" == agent){
							$('#list2 '+'#'+ids[i]).find("td").addClass("GreenBG");
						}
					}
				}
				/* var obj = $("#list2").jqGrid("getRowData");
				jQuery(obj).each(function(){
					if(this.id==supplierQuoteElementId){
						if($("#price").val()==""){
							$("#price").val(this.clientQuotePrice);
						}
						$("#freight").val(this.freight);
						$("#counterFee").val(this.counterFee);
						$("#supplier_quote_price").val(this.price);
						$("#averagePrice").val(this.averagePrice);
					    $("#compareRate").val(this.compareRate+'%');
					    $("#lowestPrice").val(this.lowestPrice);
					}else if(supplierQuoteElementId==""){
						$("#averagePrice").val(this.averagePrice);
					    $("#compareRate").val(this.compareRate+'%');
					    $("#lowestPrice").val(this.lowestPrice);
					}
			    });
				onPriceChange(); */
				
				var obj = $("#list2").jqGrid("getRowData");
                for (var i = 0; i < obj.length; i++) {
                    if (obj[i].isHisOrder == "1") {//如果审核不通过，则背景色置于红色
                        $('#' + obj[i].id).find("td").addClass("SelectBG");
                    }
                }
			},
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/clientquote/updatesupplierquotefreight',
			aftersavefunc:function(rowid,result){
				var responseJson=result.responseJSON;
				if(responseJson.success==true){
					var rowData=grid.getRowData(rowid);
					var freight=rowData.freight;
					var counterFee=rowData.counterFee;
					var price=rowData.price;
					if(""==counterFee){
						counterFee=0;
					}else if(counterFee<1){
						counterFee=counterFee*parseFloat(price);
					}
					if(""==freight){
						freight=0;
					}
					var profitmargin=rowData.profitmargin;
				
					var quotePrice=rowData.quotePrice;
					var quoteAmount=rowData.quoteAmount;
					var fixedCost=$("#fixedCost").val();
					rowData.countprice=(quoteAmount*(parseFloat(price)+parseFloat(counterFee)))+parseFloat(freight);
					
					var bankCharges=$("#bankCharges").val();
					if(""==bankCharges||parseFloat(bankCharges)>1){
						bankCharges=0;
					}
					if(null==fixedCost||""==fixedCost||parseFloat(fixedCost)>1){
						fixedCost=0;
					}
					profitmargin=1-(1/parseFloat(profitmargin));
					rowData.quotePrice=((parseFloat(price)+parseFloat(freight)+parseFloat(counterFee))/(1-parseFloat(profitmargin)-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
					rowData.profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee))/quotePrice-parseFloat(fixedCost)-parseFloat(bankCharges))*100).toFixed(1);
					$("#price").val(((parseFloat(price)+parseFloat(freight)+parseFloat(counterFee))/(1-parseFloat(profitmargin)-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4));
					
					grid.setRowData(rowid,rowData);
					$("#supplier_quote_price").val(price);
					$("#freight").val(freight);
					$("#counterFee").val(counterFee);
					onPriceChange();
				}
			},
			sortname : "sq.quote_date",
			sortorder : "desc",
			sortname : "",
			colNames : [ "供应商报价明细id","客户报价","比较比率","平均价格","最低价格","供应商","附件","件号","描述","数量","状态", "单价","证书","周期","备注","报价日期","录入人","来源", "location","Available Qty","mov per line","mov per order","有效日期","库存","MOQ","单位","竞争对手","状态","Core Charge","HAZMAT FEE","手续费","提货换单费","杂费",
			             "总价"/* "mov", *//* ,"币种" */,"warranty","serialNumber","tagSrc","tagDate","trace","询价单号", "供应商询价单号", "黑名单",
			             "运费", "客户报价日期","客户报价", "订单日期","客户订单价格","利润率百分数","利润率小数","isHisOrder","竞争对手报价","attachId","代理","relativeRate"],
			colModel : [ PJ.grid_column("id", {hidden:true,editable:true}),
			             PJ.grid_column("quotePrice", {hidden:true}),
			             PJ.grid_column("compareRate", {hidden:true}),
			             PJ.grid_column("averagePrice", {hidden:true}),
			             PJ.grid_column("lowestPrice", {hidden:true}),
			             PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left',formatter: VasColorFormatter}),
			        	 PJ.grid_column("hasAttach", {sortable:true,width:30,align:'left',
			        		 formatter: attachFormatter
			        	 }),
			         	 PJ.grid_column("quotePartNumber", {width:100,align:'left'}),
			        	 PJ.grid_column("quoteDescription", {width:120,align:'left'}),
			        	 PJ.grid_column("quoteAmount", {sortable:true,width:40,align:'left'}),
			        	 PJ.grid_column("conditionCode", {sortable:true,width:40,align:'left'}),
			        	 PJ.grid_column("price", {sortable:true,width:40,align:'left'}),
			        	 PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
			        	 PJ.grid_column("leadTime", {sortable:true,width:30,align:'left'}),
			        	 PJ.grid_column("quoteRemark", {width:180,align:'left'}),
			        	 PJ.grid_column("quoteDate", {sortable:true,width:60,align:'left'}),
			        	 PJ.grid_column("loginName", {width:60,align:'left'}),
			        	 PJ.grid_column("source", {width:50,align:'left'}),
			        	 PJ.grid_column("location", {sortable:true,width:60,align:'left'}),
			        	 PJ.grid_column("availableQty", {width:40,align:'left'}),
			        	 PJ.grid_column("movPerLine", {width:80,align:'left'}),
						 PJ.grid_column("movPerOrder", {width:80,align:'left'}),
						 PJ.grid_column("validity", {sortable:true,width:60,align:'left'}),
						 PJ.grid_column("storageAmount", {width:40,align:'left'}),
						 PJ.grid_column("moq", {sortable:true,width:40,align:'left'}),
			        	 PJ.grid_column("quoteUnit", {width:40,align:'left'}),
			             PJ.grid_column("ifCompetitor", {sortable:true,width:60,align:'left',
			            	 formatter: function(value){
									if (value==1||value=="是") {
										return '<span style="color:red">'+"是"+'<span>';
									} else {
										return "";
									}
							 }	 
			             }),
			             PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:40,align:'left'}),
			             PJ.grid_column("coreCharge", {sortable:true,width:60,align:'left'}),
			             PJ.grid_column("hazmatFee", {sortable:true,width:70,align:'left'}),
			             PJ.grid_column("counterFee", {sortable:true,width:40,align:'left'}),
			             PJ.grid_column("feeForExchangeBill", {sortable:true,width:60,align:'left'}),
			             PJ.grid_column("otherFee", {sortable:true,width:60,align:'left'}),
			         	 PJ.grid_column("countprice", {width:60,align:'left',hidden:true,
							formatter:function(value,options,data){
								var supplierCode=data['supplierCode'];
								if(supplierCode=="历史供应商报价"){
									return
								}
								var countprice=data['countprice'];
								var freight=data['freight'];
								var counterFee=data['counterFee'];
								var price=data['price'];
								if(""==freight){
									freight=0;
								}
								if(""==counterFee){
									counterFee=0;
								}else if(counterFee<1){
									counterFee=counterFee*parseFloat(price);
								}
							
								var quoteAmount=data['quoteAmount'];
								countprice= (quoteAmount*(parseFloat(price)+parseFloat(counterFee)))+parseFloat(freight);
									return countprice.toFixed(2);
							}	
						}),
						/* PJ.grid_column("mov", {width:100,align:'left'}), */
						/* PJ.grid_column("movPerOrderCurrencyValue", {width:100,align:'left'}), */
						PJ.grid_column("warranty", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("serialNumber", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("tagSrc", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("tagDate", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("trace", {sortable:true,width:80,align:'left'}),
		             	PJ.grid_column("clientInquiryQuoteNumber", {width:100,align:'left'}),
						PJ.grid_column("supplierInquiryQuoteNumber", {width:90,align:'left'}),
					 	PJ.grid_column("isBlacklist",{sortable:true,width:50,align:'left',
			        	   formatter: function(value){
								if (value==1||value=="是") {
									return '<span style="color:red">'+"是"+'<span>';
								} else if(value==0||value=="否"){
									return "否";							
								}else {
									return "";
								}
							}
			           }),
				
					   PJ.grid_column("freight", {sortable:true,width:60,editable:true,align:'left'}),
					   PJ.grid_column("clientQuoteDate", {sortable:true,width:100,align:'left'}),
					   PJ.grid_column("clientQuotePrice", {sortable:true,width:80,align:'left'}),
					   PJ.grid_column("clientOrderDate", {sortable:true,width:100,align:'left'}),
					   PJ.grid_column("clientOrderPrice", {sortable:true,width:60,align:'left'}),
					   PJ.grid_column("profitMargin", {width:80,hidden:true,align:'left'
						 ,formatter:function(value){
							if(value){
								return value+"%";
							}else{
								return value;
							}
						 } 		
					  }),
					  PJ.grid_column("profitmargin", {sortable:true,width:50,hidden:true,align:'left'}),
					  PJ.grid_column("isHisOrder", {sortable:true,width:50,hidden:true,align:'left'}),
					  PJ.grid_column("competitor", {width:400,align:'left'}),
					  PJ.grid_column("attachId", {sortable:true,width:50,align:'left'}),
					  PJ.grid_column("isAgentValue", {hidden:true}),
					  PJ.grid_column("relativeRate", {hidden:true})
					]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid1);
			var partNumber = ret["partNumber"];
			var id = ret["id"];
			var clientId = ret["clientId"];
			var clientInquiryId = ret["clientInquiryId"];
			var bankCost = ret["bankCost"];
			var fixedCost = ret["fixedCost"];
			var profitMargin = ret["profitMargin"];
			if(bankCost != ""){
				$("#bankCharges").val(bankCost);
			}
			if(fixedCost != ""){
				$("#fixedCost").val(fixedCost);
			}
			if(profitMargin != ""){
				$("#profitMargin").val(profitMargin);
			}
			$("#clientInquiryElementId").val(id);
			$("#clientInquiryId").val(clientInquiryId);
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/clientquote/checkIfHasQuote?id='+id,
				success:function(result){
					if(result.success){
						$("#supplierQuoteElementId").val(result.message);
					}
				}
			});
			//获取历史订单的条数
			<%-- $.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/clientquote/getListCount?partNumber='+partNumber,
				success:function(result){
					if(result.success){
						var rowNum = grid2.jqGrid('getGridParam', 'rowNum');
						var count = result.message;
						var total = parseFloat(rowNum) + parseFloat(count);
						grid2.jqGrid('setGridParam', { rowNum: total })
					}
				}
			}); --%>
			PJ.grid_search(grid2,'<%=path%>/clientquote/quoteDateForHis?clientId='+clientId+'&clientInquiryElementId='+id);
		};
		
		// 绑定键盘按下事件  
		<%-- $(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	var part = $("#searchForm2").val();
				var quoteNumber = $("#searchForm3").val();
				var inquiryStart = $("#inquiryStart").val();
				var inquiryEnd = $("#inquiryEnd").val();
				var manageStatus = $("#manageStatus").val();
				var elementStatus = $("#elementStatus").val();
				var clientCode = $("#searchForm1").val();
				var searchForm4 = $("#searchForm4").val();
				var beginWith = $("#beginWith").val();
				if(elementStatus!=""){
					if(elementStatus == '713'){
						PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistData?manageStatus='+manageStatus);
					}else if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!="" || beginWith!= "" || manageStatus!=""){
						PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistData?manageStatus='+manageStatus+'&beginWith='+beginWith);
					}else{
						PJ.warn("售前状态不能单独作为搜索条件查询！");
					}
				}else{
					if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!="" || beginWith!= "" || manageStatus!=""){
						PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistData?manageStatus='+manageStatus+'&beginWith='+beginWith);
					}else{
						PJ.warn("必须填写一个搜索条件！");
					}
				}
		    }  
		});  --%>
		
		//搜索
		$("#searchBtn").click(function(){
			var part = $("#searchForm2").val();
			var quoteNumber = $("#searchForm3").val();
			var inquiryStart = $("#inquiryStart").val();
			var inquiryEnd = $("#inquiryEnd").val();
			var manageStatus = $("#manageStatus").val();
			var elementStatus = $("#elementStatus").val();
			var clientCode = $("#searchForm1").val();
			var searchForm4 = $("#searchForm4").val();
			var beginWith = $("#beginWith").val();
			var description = $("#description").val();
 			if(elementStatus!=""){
				if(elementStatus == '713'){
					PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistDataForMarketHis?manageStatus='+manageStatus+'&beginWith='+beginWith);
				}else if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!="" || beginWith!= "" || manageStatus!="" || description!=""){
					PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistDataForMarketHis?manageStatus='+manageStatus+'&beginWith='+beginWith);
				}else{
					PJ.warn("售前状态不能单独作为搜索条件查询！");
				}
			}else{
				if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!="" || beginWith!= "" || manageStatus!="" || description!=""){
					PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistDataForMarketHis?manageStatus='+manageStatus+'&beginWith='+beginWith);
				}else{
					PJ.warn("必须填写一个搜索条件！");
				}
			}
		 });
		
		/* $("#amount").blur(function(){
			var amount = $("#amount").val()
			var ret = PJ.grid_getSelectedData(grid2);
			var profitmargin=ret["profitmargin"];
			var quoteAmount = ret["quoteAmount"];
			var quoteRemark = ret["quoteRemark"];
			var quotePrice = ret["quotePrice"];
			var profitMargin = ret["profitMargin"];
			var supplierQuoteElementId = ret["id"];
			var location= ret["location"];
			var freight=ret["freight"];
			var counterFee=ret["counterFee"];
			var hazmatFee=ret["hazmatFee"];
			var feeForExchangeBill = ret["feeForExchangeBill"];
			var price=ret["price"];
			var fixedCost=$("#fixedCost").val();
			if(""==freight){
				freight=0;
			}
			if(""==counterFee||counterFee<1){
				counterFee=0;
			}
			if(""==feeForExchangeBill||feeForExchangeBill<1){
				feeForExchangeBill=0;
			}
			if(""==hazmatFee||hazmatFee<1){
				hazmatFee=0;
			}
			if(null==fixedCost||""==fixedCost||parseFloat(fixedCost)>1){
				fixedCost=0;
			}
			counterFee = counterFee/amount
			feeForExchangeBill = feeForExchangeBill/amount
			hazmatFee = hazmatFee/amount
			profitmargin=1-(1/parseFloat(profitmargin));
			quotePrice=((parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee))/(1-parseFloat(profitmargin))/(1-parseFloat(fixedCost))).toFixed(4);
			profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee))/(quotePrice*(1-parseFloat(fixedCost))))*100).toFixed(1);
			$("#profitMargin").val(profitMargin+"%");
			$("#price").val(quotePrice);
		});
		
		$("#fixedCost").blur(function(){
			var amount = $("#amount").val();
			var ret = PJ.grid_getSelectedData(grid);
			var profitmargin=ret["profitmargin"];
			var quoteAmount = ret["quoteAmount"];
			var quoteRemark = ret["quoteRemark"];
			var quotePrice = ret["quotePrice"];
			var profitMargin = ret["profitMargin"];
			var supplierQuoteElementId = ret["id"];
			var location= ret["location"];
			var freight=ret["freight"];
			var counterFee=ret["counterFee"];
			var hazmatFee=ret["hazmatFee"];
			var feeForExchangeBill = ret["feeForExchangeBill"];
			var price=ret["price"];
			var fixedCost=$("#fixedCost").val();
			if(""==freight){
				freight=0;
			}
			if(""==counterFee||counterFee<1){
				counterFee=0;
			}
			if(""==feeForExchangeBill||feeForExchangeBill<1){
				feeForExchangeBill=0;
			}
			if(""==hazmatFee||hazmatFee<1){
				hazmatFee=0;
			}
			if(null==fixedCost||""==fixedCost||parseFloat(fixedCost)>1){
				fixedCost=0;
			}
			counterFee = counterFee/amount
			feeForExchangeBill = feeForExchangeBill/amount
			hazmatFee = hazmatFee/amount
			profitmargin=1-(1/parseFloat(profitmargin));
			quotePrice=((parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee))/(1-parseFloat(profitmargin))/(1-parseFloat(fixedCost))).toFixed(4);
			profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee))/(quotePrice*(1-parseFloat(fixedCost))))*100).toFixed(1);
			$("#profitMargin").val(profitMargin+"%");
			$("#price").val(quotePrice);
		}); */
		
		 function getSearchString() {
				var flag = true, searchString = "";
				$(document).find("[alias]").each(function(i, e) {
					if (!$(e).val() || !$.trim($(e).val()))
						return true;
					var result = checkRule(e);
					flag = result[0];
					if (flag) {
						var value = $(e).val();
						var alias = $(e).attr("alias").split(" or ");
						if(alias.length>=2){
							if(!searchString){
								searchString += " ( ";
							}else{
								searchString += " and ( ";
							}
							$(alias).each(function(index){
								var str = getRuleString(alias[index], result[1], value, result[2]);
								if (searchString && str){
									if(searchString.substring(searchString.length-2, searchString.length) != "( "){
										searchString += " OR ";
									}
								}
								searchString += str;
							});
							searchString += " ) ";
						}else{
							var str = getRuleString($(e).attr("alias"), result[1], value, result[2]);
							if (searchString && str){
								searchString += " AND ";
							}	
								searchString += str;
						}
						
					} else {
						return false;
					}
				});
				return searchString;
			};
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		 
		//保存按钮
		$("#addBtn").click(function(){
			var check =validate();
			var profitMargin=$("#profitMargin").val();
			if(check){
				var postData = getFormData();
				if(postData.clientInquiryElementId == undefined){
					PJ.warn("请选中询价明细！");
				}else{
					$.ajax({
						url: '<%=path%>/clientquote/creatClientQuoteByHis',
						data: postData,
						type: "POST",
						loading: "正在处理...",
						success: function(result){
								if(result.success){
									PJ.success(result.message);
								} else {
									PJ.error(result.message);
								}
								$("#leadTime").val("");
								$("#fixedCost").val("");
								$("#amount").val("");
								$("#price").val("");
								$("#profitMargin").val("");
								$("#averagePrice").val("");
								$("#clientInquiryElementId").val("");
								$("#lowestPrice").val("");
								$("#compareRate").val("");
								$("#remark").val("");
								$("#bankCharges").val("");
								$("#location").val("");
								$("#fixedCost").val("");
								$("#leadTime").val("");
								$("#leadTime").val("");
								$("#moq").val("");
						}
					});
				}
			}
		 });
		 
		//关闭按钮
		$("#colseBtn").click(function(){
			$.ajax({
				url: '<%=path%>/clientquote/changeColseStatus',
				data: getFormData(),
				type: "POST",
				loading: "正在处理...",
				success: function(result){
						if(result.success){
							PJ.success(result.message);
						} else {
							PJ.error(result.message);
						}
						$("#leadTime").val("");
						$("#fixedCost").val("");
				}
			});
		 });
		 
		 
		//改变窗口大小自适应
		/*$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});*/
		//改变窗口大小自适应
		$(window).resize(function() {
			GridResize();
		});
			
		function VasColorFormatter(cellvalue, options, rowObject){
			 if("1444库存" == cellvalue){
				 return "<font style='color:red'>"+cellvalue+"</font>"
			 }else{
				 return cellvalue
			 }
		 }
		
		function GridResize() {
			grid1.setGridWidth(PJ.getCenterWidth());
			grid2.setGridWidth(PJ.getCenterBottomWidth());
			grid1.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
			grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		}
		grid1.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
		grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));	
		
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		$("#searchForm3").blur(function(){
			var text = $("#searchForm3").val();
			$("#searchForm3").val(trim(text));
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
						$option.val(obj[i].id).text(obj[i].code);
						$("#searchForm1").append($option);
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
						$option.val(obj[i].id).text(obj[i].code);
						$("#supplierId").append($option);
					}
				}else{
					PJ.showWarn(result.msg);
				}
			}
		});
		
		//状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/staticprice/findConditionForSupplier',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#conditionId").append($option);
					}
				}else{
					PJ.warn("操作失误!");
				}
			}
		});
		
		//售前状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/elementStatus',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#elementStatus").append($option);
					}
				}else{
					PJ.warn("操作失误!");
				}
			}
		});
		
		//证书信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/staticprice/findCertificationForSupplier',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#certificationId").append($option);
					}
				}else{
					PJ.warn("操作失误!");
				}
			}
		});
		
		//货币信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/currencyType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].currency_id).text(obj[i].currency_value);
						$("#currencyId").append($option);
						
					}
				}else{
					PJ.warn("操作失误!");
				}
			}
		});
		
		//获取表单数据
		function getData(){
			var postData = {};
			var ids =  PJ.grid_getMutlSelectedRowkey(grid2);
			supplierQuoteElementIds = ids.join(",");
			if(supplierQuoteElementIds.length>0){
				postData.supplierQuoteElementIds = supplierQuoteElementIds;
			}
			return postData;
		}
		
		function attachFormatter(cellvalue, options, rowObject){
			var id = rowObject["attachId"];
			
			switch (cellvalue) {
				case 1: 
					return PJ.addTabLink("查看附件","是","<%=path%>/fj/toViewPdf?ids="+id,"red")
					break;
				default: 
					return cellvalue;
					break; 
				}
		}
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	function change(){
		var amount = $("#amount").val();
		var moq = $("#moq").val();
		if(moq != ""){
			if(parseFloat(moq) > parseFloat(amount)){
				amount = moq;
			}
		}
		var ret = PJ.grid_getSelectedData(grid2);
		var profitmargin=ret["profitmargin"];
		var quoteAmount = ret["quoteAmount"];
		var quoteRemark = ret["quoteRemark"];
		var quotePrice = ret["quotePrice"];
		var profitMargin = ret["profitMargin"];
		var supplierQuoteElementId = ret["id"];
		var location= ret["location"];
		var freight=ret["freight"];
		var counterFee=ret["counterFee"];
		var hazmatFee=ret["hazmatFee"];
		var otherFee = ret["otherFee"];
		var feeForExchangeBill = ret["feeForExchangeBill"];
		var price=ret["price"];
		var fixedCost=$("#fixedCost").val();
		var bankCharges=$("#bankCharges").val();
		var bankChargesFee = 0;
		if(""==bankCharges){
			bankCharges=0;
		}else if(parseFloat(bankCharges)>=1) {
			bankChargesFee = bankCharges
			bankCharges = 0
		}
		if(""==freight){
			freight=0;
		}
		if(""==counterFee){
			counterFee=0;
		}else{
			counterFee = counterFee.replace('$','')
		}
		if(""==feeForExchangeBill){
			feeForExchangeBill=0;
		}else{
			feeForExchangeBill = feeForExchangeBill.replace('$','')
		}
		if(""==hazmatFee){
			hazmatFee=0;
		}else{
			hazmatFee = hazmatFee.replace('$','')
		}
		if(""==otherFee){
			otherFee=0;
		}else{
			otherFee = otherFee.replace('$','')
		}
		var fixedCostFee = 0;
		if(null==fixedCost||""==fixedCost){
			fixedCost=0;
		}else if(parseFloat(fixedCost)>=1) {
			fixedCostFee = fixedCost
			fixedCost = 0
		}
		counterFee = counterFee/amount
		feeForExchangeBill = feeForExchangeBill/amount
		otherFee = otherFee/amount
		currentMargin = $("#profitMargin").val()
		profitmargin=1-(1/parseFloat(profitmargin));
		if(currentMargin != ""){
			profitmargin = parseFloat(currentMargin/100)
		}
		quotePrice=(((parseFloat(price))/(1-parseFloat(profitmargin))+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
		//profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
		profitMargin=((quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
								(quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)
		$("#profitMargin").val(profitMargin);
		$("#price").val(quotePrice);
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
			var $input = $("#addForm").find("input,textarea,select");
			var postData = {};
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
				if($(this).attr("name") == 'profitMargin'){
					var val = $(this).val()
					val = val.replace("%","")
					postData[$(this).attr("name")] = val;
				}else{
					postData[$(this).attr("name")] = $(this).val();
				}
				
			});
			return postData;
	 }	
	
	 function onlyNum(){
		 var number=$("#leadTime").val();
		 if(isNaN(number)){
			 $("#msg").html("周期必须是数字!");
		 }else if(!isNaN(number)){
			 $("#msg").html("");
		 }
	 }
	
	//获取表单数据
	function getFormData2(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};
		$input.each(function(index){
			if(!$(this).val()) {
				return;
			}
			postData[$(this).attr("name")] = $(this).val();
		});
		if($("#replace").is(':checked')==true){
			postData.replace=1;
		}else{
			postData.replace=0;
		}
		return postData;			
	}
	
	function onPriceChange() {
		var amount = $("#amount").val();
		var moq = $("#moq").val();
		if(moq != ""){
			if(parseFloat(moq) > parseFloat(amount)){
				amount = moq;
			}
		}
		var ret = PJ.grid_getSelectedData(grid2);
		var profitmargin=ret["profitmargin"];
		var quoteAmount = ret["quoteAmount"];
		var quoteRemark = ret["quoteRemark"];
		var quotePrice = ret["quotePrice"];
		var profitMargin = ret["profitMargin"];
		var supplierQuoteElementId = ret["id"];
		var location= ret["location"];
		var freight=ret["freight"];
		var counterFee=ret["counterFee"];
		var hazmatFee=ret["hazmatFee"];
		var otherFee = ret["otherFee"];
		var feeForExchangeBill = ret["feeForExchangeBill"];
		var price=ret["price"];
		var fixedCost=$("#fixedCost").val();
		var bankCharges=$("#bankCharges").val();
		var clientQuotePrice = $("#price").val();
		var bankChargesFee = 0;
		if(""==bankCharges){
			bankCharges=0;
		}else if(parseFloat(bankCharges)>=1) {
			bankChargesFee = bankCharges
			bankCharges = 0
		}
		if(""==freight){
			freight=0;
		}
		if(""==counterFee){
			counterFee=0;
		}else{
			counterFee = counterFee.replace('$','')
		}
		if(""==feeForExchangeBill){
			feeForExchangeBill=0;
		}else{
			feeForExchangeBill = feeForExchangeBill.replace('$','')
		}
		if(""==hazmatFee){
			hazmatFee=0;
		}else{
			hazmatFee = hazmatFee.replace('$','')
		}
		if(""==otherFee){
			otherFee=0;
		}else{
			otherFee = otherFee.replace('$','')
		}
		var fixedCostFee = 0;
		if(null==fixedCost||""==fixedCost){
			fixedCost=0;
		}else if(parseFloat(fixedCost)>=1) {
			fixedCostFee = fixedCost
			fixedCost = 0
		}
		counterFee = counterFee/amount
		feeForExchangeBill = feeForExchangeBill/amount
		//hazmatFee = hazmatFee/amount
		otherFee = otherFee/amount
		profitmargin=1-(1/parseFloat(profitmargin));
		if(currentMargin != ""){
			profitmargin = parseFloat(currentMargin/100)
		}
		quotePrice=((parseFloat(price))/(1-parseFloat(profitmargin))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee)).toFixed(4);
		//profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
		profitMargin=((clientQuotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - clientQuotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
								(clientQuotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - clientQuotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)
		$("#profitMargin").val(profitMargin);
	}
	
	function profitMarginChange(){
		var amount = $("#amount").val();
		var moq = $("#moq").val();
		if(moq != ""){
			if(parseFloat(moq) > parseFloat(amount)){
				amount = moq;
			}
		}
		var ret = PJ.grid_getSelectedData(grid2);
		var profitmargin=$("#profitMargin").val();
		var quoteAmount = ret["quoteAmount"];
		var quoteRemark = ret["quoteRemark"];
		var quotePrice = ret["quotePrice"];
		var profitMargin = ret["profitMargin"];
		var supplierQuoteElementId = ret["id"];
		var location= ret["location"];
		var freight=ret["freight"];
		var counterFee=ret["counterFee"];
		var hazmatFee=ret["hazmatFee"];
		var otherFee = ret["otherFee"];
		var feeForExchangeBill = ret["feeForExchangeBill"];
		var price=ret["price"];
		var fixedCost=$("#fixedCost").val();
		var bankCharges=$("#bankCharges").val();
		var bankChargesFee = 0;
		if(""==bankCharges){
			bankCharges=0;
		}else if(parseFloat(bankCharges)>=1) {
			bankChargesFee = bankCharges
			bankCharges = 0
		}
		if(""==freight){
			freight=0;
		}
		if(""==counterFee){
			counterFee=0;
		}else{
			counterFee = counterFee.replace('$','')
		}
		if(""==feeForExchangeBill){
			feeForExchangeBill=0;
		}else{
			feeForExchangeBill = feeForExchangeBill.replace('$','')
		}
		if(""==hazmatFee){
			hazmatFee=0;
		}else{
			hazmatFee = hazmatFee.replace('$','')
		}
		if(""==otherFee){
			otherFee=0;
		}else{
			otherFee = otherFee.replace('$','')
		}
		var fixedCostFee = 0;
		if(null==fixedCost||""==fixedCost){
			fixedCost=0;
		}else if(parseFloat(fixedCost)>=1) {
			fixedCostFee = fixedCost
			fixedCost = 0
		}
		counterFee = counterFee/amount
		feeForExchangeBill = feeForExchangeBill/amount
		//hazmatFee = hazmatFee/amount
		otherFee = otherFee/amount
		//profitmargin=1-(1/parseFloat(profitmargin));
		quotePrice=(((parseFloat(price))/(1-parseFloat(profitmargin/100))+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
		//profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
		profitMargin=((quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
								(quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee)- quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)
		var p = quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee)- quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))
		if(p < 0){
			profitMargin = -profitMargin;
		}
		$("#profitMargin").val(profitMargin);
		$("#price").val(quotePrice);
	}
	
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"leadTime,amount,price",
			form:"#addForm"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
		$items.each(function(i){
			var name = $(this).attr("name");
			if(!name)return;
			for(var k in nodes){
				if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
				if(!flag){
	                $(this).addClass("input-error");
					tip = $(this).attr("data-tip");
					return false;
				}else $(this).removeClass("input-error");
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
	
	function edit(){
		var ret = PJ.grid_getSelectedData(grid1);
		var client_inquiry_quote_number = ret["quoteNumber"];
		var iframeId = 'editclientquoteFrame';
		var clientInquiryElementId = ret["id"];
		var clientQuoteElementId = ret["clientQuoteElementId"];
		var supplierQuoteElementId = ret["supplierQuoteElementId"];
		var clientQuoteId = ret["clientQuoteId"];
		if(clientQuoteElementId==""){
			PJ.warn('没有报价不能进行修改');
			return;
		}
	 	PJ.topdialog(iframeId, '销售 - 修改客户报价明细','<%=path%>/clientquote/addquoteelement?clientQuoteId='+clientQuoteId+'&client_inquiry_quote_number='+client_inquiry_quote_number+
	 			'&clientInquiryElementId='+clientInquiryElementId+'&optType='+"edit"+'&id='+clientQuoteElementId+'&supplierQuoteElementId='+supplierQuoteElementId,
	 			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid2);}, 1200, 500, true);
		
	}
	
</script>

</head>
<style type="text/css">
    .SelectBG{
        background-color:#FF6666;
    }
    .GreenBG{
        background-color:#98FB98;
    }
</style>
<body>
	<div id="layout1">
		<div position="center" >
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 130px;display: block;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="5">
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="searchForm2" class="text" type="text" name="partNumber" alias="cie.part_number" oper="cn"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>询价单号：</p></form:left>
							<form:right><p><input id="searchForm3" class="text" type="text" name="quoteNumber" alias="ci.quote_number or ci.source_number" oper="cn"/> </p></form:right>
						</form:column>
						<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="c.id" oper="eq">
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
						<form:column >
							<form:left><p>报价状态：</p></form:left>
							<form:right><p><select name="manageStatus" id="manageStatus">
												<option value="">全部</option>
												<option value="0">未报价</option>
												<option value="1">已报价</option>
											</select> 
										</p>
							</form:right>
						</form:column>
					</form:row>
					<form:row columnNum="5">
						<form:column>
							<form:left><p>售前状态</p></form:left>
							<form:right><p><select name="elementStatus" id="elementStatus" alias="cie.ELEMENT_STATUS_ID" oper="eq">
												<option value="">全部</option>
											</select>
										</p>
							</form:right>
						</form:column>
						<form:column>
							<form:left><p>询价日期：</p></form:left>
							<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd HH'})" name="inquiryStart" alias="ci.create_timestamp" oper="gt"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p> - </p></form:left>
							<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',dateFmt:'yyyy-MM-dd HH'})" name="inquiryEnd" alias="ci.create_timestamp" oper="lt"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>客户开头</p></form:left>
							<form:right><p><input id="beginWith" class="text" type="text" name="beginWith"/></p></form:right>
						</form:column>
						<form:column>
							<form:left><p>描述：</p></form:left>
							<form:right><p><input id="description" class="text" type="text" name="description" alias="cie.description" oper="cn"/> </p></form:right>
						</form:column>
					</form:row>
					<form:row columnNum="5">
						<form:column>
							<form:left></form:left>
							<form:right></form:right>
						</form:column>
						<form:column>
							<form:left></form:left>
							<form:right></form:right>
						</form:column>
						<form:column>
							<form:left></form:left>
							<form:right></form:right>
						</form:column>
						<form:column>
							<form:left></form:left>
							<form:right></form:right>
						</form:column>
						<form:column >
							<p style="padding-left:45px;">
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
		<div position="centerbottom">
			<div id="toolbar2"></div>
			<div id="uploadBox" style="width: 100%;height: 110px;display: none;">
				<form id="addForm">
					<input type="text" class="hide" name="supplier_quote_price" id="supplier_quote_price" value="">
					<input type="text" class="hide" name="clientInquiryId" id="clientInquiryId" >
					<input type="text" class="hide" name="clientInquiryElementId" id="clientInquiryElementId">
					<input type="text" class="hide" name="supplierQuoteElementId" id="supplierQuoteElementId">
					<input type="text" class="hide" name="freight" id="freight">
					<input type="text" class="hide" name="counterFee" id="counterFee">
					<input type="text" class="hide" name="feeForExchangeBill" id="feeForExchangeBill">
					<input type="text" class="hide" name="hazmatFee" id="hazmatFee">
					<input type="text" class="hide" name="relativeRate" id="relativeRate">
					<%-- <input type="text" class="hide" name="fixedCost" id="fixedCost" value="${fixedCost}"> --%>
						<form:row columnNum="6">
							<form:column>
							       <form:left><p>MOQ<input id="moq"  type="text"  prefix="" name="moq" class="text" style="width: 60px" value="${clientQuoteElement.moq}" onBlur="change()"/></p></form:left>
							       <form:right><p>数量<input id="amount" type="text"  prefix="" name="amount" class="text" style="width: 60px"  value="${clientQuoteElement.amount}" onBlur="change()"/></p></form:right>
							   	</form:column>
							<form:column>
							       <form:left><p>单价</p></form:left>
							      <form:right><p> <input id="price" type="text"  prefix="" name="price" class="text"   value="${clientQuoteElement.price}" onchange="onPriceChange()"/></p></form:right>
							 	</form:column>
							<form:column>
							       <form:left><p>利润率</p></form:left>
							      <form:right><p> <input id="profitMargin" type="text"  prefix="" name="profitMargin" class="text" value="" onchange="profitMarginChange()"/>%</p></form:right>
							</form:column>
							<form:column>
								<form:left><p>平均价格</p></form:left>
								<form:right><p><input id="averagePrice" type="text"  prefix="" name="averagePrice" class="text" disabled="disabled" value="${averagePrice}"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>最低报价：</p></form:left>
								<form:right><p><input id="lowestPrice" type="text"  prefix="" name="bottomPrice" class="text" disabled="disabled" value="${lowestPrice}"/></p></form:right>
							</form:column>
						</form:row>
						<form:row columnNum="6">
							<form:column>
							       <form:left><p>比较比率：</p></form:left>
							      <form:right><p> <input id="compareRate" type="text"  prefix="" name="compareRate" class="text" disabled="disabled" value="${compareRate}"/></p></form:right>
							</form:column>
							<form:column>
							       <form:left><p>备注：</p></form:left>
							           <form:right><p><input id="remark" type="text"  prefix="" name="remark" class="text"  value="${clientQuoteElement.remark}"/></p></form:right>
							</form:column>
							<form:column>
							      <%--  <form:left><p>
							       				到货周期							       			  
							       			  </p>
							       </form:left>
							       <form:right>
							       		<input id="leadTime" onkeyup="onlyNum()" type="text"  prefix="" name="leadTime" class="text" style="width: 30px" value="${clientQuoteElement.leadTime}"/>天<span id="msg" style="color: red;"></span>
							       </form:right> --%>
							       <form:left><p>到货周期<input id="leadTime" onkeyup="onlyNum()" type="text"  prefix="" name="leadTime" class="text" style="width: 30px" value="${clientQuoteElement.leadTime}"/>天<span id="msg" style="color: red;"></span></p></form:left>
							       <form:right>
							           	银行费用<input id="bankCharges"  type="text"  prefix="" name="bankCharges" class="text" style="width: 50px" value="${bankCharges}" onBlur="change()"/>
							       </form:right>
							
							</form:column>
								<form:column>
							       <form:left><p>location：</p></form:left>
							           <form:right><p><input id="location" type="text"  prefix="" name="location" class="text"  value="${clientQuoteElement.location}"/></p></form:right>
							</form:column>
							<form:column >
								<form:left><p>佣金：</p></form:left>
								<form:right>
									<input type="text" class="text"   style="width:45px;" name="fixedCost" id="fixedCost" value="${fixedCost}" onBlur="change()"/>
								</form:right>
							</form:column>
							<form:column >
									<p style="padding-left:40px;">
									       <input class="btn btn_orange" type="button" value="新增" id="addBtn"/>		
									       <input class="btn btn_red" type="button" value="关闭" id="colseBtn"/>
									</p>	
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