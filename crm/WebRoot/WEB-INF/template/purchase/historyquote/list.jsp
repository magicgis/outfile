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
							text : '供应商询价单',
							click: function(){
									var iframeId="ideaIframe1";
									PJ.topdialog(iframeId, '供应商询价单 ', '<%=path%>/purchase/supplierinquiry/chooseInquiry',
											undefined,function(item,dialog){dialog.close();}, 1200, 700, true);
							}
						},
						{
							id:'add',
							icon : 'add',
							text : '录入完成',
							click: function(){
								var where = getSearchString();
								var postData = {};
								postData.where = where;
								$.ajax({
									url: '<%=path%>/sales/clientinquiry/updateStatus',
									type: "POST",
									data:postData,
									loading: "正在处理...",
									success: function(result){
											if(result.success){
												PJ.success(result.message);
												PJ.grid_reload(grid1);
											} else {
												PJ.error(result.message);
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
										grid1.setGridHeight(PJ.getCenterHeight()-202);
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
							id:'add',
							icon : 'add',
							text : '新增询价单',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid1);
								var id = ret["id"];
								var postData = getData();
								postData.clientInquiryElementId = id
								$.ajax({
									url: '<%=path%>/purchase/supplierinquiry/addInquiry',
									type: "POST",
									data:postData,
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
						},
						{
								id:'up',
								icon : 'up',
								text : '件号附件上传',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid2);
									var supplierCode=ret['supplierCode'];
									$("#supplierCode").val(supplierCode);
									var quotePartNumber=ret['partNumber'];
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
									},{});
								}
							},
							{
								id:'view',
								icon : 'view',
								text : 'VAS附件',
								click: function(){
										var ret = PJ.grid_getSelectedData(grid2);
										var id = ret["id"];
										var iframeId="Pdfframe1";
										PJ.topdialog(iframeId, 'PDF ', '<%=path%>/supplierquote/toPdfList?id='+id,
												undefined,function(item,dialog){dialog.close();}, 1200, 700, true);
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
							},
							{
								id:'search',
								icon : 'search',
								text : '新增报价',
								click: function(){
									$("#uploadBox").toggle(function(){
										var display = $("#uploadBox").css("display");
										if(display=="block"){
											//$("#toolbar > div[toolbarid='search'] > span").html("新增报价");
											grid2.setGridHeight(PJ.getCenterHeight()-222);
										}else{
											// $("#toolbar > div[toolbarid='search'] > span").html("新增报价");
											grid2.setGridHeight(PJ.getCenterHeight()-92);
										}
									});
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
			colNames :["id","询价id","序号","件号","描述","另件号","数量","单位","询价单号","询价时间","截至时间","售前状态","目标价","创建人","备注","状态","类型","CSN","weight","dimentions","ata","tPartRemark"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientInquiryId", {hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:20,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("alterPartNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("inquiryDate", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("deadline", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("elementStatusValue", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("aimPrice", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("userName", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("typeCode", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("weight", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("dimentions", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("ata", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("tPartRemark", {hidden:true})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: -1,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-200,
			autoSrcoll:true,
			shrinkToFit:false,
			pager: "#pager2",
			multiselect:true,
			sortname : "a.quote_date",
			sortorder : "desc",
			/* datatype:"local",
			data:data, */
			gridComplete:function(){
				var ids = jQuery("#list2").jqGrid('getDataIDs');
				if(ids){
					for(var i=0; i<ids.length; i++){
						var rowData = jQuery("#list2").jqGrid('getRowData', ids[i]);
						var description = rowData.description;
						var quoteNumber = rowData.quoteNumber;
						var agent = rowData.isAgentValue;
						var reg=new RegExp("^竞争对手");
						if(reg.test(description)){
							$('#list2 '+'#'+ids[i]).find("td").addClass("differentFlag");
						}
						if("VAS库存" == quoteNumber){
							$('#list2 '+'#'+ids[i]).find("td").addClass("differentFlag");
						}
						if("是" == agent){
							$('#list2 '+'#'+ids[i]).find("td").addClass("GreenBG");
						}
					}
				}
				var obj = $("#list2").jqGrid("getRowData");
                for (var i = 0; i < obj.length; i++) {
                    if (/* obj[i].supplierCode == "WXC" || obj[i].supplierCode == "KC" ||  */obj[i].isCompetitor == "1") {//如果审核不通过，则背景色置于红色
                        $('#' + obj[i].id).find("td").addClass("SelectBG");
                    }
                }
				
			},
			onSelectRow:function(rowid,status,e){
				var curRowData = jQuery("#list2").jqGrid('getRowData', rowid);
				if(curRowData.supplierCode == "WXC" || curRowData.supplierCode == "KC"){
					$("#list2").jqGrid("setSelection", rowid,false);
				}
			},
			colNames :["id","报价id","询价明细id","询价id","供应商id","供应商","供应商名称","客户询价单号","件号","另件号","描述","状态id","状态","价格","证书id","证书","币种id","币种","数量","MOQ","单位","报价时间","报价有效期","周期",
			           "LOCATION","HAZMAT FEE","提货换单费","银行手续费","杂费","Core Charge","SN","TAG DATE","TAG SRC","TRACE","WARRANTY","备注","isCompetitor","counterFee","代理"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("clientQuoteId", {hidden:true,editable:true}),
			           PJ.grid_column("clientInquiryElementId", {hidden:true,editable:true}),
			           PJ.grid_column("clientInquiryId", {hidden:true,editable:true}),
			           PJ.grid_column("supplierId", {hidden:true,editable:true}),
			           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left',editable:true,
			        		   editoptions:{function(){
									return supplierids;        			   
		        		   		}
							   }
			           }),
			           PJ.grid_column("supplierName", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:100,align:'left',formatter: VasColorFormatter}),
			           PJ.grid_column("partNumber", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("alterPartNumber", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("description", {sortable:true,width:120,align:'left',editable:true,formatter: ColorFormatter}),
			           PJ.grid_column("conditionId", {sortable:true,width:80,hidden:true,align:'left',editable:true}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left',editable:true,
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return conditions;        			   
		        		   		}
						   }
			           }),
			           PJ.grid_column("price", {sortable:true,width:60,align:'left',editable:true}),
			           PJ.grid_column("certificationId", {sortable:true,width:80,hidden:true,align:'left',editable:true}),
			           PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left',editable:true,
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return certifications;        			   
		        		   		}
						   }   
			           }),
			           PJ.grid_column("currencyId", {sortable:true,width:60,align:'left',editable:true,hidden:true}),
			           PJ.grid_column("currencyValue", {sortable:true,width:40,align:'left',editable:true,
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return currencys;        			   
		        		   		}
						   }   
			           }),
			           PJ.grid_column("amount", {sortable:true,width:60,align:'left',editable:true}),
			           PJ.grid_column("moq", {sortable:true,width:60,align:'left',editable:true}),
			           PJ.grid_column("unit", {sortable:true,width:30,align:'left',editable:true}),
			           PJ.grid_column("quoteDate", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("validity",{sortable:true,width:70,align:'left',editable:true,
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   }   
			           }),
			           PJ.grid_column("leadTime", {sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("location", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("hazmatFee", {sortable:true,width:70,align:'left',editable:true,
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						   }   
			           }),
			           PJ.grid_column("feeForExchangeBill", {sortable:true,width:60,align:'left',
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			           }),
			           PJ.grid_column("bankCost", {sortable:true,width:60,align:'left',
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			           }),
			           PJ.grid_column("otherFee", {sortable:true,width:40,align:'left',
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			           }),
			           PJ.grid_column("coreCharge", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("serialNumber", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("tagDate", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("tagSrc", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("trace", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("warranty", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("remark", {sortable:true,width:150,align:'left',editable:true}),
			           PJ.grid_column("isCompetitor", {hidden:true,editable:true}),
			           PJ.grid_column("counterFee", {hidden:true,editable:true}),
			           PJ.grid_column("isAgentValue", {hidden:true,editable:true})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid1);
			var partNumber = ret["partNumber"];
			var id = ret["id"];
			var clientInquiryId = ret["clientInquiryId"];
			$("#partNumber").val(ret["partNumber"]);
			$("#description").val(ret["description"]);
			//$("#amount").val(ret["amount"]);
			$("#clientInquiryElementId").val(ret["id"]);
			$("#clientInquiryId").val(ret["clientInquiryId"]);
			$("#unit").val(ret["unit"]);
			$("#remark").val(ret["tPartRemark"]);
			<%-- PJ.ajax({
				url: '<%=path%>/supplierquote/historyQuoteOnce?partNumber='+partNumber+'&id='+id+'&clientInquiryId='+clientInquiryId,
				type: "POST",
				loading: "正在处理...",
				dataType: "json",
				success: function(result){
						if(result.success){
							data = eval(result.message)[0];
							//PJ.grid_reload(grid);
							$("#list2").jqGrid('clearGridData');
							$("#list2").jqGrid('setGridParam',{  // 重新加载数据
							      datatype:'local',
							      data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
							      page:1
							}).trigger("reloadGrid");
						}	
				}
			}); --%>
			PJ.grid_search(grid2,'<%=path%>/supplierquote/historyQuote?partNumber='+partNumber+'&id='+id+'&clientInquiryId='+clientInquiryId);
		};
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
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
				 var code = $("#code").val();
				 if(elementStatus!=""){
					 if(elementStatus == '713'){
						 PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistData?manageStatus='+manageStatus+'&code='+code);
					 }else if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!="" || beginWith!= ""){
						 PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistDataForHis?manageStatus='+manageStatus+'&beginWith='+beginWith+'&code='+code);
					 }else{
						 PJ.warn("售前状态不能单独作为搜索条件查询！");
					 }
				 }else{
					 if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!="" || beginWith!= ""){
						 PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistDataForHis?manageStatus='+manageStatus+'&code='+code+'&beginWith='+beginWith);
					 }else{
						 PJ.warn("必须填写一个搜索条件！");
					 }
				 }
		    }  
		}); 
		
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
			 var code = $("#code").val();
			 if(elementStatus!=""){
				 if(elementStatus == '713'){
					 PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistData?manageStatus='+manageStatus+'&code='+code);
				 }else if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!="" || beginWith!= ""){
					 PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistDataForHis?manageStatus='+manageStatus+'&beginWith='+beginWith+'&code='+code+'&&purchase=purchase');
				 }else{
					 PJ.warn("售前状态不能单独作为搜索条件查询！");
				 }
			 }else{
				 if(part!=""  || quoteNumber!="" || inquiryStart!="" || inquiryEnd!="" || clientCode!="" || searchForm4!=""){
					 PJ.grid_search(grid1,'<%=path%>/sales/clientinquiry/elementlistDataForHis?manageStatus='+manageStatus+'&code='+code+'&beginWith='+beginWith+'&&purchase=purchase');
				 }else{
					 PJ.warn("必须填写一个搜索条件！");
				 }
			 }
		 });
		
		 function ColorFormatter(cellvalue, options, rowObject){
			 var reg=new RegExp("^竞争对手");
			 if(reg.test(cellvalue)){
				 return "<font style='color:red'>"+cellvalue+"</font>"
			 }else{
				 return cellvalue
			 }
		 }
		 
		 function VasColorFormatter(cellvalue, options, rowObject){
			 if("VAS库存" == cellvalue){
				 return "<font style='color:red'>"+cellvalue+"</font>"
			 }else{
				 return cellvalue
			 }
		 }
		 
		 function competitorFormatter(cellvalue, options, rowObject){
				var value = rowObject["isCompetitor"];
				
				switch (value) {
					case 1:
						return "<font style='color:red'>"+cellvalue+"</font>"
						break;
					default: 
						return cellvalue;
						break; 
				}
			}
		
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
		 
		 
		//保存按钮
		$("#saveBtn").click(function(){
			 var postData = getFormData2();
			 var validate = getValidate();
			 var iframeId2 = "checkFrame";
			 var price = $("#price").val();
			 var amount = $("#amount").val();
			 if(validate){
				 if(price != ""){
					 PJ.topdialog(iframeId2, '填写金额', '<%=path%>/market/clientweatherorder/toCheckTotal',
						function(item, dialog){
						 	var postDataTotal=top.window.frames[iframeId2].getFormData();
							var validate = top.window.frames[iframeId2].validate();
							if(validate){
								postDataTotal.price = price;
								postDataTotal.amount = amount;
								$.ajax({
									url: '<%=path%>/supplierquote/checkTotal',
									data: postDataTotal,
									type: "POST",
									loading: "正在处理...",
									success: function(result){
											if(result.success){
												$.ajax({
													type: "POST",
													dataType: "json",
													data: postData,
													url:'<%=path%>/supplierquote/addOrEditQuoteBtn',
													success:function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid2);
															$("#partNumber").val("");
															$("#description").val("");
															$("#price").val("");
															$("#leadTime").val("");
															$("#amount").val("");
															$("#moq").val("");
															$("#warranty").val("");
															$("#serialNumber").val("");
															$("#tagDate").val("");
															$("#tagSrc").val("");
															$("#location").val("");
															$("#remark").val("");
															$("#supplierId").val("");
															$("#unit").val("");
															$("#trace").val("");
															$("#coreCharge").val("");
															$("#sourceNumber").val("");
															$("#validity").val($("#validitySource").val());
															$("#certificationId").empty();
															$("#conditionId").empty();
															$("#currencyId").empty();
															$("#bankCost").val("");
															$("#feeForExchangeBill").val("");
															$("#hazmatFee").val("");
															$("#otherFee").val("");
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
															PJ.grid_reload(grid1);
														}else{
															PJ.warn(result.message);
														}
													}
												});
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
							dialog.close();
						},function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 250, 100, true,"确定");
				 }else{
						$.ajax({
							type: "POST",
							dataType: "json",
							data: postData,
							url:'<%=path%>/supplierquote/addOrEditQuoteBtn',
							success:function(result){
								if(result.success){
									PJ.success(result.message);
									PJ.grid_reload(grid2);
									$("#partNumber").val("");
									$("#description").val("");
									$("#price").val("");
									$("#leadTime").val("");
									$("#amount").val("");
									$("#moq").val("");
									$("#warranty").val("");
									$("#serialNumber").val("");
									$("#tagDate").val("");
									$("#tagSrc").val("");
									$("#location").val("");
									$("#remark").val("");
									$("#supplierId").val("");
									$("#unit").val("");
									$("#trace").val("");
									$("#coreCharge").val("");
									$("#sourceNumber").val("");
									$("#validity").val($("#validitySource").val());
									$("#certificationId").empty();
									$("#conditionId").empty();
									$("#currencyId").empty();
									$("#bankCost").val("");
									$("#feeForExchangeBill").val("");
									$("#hazmatFee").val("");
									$("#otherFee").val("");
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
									PJ.grid_reload(grid1);
								}else{
									PJ.warn(result.message);
								}
							}
						});
					}
				 }else{
					 PJ.warn("数据没有填写完整！");
				 }
				
			});
		 
		 <%-- PJ.ajax({
				url: '<%=path%>/sales/clientinquiry/getDataOnce',
				type: "POST",
				loading: "正在处理...",
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
		}); --%>
		 
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
		
		$("#supplierId").blur(function(){
			var supplier = $("#supplierId").val();
			if(supplier != ""){
				$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/supplierquote/checkSupplier?supplierCode='+supplier,
					success:function(result){
						if(result.success){
							var sup = eval(result.message)[0];
							if(sup.counterFee != "" && sup.counterFee != null){
								$("#bankCost").val(sup.counterFee);
							}
							//货币信息
							$.ajax({
								type: "POST",
								dataType: "json",
								url:'<%=path%>/supplierquote/currencyType?id='+sup.currencyId,
								success:function(result){
									$("#currencyId").empty();
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
							
						}else{
							if(result.message != ""){
								PJ.warn(result.message);
							}
						}
					}
				});
			}
			
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
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
	//获取表单数据
	function getFormData2(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
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
	
	
	//-- 验证
	function getValidate(){
		return validate2({
			nodeName:"partNumber,description,amount,unit",
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
	
</script>
<style>
	#cb_list2{
		margin:0
	}
	
	.differentFlag {
        color:#FF0000 ;
    }
    .SelectBG{
        background-color:#FF6666;
    }
    .GreenBG{
        background-color:#98FB98;
    }
</style>
</head>

<body>
	<div id="layout1">
		<div position="center" >
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: block;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="5">
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="searchForm2" class="text" type="text" name="partNumber" alias="cie.part_number"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>询价单号：</p></form:left>
							<form:right><p><input id="searchForm3" class="text" type="text" name="quoteNumber" alias="ci.QUOTE_NUMBER" oper="cn"/> </p></form:right>
						</form:column>
						<form:column>
								<form:left>客户或供应商代码：</form:left>
								<form:right><p>
												<!-- <select id="searchForm1" name="clientCode" alias="c.id" oper="eq">
							        			<option value="">全部</option>
							            		</select> -->
							            		<input class="text" type="text" name="code" id="code"/>
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
						<form:column >
							<p style="padding-left:10px;">
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
			<div id="uploadBox" style="width: 100%;height: 130px;display: none;">
				<form id="addForm">
					<input class="hide" name="clientInquiryElementId" id="clientInquiryElementId" value="${clientInquiryElementId }"/>
					<input class="hide" name="clientInquiryId" id="clientInquiryId" value="${clientInquiryId }"/>
					<input name="search" id="search" class="hide"/>
					<input name="validitySource" id="validitySource" class="hide" value="<fmt:formatDate value="${validity }"  pattern="yyyy-MM-dd"/>"/>
					<table id="messageTab" style=" border-collapse:   separate;   border-spacing:   5px;">
						<tr>
							<td>供应商</td>
							<td><!-- <select name="supplierId" id="supplierId">
								</select> -->
								<input name="supplierId" id="supplierId"/>
							</td>
							<td>件号<br />报价单号</td>
							<td><input name="partNumber" id="partNumber"/><br /><input name="sourceNumber" id="sourceNumber" /></td>
							<td>描述</td>
							<td><!-- <input name="description" id="description"/> --><textarea id ="description" name="description"></textarea></td>
							<td>价格<br />周期</td>
							<td><input name="price" id="price" size="15"/><br /><input name="leadTime" id="leadTime" size="15"/></td>
							<td>币种<br />单位</td>
							<td>
								<select name="currencyId" id="currencyId">
								</select>
								<br /><input name="unit" id="unit" value="EA" size="10"/>
							</td>
							<td>数量<br />MOQ</td>
							<td><input name="amount" id="amount" size="10"/><br /><input name="moq" id="moq" size="10"/></td>
							<td>有效期</td>
							<td><input name="validity" id="validity" class="tc" value="<fmt:formatDate value="${validity }"  pattern="yyyy-MM-dd"/>"/></td>
							<td>证书</td>
							<td>
								<select name="certificationId" id="certificationId">
								</select>
								
							</td>
						</tr>
						<tr>
							<td>状态<br/>杂费</td>
							<td>
								<select name="conditionId" id="conditionId" style="width:140px">
								</select>
								<br/>
								<input name="otherFee" id="otherFee"/>
							</td>
							<td>WARRANTY<br />提货换单费</td>
							<td><input name="warranty" id="warranty"/><br /><input name="feeForExchangeBill" id="feeForExchangeBill"/></td>
							<td>SN<br />银行费</td>
							<td><input name="serialNumber" id="serialNumber"/><br /><input name="bankCost" id="bankCost"/></td>
							<td>TAG DATE<br />HAZ FEE</td>
							<td><input name="tagDate" id="tagDate" size="15"/><br /><input name="hazmatFee" id="hazmatFee" size="15"/></td>
							<td>TAG SOURCE<br />CORE C</td>
							<td><input name="tagSrc" id="tagSrc" size="15"/><br /><input name="coreCharge" id="coreCharge" size="15"/></td>
							<td>Location<br />Avi Qty</td>
							<td><input name="location" id="location" size="10"/><br /><input name="availableQty" id="availableQty" size="10"/></td>
							<td>备注<br />trace</td>
							<td><input name="remark" id="remark"/><br /><input name="trace" id="trace"/></td>
							<td></td>
							<!-- <td>替换件</td> -->
							<td> <!-- <input type="checkbox" id="replace">&nbsp;&nbsp;&nbsp; --><input class="btn btn_orange" type="button" value="新增报价" id="saveBtn"/></td>
						</tr>
					</table>
					
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