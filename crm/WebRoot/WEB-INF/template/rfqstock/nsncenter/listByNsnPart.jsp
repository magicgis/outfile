<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
    String cageId = (String)request.getAttribute("cageId");
    String title = (String)request.getAttribute("title");
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
$(function(){
	layout = $("#layout1").ligerLayout();
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
									grid.setGridHeight(PJ.getCenterHeight()-195);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-142);
								}
							});
						}
				 },
				 {
						id:'add',
						icon : 'search',
						text : '获取厂商信息',
						click: function(){
							
						}
				 }				 
		         ]
	});
	var cageId = '<%=cageId%>';
	var url = '<%=path%>/rfqstock/nsncenter/searchPageByNsnPart';
	if(cageId!==''){
		url = url + '?cageId='+cageId;
	}
	grid = PJ.grid("list1", {
		rowNum: 10,
		url:url,
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-200,
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "cageId",
		sortorder : "desc",
		colNames :["CAGE编号","CAGE名称","FSG编号","FSC编号","件号","NSN编号","NSN名称","被替换NSN编号"],
		colModel :[
		           	PJ.grid_column("cageId", {key:true, sortable:true, width:50}),
		           	PJ.grid_column("cageName", {sortable:false,width:80}),
		           	PJ.grid_column("fsgId", {sortable:false,width:80}),
		           	PJ.grid_column("fscId", {sortable:false,width:80}),
		           	PJ.grid_column("partNum", {sortable:false,width:80}),
		           	PJ.grid_column("nsnId", {sortable:false,width:80}),		           	
		           	PJ.grid_column("nsnName", {sortable:false,width:80}),
		           	PJ.grid_column("replacedbyNsnId", {sortable:false,width:80})
		           ]
	});
	
	//搜索
	$("#searchBtn").click(function(){
	 	var nsnId  = $("#nsnId").val();
	 	var partNum  = $("#partNum").val();
	 	var url = '<%=path%>/rfqstock/nsncenter/searchPageByNsnPart?&nsnId='+nsnId+'&partNum='+partNum;
		var cageId = '<%=cageId%>';
		var fuzzySearchForCage = false;
		if(cageId==''){
			cageId = $("#cageId").val();
			fuzzySearchForCage = true;
		}
		if(cageId!==''){			
			url = url + '&cageId='+cageId;
			if(fuzzySearchForCage){
				url = url + '&fuzzySearchForCage=1';
			}

		}
		PJ.grid_search(grid,url);
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
</script>
</head>

<body>
<div id="layout1">
	<div position="center" title="${title}">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;display: unblock;">
			<form id="searchForm">
				<div class="search-box">
				<c:if test="${cageId eq ''}">
					<form:row columnNum="4">
						<form:column>
							<form:left><p>CAGE号：</p></form:left>
							<form:right><p><input id="cageId" class="text" type="text" name="cageId"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>NSN号：</p></form:left>
							<form:right><p><input id="nsnId" class="text" type="text" name="nsnId"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="partNum" class="text" type="text" name="partNum"/> </p></form:right>
						</form:column>
						<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
						</form:column>
					</form:row>
				</c:if>
				<c:if test="${cageId != ''}">
					<form:row columnNum="3">
						<form:column>
							<form:left><p>NSN号：</p></form:left>
							<form:right><p><input id="nsnId" class="text" type="text" name="nsnId"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="partNum" class="text" type="text" name="partNum"/> </p></form:right>
						</form:column>
						<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
						</form:column>
					</form:row>
				</c:if>
				</div>
			</form>
		</div>
		<table id="list1"></table>
		<div id="pager1"></div>
	</div>
</div>
</body>
</html>