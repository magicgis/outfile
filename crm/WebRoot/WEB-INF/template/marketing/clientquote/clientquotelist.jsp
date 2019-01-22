<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户报价管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
	var layout,layout2;
	var grid,grid2;
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 350,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#submit").click(function(){
			var ret = PJ.grid_getSelectedData(grid);
			var client_inquiry_quote_number = ret["quoteNumber"];
			var url = '<%=path%>/clientquote/uploadExcel?clientinquiryquotenumber='+client_inquiry_quote_number;
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	var da = eval(data)[0];
	            	if(da.flag==true){
		            	PJ.success(da.message);
		            	closeoropen();
		            	PJ.grid_reload(grid2);
	            	}
	            	else{
	            		iframeId = 'errorframe'
	                		PJ.topdialog(iframeId, '错误信息', '<%=path%>/clientquote/toUnknow',
	                				undefined,function(item,dialog){
	    		            			$.ajax({
	    									url: '<%=path%>/clientquote/deleteData',
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
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchairType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].air_type_code).text(obj[i].air_type_code+"-"+obj[i].air_type_value);
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
						icon : 'view',
						text : '报价单下载',
						click : function(){
							Excel();
								}
							},
					 {
					icon : 'process',
					text : '修改',
					click : function(){
						var ret = PJ.grid_getSelectedData(grid);
						var client_quote_id = ret["id"];
						var client_inquiry_quote_number = ret["quoteNumber"];
						var iframeId="clientquoteFrame";
						PJ.topdialog(iframeId, '修改客户报价', 
					 			'<%=path%>/clientquote/updateview?client_inquiry_quote_number='+client_inquiry_quote_number
					 			+'&client_quote_id='+client_quote_id,
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();
											 if(postData!=null){
											 $.ajax({
												    url: '<%=path%>/clientquote/updateclientquote',
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
									},function(item,dialog){dialog.close();}, 1000, 500, true,'修改');
			  			}
					},
				 {
					icon : 'add',
					text : '新增客户订单',
					click : function(){
						var ret = PJ.grid_getSelectedData(grid);
						var client_inquiry_quote_number = ret["quoteNumber"];
						var client_quote_id = ret["id"];
						var iframeId = 'clientquoteFrame';
					 	PJ.topdialog(iframeId, '销售 - 新增客户订单', 
					 			'<%=path%>/clientquote/addclientorder?client_inquiry_quote_number='+client_inquiry_quote_number
					 			+'&client_quote_id='+client_quote_id,
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 if(postData!=null){
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
											 }
									},function(item,dialog){dialog.close();}, 1000, 500, true,'新增');
						}
						}, {
							icon : 'add',
							text : '新增客户预订单',
							click : function(){
								var ret = PJ.grid_getSelectedData(grid);
								var client_inquiry_quote_number = ret["quoteNumber"];
								var client_quote_id = ret["id"];
								var iframeId = 'clientquoteFrame';
							 	PJ.topdialog(iframeId, '销售 - 新增客户预订单', 
							 			'<%=path%>/market/clientweatherorder/addclientorder?client_inquiry_quote_number='+client_inquiry_quote_number
							 			+'&client_quote_id='+client_quote_id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 if(postData!=null){
													 $.ajax({
														    url: '<%=path%>/market/clientweatherorder/save',
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
											},function(item,dialog){dialog.close();}, 1000, 500, true,'新增');
								}
								},
						{
							icon : 'view',
							text : '报价明细下载',
							click : function(){
								ClientOrderExcel();
									}
								},
						{
							icon : 'view',
							text : '利润表',
							click : function(){
								var ret = PJ.grid_getSelectedData(grid);
								var clientquoteid = ret["id"];
								var client_inquiry_quote_number = ret["quoteNumber"];
								var iframeId = 'clientquoteFrame';
							 	PJ.topdialog(iframeId, '财务 - 利润表', 
							 			'<%=path%>/clientquote/profitStatementByQuote?clientquoteid='+clientquoteid+'&clientInquiryQuoteNumber='+client_inquiry_quote_number,
							 			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
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
										PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/clientquote/file?id='+id,
												undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
									}
							 },
							 {
									icon : 'view',
									text : '多份报价单下载',
									click : function(){
								
										var ret = PJ.grid_getSelectedData(grid);
										var client_inquiry_id = ret["client_inquiry_id"];
										var clientTemplateType = ret["clientTemplateType"];
										var bizTypeId= ret["bizTypeId"];
										var currencyId= ret["currency_id"];
										var iframeId = 'clientquoteFrame';
									 	PJ.topdialog(iframeId, '多份报价单下载', 
									 			'<%=path%>/clientquote/quotesListPage?client_inquiry_id='+client_inquiry_id+'&clientTemplateType='
									 					+clientTemplateType+'&bizTypeId='+bizTypeId+'&currencyId='+currencyId,
									 			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
									}
							 },
							 
			          {
					id:'search1',
					icon : 'search',
					text : '展开搜索',
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
					icon : 'add',
				    text : '新增',
					click : function(){
					add();
				}
				},        
			 	{
					icon : 'edit',
					text : '修改',
					click : function(){
						edit();
					}
				}, {
					id:'down',
					icon : 'down',
					text : '件号附件下载',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid2);
						var ret2 = PJ.grid_getSelectedData(grid);
						var clientInquiryId = ret2["client_inquiry_id"];
						var clientInquiryElementId = ret["clientInquiryElementId"];
						var iframeId="uploadframe";
						PJ.topdialog(iframeId, ' 件号附件下载 ', '<%=path%>/clientquote/partfileHis?id='+clientInquiryId+'&type='+"marketing"+"&clientInquiryElementId="+clientInquiryElementId,
								undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
					}
			 },
				{
					id:'search',
					icon : 'search',
					text : '展开文件上传',
					click: function(){
						$("#uploadBox").toggle(function(){
							var display1 = $("#uploadBox").css("display");
							var display2 = $("#searchdiv2").css("display");
							if(display1=="block"&&display2=="block"){
								$("#toolbar2 > div[toolbarid='search'] > span").html("收起文件上传");
								grid2.setGridHeight(PJ.getCenterHeight()-240);
							}else if(display1=="block"){
								$("#toolbar2 > div[toolbarid='search'] > span").html("收起文件上传");
								grid2.setGridHeight(PJ.getCenterHeight()-200);
							}else if(display1=="none"&&display2=="block"){
								$("#toolbar2 > div[toolbarid='search'] > span").html("展开文件上传");
								grid2.setGridHeight(PJ.getCenterHeight()-200);
							}
							else{
								$("#toolbar2 > div[toolbarid='search'] > span").html("展开文件上传");
								grid2.setGridHeight(PJ.getCenterHeight()-160);
							}
						});
					}
				},
				 {
					id:'search2',
					icon : 'search2',
					text : '展开搜索',
					click: function(){
						$("#searchdiv2").toggle(function(){
							var display1 = $("#uploadBox").css("display");
							var display2 = $("#searchdiv2").css("display");
							if(display1=="block"&&display2=="block"){
								$("#toolbar2 > div[toolbarid='search2'] > span").html("收起搜索");
								grid2.setGridHeight(PJ.getCenterHeight()-240);
							}else if(display2=="block"){
								$("#toolbar2 > div[toolbarid='search2'] > span").html("收起搜索");
								grid2.setGridHeight(PJ.getCenterHeight()-200);
							}
							else if(display1=="block"&&display2=="none"){
								$("#toolbar2 > div[toolbarid='search2'] > span").html("展开搜索");
								grid2.setGridHeight(PJ.getCenterHeight()-200);
							}
							else{
								$("#toolbar2 > div[toolbarid='search2'] > span").html("展开搜索");
								grid2.setGridHeight(PJ.getCenterHeight()-160);
							}
						});
				}
			}]
		});
		
		grid = PJ.grid("list", {
			rowNum: 20,
			url:'<%=path%>/clientquote/clientquotedate',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "cq.update_timestamp",
			sortorder : "desc",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			pager: "#pager1",
			colNames : [ "报价ID","模板类型","商业类型","询价ID","客户","","报价单号", "报价日期","总价", "币种", "汇率","交付方式","利润率",
			             "预付","发货","账期","验货","账期","运费","最低运费","更新时间","固定成本"],
			colModel : [PJ.grid_column("id", {hidden:true}),
			            PJ.grid_column("clientTemplateType", {hidden:true}),
			            PJ.grid_column("bizTypeId", {hidden:true}),
			            PJ.grid_column("client_inquiry_id", {hidden:true}),
			            PJ.grid_column("client_code", {sortable:true,align:'left',width:60}),
			            PJ.grid_column("currency_id", {hidden:true}),
			            PJ.grid_column("quoteNumber", {width:160,align:'left'}),
						PJ.grid_column("quoteDate", {sortable:true,width: 120,align:'left'}),
						PJ.grid_column("total", {sortable:true,width: 80,align:'left'}),
						PJ.grid_column("currency_value", {width:80,align:'left'}),
						PJ.grid_column("exchangeRate", {align:'left',width: 80}),
						PJ.grid_column("termsOfDeliveryValue", {align:'left',width: 80}),
						PJ.grid_column("profitMargin", {width: 80,formatter:function(value){
							if(value){
								return value+"%";
							}
							else{
								return value;
							}
						}	
						,align:'left'}),
						PJ.grid_column("prepayRate", {width: 60,formatter:function(value){
								if(value){
									return value*100+"%";
								}
								else{
									return value;
								}
							}	,align:'left'}),
						
						PJ.grid_column("shipPayRate", {width: 85,formatter:function(value){
								if(value){
									return value*100+"%";
								}
								else{
									return value;
								}
							}	,align:'left'}),
						PJ.grid_column("shipPayPeriod", {width: 80,align:'left'}),
						PJ.grid_column("receivePayRate", {width: 85,formatter:function(value){
								if(value){
									return value*100+"%";
								}
								else{
									return value;
								}
							}	,align:'left'}),
						PJ.grid_column("receivePayPeriod", {width: 80,align:'left'}),
						PJ.grid_column("freight", {width: 80,align:'left'}),
						PJ.grid_column("lowestFreight", {width: 80,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:200,align:'left'}),
						PJ.grid_column("fixedCost", {hidden:true}),
						],
						
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 10,
			width : PJ.getCenterBottomWidth(),
			height : PJ.getCenterBottomHeight(),
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager2",
			sortname : "csn,item",
			sortorder : "asc",
			editurl:'<%=path%>/clientquote/updateclientquoteelement',
			colNames : [ "询价id","报价id","询价备注","明细id","序号", "件号","CSN","主件号","主件号","描述","数量", "单位","黑名单",
			             "供应商", "件号", "备注","单位", "数量","MOQ", "单价","手续费","证书","状态","Location","运费","到货周期","MOV","Warranty","SN","TagSrc","TagDate","Trace",
			             "数量","单价", "总价","佣金","银行费用","Location","利润率","到货周期","备注","供应商报价明细id","客户报价明细id","更新时间"],
			colModel : [PJ.grid_column("clientInquiryElementId", {hidden:true,key:true}),
			            PJ.grid_column("clientQuoteId", {hidden:true}),
			            PJ.grid_column("inquiryRemark", {hidden:true}),
			            PJ.grid_column("elementId", {hidden:true}),
			            PJ.grid_column("item", {sortable:true,width:30,align:'left'}),
			            PJ.grid_column("partNumber", {width:100,align:'left'}),
			            PJ.grid_column("csn", {width:30,align:'left',sortable:true}),
			            PJ.grid_column("isMain",{sortable:true,width:40,align:'left',
				        	   formatter: function(value){
									if (value==2) {
										return "是";
									} else if(value==1){
										return "否";							
									}else {
										return "";
									}
								}   
				           }),
				           PJ.grid_column("mainPartNumber",{sortable:true,width:100,align:'left'}),
						PJ.grid_column("description", {width:150,align:'left'}),
						PJ.grid_column("amount", {width:30,align:'left'}),
						PJ.grid_column("unit", {width:30,align:'left'}),
						 PJ.grid_column("isBlacklist",{sortable:true,width:40,align:'left',
				        	   formatter: function(value){
									if (value==1||value=="是") {
										return "是";
									} else if(value==0||value=="否"){
										return "否";							
									}else {
										return "";
									}
								}
				           }),
						PJ.grid_column("supplierCode", {width:40,align:'left'}),
						PJ.grid_column("quotePartNumber", {width:100,align:'left'}),
						PJ.grid_column("quoteRemark", {width:150,align:'left'}),
						PJ.grid_column("quoteUnit", {width:30,align:'left'}),
						PJ.grid_column("supplierQuoteAmount", {width:30,align:'left'}),
						PJ.grid_column("moq", {width:30,align:'left'}),
						PJ.grid_column("supplierQuotePrice", {width:40,align:'left'}),
						PJ.grid_column("counterFee", {width:50,align:'left',hidden:true}),
						PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("conditionCode", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("location", {width:80,align:'left'}),
						PJ.grid_column("freight", {sortable:true,width:80}),
						PJ.grid_column("leadTime", {width:50,align:'left'}),
						PJ.grid_column("mov", {width:100,align:'left'}),
						PJ.grid_column("warranty", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("serialNumber", {sortable:true,width:100,editable:true,align:'left'}),
						PJ.grid_column("tagSrc", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("tagDate", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("trace", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("clientQuoteAmount", {width:30,editable:true,align:'left'}),
						PJ.grid_column("clientQuotePrice", {width:60,editable:true,align:'left'}),
						PJ.grid_column("countprice", {width:60,align:'left'}),
						PJ.grid_column("fixedCost", {width:60,align:'left'}),
						PJ.grid_column("bankCharges", {width:60,align:'left'}),
						PJ.grid_column("quoteLocation", {width:60,editable:true,align:'left'}),
						PJ.grid_column("profitMargin", {width:60
							 ,formatter:function(value){
								if(value){
									return value+"%";
								}
								else{
									return value;
								}
							} 		
						,align:'left'}),
						PJ.grid_column("cqeLeadTime", {width:50,editable:true,align:'left'}),
						PJ.grid_column("clientQuoteRemark", {width:150,editable:true,align:'left'}),
						PJ.grid_column("supplierQuoteElementId", {hidden:true,editable:true,align:'left'}),
					    PJ.grid_column("id", {hidden:true,editable:true,align:'left'}),
						PJ.grid_column("quoteupDatTimestamp", {width:120,align:'left'})
						]
		});
		
		
		 grid2.jqGrid('setGroupHeaders', { 
			 	useColSpanStyle: true, 
			 	groupHeaders:[ 
			 		{startColumnName: 'clientInquiryElementId', numberOfColumns: 13, titleText: '<div align="center"><span>客户询价</span></div>'},
			 		{startColumnName: 'supplierCode', numberOfColumns: 19, titleText: '<div align="center"><span>供应商报价</span></div>'},
			 		{startColumnName: 'clientQuoteAmount', numberOfColumns: 12, titleText: '<div align="center"><span>客户报价</span></div>'}
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
		    	PJ.grid_search(grid,'<%=path%>/clientquote/clientquotedate');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/clientquote/clientquotedate');
		 });
		 
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		 //搜索
		 $("#searchBtn2").click(function(){
				var ret = PJ.grid_getSelectedData(grid);
				var clientquoteid = ret["id"];
			 PJ.grid_search(grid2,'<%=path%>/clientquote/clientquoteelementdate?clientquoteid='+clientquoteid+'&supplier_quote='+$("#supplier_quote").val());
		 });
		 
		 //重置条件
		 $("#resetBtn2").click(function(){
			 $("#searchForm3")[0].reset();
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
	
	function mx(){
		var ret = PJ.grid_getSelectedData(grid);
		var client_inquiry_quote_number = ret["quoteNumber"];
		var client_inquiry_id = ret["client_inquiry_id"];
		var clientquoteid = ret["id"];
	//	var fixedCost = ret["fixedCost"];
		var iframeId = 'clientquoteFrame';
	 	PJ.topdialog(iframeId, '销售 - 客户报价明细管理', 
	 			'<%=path%>/clientquote/clientquoteelementlist?client_inquiry_quote_number='+client_inquiry_quote_number+
	 					'&clientInquiryId='+client_inquiry_id+
	 					'&clientquoteid='+clientquoteid,
	 					//'&clientquoteid='+clientquoteid+'&fixedCost='+fixedCost,
	 			
	 					undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
	}
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var client_inquiry_quote_number = ret["quoteNumber"];
		var client_inquiry_id = ret["client_inquiry_id"];
		var clientquoteid = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/clientquote/clientquoteelementdate?clientquoteid='+clientquoteid);
		
	};
	
	function closeoropen(){
		$("#uploadBox").toggle(function(){
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				$("#toolbar2 > div[toolbarid='search2'] > span").html("收起文件上传");
				grid2.setGridHeight(PJ.getCenterHeight()-430);
			}else{
				$("#toolbar2 > div[toolbarid='search2'] > span").html("展开文件上传");
				grid2.setGridHeight(PJ.getCenterHeight()-400);
			}
		});
		}
		
		function add(){
			var ret2 = PJ.grid_getSelectedData(grid2);
			var ret = PJ.grid_getSelectedData(grid);
			var client_inquiry_quote_number = ret["quoteNumber"];
			//var fixedCost = ret["fixedCost"];
			var iframeId = 'clientquoteFrame';
			var clientInquiryElementId = ret2["clientInquiryElementId"];
			var clientQuoteId = ret2["clientQuoteId"];
		/* 	var inquiryPartNumber = ret["partNumber"];
			var inquiryDescription = ret["description"];
			var inquiryAmount = ret["amount"];
			var inquiryUnit = ret["unit"];
			var inquiryRemark = ret["inquiryRemark"]; */
			var supplierCode = ret2["supplierCode"];
			 if(supplierCode=="没有报价"){
				PJ.warn('没有报价不能进行新增');
				return;
			}
			if(supplierCode==""||supplierCode==null){
				PJ.warn('不能进行此操作');
				return;
			}
			if(supplierCode=="有供应商报价"||supplierCode=="有历史报价"){
			 
		 	PJ.topdialog(iframeId, '销售 - 新增客户报价明细','<%=path%>/clientquote/addquoteelement?clientQuoteId='+clientQuoteId+ 			
		 			'&client_inquiry_quote_number='+client_inquiry_quote_number+
		 			'&clientInquiryElementId='+clientInquiryElementId+
		 			'&optType='+"add", 	
		 			//	'&optType='+"add"+'&fixedCost='+fixedCost, 			
		 			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid2);}, 1200, 500, true);
			 }else{
				PJ.warn('当前状态仅能做修改操作');
				return;
			} 
			
			}
		
		function edit(){
			var ret2 = PJ.grid_getSelectedData(grid2);
			var ret = PJ.grid_getSelectedData(grid);
			var client_inquiry_quote_number = ret["quoteNumber"];
			//var fixedCost = ret["fixedCost"];
			var iframeId = 'clientquoteFrame';
			var clientInquiryElementId = ret2["clientInquiryElementId"];
			var clientQuoteElementId = ret2["id"];
			var clientQuoteAmount = ret2["clientQuoteAmount"];
			var clientQuotePrice = ret2["clientQuotePrice"];
			var Remark = ret2["clientQuoteRemark"];
			var clientQuoteId = ret2["clientQuoteId"];
			/* var inquiryPartNumber = ret["partNumber"];
			var inquiryDescription = ret["description"];
			var inquiryAmount = ret["amount"];
			var inquiryUnit = ret["unit"];
			var inquiryRemark = ret["inquiryRemark"]; */
			var supplierCode = ret2["supplierCode"];
			var supplierQuoteElementId = ret2["supplierQuoteElementId"];
			var location = ret2["location"];
			 if(supplierCode=="没有报价"){
				PJ.warn('没有报价不能进行修改');
				return;
			}
			if(supplierCode==""||supplierCode==null){
				PJ.warn('不能进行此操作');
				return;
			}
			if(supplierCode=="有供应商报价"||supplierCode=="有历史报价"){
				PJ.warn('当前状态仅能做新增操作');
				return;
			 }else{
				 	PJ.topdialog(iframeId, '销售 - 修改客户报价明细','<%=path%>/clientquote/addquoteelement?clientQuoteId='+clientQuoteId+			 			
				 			'&client_inquiry_quote_number='+client_inquiry_quote_number+
				 			'&clientInquiryElementId='+clientInquiryElementId+
				 			'&optType='+"edit"+'&Price='+clientQuotePrice+
				 			'&id='+clientQuoteElementId+'&Amount='+clientQuoteAmount+
				 			'&supplierQuoteElementId='+supplierQuoteElementId+
				 			'&Remark='+encodeURIComponent(Remark)+
				 			'&location='+location, 	
				 			//'&location='+location+'&fixedCost='+fixedCost, 	 			
				 			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid2);}, 1200, 500, true);
			} 
			
			}
	
 	function Excel(){
 		var ret = PJ.grid_getSelectedData(grid);
 		var clientquoteid = ret["id"];
 		var currencyId = ret["currency_id"];
 		var clientTemplateType = ret["clientTemplateType"];
 		var bizTypeId = ret["bizTypeId"];
 		var clientCode = ret["client_code"];
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = "";
			if(clientTemplateType==170){
				businessKey='client_quote_element.id.'+clientquoteid+'.QuotationExcel1.'+currencyId;
			}else if(clientTemplateType==171){
				if(bizTypeId==121 && clientCode == '370'){
					businessKey='client_quote_element.id.'+clientquoteid+'.QuotationExcel3.'+currencyId;
				}else{
					businessKey='client_quote_element.id.'+clientquoteid+'.QuotationExcel2.'+currencyId;
				}			
			}else if(clientTemplateType==172){
				businessKey='client_quote_element.id.'+clientquoteid+'.QuotationExcel3.'+currencyId;
			}
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
			});
	}
 	
 	function ClientOrderExcel(){
 		var ret = PJ.grid_getSelectedData(grid);
 		var clientquoteid = ret["id"];

			//根据具体业务提供相关的title
			var title = '报价明细下载';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = "";
			
				businessKey='client_order.id.'+clientquoteid+'.ClientOrderExcel';
			
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
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
/* th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
} */ 
</style>

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
							       <form:left><p>询价单号：</p></form:left>
							    <form:right><p><input id="searchForm2" type="text"  prefix="" name="quoteNumber" class="text" oper="cn" alias="cq.quote_number or ci.source_number" value=""/></p></form:right>
							   	</form:column>
							<form:column>
							<form:left>客户</form:left>
								<form:right><p><select  id="client_code" name="client_code" alias=" c.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							              </p>
								</form:right>
							</form:column>
							<form:column>
							      <form:left>机型</form:left>
							   <form:right><p><select  id="air_type_value" name="air_type_value" alias="at.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>商业类型</form:left>
							   <form:right><p><select id="biz_type_code" name="biz_type_code" alias="bt.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							          </p>
								</form:right>
							</form:column>
						
					</form:row>
					<form:row columnNum="5">	
							<form:column>
								<form:left><p>报价日期：</p></form:left>
								<form:right><p><input id="quotedatestart"  class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'quotedateend\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="quotedatestart" alias="cq.quote_date" oper="gt"/> 
							 </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left> 
							<form:right><p>
							<input id="quotedateend"  class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'quotedatestart\')}',dateFmt:'yyyy-MM-dd'})" name="quotedateend" alias="cq.quote_date" oper="lt"/> 
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
	<div id="searchdiv2" style="width: 100%;height: 50px;display: none;">
				<form id="searchForm3">
					<div class="search-box">
						<form:row columnNum="3">
							<form:column>
							<form:left>供应商报价状态</form:left>
								<form:right><p><select style="width: 100px"  id="supplier_quote" name="supplier_quote">
							            	<option value="">全部</option>
							            	<option value="0">没有报价</option>
							            	<option value="1">供应商有报价</option>
							            	<option value="2">有历史报价</option>
							            	</select>
							              </p>
								</form:right>
							</form:column>
						<form:column ><form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn2">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn2">
							</p></form:right>
							</form:column>
					</form:row>
					</div>
				</form>
			</div>
		<table id="list2" style=""></table>
		<div id="pager2"></div>
  </div>
	</div>
</body>
</html>
