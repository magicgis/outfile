<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title> </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
var code='${cageCode}';
$(function(){
	layout = $("#layout1").ligerLayout();
	if(code==''){
	$("#toolbar").ligerToolBar({
		items : [
					{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '新增商品库', '<%=path%>/stock/search/toAdd',
										function(item, dialog){
											
											var nullAble=top.window.frames[iframeId].validate();
											if(nullAble){
												var postData=top.window.frames[iframeId].getFormData();
												if(postData!=null){
													$.ajax({
														url: '<%=path%>/stock/search/saveAdd',
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
								var bsn = ret["bsn"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '修改商品库', '<%=path%>/stock/search/toEdit?bsn='+bsn,
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												if(postData!=null){
													 $.ajax({
															url: '<%=path%>/stock/search/saveEdit',
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
					 },
					 {
							id:'view',
							icon : 'view',
							text : '文件管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/sales/clientinquiry/file?id='+id,
										undefined,function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
							}
					 },
			         {
							id:'search',
							icon : 'search',
							text : '收起搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid.setGridHeight(PJ.getCenterHeight()-202);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-102);
									}
								});
							}
					 }				 
		         ]
	});
	}else{
		$("#toolbar").ligerToolBar({
			items : [
					 {
							id:'search',
							icon : 'search',
							text : '收起搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid.setGridHeight(PJ.getCenterHeight()-202);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-102);
									}
								});
							}
					 }
							 
			         ]
		});
	}
	$("#exact").change(function(){
		if($("#exact").prop("checked")){
			$("#check").val("eq");
		}else if(!$("#exact").prop("checked")){
			$("#check").val("cn");
		}
	});

	
	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/order/historicalorderprice/clientOrderList',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-202,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "hop.`year`",
		sortorder : "desc",
		colNames :["id","bsn","客户","件号","描述","年份","数量","价格","更新时间"],
		colModel :[
		           	PJ.grid_column("id", {key:true,hidden:true,sortable:true, width:50}),
		           	PJ.grid_column("bsn", {sortable:false,width:80,align:'left'}),
		        	PJ.grid_column("code", {sortable:false,width:20,align:'left'}),
		           	PJ.grid_column("partNum", {sortable:false,width:80,align:'left'}),
		           	PJ.grid_column("partName", {sortable:false,width:150,align:'left'}),	
		        	PJ.grid_column("year", {sortable:false,width:20,align:'left'}),
		        	PJ.grid_column("amount", {sortable:false,width:40,align:'left'}),
		           	PJ.grid_column("price", {sortable:false,width:40,align:'left'}),
		           	PJ.grid_column("updateTimestamp", {sortable:false,width:80,align:'left'}),
		           ]
	});
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid,'<%=path%>/order/historicalorderprice/clientOrderList');
	    }  
	});
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/order/historicalorderprice/clientOrderList');		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	$("#partNum").blur(function(){
		var text = $("#partNumber").val();
		$("#partNumber").val(trim(text));
	});
	
	$("#cageId").blur(function(){
		var text = $("#cageId").val();
		$("#cageId").val(trim(text));
	});
	
	$("#nsnId").blur(function(){
		var text = $("#nsnId").val();
		$("#nsnId").val(trim(text));
	});
	 
	$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
	
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
	
	$("#submit").click(function(){
		var url = '<%=path%>/order/historicalorderprice/uploadExcelForClient?editType='+ getFormData().toString();
		PJ.showLoading("上传中....");
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
	            	PJ.success(da.message);
	            	PJ.grid_reload(grid);
	            	$("#uploadBox").toggle(function(){
						$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
						grid.setGridHeight(PJ.getCenterHeight()-202);
	            	});
            	}else{
            		PJ.hideLoading();
            		PJ.warn(da.message);
            		<%-- $("#uploadBox").toggle(function(){
						$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
						grid.setGridHeight(PJ.getCenterHeight()-202);
	            	});
            		iframeId = 'errorframe'
            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/stock/search/toUnknow',
            				undefined,function(item,dialog){
		            			$.ajax({
									url: '<%=path%>/stock/search/deleteData',
									type: "POST",
									loading: "正在处理...",
									success: function(result){
									}
								});
            					dialog.close();}, 1000, 500, true); --%>
            	}
   				
            },  
            error: function (data, status, e) { 
            	PJ.hideLoading();
            	PJ.error("上传异常！");
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
	
	function getUpdateData(){
		 var postData = {};
		 postData.data = $("#updateform").serialize();
		 return postData;
	};
	
	<%-- $("#update").click(function(){
		var id =$("#id").val();
		var url = '<%=path%>/stock/search/updateType?editType='+ getFormData().toString();
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
            	}else{
            		PJ.error("上传异常！");
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
            /* error: function (data, status, e) { 
            	PJ.error("上传异常！");
            }  */ 
        });
		
	 }); --%>
	
	//客户代码来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/clientCode',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#client").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
});
function trim(str){
	return $.trim(str);
}
</script>
</head>

<body>
<div id="layout1">
	<div position="center">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 80px;display: unblock;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="5">
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="partNumber" class="text" type="text" name="tp.part_num" alias="tp.part_num" oper="eq"/> </p></form:right>
						</form:column>
						<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="client" name="clientCode" alias="c.id" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
						</form:column>
						<form:column>
								<form:left></form:left>
								<form:right><p></p>
								</form:right>
						</form:column>
						<form:column>
								<form:left></form:left>
								<form:right><p></p>
								</form:right>
						</form:column>
						<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
						</form:column>
					</form:row>
					<form:row columnNum="2">
						<form id="form" name="form">
						<form:column width="120">
							<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
							<form:right>
								<p><input type="file" value="" id="file" name="file"/>&nbsp;
								   <input type="button" id="submit" value="新增上传"/>
								</p>
							</form:right>
						</form:column>
						</form>
						
					</form:row> 
				</div>
			</form>
		</div>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>
<script type="text/javascript">
	var tooltipObj = new DHTMLgoodies_formTooltip();
	tooltipObj.setTooltipPosition('below');
	tooltipObj.setPageBgColor('#BBBBBB');
	tooltipObj.setTooltipCornerSize(15);
	tooltipObj.initFormFieldTooltip();
</script>
</body>
</html>