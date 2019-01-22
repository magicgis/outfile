<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-新增供应商订单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [	{
							id:'add',
							icon : 'add',
							text : '新增供应商订单明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var orderNumber = ret["orderNumber"];
								var purchaseApply = ret["purchaseApply"];
								var clientId = ret["clientId"];
								var iframeId="idealframe1";
								/* if(purchaseApply=="已生成"){ */
									PJ.topdialog(iframeId, '采购-新增供应商订单明细 ', '<%=path%>/purchase/supplierorder/addSupplierOrderElement?id='+id+'&orderNumber='+orderNumber+'&clientId='+clientId,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								/* }else{
									PJ.warn("未生成采购申请单不可新增订单！");
								} */
									
							}
						},
						{
							id:'add',
							icon : 'add',
							text : '生成库存订单',
							click: function(){
								var iframeId="idealframe2";
								PJ.topdialog(iframeId, '选择供应商 ', '<%=path%>/purchase/supplierorder/toSelectSupplier',
											function(item,dialog){
												var postData = top.window.frames[iframeId].getFormData();
												$.ajax({
													type: "POST",
													dataType: "json",
													data: postData,
													url:'<%=path%>/purchase/supplierorder/addStorageOrder',
													success:function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
														}else{
															PJ.warn(result.message);
														}
													}
												});
											}
											,function(item,dialog){dialog.close();}, 700, 150, true,"新增");
								
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
						},
						{
							id:'down',
							icon : 'down',
							text : '批量下载',
							click: function(){
									var iframeId="inquiryiframe";
									PJ.topdialog(iframeId, '选择预订单', '<%=path%>/purchase/supplierorder/toSearchOrder',
										undefined,function(item,dialog){dialog.close();}, 1200, 700, true);
							}
						
					 	}
						/*{
							id:'down',
							icon : 'down',
							text : '供应商预订单下载',
							click: function(){
					 		var ret = PJ.grid_getSelectedData(grid);
					 		var id = ret["id"];

								//根据具体业务提供相关的title
								var title = '供应商预订单下载';
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var businessKey = "";
								
									businessKey='supplier_weather_order.clientOrderId.'+id+'.SupplierWeatherOrderExcel';
								
								PJ.excelDiaglog({
									data:'businessKey='+businessKey,
									title: title,
									add:true,
									remove:true,
									download:true
								});
					 	}} */
			        ]
		});
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/purchase/supplierorder/listSupplierOrderPage',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "co.update_timestamp",
			sortorder : "desc",
			useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
			colNames :["id","客户ID","订单号","客户订单号","订单日期","采购申请单","状态","备注","更新时间"],
			colModel :[PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("orderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("sourceOrderNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("orderDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("purchaseApply", {sortable:true,width:65,align:'left',
			        	   formatter:function(value){
								if(value==0){
									return "未生成";
								}
								else if(value==1){
									return "已生成";
								}
								else{
									return value
								}
							}  
			           }),
			           PJ.grid_column("orderStatusValue",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:250,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'})
			           ]
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
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/listSupplierOrderPage');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/listSupplierOrderPage');
			
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
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="orderNumber" alias="co.order_number or co.source_number" oper="cn"/> </p></form:right>
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
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="co.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="co.order_date" oper="lt"/> </p></form:right>
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
							<p style="padding-left:60px;">
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
	</div>
</body>
</html>