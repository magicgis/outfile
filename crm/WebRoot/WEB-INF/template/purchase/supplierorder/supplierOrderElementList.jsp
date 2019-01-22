<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商订单明细管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {	
		
		layout = $("#layout1").ligerLayout();
		
	$("#submit").click(function(){
		
		var url = '<%=path%>/purchase/supplierorder/uploadExcel?clientOrderId='+'${clientOrderId}';
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
		
		if(${clientId}==65){
			$("#toolbar").ligerToolBar({
				items : [
				         {
				        	id:'add',
							icon : 'add',
							text : '新增订单',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var clientQuoteElementId = ret["clientQuoteElementId"];
								var iframeId="idealframe11";
								var orderNumber = '${orderNumber}';
								var currencyId = ret["currencyId"];
								var quoteNumber = ret["quoteNumber"];
								var exchangeRate = ret["exchangeRate"];
								var quotePartNumber = ret["quotePartNumber"];
								PJ.topdialog(iframeId, '采购-新增供应商订单明细 ', '<%=path%>/purchase/supplierorder/addSupplierOrder?id='+id+'&clientQuoteElementId='+clientQuoteElementId
										+'&orderNumber='+orderNumber+'&currencyId='+currencyId+'&exchangeRate='+exchangeRate+'&quoteNumber='+quoteNumber+'&quotePartNumber='+quotePartNumber+'&type=order',
										undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1100, 500, true);
							}
				         },
				         {
					        	id:'add',
								icon : 'add',
								text : '新增库存明细',
								click: function(){
									var iframeId="idealframe12";
									PJ.topdialog(iframeId, '采购-新库存订单明细 ', '<%=path%>/purchase/supplierorder/toAddStorageElement?clientOrderId='+${clientOrderId},
									undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);

				         		}
				         }
				         ]
			});
		}else{
			$("#toolbar").ligerToolBar({
				items : [
				         {
				        	id:'add',
							icon : 'add',
							text : '新增',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var clientQuoteElementId = ret["clientQuoteElementId"];
								var iframeId="idealframe11";
								var orderNumber = '${orderNumber}';
								var currencyId = ret["currencyId"];
								var quoteNumber = ret["quoteNumber"];
								var exchangeRate = ret["exchangeRate"];
								var quotePartNumber = ret["quotePartNumber"];
								PJ.topdialog(iframeId, '采购-新增供应商订单明细 ', '<%=path%>/purchase/supplierorder/addSupplierOrder?id='+id+'&clientQuoteElementId='+clientQuoteElementId
										+'&orderNumber='+orderNumber+'&currencyId='+currencyId+'&exchangeRate='+exchangeRate+'&quoteNumber='+quoteNumber+'&quotePartNumber='+quotePartNumber,
										undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1100, 500, true);
							}
				         },
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
		}
		
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/purchase/supplierorder/ListClientOrderElement?clientOrderId='+${clientOrderId},
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-172,
			subGrid:true,
			jsonReader:{  
			    repeatitems : true,
			    subgrid: {
			      root:"subgridchid", //子级的内容 Action类中必须有与之匹配的属性
			      repeatitems: false  //false之后 subGridModel的mapping才起作用
			   }            
			},
			subGridUrl:"<%=path%>/purchase/supplierorder/ListSupplierOrderElement",
			subGridModel:[
				{name:["供应商订单号","供应商","订单时间","数量","单价	","总价","周期","截止时间"],
				width:[120,100,100,100,100,100,100,100],
			    align: ['center','center','center','center','center','center','center','center']}
			],
			subGridOptions:{expandOnLoad:true},
			autoSrcoll:true,
			shrinkToFit:false,
			//sortname : "",
			colNames :["id","客户报价明细ID","item","csn","件号","location","描述","单位","数量","备注","库存数量","周期","截止日期","供应商","供应商订单条数","货币ID","汇率","询价单号"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientQuoteElementId", {hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:39,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:32,align:'left'}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:65,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:110,align:'left'}),
			           PJ.grid_column("quoteUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("clientOrderAmount", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("storageAmount", {sortable:true,width:60 ,align:'left'}),
			           PJ.grid_column("clientOrderLeadTime", {sortable:true,width:38,align:'left'}),
			           PJ.grid_column("clientOrderDeadline",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("supplierCode",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("supplierOrderCount",{sortable:true,width:100,align:'left',
			        	   formatter: ColorFormatter
			           }),
			           PJ.grid_column("currencyId",{hidden:true}),
			           PJ.grid_column("exchangeRate",{hidden:true}),
			           PJ.grid_column("quoteNumber",{hidden:true})
			           ]
		});
		
		function OrderFormatter(cellvalue, options, rowObject){
			var amount = rowObject["clientOrderAmount"];
			
			switch (amount) {
				case '':
					return "无订单";
					break;
					
				default: 
					return cellvalue;
					break; 
			}
		}
		
		function ColorFormatter(cellvalue, options, rowObject){
			switch (cellvalue) {
				case '':
					return cellvalue;
					break;
				case 0:
					return cellvalue;
					break;	
				default: 
					return '<span style="color:red">'+cellvalue+'<span>';
					break; 
			}
		}
		
		function CountFormatter(cellvalue, options, rowObject){
			switch (cellvalue) {
			case 0:
				return '0';
				break;
				
			case '':
				return "无订单";
				break;
				
			default: 
				return cellvalue.substr(0, 1);
				break; 
			}
			
		}
		
		//二级表头
		$( "#list1" ).jqGrid( 'setGroupHeaders' , {
			useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
			groupHeaders : [ {
								startColumnName :  'id' ,  // 对应colModel中的name
								numberOfColumns : 20,  // 跨越的列数
								titleText :  '<div align="center"><span>客户订单</span></div>'
							 }
							]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid.setGridHeight(PJ.getCenterHeight()-162);
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
	
	function CountFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '0';
			break;
			
		case '':
			return "无订单";
			break;
			
		default: 
			return cellvalue;
			break; 
	}
	}
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:20px;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
/*  th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
} */
</style>
</head>

<body>
	<div id="layout1">
		<div position="center" title="订单号   ${orderNumber }">
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