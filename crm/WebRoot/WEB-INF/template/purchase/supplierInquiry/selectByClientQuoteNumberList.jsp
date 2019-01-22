<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>生成excel</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-top;
  padding-top:2px;
 } */
</style>

<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [
						{
						    id:'down',
							icon : 'down',
							text : '生成excel',
							click: function(){
									var postData = getData();
									$.ajax({
										type: "POST",
										dataType: "json",
										data: postData,
										url:'<%=path%>/purchase/supplierinquiry/lotsExcel',
										success:function(result){
											if(result.success){
												var id = getData().quoteNumbers;
												//根据具体业务提供相关的title
												var title = 'excel管理';
												//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
												//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
												var businessKey = 'supplier_inquiry.id.'+id+'.LotsSupplierInuquiryExcel';
												PJ.excelDiaglog({
													data:'businessKey='+businessKey,
													title: title,
													add:true,
													remove:true,
													download:true
												});
											}else{
												
													PJ.warn(result.message);
											}
										}
									});
							}
						}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 100,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-202,
			autoSrcoll:true,
			shrinkToFit:true,
			multiselect:true,
			sortname : "si.update_timestamp",
			sortorder : "desc",
			colNames :["id","供应商询价单号","询价日期"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:200,align:'left'}),
			           PJ.grid_column("updateTimestamp", {sortable:true,width:100,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/selectClientQuoteNumber');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var where = $("#quoteNumber").val();
			 if(where != "" && where != null){
				 PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/selectClientQuoteNumber');
			 }
			
		 });
		
		 $("#quoteNumber").blur(function(){
				var text = $("#quoteNumber").val();
				$("#quoteNumber").val(trim(text));
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
		
	});
	
	//获取表单数据
	function getData(){
		var postData = {};
		var quoteNumber =  PJ.grid_getMutlSelectedRowkey(grid);
		quoteNumbers = quoteNumber.join(",");
		if(quoteNumbers.length>0){
			postData.quoteNumbers = quoteNumbers;
		}
		return postData;
	 }	
	
	//获取表单数据
	 function getFormData(){
			var $input = $("#addForm").find("input,textarea,select");
			var postData = {};

			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
				
				postData[$(this).attr("name")] = $(this).val();
			});
			return postData;
	 }		
		//-- 验证，默认通过true,有空未填则返回false
		function validate2(opt){
			var def = {nodeName:"",form: ""};
			for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
			var $items = $(def.form).find("input,select");var nodes = def.nodeName.split(",");var flag = true;var tip;
			$items.each(function(i){
				var name = $(this).attr("name");
				if(!name)return;
				for(var k in nodes){
					if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
					if(!flag){
		                $(this).addClass("input-error");
						tip = $(this).attr("data-tip");
						return false;
					}else $(this).removeClass("input-error");
				}
			});
				//if(!flag) PJ.tip("未填写"+tip);
			return flag;
		};
		function trim(str){
			return $.trim(str);
		}
	
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
	<div id="layout1">
		<div position="center" title="搜索">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 60px;">
		<form id="addForm" action="">
			<div class="search-box">
				<form:row columnNum="3">
					
					<form:column>
						<form:left><p>客户询价单号</p></form:left>
						<form:right><p><input id="quoteNumber" name="quoteNumber" class="text" alias="ci.QUOTE_NUMBER" oper="eq"/></p></form:right>
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