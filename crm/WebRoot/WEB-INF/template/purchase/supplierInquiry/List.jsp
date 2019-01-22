<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-新增供应商询价 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function(){
	layout = $("#layout1").ligerLayout({
		centerBottomHeight: 270,
		onEndResize:function(e){
		  GridResize();
		}
	});
	
	layout = $("#layout1").ligerLayout();
	$("#toolbar").ligerToolBar({
		items : [{
						id:'add',
						icon : 'add',
						text : '明细',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '采购-客户询价明细管理 ', '<%=path%>/purchase/supplierinquiry/element?id='+id,
										undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
						}
				 },
				 {
						id:'add',
						icon : 'add',
						text : '新增供应商询价',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var iframeId="ideaIframe2";
							var zt = ret["inquiryStatusValue"];
								PJ.topdialog(iframeId, '采购-新增供应商询价 ', '<%=path%>/purchase/supplierinquiry/addsupplierinquiry?id='+id,
										function(item, dialog){
									var postData=top.window.frames[iframeId].getData();	 
									var emailIframeId = "emailIframe";
									var roleName = "${roleName}";
									if(roleName=="国内采购"){
										PJ.topdialog(emailIframeId, '添加邮件内容', '<%=path%>/purchase/supplierinquiry/toAddEmailText',
												function(item, dialog){
													var email=top.window.frames[emailIframeId].getForm();
													postData.firstText = email.firstText;
													postData.secondText = email.secondText;
													var contactIframeId = "contactIframe";
													PJ.topdialog(contactIframeId, '选择收件人', '<%=path%>/purchase/supplierinquiry/getEmailList?supplierIds='+postData.supplierIds,
															function(item, dialog){
															var data=top.window.frames[contactIframeId].getFormData();
															data.supplierIds = postData.supplierIds;
															data.orderElementIds = postData.orderElementIds;
															data.firstText = postData.firstText;
															data.secondText = postData.secondText;
															PJ.ajax({
																url: '<%=path%>/purchase/supplierinquiry/saveSupplierInquiry?id='+id,
																data: data,
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
															dialog.close();	
													},function(item,dialog){dialog.close();}, 1100, 500, true,"确定");
													dialog.close();	
												},function(item,dialog){dialog.close();}, 1100, 500, true,"确定");
										}else{
											var contactIframeId = "contactIframe";
											PJ.topdialog(contactIframeId, '选择收件人', '<%=path%>/purchase/supplierinquiry/getEmailList?supplierIds='+postData.supplierIds,
													function(item, dialog){
													var data=top.window.frames[contactIframeId].getFormData();
													data.supplierIds = postData.supplierIds;
													data.orderElementIds = postData.orderElementIds;
													data.firstText = postData.firstText;
													data.secondText = postData.secondText;
													PJ.ajax({
														url: '<%=path%>/purchase/supplierinquiry/saveSupplierInquiry?id='+id,
														data: data,
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
													dialog.close();	
											},function(item,dialog){dialog.close();}, 1100, 500, true,"确定");
										}
											dialog.close();
										},function(item,dialog){dialog.close();}, 1200, 700, true,"新增");
						}
				 },
				 {
						id:'add',
						icon : 'add',
						text : '自动生成供应商询价单',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var iframeId="ideaIframe3";
								PJ.topdialog(iframeId, '自动新增供应商询价单 ', '<%=path%>/purchase/supplierinquiry/toAutoList?id='+id,
										function(item, dialog){
										var postData=top.window.frames[iframeId].getData();
										var roleName = "${roleName}";
										if(roleName=="国内采购"){
											PJ.topdialog(emailIframeId, '添加邮件内容', '<%=path%>/purchase/supplierinquiry/toAddEmailText',
													function(item, dialog){
														var email=top.window.frames[emailIframeId].getForm();
														postData.firstText = email.firstText;
														postData.secondText = email.secondText;
														var contactIframeId = "contactIframe";
														PJ.topdialog(contactIframeId, '选择收件人', '<%=path%>/purchase/supplierinquiry/getEmailListByMsn?msn='+postData.msn,
																function(item, dialog){
																var data=top.window.frames[contactIframeId].getFormData();
																data.clientInquiryElementIds = postData.clientInquiryElementIds;
																data.bsn = postData.bsn;
																data.msn = postData.msn;
																data.firstText = postData.firstText;
																data.secondText = postData.secondText;
																PJ.showLoading("处理中....");
																PJ.ajax({
																	url: '<%=path%>/purchase/supplierinquiry/saveAdd?id='+id,
																	data: data,
																	type: "POST",
																	loading: "正在处理...",
																	success: function(result){
																			if(result.success){
																				PJ.hideLoading();
																				PJ.success(result.message);
																				PJ.grid_reload(grid);
																				dialog.close();
																			} else {
																				PJ.hideLoading();
																				PJ.error(result.message);
																				dialog.close();
																			}		
																	}
																});
																dialog.close();	
														},function(item,dialog){dialog.close();}, 1100, 500, true,"确定");
														dialog.close();	
													},function(item,dialog){dialog.close();}, 1100, 500, true,"确定");
											}else{
												var contactIframeId = "contactIframe";
												PJ.topdialog(contactIframeId, '选择收件人', '<%=path%>/purchase/supplierinquiry/getEmailListByMsn?msn='+postData.msn,
														function(item, dialog){
														var data=top.window.frames[contactIframeId].getFormData();
														data.clientInquiryElementIds = postData.clientInquiryElementIds;
														data.bsn = postData.bsn;
														data.msn = postData.msn;
														data.firstText = postData.firstText;
														data.secondText = postData.secondText;
														PJ.ajax({
															url: '<%=path%>/purchase/supplierinquiry/saveAdd?id='+id,
															data: data,
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
														dialog.close();	
												},function(item,dialog){dialog.close();}, 1100, 500, true,"确定");
											}
											<%-- var postData=top.window.frames[iframeId].getData();	 							
											 $.ajax({
												url: '<%=path%>/purchase/supplierinquiry/saveAdd?id='+id,
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
											}); --%>
											 
										},function(item,dialog){dialog.close();}, 1100, 500, true,"新增");
						}
				 },
				 {
						id:'down',
						icon : 'down',
						text : '下载',
						click: function(){
							    var ret = PJ.grid_getSelectedData(grid);
					 		    var id = ret["id"];
								//根据具体业务提供相关的title
								var title = 'excel管理';
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var businessKey = 'client_supplier_inquiry.id.'+id+'.ClientSupplierInquieyExcel';
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
						id:'down',
						icon : 'down',
						text : '批量下载',
						click: function(){
								var iframeId="inquiryiframe";
								PJ.topdialog(iframeId, '选择询价单', '<%=path%>/purchase/supplierinquiry/toSearchInquiry',
									undefined,function(item,dialog){dialog.close();}, 1200, 700, true);
						}
					
				 }, 
				 {
						id:'add',
						icon : 'add',
						text : '发起邮件爬虫',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
				 		    var id = ret["id"];
							$.ajax({
								url: '<%=path%>/purchase/supplierinquiry/crawEmail?id='+id,
								type: "POST",
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
				 {
						id:'view',
						icon : 'view',
						text : '查询即将发送爬虫邮件',
						click: function(){
							var iframeId="ideaIframe12";
								PJ.topdialog(iframeId, '查询即将发送爬虫邮件', '<%=path%>/purchase/supplierinquiry/toCrawlEmailSupplierList',
										undefined,function(item,dialog){dialog.close();}, 1100, 600, true);
						}
				 },
				 {
						id:'add',
						icon : 'add',
						text : '手动发起爬虫',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
				 		    var id = ret["id"];
				 		    var crawlIframeId = "crawlframe";
				 		    PJ.topdialog(crawlIframeId, '选择爬虫网站', '<%=path%>/purchase/supplierinquiry/selectSupplierToCrawl',
									function(item, dialog){
				 		    			suppliers = top.window.frames[crawlIframeId].getFormData();
				 		    			if(suppliers != undefined && suppliers != ""){
				 		    				$.ajax({
												url: '<%=path%>/purchase/supplierinquiry/commitCrawl?id='+id+'&suppliers='+suppliers,
												type: "POST",
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
				 		    				PJ.warn("请选择供应商！");
				 		    			}
										dialog.close();	
									},function(item,dialog){dialog.close();}, 300, 200, true,"发起");
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
		items : [
				 {
						id:'add',
						icon : 'add',
						text : '黑名单标注',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid2);
							var partNumber = ret["partNumber"];
							var id = ret["id"];
							var iframeId="ideaIframe5";
							PJ.topdialog(iframeId, '商品库件号黑名单标注', '<%=path%>/sales/clientinquiry/toisblacklist?partNumber='+partNumber,
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getData();	 							
										 $.ajax({
												url: '<%=path%>/sales/clientinquiry/editBlackList?id='+id,
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
									PJ.grid_reload(grid2);}
								   ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"黑名单标注");
						}
				 }<%-- , 
				 {
						id:'edit',
						icon : 'edit',
						text : '推送询价邮件',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
				 		    var id = ret["id"];
				 		    var postData = getEmailData()
				 		    postData.id = id
							$.ajax({
								url: '<%=path%>/purchase/supplierinquiry/sendEmailBySelect',
								type: "POST",
								data:postData,
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
				 } --%>
				 
						 
		         ]
	});
	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/purchase/supplierinquiry/listinquiry',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-102,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "ci.update_timestamp",
		sortorder : "desc",
		pager: "#pager1",
		onSelectRow:function(rowid,status,e){
			onSelect();
		},
		colNames :["id","询价单号","客户询价单号","询价日期","截至日期","实际截标时间","状态","期限","邮件爬虫","备注","更新时间"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("quoteNumber", {sortable:true,width:130,align:'left'}),
		           PJ.grid_column("sourceNumber", {sortable:true,width:170,align:'left'}),
		           PJ.grid_column("inquiryDate", {sortable:true,width:90,align:'left'}),
		           PJ.grid_column("deadline", {sortable:true,width:90,align:'left'}),
		           PJ.grid_column("realDeadline", {sortable:true,width:90,align:'left'}),
		           PJ.grid_column("inquiryStatusValue",{sortable:true,width:80,align:'left'}),
		           PJ.grid_column("overdue", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("crawlEmail", {sortable:true,width:60,align:'left',
		        		formatter:statusFormatter 
		           }),
		           PJ.grid_column("remark",{sortable:true,width:250,align:'left'}),
		           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'})
		           ]
	});
	
	grid2 = PJ.grid("list2", {
		rowNum: 100,
		url:'<%=path%>/sales/clientinquiry/elementData?id=0',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-142,
		autoSrcoll:true,
		multiselect:true,
		pager: "#pager2",
		aftersavefunc:function(rowid,result){
			var responseJson=result.responseJSON;
			if(responseJson.success==true){
				var rowData=grid2.getRowData(rowid);
				var isBlacklist=rowData.isBlacklist;
				if(isBlacklist=="是"){
				var partNumber=rowData.partNumber;
				var iframeId="ideaIframe5";
				PJ.topdialog(iframeId, '商品库件号黑名单标注', '<%=path%>/sales/clientinquiry/toisblacklist?partNumber='+partNumber,
						function(item,dialog){
							 var postData=top.window.frames[iframeId].getData();	 							
							 $.ajax({
									url: '<%=path%>/sales/clientinquiry/editBlackList',
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
						PJ.grid_reload(grid);}
					   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"黑名单标注");
				}
			}
		},
		<%-- editurl:'<%=path%>/sales/clientinquiry/editisblacklist',
		inLineEdit: true, --%>
		editurl:'<%=path%>/sales/clientinquiry/editisblacklist',
		inLineEdit: false,
		sortname : "cie.item",
		onSelectRow:function(rowid,status,e){
			var ret = PJ.grid_getSelectedData(grid2);
			var isBlacklist = ret["isBlacklist"];
			$("#isBlacklist").val(isBlacklist);
		},
		//shrinkToFit:false,
		colNames :["id","序号","件号","另件号","描述","单位","数量","状态","是否黑名单","MSN","BSN","来源","备注","更新时间"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("item", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
		           PJ.grid_column("alterPartNumber", {sortable:true,width:170,align:'left'}),
		           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("unit", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("amount",{sortable:true,width:60,align:'left'}),
		           PJ.grid_column("conditionCode",{sortable:true,width:40,align:'left'}),
		           PJ.grid_column("isBlacklist",{sortable:true,width:40,align:'left',editable:true,
						edittype:"select",formatter:"",
						editoptions:{value:
							function(){
							var arr=["否","是"];
							var isBlacklist=$("#isBlacklist").val();
							if(isBlacklist==arr[0]){
								return "0:否;1:是";
							}else if(isBlacklist==arr[1]){
								return "1:是;0:否";
							}
						}
						},
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
		           PJ.grid_column("cageCode", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("bsn",{sortable:true,width:100,align:'left'}),
		           PJ.grid_column("source",{sortable:true,width:60,align:'left'}),
		           PJ.grid_column("remark", {sortable:true,width:200,align:'left'}),
		           PJ.grid_column("updateTimestamp",{sortable:true,width:120,align:'left'})
		           ]
	});
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/sales/clientinquiry/elementData?id='+id+'&location='+'supplier');
	};
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/listinquiry');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/listinquiry');
		
	});
	
	function statusFormatter(cellvalue, options, rowObject){
		var crawlEmail = rowObject["crawlEmail"];
		
		switch (crawlEmail) {
			case 0:
				return "";
				break;
				
			case 1:
				return "已发起";
				break;
				
			default: 
				return cellvalue;
				break; 
		}
	}
	
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
	
	$("#submit").click(function(){
		var id =$("#id").val();
		var url = '<%=path%>/purchase/supplierinquiry/uploadExcel?editType='+ getFormData().toString();
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
	
	function getEmailData(){
		var postData = {};
		var ids =  PJ.grid_getMutlSelectedRowkey(grid2);
		ids = ids.join(",");
		if(ids.length>0){
			postData.ids = ids;
		}
		return postData;
	};
	
});

function trim(str){
	return $.trim(str);
}
</script>
<style>
	#cb_list1{
		margin:0
	}
	#cb_list2{
		margin:0
	}
</style>
</head>

<body>
<div id="layout1">
	<div position="center">
	<input type="text" name="isBlacklist" id="isBlacklist" class="hide"/>
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>询价单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="quoteNumber" alias="ci.quote_number or ci.source_number" oper="cn"/> </p></form:right>
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
								<form:left><p>询价日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ci.inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ci.inquiry_date" oper="lt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p>截至日期：</p></form:left>
								<form:right><p><input id="deadlineStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'deadlineEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="deadlineStart" alias="ci.deadline" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="deadlineEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'deadlineStart\')}',dateFmt:'yyyy-MM-dd'})" name="deadlineEnd" alias="ci.deadline" oper="lt"/> </p></form:right>
							</form:column>
							
							<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
							</form:column>
						</form:row>
						<%-- <form:row columnNum="1">
							<form:column width="120">
							<form:left><p style="line-height: 30px;">匹配件号excel导入</p></form:left>
							<form:right>
								<p><input type="file" value="" id="file" name="file"/>&nbsp;
								   <input type="button" id="submit" value="上传"/>
								</p>
							</form:right>
							</form:column>
						</form:row> --%>
					</div>
				</form>
			</div>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
	<div position="centerbottom" title="">
		<div id="toolbar2"></div>
			<table id="list2"></table>
			<div id="pager2"></div>
	</div>
</div>
</body>
</html>