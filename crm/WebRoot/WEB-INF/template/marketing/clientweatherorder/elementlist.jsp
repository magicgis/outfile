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
	

	
	grid = PJ.grid("list1", {
		rowNum: 100,
		rowList: [100,500,1000],
		url:'<%=path%>/market/clientweatherorder/orderApprovalDate?clientOrderElementId='+'${clientOrderElementId}'+'&taskId='+'${taskId}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-160,
		autoSrcoll:true,
		multiselect:true,
		inLineEdit: true,
		editurl:'<%=path%>/market/clientweatherorder/editUseStorageAmount',
		aftersavefunc:function(rowid,result){
			PJ.grid_reload(grid);
		},
		shrinkToFit:false,
	colNames :["id","taskId","clientOrderElementId","differentUnit","item","件号","描述","数量","单位","备注","件号","描述","单位","数量","已使用数量","价格","类型","ExpirationDate","InspectionDate","ManufactureDate","入库日期","供应商","单价","流程图","经办意见"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("taskId", {editable:true,hidden:true}),
			           PJ.grid_column("clientOrderElementId", {hidden:true}),
			           PJ.grid_column("differentUnit", {hidden:true}),
			           PJ.grid_column("item", {sortable:true,align:'left',width:30}),
			           PJ.grid_column("partNumber", {sortable:true,align:'left',width:120}),
			           PJ.grid_column("description", {sortable:true,align:'left',width:120}),
			           PJ.grid_column("orderAmount", {sortable:true,align:'left',width:40}),
			           PJ.grid_column("orderUnit", {sortable:true,align:'left',width:40,
			        	   formatter:function(value,options,data){
								var differentUnit=data['differentUnit'];
								if(differentUnit=="1"){
									return '<span style="color:red">'+value+'<span>'
								}
							}	   
			           }),
			           PJ.grid_column("orderRemark", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("importPartNumber", {sortable:true,align:'left',width:120}),
			           PJ.grid_column("importDescription", {sortable:true,align:'left',width:120}),
			           PJ.grid_column("storageUnit", {sortable:true,align:'left',width:40,
			        	   formatter:function(value,options,data){
								var differentUnit=data['differentUnit'];
								if(differentUnit=="1"){
									return '<span style="color:red">'+value+'<span>'
								}
							}   
			           }),
			           PJ.grid_column("amount", {sortable:true,align:'left',width:40,editable:true}),
			           PJ.grid_column("storageUsedAmount", {sortable:true,align:'left',width:50}),
			           PJ.grid_column("price", {sortable:true,align:'left',width:50}),
			           PJ.grid_column("type", {sortable:true,align:'left',width:60 ,formatter: function(value){
							if (value==1) {
								return "在途库存";
							} else {
								return "自有库存";							
							}
						}}),
						PJ.grid_column("expirationDate", {sortable:true,align:'left',width:100}), 
				       PJ.grid_column("inspectionDate", {sortable:true,align:'left',width:100}),
				       PJ.grid_column("manufactureDate", {sortable:true,align:'left',width:100}),
				       PJ.grid_column("importDate", {sortable:true,align:'left',width:80}),
			           PJ.grid_column("code", {sortable:true,align:'left',width:50}),
			           PJ.grid_column("quotePrice", {sortable:true,align:'left',width:50}),
			           PJ.grid_column("spzt", {sortable:true,formatter : cbspztFormatter,width:50}),
			           PJ.grid_column("jbyj", {sortable:true,align:'left',width:150, editoptions:{style:"width:100px;height:30px;"}})
			           ]
	});
	 grid.jqGrid('setGroupHeaders', { 
		 	useColSpanStyle: true, 
		 	groupHeaders:[ 
		 		{startColumnName: 'id', numberOfColumns: 10, titleText: '<div align="center"><span>客户预订单</span></div>'},
		 		{startColumnName: 'importPartNumber', numberOfColumns: 11, titleText: '<div align="center"><span>库存</span></div>'},
		 		{startColumnName: 'code', numberOfColumns: 2, titleText: '<div align="center"><span>销售预报价</span></div>'},
		 		{startColumnName: 'spzt', numberOfColumns: 2, titleText: '<div align="center"><span>流程</span></div>'}
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
	<div position="center" title="客户订单号：${orderNumber}"  >
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>

</body>
</html>