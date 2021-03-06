<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-供应商机型管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:30px;
  vertical-align:text-top;
  padding-top:2px;
 } */
</style>
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [  {
						icon : 'add',
						text : '新增',
						click : function(){
							var iframeId="addsupplierFrame";
							PJ.topdialog(iframeId, '新增供应商资质', 
						 			'<%=path%>/suppliermanage/toAddAptitude?supplierId='+'${supplierId}',
										function(item, dialog){
												 var postData=top.window.frames[iframeId].getFormData();
												 $.ajax({
													    url: '<%=path%>/suppliermanage/saveAddAptitude',
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	PJ.success(result.message);
																	PJ.grid_reload(grid);
																	dialog.close();
																} else {
																	PJ.error(result.message);
																}		
														}
													});
										},function(item,dialog){dialog.close();}, 1000, 500, true,'新增');
				  			}
						},
						{
							icon : 'add',
							text : '删除',
							click : function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								PJ.confirm("确定删除?", function(yes){
									if(yes){
										 $.ajax({
											    url: '<%=path%>/suppliermanage/deleteAptitude?id='+id,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
															dialog.close();
														} else {
															PJ.error(result.message);
														}		
												}
										});
									}
								})
					  		}
						}]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/suppliermanage/supplierAptitudeListData?supplierId='+'${supplierId}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:true,
			inLineEdit: true,
			colNames :["id","supplierId","资质名称","到期日期","操作人","更新日期"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("supplierId",{hidden:true}),
			           PJ.grid_column("name",{sortable:true,width:150}),
			           PJ.grid_column("expireDate",{sortable:true,width:150}),
			           PJ.grid_column("lastUpdateUserName",{sortable:true,width:150}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150})
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
<input type="text" class="hide" name="sex" id="sex" value="">
	<div id="layout1">
		<div position="center" title="">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>