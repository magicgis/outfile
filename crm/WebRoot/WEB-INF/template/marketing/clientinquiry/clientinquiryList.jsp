<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>销售-客户询价管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	var conditions;
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 270,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		var clientInquiryId = 0;
		
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [{
						id:'add',
						icon : 'add',
						text : '新增',
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
																PJ.grid_reload(grid);
																dialog.close();
																var id = result.message.split(',')[0]
																var quoteNumber = result.message.split(',')[1]
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
																								PJ.grid_reload(grid);
																								PJ.grid_reload(grid2);
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
							id:'modify',
							icon : 'modify',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '销售-修改客户询价', '<%=path%>/sales/clientinquiry/editMessage?id='+id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 PJ.ajax({
															url: '<%=path%>/sales/clientinquiry/edit',
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
							id:'view',
							icon : 'view',
							text : '明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe3";
									PJ.topdialog(iframeId, '销售-客户询价明细管理 ', '<%=path%>/sales/clientinquiry/element?id='+id,
											undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '新增客户报价',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe4";
								var zt = ret["inquiryStatusValue"];
								//if(zt=='供应商已报'){
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
								//}else{
									//PJ.warn("只有供应商报价以后才能进行操作！");
								//}
							}
					 },
					 {
							id:'view',
							icon : 'view',
							text : '文件管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/sales/clientinquiry/file?id='+id,
										undefined,function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
							}
					 },
					 {
							id:'view',
							icon : 'view',
							text : '竞争对手报价管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe6";
								PJ.topdialog(iframeId, '销售-竞争对手报价管理', '<%=path%>/sales/clientinquiry/toTender?id='+id,
										undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
							}
					 },
					 /* {
							id:'down',
							icon : 'down',
							text : '下载',
							click: function(){
								    var ret = PJ.grid_getSelectedData(grid);
						 		    var id = ret["id"];
									//根据具体业务提供相关的title
									var title = 'excel管理';
									var inquiryStatusValue = ret["inquiryStatusValue"];
									//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
									//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
									var businessKey = 'client_inquiry.id.'+id+'.ClientInquieyExcel';
									if(inquiryStatusValue!='未询价'&&inquiryStatusValue!='已询价'&&inquiryStatusValue!='供应商已报'){
										PJ.excelDiaglog({
											data:'businessKey='+businessKey,
											title: title,
											add:true,
											remove:true,
											download:true
										});
									}else{
										PJ.warn("只有生成客户报价单后才可以下载！");
									}
							}
					 } */
					 {
							id:'add',
							icon : 'add',
							text : '客户预报价',
							click: function(){
								
								var iframeId="airtypeiframe";
								PJ.topdialog(iframeId, ' 客户预报价 ', '<%=path%>/sales/clientinquiry/weatherQuote',
										function(item, dialog){
									 var postData=top.window.frames[iframeId].getFormData();	 
									 if(postData){
									 $.ajax({
										 url: '<%=path%>/sales/clientinquiry/weatherQuoteData',
											data: postData,
											type: "POST",
											loading: "正在处理...",
											success: function(result){
													if(result.success){
														var iframeId="weatherquoteiframe2";
														PJ.topdialog(iframeId, ' 预报价 ', '<%=path%>/sales/clientinquiry/weatherQuotePage?id='+result.message,
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
					 }
					 ,
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
			items : [<%-- {
						id:'add',
						icon : 'add',
						text : '批量新增',
						click: function(){
								var iframeId="ideaIframe12";
								PJ.topdialog(iframeId, '新增客户询价明细', '<%=path%>/sales/clientinquiry/addelement?id='+${id },
										function(item,dialog){
											var data = top.window.frames[iframeId].getSubmit();
											if(data.flag==true){
								            	PJ.success(data.message);
								        	}
											if(data.flag==false){
								        		PJ.error(data.message);
								        	}
											if(data.flag=='error'){
								        		PJ.error("上传异常！");
								        	}
											dialog.close();
											PJ.grid_reload(grid);
										},function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"上传");
						}
					 
					 }, --%>
					 {
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe5";
							var ret = PJ.grid_getSelectedData(grid);
							 var id = ret["id"];
							PJ.topdialog(iframeId, '新增客户询价明细', '<%=path%>/sales/clientinquiry/toAddElement?id='+id,
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getFormData();
										 PJ.ajax({
												url: '<%=path%>/sales/clientinquiry/addElement?id='+id,
												data: postData,
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
									PJ.grid_reload(grid2);}
								   ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
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
										grid2.setGridHeight(PJ.getCenterHeight()-440);
									}else{
										$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
										grid2.setGridHeight(PJ.getCenterHeight()-400);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/sales/clientinquiry/listData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "ci.update_timestamp",
			sortorder : "desc",
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			colNames :["id","询价单号","客户询价单号","询价日期","截至日期","实际截标时间","状态","询报比","总价","期限","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("sourceNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("inquiryDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("deadline", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("realDeadline", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("inquiryStatusValue",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("proportion", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("overdue",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:250,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'<%=path%>/sales/clientinquiry/elementData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager2",
			editurl:'<%=path%>/sales/clientinquiry/editElement',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
			},
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var conditionCode = ret["conditionCode"];
				var conditionId = ret["conditionId"];
				$.ajax({
					type: "POST",
					url:'<%=path%>/sales/clientinquiry/getConditions?conditionCode='+conditionCode,
					success:function(result){
						if(result.success){
							conditions =/* conditionId+":"+conditionCode+";"+  */result.message;
						}
					}
				});
			},
			sortname : "cie.item",
			colNames :["id","序号","件号","CSN","另件号","描述","单位","数量","售前状态","黑名单","目标价","备注","更新时间","是否主件号","主件号","状态","状态ID","来源","msn","BSN"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:130,editable:true,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("alterPartNumber", {sortable:true,width:130,editable:true,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:180,editable:true,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:40,editable:true,align:'left'}),
			           PJ.grid_column("amount",{sortable:true,width:40,editable:true,align:'left'}),
			           PJ.grid_column("elementStatusValue",{sortable:true,width:60,align:'left'}),
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
			           PJ.grid_column("aimPrice",{sortable:true,width:40,editable:true,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:200,editable:true,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("isMain",{sortable:true,width:60,align:'left',
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
			           PJ.grid_column("mainPartNumber",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("conditionCode",{sortable:true,width:40,align:'left',editable:true,
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
									return conditions;        			   
		        		   		}
							}
			           }),
			           PJ.grid_column("conditionId",{sortable:true,width:40,align:'left',hidden:true}),
			           PJ.grid_column("source",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("cageCode", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("bsn",{sortable:true,width:100,align:'left'})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			$("#id").val(id);
			PJ.grid_search(grid2,'<%=path%>/sales/clientinquiry/elementData?id='+id);
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
		    	PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		/* $(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		}); */
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
		
		$("#searchForm2").change(function(){
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/sales/clientinquiry/test',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						
					}
				}
			});
		});
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/sales/clientinquiry/uploadExcel?id='+getFormData().id+'&editType='+ getFormData().toString();
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
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	PJ.grid_reload(grid2);
		            	$("#uploadBox").toggle(function(){
							$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
							grid2.setGridHeight(PJ.getCenterHeight()-310);
		            	});
	            	}else{
	            		PJ.hideLoading();
	            		$("#uploadBox").toggle(function(){
							$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
							grid2.setGridHeight(PJ.getCenterHeight()-310);
		            	});
	            		iframeId = 'errorframe'
	            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/sales/clientinquiry/toErrorMessage',
	            				undefined,function(item,dialog){
			            			$.ajax({
										url: '<%=path%>/sales/clientinquiry/deleteMessage',
										type: "POST",
										loading: "正在处理...",
										success: function(result){
										}
									});
	            					dialog.close();}, 1000, 500, true);
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
	            	PJ.error("上传异常！");
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
	
	function trim(str){
		return $.trim(str);
	}
</script>
<style>
/* th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
}  */
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