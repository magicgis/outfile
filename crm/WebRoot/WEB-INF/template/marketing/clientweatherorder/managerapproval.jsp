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
			items : [
						
			         ]
		});

	
	grid = PJ.grid("list1", {
		rowNum: 100,
		rowList: [100,500,1000],
		url:'<%=path%>/market/clientweatherorder/updateClientOrderPage?clientOrderElementId='+'${clientOrderElementId}'+'&taskId='+'${taskId}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-180,
		autoSrcoll:true,
		multiselect:true,
		<%-- onSelectRow:function(rowid,result){
			 var obj = $("#list1").jqGrid("getRowData");
			
			var orderStatusId = obj[0].orderStatusId;
			var orderStatusValue = obj[0].orderStatusValue;
			var postData = {};
			postData.id = rowid;
			
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
		}, --%>
		aftersavefunc:function(rowid,result){
			PJ.grid_reload(grid);
		},
		editurl:'<%=path%>/market/clientweatherorder/updateElement',
		inLineEdit: true,
		shrinkToFit:false,
	colNames :["id","taskId","clientOrderElementId","件号","描述","数量","MOQ","单价","佣金","银行费用","备注","供应商","价格","银行费用","提货换单费","Hazmat Fee",
	           "利润率","供应商","价格","银行费用","提货换单费","杂费","Hazmat Fee","利润率","利润","利润标准","数量","周期","截至日期","流程图","经办意见","clientQuoteElementId","currencyId",
	           "quoteNumber","exchangeRate","quotePartNumber"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("taskId", {key:true,editable:true,hidden:true}),
			           PJ.grid_column("clientOrderElementId", {editable:true,hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:110,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("orderAmount", {sortable:true,width:65,align:'left',editable:true}),
			           PJ.grid_column("clientMoq", {sortable:true,width:65,align:'left'}),
			           PJ.grid_column("orderPrice", {sortable:true,width:65,align:'left',editable:true}),
					   PJ.grid_column("fixedCost", {sortable:true,width:60,align:'left'}),
					   PJ.grid_column("bankCharges", {sortable:true,width:80,align:'left'}),
					   PJ.grid_column("orderRemark", {sortable:true,width:150,align:'left'}),
					   PJ.grid_column("supplierCode",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("quotePrice", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("quoteBankCost", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("quoteFeeForExchangeBill", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("quoteHazmatFee", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("weatherQuoteprofitMargin", {sortable:true,width:70,align:'left'
			        	   ,formatter:function(value){
								if(value){
									return value.toFixed(3)+"%";
								}
								else{
									return value;
								}
							},hidden:true}),
					   PJ.grid_column("code",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("price", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("bankCost", {sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("feeForExchangeBill", {sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("otherFee", {sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("hazmatFee", {sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("weatherOrderprofitMargin", {sortable:true,width:70,align:'left'
			        	   ,formatter:function(value){
			        		   if(value=="NaN"){
			        			   return "";
			        		   }
			        		   else if(value){
									return value.toFixed(3)+"%";
								}
								else{
									return value;
								}
							}}),
					   PJ.grid_column("grossProfitAmount", {sortable:true,width:80,align:'left' ,formatter:function(value){
									if(value){
										return value.toFixed(2);
									}
									else{
										return value;
									}
							}}),
							PJ.grid_column("clientProfitMargin", {sortable:true,width:70,align:'left'
					        	   ,formatter:function(value){
								if(value>0){
								return value.toFixed(3)+"%";
								}
								else{
								return value;
							}
						}}),
			           PJ.grid_column("amount", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("leadTime", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("deadline", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("spzt", {sortable:true,width:50,formatter : cbspztFormatter}),
			           PJ.grid_column("jbyj", {sortable:true,width:150,editable:true,align:'left', editoptions:{style:"width:100px;height:30px;"}}),
			           PJ.grid_column("clientQuoteElementId", {sortable:true,width:30,hidden:true}),
			           PJ.grid_column("currencyId", {sortable:true,width:30,align:'left',hidden:true}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:30,align:'left',hidden:true}),
			           PJ.grid_column("exchangeRate", {sortable:true,width:30,align:'left',hidden:true}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:30,align:'left',hidden:true}),
			         
			           ]
	});
	 grid.jqGrid('setGroupHeaders', { 
		 	useColSpanStyle: true, 
		 	groupHeaders:[ 
		 		{startColumnName: 'id', numberOfColumns:11, titleText: '<div align="center"><span>客户预订单</span></div>'},
		 		{startColumnName: 'supplierCode', numberOfColumns: 6, titleText: '<div align="center"><span>预报价供应商</span></div>'},
		 		{startColumnName: 'code', numberOfColumns: 12, titleText: '<div align="center"><span>供应商预订单</span></div>'},
		 		{startColumnName: 'spzt', numberOfColumns: 7, titleText: '<div align="center"><span>流程</span></div>'},
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
	var partNumber=rowObject["partNumber"];
	if(partNumber=="总计"){
		return;
	}
	
	
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
	/*  if(rowKey!= ""){
		 var id =  PJ.grid_getMutlSelectedRowkey(grid);
		 ids = id.join(",");
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
			 if(typeof(data.taskId) != "undefined"){
				 ids+=data.taskId+",";
			 }
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
		<div id="toolbar"></div>
		<div id="uploadBox" style="width: 100%;height: 30px;display: none;">
	<form id="form" name="form">
			<form:row columnNum="2">
				<form:column width="120">
					<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
					<form:right>
						<p><input type="file" value="" id="file" name="file"/>&nbsp;
						   <input type="button" id="submit" value="上传"/>
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