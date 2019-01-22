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
		rowNum: 100,
		rowList: [100,500,1000],
		url:'<%=path%>/sales/clientorder/finalOrderDataPage?clientOrderElementId='+'${clientOrderElementId}'+'&taskId='+'${taskId}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-160,
		autoSrcoll:true,
		multiselect:true,
		inLineEdit: true,
		editurl:'<%=path%>/sales/clientorder/insertJbyj',
		//shrinkToFit:false,
	colNames :["id","taskId","clientOrderElementId","件号","描述","流程图","供应商价格","订单价格","利润率","利润率标准","经办意见"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("taskId", {editable:true,hidden:true}),
			           PJ.grid_column("clientOrderElementId", {hidden:true,editable:true,}),
			           PJ.grid_column("partNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("spzt", {sortable:true,width:30,formatter : cbspztFormatter}),
			           PJ.grid_column("price", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("orderPrice", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("profitMargin", {sortable:true,width:30,align:'left'
			        	   ,formatter:function(value){
								if(value){
									return value.toFixed(2)+"%";
								}
								else{
									return value+"%";
								}
							}}),
			           PJ.grid_column("clientProfitMargin", {sortable:true,width:30,align:'left',formatter:function(value){
							if(value){
								return value.toFixed(2)+"%";
							}
							else{
								return value+"%";
							}
						}}),
			           PJ.grid_column("jbyj", {sortable:true,width:100,editable:true,align:'left', editoptions:{style:"width:100px;height:30px;"}})
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

function cbspztFormatter(cellvalue, options, rowObject){
	var id = rowObject["id"];
	
	
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
			return PJ.addTabLink("流程图","查看","/workflow/viewFlowInfo?businessKey=ORDER_APPROVAL.ID."+id,"blue")
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
	 var ids="";
	// if(rowKey!= ""){
		// var id =  PJ.grid_getMutlSelectedRowkey(grid);
		 /*  ids = id.join(",");
		 //if(ids.length>0){
		//		postData.ids = ids;
			//}
	 } */
	 var obj = $("#list1").jqGrid("getRowData");
	
	 if(obj.length>1&&rowKey== ""){
		 PJ.warn("请选择需要操作的数据");
			return false;
	 }else if(parseInt(obj.length)==1){
		 ids=obj[0].taskId;
	 }else{
	 for(var i=0, len=rowKey.length; i<len; i++) {
			var row_id= rowKey[i];
			 var data = grid.jqGrid("getRowData",row_id);
			 ids+=data.taskId+",";
	     }
	 }
	 <%-- if(""!=ids){
		 $.ajax({
				url: '<%=path%>/sales/clientorder/checkAmount?ids='+rowKey+'&id='+id,
				data: '',
				type: "POST",
				loading: "正在处理...",
				success: function(result){
					
				}
			});
	 } --%>
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
	<div position="center" title="客户订单号：${clientOrder.orderNumber}"  >
	<%-- <div class="l-layout-header" style="height: 50px;padding-left: 90%;padding-top: 10px">
	<form id="Form">
				  <input class="btn btn_orange <c:if test="${ history eq 'yes'}">hide</c:if>" type="button" value="通过" id="addBtn"/>	
				  </form>
				</div> --%>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>

</body>
</html>