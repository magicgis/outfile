<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>位置管理</title>

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
	var layout, grid,grid2;
	$(function() {
		//layout = $("#layout1").ligerLayout();
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 300,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [
						{
							id:'add',
							icon : 'add',
							text : '新增',
							click: function(){
								var iframeId="ideaIframe1";
									PJ.topdialog(iframeId, '新增仓库地址', '<%=path%>/system/storehouseaddress/toAdd',
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
													$.ajax({
														url: '<%=path%>/system/storehouseaddress/addStorehouseAddress',
														data: postData,
														type: "POST",
														loading: "正在处理...",
														success: function(result){
																if(result.success){
																	//PJ.success(result.message);
																	PJ.grid_reload(grid);
																	dialog.close();
																	PJ.topdialog(iframeId, '新增位置明细', '<%=path%>/system/storehouseaddress/toAddElementAfterAdd?id='+result.message,
																			function(item, dialog){
																				var postData=top.window.frames[iframeId].getFormData();
																				PJ.ajax({
																					url: '<%=path%>/system/storehouseaddress/addElement?id='+result.message,
																					data: postData,
																					type: "POST",
																					loading: "正在处理...",
																					success: function(result){
																							if(result.success){ 
																								PJ.success(result.message);
																								PJ.grid_reload(grid);
																								PJ.grid_reload(grid2);
																							} else {
																								PJ.error(result.message);
																							}		
																					}
																				});
																				PJ.grid_reload(grid2);
																				dialog.close();
																			},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
																} else {
																	PJ.error(result.message);
																	dialog.close();
																}		
														}
													});
												}else{
													PJ.warn("数据还没有填写完整！");
												}
											},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
							}
						 },
						 {
								id:'modify',
								icon : 'modify',
								text : '修改',
								click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId="ideaIframe2";
										PJ.topdialog(iframeId, '修改竞争对手', '<%=path%>/system/storehouseaddress/toEdit?id='+id,
												function(item, dialog){
														 var postData=top.window.frames[iframeId].getFormData();	 							
														 $.ajax({
																url: '<%=path%>/system/storehouseaddress/editStorehouseAddress',
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
												},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
									
								}
						 },
						 {
								id:'search',
								icon : 'search',
								text : '展开搜索',
								click: function(){
									$("#searchdiv").toggle(function(){
										var display = $("#searchdiv").css("display");
										if(display=="block"){
											$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
											grid.setGridHeight(PJ.getCenterHeight()-152);
										}else{
											$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
											grid.setGridHeight(PJ.getCenterHeight()-102);
										}
									});
								}
						}
			        ]
		});
		
		$("#toolbar2").ligerToolBar({
			items : [{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe5";
							var ret = PJ.grid_getSelectedData(grid);
							 var id = ret["id"];
							PJ.topdialog(iframeId, '新增位置', '<%=path%>/system/storehouseaddress/toAddElement',
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getFormData();
										 PJ.ajax({
												url: '<%=path%>/system/storehouseaddress/addElement?id='+id,
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){ 
															PJ.success(result.message);
															PJ.grid_reload(grid);
															PJ.grid_reload(grid2);
														} else {
															PJ.error(result.message);
														}		
												}
											});
									PJ.grid_reload(grid2);
									dialog.close();}
								   ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
							id:'search',
							icon : 'search',
							text : '收起excel上传',
							click: function(){
								$("#uploadBox").toggle(function(){
									var display = $("#uploadBox").css("display");
									if(display=="block"){
										$("#toolbar2 > div[toolbarid='search'] > span").html("收起excel上传");
										grid2.setGridHeight(PJ.getCenterHeight()-300);
									}else{
										$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
										grid2.setGridHeight(PJ.getCenterHeight()-260);
									}
								});
							}
					}
			         ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/system/storehouseaddress/StorehouseAddressList',
			width : PJ.getCenterWidth(),
			height :PJ.getCenterHeight(),
			autoSrcoll:true,
			autowidth:true,
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			pager: "#pager1",
			//shrinkToFit:false,
			colNames :["id","仓库名","仓库地址","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("name", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("address", {sortable:true,width:500,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 10,
			autowidth:true,
			width : PJ.getCenterBottomWidth(),
			height : PJ.getCenterBottomHeight(),
			pager: "#pager2",
			autoSrcoll:true,
			shrinkToFit:true,
			autowidth:true,
			colNames : ["id","位置"],
			colModel : [PJ.grid_column("id", {sortable:false,hidden:true}),
			            PJ.grid_column("location", {sortable:false,width:150})
						],
						
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			$("#id").val(id);
			PJ.grid_search(grid2,'<%=path%>/system/storehouseaddress/elementList?id='+id);
			
		};
		
		$(window).resize(function() {
			GridResize();
		});
		
		function GridResize() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid2.setGridWidth(PJ.getCenterBottomWidth());
			grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
			grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		}
		grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
		grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/system/storehouseaddress/StorehouseAddressList');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/system/storehouseaddress/StorehouseAddressList');
			
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
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/system/storehouseaddress/uploadExcel?id='+getFormData().id+'&editType='+ getFormData().toString();
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
		            	PJ.grid_reload(grid2);
		            	$("#uploadBox").toggle(function(){
							$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
							grid2.setGridHeight(PJ.getCenterHeight()-260);
		            	});
	            	}
	            	
	            	/* if(da.flag==true){
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
	            	} */
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            }  
	        });
			
		 });
		
		function getFormData(){
			 var postData = {};
			 postData.data = $("#form").serialize();
			 postData.id = $("#id").val();
			 return postData;
		};
		
	});
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;display: none;">
		<form id="searchForm" action="">
			<div class="search-box">
				<form:row columnNum="2">
					<form:column>
						<form:left><p>仓库名</p></form:left>
						<form:right><p><input id="name" name="name" class="text" alias="name" oper="cn"/></p></form:right>
					</form:column>
					<form:column >
							<p style="padding-left:200px;">
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
		<div position="centerbottom" >
			<div id="toolbar2"></div>
			<div id="uploadBox" style="display: none;">
			<form id="form" name="form">
			 	<input type="hidden" name="id" id="id"/>
		   		
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
			<table id="list2"></table>
			<div id="pager2"></div>
		
		</div>
	</div>
</body>
</html>