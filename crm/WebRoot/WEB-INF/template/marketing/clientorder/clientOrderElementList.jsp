<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户订单明细管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	var orderStatusValue;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/sales/clientorder/uploadExcel?clientOrderId='+${id }+'&editType='+ getFormData().toString();
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
	            	//var message = decodeURI(data);
	            	//var a = decodeURI(data);
	            	//var da = jQuery.parseJSON(jQuery(data).text());
	            	var da = eval(data)[0];
	            	//var falg = data.flag;
	            	if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.getCenterHeight()-172);
		            	});
	            	}else{
	            		$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.getCenterHeight()-172);
		            	});
	            		if(da.message=='dotmatch'){
							iframeId = 'errorframe'
			            		PJ.topdialog(iframeId, '描述不匹配', '<%=path%>/sales/clientorder/toOrderDesc',
			            				function(item, dialog){
										 $.ajax({
												url: '<%=path%>/sales/clientorder/insertDate',
												data: '',
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
								},function(item,dialog){
					            			$.ajax({
												url: '<%=path%>/sales/clientorder/deleteDate',
												type: "POST",
												loading: "正在处理...",
												success: function(result){
												}
											});
			            					dialog.close();}, 1000, 500, true,"修改完成");
	            		}else{
		            		iframeId = 'errorframe'
		            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/sales/clientorder/toErrorMessage',
		            				undefined,function(item,dialog){
				            			$.ajax({
											url: '<%=path%>/sales/clientorder/deleteError',
											type: "POST",
											loading: "正在处理...",
											success: function(result){
											}
										});
		            					dialog.close();}, 1000, 500, true);
	            		}
	            	}
	            	////////////////////////////////////////////////////
	            	/* if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.calculateGridHeight("#layout1"));
		            	});
	            	}
	            	else{
	            		PJ.error(da.message);
	            		$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.calculateGridHeight("#layout1"));
		            	});
	            	} */
	            },
	            error: function (data, status, e) { 
	            	PJ.warn("上传出现异常！");
	            }  
	        });  
			
		});	
		
		$("#cover").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/sales/clientorder/coverExcel?clientOrderId='+${id }+'&editType='+ getCoverFormData().toString();
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
	            	//var message = decodeURI(data);
	            	//var a = decodeURI(data);
	            	//var da = jQuery.parseJSON(jQuery(data).text());
	            	var da = eval(data)[0];
	            	//var falg = data.flag;
	            	if(da.flag==true){
		            	PJ.success("修改成功！");
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.getCenterHeight()-172);
		            	});
	            	}else{
	            		$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
							grid.setGridHeight(PJ.getCenterHeight()-172);
		            	});
	            		iframeId = 'errorframe'
	            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/sales/clientorder/toErrorMessage',
	            				undefined,function(item,dialog){
			            			$.ajax({
										url: '<%=path%>/sales/clientorder/deleteError',
										type: "POST",
										loading: "正在处理...",
										success: function(result){
										}
									});
	            					dialog.close();}, 1000, 500, true);
	            	}
	            },
	            error: function (data, status, e) { 
	            	PJ.warn("上传出现异常！");
	            }  
	        });  
			
		});
		
		$("#toolbar").ligerToolBar({
			items : [
						{
							id:'add',
							icon : 'add',
							text : '新增',
							click: function(){
								var iframeId="idealframe12";
									PJ.topdialog(iframeId, '销售-新增客户订单明细', '<%=path%>/sales/clientorder/addOrder?clientOrderId='+${id},
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												 $.ajax({
														url: '<%=path%>/sales/clientorder/addOrderElement?clientOrderId='+${id},
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
												 PJ.grid_reload(grid);
											},function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
							}
						},
						{
							id:'search',
							icon : 'search',
							text : '上传客户订单明细',
							click: function(){
								$("#uploadBox").toggle(function(){
									var display = $("#uploadBox").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
										grid.setGridHeight(PJ.calculateGridHeight("#layout1"));
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("上传客户订单明细");
										grid.setGridHeight(PJ.calculateGridHeight("#layout1"));
									}
								});
							}
					}
					]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/sales/clientorder/elementList?id='+${id},
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			onSelectRow:function(rowid,result){
				var ret = PJ.grid_getSelectedData(grid);
				var certificationCode= ret["certificationCode"];
				var certificationId= ret["certificationId"];
				var ret = PJ.grid_getSelectedData(grid);
				var orderStatusId = ret["orderStatusId"];
				orderStatusValue = ret["orderStatusValue"];
				$("#certificationCode").val(certificationCode);
				var postData = {};
				postData.id = rowid;
				//证书
			 	$.ajax({
					type: "POST",
					data: postData,
					url:'<%=path%>/supplierquote/findcert?certificationId='+certificationId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							if(certificationId==""){
							cert = result.message;
							}else{
								cert =certificationId+":"+certificationCode+";"+ result.message;
							}
						}
					}
				}); 
				
			 	$.ajax({
					type: "POST",
					data: postData,
					url:'<%=path%>/purchase/supplierorder/findorderStatus?orderStatusId='+orderStatusId,
					success:function(result){
						if(result.success){
							os =orderStatusId+":"+orderStatusValue+";"+ result.message;
						}
					}
				}); 
			},
			editurl:'<%=path%>/sales/clientorder/editElement',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			sortname : "coe.price",
			sortorder : "asc",
			colNames :["id","spId","item","件号","另件号","描述","拆分","证书id","证书","供应商状态","状态id","售中状态","原数量","数量","库存数量","出库数量","单价","佣金","总价","报价选择供应商","备注","收款金额","收款时间","周期","截止日期","更新日期","明细ID","截止时间","供应商AWB","供应商运输方式","目的地","出库AWB","运输方式","尺寸","重量","出库数量比","出库时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("spId", {hidden:true,editable:true}),
			           PJ.grid_column("item",{sortable:true,width:40,align:'left'}),
			           PJ.grid_column("inquiryPartNumber",{sortable:true,width:130,align:'left'}),
			           PJ.grid_column("alterPartNumber",{sortable:true,width:130,align:'left'}),
			           PJ.grid_column("inquiryDescription",{sortable:true,width:210,align:'left'}),
			           PJ.grid_column("splitReceive",{sortable:true,width:40,align:'left',
			        	   formatter: splitFormatter
			           }),
			           PJ.grid_column("certificationId",{sortable:true,width:210,align:'left',hidden:true}),
			           PJ.grid_column("certificationCode", {sortable:true,width:120,editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								return cert;
								
							}
								/* "50:OEM COC-原厂合格证;51:FAA 8130-3-FAA 8130-3;52:EASA Form One-EASA Form One;53:Vendor COC-Vendor COC;54:Other-Other" */}
						,align:'left'}),
					   PJ.grid_column("supplierStatus",{sortable:true,width:88,align:'left'}),
				       PJ.grid_column("orderStatusId",{sortable:true,width:60,align:'left',hidden:true}),
				       PJ.grid_column("orderStatusValue", {sortable:true,width:70,editable:true,align:'left',
			        	   edittype:"select",formatter:"",
			        		   editoptions:{value:
									function(){
			        			  			var ret = PJ.grid_getSelectedData(grid);
			        			  			var orderStatusId = ret["orderStatusId"];
											return orderStatusId+":"+orderStatusValue+";"+"711:客户取消合同";        			   
			        		   		}
								}}),
					   PJ.grid_column("originalAmount",{sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("clientOrderAmount",{sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("storageAmount",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("exportAmount",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("clientOrderPrice",{sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("fixedCost", {width:80,align:'left',editable:true}),
			           PJ.grid_column("totalprice",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:180,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:80,align:'left'}),
				       PJ.grid_column("receiveDate", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientOrderLeadTime",{sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("clientOrderDeadline",{sortable:true,width:120,editable:true,
			        	   formatter: OrderFormatter,
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   } 
			           ,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'}),
			           PJ.grid_column("clientOrderElementId", {hidden:true,editable:true}),
			           PJ.grid_column("supplierOrderDeadline", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("supplierOrderAwb", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("shipWayValue", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("destination", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("exportPackageAwb", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("logisticsWayValue", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("size", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("weight", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("exportAmountPercent", {sortable:true,width:80,align:'left',align:'left'}),
			           PJ.grid_column("exportDate", {sortable:true,width:150,align:'left'})
			           
			           
			           ]
		});
		
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
		}
		
		function CountFormatter(cellvalue, options, rowObject){
			switch (cellvalue) {
			case 0:
				return '0';
				break;
				
			case '':
				return "无订单";
				break;
				
			default: 
				return cellvalue;
				break; 
			}
		}
		
		function splitFormatter(cellvalue, options, rowObject){
			switch (cellvalue) {
			case 1:
				return '是';
				break;
				
			case '':
				return '否';
				break;
				
			default: 
				return '否';
				break; 
			}
		}
		
		//二级表头
		$( "#list1" ).jqGrid( 'setGroupHeaders' , {
			useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
			groupHeaders : [ 
							 {
								startColumnName :  'id' ,  // 对应colModel中的name
								numberOfColumns : 25,  // 跨越的列数
								titleText :  '客户订单'
							  },
							  {
									 startColumnName :  'supplierOrderDeadline' ,  // 对应colModel中的name
									 numberOfColumns : 4,  // 跨越的列数
									 titleText :  '采购'
							  },
							  {
								 startColumnName :  'exportPackageAwb' ,  // 对应colModel中的name
								 numberOfColumns : 6,  // 跨越的列数
								 titleText :  '物流'
							   }
							]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-212);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-172);
			}
		});

		grid.setGridHeight(PJ.calculateGridHeight("#layout1"));

	});
	
	function spztFormatter(cellvalue, options, rowObject){
		var id = rowObject["spId"];
		
		switch (cellvalue) {
			case 234:
				return "未发起审批"
				break;
			case 233: 
				return PJ.addTabLink("审批不通过","审批不通过","/workflow/viewFlowInfo?businessKey=ORDER_APPROVAL.ID."+id,"red")
				break;
			case 232: 
				return PJ.addTabLink("审批中","审批中","/workflow/viewFlowInfo?businessKey=ORDER_APPROVAL.ID."+id,"blue")
				break;
			case 235: 
				return PJ.addTabLink("审批完成","审批完成","/workflow/viewFlowInfo?businessKey=ORDER_APPROVAL.ID."+id,"green")
				break;
			default: 
				return cellvalue;
				break; 
			}
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var data = $("#file").serialize();
		 return data;
	}
	
	function getCoverFormData(){
		 var data = $("#coverFile").serialize();
		 return data;
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

<body>
	<div id="layout1">
		<div position="center" title="订单号   ${orderNumber }">
		<div id="toolbar"></div>
		<div id="uploadBox">
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
						<form:column width="120">
							<form:left></form:left>
							<form:right>
								<p>
								   <input type="button" id="cover" value="覆盖"/>
								</p>
							</form:right>
						</form:column>
					</form:row>         
					   
			 </form>
		</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>