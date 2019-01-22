<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-供应商联系人管理</title>

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
					PJ.topdialog(iframeId, '新增供应商联系人', 
				 			'<%=path%>/suppliermanage/toAddSupplierContact?supplierId='+'${supplierId}',
								function(item, dialog){
										 var postData=top.window.frames[iframeId].getFormData();
										 var nullAble=top.window.frames[iframeId].validate();
											if(nullAble){
										 $.ajax({
											    url: '<%=path%>/suppliermanage/addSupplierContact',
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
										 }
								},function(item,dialog){dialog.close();}, 1000, 500, true,'新增');
		  			}
				}, {
					id:'edit',
					icon : 'edit',
					text : '修改',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"];
						var iframeId="ideaIframe2";
							PJ.topdialog(iframeId, '修改供应商联系人信息', '<%=path%>/suppliermanage/toEditSupplierContact?id='+id,
									function(item, dialog){
											 var postData=top.window.frames[iframeId].getFormData();
											 var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
											 $.ajax({
													url: '<%=path%>/suppliermanage/updateSupplierContact',
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
																dialog.close();
															}		
													}
												});
												}
									},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
						
					}
			 }, {
					id:'edit',
					icon : 'edit',
					text : '设置为邮件接收人',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"];
						$.ajax({
							url: '<%=path%>/suppliermanage/setEmailPerson?id='+id,
							type: "POST",
							loading: "正在处理...",
							success: function(result){
									if(result.success){
										PJ.success(result.message);
										PJ.grid_reload(grid);
										dialog.close();
									} else {
										PJ.error(result.message);
										dialog.close();
									}		
							}
						});
					}
			 }, {
					id:'add',
					icon : 'add',
					text : '删除',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid);
						var id = ret["id"];
											 $.ajax({
													url: '<%=path%>/suppliermanage/deleteSupplierContact?id='+id,
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
			 }
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/suppliermanage/contactlistData?supplierId='+'${supplierId}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var sexValue = ret["sexValue"];
				$("#sex").val(sexValue);
			},
			editurl:'<%=path%>/suppliermanage/updateSupplierContact',
			colNames :["id","全称","姓","名字","称谓","性别","职位","邮件接收人","生日","所属部门","电话","手机","传真","建立日期","邮箱","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("fullName", {sortable:true,width:150,editable:true,align:'left'}),
			           PJ.grid_column("surName", {sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("name", {sortable:true,width:120,editable:true,align:'left'}),
			           PJ.grid_column("appellation", {sortable:true,width:100,editable:true,align:'left'}),
			           PJ.grid_column("sexValue",{sortable:true,width:30,editable:true,align:'left',
			        		edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								var arr=["男","女"];
								var sex=$("#sex").val();
								if(sex==arr[0]){
									return "1:男;2:女";
								}else if(sex==arr[1]){
									return "2:女;1:男";
								}
							}
						}
			           }),
			           PJ.grid_column("position", {sortable:true,width:150,editable:true,align:'left'}),
			           PJ.grid_column("emailPerson", {sortable:true,width:80,editable:true,align:'left',
			        	   formatter: statusFormatter}),
			           PJ.grid_column("birthday", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("department", {sortable:true,width:100,editable:true,align:'left'}),
			           PJ.grid_column("phone",{sortable:true,width:130,editable:true,align:'left'}),
			           PJ.grid_column("mobile",{sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("fax",{sortable:true,width:130,editable:true,align:'left'}),
			           PJ.grid_column("creationDate", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("email",{sortable:true,width:150,editable:true,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:150,editable:true,align:'left'})
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
	
	function statusFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 1:
			return '是';
			break;
		default: 
			return '';
			break; 
		}
	}
</script>

</head>

<body>
<input type="text" class="hide" name="sex" id="sex" value="">
	<div id="layout1">
		<div position="center" title="供应商信息管理">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>