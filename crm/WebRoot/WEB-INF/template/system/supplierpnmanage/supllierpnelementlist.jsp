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
		
		$("#submit").click(function(){
			
			var url = '<%=path%>/clientquote/uploadExcel?clientinquiryquotenumber='+'${client_inquiry_quote_number}';
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	//var message = decodeURI(data);
	            	//var a = decodeURI(data);
	            	//var da = jQuery.parseJSON(jQuery(data).text());
	            	var da = eval(data)[0];
	            	//var falg = data.flag;
	            	if(da.flag==true){
	            		
		            	PJ.success(da.message);
		            	closeoropen();
		            	PJ.grid_reload(grid);
	            	}
	            	else{
	            		PJ.error(da.message);
	            		closeoropen();
	    	           
	            	}
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.error(data.message);
	            	closeoropen();
	            }  
	        });  
		});	

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ 
			{
				icon : 'add',
			    text : '新增',
				click : function(){
				add();
			}
			},        
		 	{
				icon : 'edit',
				text : '修改',
				click : function(){
					edit();
				}
			},
			{
				id:'search',
				icon : 'search',
				text : '展开文件上传',
				click: function(){
					closeoropen();
				}
			}]
		});
		grid = PJ.grid("list", {
			rowNum: 20,
			url:'<%=path%>/supplierpnmanage/listData?supplierId='+'${supplierId}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 220,
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			sortname : "cie.item",
			sortorder : "asc",
			editurl:'<%=path%>/clientquote/updateclientquoteelement',
			sortname : "",
			colNames :["id", "代码","旧系统代码","供应商名称","供应商简称","币种"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("code", {sortable:true,width:80}),
			           PJ.grid_column("oldCode", {sortable:true,width:80}),
			           PJ.grid_column("name", {width:170}),
			           PJ.grid_column("supplierAbbreviation", {width:170}),
			           PJ.grid_column("currencyValue", {width:90})
			           ]
		});
		
		 grid.jqGrid('setGroupHeaders', { 
		 	useColSpanStyle: true, 
		 	groupHeaders:[ 
		 		{startColumnName: 'clientInquiryElementId', numberOfColumns: 12, titleText: '<div align="center"><span>客户询价</span></div>'},
		 		{startColumnName: 'supplierCode', numberOfColumns: 12, titleText: '<div align="center"><span>供应商报价</span></div>'},
		 		{startColumnName: 'clientQuoteAmount', numberOfColumns: 11, titleText: '<div align="center"><span>客户报价</span></div>'}
		 	] 
		}); 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-222);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-182);
			}
		});
		
	});
	
	function closeoropen(){
	$("#uploadBox").toggle(function(){
		var display = $("#uploadBox").css("display");
		if(display=="block"){
			$("#toolbar > div[toolbarid='search'] > span").html("收起文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-220);
		}else{
			$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-182);
		}
	});
	}
	
	function add(){
		var ret = PJ.grid_getSelectedData(grid);
		var iframeId = 'clientquoteFrame';
		var clientInquiryElementId = ret["id"];
		var clientQuoteId = ret["clientQuoteId"];
	/* 	var inquiryPartNumber = ret["partNumber"];
		var inquiryDescription = ret["description"];
		var inquiryAmount = ret["amount"];
		var inquiryUnit = ret["unit"];
		var inquiryRemark = ret["inquiryRemark"]; */
		var supplierCode = ret["supplierCode"];
		 if(supplierCode=="没有报价"){
			PJ.warn('没有报价不能进行新增');
			return;
		}
		if(supplierCode==""||supplierCode==null){
			PJ.warn('不能进行此操作');
			return;
		}
		if(supplierCode=="有供应商报价"||supplierCode=="有历史报价"){
		 
	 	PJ.topdialog(iframeId, '销售 - 新增客户报价明细','<%=path%>/clientquote/addquoteelement?clientQuoteId='+clientQuoteId+ 			
	 			'&client_inquiry_quote_number='+"${client_inquiry_quote_number}"+
	 			'&clientInquiryElementId='+clientInquiryElementId+
	 			'&optType='+"add", 			
	 			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
		 }else{
			PJ.warn('当前状态仅能做修改操作');
			return;
		} 
		
		}
	
	function edit(){
		var ret = PJ.grid_getSelectedData(grid);
		var iframeId = 'clientquoteFrame';
		var clientInquiryElementId = ret["clientInquiryElementId"];
		var clientQuoteElementId = ret["clientQuoteElementId"];
		var clientQuoteAmount = ret["clientQuoteAmount"];
		var clientQuotePrice = ret["clientQuotePrice"];
		var Remark = ret["clientQuoteRemark"];
		var clientQuoteId = ret["clientQuoteId"];
		/* var inquiryPartNumber = ret["partNumber"];
		var inquiryDescription = ret["description"];
		var inquiryAmount = ret["amount"];
		var inquiryUnit = ret["unit"];
		var inquiryRemark = ret["inquiryRemark"]; */
		var supplierCode = ret["supplierCode"];
		var supplierQuoteElementId = ret["supplierQuoteElementId"];
		var location = ret["location"];
		 if(supplierCode=="没有报价"){
			PJ.warn('没有报价不能进行修改');
			return;
		}
		if(supplierCode==""||supplierCode==null){
			PJ.warn('不能进行此操作');
			return;
		}
		if(supplierCode=="有供应商报价"||supplierCode=="有历史报价"){
			PJ.warn('当前状态仅能做新增操作');
			return;
		 }else{
			 	PJ.topdialog(iframeId, '销售 - 修改客户报价明细','<%=path%>/clientquote/addquoteelement?clientQuoteId='+clientQuoteId+			 			
			 			'&client_inquiry_quote_number='+"${client_inquiry_quote_number}"+
			 			'&clientInquiryElementId='+clientInquiryElementId+
			 			'&optType='+"edit"+'&Price='+clientQuotePrice+
			 			'&id='+clientQuoteElementId+'&Amount='+clientQuoteAmount+
			 			'&supplierQuoteElementId='+supplierQuoteElementId+
			 			'&Remark='+encodeURIComponent(Remark)+
			 			'&location='+location, 	 			
			 			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
		} 
		
		}
	
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:20px;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
</head>

<body style="padding:3px">
<input type="text" class="hide" name="client_inquiry_quote_number" value="${client_inquiry_quote_number}">
	<div id="layout_main">
		<div position="center" title="询价单号:  ${client_inquiry_quote_number}">
		<div id="toolbar"></div>
		
		<div id="uploadBox" >
	<form id="form" name="form">
			<form:row columnNum="2">
				<form:column width="120">
					<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
					<form:right>
						<p><input type="file" value="" id="file" name="file"/>&nbsp;
						   <input type="button" id="submit" value="上传"/>
						</p>
					</form:right>
				</form:column>
			</form:row>
	            
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
