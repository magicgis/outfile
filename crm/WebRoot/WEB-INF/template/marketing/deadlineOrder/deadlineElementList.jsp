<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>货款到期提醒 </title>

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
						id:'search',
						icon : 'search',
						text : '展开搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
								var display = $("#searchdiv").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid.setGridHeight(PJ.getCenterHeight()-212);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-112);
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
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/sales/clientorder/deadlineOrder',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-112,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "order_date",
			sortorder : "desc",
			colNames :["id","件号","订单号","客户订单号","出库日期","发货支付比例","账期","验货支付比例","账期","类型","比例","账期","超期","单价","出库数量","应收总价","已收","待收"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("inquiryPartNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("sourceNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("exportDate", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("shipPayRate",{sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("shipPayPeriod", {sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("receivePayRate",{sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("receivePayPeriod", {sortable:true,width:80,align:'left',hidden:true}),
			           PJ.grid_column("paymentType", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("rate", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("period", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("overDay", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("orderPrice", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("exportAmount",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("total",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("incomeTotal",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("remainTotal",{sortable:true,width:70,align:'left'})
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
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/sales/clientorder/deadlineOrder');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/sales/clientorder/deadlineOrder');
			
		 });
		
		//搜索
		<%--  $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/supplierquote/crawerList?quoteStatus='+quoteStatus);
			 var postData = {};
			 var searchString = getSearchString();
			 postData.searchString = searchString;
			 PJ.ajax({
					url: '<%=path%>/sales/clientorder/deadlineOrder',
					type: "POST",
					loading: "正在处理...",
					data: postData,
					dataType: "json",
					success: function(result){
							if(result.success){
								data = eval(result.message)[0];
								//PJ.grid_reload(grid);
								$("#list1").jqGrid('clearGridData');
								$("#list1").jqGrid('setGridParam',{  // 重新加载数据
								      datatype:'local',
								      data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
								      page:1
								}).trigger("reloadGrid");
							}	
					}
			});
		 }); --%>
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-182);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
	function getSearchString() {
		var flag = true, searchString = "";
		$(document).find("[alias]").each(function(i, e) {
			if (!$(e).val() || !$.trim($(e).val()))
				return true;
			
			var result = checkRule(e);
			flag = result[0];
			if (flag) {
				var value = $(e).val();
				//var name = $(e).attr("prefix");
				//if (name) {
					//name += ".";
				//}else{
					//name="";
				//}
				var alias = $(e).attr("alias").split(" or ");
				if(alias.length>=2){
					if(!searchString){
						searchString += " ( ";
					}else{
						searchString += " and ( ";
					}
					
					$(alias).each(function(index){
						//name += alias[index];
						var str = getRuleString(alias[index], result[1], value, result[2]);
						if (searchString && str){
							if(searchString.substring(searchString.length-2, searchString.length) != "( "){
								searchString += " OR ";
							}
						}
						/*if(!searchString){
							searchString += " ( ";
						}*/
						searchString += str;
					});
					searchString += " ) ";
				}else{
					//name += $(e).attr("alias");
					var str = getRuleString($(e).attr("alias"), result[1], value, result[2]);
					if (searchString && str){
						searchString += " AND ";
					}	
						searchString += str;
				}
				
			} else {
				return false;
			}
		});
		return searchString;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="">
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
	</div>
</body>
</html>