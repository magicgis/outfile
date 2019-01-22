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
		
		var url = '<%=path%>/market/clientweatherorder/weatherOrderuploadExcel?clientOrderId='+'${clientOrderId}'+'&taskdefname='+'${taskdefname}';
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
	 $("#toolbar").ligerToolBar({
			items : [
			         {
			        	id:'edit',
						icon : 'edit',
						text : '更换供应商',
						click: function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["clientOrderElementId"];
							var clientQuoteElementId = ret["clientQuoteElementId"];
							var supplierWeatherOrderElementId = ret["supplierWeatherOrderElementId"];
							var iframeId="idealframe11";
							var orderNumber = '${orderNumber}';
							var currencyId = ret["currencyId"];
							var quoteNumber = ret["quoteNumber"];
							var exchangeRate = ret["exchangeRate"];
							var quotePartNumber = ret["quotePartNumber"];
							PJ.topdialog(iframeId, '采购-更换供应商 ', '<%=path%>/purchase/supplierorder/addSupplierOrder?id='+id+'&clientQuoteElementId='+clientQuoteElementId
									+'&orderNumber='+orderNumber+'&currencyId='+currencyId+'&exchangeRate='+exchangeRate+'&quoteNumber='+quoteNumber+'&quotePartNumber='+quotePartNumber
									+'&weather='+'update'+'&supplierWeatherOrderElementId='+supplierWeatherOrderElementId,
									undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1100, 500, true);
						}
			         },{
							id:'down',
							icon : 'down',
							text : '供应商预订单下载',
							click: function(){
								/* var obj = $("#list1").jqGrid("getRowData");
								var id=0+"-"+obj[0].clientOrderElementId;
							 	 for(var i=1, len=obj.length; i<len; i++) {
							 		var clientOrderElementId= obj[i].clientOrderElementId;
							 		id+=","+clientOrderElementId;
							 	 } */

								//根据具体业务提供相关的title
								var title = '供应商预订单下载';
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var businessKey = "";
								var id=0+"-"+'${clientOrderElementId}';
									businessKey='supplier_weather_order.clientOrderId.'+id+'.SupplierWeatherOrderExcel';
								
								PJ.excelDiaglog({
									data:'businessKey='+businessKey,
									title: title,
									add:true,
									remove:true,
									download:true
								});
					 	}
			         } ,
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
		url:'<%=path%>/market/clientweatherorder/supplierWeatherOrderDate?clientOrderElementId='+'${clientOrderElementId}'+'&taskId='+'${taskId}'+'&taskdefname='+'${taskdefname}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-180,
		onSelectRow:function(rowid,result){
				var postData = {};
				postData.id = rowid;
				$.ajax({
					url: '<%=path%>/purchase/supplierorder/getShipWay?IfOrder=0',
					data: postData,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								shipway = result.message;
							}
					}
				});
				
				$.ajax({
					url: '<%=path%>/purchase/supplierorder/destinationList?IfOrder=0',
					data: postData,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								destination = result.message;
							}
					}
				});
		},
		autoSrcoll:true,
		multiselect:true,
		inLineEdit: true,
		editurl:'<%=path%>/market/clientweatherorder/updateSupplierPrice',
		shrinkToFit:false,
	colNames :["id","taskId","clientOrderElementId","clientWeatherOrderId","item","件号","描述","单位","数量","备注","供应商","Hazmat Fee","提货换单费","银行手续费","单价","supplierQuoteElementId","supplierWeatherOrderElementId","状态","供应商","银行手续费","价格","数量","备注","周期","截至日期","收货地址","运输方式","流程图","经办意见","clientQuoteElementId","currencyId",
	           "quoteNumber","exchangeRate","quotePartNumber"],
			colModel :[PJ.grid_column("id", {key:true,editable:true,hidden:true}),
			           PJ.grid_column("taskId", {key:true,editable:true,hidden:true}),
			           PJ.grid_column("clientOrderElementId", {editable:true,hidden:true}),
			           PJ.grid_column("clientWeatherOrderId", {editable:true,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("orderUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderRemark", {sortable:true,width:140,align:'left'}),
			       		
			           PJ.grid_column("code", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quoteHazmatFee", {sortable:true,width:60,align:'left',
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			           }),
			           PJ.grid_column("quoteFeeForExchangeBill", {sortable:true,width:60,align:'left',
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			           }),
			           PJ.grid_column("quoteBankCost", {sortable:true,width:60,align:'left',
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			           }),
			           PJ.grid_column("quotePrice", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("supplierQuoteElementId", {editable:true,hidden:true}),
			           PJ.grid_column("supplierWeatherOrderElementId", {editable:true,hidden:true}),
			           PJ.grid_column("supplierStatus", {sortable:true,width:60,align:'left',
			        		edittype:"select",formatter:function(value){
			        			if(value=='1'){
			        				return "执行中";
			        			}else if(value=='0'){
			        				return "取消";
			        			}
			        		}}),
			           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("bankCost", {sortable:true,width:60,align:'left',editable:true,
			        	   formatter:function(value){
								if (value) {
									return "$"+value;
								}
						    }   
			           }),
			           PJ.grid_column("price", {sortable:true,width:60,align:'left',editable:true}),
			           PJ.grid_column("amount", {sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("remark", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("leadTime", {sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("deadline", {sortable:true,width:80,align:'left',
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   }}),
			           PJ.grid_column("destinationValue", {sortable:true,width:80,align:'left',
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return destination;        			   
		        		   		}
							}}),
			           PJ.grid_column("shipWayValue", {sortable:true,width:80,align:'left',
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return shipway;        			   
		        		   		}
							}}),
			           PJ.grid_column("spzt", {sortable:true,width:60,formatter : cbspztFormatter}),
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
	 		{startColumnName: 'id', numberOfColumns: 9, titleText: '<div align="center"><span>客户预订单</span></div>'},
	 		{startColumnName: 'code', numberOfColumns:  5, titleText: '<div align="center"><span>预报价供应商</span></div>'},
	 		{startColumnName: 'supplierStatus', numberOfColumns: 10, titleText: '<div align="center"><span>供应商预订单</span></div>'},
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