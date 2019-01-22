<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商到货清单</title>

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
		
		
		
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [  
							{
								icon : 'view',
								text : 'Excel管理',
								click : function(){
									Excel();
										}
									},
			          {
					id:'search',
					icon : 'search',
					text : '展开搜索',
					click: function(){
						$("#searchdiv").toggle(function(){
						var display = $("#searchdiv").css("display");
						if(display=="block"){
							$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
							grid.setGridHeight(PJ.getCenterHeight()-162);
						}else{
							$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
							grid.setGridHeight(PJ.getCenterHeight()-102);
						}
					});
				}
			}   ]
		});

		grid = PJ.grid("list", {
			rowNum: 20,
			autowidth:true,
			url:'<%=path%>/importpackage/importpackagedate?purchase='+'yes',
			width : PJ.getCenterWidth(),
			autoSrcoll:true,
			shrinkToFit:true,
			autowidth:true,
			height : PJ.getCenterHeight()-102,
			sortname : "ip.update_timestamp",
			sortorder: "desc",
			colNames : ["id","供应商id","币种id", "入库单号","供应商","入库日期","币种", "汇率","备注","更新时间"],
			colModel : [PJ.grid_column("id", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierId", {sortable:true,hidden:true}),
			            PJ.grid_column("currencyId", {sortable:true,hidden:true}),
			            PJ.grid_column("importNumber", {sortable:true,align:'left'}),
			            PJ.grid_column("supplierCode", {sortable:true,align:'left'}),
			            PJ.grid_column("importDate", {sortable:true,width:150,align:'left'}),
			            PJ.grid_column("currencyValue", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("exchangeRate", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("remark", {sortable:true,width:180,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:180,align:'left'}),
						],
						
		});
		
		//搜索条件是日期类型的加入日期控件
		PJ.datepickerAll();
		
		//右上角的帮助按扭float:right
		$("#toolbar > div[toolbarid='help']").addClass("fr");
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate?purchase='+'yes');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/importpackage/importpackagedate?purchase='+'yes');
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
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
	});
	
	
	function Excel(){
 		var ret = PJ.grid_getSelectedData(grid);
 		var id = ret["id"];
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = 'Supplier_arrival_list.id.'+id+'.SupplierArrivalListExcel'
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
			});
	}
	function trim(str){
		return $.trim(str);
	}
</script>
</head>

<body style="padding:3px">
	<div id="layout1">
		<div position="center">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
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
			<table id="list"></table>
			<div id="pager1"></div>
		
		</div>
	</div>
</body>
</html>
