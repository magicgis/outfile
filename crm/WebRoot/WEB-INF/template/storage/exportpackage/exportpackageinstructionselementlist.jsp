<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>出库指令</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findsid',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#supplier_code").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		layout = $("#layout1").ligerLayout();
		if('${flow}'=='yes'){
		$("#toolbar").ligerToolBar({
			items : [
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
										grid.setGridHeight(PJ.getCenterHeight()-142);
									}
								});
							}
					}
			        ]
		});
		}else{
			$("#toolbar").ligerToolBar({
				items : [
						 {
								id:'add',
								icon : 'add',
								text : '不出库',
								click: function(){
									 var rowKey = grid.getGridParam("selarrrow");
									 if(rowKey!= ""){
										 var air =  PJ.grid_getMutlSelectedRowkey(grid);
										 var id = air.join(",");
									 }
									 if(rowKey== ""){
											PJ.warn("请选择需要操作的数据");
											return false;
									 }
													$.ajax({	
														url: '<%=path%>/storage/exportpackage/noExportpackage?id='+id,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	PJ.success(result.message);
																	PJ.grid_reload(grid);
																} else {
																	PJ.error(result.message);
																	PJ.grid_reload(grid);
																}		
														}
													});
								}
						 },{
								icon : 'view',
								text : '新增',
								click : function(){
									var iframeId = 'clientquoteFrame';
								 	PJ.topdialog(iframeId, '新增出库指令明细', 
								 			'<%=path%>/storage/exportpackage/toAddExportPackageInstructionsElement?code='+'${code}'+'&epiId='+'${id}',
												function(item, dialog){
														 var postData=top.window.frames[iframeId].getFormData();	 							
														 $.ajax({
															    url: '<%=path%>/storage/exportpackage/addExportPackageInstructionsElement?epiId='+'${id}',
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
												},function(item,dialog){dialog.close();}, 1000, 500, true);
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
											grid.setGridHeight(PJ.getCenterHeight()-142);
										}
									});
								}
						}
				        ]
			});
		}
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exportpackage/exportPackageInstructionsElementList?id='+'${id}'+'&flow='+'${flow}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			multiselect:true,
			shrinkToFit:false,
			gridComplete:function(){
				var location=$("#location").val();
					var obj = $("#list1").jqGrid("getRowData");
						if(obj.length>0){
							$("#boxWeight").text(obj[0].countWeight);
						}
						
				
			},
			inLineEdit: true,
			editurl:'<%=path%>/storage/exportpackage/updateExportpackageInstructionsElement',
			//sortname : "ep.export_date",
			sortorder : "desc",
			colNames :["id","入库明细id","件号","描述","状态","证书","单位","数量","退税","","符合性证明","是否完成符合性证明","人民币价格","总价","位置","订单号","客户订单号","供应商订单号","入库日期","重量(g)","总重量","订单单价","订单币种","订单总价","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("importPackageElementId", {key:true,hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("certificationCode", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("exportPackageInstructionsAmount", {sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("taxValue", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("completeComplianceCertificateValue",{hidden:true,formatter:function(value){
	   	                	$("#completeComplianceCertificateValue").val(value);
		                	return value;
						}}),
				           PJ.grid_column("complianceCertificateValue", {sortable:true,width:80,align:'left',formatter:function(value){
		   	                	var completeComplianceCertificateValue=$("#completeComplianceCertificateValue").val();
		   	                	if(completeComplianceCertificateValue=="否"){
		   	                		return '<span style="color:red">'+value+'<span>';
		   	                	}else{
			                	return value;
		   	                	}
							}}),
				           PJ.grid_column("completeComplianceCertificateValue",{sortable:true,width:80,align:'left',formatter:function(value){
		   	                	if(value=="否"){
		   	                		return '<span style="color:red">'+value+'<span>';
		   	                	}else{
			                	return value;
		   	                	}
							}}),
			           PJ.grid_column("basePrice",{sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}   
			           ,align:'left'}),
			           PJ.grid_column("totalBasePrice", {sortable:true,width:100,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}   
			           ,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("sourceNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("importDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("boxWeight",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("countWeight",{hidden:true}),
			           PJ.grid_column("orderPrice",{hidden:true}),
			           PJ.grid_column("clientOrderCurrencyValue",{hidden:true}),
			           PJ.grid_column("orderTotal",{hidden:true}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:120,align:'left'})
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
		    	PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportPackageInstructionsElementList?id='+'${id}'+'&flow='+'${flow}');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportPackageInstructionsElementList?id='+'${id}'+'&flow='+'${flow}');
			
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
		
		$("#searchForm4").blur(function(){
			var text = $("#searchForm4").val();
			$("#searchForm4").val(trim(text));
		});
		
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
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
	<div id="layout1">
		<div position="center" title="总重量<span id='boxWeight'></span>KG"  >
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<input type="text" class="hide" id="completeComplianceCertificateValue"/>
					<div class="search-box">
						<form:row columnNum="5">
						
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="partNumber" alias="ipe.part_number" oper="cn"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p>位置：</p></form:left>
								<form:right><p><input id="searchForm3" class="text" type="text" name="location" alias="ipe.location" oper="cn"/> </p></form:right>
							</form:column>
							
							<form:column>
							      <form:left>供应商</form:left>
							   	<form:right><p><select style="width:80px" id="supplier_code" name="supplier_code" alias="s.id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							
							<form:column>
							      <form:left>退税：</form:left>
							   	<form:right><p><select style="width:50px" id="tax" name="tax" alias="soe.tax_reimbursement_id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
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
	</div>
</body>
</html>