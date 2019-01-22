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
	
$("#submit").click(function(){
$("#submit").click(function(){
		
		var url = '<%=path%>/sales/clientorder/updateWeatherOrderuploadExcel?clientOrderId='+'${clientOrderId}';
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
	            	closeoropen();
	            	PJ.grid_reload(grid);
            	}
            	else{
            		iframeId = 'errorframe'
                		PJ.topdialog(iframeId, '错误信息', '<%=path%>/clientquote/toUnknow',
                				undefined,function(item,dialog){
    		            			$.ajax({
    									url: '<%=path%>/clientquote/deleteData',
    									type: "POST",
    									loading: "正在处理...",
    									success: function(result){
    									}
    								});
                					dialog.close();}, 1000, 500, true);
            		closeoropen();
    	           
            	}
   				
            },  
            error: function (data, status, e) { 
            	PJ.error("上传异常！");
            	closeoropen();
            }  
        });  
		
 
	});	
 
	});	
	 $("#toolbar").ligerToolBar({
			items : [{
	        	id:'add',
				icon : 'add',
				text : '修改',
				click: function(){
					var ret = PJ.grid_getSelectedData(grid);
					var id = ret["clientOrderElementId"];
					var clientQuoteElementId = ret["clientQuoteElementId"];
					var iframeId="idealframe11";
					var orderNumber = '${orderNumber}';
					var currencyId = ret["currencyId"];
					var quoteNumber = ret["quoteNumber"];
					var exchangeRate = ret["exchangeRate"];
					var quotePartNumber = ret["quotePartNumber"];
					var supplierWeatherOrderElementId = ret["supplierWeatherOrderElementId"];
					PJ.topdialog(iframeId, '采购-修改供应商预订单明细 ', '<%=path%>/purchase/supplierorder/addSupplierOrder?id='+id+'&clientQuoteElementId='+clientQuoteElementId
							+'&orderNumber='+orderNumber+'&currencyId='+currencyId+'&exchangeRate='+exchangeRate+'&quoteNumber='+quoteNumber+'&quotePartNumber='+quotePartNumber
							+'&weather='+'update'+'&supplierWeatherOrderElementId='+supplierWeatherOrderElementId,
							undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1100, 500, true);
				}
	         },{
					id:'down',
					icon : 'down',
					text : '供应商预订单下载',
					click: function(){
						var obj = $("#list1").jqGrid("getRowData");
						var id=0+"-"+obj[0].supplierWeatherOrderElementId;
					 	 for(var i=1, len=obj.length; i<len; i++) {
					 		var supplierWeatherOrderElementId= obj[i].supplierWeatherOrderElementId;
					 		id+=","+supplierWeatherOrderElementId;
					 	 }

						//根据具体业务提供相关的title
						var title = '供应商预订单下载';
						//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
						//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
						var businessKey = "";
						
							businessKey='update_supplier_weather_order.clientOrderId.'+id+'.UpdateSupplierWeatherOrderExcel';
						
						PJ.excelDiaglog({
							data:'businessKey='+businessKey,
							title: title,
							add:true,
							remove:true,
							download:true
						});
			 	}
	         }  ,
				{
					id:'search',
					icon : 'search',
					text : '展开文件上传',
					click: function(){
						closeoropen();
					}
				}
			         ]
		});

	
	grid = PJ.grid("list1", {
		rowNum: 100,
		rowList: [100,500,1000],
		url:'<%=path%>/sales/clientorder/updateSupplierOrderPage?clientOrderElementId='+'${clientOrderElementId}'+'&taskId='+'${taskId}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-160,
		autoSrcoll:true,
		multiselect:true,
		editurl:'<%=path%>/sales/clientorder/updateSupplierPrice',
		inLineEdit: true,
		//shrinkToFit:false,
	colNames :["id","taskId","clientOrderElementId","supplierweatherOrderId","件号","描述","流程图","供应商","供应商预订单价格","数量","周期","截至日期","经办意见","clientQuoteElementId","currencyId",
	           "quoteNumber","exchangeRate","quotePartNumber"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("taskId", {key:true,editable:true,hidden:true}),
			           PJ.grid_column("clientOrderElementId", {hidden:true}),
			           PJ.grid_column("supplierWeatherOrderElementId", {editable:true,hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:100,align:'left'}),
			          
			           PJ.grid_column("spzt", {sortable:true,width:30,formatter : cbspztFormatter}),
			           PJ.grid_column("code", {sortable:true,width:30,align:'left',editable:true}),
			           PJ.grid_column("price", {sortable:true,width:60,align:'left',editable:true}),
			           PJ.grid_column("amount", {sortable:true,width:30,align:'left',editable:true}),
			           PJ.grid_column("leadTime", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("deadline", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("jbyj", {sortable:true,width:100,editable:true,align:'left', editoptions:{style:"width:100px;height:30px;"}}),
			           PJ.grid_column("clientQuoteElementId", {sortable:true,width:30,hidden:true}),
			           PJ.grid_column("currencyId", {sortable:true,width:30,align:'left',hidden:true}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:30,align:'left',hidden:true}),
			           PJ.grid_column("exchangeRate", {sortable:true,width:30,align:'left',hidden:true}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:30,align:'left',hidden:true}),			          
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

function closeoropen(){
	$("#uploadBox").toggle(function(){
		var display = $("#uploadBox").css("display");
		if(display=="block"){
			$("#toolbar > div[toolbarid='search'] > span").html("收起文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-220);
		}else{
			$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-182);
		}
	});
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