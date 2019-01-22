<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>收款管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 250,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [
					 {
							id:'add',
							icon : 'add',
							text : '明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '财务-修改收款', '<%=path%>/finance/clientincome/toIncomeDetail?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
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
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/finance/clientincome/income',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:true,
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			sortname : "i.update_timestamp",
			sortorder : "desc",
			colNames :["id","发票号","订单号","收款金额","收款时间","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("invoiceNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("receiveDate", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'<%=path%>/sales/clientorder/incomeDetail?id=0',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager2",
			editurl:'<%=path%>/sales/clientorder/editIncomeDetail',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
				PJ.grid_reload(grid);
			},
			sortname : "id.UPDATE_TIMESTAMP",
			colNames :["id","订单明细id","件号","单价","数量","总价","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("clientOrderElementId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("price", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:300,editable:true,align:'left'})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			PJ.grid_search(grid2,'<%=path%>/sales/clientorder/incomeDetail?id='+id);
		};
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/finance/clientincome/income');
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/finance/clientincome/income');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		/* $(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		}); */
		
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
						$("#clientCode").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		$("#invoiceNumber").blur(function(){
			var text = $("#invoiceNumber").val();
			$("#invoiceNumber").val(trim(text));
		});
		
		$("#orderNumber").blur(function(){
			var text = $("#orderNumber").val();
			$("#orderNumber").val(trim(text));
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
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>发票号：</p></form:left>
								<form:right><p><input id="invoiceNumber" class="text" type="text" name="invoiceNumber" alias="ci.INVOICE_NUMBER" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>订单号：</form:left>
								<form:right><p>
												<input id="orderNumber" class="text" type="text" name="orderNumber" alias="co.ORDER_NUMBER" oper="cn"/>
											</p>
								</form:right>
							</form:column>	
							<form:column>
								<form:left><p>收款日期：</p></form:left>
								<form:right><p><input id="receiptStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'receiptEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="receiptStart" alias="i.RECEIVE_DATE" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="receiptEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'receiptStart\')}',dateFmt:'yyyy-MM-dd'})" name="receiptEnd" alias="i.RECEIVE_DATE" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>客户</p></form:left>
								<form:right><p>
											<select name="clientCode" id="clientCode" alias="c.code" oper="eq">
													<option value="">全部</option>
											</select>
								</p></form:right>
							</form:column>
						</form:row>	
						<form:row columnNum="5">	
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
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
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
							<p style="padding-left:10px;">
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