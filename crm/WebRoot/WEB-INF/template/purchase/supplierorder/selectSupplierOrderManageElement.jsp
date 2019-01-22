<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商订单明细管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />

<script type="text/javascript">
var layout, grid;

$(function(){
	layout = $("#layout1").ligerLayout();
	
	var shipway;
	var destination;
	var os;
	var orderStatusValue;
	$("#toolbar").ligerToolBar({
		items : [{
					id:'up',
					icon : 'up',
					text : '附件上传',
					click: function(){
						var ids = getData();
						$.ajax({
							url:'<%=path%>/purchase/supplierorder/createFjId',
							data:'ids='+ids,
							type: "POST",
							success: function(result){
								if(result.success){
									var businessKey={};
									businessKey['businessKey'] ='supplier_order_element_lot'+'.id.'+result.message,
									PJ.uploadAttachment(null,businessKey,function(result){
										if(result && result.id){
											PJ.success("上传成功", function(){
												if(parent.afterUpload){
													parent.afterUpload(result);
												}
												PJ.grid_reload(grid2);
											})
										}
									}, {});
								} else {
									PJ.error(result.message);
								}		
							}
						});
						
					}
				}
		         ]
	});
	
	grid = PJ.grid("list1", {
		rowNum: 1000,
		url:'<%=path%>/purchase/supplierorder/SupplierOrderElementForUpload?id='+${id},
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-112,
		autoSrcoll:true,
		shrinkToFit:false,
		multiselect:true,
		//sortname : "ci.inquiry_date",
		colNames :["id","件号","另件号","location","描述","周期","截至日期","退税","单位","状态","证书","客户订单数量","采购数量","入库数量","售中状态","状态id","单价","总价","银行","入库费","入库运费","付款金额","付款时间","供应商询价单号","更新时间","AWB","到货周期","发票日期","发货方式id","发货方式","目的地id","目的地","入库单号"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("quotePartNumber", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("alterPartNumber", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("location", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("quoteDescription", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("leadTime", {sortable:true,width:30,editable:true,align:'left'}),
		           PJ.grid_column("deadline", {sortable:true,width:70,editable:true,
		        	   editoptions:{ 
		        		   dataInit:function(el){ 
		        		     $(el).click(function(){ 
		        		       WdatePicker(); 
		        		     }); 
		        		   } 
		        	   }    
		           ,align:'left'}),
		           PJ.grid_column("taxReturnValue", {sortable:true,width:30,align:'left',
		        	   formatter: taxFormatter}),
		           PJ.grid_column("quoteUnit", {sortable:true,width:30,align:'left'}),
		           PJ.grid_column("conditionCode", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("clientOrderAmount", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("supplierOrderAmount", {sortable:true,width:60,editable:true,align:'left'}),
		           PJ.grid_column("importAmount", {sortable:true,width:60,
		        	   formatter: CountFormatter   
		        	   ,align:'left'}),
		           PJ.grid_column("orderStatusValue", {sortable:true,width:70,editable:true,align:'left',
		        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
				        			    var ret = PJ.grid_getSelectedData(grid);
				        				var orderStatusId = ret["orderStatusId"];
				        				orderStatusValue 
										return orderStatusId+":"+orderStatusValue+";"+"712:供应商取消合同";        			   
		        		   		}
							}}),
		           PJ.grid_column("orderStatusId", {sortable:true,width:70,editable:true,align:'left',hidden:true}),
		        	   
		           PJ.grid_column("supplierOrderPrice", {sortable:true,width:70,editable:true,align:'left'}),
		           PJ.grid_column("supplierOrderTotalPrice", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("bankCost", {sortable:true,width:40,align:'left',
		        	   formatter:function(value){
							if (value) {
								return "$"+value;
							}
					    }
		           }),
		           PJ.grid_column("importFee", {sortable:true,width:60,align:'left',
		        	   formatter:function(value){
							if (value) {
								return "$"+value;
							}
					    }   
		           }),
		           PJ.grid_column("importFreight", {sortable:true,width:60,align:'left',
		        	   formatter:function(value){
							if (value) {
								return "$"+value;
							}
					    }   
		           }),
		           PJ.grid_column("paymentTotal", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("paymentDate", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("supplierQuoteNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("updateTimestamp", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("awb", {sortable:true,width:150,editable:true,align:'left'}),
		           PJ.grid_column("shipLeadTime", {sortable:true,width:70,editable:true,align:'left'}),
		           PJ.grid_column("invoiceDate", {sortable:true,width:80,editable:true,
		        	   editoptions:{ 
		        		   dataInit:function(el){ 
		        		     $(el).click(function(){ 
		        		       WdatePicker(); 
		        		     }); 
		        		   } 
		        	   }
		        	   ,align:'left'}),
		           PJ.grid_column("shipWayId", {sortable:true,width:70,editable:true,align:'left',hidden:true}),
		           PJ.grid_column("shipWayValue", {sortable:true,width:120,editable:true,
		        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return shipway;        			   
		        		   		}
							}
		        	   ,align:'left'}),
		           PJ.grid_column("destinationId", {sortable:true,width:70,editable:true,align:'left',hidden:true}),
		           PJ.grid_column("destination", {sortable:true,width:100,editable:true,align:'left',
		        	   edittype:"select",formatter:"",
	        		   editoptions:{value:
							function(){
									return destination;        			   
	        		   		}
						}
	        	   ,align:'left'}),
		           PJ.grid_column("importNumber", {sortable:true,width:140,align:'left'})
		           ]
	});
	
	function CountFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '0';
			break;
			
		
			
		default: 
			return cellvalue;
			break; 
		}
	}
	
	function taxFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '否';
			break;
		case 1:
			return '是';
			break;
			
		default: 
			return cellvalue;
			break; 
		}
	}
	
	//改变窗口大小自适应
	$(window).resize(function() {
		grid.setGridWidth(PJ.getCenterWidth());
		grid.setGridHeight(PJ.getCenterHeight()-112);
	});
	
});

//获取表单数据
function getData(){
	var ids =  PJ.grid_getMutlSelectedRowkey(grid);
	var elementIds = ids.join(",");
	return elementIds;
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
		<div position="center" title="">
		<div id="toolbar"></div>
			<table id="list1"></table>
		</div>
	</div>
	<div class="table-box" style="display: none;width:330px" id="uploadDiv" >
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