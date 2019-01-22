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
	
	
	$("#toolbar").ligerToolBar({
		items : [  {
			id:'view',
			icon : 'view',
			text : '异常件转ALT',
			click: function(){
				var ret = PJ.grid_getSelectedData(grid);
				var id = ret["importPackageElementId"]; 
				var supplierId = ret["supplierId"]; 
				var iframeId = 'importpackageelementFrame';
					 	PJ.topdialog(iframeId, '异常件转ALT','<%=path%>/importpackage/supplierorderlement?supplierId='+supplierId,
							function(item, dialog){
									 var postData=top.window.frames[iframeId].getFormData();	 
									 dialog.close();
										var iframeid = 'altFrame';
									 	PJ.topdialog(iframeid, '异常件转ALT','<%=path%>/importpackage/altimportstoragepage?id='+id+'&quotePartNumber='+postData.quotePartNumber
									 			+'&supplierOrderNumber='+postData.supplierOrderNumber+'&clientId='+postData.clientId,
											function(item, dialog){
													 var postdata=top.window.frames['altFrame'].getData();	 							
													 var nullAble=top.window.frames[iframeid].validate();
														if(nullAble){
													 $.ajax({
														    url: '<%=path%>/importpackage/altimportstorage?id='+id+'&supplierOrderELementId='+postData.id,
															data: postdata,
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
							},function(item,dialog){dialog.close();}, 1000, 500, true,'关联');
			}
		}
		]
				});
	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/importpackage/listData?id='+'${ipeIds}'+'&taskId='+'${taskId}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-80,
		autoSrcoll:true,
		multiselect:true,
		inLineEdit: true,
		editurl:'<%=path%>/finance/importpackagepayment/insertJbyj?taskName=${taskName}',
		//shrinkToFit:false,
	colNames :["id","supplier_id","明细id","件号","数量","描述","备注","流程图","审批意见"],
	colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
	           PJ.grid_column("supplierId", {hidden:true}),
	           PJ.grid_column("importPackageElementId", {hidden:true,editable:true}),
	           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
	           PJ.grid_column("amount", {sortable:true,width:50,align:'left',editable:true}),
	           PJ.grid_column("description", {sortable:true,width:60,align:'left',editable:true}),
	           PJ.grid_column("remark", {sortable:true,width:300,align:'left'}),
	           PJ.grid_column("spzt", {sortable:true,width:80,formatter : cbspztFormatter}),
	           PJ.grid_column("jbyj", {sortable:true,width:300,editable:true,align:'left', editoptions:{style:"width:100px;height:30px;"}})
	           ]
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
	var id = rowObject["importPackageElementId"];
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
			return PJ.addTabLink("流程图","查看","/workflow/viewFlowInfo?businessKey=IMPORT_PACKAGE_ELEMENT.ID."+id,"blue")
}





//获取表单数据
function getFormData(){
		 var postData = {};
		 var obj = $("#list1").jqGrid("getRowData");
		 var ids=0;
		 var rowKey = grid.getGridParam("selarrrow");
		 if(rowKey!= ""){
			 var taskIds =  PJ.grid_getMutlSelectedRowkey(grid);
			 for(var i=0, len=taskIds.length; i<len; i++) {
				var  id = taskIds[i];
				var rowData=grid.getRowData(id);
				ids+=","+rowData.importPackageElementId;
	         }
			
			 //if(ids.length>0){
			//		postData.ids = ids;
				//}
		 }
		
		 if(obj.length>1&&rowKey== ""){
			 PJ.warn("请选择需要操作的数据");
				return false;
		 }else if(parseInt(obj.length)==1){
			 ids=obj[0].importPackageElementId;
		 }
		 return ids;
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
	<div position="center" title=""  >
		<div id="toolbar"></div>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>

</body>
</html>