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
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/suppliermanage/factoryExcel?id='+getFormData().id+'&editType='+ getFormData().toString();
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
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	}
	            	else{
	            		PJ.error(da.message);
	            		$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
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
							id:'add',
							icon : 'add',
							text : '新增生产商',
							click: function(){
								var iframeId="manufactoryIframe";
									PJ.topdialog(iframeId, '系统配置-新增生产商', '<%=path%>/suppliermanage/toAddManufactory?supplierId='+${id },
											function(item, dialog){
										var postData=top.window.frames[iframeId].getFormData();
										//var nullAble=top.window.frames[iframeId].validate();
										nullAble = true;
										//if(nullAble&&postData){
											$.ajax({
												url: '<%=path%>/suppliermanage/addManufactory?supplierId='+${id}+'&msn='+postData,
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
										//}
									},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
							
						}
					 },{
							id:'view',
							icon : 'view',
							text : '删除生产商',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
						 		var msn = ret["msn"];
											$.ajax({
												url: '<%=path%>/suppliermanage/deleteManufactory?supplierId='+'${id }'+'&msn='+msn,
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
										grid.setGridHeight(PJ.getCenterHeight()-182);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
										grid.setGridHeight(PJ.getCenterHeight()-142);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/suppliermanage/factoryList?id='+${id },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-182,
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/sales/clientinquiry/editElement',
			sortname : "ci.inquiry_date",
			colNames :["msn","cage_code","名字"],
			colModel :[PJ.grid_column("msn", {key:true,hidden:true}),
			           PJ.grid_column("code", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("name", {sortable:true,width:170,editable:true,align:'left'})
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
		<div position="center" title="供应商   ${supplierCode } ">
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