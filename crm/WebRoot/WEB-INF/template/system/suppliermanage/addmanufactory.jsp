<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>${title} </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
var code='${cageCode}';
$(function(){
	
	$("#submit").click(function(){
		
		var url = '<%=path%>/stock/search/uploadExcel';
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
            	//var message = decodeURI(data);
            	//var a = decodeURI(data);
            	//var da = jQuery.parseJSON(jQuery(data).text());
            	var da = eval(data)[0];
            	//var falg = data.flag;
            	if(da.flag==true){
            		
	            	PJ.success(da.message);
	            	closeoropen();
	            	PJ.grid_reload(grid);
            	}
            	else{
            		PJ.error(da.message);
            		closeoropen();
    	           
            	}
   				
            },  
            error: function (data, status, e) { 
            	PJ.error(data.message);
            	closeoropen();
            }  
        });  
	});
	
	$("#toolbar").ligerToolBar({
		items : [{
			id:'add',
			icon : 'add',
			text : '新增',
			click: function(){
				var iframeId="ideaIframe1";
					PJ.topdialog(iframeId, '新增商品库', '<%=path%>/stock/search/toUpdateCageCode',
							function(item, dialog){
								
								var nullAble=top.window.frames[iframeId].validate();
								if(nullAble){
									var postData=top.window.frames[iframeId].getFormData();
									if(postData!=null){
										$.ajax({
											url: '<%=path%>/stock/search/saveAddCageCode',
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
		 }			 
		         ]
	});
	
	layout = $("#layout1").ligerLayout();
	grid = PJ.grid("list1", {
		rowNum: 50,
		url:'<%=path%>/stock/search/findCagePage?cageCode='+'${cageCode}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-160,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "cageId",
		sortorder : "desc",
		colNames :["cage code","厂商", "MSN","生产能力","检测能力","维修能力","改装能力","翻修能力"],
		colModel :[
		           	PJ.grid_column("cageCode", {key:true, sortable:true, width:50}),
		           	PJ.grid_column("manName", {sortable:false,width:80}),
		        	PJ.grid_column("msn", {sortable:false,width:40}),
		        	PJ.grid_column("capMan", {key:true, sortable:true, width:50,
		        		 formatter: function(value){
								if (value==1) {
									return "是";
								} else if(value==0){
									return "否";							
								}else {
									return "";
								}
							}   }),
		        	PJ.grid_column("capInspection", {key:true, sortable:true, width:50,
		        		 formatter: function(value){
								if (value==1) {
									return "是";
								} else if(value==0){
									return "否";							
								}else {
									return "";
								}
							} }),
		        	PJ.grid_column("capRepair", {key:true, sortable:true, width:50,
		        		 formatter: function(value){
								if (value==1) {
									return "是";
								} else if(value==0){
									return "否";							
								}else {
									return "";
								}
							} }),
		        	PJ.grid_column("capModification", {key:true, sortable:true, width:50,
		        		 formatter: function(value){
								if (value==1) {
									return "是";
								} else if(value==0){
									return "否";							
								}else {
									return "";
								}
							} }),
		        	PJ.grid_column("capOverhaul", {key:true, sortable:true, width:50,
		        		 formatter: function(value){
								if (value==1) {
									return "是";
								} else if(value==0){
									return "否";							
								}else {
									return "";
								}
							} })
		           ]
	});
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid,'<%=path%>/stock/search/findCagePage');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/stock/search/findCagePage');
		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	$("#cageCode").blur(function(){
		var text = $("#cageCode").val();
		$("#cageCode").val(trim(text));
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

function closesearch(){
	$("#searchdiv").toggle(function(){
		var display1 = $("#uploadBox").css("display");
		var display2 = $("#searchdiv").css("display");
		if(display1=="block"&&display2=="block"){
			$("#toolbar > div[toolbarid='search1'] > span").html("收起搜索");
			grid.setGridHeight(PJ.getCenterHeight()-200);
		}else if(display2=="block"){
			$("#toolbar > div[toolbarid='search1'] > span").html("收起搜索");
			grid.setGridHeight(PJ.getCenterHeight()-165);
		}
		else if(display1=="block"&&display2=="none"){
			$("#toolbar > div[toolbarid='search1'] > span").html("展开搜索");
			grid.setGridHeight(PJ.getCenterHeight()-150);
		}
		else{
			$("#toolbar > div[toolbarid='search1'] > span").html("展开搜索");
			grid.setGridHeight(PJ.getCenterHeight()-112);
		}
	});
}

function closeoropen(){
	$("#uploadBox").toggle(function(){
		var display1 = $("#uploadBox").css("display");
		var display2 = $("#searchdiv").css("display");
		if(display1=="block"&&display2=="block"){
			$("#toolbar > div[toolbarid='search2'] > span").html("收起文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-200);
		}else if(display1=="block"){
			$("#toolbar > div[toolbarid='search2'] > span").html("收起文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-155);
		}else if(display1=="none"&&display2=="block"){
			$("#toolbar > div[toolbarid='search2'] > span").html("展开文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-155);
		}
		else{
			$("#toolbar > div[toolbarid='search2'] > span").html("展开文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-112);
		}
	});
	}
function trim(str){
	return $.trim(str);
}

//获取表单数据
function getFormData(){
	var ret = PJ.grid_getSelectedData(grid);
	var msn = ret["msn"];
	return msn;
}
</script>
</head>

<body>
<div id="layout1">
	<div position="center" title="${title}">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;display: block;">
			<form id="searchForm">
				
				<div class="search-box">
					<form:row columnNum="4">
						<form:column>
							<form:left><p>CAGE CODE：</p></form:left>
							<form:right><p><input id="cageCode" class="text" type="text" name="cageCode"  oper="cn" alias="tm.cage_code" value="${cageCode}" <c:if test="${not empty cageCode}">readonly="readonly"</c:if>/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>厂商名称：</p></form:left>
							<form:right><p><input id="manName" class="text" type="text" name="manName"  oper="cn" alias="tm.man_name" value="${cageCode}" <c:if test="${not empty manName}">readonly="readonly"</c:if>/> </p></form:right>
						</form:column>
						
						<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
						<c:if test="${ empty cageCode}">
						<form:column ><form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
							</form:column>
						</c:if>
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