<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>查询 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>

</style>
<script type="text/javascript">
var layout, grid;


$(function(){
	layout = $("#layout1").ligerLayout();
	
	$("#addBtn").click(function(){
		if( getData()){
		 $.ajax({
				url: '<%=path%>/sales/clientorder/quoteValid',
				data: getData(),
				type: "POST",
				loading: "正在处理...",
				success: function(result){
						if(result.success){
							window.location.reload();
						} else {
							PJ.error(result.message);
						}
					
				}
			});
		}
	 }); 
	$("#toolbar").ligerToolBar({
		items : [ 
	 	{
			icon : 'edit',
			text : '修改',
			click : function(){
				edit();
			}
		}]
		
	});
	
	

	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/sales/clientorder/orderApprovalDate?clientOrderElementId='+'${clientOrderElementId}'+'&type='+1,
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-160,
		autoSrcoll:true,
		editurl:'<%=path%>/supplierquote/updatesupplierquoteelement',
		inLineEdit: true,
		aftersavefunc:function(rowid,result){
			var responseJson=result.responseJSON;
			if(responseJson.success==true){
				window.location.reload();
				PJ.grid_reload(grid);
			}
		},
		multiselect:true,
		//shrinkToFit:false,
	colNames :["id","supplierQuoteElementId","supplierinquirynumber","供应商","供应商报价单号","询价单号","件号","供应商单价","订单单价","利润率","客户利润率","状态"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("supplierQuoteElementId", {hidden:true,editable:true,editable:true}),
			            PJ.grid_column("supplierinquirynumber", {hidden:true}),
			           PJ.grid_column("code", {align:'left',width:80}),
			           PJ.grid_column("supplierQuoteNumber", {align:'left'}),
			           PJ.grid_column("inquiryNumber", {align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("quotePrice", {sortable:true,width:80,align:'left',editable:true}),
			           PJ.grid_column("orderPrice", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("profitMargin", {sortable:true,width:80,align:'left'
			        	   ,formatter:function(value){
								if(value){
									return value.toFixed(2)+"%";
								}
								else{
									return value+"%";
								}
							}}),
			           PJ.grid_column("clientProfitMargin", {sortable:true,width:80,align:'left',formatter:function(value){
							if(value){
								return value.toFixed(2)+"%";
							}
							else{
								return value+"%";
							}
						}}),
			           PJ.grid_column("state", {sortable:true,width:80,align:'left',
							  formatter: function(value){
									if (value==0) {
										return "利润低于标准";
									} else {
										return "通过";
									}
								}
			        	   
			           }),
			           ]
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
	
});

//获取表单数据
function getFormData(){
		var $input = $("#Form").find("input,textarea,select");
		var postData = {};
		$input.each(function(index){
			if(!$(this).val()) {
				//PJ.tip("必填数据项没有填写完整");
				//flag = true;
				return;
			}
			postData[$(this).attr("name")] = $(this).val();
		});
		return postData;
}

//获取表单数据
function getData(){
	 var postData = {};
	 var rowKey = grid.getGridParam("selarrrow");
	 if(rowKey!= ""){
		 var id =  PJ.grid_getMutlSelectedRowkey(grid);
		 ids = id.join(",");
		 if(ids.length>0){
				postData.ids = ids;
			}
	 }
	 var obj = $("#list1").jqGrid("getRowData");
	 if(obj.length>1&&rowKey== ""){
		 PJ.warn("请选择需要操作的数据");
			return false;
	 }else if(parseInt(obj.length)==1){
		 ids=obj[0].id+",";
	 }
	 return ids;
 }	
 
function edit(){
	var ret = PJ.grid_getSelectedData(grid);
	var iframeId = 'supplierquoteelementFrame';
	var supplierQuoteNumber = ret["supplierinquirynumber"];
	var clientInquiryQuoteNumber=ret["inquiryNumber"];
	var id = ret["supplierQuoteElementId"]; 
		 	PJ.topdialog(iframeId, '采购 - 修改供应商报价明细','<%=path%>/supplierquote/editsupplierquoteelementdate?id='+id
		 			+'&supplierInquiryQuoteNumber='+supplierQuoteNumber+'&clientInquiryQuoteNumber='+clientInquiryQuoteNumber,
				function(item, dialog){
						 var postData=top.window.frames[iframeId].getFormData();	 							
						 $.ajax({
							    url: '<%=path%>/supplierquote/editsupplierquoteelement?id='+id,
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
				},function(item,dialog){dialog.close();}, 1000, 500, true,'修改');
	
	
	}
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
<div id="layout1" style="float: left">
	<div position="center" title="客户订单号：${clientOrder.orderNumber}"  >
		<c:if test="${ history != 'yes'}">
	<div id="toolbar"></div>
	</c:if>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>

</body>
</html>