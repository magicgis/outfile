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

var name ,size;
$(function(){
	layout = $("#layout1").ligerLayout();
	
$("#submit").click(function(){
		
		var url = '<%=path%>/market/clientweatherorder/finaluploadExcel?clientOrderId='+'${clientOrderId}';
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
            		PJ.grid_reload(grid);
	            	PJ.success(da.message);
	            	closeoropen();
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
					icon : 'view',
					text : '供应商预订单下载',
					click : function(){
						supplierWeatherOrder();
							}
						},
				{
						icon : 'view',
						text : '客户预订单下载',
						click : function(){
							Excel();
								}
							},{
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
			url:'<%=path%>/market/clientweatherorder/clientOrderFinalDate?clientOrderElementId='+'${clientOrderElementId}'+'&taskId='+'${taskId}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-180,
			autoSrcoll:true,
			multiselect:true,
		<%-- 	onSelectRow:function(rowid,result){
				 var obj = $("#list1").jqGrid("getRowData");
				
				var orderStatusId = obj[0].orderStatusId;
				var orderStatusValue = obj[0].orderStatusValue;
				var certificationId= obj[0].certificationId;
				var certificationCode= obj[0].certificationValue;
				var postData = {};
				postData.id = rowid;
				
			 	$.ajax({
					type: "POST",
					data: postData,
					url:'<%=path%>/purchase/supplierorder/findorderStatus?orderStatusId='+orderStatusId,
					success:function(result){
						if(result.success){
							if(""==orderStatusValue){
								os = result.message;
							}else{
								os =orderStatusId+":"+orderStatusValue+";"+ result.message;
							}
							
						}
					}
				});
			 	
			 	//证书
			 	$.ajax({
					type: "POST",
					data: postData,
					url:'<%=path%>/supplierquote/findcert?certificationId='+certificationId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							if(""==certificationCode){
								cert = result.message;
							}else{
								cert =certificationId+":"+certificationCode+";"+ result.message;
							}
							
						}
					}
				}); 
			}, --%>
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			editurl:'<%=path%>/market/clientweatherorder/insertElement',
			inLineEdit: true,
			shrinkToFit:false,
			colNames :["id","taskId","clientOrderElementId","item","csn","件号","描述","单价","单位","数量","MOQ","佣金","银行费用","备注","供应商","单价","Hazmat Fee","银行费用","提货换单费","杂费","利润率","状态","供应商","单价","数量","Hazmat Fee","银行费用","提货换单费","杂费","总价","利润率","利润标准","备注","数量","数量","数量","单价",
			           "订单序号","状态","周期","佣金","银行费","证书id","证书","证书","流程图","经办意见"],
					colModel :[  PJ.grid_column("id", {key:true/* ,editable:true */,hidden:true}),
	           	                  PJ.grid_column("taskId", {key:true,editable:true,hidden:true}),
	          			           PJ.grid_column("clientOrderElementId", {editable:true,hidden:true}),
	          			           PJ.grid_column("item", {sortable:true,width:30,align:'left'}),
	          			    	   PJ.grid_column("csn", {sortable:true,width:30,align:'left'}),
		       			           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
		       			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
		       			           PJ.grid_column("orderPrice", {sortable:true,width:60,align:'left'}),
		       			           PJ.grid_column("orderUnit", {sortable:true,width:40,align:'left'}),
		       			           PJ.grid_column("orderAmount", {sortable:true,width:40,align:'left'}),
		       			           PJ.grid_column("clientMoq", {sortable:true,width:40,align:'left'}),
		       			   		   PJ.grid_column("fixedCost", {sortable:true,width:50,align:'left'}),
		       			   		   PJ.grid_column("bankCharges", {sortable:true,width:50,align:'left'}),
		       			       	   PJ.grid_column("orderRemark", {sortable:true,width:140,align:'left'}),
		       			           PJ.grid_column("supplierCode",{sortable:true,width:50,align:'left'}),
						           PJ.grid_column("quotePrice", {sortable:true,width:60,align:'left',formatter:function(value){
										if(value>0){
											return value.toFixed(2);
										}
										else{
											return value;
										}
									}}),
								   PJ.grid_column("quoteHazmatFee", {sortable:true,width:50,align:'left'}),
								   PJ.grid_column("quoteBankCost", {sortable:true,width:50,align:'left'}),
			       			       PJ.grid_column("quoteFeeForExchangeBill", {sortable:true,width:60,align:'left'}),
			       			       PJ.grid_column("quoteOtherFee", {sortable:true,width:60,align:'left'}),
						           PJ.grid_column("weatherQuoteprofitMargin", {sortable:true,width:60,align:'left'
						        	   ,formatter:function(value){
											if(value){
												return value.toFixed(3)+"%";
											}
											else{
												return value;
											}
										}}),
								PJ.grid_column("supplierStatus", {sortable:true,width:50,align:'left',
							        		edittype:"select",formatter:function(value){
							        			if(value=='1'){
							        				return "执行中";
							        			}else if(value=='0'){
							        				return "取消";
							        			}
							        		}}),		
								PJ.grid_column("code",{sortable:true,width:50,align:'left'}),
						        PJ.grid_column("supplierOrderPrice", {sortable:true,width:70,align:'left',formatter:function(value){
										if(value){
											return value.toFixed(2);
										}
										else{
											return value;
										}
									}}),
								PJ.grid_column("weatherOrderAmount", {sortable:true,width:40,align:'left'}),
								PJ.grid_column("hazmatFee", {sortable:true,width:50,align:'left',hidden:true}),
								PJ.grid_column("bankCost", {sortable:true,width:50,align:'left'}),
								PJ.grid_column("feeForExchangeBill", {sortable:true,width:60,align:'left',hidden:true}),
								PJ.grid_column("otherFee", {sortable:true,width:50,align:'left',hidden:true}),
								PJ.grid_column("total", {sortable:true,width:70,align:'left'}),
						        PJ.grid_column("weatherOrderprofitMargin", {sortable:true,width:60,align:'left'
						        	   ,formatter:function(value){
											if(value){
												return value.toFixed(3)+"%";
											}
											else{
												return value;
											}
										}}),
								PJ.grid_column("clientProfitMargin", {sortable:true,width:60,align:'left'
								        	   ,formatter:function(value){
											if(value){
											return value.toFixed(3)+"%";
											}
											else{
											return value;
										}
									}}),
								PJ.grid_column("remark", {sortable:true,width:100,align:'left'}),
	       			        	   PJ.grid_column("storageAmount", {sortable:true,width:80,align:'left'}),
	       			     		   PJ.grid_column("onpassStorageAmount", {sortable:true,width:80,align:'left'}),
	       						   PJ.grid_column("amount", {sortable:true,width:50,align:'left',editable:true}),
	       	      				   PJ.grid_column("price", {sortable:true,width:80,align:'left',editable:true}),
	       	      				PJ.grid_column("orderNumberIndex", {sortable:true,width:70,align:'left',editable:true}),
	       	      				 PJ.grid_column("orderStatusId", {sortable:true,width:60,align:'left',editable:true,
		     							edittype:"select",formatter:function(value){
											if(value=="60"){
												return "执行中";
											}
											else if(value=="64"){
												return "客户取消合同";
											}
										},
		    							editoptions:{value:
		    								 "60:执行中;64:客户取消合同"}}),
	       						   PJ.grid_column("leadTime", {sortable:true,width:50,align:'left',editable:true}),
		       			     	   PJ.grid_column("finalFixedCost", {sortable:true,width:50,align:'left'}),
		       			     	   PJ.grid_column("finalBankCharges", {sortable:true,width:50,align:'left'}),
		       			           PJ.grid_column("certificationId", {sortable:true,width:50,align:'left',hidden:true}),
	       			     		   PJ.grid_column("certificationValue", {sortable:true,width:50,align:'left',hidden:true}),
	       						   PJ.grid_column("certificationValue", {sortable:true,width:120,align:'left',
	       				        	   edittype:"select",formatter:"",
	       			        		   editoptions:{value:
	       									function(){
	       											return cert;        			   
	       			        		   		}
	       								}}),
	       						    PJ.grid_column("spzt",  {sortable:true,width:50,formatter : cbspztFormatter}),
			       			     	PJ.grid_column("jbyj", {sortable:true,width:150,editable:true,align:'left', editoptions:{style:"width:100px;height:30px;"}})]
			});
				grid.jqGrid('setGroupHeaders', { 
					
				 	useColSpanStyle: true, 
				 	groupHeaders:[ 
				 		{startColumnName: 'taskId', numberOfColumns: 13, titleText: '<div align="center"><span>客户预订单</span></div>'},
				 		{startColumnName: 'supplierCode', numberOfColumns:  7, titleText: '<div align="center"><span>供应商预报价</span></div>'},
				 		{startColumnName: 'supplierStatus', numberOfColumns:  12, titleText: '<div align="center"><span>供应商预订单</span></div>'},
				 		{startColumnName: 'storageAmount', numberOfColumns: 1, titleText: '<div align="center"><span>库存订单</span></div>'},
				 		{startColumnName: 'onpassStorageAmount', numberOfColumns: 1, titleText: '<div align="center"><span>在途库存</span></div>'},
				 		{startColumnName: 'amount', numberOfColumns: 10, titleText: '<div align="center"><span>客户订单</span></div>'},
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

function name(){
	var name='$("#supplierCode").val()';
	 return name;
}
function size(){
	var size='$("#supplierCodeSize").val()';
	 return size;
}


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
	/*  if(rowKey!= ""){
		 var id =  PJ.grid_getMutlSelectedRowkey(grid);
		 ids = id.join(",");
		 //if(ids.length>0){
		//		postData.ids = ids;
			//}
	 } */
	 var obj = $("#list1").jqGrid("getRowData");
	
	/*  if(obj.length>1&&rowKey== ""){
		 PJ.warn("请选择需要操作的数据");
			return false;
	 }else */ if(parseInt(obj.length)==1){
		/*  var price=obj[0].price
		 if(null!=price&&price!=""){  */
			 ids=obj[0].taskId;
		 /* } else{
			 PJ.warn("请先生成订单再提交任务");
				return false;
		 } */
	 }else{
	 for(var i=0, len=rowKey.length; i<len; i++) {
			var row_id= rowKey[i];
			 var data = grid.jqGrid("getRowData",row_id);
			/*  var price=data.price
			 if(null!=price&&price!=""){  */
				 ids+=data.taskId+",";
			 /*  }else{
				 PJ.warn("请先生成订单再提交任务");
					return false;
			 } */
	     }
	 }
	 return ids;
 }	
 
function Excel(){
	//根据具体业务提供相关的title
	var title = 'excel管理';
	//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
	//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现

 	var obj = $("#list1").jqGrid("getRowData");
	var id=0+"-"+obj[0].clientOrderElementId;
 	 for(var i=1, len=obj.length; i<len; i++) {
 		var clientOrderElementId= obj[i].clientOrderElementId;
 		id+=","+clientOrderElementId;
 	 }
	var businessKey = 'client_order_final.id.'+id+'.ClientOrderFinalExcel';
	
	PJ.excelDiaglog({
		data:'businessKey='+businessKey,
		title: title,
		add:true,
		remove:true,
		download:true
	});
}

function supplierWeatherOrder(){
	//根据具体业务提供相关的title
	var title = 'excel管理';
	//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
	//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
	var businessKey = 'client_order_supplier_quote.id.'+'${clientOrderId}'+'.ClientOrderSupplierQuoteExcel';
	
	PJ.excelDiaglog({
		data:'businessKey='+businessKey,
		title: title,
		add:true,
		remove:true,
		download:true
	});
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
		<input type="text" class="hide" name="size" id="size" value="${size}">
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