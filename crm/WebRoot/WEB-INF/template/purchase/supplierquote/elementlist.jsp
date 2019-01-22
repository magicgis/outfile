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
	

	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/sales/clientorder/orderApprovalDate?clientOrderId='+'${clientOrderId}'+'&type='+0,
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-160,
		autoSrcoll:true,
		multiselect:true,
		//shrinkToFit:false,
	colNames :["id","供应商","供应商报价单号","件号","单价","报价日期","有效期","状态"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("code", {align:'left',width:80}),
			           PJ.grid_column("supplierQuoteNumber", {align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("quotePrice", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("quoteDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("validity", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("state", {sortable:true,width:80,align:'left',
							  formatter: function(value){
									if (value==0) {
										return "报价超过有效期";
									} else {
										return "报价有效";
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
	 if(rowKey== ""){
			PJ.warn("请选择需要操作的数据");
			return false;
	 }
	 return postData;
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
	<div class="l-layout-header" style="height: 50px;padding-left: 90%;padding-top: 10px">
	<form id="Form">
	<input type="text" class="hide" name="supplierCode" value="${supplierCode}"/>
	<input type="text" class="hide" name="importPackagePaymentId" value="${id}"/>
	<input type="text" class="hide" name="id" value="${arrearsUse.id}"/>
	
				  <input class="btn btn_orange <c:if test="${ history eq 'yes'}">hide</c:if>" type="button" value="报价有效" id="addBtn"/>	
				 
				  </form>
				</div>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>

</body>
</html>