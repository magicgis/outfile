<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>明细列表</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>

<script type="text/javascript">
 	//-- Set Attribute
 	
	var layout, grid;
	$(function() {
		layout = $("#layout_main").ligerLayout();
		
		
		

		//数据区的工具栏
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
									grid.setGridHeight(PJ.getCenterHeight()-160);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-111);
								}
							});
						}
					}, 
					{
						icon : 'view',
						text : 'Excel管理',
						click : function(){
							var postData = {};
							postData.id = "${id}";
							$.ajax({
								url: '<%=path%>/sales/clientinquiry/checkQuote',
								data: postData,
								type: "POST",
								loading: "正在处理...",
								success: function(result){
										if(result.success){
											Excel();
										} else {
											PJ.warn(result.message);
										}		
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
		
		PJ.ajax({
			url: "<%=path%>/sales/clientinquiry/list/dynamicColNames?id="+"${id}",
			 dataType:'json',	
			 loading: "正在加载...",
			   success: function(result) {
				   var colNames = [ "id","参考号","序号","件号","机型","描述","单位","数量"];
				   var colModel = [PJ.grid_column("id", {sortable:true,hidden:true,editable:true}),
						            PJ.grid_column("quoteNumber", {sortable:true,width:130}),
						            PJ.grid_column("item", {sortable:true}),
						            PJ.grid_column("partNumber", {sortable:true}),
						            PJ.grid_column("airType", {sortable:true}),
						            PJ.grid_column("description", {sortable:true}),
						            PJ.grid_column("unit", {sortable:true}),
						            PJ.grid_column("amount", {sortable:true})
									];
					for(var index in result.columnDisplayNames){
						colNames.push(result.columnDisplayNames[index]);
						colModel.push(PJ.grid_column(result.columnKeyNames[index], {sortable:false,width:100}));
					}	
					var id='${id}';
					grid = PJ.grid("list", {
						rowNum: 20,
						url: '<%=path%>/supplierquote/weatherquotedata?id='+id+'&type='+'${type}',
						width : PJ.getCenterWidth(),
						height : PJ.getCenterHeight() - 115,
						autoSrcoll:true,
						shrinkToFit:false,
						rowList:[10,20],
	    				colNames : colNames,
	    				colModel : colModel

					});
			   }
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/supplierquote/weatherquotedata?part_number='+$("#partNumber").val()+'&check='+$("#check").val()+'&id='+'${id}');
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/supplierquote/weatherquotedata?part_number='+$("#partNumber").val()+'&check='+$("#check").val()+'&id='+'${id}');
		 });
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-192);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
	});
	
	function Excel(){
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey = "";
			var amount = $("#amount").val();
			var id=0+"-"+'${id}'+"-"+amount;
			var businessKey = 'client_inquiry.id.'+id+'.ClientInquieyExcel';
			
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
			});
	}
	
	
	function getFormData(){
		 var data = $("#form").serialize();
		 return data;
	}
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
</head>

<body style="padding:3px">
<input type="text" class="hide" name="conditionCode" id="conditionCode" value="">
<input type="text" class="hide" name="certificationCode" id="certificationCode" value="">
<input type="text" class="hide" name="supplierQuoteStatusValue" id="supplierQuoteStatusValue" value="">
<input type="text" class="hide" name="supplierInquiryId" id="supplierInquiryId" value="${supplierInquiryId}">
	<div id="layout_main">
		<div position="center" title="">
		<div id="toolbar"></div>
				<div id="searchdiv" style="width: 100%;height: 50px;display: none;">
				<form id="searchForm">
					<div class="search-box">
							<form:row columnNum="4">
							<form:column>
							      <form:left>件号</form:left>
							   	<form:right><p>
							            	<input  type="text" style="width:150px" prefix="" id="partNumber" name="partNumber" class="text" value=""/>
							           </p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>精确查询</form:left>
								<form:right><input type="checkbox" name="exact" id="exact"/></form:right>
								<input type="text" name="check" id="check" class="hide"/>
							</form:column>
							<form:column >
								<p style="padding-left:10px;">
									<input class="btn btn_orange" type="button" value="搜索" id="searchBtn" >
									<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
								</p>
							</form:column>
							<form:column>
							      <form:left>数量</form:left>
							   	<form:right><p>
							            	<input  type="text" style="width:150px" prefix="" id="amount" name="amount" class="text" value=""/>
							           </p>
								</form:right>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>
		
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
