<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>未订货清单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />

<script type="text/javascript">
var layout, grid;

$(function(){
	layout1 = $("#layout1").ligerLayout({
		centerBottomHeight: 210,
		onEndResize:function(e){
		GridResize();
		}
	});
	
	$("#toolbar").ligerToolBar({
		items : [  
					{
						id:'search',
						icon : 'search',
						text : '展开搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
								var display = $("#searchdiv").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid1.setGridHeight(PJ.getCenterHeight()-202);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid1.setGridHeight(PJ.getCenterHeight()-102);
								}
							});
						}
				 	},
				 	{	icon : 'logout',
						text : '导出excel',
						click : function() {
								PJ.grid_export("list1");
							}
					}
				]
	});
	
	$("#toolbar2").ligerToolBar({
		items : [
					{	icon : 'logout',
						text : '导出excel',
						click : function() {
								PJ.grid_export("list2");
							}
					}
		         ]
	});
	
	/* $("#exact").change(function(){
		if($("#exact").prop("checked")){
			$("#partNumber").attr("oper","eq");
		}else if(!$("#exact").prop("checked")){
			$("#partNumber").attr("oper","cn");
		}
	}); */
	
	grid1 = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/purchase/supplierorder/NoOrder',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-142,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "co.order_date",
		sortorder : "desc",
		onSelectRow:function(rowid,status,e){
			onSelect();
		},
		pager: "#pager1",
		colNames :["id","订单号","客户订单号","客户","机型","订单日期"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("orderNumber", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("sourceNumber", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("clientCode", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("airCode", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("orderDate", {sortable:true,width:100,align:'left'}),
		           ]
	});
	
	
	grid2 = PJ.grid("list2", {
		rowNum: 20,
		url:'<%=path%>/purchase/supplierorder/NoOrderElement?id=0',
		width : PJ.getCenterBottomWidth(),
		height : PJ.getCenterBottomHeight(),
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "coe.deadline",
		sortorder : "desc",
		pager: "#pager2",
		colNames :["id","客户","询价单号","item","csn","件号","描述","单位","客户订单数量","数量","周期","订单日期","截至日期"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("code", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("quoteNumber", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("item", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("csn", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("partNumber", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("description", {sortable:true,width:140,align:'left'}),
		           PJ.grid_column("unit", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("clientOrderAmount", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("amount", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("leadTime", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("orderDate", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("deadline", {sortable:true,width:100,align:'left'})
		           ]
	});
	
	//改变窗口大小自适应
	$(window).resize(function() {
		GridResize();
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
	
		
	function GridResize() {
		grid1.setGridWidth(PJ.getCenterWidth());
		grid2.setGridWidth(PJ.getCenterBottomWidth());
		grid1.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
		grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
	}
	grid1.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
	grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid1);
		var id = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/purchase/supplierorder/NoOrderElement?id='+id)};
	
	
	function CountFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '0';
			break;
			
		default: 
			return cellvalue;
			break; 
	}
	}
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid1,'<%=path%>/purchase/supplierorder/NoOrder');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid1,'<%=path%>/purchase/supplierorder/NoOrder');
		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	$("#orderNumber").blur(function(){
		var orderNumber = $("#orderNumber").val();
		$("#orderNumber").val(trim(orderNumber));
	});
	
});

function trim(str){
	return $.trim(str);
}

</script>
</head>

<body>
<div id="layout1">
		<div position="center">
			<div id="toolbar"></div>
			<form id="searchForm" action="">
				<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
					<div class="search-box">
						<form:row columnNum="4">
							<form:column>
								<form:left><p>客户</p></form:left>
								<form:right>
											<p>
												<select id="searchForm1" name="clientCode" alias="c.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>机型</p></form:left>
								<form:right>
											<p>
													<select id="searchForm3" name="airTypeValue" alias="sc.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>订单日期</p></form:left>
								<form:right><p><input id="orderStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'orderEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="orderStart" alias="co.order_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="orderEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderStart\')}',dateFmt:'yyyy-MM-dd'})" name="orderEnd" alias="co.order_date" oper="lt"/></form:right>
							</form:column>
						</form:row>	
						<form:row columnNum="5">
							<form:column>
								<form:left>订单号：</form:left>
								<form:right><input id="orderNumber" class="text" type="text" name="orderNumber" alias="co.ORDER_NUMBER or co.SOURCE_NUMBER" oper="cn"/></form:right>
							</form:column>
							<form:column>
								<form:left>件号：</form:left>
								<form:right><input id="partNumber" class="text" type="text" name="partNumber" alias="coe.PART_NUMBER" oper="cn"/></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column>
								<p style="padding-left:40px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
								</p>
							</form:column>
						</form:row>
					</div>
				</div>
			</form>
				
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
		<div position="centerbottom">
		<div id="toolbar2"></div>
			<table id="list2"></table>
			<div id="pager2"></div>
		</div>
</div>

</body>
</html>