<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户询价单统计</title>

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
									grid.setGridHeight(PJ.getCenterHeight()-162);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-62);
								}
							});
						}
					}
			        ]
		});
		
		
		grid = PJ.grid("list1", {
			rowNum: -1,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-162,
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "c.id",
			colNames :["id","客户","机型","商业类型","询价单数量","报价单数量","报价单数额","订单数量","订单金额","出库单数量","出库单金额"],
			colModel :[PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("airTypeCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("bizTypeCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("clientInquiryCount", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientQuoteCount", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientQuoteSum", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientOrderCount", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientOrderSum", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientExportCount", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("clientExportSum", {sortable:true,width:150,align:'left'}),
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
		    	if($("#clientCode").val()==null){
					 alert("不能搜索全部客户！");
				}else{
					 PJ.grid_search(grid,'<%=path%>/sales/clientorder/Statistics');
				}
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 if($("#clientCode").val()==null){
				 alert("不能搜索全部客户！");
			 }else{
				 PJ.grid_search(grid,'<%=path%>/sales/clientorder/Statistics');
			 }
				 
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
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
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
								<form:left><p>日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ci.inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ci.inquiry_date" oper="lt"/> </p></form:right>
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
		</div>
	</div>
</body>
</html>