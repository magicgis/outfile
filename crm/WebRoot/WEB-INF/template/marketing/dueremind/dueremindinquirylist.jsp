<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>到期提醒</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
	var layout, grid;
	$(function() {
		
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [  {
				icon : 'view',
				text : '超时拒报',
				click : function(){
					mx();
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
			}   ]
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
			url:'<%=path%>/sales/clientinquiry/bizType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
						$("#biz_type_code").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 

		grid = PJ.grid("list", {
			rowNum: 20,
			url:'<%=path%>/dueremind/listDataByInquiry?warningDate='+$("#warningDate").val()+'&type='+'${type}',
			width : PJ.getCenterWidth(),
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "ci.deadline",
			sortorder : "desc",
			height : PJ.getCenterHeight()-102,
			colNames : ["id","询价单号","客户询价单号", "询价日期", "截止日期", "状态","报价比例","期限",
			             "更新时间"],
			colModel : [ PJ.grid_column("id", {sortable:true,hidden:true}),
			            PJ.grid_column("quoteNumber", {align:'left'}),
			            PJ.grid_column("sourceNumber", {hidden:true}),
			            PJ.grid_column("inquiryDate", {width:200,align:'left'}),
						PJ.grid_column("deadline", {width: 250,align:'left'}),
						PJ.grid_column("inquiryStatusValue", {width:200,align:'left'}),
						PJ.grid_column("proportion", {align:'left'}),
						PJ.grid_column("overdue", {align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,align:'left'}),
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
		    	PJ.grid_search(grid,'<%=path%>/dueremind/listDataByInquiry?warningDate='+$("#warningDate").val()+'&type='+'${type}');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/dueremind/listDataByInquiry?warningDate='+$("#warningDate").val()+'&type='+'${type}');
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
	
	function mx(){
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		 $.ajax({
				url: '<%=path%>/dueremind/refuse?id='+id,
				type: "POST",
				loading: "正在处理...",
				success: function(result){
						if(result.success){
							PJ.success(result.message);
							PJ.grid_reload(grid);
						} else {
							PJ.error(result.message);
						}
					
				}
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
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
					<c:if test="${type=='marketing'}">
						<form:row columnNum="5">
						
							<form:column>
							       <form:left><p>期限：</p></form:left>
							    <form:right><p><input type="text"  id="warningDate" prefix="" name="warningDate" class="text"  value="7"/></p></form:right>
							   	</form:column>
							   	
							   	<form:column>
							       <form:left><p>询价单号：</p></form:left>
							    <form:right><p><input id="searchForm2" type="text"  prefix="" name="quoteNumber" class="text" oper="cn" alias="ci.quote_number or ci.source_number" value=""/></p></form:right>
							   	</form:column>
							   	<form:column>
							<form:left>客户</form:left>
								<form:right><p><select  id="client_code" name="client_code" alias=" c.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							              </p>
								</form:right>
							</form:column>
							<form:column>
							      <form:left>机型</form:left>
							   <form:right><p><select  id="air_type_value" name="air_type_value" alias="at.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>商业类型</form:left>
							   <form:right><p><select id="biz_type_code" name="biz_type_code" alias="bt.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							          </p>
								</form:right>
							</form:column>
					</form:row>
							<form:row columnNum="5">	
							<form:column>
								<form:left><p>询价日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ci.inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ci.inquiry_date" oper="lt"/> </p></form:right>
							</form:column>
								<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							
						<form:column ><form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
							</form:column>
							</form:row>
					</c:if>
					<c:if test="${type=='purchase'}">
							<form:row columnNum="5">
						
							<form:column>
							       <form:left><p>期限：</p></form:left>
							    <form:right><p><input type="text"  id="warningDate" prefix="" name="warningDate" class="text"  value="7"/></p></form:right>
							   	</form:column>
							   	
							   	<form:column>
							       <form:left><p>询价单号：</p></form:left>
							    <form:right><p><input id="searchForm2" type="text"  prefix="" name="quoteNumber" class="text" oper="cn" alias="ci.quote_number or ci.source_number" value=""/></p></form:right>
							   	</form:column>
							   	<form:column>
							<form:left>客户</form:left>
								<form:right><p><select  id="client_code" name="client_code" alias=" c.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							              </p>
								</form:right>
							</form:column>
							<form:column>
							      <form:left>机型</form:left>
							   <form:right><p><select  id="air_type_value" name="air_type_value" alias="at.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							           </p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>商业类型</form:left>
							   <form:right><p><select id="biz_type_code" name="biz_type_code" alias="bt.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							          </p>
								</form:right>
							</form:column>
					</form:row>
							<form:row columnNum="5">	
							<form:column>
								<form:left><p>询价日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ci.inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ci.inquiry_date" oper="lt"/> </p></form:right>
							</form:column>
								<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							
						<form:column ><form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
							</form:column>
							</form:row>
					</c:if>
					</div>
				</form>
			</div>
			<table id="list"></table>
			<div id="pager1"></div>
		
		</div>
	</div>
</body>
</html>
