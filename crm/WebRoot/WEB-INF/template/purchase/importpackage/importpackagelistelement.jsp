
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>入库管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
	var layout, grid;
	$(function() {
		
		<%-- var url = "<%=path%>";
		
		var iframeId = 'editFrame';
		
		var btnArr=[];
		
		var btnCancel={text:"dakai",onclick:function(item,dialog){
			PJ.grid_reload(grid);dialog.close();
		}};
		
		btnArr.push(btnCancel);
		
		var opts = {
				buttons: btnArr,
				width: 1000, 
				height: 560,
				showMax: true
		};
		
		PJ.openTopDialog(iframeId, "选案建议", url, opts); --%>
		
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [  {
				icon : 'add',
				text : '新增',
				click : function(){
					/* var originalNumber=$("#originalNumber").val();
					var certificationDate=$("#certificationDate").val(); */
					var url = '<%=path%>/importpackage/addimportpackageelement?importNumber='+'${importNumber}'+'&id='+'${id}'
 					+'&supplierId='+'${supplierId}';
 					var iframeId="importpackageelementFrame";
 					var btnArr=[];
 					
 					var addbtnCancel={text:"新增",onclick:function(item,dialog){
 						 var postData=top.window.frames[iframeId].getFormData();	 							
						 var nullAble=top.window.frames[iframeId].validate();
						 if(nullAble){
							 $.ajax({
								 url: '<%=path%>/importpackage/addimportpackageelementdate?supplierOrderElementId='+
										 postData.supplierOrderElementId+'&importPackageId='+'${id}'+'&soeid='+postData.soeid,
									data: postData,
									type: "POST",
									loading: "正在处理...",
									success: function(result){
										
										if(!isNaN(result.message)){
											if(result.success){
												var originalNumber=$("#originalNumber").val();
												var certificationDate=$("#certificationDate").val();
												var url = '<%=path%>/importpackage/addStock?importNumber='+'${importNumber}'+'&elementId='+ postData.elementId
							 					+'&supplierOrderElementId='+postData.supplierOrderElementId+'&clientId='+postData.clientId+'&id='+'${id}'+'&message='+result.message;
							 					var iframeId="stockFrame";
							 					var btnArr=[];
							 					
							 					var addbtnCancel={text:"新增",onclick:function(item,dialog){
							 						 var postData=top.window.frames[iframeId].getFormData();	 							
													 var nullAble=top.window.frames[iframeId].validate();
													 if(nullAble){
														 $.ajax({
															 url: '<%=path%>/importpackage/addimportpackageelementdate?supplierOrderElementId='+
																	0+'&importPackageId='+'${id}',
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
							 					}};
							 					var printCancel1={text:"件号条形码打印",onclick:function(item,dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 
													var elementId=postData.elementId;
													var id=postData.soeid;
													var printAmount;
													if (typeof(postData.printAmount) == "undefined") { 
														printAmount=0; 
														}else{
															printAmount=postData.printAmount;
														}
													var amount=postData.amount;
													var inspectionDate=postData.inspectionDate;
													var manufactureDate=postData.manufactureDate;
													var printPartNumber=postData.printPartNumber;
													var printDescription=postData.printDescription;
													var resume=postData.completeComplianceCertificate;
													var complianceCertificate=postData.complianceCertificate;
													var location=postData.location;
													var serialNumber=postData.serialNumber;
													var $a = $("<a href='partiframes?idate="+inspectionDate+"&id=+"+id+"&pamount=+"+printAmount+"&sl=+"+location+"&resume=+"+resume
															+"&complianceCertificate=+"+complianceCertificate
															+"&amount=+"+amount+"&ipid=+"+${id}+"&mdate=+"+manufactureDate+"&sn=+"+encodeURIComponent(serialNumber)+
															"&pdesc=+"+encodeURIComponent(printDescription)+"&ppart=+"+encodeURIComponent(printPartNumber)+"'></a>");
													$a.printPage();
													$a.trigger("click");
													
											
												}};
							 					
							 					var printCancel2={text:"箱子条形码打印",onclick:function(item,dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 
							 						var location=postData.location;
							 						var clientCode=postData.clientCode;
							 						var clientOrderElementId=postData.clientOrderElementId;
													var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"&clientOrderElementId=+"+clientOrderElementId+"'></a>");
													$a.printPage();
													$a.trigger("click");
												}};
							 					
							 					var closeCancel={text:"关闭",onclick:function(item,dialog){
							 						PJ.grid_reload(grid);dialog.close();
							 					}};
							 					
							 					btnArr.push(closeCancel);
							 					btnArr.push(printCancel1);
							 					btnArr.push(printCancel2);
							 					btnArr.push(addbtnCancel);
							 					
							 					var opts = {
							 							buttons: btnArr,
							 							width: 1000, 
							 							height: 500,
							 							showMax: true
							 					};
							 					
							 					PJ.openTopDialog(iframeId, "财务 - 新增入库单明细", url, opts);
												PJ.success("新增成功，跳转新增剩余"+result.message+"件库存");
												PJ.grid_reload(grid);
												dialog.close();
											} else {
												PJ.error(result.message);
												dialog.close();
											}	
									}else{
										
										if(result.success){		
											PJ.success("新增成功");		
											dialog.close();
										}else{
										PJ.error(result.message);
										}
										PJ.grid_reload(grid);
									}
									}
								});
								}
 					}};
 					var printCancel1={text:"件号条形码打印",onclick:function(item,dialog){
						 var postData=top.window.frames[iframeId].getFormData();	 
						var elementId=postData.elementId;
						var id=postData.soeid;
						var printAmount;
						if (typeof(postData.printAmount) == "undefined") { 
							printAmount=0; 
							}else{
								printAmount=postData.printAmount;
							}
						var amount=postData.amount;
						var inspectionDate=postData.inspectionDate;
						var manufactureDate=postData.manufactureDate;
						var printPartNumber=postData.printPartNumber;
						var resume=postData.completeComplianceCertificate;
						var complianceCertificate=postData.complianceCertificate;
						printPartNumber=printPartNumber.replace(/'/g,"%27");
						var printDescription=postData.printDescription;
						var location=postData.location;
						var serialNumber=postData.serialNumber;
						var $a = $("<a href='partiframes?idate="+inspectionDate+"&id=+"+id+"&pamount=+"+printAmount+"&sl=+"+location+"&resume=+"+resume
								+"&complianceCertificate=+"+complianceCertificate
								+"&amount=+"+amount+"&ipid=+"+${id}+"&mdate=+"+manufactureDate+"&sn=+"+encodeURIComponent(serialNumber)+
								"&pdesc=+"+encodeURIComponent(printDescription)+"&ppart=+"+encodeURIComponent(printPartNumber)+"'></a>");
						$a.printPage();
						$a.trigger("click");
						//$a.trigger("click");
				
					}};
 					
 					var printCancel2={text:"箱子条形码打印",onclick:function(item,dialog){
						 var postData=top.window.frames[iframeId].getFormData();	 
 						var location=postData.location;
 						var clientCode=postData.clientCode;
 						var clientOrderElementId=postData.clientOrderElementId;
						var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"&clientOrderElementId=+"+clientOrderElementId+"'></a>");
						$a.printPage();
						$a.trigger("click");
					}};
 					
 					var closeCancel={text:"关闭",onclick:function(item,dialog){
 						PJ.grid_reload(grid);dialog.close();
 					}};
 					
 					btnArr.push(closeCancel);
 					btnArr.push(printCancel1);
 					btnArr.push(printCancel2);
 					btnArr.push(addbtnCancel);
 					
 					var opts = {
 							buttons: btnArr,
 							width: 1000, 
 							height: 500,
 							showMax: true
 					};
 					
 					PJ.openTopDialog(iframeId, "财务 - 新增入库单明细", url, opts);
					
		  			}
				},
				{
					icon : 'process',
					text : '修改',
					click : function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"]; 
						var clientId = ret["clientId"]; 
						var clientOrderNumber = ret["clientOrderNumber"]; 
						var url='<%=path%>/importpackage/editimportpackageelement?id='+id
			 			+'&supplierId='+'${supplierId}'+'&clientId='+clientId+'&clientOrderNumber='+clientOrderNumber;
						var iframeId = 'importpackageelementFrame';
						var btnArr=[];
						var editCancel={text:"修改",onclick:function(item,dialog){
							 var postData=top.window.frames[iframeId].getFormData();	 							
							 var nullAble=top.window.frames[iframeId].validate();
								if(nullAble){
							 $.ajax({
								    url: '<%=path%>/importpackage/editimportpackageelementdate?importPackageId='+id,
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
						}};
						
						var printCancel1={text:"件号条形码打印",onclick:function(item,dialog){
							 var postData=top.window.frames[iframeId].getFormData();	 
							var elementId=postData.elementId;
							var id=postData.soeid;
							var printAmount;
							if (typeof(postData.printAmount) == "undefined") { 
								printAmount=0; 
								}else{
									printAmount=postData.printAmount;
								}
							var ipeId=postData.id;
							var amount=postData.amount;
							var inspectionDate=postData.inspectionDate;
							var manufactureDate=postData.manufactureDate;
							var printPartNumber=postData.printPartNumber;
							var resume=postData.completeComplianceCertificate;
							var complianceCertificate=postData.complianceCertificate;
							var type="update";
							printPartNumber=printPartNumber.replace(/'/g,"%27");
							var printDescription=postData.printDescription;
							var location=postData.location;
							var serialNumber=postData.serialNumber;
							var $a = $("<a href='partiframes?idate="+inspectionDate+"&id=+"+id+"&pamount=+"+printAmount+"&sl=+"+location+"&resume=+"+resume
									+"&complianceCertificate=+"+complianceCertificate
									+"&amount=+"+amount+"&ipid=+"+${id}+"&mdate=+"+manufactureDate+"&sn=+"+encodeURIComponent(serialNumber)+"&ipeId=+"+ipeId+"&type=+"+type+
									"&pdesc=+"+encodeURIComponent(printDescription)+"&ppart=+"+encodeURIComponent(printPartNumber)+"'></a>");
							$a.printPage();
							$a.trigger("click");
							
							 var ret = PJ.grid_getSelectedData(grid);
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
										 		 var type="update";
												 var $a = $("<a href='<%=path%>/importpackage/partiframes?id="+supplierOrderElementId+"&ipid=+"
														+importPackageId+"&ipeId=+"+importPackageElementId+"&type=+"+type+"&amount=+"+storageAmount+"&ppart=+"+partNumber+"'></a>");
												$a.printPage();
												$a.trigger("click");
											} 	
									}
								});
							
						}};
	 					
	 					var printCancel2={text:"箱子条形码打印",onclick:function(item,dialog){
							 var postData=top.window.frames[iframeId].getFormData();	 
	 						var location=postData.location;
	 						var clientCode=postData.clientCode;
	 						var clientOrderElementId=postData.clientOrderElementId;
							var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"&clientOrderElementId=+"+clientOrderElementId+"'></a>");
							$a.printPage();
							$a.trigger("click");
						}};
	 					
	 					var closeCancel={text:"关闭",onclick:function(item,dialog){
	 						PJ.grid_reload(grid);dialog.close();
	 					}};
	 					
	 					btnArr.push(closeCancel);
	 					btnArr.push(printCancel1);
	 					btnArr.push(printCancel2);
	 					btnArr.push(editCancel);
	 					
	 					var opts = {
	 							buttons: btnArr,
	 							width: 1000, 
	 							height: 500,
	 							showMax: true
	 					};
	 					
	 					PJ.openTopDialog(iframeId, "财务 - 修改入库单明细", url, opts);
						
							 	<%-- PJ.topdialog(iframeId, '财务 - 修改入库单明细','<%=path%>/importpackage/editimportpackageelement?id='+id
							 			+'&supplierId='+'${supplierId}'+'&clientId='+clientId+'&clientOrderNumber='+clientOrderNumber,
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
											 $.ajax({
												    url: '<%=path%>/importpackage/editimportpackageelementdate?importPackageId='+id,
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
									},function(item,dialog){dialog.close();}, 1000, 500, true,'修改'); --%>
			  			}
					} ,
					{
						icon : 'process',
						text : '入库转库存',
						click : function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"]; 
							var iframeId = 'importpackageelementFrame';
								 	PJ.topdialog(iframeId, '修改入库单明细','<%=path%>/importpackage/toalterstoragepage?id='+id
								 			+'&supplierId='+'${supplierId}',
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
						} ,	{
							icon : 'process',
							text : '退货',
							click : function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"]; 
								var supplierOrderElementId = ret["supplierOrderElementId"]; 
								
								var iframeId = 'returngoodsFrame';
									 	PJ.topdialog(iframeId, '退货','<%=path%>/importpackage/returnGoodsPage?id='+id
									 			+'&supplierId='+'${supplierId}',
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	 							
													 var nullAble=top.window.frames[iframeId].validate();
														if(nullAble){
													 $.ajax({
														    url: '<%=path%>/importpackage/returnGoods?importpackageelementid='+id+'&supplierOrderElementId='+supplierOrderElementId,
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
											},function(item,dialog){dialog.close();}, 1000, 500, true,'退货');
					  			}
							} ,
				{
					icon : 'view',
					text : '手动生成合格证',
					click : function(){
						Excel();
							}
						},
						{
							icon : 'view',
							text : '合格证下载',
							click :
						function Excel(){
							
								//根据具体业务提供相关的title
								var title = 'excel管理';
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var ipid=${id}
								var businessKey = 'import_package_element.id.'+ipid+'.CertificationExcel'
								PJ.excelDiaglog({
									data:'businessKey='+businessKey,
									title: title,
									add:true,
									remove:true,
									download:true
								});
						}
						} ,{
							id:'up',
							icon : 'up',
							text : '异常件附件上传',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var partNumber=ret['partNumber'];
								var id=ret['id'];
								$("#fjName").val(partNumber);
								var businessKey={};
								businessKey['businessKey'] ='abnormal_part'+'.id.'+id,
								PJ.uploadAttachment(null,businessKey,function(result){
									if(result && result.id){
										PJ.success("上传成功", function(){
											if(parent.afterUpload){
												parent.afterUpload(result);
											}
										})
									}
								}, {});
							}
						} /**/]
		});

		grid = PJ.grid("list", {
			rowNum: 20,
			autowidth:true,
			url:'<%=path%>/importpackage/importpackageelementdate?id='+'${id}',
			width : PJ.getCenterWidth(),
			autoSrcoll:true,
			shrinkToFit:false,
			autowidth:true,
			/* gridComplete:function(){
				var obj = $("#list").jqGrid("getRowData");
				jQuery(obj).each(function(){
					$("#originalNumber").val(this.originalNumber);
					$("#certificationDate").val(this.certificationDate);
					
			    });
			}, */
			height : PJ.getCenterHeight()-142,
			sortname : "ip.import_date",
			colNames : ["id","supplierOrderElementId","supplierQuoteElementId","importpackageid", "件号","描述","状态","证书","符合性证明","是否完成符合性证明", "单位","数量","单价"
			            , "位置","重量","溯源号","系列号","批次号","生产日期","检验日期", "订单号","客户订单号","询价单号","更新时间","客户id","客户订单号"],
			colModel : [PJ.grid_column("id", {sortable:false,hidden:true}),
			            PJ.grid_column("supplierOrderElementId", {sortable:false,hidden:true}),
			            PJ.grid_column("supplierQuoteElementId", {hidden:true}),
			            PJ.grid_column("importPackageId", {sortable:false,hidden:true}),
			            PJ.grid_column("partNumber", {sortable:false}),
			            PJ.grid_column("description", {sortable:false}),
			            PJ.grid_column("conditionCode", {sortable:false,width:150}),
			            PJ.grid_column("certificationCode", {sortable:false,width:150}),
			            PJ.grid_column("complianceCertificateValue", {sortable:false,width:150}),
			            PJ.grid_column("completeComplianceCertificateValue", {sortable:false,width:150}),
						PJ.grid_column("unit", {sortable:false,width:100}),
						PJ.grid_column("amount", {sortable:false,width:80}),
						PJ.grid_column("price", {sortable:false}),
			            PJ.grid_column("location", {sortable:false}),
			           /*  PJ.grid_column("spzt", {sortable:false,
							formatter : cbspztFormatter}), */
			            PJ.grid_column("boxWeight", {sortable:false,formatter:function(value){
							if(value){
								return value+"g";
							}
						}}),
			            PJ.grid_column("originalNumber", {sortable:false}),
			            PJ.grid_column("serialNumber", {sortable:false,width:150}),
			            PJ.grid_column("batchNumber", {sortable:false,width:150}),
			            PJ.grid_column("manufactureDate", {sortable:false,width:150}),
			            PJ.grid_column("inspectionDate", {sortable:false,width:150}),
						PJ.grid_column("orderNumber", {sortable:false,width:100}),
						PJ.grid_column("sourceOrderNumber", {sortable:false,width:80}),
						PJ.grid_column("quoteNumber", {sortable:false,width:80}),
						PJ.grid_column("updateTimestamp", {sortable:false,width:180}),
						PJ.grid_column("clientId", {sortable:false,width:180,hidden:true}),
						PJ.grid_column("clientOrderNumber", {sortable:false,width:180}),
						],
						
		});
		
		//搜索条件是日期类型的加入日期控件
		PJ.datepickerAll();
		
		//右上角的帮助按扭float:right
		$("#toolbar > div[toolbarid='help']").addClass("fr");
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate');
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
		$("#test4_dropdown").hide();
	});
	
	function cbspztFormatter(cellvalue, options, rowObject){
		var id = rowObject["id"];
		
		switch (cellvalue) {
			case 232: 
				return PJ.addTabLink("审批中","审批中","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_ELEMENT.ID."+id,"blue")
				break;
			case 235: 
				return PJ.addTabLink("审批完成","审批完成","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_ELEMENT.ID."+id,"green")
				break;
			default: 
				return cellvalue;
				break; 
			}
	}
	
	function Excel(){
		var id=${id};
		var importNumber = ${importNumber};
		var supplierId = ${supplierId};
		var supplierCode = ${supplierCode};
		var iframeId = 'clientquoteFrame';
	 	PJ.topdialog(iframeId, '生成合格证', 
	 			'<%=path%>/importpackage/partCertification?id='+id+'&supplierCode='+supplierCode+'&importNumber='+importNumber+'&supplierId='+supplierId,
	 			
	 			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
	}
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
</head>

<body style="padding:3px">
	<div id="layout1">
		<div position="center" title="入库单号：${importNumber}   供应商：${supplierCode}">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
				
				</form>
			</div>
			<table id="list"></table>
			<div id="pager1"></div>
		
		</div>
	</div>
	 <div class="hide" id="tmpBox">
    </div>
	<div class="table-box" style="display: none;width:330px" id="uploadDiv" >
		<input type="text" name="folderName" id="folderName" class="hide" value="${importNumber}"/>
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
