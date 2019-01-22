<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>利润表统计</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [{	icon : 'logout',
				text : '导出excel',
				click : function() {
						PJ.grid_export("list1");
						}
			  },
				 	{
						id:'search',
						icon : 'search',
						text : '收起搜索',
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
				$("#check").val("eq");
			}else if(!$("#exact").prop("checked")){
				$("#check").val("cn");
			}
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-250,
			url:'<%=path%>/clientquote/profitStatisticsData',
			height : PJ.getCenterHeight()-202,
			autoSrcoll:true,
			shrinkToFit:false,
			autowidth:true,
			inLineEdit: true,
			editurl:'<%=path%>/clientquote/addOtherFeeInProfit',
			sortname : "co.order_date ",
			sortorder : "desc",
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			colNames :["id","客户","订单号","供应商订单号","订单日期","件号","描述","机型","数量","供应商","采购价格","入库费","入库运费","出库费","出库运费","危险品","银行费","提货换单费","其他费用","采购总价","付款时间","付款金额","销售价格","杂费","佣金","实际价格","收款时间","收款金额","利润率"],
			colModel :[PJ.grid_column("id", {hidden:true,editable:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("supplierOrderNumber", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("orderDate", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("airType", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("quoteBasePrice", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("importFee", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("importFreight", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("exportFee", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("exportFreight", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("hazFee", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("bankCost", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("feeForExchangeBill", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("other", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quoteTotalPrice", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("paymentDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("paymentSum", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("orderBasePrice", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("profitOtherFee", {sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("fixedCost", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderBasePrice", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("receiveDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("profitMargin", {sortable:true,width:60,align:'left'})
			           ]
		});

		
		
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
						$("#clientCode").append($option);
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
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/clientquote/profitStatisticsData?partNumber='+$("#partNumber").val()+'&check='+$("#check").val());
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			// if($("#clientCode").val()==""&&$("#searchForm2").val()==""&&$("#partNumber").val()==""&&$("#searchForm3").val()==""){
			//	 PJ.warn("请输入搜索条件");
			// }else{
				 PJ.grid_search(grid,'<%=path%>/clientquote/profitStatisticsData?partNumber='+$("#partNumber").val()+'&check='+$("#check").val());
			// }
				 
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
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		$("#partNumber").blur(function(){
			var text = $("#partNumber").val();
			$("#partNumber").val(trim(text));
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
			<div id="searchdiv" style="width: 100%;height: 100px;display: block;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="clientCode" name="clientCode" alias="c.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="orderNumber" alias="co.order_number or so.order_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" name="partNumber" /> </p></form:right>
							</form:column>
								<form:column>
							<form:left>精确查询</form:left>
							<form:right><input type="checkbox" name="exact" id="exact"/></form:right>
							<input type="text" name="check" id="check" class="hide"/>
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
						
					
						</form:row>
						<form:row columnNum="5">	
								<form:column>
								<form:left><p>日期：</p></form:left>
								<form:right><p><input id="orderStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'orderEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="orderStart" alias="co.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="orderEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="orderEnd" alias="co.order_date" oper="lt"/> </p></form:right>
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
							<p style="padding-left:65px;">
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