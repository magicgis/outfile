<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户询价明细管理 </title>

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
					 {
							id:'add',
							icon : 'add',
							text : '黑名单标注',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var partNumber = ret["partNumber"];
								var id = ret["id"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, '商品库件号黑名单标注', '<%=path%>/sales/clientinquiry/toisblacklist?partNumber='+partNumber,
										function(item,dialog){
											 var postData=top.window.frames[iframeId].getData();	 							
											 $.ajax({
													url: '<%=path%>/sales/clientinquiry/editBlackList?id='+id,
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
										PJ.grid_reload(grid);}
									   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"黑名单标注");
							}
					 }
							 
			         ]
		});
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/sales/clientinquiry/elementData?id='+${id },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			aftersavefunc:function(rowid,result){
				var responseJson=result.responseJSON;
				if(responseJson.success==true){
					var rowData=grid.getRowData(rowid);
					var isBlacklist=rowData.isBlacklist;
					if(isBlacklist=="是"){
					var partNumber=rowData.partNumber;
					var iframeId="ideaIframe5";
					PJ.topdialog(iframeId, '商品库件号黑名单标注', '<%=path%>/sales/clientinquiry/toisblacklist?partNumber='+partNumber,
							function(item,dialog){
								 var postData=top.window.frames[iframeId].getData();	 							
								 $.ajax({
										url: '<%=path%>/sales/clientinquiry/editBlackList',
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
							PJ.grid_reload(grid);}
						   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"黑名单标注");
					}
				}
			},
			editurl:'<%=path%>/sales/clientinquiry/editisblacklist',
			inLineEdit: true,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var isBlacklist = ret["isBlacklist"];
				$("#isBlacklist").val(isBlacklist);
			},
			//shrinkToFit:false,
			sortname : "ci.inquiry_date",
			colNames :["id","序号","件号","另件号","描述","单位","数量","是否黑名单","机型","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("alterPartNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("amount",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("isBlacklist",{sortable:true,width:70,align:'left',editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								var arr=["否","是"];
								var isBlacklist=$("#isBlacklist").val();
								if(isBlacklist==arr[0]){
									return "0:否;1:是";
								}else if(isBlacklist==arr[1]){
									return "1:是;0:否";
								}
							}
							},
							  formatter: function(value){
									if (value==1||value=="是") {
										return "是";
									} else if(value==0||value=="否"){
										return "否";							
									}else {
										return "";
									}
								}
			           }),
			           PJ.grid_column("typeCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:200,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:180,align:'left'})
			           ]
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
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:20px;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
/*  th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
} */
</style>
</head>

<body>
<input type="text" name="isBlacklist" id="isBlacklist" class="hide"/>
	<div id="layout1">
		<div position="center" title="询价单号   ${quoteNumber } ">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>