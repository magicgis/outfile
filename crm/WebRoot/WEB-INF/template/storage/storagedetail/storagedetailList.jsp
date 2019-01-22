<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>库存明细</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [
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
					 },
					 {	icon : 'logout',
						text : '导出excel',
						click : function() {
								PJ.grid_export("list1");
								}
					  },
					  {	icon : 'add',
							text : '完成符合性证明',
							click : function() {
								  var ret = PJ.grid_getSelectedData(grid);
						 		    var id = ret["id"];
								$.ajax({
									type: "POST",
									dataType: "json",
									url:'<%=path%>/storage/detail/editimportpackageelementdate?importPackageElementId='+id,
									success:function(result){
										if(result.success){
											PJ.grid_reload(grid);
										}else{
												PJ.showWarn(result.msg);
										}
									}
								});
									}
						  },
					  {	icon : 'add',
							text : '新增出库指令',
							click : function() {
								var ids = getData();
								if(ids != ""){
									var clientCode=$("#searchForm1").val();
									if(clientCode==""){
										PJ.error("请选择客户");
										return;
									}
									var iframeId="ecportpackageInstructionsIframe";
									PJ.topdialog(iframeId, '选择或者新增出库指令', '<%=path%>/storage/detail/toEcportpackageInstructionsWithSelect?clientCode='+$("#searchForm1").val()+'&supplierCode='+$("#supplierCode").val()
											+'&partNumber='+$("#partNumber").val()+'&location='+$("#location").val()+'&importDateStart='+$("#importDateStart").val()+'&importDateEnd='+$("#importDateEnd").val()
											+'&complianceCertificate='+$("#complianceCertificate").val()+'&completeComplianceCertificate='+$("#completeComplianceCertificate").val()+'&tax='+$("#tax").val()+'&exportpackage='+$("#exportpackage").val(),
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();
													 //var validate=top.window.frames[iframeId].validate();
													 //if(postData&&validate){
														 PJ.showLoading("处理中...");
														 $.ajax({
																url: '<%=path%>/storage/detail/addEcportpackageInstructions?ids='+ids,
																data: postData,
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
													 //}
										},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
								}else{
									var clientCode=$("#searchForm1").val();
									if(clientCode==""){
										PJ.error("请选择客户");
										return;
									}
									var iframeId="ecportpackageInstructionsIframe";
									PJ.topdialog(iframeId, '新增出库指令', '<%=path%>/storage/detail/toEcportpackageInstructions?clientCode='+$("#searchForm1").val()+'&supplierCode='+$("#supplierCode").val()
											+'&partNumber='+$("#partNumber").val()+'&location='+$("#location").val()+'&importDateStart='+$("#importDateStart").val()+'&importDateEnd='+$("#importDateEnd").val()
											+'&complianceCertificate='+$("#complianceCertificate").val()+'&completeComplianceCertificate='+$("#completeComplianceCertificate").val()+'&tax='+$("#tax").val()+'&exportpackage='+$("#exportpackage").val(),
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();
													 var validate=top.window.frames[iframeId].validate();
													 if(postData&&validate){
														 PJ.showLoading("处理中...");
														 $.ajax({
																url: '<%=path%>/storage/detail/addEcportpackageInstructions',
																data: postData,
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
													 }
										},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
								}
							}
						  } ,{	
							  id:'add',
							  icon : 'add',
								text : '打印条码',
								click : function() {
									 var ret = PJ.grid_getSelectedData(grid);
							 		 var importPackageId = ret["importPackageId"];
							 		 var supplierOrderElementId=ret["supplierOrderElementId"];
							 		 var storageAmount=ret["storageAmount"];
							 		 var partNumber=ret["partNumber"];
							 		 var importPackageElementId=ret["id"];
							 		 var manufactureDate= ret["manufactureDate"];
									 var inspectionDate= ret["inspectionDate"];
							 		 var type="update";
									var $a = $("<a href='<%=path%>/importpackage/partiframes?id="+supplierOrderElementId
											+"&idate="+inspectionDate+"&mdate="+manufactureDate
											+"&ipid="+importPackageId+"&ipeId="+importPackageElementId+"&type="+type+"&amount="+storageAmount+"&ppart="+partNumber+"'></a>");
									$a.printPage();
									$a.trigger("click");
									
									 var supplierQuoteElementId=ret["supplierQuoteElementId"];
									 $.ajax({
										    url: '<%=path%>/importpackage/storage?supplierQuoteElementId='+supplierQuoteElementId,
											data: '',
											type: "POST",
											loading: "正在处理...",
											success: function(result){
													if(result.success){
														var message=result.message;
														var obj = eval(message);
														 var importPackageId = obj[0].importPackageId;
												 		 var supplierOrderElementId=obj[0].supplierOrderElementId;
												 		 var storageAmount=obj[0].storageAmount;
												 		 var partNumber=obj[0].partNumber;
												 		 var importPackageElementId=obj[0].importPackageElementId;
												 		var complianceCertificate=postData.complianceCertificate;
												 		 var type="update";
														 var $a = $("<a href='<%=path%>/importpackage/partiframes?id="+supplierOrderElementId
																 +"&idate="+inspectionDate+"&mdate="+manufactureDate
																 +"&ipid=+"+importPackageId+"&ipeId=+"+importPackageElementId+"&type=+"+type+"&amount=+"+storageAmount+"&ppart=+"+partNumber+"'></a>");
														$a.printPage();
														$a.trigger("click");
													} 	
											}
										});
									
										}
							  },{
									icon : 'process',
									text : '入库转库存',
									click : function(){
										var ret = PJ.grid_getSelectedData(grid);
										var id = ret["id"]; 
										var storageAmount= ret["storageAmount"];
										var iframeId = 'importpackageelementFrame';
									 	PJ.topdialog(iframeId, '修改入库单明细','<%=path%>/importpackage/toalterstoragepage?id='+id
									 			+'&supplierId='+'${supplierId}'+'&storageAmount='+storageAmount,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 var nullAble=top.window.frames[iframeId].validate();
														if(nullAble){
													 $.ajax({
														    url: '<%=path%>/importpackage/alterstoragea?importPackageId='+id,
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
											},function(item,dialog){dialog.close();}, 1000, 500, true,'修改');
							  			}
							},{	icon : 'edit',
								text : '绑定库存替换件',
								click : function() {
									$.ajax({
									    url: '<%=path%>/storage/detail/isPurchase',
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													var ret = PJ.grid_getSelectedData(grid);
													var id = ret["id"];
													var partNumber = ret["partNumber"];
													var description = ret["description"];
													var clientCode = ret["clientCode"];
													if(clientCode == '197' || clientCode == '199'){
														var iframeId = 'storagecorrelationFrame';
														PJ.topdialog(iframeId, '绑定库存替换件','<%=path%>/storage/detail/toCorrelationList?importPackageId='+id
													 			+'&partNumber='+partNumber+'&description='+description,
															undefined,function(item,dialog){dialog.close();}, 1000, 500, true,'修改');
													}else{
														PJ.warn("自由库存才能关联")
													}
												} else {
													PJ.warn("只有采购可以操作！");
													dialog.close();
												}		
										}
									});
								}
							},{
								icon : 'view',
								text : 'Excel管理',
								click : function(){
									Excel();
								}
							},{
								icon : 'edit',
								text : '批量修改库位',
								click : function(){
									var iframeId = 'editLocationFrame';
								 	PJ.topdialog(iframeId, '选择库位','<%=path%>/importpackage/toChangeLocationForLot',
										function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();	 							
												var nullAble=top.window.frames[iframeId].validate();
												postData.importPackageElementIds = getData();
												if(nullAble){
												 	$.ajax({
													    url: '<%=path%>/importpackage/updateLocationForLot',
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
										},function(item,dialog){dialog.close();}, 350, 100, true,'修改');
								}
							},{
								icon : 'view',
								text : '库存清算',
								click : function(){
									var iframeId = 'checkStorageFrame';
									var locationiframeId = 'selectLoactionFrame';
								 	PJ.topdialog(locationiframeId, '选择库位','<%=path%>/importpackage/toChangeLocationForLot',
										function(item, dialog){
												var postData=top.window.frames[locationiframeId].getFormData();	 							
												var nullAble=top.window.frames[locationiframeId].validate();
												postData.importPackageElementIds = getData();
												if(nullAble){
													PJ.topdialog(iframeId, '位置'+postData.location+'入库明细','<%=path%>/storage/detail/toPartInLoaction?location='+postData.location,
															undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
													dialog.close();
												}
										},function(item,dialog){dialog.close();}, 350, 100, true,'查看');
								}
							}
			        ]
				});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/detail/StorageDetail',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:false,
			multiselect:true,
			gridComplete:function(){
				var location=$("#location").val();
					var obj = $("#list1").jqGrid("getRowData");
					//jQuery(obj).each(function(){
						if(obj.length>0){
							$("#boxWeight").val(obj[0].countWeight);
							if(obj[0].clientOrderPrice != null && obj[0].clientOrderPrice != ''){
								$("#clientOrderPrice").text(obj[0].clientOrderPrice);
							}
							if(obj[0].totalAmount != null && obj[0].totalAmount != ''){
								$("#amount").text(obj[0].totalAmount);
							}
							if(obj[0].totalPrice != null && obj[0].totalPrice != ''){
								$("#price").text(obj[0].totalPrice);
							}
						}
						
					//});
				 /* */
				
			},
			sortname : "a.import_date",
			sortorder : "desc",
			colNames :["id","importPackageId","supplierOrderElementId","supplierQuoteElementId","客户","客户订单号","供应商","件号","描述","状态",
			           "证书","","符合性证明","是否完成符合性证明","退税","单位","数量","流程占用数量","人民币价格","总价","位置","入库单号","供应商订单号","入库日期","生产日期","到期时间","寿命","运输单号",
			           "物流方式","重量(g)","总重量","总数量","总金额","客户订单金额","备注","检验日期"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("importPackageId", {hidden:true}),
			           PJ.grid_column("supplierOrderElementId", {hidden:true}),
			           PJ.grid_column("supplierQuoteElementId", {hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("completeComplianceCertificateValue",{hidden:true,formatter:function(value){
   	                	$("#completeComplianceCertificateValue").val(value);
	                	return value;
					}}),
			           PJ.grid_column("complianceCertificateValue", {sortable:true,width:70,align:'left',formatter:function(value){
	   	                	var completeComplianceCertificateValue=$("#completeComplianceCertificateValue").val();
	   	                	if(completeComplianceCertificateValue=="否"){
	   	                		return '<span style="color:red">'+value+'<span>';
	   	                	}else{
		                	return value;
	   	                	}
						}}),
			           PJ.grid_column("completeComplianceCertificateValue",{sortable:true,width:110,align:'left',formatter:function(value){
	   	                	if(value=="否"){
	   	                		return '<span style="color:red">'+value+'<span>';
	   	                	}else{
		                	return value;
	   	                	}
						}}),
					   PJ.grid_column("taxValue",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("unit",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("storageAmount",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("lockAmount",{sortable:true,width:80,align:'left'
			        	   ,formatter:function(value){
			        		   return '<span style="color:red">'+value+'<span>';
			        	   }   
			           }),
			           PJ.grid_column("basePrice",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("totalBasePrice",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("location",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("importNumber",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("orderNumber",{sortable:true,width:90,align:'left'}),
			           PJ.grid_column("importDate",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("manufactureDate",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("expireDate",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("restLife",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("logisticsNo",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("logisticsValue",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("boxWeight",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("countWeight",{hidden:true}),
			           PJ.grid_column("totalAmount",{hidden:true}),
			           PJ.grid_column("totalPrice",{hidden:true}),
			           PJ.grid_column("clientOrderPrice",{hidden:true}),
			           PJ.grid_column("remark",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("inspectionDate",{hidden:true})
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
		
		//退税
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/detail/tax',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#tax").append($option);
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
		    	PJ.grid_search(grid,'<%=path%>/storage/detail/StorageDetail?exportpackage='+$("#exportpackage").val());
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/detail/StorageDetail?exportpackage='+$("#exportpackage").val());
			
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
		$("#partNumber").blur(function(){
			var text = $("#partNumber").val();
			text = trim(text);
			$("#partNumber").val(text.replace(" ", ""));
		});
		
		//获取表单数据
		function getData(){
			var postData = {};
			var ids =  PJ.grid_getMutlSelectedRowkeyNotValidate(grid);
			if (ids != ""){
				importPackageElementIds = ids.join(",");
				return importPackageElementIds;
			}else{
				return "";
			}
			
		}
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	function Excel(){
		 var rowKey = grid.getGridParam("selarrrow");
		 var ids;
		 if(rowKey!= ""){
			 var id =  PJ.grid_getMutlSelectedRowkey(grid);
			 ids = id.join(",");
		 }
		 if(rowKey== ""){
				PJ.warn("请选择需要操作的数据");
				return false;
		 }
		 var result=ids.split(",");
		 var length=result.length;
		 if(length>4){
			 PJ.warn("最多只能选中四条数据");
			 return false;
		 }
		//根据具体业务提供相关的title
		var title = 'excel管理';
		//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
		//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
		var id = '${id}'
		if(id == 'NaN'){
			id=''
		}
		var ipid=id+"-"+ids
		var businessKey = 'import_package_element.id.'+ipid+'.CertificationExcel'
		PJ.excelDiaglog({
			data:'businessKey='+businessKey,
			title: title,
			add:true,
			remove:true,
			download:true
		});
	}
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>

</head>

<body>
	<div id="layout1">
		<div position="center" title="库存数量：<span id='amount'>${storageDetailVo.storageAmount }</span>
		&nbsp;&nbsp;&nbsp;库存金额：<span id='price'>${storageDetailVo.totalBasePrice}</span>&nbsp;&nbsp;&nbsp;
		<c:if test="${role eq '销售'}">发票金额： <span id='clientOrderPrice'>${storageDetailVo.clientOrderPrice}</span></c:if> ">
		
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
				<input type="text" class="hide" id="completeComplianceCertificateValue"/>
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left>件号</form:left>
								<form:right><input name="partNumber" id="partNumber"  class="text" alias="a.part_number" oper="cn"/></form:right>
							</form:column>
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="a.client_code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>供应商</form:left>
								<form:right><p>
												<select id="supplierCode" name="supplierCode" alias="a.supplier_code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>入库日期：</p></form:left>
								<form:right><p><input id="importDateStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'importDateEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="importDateStart" alias="a.import_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="importDateEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'importDateStart\')}',dateFmt:'yyyy-MM-dd'})" name="importDateEnd" alias="a.import_date" oper="lt"/> </p></form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column>
								<form:left>位置</form:left>
								<form:right><input name="location" id="location"  class="text"  alias="a.location" oper="cn"/></form:right>
							</form:column>
							
							<form:column>
								<form:left>总重量:</form:left>
								<form:right><input id="boxWeight" style="border: 0px;width: 80px" readonly="readonly"/>KG&nbsp;&nbsp;&nbsp;&nbsp
								可出库
								<select name="exportpackage" id="exportpackage"   style="width:60px">
									<option value="">全部</option>
									<option value="1">是</option>
							        <option value="0">否</option>
								</select>
								</form:right>
							</form:column>
						
						<form:column>
								<form:left>符合性证明</form:left>
								<form:right><p>
												<select id="complianceCertificate" name="complianceCertificate" alias="a.compliance_certificate" oper="eq">
							        			<option value="">全部</option>
							        			<option value="300">合格证</option>
							        			<option value="301">履历表</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
						
						<form:column>
								<form:left><p>是否完成</p><p>符合性证明</p></form:left>
								<form:right><p>
												<select id="completeComplianceCertificate" name="completeComplianceCertificate" alias="a.complete_compliance_certificate" oper="eq">
							        			<option value="">全部</option>
							        			<option value="520">是</option>
							        			<option value="521">否</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							
						
							<form:column >
							<form:left>退税：
								<select name="tax" id="tax" alias="a.tax_reimbursement_id" oper="eq" style="width:50px">
									<option value="">全部</option>
								</select>
							</form:left>
							<form:right>
								<p style="padding-left:1px;">
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