<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>利润表</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
	var layout, grid;
	$(function() {
		
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchclient',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].client_code).text(obj[i].client_code);
						$("#client_code").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchairType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].air_type_code).text(obj[i].air_type_code+"-"+obj[i].air_type_value);
						$("#air_type_value").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchbizType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].biz_type_code).text(obj[i].biz_type_code);
						$("#biz_type_code").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [  
					{
						icon : 'view',
						text : 'Excel管理',
						click : function(){
							Excel();
								}
							}
			          ]
		});

		PJ.ajax({
			 url: '<%=path%>/clientquote/list/dynamicColNames?clientquoteid='+'${clientquoteid}'+'&type='+'quote',
			 dataType:'json',
	            loading: "正在加载...",
	            success: function(result) {
	            	var colNames = ["id","选中供应商","供应商价格","序号","件号", "描述","运费",  "库存数量","报价数量"];
	            	var colModel = [PJ.grid_column("id", {sortable:true,hidden:true}),
	            	                PJ.grid_column("supplierCode", {sortable:true,hidden:true,formatter:function(value){
	            	                	$("#supplierCode").val(value);
	            	                	return value;
	        						}}),
	            	                PJ.grid_column("quoteBasePrice", {sortable:true,hidden:true,formatter:function(value){
	            	                	$("#quoteBasePrice").val(value);
	            	                	return value;
	        						}}),
	        			            PJ.grid_column("item", {sortable:true,width:60}),
	        			            PJ.grid_column("inquiryPartNumber", {sortable:true}),
	        			            PJ.grid_column("inquiryDescription", {sortable:true,width:200}),
	        			            PJ.grid_column("freight", {sortable:true,width:60}),
	        						PJ.grid_column("storageAmount", {sortable:true,width:100}),
	        						PJ.grid_column("quoteAmount", {sortable:true,width:100}),
	        					];
	            	for(var index in result.columnDisplayNames){
						colNames.push(result.columnDisplayNames[index]);
						colModel.push(PJ.grid_column(result.columnKeyNames[index], {sortable:false,width:100,formatter:function(value,rowid){
							
								var name = rowid.colModel.name;
								var price=value;
								var quoteBasePrice=$("#quoteBasePrice").val();
								var supplierCode="supplier"+$("#supplierCode").val();
								if(name==supplierCode&&price==quoteBasePrice){
									return '<span style="color:red">'+value+'<span>';
								}
							
							if(value==.00){
								return 0;
							}
							else{
								return value;
							}
						}	}));
					}	   
	            	colNames.push("总价");
					colModel.push( PJ.grid_column("quoteTotalPrice", {sortable:true}));
					colNames.push("库存价格");
					colModel.push( PJ.grid_column("baseStoragePrice", {sortable:true}));
					colNames.push("客户报价");
					colModel.push( PJ.grid_column("clientQuoteBasePrice", {sortable:true}));
					colNames.push("佣金");
					colModel.push( PJ.grid_column("fixedCost", {sortable:true}));
					colNames.push("利润率");
					colModel.push(PJ.grid_column("profitMargin", {sortable:true
						,formatter:function(value){
							if(value){
								return value.toFixed(2)+"%";
							}
							else{
								return value;
							}
						}	
						}));
					
					
					grid = PJ.grid("list", {
	    				rowNum: '${rownum}',
	    				url:'<%=path%>/clientquote/list/data?clientquoteid='+'${clientquoteid}'+'&type='+'quote',
	    				autoSrcoll:true,
	    				shrinkToFit:true,
	    				width : PJ.getCenterWidth(),
	    				gridComplete:function(){
	    					var totalPrice=0.0;
	    					var bidPrice=0.0;
	    					var totalClientQuoteBasePrice=0.0;
	    					var totalOrderTotalPrice=0.0;
	    					var fixedCost=0.0;
	    					var obj = $("#list").jqGrid("getRowData");
	    					jQuery(obj).each(function(){
	    						if(!isNaN(parseFloat(this.quoteTotalPrice))){ 
	    						totalPrice+=parseFloat(this.quoteTotalPrice);
	    						}
	    						 if(!isNaN(parseFloat(this.orderBasePrice))){
	    							bidPrice+=parseFloat(this.quoteTotalPrice);
	    						}
	    						if(!isNaN(parseFloat(this.clientQuoteBasePrice))){
	    							totalClientQuoteBasePrice+=parseFloat(this.clientQuoteBasePrice)*parseFloat(this.quoteAmount);
		    					}
	    						if(!isNaN(parseFloat(this.orderTotalPrice))){
	    							totalOrderTotalPrice+=parseFloat(this.orderTotalPrice);
		    					}
	    						if(!isNaN(parseFloat(this.fixedCost))){
	    						fixedCost+=parseFloat(this.fixedCost)*this.quoteAmount;
	    						}
	    				    });
	    					
	    					var rowData=grid.getRowData(0);
	    					rowData.quoteTotalPrice=totalPrice.toFixed(2);
	    					rowData.clientQuoteBasePrice=totalClientQuoteBasePrice.toFixed(3);
	    					rowData.orderTotalPrice=totalOrderTotalPrice.toFixed(1);
	    					rowData.fixedCost=fixedCost;
	    				//	rowData.profitMargin=(((rowData.clientQuoteBasePrice-rowData.fixedCost-rowData.quoteTotalPrice)/rowData.clientQuoteBasePrice)*100);
	    					grid.setRowData(0,rowData);
	    					
	    				},
	    				height : PJ.getCenterHeight()-142,
	    				rowList:[10,20],
	    				colNames : colNames,
	    				colModel : colModel

	    			});
	            }
		});
	
		
		//搜索条件是日期类型的加入日期控件
		PJ.datepickerAll();
		
		//右上角的帮助按扭float:right
		$("#toolbar > div[toolbarid='help']").addClass("fr");
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/clientquote/clientquotedate');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/clientquote/clientquotedate');
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
		$("#test4_dropdown").hide();
	});
	
	function Excel(){
 		var clientquoteid =${clientquoteid};
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = 'client_quote_pro.id.'+clientquoteid+'.QuoteProfitExcel'
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
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
</head>

<body style="padding:3px">
	<div id="layout1">
		<div position="center" title="询价单号 ${clientInquiryQuoteNumber}">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<input  type="text" value="" class="" id="rowId"/>
					<input  type="text" value="" class="" id="supplierCode"/>
					<input  type="text" value="" class="" id="quoteBasePrice"/>
				</form>
			</div>
			<table id="list"></table>
			<div id="pager1"></div>
		
		</div>
	</div>
</body>
</html>
