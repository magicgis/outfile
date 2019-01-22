<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商询价管理 </title>

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
	
	$("#toolbar").ligerToolBar({
		items : [
					{
						id:'add',
						icon : 'add',
						text : '明细',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var supplierInquiryQuoteNumber = ret["supplierInquiryQuoteNumber"];
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '采购-供应商询价明细 ', '<%=path%>/purchase/supplierinquiry/toManageElement?id='+id+'&supplierInquiryQuoteNumber='+supplierInquiryQuoteNumber,
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
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var businessKey = 'supplier_inquiry.id.'+id+'.SupplierInquieyExcel';
								PJ.excelDiaglog({
									data:'businessKey='+businessKey,
									title: title,
									add:true,
									remove:true,
									download:true
								});
						}
					}, */
					{
						id:'down',
						icon : 'down',
						text : '批量下载excel',
						click: function(){
							var iframeId="ideaIframe2";
								PJ.topdialog(iframeId, '采购-批量下载excel ', '<%=path%>/purchase/supplierinquiry/toLotsExcel',
										undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
						}
					},
					{
						id:'add',
						icon : 'add',
						text : '新增供应商报价',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"];
							var iframeId="ideaIframe3";
							<%-- $.ajax({
								url: '<%=path%>/purchase/supplierinquiry/searchOrder?id='+id,
								type: "POST",
								loading: "正在处理...",
								success: function(result){
									if(result.success){ --%>
										PJ.topdialog(iframeId, '采购-新增供应商报价 ', '<%=path%>/purchase/supplierinquiry/toAddQuote?id='+id,
												function(item, dialog){
											var postData=top.window.frames[iframeId].getFormData();	 							
											 $.ajax({
													url: '<%=path%>/purchase/supplierinquiry/saveSupplierQuote',
													data: postData,
													type: "POST",
													loading: "正在处理...",
													success: function(result){
														if(result.success){
															//PJ.success(result.message);
															PJ.grid_reload(grid);
															dialog.close();
															PJ.topdialog(iframeId, '新增报价明细', '<%=path%>/purchase/supplierinquiry/addelementafteradd?id='+result.message+'&supplierInquiryId='+id,
																	function(item, dialog){
																		var postData=top.window.frames[iframeId].getFormData();
																		var supplierQuoteId = result.message;
																		 PJ.ajax({
																				url: '<%=path%>/supplierquote/addElement?id='+result.message,
																				data: postData,
																				type: "POST",
																				loading: "正在处理...",
																				success: function(result){
																						if(result.success){ 
																							PJ.success(result.message);
																							PJ.grid_reload(grid);
																							dialog.close();
																							var iframeId2 = "checkFrame";
																							PJ.topdialog(iframeId2, '填写金额', '<%=path%>/market/clientweatherorder/toCheckTotal',
																									function(item, dialog){
																										var postData=top.window.frames[iframeId2].getFormData();
																										var validate = top.window.frames[iframeId2].validate();
																										if(validate){
																											$.ajax({
																												url: '<%=path%>/supplierquote/checkTotalByQuote?id='+supplierQuoteId,
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
																						}		
																				}
																			});
																		PJ.grid_reload(grid);
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
													 
												},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
									/* }else {
										PJ.warn(result.message);
									} */
								//}
							//});	
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
				}
		        ]
	});
	
	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight(),
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "si.update_timestamp",
		sortorder : "desc",
		inLineEdit: true,
		pager: "#pager1",
		onSelectRow:function(rowid,status,e){
			onSelect();
		},
		editurl:'<%=path%>/purchase/supplierinquiry/edit',
		colNames :["id","供应商","询价单号","供应商询价单号","询价日期","截至日期","报价比例","备注","更新时间","报价单数量"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("supplierCode", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("supplierInquiryQuoteNumber", {sortable:true,width:90,align:'left'}),
		           PJ.grid_column("supplierInquiryDate", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("supplierDeadline", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("proportion",{sortable:true,width:60,align:'left'}),
		           PJ.grid_column("remark",{sortable:true,width:120,align:'left',editable:true}),
		           PJ.grid_column("updateTimestamp", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("supplierQuoteCount", {sortable:true,width:60,align:'left'})
		           ]
	});
	
	grid2 = PJ.grid("list2", {
		rowNum: 20,
		url:'<%=path%>/purchase/supplierinquiry/ManageElementList?id=0',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight(),
		autoSrcoll:true,
		pager: "#pager2",
		//shrinkToFit:false,
		//sortname : "ci.inquiry_date",
		colNames :["id","序号","件号","另件号","描述","单位","数量","状态","更新时间"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("item", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("alterPartNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("description", {sortable:true,width:200,align:'left'}),
		           PJ.grid_column("unit", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("amount", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("conditionCode", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("updateTimestamp",{sortable:true,width:100,align:'left'})
		           ]
	});
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/ManageElementList?id='+id);
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
					$option.val(obj[i].code).text(obj[i].code);
					$("#searchForm5").append($option);
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
	    	PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/listManage');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/listManage');
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	$("#searchForm2").blur(function(){
		var text = $("#searchForm2").val();
		$("#searchForm2").val(trim(text));
	});
	
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
	
});

function getData(){
	return postData;
}

function trim(str){
	return $.trim(str);
}
</script>
</head>

<body>
<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: block;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>询价单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="quoteNumber" alias="ci.quote_number or si.quote_number" oper="cn"/> </p></form:right>
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
								<form:left>供应商：</form:left>
								<form:right><p>
													<select id="searchForm5" name="inquiryStatusCode" alias="s. CODE" oper="eq">
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
							
						</form:row>
						<form:row columnNum="5">	
							<form:column>
								<form:left><p>询价日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ci.inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ci.inquiry_date" oper="lt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p>截至日期：</p></form:left>
								<form:right><p><input id="deadlineStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'deadlineEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="deadlineStart" alias="ci.deadline" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="deadlineEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'deadlineStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="deadlineEnd" alias="ci.deadline" oper="lt"/> </p></form:right>
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
		<div position="centerbottom" title="">
		<div id="toolbar"></div>
			<table id="list2"></table>
			<div id="pager2"></div>
		</div>
	</div>

</body>
</html>