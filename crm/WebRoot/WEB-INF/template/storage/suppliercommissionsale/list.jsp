<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商寄卖</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-top;
  padding-top:2px;
 } */
</style>

<script type="text/javascript">
	var layout, grid,grid2;
	$(function() {
		//layout = $("#layout1").ligerLayout();
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 310,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [
						{
							id:'add',
							icon : 'add',
							text : '新增',
							click: function(){
								var iframeId="ideaIframe1";
									PJ.topdialog(iframeId, '新增供应商寄卖', '<%=path%>/storage/suppliercommissionsale/toAdd',
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
													$.ajax({
														url: '<%=path%>/storage/suppliercommissionsale/saveAdd',
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	//PJ.success(result.message);
																	PJ.grid_reload(grid);
																	dialog.close();
																	PJ.topdialog(iframeId, '新增寄卖明细', '<%=path%>/storage/suppliercommissionsale/toAddElementAfterAdd?id='+result.message,
																			function(item, dialog){
																				var postData=top.window.frames[iframeId].getFormData();
																				 PJ.ajax({
																						url: '<%=path%>/storage/suppliercommissionsale/addElement?id='+result.message,
																						data: postData,
																						type: "POST",
																						loading: "正在处理...",
																						success: function(result){
																								if(result.success){ 
																									PJ.success(result.message);
																									PJ.grid_reload(grid);
																									PJ.grid_reload(grid2);
																								} else {
																									PJ.error(result.message);
																								}		
																						}
																					});
																				PJ.grid_reload(grid2);
																				dialog.close();
																			},function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
																} else {
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
								id:'modify',
								icon : 'modify',
								text : '修改',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId="ideaIframe2";
										PJ.topdialog(iframeId, '修改供应商寄卖', '<%=path%>/storage/suppliercommissionsale/toEdit?id='+id,
												function(item, dialog){
														 var postData=top.window.frames[iframeId].getFormData();	 							
														 $.ajax({
																url: '<%=path%>/storage/suppliercommissionsale/saveEdit',
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
						 },
						 {
								id:'modify',
								icon : 'modify',
								text : '发起爬虫',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId = "airTypeFrame"
									PJ.topdialog(iframeId, '修改供应商寄卖', '<%=path%>/storage/suppliercommissionsale/toSelectAirType',
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 $.ajax({
															url: '<%=path%>/storage/suppliercommissionsale/getCount?id='+id,
															type: "POST",
															loading: "正在处理...",
															success: function(result){
																	if(result.success){
																		if(parseInt(result.message) > 100){
																			PJ.confirm("此单件号数量为"+result.message+",数量过多会阻碍正常询价件号的爬虫，是否继续进行?", function(yes){
																				if(yes){
																					$.ajax({
																						url: '<%=path%>/storage/suppliercommissionsale/commitCrawl?id='+id+'&airType='+postData.airType,
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
																			})
																		}else{
																			$.ajax({
																				url: '<%=path%>/storage/suppliercommissionsale/commitCrawl?id='+id+'&airType='+postData.airType,
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
																		
																	} else {
																		PJ.error(result.message);
																		dialog.close();
																	}		
															}
														});
											},function(item,dialog){dialog.close();}, 400, 200, true,"修改");
								}
						 },
						 {
								id:'modify',
								icon : 'modify',
								text : '预报价',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId="ideaIframe3";
									var airTypeId = ret["airTypeId"];
									var crawlClientInquiryId = ret["crawlClientInquiryId"];
									var clientId = ret["clientId"];
									var str = airTypeId+"/"+crawlClientInquiryId+"//"+clientId
									PJ.topdialog(iframeId, ' 预报价 ', '<%=path%>/storage/suppliercommissionsale/toPriceList?id='+str,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
									
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
											grid.setGridHeight(PJ.getCenterHeight()-152);
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
			items : [{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe5";
							var ret = PJ.grid_getSelectedData(grid);
							 var id = ret["id"];
							PJ.topdialog(iframeId, '新增明细', '<%=path%>/storage/suppliercommissionsale/toAddElement',
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getFormData();
										 PJ.ajax({
												url: '<%=path%>/storage/suppliercommissionsale/addElement?id='+id,
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){ 
															PJ.success(result.message);
															PJ.grid_reload(grid);
															PJ.grid_reload(grid2);
														} else {
															PJ.error(result.message);
														}		
												}
											});
									PJ.grid_reload(grid2);
									dialog.close();}
								   ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
							id:'download',
							icon:'down',
							text:'下载模板',
							click : function() {
								PJ.showLoading("处理中...");
								window.location.href='<%=path%>/storage/suppliercommissionsale/downloadTemplate';
								setTimeout(function(){
									PJ.hideLoading();
								}, 5000);
							}
					 },
					 {
							id:'view',
							icon : 'view',
							text : '爬虫供应商列表',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid2);
								var id = ret["id"];
								var iframeId="airSupplierframe";
									PJ.topdialog(iframeId, '爬虫供应商列表', '<%=path%>/storage/suppliercommissionsale/toAddSupplier?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
							}
					 },
					 {
							id:'search',
							icon : 'search',
							text : '收起excel上传',
							click: function(){
								$("#uploadBox").toggle(function(){
									var display = $("#uploadBox").css("display");
									if(display=="block"){
										$("#toolbar2 > div[toolbarid='search'] > span").html("收起excel上传");
										grid2.setGridHeight(PJ.getCenterHeight()-260);
									}else{
										$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
										grid2.setGridHeight(PJ.getCenterHeight()-220);
									}
								});
							}
					}
			         ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/suppliercommissionsale/List',
			width : PJ.getCenterWidth(),
			height :PJ.getCenterHeight(),
			autoSrcoll:true,
			autowidth:true,
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			pager: "#pager1",
			sortname : "scs.sale_status desc ,scs.update_timestamp",
			sortorder : "desc",
			//shrinkToFit:false,
			colNames :["id","供应商","币种","类型","StockMarket爬虫","报价时间","有效期","状态","对应询价单号","爬虫发起","爬虫对应询价单","备注","更新时间","airTypeId","crawlClientInquiryId","clientId","commissionAirTypeId"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("supplierCode", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("currencyCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("airTypeValue", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("crawlStorageStatus", {sortable:true,width:100,align:'left',
			        	   formatter:function(value){
								if (value == '1') {
									return "是";
								}else if (value == '0') {
									return "否";
								}else {
									return "";							
								}
			           	   }   
			           }),
			           PJ.grid_column("commissionDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("validity", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("saleStatus", {sortable:true,width:70,align:'left',
			        	   formatter: statusFormatter
			           }),
			           PJ.grid_column("quoteNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("crawlStatus", {sortable:true,width:80,align:'left',
			           	   formatter:function(value){
								if (value == '1') {
									return "已发起";
								} else {
									return "";							
								}
			           	   }
			           }),
			           PJ.grid_column("crawlQuoteNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:350,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("airTypeId", {hidden:true}),
			           PJ.grid_column("crawlClientInquiryId", {hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("commissionAirTypeId", {hidden:true})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 10,
			autowidth:true,
			width : PJ.getCenterBottomWidth(),
			height : PJ.getCenterBottomHeight(),
			pager: "#pager2",
			autoSrcoll:true,
			shrinkToFit:false,
			autowidth:true,
			inLineEdit: true,
			editurl:'<%=path%>/storage/suppliercommissionsale/saveElementEdit',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
			},
			colNames : ["id","件号","ALT","描述","价格","数量","单位","MOQ","周期","状态","证书","HAZMAT FEE","提货换单费","银行费用","杂费","Location"
			            ,"Core Charge","Warranty","SN","TagSrc","TagDate","Trace","备注","CSN","TSN","机型","更新时间"],
			colModel : [PJ.grid_column("id", {sortable:false,hidden:true,editable:true,align:'left'}),
			            PJ.grid_column("partNumber", {sortable:false,width:80,editable:true,align:'left',
			            	formatter : tableFormatter	
			            }),
			            PJ.grid_column("alt", {sortable:false,width:80,editable:true,align:'left'}),
			            PJ.grid_column("description", {sortable:false,width:150,editable:true,align:'left'}),
			            PJ.grid_column("price", {sortable:false,width:60,editable:true,align:'left'}),
			            PJ.grid_column("amount", {sortable:false,width:30,editable:true,align:'left'}),
			            PJ.grid_column("unit", {sortable:false,width:30,editable:true,align:'left'}),
			            PJ.grid_column("moq", {sortable:false,width:30,editable:true,align:'left'}),
			            PJ.grid_column("leadTime", {sortable:false,width:30,editable:true,align:'left'}),
			            PJ.grid_column("conditionCode", {sortable:false,width:40,align:'left'}),
			            PJ.grid_column("certificationCode", {sortable:false,width:80,align:'left'}),
			            PJ.grid_column("quoteHazmatFee", {sortable:true,width:60,editable:true,align:'left',
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
						PJ.grid_column("quoteBankCost", {sortable:true,width:60,editable:true,align:'left',
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}	
						}),
						PJ.grid_column("quoteOtherFee", {sortable:true,width:60,editable:true,align:'left',
							formatter:function(value){
								if (value) {
									return "$"+value;
								}
							}	
						}),
						PJ.grid_column("location", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("coreCharge", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("warranty", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("serialNumber", {sortable:true,width:100,editable:true,align:'left'}),
						PJ.grid_column("tagSrc", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("tagDate", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("trace", {sortable:true,width:80,editable:true,align:'left'}),
			            PJ.grid_column("remark", {sortable:false,width:200,editable:true,align:'left'}),
			            PJ.grid_column("csn", {sortable:false,editable:true,align:'left',width:40}),
			            PJ.grid_column("tsn", {sortable:false,editable:true,align:'left',width:40}),
						PJ.grid_column("airType",{sortable:false,editable:true,align:'left',width:40}),
			            PJ.grid_column("updateTimestamp", {sortable:false,width:150,align:'left'})
						],
						
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			$("#id").val(id);
			PJ.grid_search(grid2,'<%=path%>/storage/suppliercommissionsale/elementList?id='+id);
			
		};
		
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
		
		function statusFormatter(cellvalue, options, rowObject){
			switch (cellvalue) {
				case 0:
					return "无效";
					break;
					
				case 1:
					return "有效";
					break;
					
				default: 
					return cellvalue;
					break; 
			}
		}
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/suppliercommissionsale/List');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/suppliercommissionsale/List');
			
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
							$("#searchForm5").append($option);
						}
					}else{
						
							PJ.showWarn(result.msg);
					}
				}
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
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		//跳转统计页面
		function tableFormatter(cellvalue, options, rowObject){
			var id = rowObject["id"];
			var part = rowObject["partNumber"];
			return PJ.addTabLink("件号"+part+"StockMarket爬虫统计",part,"/storage/suppliercommissionsale/toCountTable?id="+id)
		}
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/storage/suppliercommissionsale/uploadExcel?id='+getFormData().id+'&editType='+ getFormData().toString();
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
	            	var da = eval(data)[0];
	            	if(da.flag==true){
		            	PJ.success("上传成功,已新增"+da.message+"条数据！");
		            	PJ.grid_reload(grid);
		            	PJ.grid_reload(grid2);
		            	$("#uploadBox").toggle(function(){
							$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
							grid2.setGridHeight(PJ.getCenterHeight()-260);
		            	});
	            	}else{
	            		PJ.grid_reload(grid);
	            		var message = da.message.split(",");
	            		if(message[0] == "condition"){
	            			PJ.error("第"+message[1]+"行状态不存在！")
	            		}else if(message[0] == "certification"){
	            			PJ.error("第"+message[1]+"行证书不存在！")
	            		}else if(message[0] == "all"){
	            			PJ.error("第"+message[1]+"行出现异常！")
	            		}else{
	            			PJ.error("生成供应商报价异常！");
	            		}
	            	}
	            	
	            	/* if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	}
	            	else{
	            		PJ.error(da.message);
	            		$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	} */
	   				
	            },  
	            error: function (data, status, e) { 
	            	var da = eval(eval(data))[0];
	            	PJ.grid_reload(grid);
            		var message = da.message.split(",");
            		if(message[0] == "condition"){
            			PJ.error("第"+message[1]+"行状态不存在！")
            		}else if(message[0] == "certification"){
            			PJ.error("第"+message[1]+"行证书不存在！")
            		}else{
            			PJ.error("第"+message[1]+"行出现异常！")
            		}
	            }  
	        });
			
		 });
		
		function getFormData(){
			 var postData = {};
			 postData.data = $("#form").serialize();
			 postData.id = $("#id").val();
			 return postData;
		};
		
	});
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;display: none;">
		<form id="searchForm" action="">
			<div class="search-box">
				<form:row columnNum="2">
					<form:column>
						<form:left><p>供应商</p></form:left>
						<form:right><p><select id="searchForm5" name="searchForm5" class="text" alias="s.id" oper="cn">
											<option value="">全部</option>
									   </select>
									</p>
						</form:right>
					</form:column>
					<form:column >
							<p style="padding-left:200px;">
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
			<table id="list2"></table>
			<div id="pager2"></div>
		
		</div>
	</div>
</body>
</html>