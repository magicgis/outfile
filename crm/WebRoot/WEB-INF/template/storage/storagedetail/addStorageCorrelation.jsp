<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-新增供应商订单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		PJ.datepicker('importDate');
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [	
					{	icon : 'add',
						text : '绑定件号',
						click : function() {
							var ret = PJ.grid_getSelectedData(grid);
					 		var id = ret["id"];
							$.ajax({
								type: "POST",
								dataType: "json",
								url:'<%=path%>/storage/detail/addStorageCorrelation?importPackageId='+'${importPackageId}'+'&correlationId='+id,
								success:function(result){
									if(result.success){
										PJ.success(result.message);
										PJ.grid_reload(grid);
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
			rowNum: 20,
			url:'<%=path%>/storage/detail/StorageDetail?correlation=1&importId='+'${importPackageId}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-242,
			autoSrcoll:true,
			sortname : "a.import_date",
			sortorder : "desc",
			colNames :["id","importPackageId","supplierOrderElementId","supplierQuoteElementId","客户","客户订单号","供应商","件号",
			           "描述","状态","证书","单位","数量","流程占用数量","人民币价格","总价","位置","入库单号","订单号"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("importPackageId", {hidden:true}),
			           PJ.grid_column("supplierOrderElementId", {hidden:true}),
			           PJ.grid_column("supplierQuoteElementId", {hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("unit",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("storageAmount",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("lockAmount",{sortable:true,width:80,align:'left'
			        	   ,formatter:function(value){
			        		   return '<span style="color:red">'+value+'<span>';
			        	   }   
			           }),
			           PJ.grid_column("basePrice",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("totalBasePrice",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("location",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("importNumber",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("orderNumber",{sortable:true,width:120,align:'left'})
			          ]
		});

		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/detail/StorageDetail?correlation=1&importId='+'${importPackageId}');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/detail/StorageDetail?correlation=1&importId='+'${importPackageId}');
			
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
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		
	});
	
	function trim(str){
		return $.trim(str);
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
			if($("#taxReturn").is(':checked')==true){
				postData.taxReturn=1;
			}else{
				postData.taxReturn=0;
			}
			return postData;
	}
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"location,description",
			form:"#addForm"
		});
	};
	
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
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="件号：${partNumber },描述：${description }">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;">
				<form id="addForm">
					<input id="clientOrderElementId" class="hide" type="text" name="clientOrderElementId" />
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left>件号</form:left>
								<form:right><input name="partNumber" id="partNumber"  class="text" alias="a.part_number" oper="cn"/></form:right>
							</form:column>
							<form:column>
								<form:left><p>入库日期：</p></form:left>
								<form:right><p><input id="importDateStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'importDateEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="importDateStart" alias="a.import_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="importDateEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'importDateStart\')}',dateFmt:'yyyy-MM-dd'})" name="importDateEnd" alias="a.import_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>位置</form:left>
								<form:right><input name="location" id="location"  class="text"  alias="a.location" oper="cn"/></form:right>
							</form:column>
							<form:column>
							<p style="padding-left:60px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
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