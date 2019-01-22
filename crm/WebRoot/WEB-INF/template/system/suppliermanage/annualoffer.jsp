<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商年度报价管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/suppliermanage/quoteUploadExcel?id='+getFormData().id;
			PJ.showLoading("处理中...");
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
	            	PJ.hideLoading();
	            	var da = eval(data)[0];
	            	if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	}else{
	            		PJ.hideLoading();
	            		$("#uploadBox").toggle(function(){
	            			$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            		iframeId = 'errorframe'
	            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/supplierpnmanage/toUnknow',
	            				undefined,function(item,dialog){
			            			$.ajax({
										url: '<%=path%>/stock/search/deleteData',
										type: "POST",
										loading: "正在处理...",
										success: function(result){
										}
									});
	            					dialog.close();	PJ.grid_reload(grid);}, 1000, 500, true);
	            	}
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            }  
	        });
			
		 });
		
		
		$("#toolbar").ligerToolBar({
			items : [
					 {
							id:'view',
							icon : 'view',
							text : '删除',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
						 		var id = ret["id"];
											$.ajax({
												url: '<%=path%>/suppliermanage/deleteSupplierAnnualOffer?id='+id,
												data: '',
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
					 }
					 ,{
							id:'search',
							icon : 'search',
							text : '收起excel上传',
							click: function(){
								$("#uploadBox").toggle(function(){
									var display = $("#uploadBox").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起excel上传");
										grid.setGridHeight(PJ.getCenterHeight()-155);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
										grid.setGridHeight(PJ.getCenterHeight()-122);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/suppliermanage/annualOfferList?id='+'${id }',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-155,
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/sales/clientinquiry/editElement',
			sortname : "ci.inquiry_date",
			colNames :["id","msn","厂商", "件号", "描述", "单位","数量","MOQ", "单价","周期",
			             "状态", "证书","有效期","location","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("msn", {}),
			           PJ.grid_column("manName", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("desc", {sortable:true,width:250,align:'left'}),
						PJ.grid_column("unit", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("amount", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("moq", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("price", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("leadTime", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("conditionCode", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("certificationCode", {sortable:true,width:120,align:'left'}),
						PJ.grid_column("validity", {sortable:true,width:100,align:'left',align:'left'}),
						PJ.grid_column("location", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("remark", {sortable:true,width:200,align:'left'}),
			           ]
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
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="">
		<div id="toolbar"></div>
		<div id="uploadBox" >
			<form id="form" name="form">
			 	<input type="hidden" name="id" id="id" value="${id}"/>
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
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>