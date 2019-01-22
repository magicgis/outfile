<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>销售-客户询价管理</title>

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
			         
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exportpackage/clientOrderList?exportPackageId='+'${id}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var iframeId = 'invoiceIframe';
				var id=ret["id"];
				$("#id").val(id);
			},
			sortname : "ep.export_date",
			sortorder : "desc",
			colNames :["id","客户订单号","预付比例","发货时支付比例","验货时支付比例","订单总数量/出库总数量"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("orderNumber", {align:'left'}),
			           PJ.grid_column("prepayRate", {align:'left',
			        	   formatter: function(value){
								if (value) {
									return value*100+"%";
								} else {
									return '0'+"%";							
								}
							}}),
			            PJ.grid_column("shipPayRate", {align:'left',
			            	 formatter: function(value){
									if (value) {
										return value*100+"%";
									} else {
										return '0'+"%";							
									}
								}}),
			             PJ.grid_column("receivePayRate", {align:'left',
			            	 formatter: function(value){
									if (value) {
										return value*100+"%";
									} else {
										return '0'+"%";							
									}
								}}),
			             PJ.grid_column("remark", {align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
			
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
	 function getFormData(){
		 var ret = PJ.grid_getSelectedData(grid);
		
			var $input = $("#Form").find("input,textarea,select");
			var postData = {};
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return false;
				}
				postData[$(this).attr("name")] = $(this).val();
			});
			return postData;
		}
	 
	
	function trim(str){
		return $.trim(str);
	}
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="客户订单">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="Form">
				<input type="text" class="" name="id" id="id" value="">
					<div class="search-box">
					</div>
				</form>
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>