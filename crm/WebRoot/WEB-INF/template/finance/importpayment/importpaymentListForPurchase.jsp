<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-付款管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	
	$(function() {
		var paymentStatus;
		var paymentType = "0:预付款;1:发货付款;2:验收付款";
		
		
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 170,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [
					 {
						 	id:'add',
							icon : 'add',
							text : '新增付款',
							click: function(){
								var iframeId1="searchIframe";
								var iframeId2="addIframe";
								PJ.topdialog(iframeId1, '搜索', '<%=path%>/finance/importpackagepayment/toSearchNumber',
										function(item,dialog){
											 var id=top.window.frames[iframeId1].getData();
											 PJ.topdialog(iframeId2, '新增付款单', '<%=path%>/finance/importpackagepayment/toAddPayment?id='+id,
													 function(item,dialog){
												 	 var postData=top.window.frames[iframeId2].getFormData();
													 $.ajax({
															url: '<%=path%>/finance/importpackagepayment/savePayment?postData='+postData,
															data: postData,
															type: "POST",
															loading: "正在处理...",
															success: function(result){
																	if(result.success){
																		PJ.success(result.message);
																		PJ.grid_reload(grid2);
																		PJ.grid_reload(grid);
																	} else {
																		PJ.error(result.message);
																	}		
															}
														});
													 PJ.grid_reload(grid);
													 dialog.close();}													 
													 ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
										dialog.close();}
									   ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
					 	}
					 },
					 {
						 	id:'add',
							icon : 'add',
							text : '新增明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var supplierId = ret["supplierId"];
								var paymentType = ret["paymentType"];
								var supplierOrderId = ret["supplierOrderId"];
								var importPackageId = ret["importPackageId"];
								var iframeId="incomeIframe";
								if(paymentType=="验收付款"){
									PJ.topdialog(iframeId, '新增付款明细', '<%=path%>/importpackage/toAddPaymentElement?supplierId='+supplierId+'&id='+id+'&importPackageId='+importPackageId,
											function(item,dialog){
												 var postData=top.window.frames[iframeId].getFormData();	 							
												 $.ajax({
														url: '<%=path%>/importpackage/AddPaymentElement?id='+id,
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
											PJ.grid_reload(grid);
											PJ.grid_reload(grid2);
											dialog.close();}
										   ,function(item,dialog){PJ.grid_reload(grid);PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
								}else{
									PJ.topdialog(iframeId, '新增付款明细', '<%=path%>/importpackage/toAddPaymentElementBySupplierOrderElementId?supplierOrderId='+supplierOrderId+'&id='+id,
											function(item,dialog){
												 var postData=top.window.frames[iframeId].getFormData();	 							
												 $.ajax({
														url: '<%=path%>/importpackage/AddPaymentElement?id='+id,
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
											PJ.grid_reload(grid);
											PJ.grid_reload(grid2);
											dialog.close();}
										   ,function(item,dialog){PJ.grid_reload(grid);PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
								}
								
					 	}
					 } ,<%-- --%>{
							icon : 'edit',
							text : '付款申请',
							click : function() {
								

								var selectRow = PJ.grid_getSelectedData(grid);;

								if( !selectRow ) return;
								PJ.popupAssigneeWindow( 
										{
											type:"0",
											assignee:"5",
										},
										function(assigneedata){
											PJ.ajax({
												url:'<%=path%>/finance/importpackagepayment/paymentRequest?id='+selectRow.id+'&ids='+assigneedata,
												data:'',
												success:function(result){
													if(result.success){
														PJ.tip(result.message);
														PJ.grid_reload(grid);
														PJ.grid_reload(grid2);
													}else{
														PJ.error(result.message);
													}
												}
											});
										}
								);
							}
						},{
							icon : 'view',
							text : 'Excel管理',
							click : function(){
								Excel();
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
					 
					]
		});
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/finance/importpackagepayment/List',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
				var postData = {};
				postData.id = rowid;
				$.ajax({
					url: '<%=path%>/finance/importpackagepayment/getPaymentStatus',
					data: postData,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								paymentStatus = result.message;
							}
					}
				});
			},
			editurl:'<%=path%>/importpackage/editPayment',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			sortname : "ipp.update_timestamp",
			sortorder : "desc",
			colNames :["id","供应商id","订单id","入库id","供应商","付款单号","付款日期","应付金额","已付金额","周期","审批状态","类型","状态","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("supplierId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("supplierOrderId", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("importPackageId", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("code", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("paymentNumber", {key:true,width:120,editable:true,align:'left'}),
			           PJ.grid_column("paymentDate", {sortable:true,width:100,editable:true,align:'left',
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   } 
			           }),
			           PJ.grid_column("shouldPay", {sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("paymentTotal", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("leadTime", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("spzt",{sortable:true,align:'left',formatter: function(value){
							if (value==232) {
								return "审批中";
							} else if (value==235||value==233) {
								return "审批结束";
							}if (value==234) {
								return "未发起审批";
							}
						}
						}),
			           PJ.grid_column("paymentType", {sortable:true,width:90,editable:true,align:'left',
		        	   edittype:"select",formatter:typeFormatter,
		        		   editoptions:{value:
								function(){
										return paymentType;        			   
		        		   		}
							}
			           }),
			           PJ.grid_column("paymentStatusValue", {sortable:true,width:80,editable:true,align:'left',
		        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return paymentStatus;        			   
		        		   		}
							}}),
			           PJ.grid_column("remark", {sortable:true,width:200,editable:true,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager2",
			editurl:'<%=path%>/importpackage/editPaymentElement',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
				PJ.grid_reload(grid);
			},
			sortname : "ippe.update_timestamp",
			colNames :["id","付款id","订单明细id","件号","数量","比例","应付","已付","审批状态","当前处理人","经办意见","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("importPackagePaymentId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("supplierOrderElementId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("paymentPercentage", {sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("shouldPay", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("paymentSum", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("spzt",{
							sortable:true,
							formatter : cbspztFormatter
						}),
					   PJ.grid_column("userName",{sortable:true,width:80,editable:true,align:'left'}),		
					   PJ.grid_column("jbyj", {sortable:true,width:300,editable:true,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:300,editable:true,align:'left'})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			PJ.grid_search(grid2,'<%=path%>/importpackage/paymentElementList?importPackagePaymentId='+id);
		};
		
		 
		//改变窗口大小自适应
		/* $(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-182);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		}); */
		$(window).resize(function() {
			GridResize();
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/List');
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/List');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
		 $("#searchForm2").blur(function(){
				var text = $("#searchForm2").val();
				$("#searchForm2").val(trim(text));
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
							$("#supplierCode").append($option);
						}
					}else{
						
							PJ.showWarn(result.msg);
					}
				}
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

	function cbspztFormatter(cellvalue, options, rowObject){
		var id = rowObject["id"];
		
		switch (cellvalue) {
			case 234:
				return "未发起审批"
				break;
			case 233: 
				return PJ.addTabLink("审批不通过","审批不通过","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+id,"red")
				break;
			case 232: 
				return PJ.addTabLink("审批中","审批中","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+id,"blue")
				break;
			case 235: 
				return PJ.addTabLink("审批通过","审批通过","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+id,"green")
				break;
			default: 
				return cellvalue;
				break; 
			}
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
	function typeFormatter(cellvalue, options, rowObject){
		var type = rowObject["paymentType"];
		
		switch (type) {
			case 0:
				return "预付款";
				break;
			case 1:
				return "发货付款";
				break;
			case 2:
				return "验收付款";
				break;	
				
			default: 
				return cellvalue;
				break; 
		}
	}
	
	function Excel(){
 		var ret = PJ.grid_getSelectedData(grid);
 		var id = ret["id"];
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = 'import_package_payment.id.'+id+'.ImportPackgePaymentExcel'
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

</head>

<body>
	<div id="layout1">
		<div position="center" title="">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>单号：</p></form:left>
								<form:right><p><input id="Number" class="text" type="text" name="Number" alias="ipp.PAYMENT_NUMBER or so.ORDER_NUMBER or ip.IMPORT_NUMBER" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>供应商：</form:left>
								<form:right><p>
												<select id="supplierCode" name="supplierCode" alias="s.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>类型：</p></form:left>
								<form:right><p>
												<select id="searchForm1" name="supplierCode" alias="ipp.PAYMENT_TYPE" oper="eq">
												<option value="">全部</option>
							        			<option value="0">预付款</option>
							        			<option value="1">发货付款</option>
							        			<option value="2">尾款</option>
							            		</select>
								</p></form:right>
							</form:column>
							<form:column>
								<form:left><p>付款日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ipp.PAYMENT_DATE" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ipp.PAYMENT_DATE" oper="lt"/> </p></form:right>
							</form:column>
						</form:row>
						
						<form:row columnNum="5">
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
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
					</div>
				</form>
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
		<div position="centerbottom" >
			<table id="list2" style=""></table>
			<div id="pager2"></div>
  		</div>
	</div>
</body>
</html>