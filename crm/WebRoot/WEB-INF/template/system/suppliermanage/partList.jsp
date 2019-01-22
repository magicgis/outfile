<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-供应商件号管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout,layout2;
var grid,grid2;

$(function() {
	layout = $("#layout1").ligerLayout();
	$("#submit").click(function(){
		var url = '<%=path%>/supplierpnmanage/partUploadExcel?id='+'${id}'+'&supplierPnType='+$("#supplierPnType").val();
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
            	var da = eval(data)[0];
            	if(da.flag==true){
            		PJ.hideLoading();
	            	PJ.success("save successful");
	            	PJ.grid_reload(grid2);
	            	$("#uploadBox").toggle(function(){
						$("#toolbar2 > div[toolbarid='upload'] > span").html("展开excel上传");
						grid2.setGridHeight(PJ.getCenterHeight()-142);
	            	});
            	}else{
            		PJ.hideLoading();
            		$("#uploadBox").toggle(function(){
						$("#toolbar2 > div[toolbarid='upload'] > span").html("展开excel上传");
						grid2.setGridHeight(PJ.getCenterHeight()-142);
	            	});
            		iframeId = 'errorframe'
            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/supplierpnmanage/toPartAbilityErrorList',
            				function(item,dialog){
            					var data=top.window.frames[iframeId].getData();
		            			PJ.ajax({
									url: '<%=path%>/supplierpnmanage/saveMatch',
									data: data,
									type: "POST",
									loading: "正在处理...",
									success: function(result){
											if(result.success){
												PJ.hideLoading();
												PJ.success(result.message);
												dialog.close();
												$.ajax({
													url: '<%=path%>/supplierpnmanage/deleteMessage',
													type: "POST",
													success: function(result){
													}
												});
												PJ.grid_reload(grid2);
											} else {
												PJ.hideLoading();
												PJ.error(result.message);
												dialog.close();
												$.ajax({
													url: '<%=path%>/supplierpnmanage/deleteMessage',
													type: "POST",
													success: function(result){
													}
												});
												PJ.grid_reload(grid2);
											}		
									}
								});
            				}
            				,function(item,dialog){
			            			$.ajax({
										url: '<%=path%>/supplierpnmanage/deleteMessage',
										type: "POST",
										success: function(result){
										}
									});
            					dialog.close();	PJ.grid_reload(grid2);}, 1000, 500, true);
            	}
   				
            },  
            error: function (data, status, e) { 
            	closeoropen();
            }  
        });  
	});	
	
	//归类
	 $.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/supplierpnmanage/supplierPnType',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].value);
					$("#supplierPnType").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
	});
		
		$("#toolbar2").ligerToolBar({
			
			items :	[ 
			    	 <%----%> {
							icon : 'delete',
							text : '删除',
							click : function(){
								var ret = PJ.grid_getSelectedData(grid2);
								var bsn = ret["bsn"];
													 $.ajax({
														    url: '<%=path%>/supplierpnmanage/deleteData?supplierId='+'${id}'+'&bsn='+bsn,
															data: '',
															type: "POST",
															loading: "正在处理...",
															success: function(result){
																	if(result.success){
																		PJ.success(result.message);
																		PJ.grid_reload(grid2);
																	} else {
																		PJ.error(result.message);
																		PJ.grid_reload(grid2);
																	}		
															}
														});
					  			}
			    	 }, 
				{
					id:'search2',
					icon : 'search',
					text : '展开文件上传',
					click: function(){
						closeoropen();
					}
				}]
		});
		
		
		grid2 = PJ.grid("list2", {
			rowNum: 10,
			url:'<%=path%>/supplierpnmanage/listData?supplierId='+'${id}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-160,
			autoSrcoll:true,
			shrinkToFit:false,
			pager: "#pager2",
			inLineEdit: true,
			editurl:'<%=path%>/supplierpnmanage/updateSupplierPn',
			/* sortname : "s.code",
			sortorder : "asc", */
			colNames :["id","bsn", "件号","描述","cage code","生产商","ATA","Aircraft","Condition","Qty",
			           "SN","维修","证书","备注",
			           "供应能力","维修能力","库存能力","交换能力",
			           ],
			colModel :[PJ.grid_column("supplierId", {hidden:true,editable:true}),
			           PJ.grid_column("bsn", {hidden:true,key:true}),
			           PJ.grid_column("partNum", {sortable:true,width:180}),
			           PJ.grid_column("partName", {sortable:true,width:180}),
			           PJ.grid_column("cageCode", {sortable:true,width:80}),
			           PJ.grid_column("manName", {sortable:true,width:100}),
			           PJ.grid_column("ata", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("aircraft", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("condition", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("qty", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("sn", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("repair", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("cert", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("remark", {sortable:true,width:80,editable:true}),
			           PJ.grid_column("supplyAbility", {sortable:true,width:80,
							formatter: function(value){
								if(value=="1"){
									return  "是";
								}
						}}),
			           PJ.grid_column("stockAbility", {sortable:true,width:80,
							formatter: function(value){
								if(value=="1"){
									return  "是";
								}
						}}),
			           PJ.grid_column("repairAbility", {sortable:true,width:80,
							formatter: function(value){
								if(value=="1"){
									return  "是";
								}
						}}),
			           PJ.grid_column("exchangeAbility", {sortable:true,width:80,
							formatter: function(value){
								if(value=="1"){
									return  "是";
								}
						}}),
			           ]
		});
		
		
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
			//改变窗口大小自适应
			$(window).resize(function() {
				grid.setGridWidth(PJ.getCenterWidth());
				var display = $("#uploadBox").css("display");
				if(display=="block"){
					grid.setGridHeight(PJ.getCenterHeight()-182);
				}else{
					grid.setGridHeight(PJ.getCenterHeight()-142);
				}
			});
	});
	
function closeoropen(){
	$("#uploadBox").toggle(function(){
		var display = $("#uploadBox").css("display");
		if(display=="block"){
			$("#toolbar2 > div[toolbarid='search2'] > span").html("收起文件上传");
			grid2.setGridHeight(PJ.getCenterHeight()-182);
		}else{
			$("#toolbar2 > div[toolbarid='search2'] > span").html("展开文件上传");
			grid2.setGridHeight(PJ.getCenterHeight()-142);
		}
	});
}
	
	
	function trim(str){
		return $.trim(str);
	}
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: auto;
	vertical-align: text-bottom;
	padding-top: 10px;
} */
</style>
</head>

<body>
	<div id="layout1">
		<div position="center" title="供应商件号管理">
			<div id="toolbar2"></div>

			<div id="uploadBox" style="display: none;">
				<form id="form" name="form">
					<form:row columnNum="2">
						<form:column width="120">
							<form:left>
								<p style="line-height: 30px;">excel批量导入</p>
							</form:left>
							<form:right>
								<p>
									<input type="file" value="" id="file" name="file" />&nbsp; <input
										type="button" id="submit" value="上传" />
								</p>
							</form:right>
						</form:column>
						<form:column width="120">
							<form:left>
								<p style="line-height: 30px;">类型</p>
							</form:left>
							<form:right>
								<p>
									<select id="supplierPnType" name="supplierPnType">
									</select>
								</p>
							</form:right>
						</form:column>
					</form:row>

				</form>
			</div>
			<table id="list2" style=""></table>
			<div id="pager2"></div>
		</div>
	</div>
</body>
</html>