<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-流程管理</title>

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
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [
						{
							id:'edit',
							icon : 'edit',
							text : '修改处理人',
							click: function(){
								 var ret = PJ.grid_getSelectedData(grid);
								 var ids=ret["ids"];
								var iframeId="ideaIframe1";
									PJ.topdialog(iframeId, '修改处理人', '<%=path%>/workflow/touserlist',
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
													$.ajax({
														url: '<%=path%>/workflow/updateUser?ids='+ids,
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
						 } ,
				         {
								id:'search',
								icon : 'search',
								text : '展开搜索',
								click: function(){
									$("#searchdiv").toggle(function(){
										var display = $("#searchdiv").css("display");
										if(display=="block"){
											$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
											grid.setGridHeight(PJ.getCenterHeight()-170);
										}else{
											$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
											grid.setGridHeight(PJ.getCenterHeight()-120);
										}
									});
								}
						}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			url:'<%=path%>/workflow/processListData',
			colNames : [ "id", "ywTableId","tableName","任务单号","任务名", "任务描述","所属模块","创建时间","任务处理人","form","ids" ],
			colModel : [
						PJ.grid_column("id", {key:true,width:80,hidden:true}),
						PJ.grid_column("ywTableId", {key:true,hidden:true}),
						PJ.grid_column("tableName", {hidden:true}),
						PJ.grid_column("taskNumber", {}),
						PJ.grid_column("name", {}),
						PJ.grid_column("descr", {}),
						PJ.grid_column("deploymentName", {}),
						PJ.grid_column("create", {width:80}),
						PJ.grid_column("userName", {width:60}),
						PJ.grid_column("form", {hidden:true}),
						PJ.grid_column("ids", {hidden:true})
					 ]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/workflow/processListData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/workflow/processListData');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
	});
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>任务单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="taskNumber" alias="IFNULL(IFNULL(ip.IMPORT_NUMBER,ipp.PAYMENT_NUMBER),cwo.ORDER_NUMBER)" oper="cn"/> </p></form:right>
							</form:column>
								<form:column>
								<form:left><p>任务名：</p></form:left>
								<form:right><p><input id="searchForm3" class="text" type="text" name="name" alias="jt.name_" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>所属模块：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="jdt.name_" oper="eq">
							        			<option value="">全部</option>
							        			<option value="合同评审">合同评审</option>
							        			<option value="付款申请">付款申请</option>
							        			<option value="异常件审核">异常件审核</option>
							            		</select>
											</p>
								</form:right>
							</form:column>	
							<form:column >
								<form:right>
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
							</form:right>
							</form:column>
						<%-- 	<form:column>
								<form:left><p>装箱日期：</p></form:left>
								<form:right><p><input id="shipStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'shipEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="shipStart" alias="cs.ship_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="shipEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'shipStart\')}',dateFmt:'yyyy-MM-dd'})" name="shipEnd" alias="cs.ship_date" oper="lt"/> </p></form:right>
							</form:column> --%>
						</form:row>
					<%-- 		<form:row columnNum="5">
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right><p></p></form:right>
							</form:column>
								<form:column >
								<form:right>
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
							</form:right>
							</form:column>
							</form:row> --%>
					</div>
				</form>
			</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>