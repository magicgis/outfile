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
		 $.ajax({
				url: '<%=path%>/finance/importpackagepayment/arrearsUse',
				data: getFormData(),
				type: "POST",
				loading: "正在处理...",
				success: function(result){
						if(result.success){
							PJ.success(result.message);
							window.location.reload();
						} else {
							PJ.error(result.message);
						}
					
				}
			});
	 }); 
	
	$("#editBtn").click(function(){
		 $.ajax({
				url: '<%=path%>/finance/importpackagepayment/updateArrearsUse',
				data: getFormData(),
				type: "POST",
				loading: "正在处理...",
				success: function(result){
						if(result.success){
							PJ.success(result.message);
							window.location.reload();
						} else {
							PJ.error(result.message);
						}
					
				}
			});
	 }); 
	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/importpackage/paymentTaskElementList?ipeeId='+'${importPackagePaymentElementId}'+'&taskId='+'${taskId}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-200,
		autoSrcoll:true,
		multiselect:true,
		inLineEdit: true,
		editurl:'<%=path%>/finance/importpackagepayment/insertJbyj?taskName=${taskName}',
		//shrinkToFit:false,
	colNames :["id","付款id","付款明细id","订单明细id","件号","数量","比例","应付","使用欠款","入库日期","备注","流程图","审批意见"],
	colModel :model()
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
	
});

function cbspztFormatter(cellvalue, options, rowObject){
	var id = rowObject["importPackagePaymentElementId"];
	var partNumber=rowObject["partNumber"];
	
	
	//return "<a href=\"javascript:void(0)\" onclick="+subgo('"+partNumber+"','"+id+"')+" style='color:"+"blue"+"'>"+"查看"+"</a>";
	/*  myTab.addTabItem({
         tabid: partNumber,
         text: "查看",
         url: "/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+id,
         callback: function ()
         {
             addFrameSkinLink(tabid); 
         }
     });  */
     //window.parent.frames["zcbsmxIframe"].contentWindow || window.parent.frames["zcbsmxIframe"];
     //window.parent.frames["returnMyTab"]();
			//return PJ.addTabLink(partNumber,"查看","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+id,"blue")
			return PJ.addTabLink("流程图","查看","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+id,"blue")
}



function model(){
	var model={};
	var taskName='${taskName}';
	if(taskName=='cgzxsq'){
		model=[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
         PJ.grid_column("importPackagePaymentId", {hidden:true}),
         PJ.grid_column("importPackagePaymentElementId", {hidden:true,editable:true}),
         PJ.grid_column("supplierOrderElementId", {hidden:true}),
         PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
         PJ.grid_column("amount", {sortable:true,width:50,align:'left',editable:true}),
         PJ.grid_column("paymentPercentage", {sortable:true,width:60,align:'left',editable:true}),
         PJ.grid_column("shouldPay", {sortable:true,width:170,align:'left'}),
         PJ.grid_column("arrearsTotal", {sortable:true,width:170,align:'left'}),
         PJ.grid_column("importDate", {sortable:true,width:170,align:'left'}),
         PJ.grid_column("remark", {sortable:true,width:300,align:'left'}),
         PJ.grid_column("spzt", {sortable:true,width:80,formatter : cbspztFormatter}),
         PJ.grid_column("jbyj", {sortable:true,width:300,editable:true,align:'left', editoptions:{style:"width:100px;height:30px;"}})
         ]
	}else{
		model=[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
		         PJ.grid_column("importPackagePaymentId", {hidden:true}),
		         PJ.grid_column("importPackagePaymentElementId", {hidden:true}),
		         PJ.grid_column("supplierOrderElementId", {hidden:true}),
		         PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
		         PJ.grid_column("amount", {sortable:true,width:50,align:'left',}),
		         PJ.grid_column("paymentPercentage", {sortable:true,width:60,align:'left'}),
		         PJ.grid_column("shouldPay", {sortable:true,width:170,align:'left'}),
		         PJ.grid_column("arrearsTotal", {sortable:true,width:170,align:'left'}),
		         PJ.grid_column("importDate", {sortable:true,width:170,align:'left'}),
		         PJ.grid_column("remark", {sortable:true,width:300,align:'left'}),
		         PJ.grid_column("spzt", {sortable:true,width:80,formatter : cbspztFormatter}),
		         PJ.grid_column("jbyj", {sortable:true,width:300,editable:true,align:'left', editoptions:{style:"width:100px;height:30px;"}})
		         ]
	}
	return model;
}

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
	// var postData = {};
	 var rowKey = grid.getGridParam("selarrrow");
	 if(rowKey!= ""){
		 var id =  PJ.grid_getMutlSelectedRowkey(grid);
		 ids = id.join(",");
		 //if(ids.length>0){
		//		postData.ids = ids;
			//}
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
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
<div id="layout1" style="float: left">
	<div position="center" title="应付总额：${shouldPay}  供应商:${supplierCode}"  >
		<c:if test="${taskName != 'cgzxsq'}">
	<div class="l-layout-header" style="height: 80px;">
	<form id="Form">
	<input type="text" class="hide" name="id" value="${arrearsUse.id}"/>
	<input type="text" class="hide" name="supplierCode" value="${supplierCode}"/>
	<input type="text" class="hide" name="importPackagePaymentId" value="${id}"/>
	<input type="text" class="hide" name="importPackagePaymentElementId" value="${importPackagePaymentElementId}"/>
	<input type="text" class="hide" name="taskName" value="${taskName}"/>
	
			<c:if test="${taskName eq 'cwsh'}">
				供应商欠款：${supplierDebt.total}<br />
				使用欠款：	 <input type="text" name="total" value="${arrearsUseTotal}" <c:if test="${history eq 'yes'}">readonly="readonly"</c:if>/> 
				手续费：<input type="text" name="counterFee" value="${counterFee}" <c:if test="${history eq 'yes'}">readonly="readonly"</c:if>/>
				  <input class="btn btn_orange <c:if test="${ history eq 'yes'}">hide</c:if>" type="button" value="确定" id="addBtn"/>	
				   <%--  <input class="btn btn_orange <c:if test="${optType eq 'add' || history eq 'yes'}">hide</c:if>" type="button" value="修改" id="editBtn" />	 --%>
				 </c:if>
				 	
				 	<c:if test="${taskName eq 'fk' || taskName eq 'zjlsh'}">
				 	供应商欠款：${supplierDebt.total}<br />
				 	使用欠款：${arrearsUseTotal} 手续费：${counterFee}
				 	</c:if>
				 
				  </form>
				</div>
				</c:if>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>

</body>
</html>