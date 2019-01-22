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
	
	var layout,layout2;
	var grid,grid2;
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 300,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
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
									grid.setGridHeight(PJ.getCenterHeight()-202);
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
			url:'<%=path%>/sales/clientorder/unFinish',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			pager: "#pager1",
			autoSrcoll:true,
			sortname : "co.id",
			sortorder : "desc",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			colNames :["id","订单号","客户订单号","未发货比例","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("orderNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("sourceNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("terms", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:150,align:'left'})
			           ]
		});
	
	grid2 = PJ.grid("list2", {
		rowNum: 20,
		url:'<%=path%>',
		width : PJ.getCenterBottomWidth(),
		height : PJ.getCenterBottomHeight()-72,
		autoSrcoll:true,
		sortname : "cqe.update_timestamp",
		sortorder : "desc",
		pager: "#pager2",
		colNames :["id","件号","另件号","描述","订单数量","未出货数量","供应商","周期","截止日期","备注"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("quotePartNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("alterPartNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("inquiryDescription", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("clientOrderAmount", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("amount",{sortable:true,width:60,align:'left'}),
		           PJ.grid_column("supplierCode",{sortable:true,width:60,align:'left'}),
		           PJ.grid_column("clientOrderLeadTime",{sortable:true,width:80,align:'left'}),
		           PJ.grid_column("clientOrderDeadline",{sortable:true,width:120,align:'left'}),
		           PJ.grid_column("clientQuoteRemark", {sortable:true,width:150,align:'left'})
		           ]
	});
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/sales/clientorder/unFinishElement?id='+id);
	};
	
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
		
		//商业类型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/bizType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
						$("#searchForm4").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//供应商代码
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/purchase/supplierinquiry/getSuppliers',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm5").append($option);
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
		    	PJ.grid_search(grid,'<%=path%>/sales/clientorder/unFinish');
		    }  
		}); 
		
		//搜索
		$("#searchBtn").click(function(){
			PJ.grid_search(grid,'<%=path%>/sales/clientorder/unFinish');
			
		});
		
		$(window).resize(function() {
			GridResize();
		});
		
		function GridResize() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid2.setGridWidth(PJ.getCenterBottomWidth());
			grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
			grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		}
		grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
		grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		
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
		<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
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
								<form:left>机型：</form:left>
								<form:right><p>
													<select id="searchForm3" name="airTypeValue" alias="at.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							
							<form:column>
								<form:left>商业类型：</form:left>
								<form:right><p>
													<select id="searchForm4" name="bizTypeCode" alias="bt.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							
							<form:column>
								<form:left>状态：</form:left>
								<form:right><p>
													<select id="searchForm5" name="inquiryStatusCode" alias="sta.code" oper="eq">
							            			<option value="">全部</option>
							            			<option value="CANCEL">作废</option>
							            			<option value="NO_INQUIRY">未询价</option>
							            			<option value="QUOTED">已报</option>
							            			<option value="REFUSE">拒报</option>
							            			<option value="SEND_INQUIRY">已询价</option>
							            			<option value="SUPPLIER_QUOTE">供应商已报</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">	
							<form:column>
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="co.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="co.order_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
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
		<div position="centerbottom" >
			<table id="list2" style=""></table>
			<div id="pager2"></div>
		</div>
		
	</div>
</body>
</html>