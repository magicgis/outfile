<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-供应商订单管理</title>

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
							text : '明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe";
									PJ.topdialog(iframeId, '采购-供应商订单明细管理 ', '<%=path%>/purchase/supplierorder/SupplierOrderElement?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1100, 500, true);
							}
					 },
					 {
							id:'modify',
							icon : 'modify',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe";
									PJ.topdialog(iframeId, '采购-修改供应商订单', '<%=path%>/purchase/supplierorder/editSupplierOrder?id='+id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	
													 if(postData){
													 $.ajax({
															url: '<%=path%>/purchase/supplierorder/saveEditSupplierOrder',
															data: postData,
															type: "POST",
															loading: "正在处理...",
															success: function(result){
																	if(result.success){
																		PJ.success(result.message);
																		dialog.close();
																		 PJ.grid_reload(grid);
																	} else {
																		PJ.error(result.message);
																		
																	}		
															}
														});
													 }
													 
											},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
								
							}
					 },
					 <%-- {
							id:'modify',
							icon : 'modify',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe";
									PJ.topdialog(iframeId, '采购-修改供应商订单', '<%=path%>/purchase/supplierorder/toSelectUploadElement?id='+id,
											undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
								
							}
					 }, --%>
					 {
						    id:'down',
							icon : 'down',
							text : '下载',
							click: function(){
								    var ret = PJ.grid_getSelectedData(grid);
						 		    var id = ret["id"]+"-"+${userId};
						 		    var currencyId = ret["currencyId"];
									//根据具体业务提供相关的title
									var title = 'excel管理';
									//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
									//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
									var businessKey = 'supplier_order.id.'+id+'.SupplierOrderExcel.'+currencyId;
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
							id:'view',
							icon : 'view',
							text : '文件管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/purchase/supplierorder/fj?id='+id,
										undefined,function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
							}
					 },
					 {
							id:'view',
							icon : 'view',
							text : '财务水单上传',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/purchase/supplierorder/paymentFile?id='+id,
										undefined,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true);
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
										grid.setGridHeight(PJ.getCenterHeight()-242);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-142);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/purchase/supplierorder/SupplierOrderData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "so.update_timestamp",
			sortorder : "desc",
			multiselect:true,
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			onSelectAll:function(rowids,statue,e){ 
				onSelect();
			},
			colNames :["id","货币id","供应商","状态","订单号","供应商订单号","库存订单","订单日期","币种","汇率","总价","紧急程度","预付","发货前","验收","账期","付款数量比","付款金额比","状态","创建人","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("currencyId", {key:true,hidden:true}),
			           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left'}),
				       PJ.grid_column("supplierStatus", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("supplierOrderNumber", {sortable:true,width:100,align:'left',
			        	   formatter:function(cellvalue, options, rowObj){  
			                    return "<span id='"+rowObj.id+"' class='xmidData' style='display:block; width:100%; cursor:pointer;'>"+cellvalue+"</span>";  
			        	   }   
			           }),
			           PJ.grid_column("orderType", {sortable:true,width:60,editable:true,align:'left',
			        		formatter:function(value){
			        			if(value==1){
			        				return '<span style="color:red">'+'是'+'<span>';
			        			}
			        		}   
			        	   }),
			           PJ.grid_column("orderDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("currencyValue",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("exchangeRate", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("urgentLevelValue", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("prepayRate", {sortable:true,width:80,align:'left',
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}}),
			           PJ.grid_column("shipPayRate", {sortable:true,width:80,align:'left',
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}}),
			           PJ.grid_column("receivePayRate", {sortable:true,width:80,align:'left',
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}}),
			           PJ.grid_column("receivePayPeriod", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("amountPercent", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("totalPercent", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("orderStatusValue",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("userName",{sortable:true,width:80,align:'left'}),
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
		
		function onSelect(){
			var rowKey = grid.getGridParam("selarrrow");
			var total = 0;
			for(var i=0, len=rowKey.length; i<len; i++) {
				var row_id= rowKey[i];
				var data = grid.jqGrid("getRowData",row_id);
				if (data.total != ""){
					total = parseFloat(total) + parseFloat(data.total)
				}
		    }
			var title = "总金额："+total
			$("#titleId").text(title);
		};
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	var partNumber = $("#partNumber").val()
				PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/SupplierOrderData?partNumber='+partNumber);
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var partNumber = $("#partNumber").val()
			 PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/SupplierOrderData?partNumber='+partNumber);
			
		 });
		
		/* $("#list1").on("click", ".xmidData", function(e){ 
			xmid = e.currentTarget.id;
			alert(xmid);
	    }); */
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		 $("#partNumber").blur(function(){
			 var text = $("#partNumber").val();
			 text = trim(text);
			 $("#partNumber").val(text.replace(" ", ""));
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
		
		function getData(options){
			var a = options;
		}
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	function getData(options){
		var a = options;
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
/*  th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
} */
</style>
</head>
<style>
	#cb_list1{
		margin:0
	}
</style>
<body>
	<div id="layout1">
		<div position="center" title="<span id='titleId'>总金额：0</spam>">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" alias="co.order_number or so.order_number" oper="cn"/> </p></form:right>
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
								<form:left>件号</form:left>
								<form:right><input id="partNumber" class="text" type="text" /></form:right>
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
							<form:column>
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="orderStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'orderEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="orderStart" alias="so.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="orderEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="orderEnd" alias="so.order_date" oper="lt"/> </p></form:right>
							</form:column>
							
							<form:column >
							<p style="padding-left:30px;">
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