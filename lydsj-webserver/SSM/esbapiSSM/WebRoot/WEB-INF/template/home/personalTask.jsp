<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">  
<title>个人任务</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import> 
<script type="text/javascript">
    var dcl,ywc,dcltoolbar,ycltoolbar;
	$(function(){
		$("#layout1").ligerLayout();
 		$("#framecenter").ligerTab({
 			 contextmenu:false//不要右键菜单
 		});
 		//数据区的工具栏
		dcltoolbar = $("#toolbardcl").ligerToolBar({
			items : [ 
			{
					icon : 'refresh',
					text : '刷新',
					click: function(){
						PJ.grid_reload(dcl);
					}
				},
				 {
					id:'clrw',
					icon : 'process',
					text : '处理任务',
					click: function(){
						 var ret = PJ.grid_getSelectedData(dcl);
						 PJ.addTab(ret.name, ret.form + '?history=true&&taskId=' + ret.id);
					}
				}
			  ]
			});
			 
		 //数据区的工具栏
		ycltoolbar = $("#toolbarywc").ligerToolBar({
			items : [ 
			{
					icon : 'refresh',
					text : '刷新',
					click: function(){
						PJ.grid_reload(ywc);
					}
				},{
					id:'ckrw',
					icon : 'view',
					text : '查看',
					click: function(){
						 var ret = PJ.grid_getSelectedData(ywc);
						 PJ.addTab(ret.name+'_历史', basePath +'/workflow/historyTask/' + ret.id);
					}
				}, {
					id:'search',
					icon : 'search',
					text : '展开搜索',
					click: function(){
						$("#searchdiv").toggle(function(){
							var display = $("#searchdiv").css("display");
							if(display=="block"){
								$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
								ywc.setGridHeight(PJ.getCenterHeight()-202);
							}else{
								$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
								ywc.setGridHeight(PJ.getCenterHeight()-142);
							}
						});
					}
				}
			  ]
		});	
		
		dcl = PJ.grid("list1", {
			rowNum: 20,
			width : PJ.getCenterWidth(),
			pager:"#pager1",
			height :PJ.getCenterHeight() - 146,
			url:'<%=path%>/workflow/mytask',
			colNames : [ "任务ID", "所属模块","任务名","任务名", "任务描述", "创建时间","form" ],
			colModel : [
						PJ.grid_column("id", {key:true,width:80}),
						PJ.grid_column("deploymentName", {}),
						PJ.grid_column("taskName", {hidden:true,align:'left'}),
						PJ.grid_column("name", {}),
						PJ.grid_column("description", {}),
						PJ.grid_column("createTime", {width:80}),
						PJ.grid_column("form", {hidden:true})
					 ],
			ondblClickRow:function(){
				dcltoolbar.toolBar.find(".l-toolbar-item[toolbarid=clrw]").trigger("click");
			}
		}); 
		
		ywc = PJ.grid("list2", {
			rowNum: 20,
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-146,
			pager:"#pager2",
			url:'<%=path%>/workflow/myfinished',
			colNames : [ "任务ID", "所属模块", "任务名" ,"完成时间"],
			colModel : [
						PJ.grid_column("id", {key:true}),
						PJ.grid_column("name", {}),
						PJ.grid_column("curentName", {}),
						PJ.grid_column("endTime", {})
					 ],
			 ondblClickRow:function(){
				 ycltoolbar.toolBar.find(".l-toolbar-item[toolbarid=ckrw]").trigger("click");
			 }
		});
		
		//搜索框 收缩/展开
		$(".searchtitle .togglebtn").live('click', function() {
			var searchbox = $(this).parent().nextAll("div.searchbox:first");
			toggle(searchbox);
		});

		//边框去除
		$(".l-panel").css("border", "none");
		
		//搜索
		 $("#searchBtn").click(function(){
		     var endTime = $("#endTime").val();
		     var param = "?endTime="+$("#endTime").val()
		     			+"&endTimeTo="+$("#endTimeTo").val()
		     			+"&modelName="+$("#modelName").val();
			 PJ.grid_search(ywc,'<%=path%>/workflow/getAlreadyProcessed');
		 });
		 
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//jqGrid 自适应
		$(window).resize(function() {
			dcl.setGridWidth(PJ.getCenterWidth());
			dcl.setGridHeight(PJ.getCenterHeight()-146);
			ywc.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				ywc.setGridHeight(PJ.getCenterHeight()-206);
			}else{
				ywc.setGridHeight(PJ.getCenterHeight()-146);
			}
		});
	});
	
	//重新加载数据
	function reloadPersonalTask()
	{
		PJ.grid_reload(dcl);
	}
	
</script>

</head>
	<body style="overflow: hidden;">
	   	<div id="layout1">
			<div position="center" id="framecenter">
				<div tabid="dcl" title="待处理任务" >
					<div id="toolbardcl"></div>
					<table id="list1"></table>
					<div id="pager1"></div>
				</div>  
				<div tabid="ywc" title="已完成任务" >
					<div id="toolbarywc"></div>
					<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
					<form id="searchForm">
						<div class="search-box">
							<form:row columnNum="3">
								<form:column>
									<form:left> <p>模块名称：</p></form:left>
									<form:right><p><ef:editForm name="modelName" type="text" cssName="input"/></p></form:right>
								</form:column>
								<form:column>
									<form:left> <p>开始时间：</p></form:left>
									<form:right> 
										<div style="float: left;width:45%;"><ef:editForm name="endTime" type="text" date="true" cssName="input"/></div>
						           		<div  style="float: left;width:10%;">
						           			<p style="padding-left: 10px;line-height: 28px;">至</p>
						           		</div>
						           		<div style="float: left;width:45%;"><ef:editForm name="endTimeTo" type="text" date="true" cssName="input"/></div>
									</form:right>
								</form:column>
								<form:column>
									<p style="padding-left:10px;"><input class="btn btn_orange" type="button" value="确定" id="searchBtn">
									<input class="btn btn_blue" type="button" value="重置" id="resetBtn"></p>
								</form:column>
							</form:row>
						</div>
					</form>
				</div>
					<table id="list2"></table>
					<div id="pager2"></div>
				</div>
			</div>
		</div>
	</body>
</html>