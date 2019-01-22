<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>出库询价</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '仓储-新增出库单', '<%=path%>/storage/exportpackage/toAddExport',
										function(item, dialog){
											var postData=top.window.frames[iframeId].getFormData();
											var nullAble=top.window.frames[iframeId].validate();
											if(nullAble || postData.clientId==16 || postData.clientId==65){
												$.ajax({
													url: '<%=path%>/storage/exportpackage/saveAddExport',
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
											}else{
												PJ.warn("数据还没有填写完整！");
											}
										},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '仓储-修改出库单', '<%=path%>/storage/exportpackage/toEditExport?id='+id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 $.ajax({
															url: '<%=path%>/storage/exportpackage/saveEditExport',
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
							id:'add',
							icon : 'add',
							text : '明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var exportNumber = ret["exportNumber"];
								var clientId = ret["clientId"];
								var iframeId="ideaIframe3";
									PJ.topdialog(iframeId, '仓储-出库单明细管理 ', '<%=path%>/storage/exportpackage/toElement?id='+id+'&exportNumber='+exportNumber
											+'&clientId='+clientId,
											function(item, dialog){
												$.ajax({	
													url: '<%=path%>/storage/exportpackage/check?id='+id,
													type: "POST",
													loading: "正在处理...",
													success: function(result){
															if(result.success){
																PJ.success(result.message);
															} else {
																var iframeId = "lackIframe";
																PJ.topdialog(iframeId, "缺少件清单", '<%=path%>/storage/exportpackage/toLack?id='+id+'&count='+result.message, 
																		undefined, function(item, dialog){dialog.close();}, 1100, 600, true)
															}		
													}
												});
											},function(item,dialog){dialog.close();}, 1200, 700, true,"检查");
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '新增收款',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe4";
								PJ.topdialog(iframeId, '仓储-新增收款 ', '<%=path%>/storage/exportpackage/toAddReceipt?id='+id,
										 function(item, dialog){
											 var nullAble=top.window.frames[iframeId].validate();
											 if(nullAble){
												 var postData=top.window.frames[iframeId].getFormData();
												 if(postData!=null){
													 $.ajax({
															url: '<%=path%>/storage/exportpackage/saveReceipt',
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
											 }else{
													PJ.warn("数据还没有填写完整");
											 }
										 },function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '新增客户箱单',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, '仓储-新增客户箱单 ', '<%=path%>/storage/exportpackage/toClientShip?id='+id,
										 function(item, dialog){
											 var nullAble=top.window.frames[iframeId].validate();
											 if(nullAble){
												 var postData=top.window.frames[iframeId].getFormData();
												 if(postData!=null){
													 $.ajax({
															url: '<%=path%>/storage/exportpackage/saveClientShip',
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
											 }else{
													PJ.warn("数据还没有填写完整");
											 }
										 },function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '下载',
							click: function(){
								    var ret = PJ.grid_getSelectedData(grid);
						 		    var id = ret["id"];
									//根据具体业务提供相关的title
									var title = 'excel管理';
									//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
									//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
									var businessKey = 'export_package.id.'+id+'.ExportPackageExcel';
										PJ.excelDiaglog({
											data:'businessKey='+businessKey,
											title: title,
											add:true,
											remove:true,
											download:true
										});
							}
					 },
					 {	icon : 'logout',
						text : '导出excel',
						click : function() {
								PJ.grid_export("list1");
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
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exportpackage/exportList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "ep.export_date",
			sortorder : "desc",
			colNames :["id","客户id","出库单号","出库指令单号","客户","出库日期","实际出库日期","尺寸","重量","运输单号","运输方式","费用币种","出库费","运费","币种","汇率","备注","操作人","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientId", {key:true,hidden:true}),
			           PJ.grid_column("exportNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("exportPackageInstructionsNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientCode", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("exportDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("realExportDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("size", {sortable:true,width:150,align:'left',
			        	   formatter: function(value){
								if (value) {
									return value+"CM";
								} else {
									return value;							
								}
							}   
			           }),
			           PJ.grid_column("weight", {sortable:true,width:90,align:'left',
			        	   formatter: function(value){
								if (value) {
									return value+"KG";
								} else {
									return value;							
								}
							}      
			           }),
			           PJ.grid_column("awb", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("logisticsValue", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("feeCurrencyValue", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("exportFee", {sortable:true,width:70,align:'left'/* ,
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			            */}),
			           PJ.grid_column("freight", {sortable:true,width:70,align:'left'/* ,
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			            */}),
			           PJ.grid_column("currencyValue", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("exchangeRate",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:270,align:'left'}),
			           PJ.grid_column("createUserName",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:140,align:'left'})
			           ]
		});
		
		//客户代码来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exportpackage/clientCode',
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
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportList');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var orderNumber = $("#orderNumber").val();
			 if(orderNumber != ""){
				 PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportList?orderNumber='+orderNumber);
			 }else{
				 PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportList');
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
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		$("#searchForm3").blur(function(){
			var text = $("#searchForm3").val();
			$("#searchForm3").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 85px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>出库单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="exportNumber" alias="ep.export_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>出库指令单号：</p></form:left>
								<form:right><p><input id="searchForm3" class="text" type="text" name="exportPackageInstructionsNumber" alias="epi.export_package_instructions_number" oper="cn"/> </p></form:right>
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
								<form:left><p>出库日期：</p></form:left>
								<form:right><p><input id="exportStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'exportEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="exportStart" alias="ep.export_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="exportEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'exportStart\')}',dateFmt:'yyyy-MM-dd'})" name="exportEnd" alias="ep.export_date" oper="lt"/> </p></form:right>
							</form:column>
						</form:row>
							<form:row columnNum="5">
							<form:column >
								<form:left>订单号</form:left>
								<form:right><p><input id="orderNumber" class="text" type="text" name="orderNumber" /></p></form:right>
							</form:column>
							<form:column >
								<form:left>运单号</form:left>
								<form:right><p><input id="shipNumber" class="text" type="text" name="shipNumber" alias="ep.awb" oper="cn"/></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
								<form:column >
								<form:right>
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
							</form:right>
							</form:column>
							</form:row>
					</div>
				</form>
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>