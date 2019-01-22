<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>选择客户询价单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [
					 {
							id:'down',
							icon : 'down',
							text : '下载',
							click: function(){
								    //var ret = PJ.grid_getSelectedData(grid);
						 		    var id = getData();
									//根据具体业务提供相关的title
									var title = 'excel管理';
									//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
									//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
									var businessKey = 'lot_import_element.id.'+id+'.LotImportElementExcel';
									PJ.excelDiaglog({
										data:'businessKey='+businessKey,
										title: title,
										add:true,
										remove:true,
										download:true
									});
							}
						
					 }
			         ]
		});
		grid = PJ.grid("list1", {
			rowNum: 500,
			url:'<%=path%>/importpackage/importpackagedate',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-172,
			autoSrcoll:true,
			multiselect:true,
			//shrinkToFit:false,
			sortname : "ip.import_date",
			sortorder : "desc",
			colNames : ["id","供应商id","币种id", "入库单号","供应商","入库日期","币种", "汇率","物流方式","运输单号","状态","重量","备注","更新时间"],
			colModel : [PJ.grid_column("id", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierId", {sortable:true,hidden:true}),
			            PJ.grid_column("currencyId", {sortable:true,hidden:true}),
			            PJ.grid_column("importNumber", {sortable:true,align:'left'}),
			            PJ.grid_column("supplierCode", {sortable:true,align:'left'}),
			            PJ.grid_column("importDate", {sortable:true,width:150,align:'left'}),
			            PJ.grid_column("currencyValue", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("exchangeRate", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("logisticsWayValue", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("logisticsNo", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("importStatus", {sortable:true,width:60,align:'left',
							formatter: StatusFormatter	
						}),
						PJ.grid_column("weight", {sortable:true,width:50,align:'left',
							formatter: function(value){
								if(value){
									return value + "kg";
								}
								
						}}),
						PJ.grid_column("remark", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:180,align:'left'})
						]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate');
		    }  
		}); 
		
		$("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate');
			
		});
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
		//客户代码来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/clientCode',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm1").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//机型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/airType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
						$("#searchForm3").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findsid',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#supplier_code").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		});
		
		function StatusFormatter(cellvalue, options, rowObject){
			switch (cellvalue) {
			case 0:
				return '未完成';
				break;
				
			case 1:
				return '完成';
				break;
				
			default: 
				return cellvalue;
				break; 
			}
		}
		
	});
	
	function getData(){
		var postData = {};
		var id =  PJ.grid_getMutlSelectedRowkey(grid);
		ids = id.join(",");
		/* if(ids.length>0){
			postData.ids = ids;
		} */
		return ids;
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
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
<input type="text" name="isBlacklist" id="isBlacklist" class="hide"/>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
							       <form:left><p>入库单号：</p></form:left>
							     <form:right><p><input  id="searchForm2" type="text" style="width:150px" prefix="" name="ip.import_number" class="text" oper="cn" alias="ip.import_number" value=""/></p></form:right>
							   	</form:column>
							<form:column>
							      <form:left>供应商</form:left>
							   	<form:right><p><select style="width:150px" id="supplier_code" name="supplier_code" alias="s.id" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
								<form:column>
							          <form:left>入库日期：</form:left>
							  <form:right><p><input id="quotedatestart" style="width:150px" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'quotedateend\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="quotedatestart" alias="ip.import_date" oper="gt"/> 
							 </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left> 
							<form:right><p>
							<input id="quotedateend" style="width:150px" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'quotedatestart\')}',dateFmt:'yyyy-MM-dd'})" name="quotedateend" alias="ip.import_date" oper="lt"/> 
							</p></form:right>
							</form:column>
							<form:column >
							<form:right>
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>			
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>