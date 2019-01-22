<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title> </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
var code='${cageCode}';
$(function(){
	//var role = "${cols}";
	var data =[];
	var partNumber,check;
	layout = $("#layout1").ligerLayout({
		centerBottomHeight: 270,
		onEndResize:function(e){
		  GridResize();
		}
	});
	if(code==''){
	$("#toolbar").ligerToolBar({
		items : [
					{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '新增商品库', '<%=path%>/stock/search/toAdd',
										function(item, dialog){
											
											var nullAble=top.window.frames[iframeId].validate();
											if(nullAble){
												var postData=top.window.frames[iframeId].getFormData();
												if(postData!=null){
													$.ajax({
														url: '<%=path%>/stock/search/saveAdd',
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
								var bsn = ret["bsn"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '修改商品库', '<%=path%>/stock/search/toEdit?bsn='+bsn,
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												if(postData!=null){
													 $.ajax({
															url: '<%=path%>/stock/search/saveEdit',
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
							text : '文件管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var bsn = ret["bsn"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/stock/search/file?bsn='+bsn,
										undefined,function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
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
										grid.setGridHeight(PJ.getCenterHeight()-202);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-102);
									}
								});
							}
					 },
					 {
							id:'add',
							icon : 'search',
							text : '获取厂商信息',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								if(ret==undefined){
									return;
								}
								var cageCode = ret["cageCode"];
								var iframeId="ideaIframe";
									PJ.topdialog(iframeId, '按cageCode查询 ', '<%=path%>/stock/search/listByCage?cageCode='+cageCode,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
							}
					 },
					 {	icon : 'logout',
							text : '导出excel',
							click : function() {
									var searchString = getSearchString();
									if("cn" == $("#check").val()){
										searchString = searchString + " and t.short_part_num LIKE "+"'%"+$("#partNumber").val()+"%'"; 
									}else if ("eq" == $("#check").val()) {
										searchString =searchString + " and t.short_part_num = "+"'"+$("#partNumber").val()+"'";
									}else if ("rcn" == $("#check").val()) {
										searchString =searchString + " and t.short_part_num = "+"'"+$("#partNumber").val()+"%'";
									}else if ("ecn" == $("#check").val()) {
										searchString =searchString + " and t.short_part_num = "+"'%"+$("#partNumber").val()+"'";
									}
									grid.jqGrid('setGridParam', {
										postData : {
											searchString : searchString
										}
									});
									PJ.grid_export("list1");
								}
					 },
					 {
							id:'upload',
							icon : 'search',
							text : '收起excel上传',
							click: function(){
								$("#uploadBox").toggle(function(){
									var display = $("#uploadBox").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='upload'] > span").html("收起excel上传");
										grid.setGridHeight(PJ.getCenterHeight()-242);
									}else{
										$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
										grid.setGridHeight(PJ.getCenterHeight()-202);
									}
								});
							}
					}				 
		         ]
	});
	}else{
		$("#toolbar").ligerToolBar({
			items : [
					 {
							id:'search',
							icon : 'search',
							text : '收起搜索',
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
	}
	$("#exact").change(function(){
		if($("#exact").prop("checked")){
			$("#check").val("eq");
		}else if(!$("#exact").prop("checked")){
			$("#check").val("cn");
		}
	});
	
	$("#model").blur(function(){
		if($("#model").val() == '1'){
			$("#check").val("eq");
		}else if($("#model").val() == '2'){
			$("#check").val("cn");
		}if($("#model").val() == '3'){
			$("#check").val("rcn");
		}if($("#model").val() == '4'){
			$("#check").val("ecn");
		}
	});

	
	grid = PJ.grid("list1", {
		rowNum: '50',
		url:'<%=path%>/stock/search/getDataOnce',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-202,
		autoSrcoll:true,
		shrinkToFit:false,
		sortname : "cageId",
		sortorder : "desc",
		pager: "#pager1",
		datatype:"local",
		data:data,
		onSelectRow:function(rowid,status,e){
			onSelect();
		},
		colNames :["BSN","件号","描述","黑","附件","MSN","厂商","类型","NSN编号","有效期","ata chapter section","weight","dimentions","country of origin","eccn","scheduleBCode","category no","usml","hazmat code","HS Code","remark"],
		colModel :[
		           	PJ.grid_column("bsn", {key:true, sortable:true, width:120,align:'left'}),
		        	PJ.grid_column("partNum", {sortable:false,width:120,align:'left'}),
		           	PJ.grid_column("partName", {sortable:false,width:180,align:'left'}),
		            PJ.grid_column("isBlacklistValue",{sortable:true,width:40,align:'left',
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
		           	PJ.grid_column("haveAttachment",{sortable:true,width:40,align:'left',
						  formatter: function(value){
								if (value==1) {
									return "有";
								}else {
									return "";
								}
							}
		           	}),
		           	PJ.grid_column("msn", {sortable:false,width:80,align:'left'}),	
		        	PJ.grid_column("manName", {sortable:false,width:150,align:'left'}),
		        	PJ.grid_column("code", {sortable:false,width:40,align:'left'}),
		           	PJ.grid_column("nsn", {sortable:false,width:150,align:'left'}),
		           	PJ.grid_column("shelfLife", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("ataChapterSection", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("weight", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("dimentions", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("countryOfOrigin", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("eccn", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("scheduleBCode", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("categoryNo", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("usml", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("hazmatCode", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("hsCode", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("remark", {sortable:false,width:150,align:'left'})
		           ]
	});
	
	grid2 = PJ.grid("list2", {
		rowNum: 20,
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight(),
		autoSrcoll:true,
		shrinkToFit:false,
		pager: "#pager2",
		sortname : sorts('${cols}'),
		sortorder : "desc",
		colNames :colNames('${cols}'),
		colModel :colModel('${cols}')
	});
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var bsn = ret["bsn"];
		$("#id").val(id);
		var url = getUrl('${cols}');
		PJ.grid_search(grid2,url+bsn);
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
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	var searchString = getSearchString();
			var postData = {};
			postData.searchString = searchString;
			partNumber = $("#partNumber").val();
			check = $("#check").val();
			var shelfLife = $("#shelfLife").val();
			if(searchString != '' || partNumber != ''){
				if($("#partNumber").val() != ""){
					PJ.ajax({
						url: '<%=path%>/stock/search/getDataOnce?partNum='+$("#partNumber").val()+'&check='+$("#check").val()+'&shelfLife='+shelfLife,
						type: "POST",
						loading: "正在处理...",
						data: postData,
						dataType: "json",
						success: function(result){
								if(result.success){
									data = eval(result.message)[0];
									//PJ.grid_reload(grid);
									$("#list1").jqGrid('clearGridData');
									$("#list1").jqGrid('setGridParam',{  // 重新加载数据
									      datatype:'local',
									      data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
									      page:1
									}).trigger("reloadGrid");
								}	
						}
					});
				}else{
					PJ.ajax({
						url: '<%=path%>/stock/search/getDataOnce?shelfLife='+shelfLife,
						type: "POST",
						loading: "正在处理...",
						data: postData,
						dataType: "json",
						success: function(result){
								if(result.success){
									data = eval(result.message)[0];
									//PJ.grid_reload(grid);
									$("#list1").jqGrid('clearGridData');
									$("#list1").jqGrid('setGridParam',{  // 重新加载数据
									      datatype:'local',
									      data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
									      page:1
									}).trigger("reloadGrid");
								}	
						}
					});
				}
			}else{
				PJ.warn("没有条件不能查询")
			}
	    }  
	});
	
	//搜索
	$("#searchBtn").click(function(){
		<%-- if($("#partNumber").val() != ""){
			PJ.grid_search(grid,'<%=path%>/stock/search/findStockPage?partNum='+$("#partNumber").val()+'&check='+$("#check").val());
		}else{
			PJ.grid_search(grid,'<%=path%>/stock/search/findStockPage');
		} --%>
		var searchString = getSearchString();
		var postData = {};
		postData.searchString = searchString;
		partNumber = $("#partNumber").val();
		check = $("#check").val();
		var shelfLife = $("#shelfLife").val();
		if(searchString =! '' || partNumber != ''){
			if($("#partNumber").val() != ""){
				PJ.ajax({
					url: '<%=path%>/stock/search/getDataOnce?partNum='+$("#partNumber").val()+'&check='+$("#check").val()+'&shelfLife='+shelfLife,
					type: "POST",
					loading: "正在处理...",
					data: postData,
					dataType: "json",
					success: function(result){
							if(result.success){
								data = eval(result.message)[0];
								//PJ.grid_reload(grid);
								$("#list1").jqGrid('clearGridData');
								$("#list1").jqGrid('setGridParam',{  // 重新加载数据
								      datatype:'local',
								      data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
								      page:1
								}).trigger("reloadGrid");
							}	
					}
				});
			}else{
				PJ.ajax({
					url: '<%=path%>/stock/search/getDataOnce?shelfLife='+shelfLife,
					type: "POST",
					loading: "正在处理...",
					data: postData,
					dataType: "json",
					success: function(result){
							if(result.success){
								data = eval(result.message)[0];
								//PJ.grid_reload(grid);
								$("#list1").jqGrid('clearGridData');
								$("#list1").jqGrid('setGridParam',{  // 重新加载数据
								      datatype:'local',
								      data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
								      page:1
								}).trigger("reloadGrid");
							}	
					}
				});
			}
		}else{
			PJ.warn("没有条件不能查询")
		}
		
	});
	
	function getSearchString() {
		var flag = true, searchString = "";
		$(document).find("[alias]").each(function(i, e) {
			if (!$(e).val() || !$.trim($(e).val()))
				return true;
			
			var result = checkRule(e);
			flag = result[0];
			if (flag) {
				var value = $(e).val();
				//var name = $(e).attr("prefix");
				//if (name) {
					//name += ".";
				//}else{
					//name="";
				//}
				var alias = $(e).attr("alias").split(" or ");
				if(alias.length>=2){
					if(!searchString){
						searchString += " ( ";
					}else{
						searchString += " and ( ";
					}
					
					$(alias).each(function(index){
						//name += alias[index];
						var str = getRuleString(alias[index], result[1], value, result[2]);
						if (searchString && str){
							if(searchString.substring(searchString.length-2, searchString.length) != "( "){
								searchString += " OR ";
							}
						}
						/*if(!searchString){
							searchString += " ( ";
						}*/
						searchString += str;
					});
					searchString += " ) ";
				}else{
					//name += $(e).attr("alias");
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
	
	$("#partNum").blur(function(){
		var text = $("#partNumber").val();
		$("#partNumber").val(trim(text));
	});
	
	$("#cageId").blur(function(){
		var text = $("#cageId").val();
		$("#cageId").val(trim(text));
	});
	
	$("#nsnId").blur(function(){
		var text = $("#nsnId").val();
		$("#nsnId").val(trim(text));
	});
	 
	$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
	
	//改变窗口大小自适应
	$(window).resize(function() {
		grid.setGridWidth(PJ.getCenterWidth());
		var display = $("#searchdiv").css("display");
		if(display=="block"){
			grid.setGridHeight(PJ.getCenterHeight()-202);
		}else{
			grid.setGridHeight(PJ.getCenterHeight()-142);
		}
	});
	
	$("#submit").click(function(){
		var url = '<%=path%>/stock/search/tpUploadExcel?editType='+ getFormData().toString();
		PJ.showLoading("上传中....");
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
            		PJ.hideLoading();
	            	PJ.success(da.message);
	            	PJ.grid_reload(grid);
	            	$("#uploadBox").toggle(function(){
						$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
						grid.setGridHeight(PJ.getCenterHeight()-202);
	            	});
            	}else{
            		PJ.hideLoading();
            		$("#uploadBox").toggle(function(){
						$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
						grid.setGridHeight(PJ.getCenterHeight()-202);
	            	});
            		iframeId = 'errorframe'
            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/stock/search/toUnknow',
            				undefined,function(item,dialog){
		            			$.ajax({
									url: '<%=path%>/stock/search/deleteData',
									type: "POST",
									loading: "正在处理...",
									success: function(result){
									}
								});
            					dialog.close();}, 1000, 500, true);
            	}
   				
            },  
            error: function (data, status, e) { 
            	PJ.error("上传异常！");
            }  
        });
		
	 });
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};	
	
	function getUpdateData(){
		 var postData = {};
		 postData.data = $("#updateform").serialize();
		 return postData;
	};
	
	$("#update").click(function(){
		var id =$("#id").val();
		var url = '<%=path%>/stock/search/updateType?editType='+ getFormData().toString();
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
	            	PJ.success(da.message);
	            	PJ.grid_reload(grid);
	            	PJ.grid_reload(grid2);
	            	$("#uploadBox").toggle(function(){
						$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
						grid2.setGridHeight(PJ.getCenterHeight()-260);
	            	});
            	}else{
            		PJ.error("上传异常！");
            	}
            },  
        });
	 });
	
	$("#check").click(function(){
		var id =$("#id").val();
		var url = '<%=path%>/stock/search/updateType?editType='+ getFormData().toString();
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
            },  
        });
	 });
	
	
	function getUrl(roleId){
		if(roleId == 'market' || roleId == 'other'){
			return '<%=path%>/stock/search/systeminformationformarket?bsn=';
		}else if(roleId == 'purchase'){
			return  '<%=path%>/stock/search/systeminformationforpurchase?bsn=';
		}
	};
	
	function colNames(roleId){
		if(roleId == 'market' || roleId == 'other'){
			return ["item","csn","件号","描述","客户","询价单号","询价日期","单位","数量","件号","描述","供应商","供应商询价单号","报价日期","单位","数量","币种","价格","备注","报价状态","客户报价日期","客户数量","客户价格","订单号","订单日期","数量","价格","状态","备注","竞争对手报价"];
		}else if(roleId == 'purchase'){
			return ["id","item","csn","件号","描述","客户","询价单号","询价日期","单位","数量","件号","描述","供应商","价格","单位","数量","币种","证书","状态","备注","报价状态","供应商询价单号","报价日期","供应商","订单号","订单日期","截至日期","数量","单价","总价"];
		}
	};
	
	function colModel(roleId){
		if(roleId == 'market' || roleId == 'other'){
			return [   PJ.grid_column("item", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("inquiryPartNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("inquiryDescription", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("clientCode", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("inquiryDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("inquiryUnit", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("inquiryAmount", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("quoteDescription", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("supplierInquiryQuoteNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("supplierQuoteDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("quoteUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quoteAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("supplierCurrencyValue", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("supplierBasePrice", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("quoteRemark", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("clientQuoteDate", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("clientQuoteAmount", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientBasePrice", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:130,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("clientOrderDate", {sortable:true,width:120,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("clientOrderAmout", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientOrderBasePrice", {sortable:true,width:120,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("orderStatus", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("orderRemark", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("competitorPrice", {sortable:true,width:90,align:'left'})
			           ];
		}else if(roleId == 'purchase'){
			return [   PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("inquiryPartNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("inquiryDescription", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("clientCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,width:200,align:'left'}),
			           PJ.grid_column("inquiryDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("inquiryUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("inquiryAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("quoteDescription", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("supplierBasePrice", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("quoteUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quoteAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("supplierCurrencyValue", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("certificationCode", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("quoteRemark", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("supplierInquiryQuoteNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("supplierQuoteDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("supplierOrderCode", {sortable:true,width:70,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderNumber", {sortable:true,width:120,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderDate", {sortable:true,width:120,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderDeadline", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("supplierOrderAmout", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("supplierOrderBasePrice", {sortable:true,width:120,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderTotalPrice", {sortable:true,width:130,align:'left'}),
			           ];
		}
	};
	
	function sorts(roleId){
		if(roleId == 'market' || roleId == 'other'){
			return "co.order_date desc,cq.quote_date desc,sq.quote_date desc,sqe.supplier_quote_status_id";
		}else if(roleId == 'purchase'){
			return  "so.order_date desc,sq.quote_date desc,sqe.supplier_quote_status_id";
		}
		
	}
	
	//二级表头
	
		var roleId = '${cols}';
		if(roleId == 'market' || roleId == 'other'){
			$( "#list1" ).jqGrid( 'setGroupHeaders' , {
				useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
				groupHeaders : [ {
									startColumnName :  'item' ,  // 对应colModel中的name
									numberOfColumns : 9,  // 跨越的列数
									titleText :  '客户询价'
								 },
								 {
										startColumnName :  'quotePartNumber' ,  // 对应colModel中的name
										numberOfColumns : 11,  // 跨越的列数
										titleText :  '供应商报价'
								 },
								 {
									startColumnName :  'clientQuoteDate' ,  // 对应colModel中的name
									numberOfColumns : 3,  // 跨越的列数
									titleText :  '客户报价'
								 },
								 {
									startColumnName :  'clientOrderNumber' ,  // 对应colModel中的name
									numberOfColumns : 6,  // 跨越的列数
									titleText :  '客户订单'
								 }
								]
			});
		}else if(roleId == 'purchase'){
			$( "#list1" ).jqGrid( 'setGroupHeaders' , {
				useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
				groupHeaders : [ {
									startColumnName :  'item' ,  // 对应colModel中的name
									numberOfColumns : 9,  // 跨越的列数
									titleText :  '客户询价'
								 },
				                 {
									startColumnName :  'quotePartNumber' ,  // 对应colModel中的name
									numberOfColumns : 13,  // 跨越的列数
									titleText :  '供应商报价'
								 },
								 {
									startColumnName :  'supplierOrderCode' ,  // 对应colModel中的name
									numberOfColumns : 7,  // 跨越的列数
									titleText :  '供应商订单'
								 }
								]
			});
		};
	
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
	};
	
	/* for(var i=0;i<rows.length;i++){  
        $("#list1").jqGrid('addRowData',i+1,rows[i]);  
    }; */
	var reader = {
			  root: function(obj) { return data.rows; },
			  page: function(obj) { return data.page; },
			  total: function(obj) { return data.total; },
			  records: function(obj) { return data.records; },
			  repeatitems: false
			  };
	/* grid.setGridParam({data: data.rows, localReader: reader}).trigger('reloadGrid'); */
	
});
function trim(str){
	return $.trim(str);
}
</script>
</head>

<body>
<div id="layout1">
	<div position="center" title="${title}">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 100px;display: unblock;">
			<form id="searchForm">
				<div class="search-box">
				<%-- <c:if test="${ empty cageCode}"> --%>
					<form:row columnNum="5">
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="partNumber" class="text" type="text" name="partNumber"/> </p></form:right>
						<%-- </form:column>
							<form:column>
							<form:left>模糊查询</form:left>
							<form:right><input type="checkbox" name="exact" id="exact"/></form:right>
							<input type="text" name="check" id="check" class="hide"/>
						</form:column> --%>
						</form:column>
							<form:column>
							<form:left>查询模式</form:left>
							<form:right><select id="model">
											<option value="1">精确查询</option>
											<option value="2">包含</option>
											<option value="3">开头</option>
											<option value="4">结尾</option>
										</select>
							</form:right>
							<input type="text" name="check" id="check" class="hide"/>
						</form:column>
						<form:column>
							<form:left><p>件号规则搜索：</p></form:left>
							<form:right><p><input id="partNum" class="text" type="text" name="partNum" oper="regex" alias="t.part_num" 
							tooltipText="%:数字,#:字母,?:数字或字母,<br/>{数字}:个数<br/>例子:<br/>%{4}D-%{1}表示匹配4个数字加上D加上-加上1个数字的件号<br/>如:1524D-1就能查出来"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>描述：</p></form:left>
							<form:right><p><input id="partName" class="text" type="text" alias="t.part_name" name="partName" oper="cn"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>CAGE号：</p></form:left>
							<form:right><p><input id="cageId" class="text" type="text" name="cageId" oper="eq" alias="t.cage_code" value="${cageCode}"/> </p></form:right>
						</form:column>
						
					</form:row>
					<form:row columnNum="5">
						<form:column>
							<form:left><p>nsn：</p></form:left>
							<form:right><p><input id="nsn" class="text" type="text" name="nsn" oper="eq" alias="t.nsn" /> </p></form:right>
						</form:column>
						<form:column>
							<form:left>MSN标志位</form:left>
							<form:right><p><select id="msnFlag" class="text" type="text" alias="t.msn_flag" name="msnFlag" oper="eq">
												<option value="0">0</option>
												<option value="1">1</option>
												<option value="6">6</option>
											</select>
										 </p>
							</form:right>
						</form:column>
						<form:column>
							<form:left>是否有寿命</form:left>
							<form:right>
										<p>
											<select id="shelfLife" class="text" type="text" name="shelfLife">
													<option value="">全部</option>
													<option value="0">无</option>
													<option value="1">有</option>
											</select>
										</p>
							</form:right>
						</form:column>
						<form:column>
							<form:left></form:left>
							<form:right></form:right>
						</form:column>
						<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
						</form:column>
					</form:row>
					<%-- </c:if>
					<c:if test="${not empty cageCode}">
					<input id="cageId" class="hide" type="text" name="cageId" oper="cn" alias="t.cage_code" value="${cageCode}"/>
					<form:row columnNum="4">
					<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="partNum" class="text" type="text" name="partNum"/> </p></form:right>
						</form:column>
							<form:column>
							<form:left>模糊查询</form:left>
							<form:right><input type="checkbox" name="exact" id="exact"/></form:right>
							<input type="text" name="check" id="check" class="hide"/>
						</form:column>
						<form:column>
							<form:left><p>描述：</p></form:left>
							<form:right><p><input id="partName" class="text" type="text" name="partName"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>NSN号：</p></form:left>
							<form:right><p><input id="nsnId" class="text" type="text" name="nsnId" oper="cn" alias="t.nsn"/> </p></form:right>
						</form:column>
						<form:column ><form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
							</form:column>
					</form:row>
					</c:if> --%>
				</div>
			</form>
		</div>
		<div id="uploadBox" style="display: none;">
			
			 	<input type="hidden" name="id" id="id" value="${id}"/>
		   		
					<form:row columnNum="2">
						<form id="form" name="form">
						<form:column width="120">
							<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
							<form:right>
								<p><input type="file" value="" id="file" name="file"/>&nbsp;
								   <input type="button" id="submit" value="新增件号"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								   <input type="button" id="update" value="更新信息"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								   <input type="button" id="check" value="检查件号是否存在"/>
								</p>
							</form:right>
						</form:column>
						</form>
						
					</form:row>            
			 
		</div>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
	<div position="centerbottom" >
		<table id="list2" style=""></table>
			<div id="pager2"></div>
	</div>
</div>
<script type="text/javascript">
	var tooltipObj = new DHTMLgoodies_formTooltip();
	tooltipObj.setTooltipPosition('below');
	tooltipObj.setPageBgColor('#BBBBBB');
	tooltipObj.setTooltipCornerSize(15);
	tooltipObj.initFormFieldTooltip();
</script>
</body>
</html>