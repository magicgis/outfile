<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>欠款管理</title>

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
			url:'<%=path%>/finance/importpackagepayment/debtList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-202,
			autoSrcoll:true,
			sortname : "sd.update_timestamp",
			sortorder : "desc",
			inLineEdit: true,
			editurl:'<%=path%>/finance/importpackagepayment/editSupplierDebt',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			colNames :["id","供应商","供应商订单号","欠款","已用","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("supplierCode", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("paid", {sortable:true,width:100,align:'left',editable:true}),
			           PJ.grid_column("updateTimestamp", {sortable:true,width:120,align:'left'})
			           ]
		});
		
		//供应商
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/purchase/supplierinquiry/getSuppliers',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#supplier").append($option);
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
		    	PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/debtList');
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
				 PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/debtList');
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("play");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-262);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-262);
			}
		});
		
		$("#orderNumber").blur(function(){
			var text = $("#orderNumber").val();
			$("#orderNumber").val(trim(text));
		});
		
		
		function getSearchString(){
			var flag = true, searchString = "";
			$(document).find("[alias]").each(function(i, e) {
				if (!$(e).val() || !$.trim($(e).val())){
					return searchString;
				}
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
						$(alias).each(function(index){
							//name += alias[index];
							var str = getRuleString(alias[index], result[1], value, result[2]);
							if (searchString && str){
								searchString += " or ";
							}
							if(!searchString){
								searchString += " ( ";
							}
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
					
				} 
			});
			return searchString;
		}
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var data = $("#form").serialize();
		 return data;
	}
	
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="欠款管理">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="3">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input name="orderNumber" class="text" id="orderNumber" alias="so.ORDER_NUMBER" oper="cn"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>供应商：</p></form:left>
								<form:right><p><select id="supplier" name="supplier"  alias="s.id" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
							<p style="padding-left:60px;">
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