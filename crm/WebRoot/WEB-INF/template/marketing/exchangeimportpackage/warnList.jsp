<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>未发货清单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
		items : [ {	icon : 'logout',
					text : '导出excel',
					click : function() {
							PJ.grid_export("list1");
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
									grid.setGridHeight(PJ.getCenterHeight()-152);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-102);
								}
							});
						}
				 }
				]
		
	});
		
	$("#exact").change(function(){
			if($("#exact").prop("checked")){
				$("#partNumber").attr("oper","eq");
			}else if(!$("#exact").prop("checked")){
				$("#partNumber").attr("oper","cn");
			}
	});
		
	grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exchangeimport/warnList?exchange=yes',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			sortname : "co.order_date",
			colNames :["订单明细id","客户","订单号","件号","订单时间","未收数量"],
			colModel :[PJ.grid_column("clientOrderElementId", {key:true,hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("orderDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("amount",{sortable:true,width:80,align:'left'})
			          ]
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
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/exchangeimport/warnList');
		    }  
		}); 
		
		//搜索
		$("#searchBtn").click(function(){
			PJ.grid_search(grid,'<%=path%>/storage/exchangeimport/warnList');
			
		});
		
		//重置条件
		$("#resetBtn").click(function(){
			$("#searchForm")[0].reset();
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		$("#orderNumber").blur(function(){
			var text = $("#orderNumber").val();
			$("#orderNumber").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
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
	
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var data = $("#form").serialize();
		 return data;
	}
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 40px;display: none;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="5">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="orderNumber" alias="co.order_number or co.source_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="c.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="co.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="co.order_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column >
							<p style="padding-left:60px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
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