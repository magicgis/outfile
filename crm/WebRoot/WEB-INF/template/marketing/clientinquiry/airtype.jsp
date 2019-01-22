<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商订单明细列表</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<style>
	.l-layout { width: 22%; margin: 0; padding: 0}
	th{bgcolor:#f5f5f6;}
</style>
<script type="text/javascript">
 	//-- Set Attribute
 	
	var layout,layout2, grid,grid2;
	$(function() {
		layout = $("#layout_main").ligerLayout();

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
						$option.val(obj[i].id).text(obj[i].code);
						$("#searchForm1").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ {
				id:'search',
				icon : 'search',
				text : '收起搜索',
				click: function(){
					$("#searchdiv").toggle(function(){
						var display = $("#searchdiv").css("display");
						if(display=="block"){
							$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
							grid2.setGridHeight(PJ.getCenterHeight()-119);
						}else{
							$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
							grid2.setGridHeight(PJ.getCenterHeight()-70);
						}
					});
				}
		}
			]
		});
		
		grid = PJ.grid("list", {
			rowNum: -1,
			url: "<%=path%>/supplierquote/airtypedata",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-25,
			autoSrcoll:true,
			shrinkToFit:true,
			multiselect:true,
			sortname : "",
			colNames : ["id", "机型"],
			colModel : [ PJ.grid_column("id", {sortable:true,width:100,key:true,hidden:true}),
			            PJ.grid_column("code", {sortable:true,width:100})
						]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: -1,
			url: "<%=path%>/supplierquote/clientinquirydata",
			width : PJ.getCenterWidth()+550,
			height : PJ.getCenterHeight()-119,
			autoSrcoll:true,
			shrinkToFit:true,
			multiselect:true,
			
			sortname : "",
			colNames : ["id", "客户参考号"],
			colModel : [ PJ.grid_column("id", {sortable:true,width:100,key:true,hidden:true}),
			            PJ.grid_column("quoteNumber", {sortable:true,width:100})
						]
		});
		
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid2.setGridWidth(PJ.getCenterWidth()+540);
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight());
				grid2.setGridHeight(PJ.getCenterHeight()-120);
			}else{
				grid.setGridHeight(PJ.getCenterHeight());
				grid2.setGridHeight(PJ.getCenterHeight()-120);
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid2,'<%=path%>/supplierquote/clientinquirydata');
		    }  
		});
		
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid2,'<%=path%>/supplierquote/clientinquirydata');
		 });
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
	});
	//获取表单数据
	 function getFormData(){
		 var postData = {};
		 var rowKey = grid.getGridParam("selarrrow");
		 var rowKey2 = grid2.getGridParam("selarrrow");
		 if(rowKey!= ""){
			 var air =  PJ.grid_getMutlSelectedRowkey(grid);
			 airTypeId = air.join(",");
			 if(airTypeId.length>0){
					postData.airTypeId = airTypeId;
				}
		 }
		 if(rowKey2!= ""){
		 var client =  PJ.grid_getMutlSelectedRowkey(grid2);
		 clientinquiryId = client.join(",");	
		 if(clientinquiryId.length>0){
			 postData.clientinquiryId = clientinquiryId;
		 }
		 }
		 if(rowKey== ""&&rowKey2== ""){
				PJ.warn("请选择需要操作的数据");
				return false;
		 }
		 postData.clientId=$("#searchForm1").val();
			return postData;
	 }		
	 
	
	 
</script>
<style>
	#cb_list{
		margin:0
	}
	#cb_list12{
		margin:0
	}
</style>
</head>

<body>
	<div id="layout_main" style="float: left;" >
		<div position="center" title="">
	 
			<table id="list"></table>
		</div>
	</div>
	<div id="layout_main" style="float: right;width: 78%; margin: 0; padding: 0">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 50px;display: block;">
		 <form id="searchForm">
		<input type="text" class="hide" name="client" value="1">
					<div class="search-box">
						<form:row columnNum="4">
					<form:column>
								<form:left><p>询价日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="inquiry_date" oper="lt"/> </p></form:right>
							</form:column>
								<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="client_id" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
							</form:column>
					</form:row>
							</div></form>  
							</div>
	
		<table id="list2"></table>
</div>
</body>
</html>
