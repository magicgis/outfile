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
							icon : 'view',
							text : 'Excel管理',
							click : function(){
								var data=getFormData();
								if(data){
							var title = 'excel管理';
							//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
							//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
							var businessKey = "";
							var id=0+"-"+data;
							var clientTemplateType=${clientTemplateType};
							var bizTypeId=${bizTypeId};
							var currencyId=${currencyId};
							if(clientTemplateType==170){
								businessKey='client_quote_element.id.'+id+'.QuotationExcel1.'+currencyId;
								}else if(clientTemplateType==171){
									if(bizTypeId==121){
										businessKey='client_quote_element.id.'+id+'.QuotationExcel3.'+currencyId;
									}else{
									businessKey='client_quote_element.id.'+id+'.QuotationExcel2.'+currencyId;
								}			
								}else if(clientTemplateType==172){
									businessKey='client_quote_element.id.'+id+'.QuotationExcel3.'+currencyId;
								}
							
							PJ.excelDiaglog({
								data:'businessKey='+businessKey,
								title: title,
								add:true,
								remove:true,
								download:true
							});
							}
							}
						}
			         ]
		});
		
		
		grid = PJ.grid("list", {
			rowNum: -1,
			url: "<%=path%>/clientquote/quotesList?clientInquiryId="+'${clientInquiryId}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-70,
			autoSrcoll:true,
			shrinkToFit:true,
			multiselect:true,
			sortname : "",
			colNames : ["id", "currencyId","报价单号"],
			colModel : [ PJ.grid_column("id", {sortable:true,width:100,key:true,hidden:true}),
			             PJ.grid_column("currencyId", {sortable:true,width:100,hidden:true}),
			            PJ.grid_column("quoteNumber", {sortable:true,width:100}),
						]
		});
		
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		
	});
	
	
	
	 function getFormData(){
		 var ids = "";
		 var rowKey = grid.getGridParam("selarrrow");
		 if(rowKey!= ""){
			 var air =  PJ.grid_getMutlSelectedRowkey(grid);
			 ids = air.join(",");
		 }
		 if(rowKey== ""){
				PJ.warn("请选择需要操作的数据");
				return false;
		 }
			return ids;
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
<style>
	#cb_list{
		margin:0
	}
</style>
</head>

<body style="padding:3px">
	<div id="layout_main">
		<div position="center" title="">
		<div id="toolbar"></div>
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
