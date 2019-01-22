<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统帮助管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid,searchHeight,heightDiff = 142;
	$(function() {
		layout = $("#layout1").ligerLayout();
		searchHeight = $("#searchdiv").height();//取得搜索区的高度

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ {
				icon:'add',
				text:'新建',
				click : function(){
					var iframeId = "addHelpDialog";
					var title = "新建帮助";
					var url = "<%=path%>/help/add";
					PJ.openTopDialog(iframeId,title,url,{
						save:function(item,dialog){
							//调用子页面的校验方法
							var flag = PJ.iframeValidate(iframeId);
							if(flag){
								 dialog.hide(); //先不关闭,否则子窗口会被销毁
					             PJ.ajax({
				                    url: "<%=path%>/help/add",
				                    //调用子页面的数据获取方法
				                    data: PJ.iframeGetFormData(iframeId),
				                    loading: "正在保存...",
				                    success: function(result) {
				                        if (result.success) {
				                            PJ.success(result.message, function() {
				                                PJ.grid_reload(grid);
				                            });
				                        } else {
				                            PJ.error(result.message);
				                        }
				                        dialog.close();
				                    }
					              });
							}
						},
						cancle:function(item,dialog){
							dialog.close();
						},
						width:800,
						height:550,
						showMax:true
					});
				}
			},{
				icon:'modify',
				text:'修改',
				click : function(){
					var ret = PJ.grid_getSelectedData(grid);
					var helpUuid = ret["helpUuid"];
					var iframeId = "modifyHelpDialog";
					var title = "修改帮助";
					var url = "<%=path%>/help/modify/"+helpUuid;
					PJ.openTopDialog(iframeId, title, url, {
						save:function(item,dialog){
							//调用子页面的校验方法
							var flag = PJ.iframeValidate(iframeId);
							if(flag){
								 dialog.hide(); //先不关闭,否则子窗口会被销毁
					             PJ.ajax({
				                    url: "<%=path%>/help/modify",
				                    //调用子页面的数据获取方法
				                    data: PJ.iframeGetFormData(iframeId),
				                    loading: "正在保存...",
				                    success: function(result) {
				                        if (result.success) {
				                            PJ.success(result.message, function() {
				                                PJ.grid_reload(grid);
				                            });
				                        } else {
				                            PJ.error(result.message);
				                        }
				                        dialog.close();
				                    }
					              });
							}
						},
						cancle:function(item,dialog){
							dialog.close();
						},
						width:800,
						height:550,
						showMax:true
					});
				}
			}, {
				icon : 'delete',
				text : '删除',
				click: function(){
					var ret = PJ.grid_getSelectedData(grid);
					var helpUuid = ret["helpUuid"];
					PJ.confirm("确定删除["+ret["title"]+"]", function(yes){
						if(yes){
							PJ.ajax({
			                    url: "<%=path%>/help/remove",
			                    //调用子页面的数据获取方法
			                    data: 'id='+helpUuid,
			                    loading: "正在删除...",
			                    success: function(result) {
			                        if (result.success) {
			                            PJ.tip(result.message, function() {
			                                PJ.grid_reload(grid);
			                            });
			                        } else {
			                            PJ.error(result.message);
			                        }
			                    }
				              });
						}
					});
				}
			}, {
				icon : 'refresh',
				text : '刷新',
				click: function(){
					PJ.grid_reload(grid);
				}
			}, {
				icon : 'view',
				text : '查看',
				click: function(){
					PJ.view("list1");
				}
			}, {
				icon : 'logout',
				text : '导出excel',
				click: function(){
					PJ.grid_export("list1");
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
							grid.setGridHeight(PJ.getCenterHeight()-(heightDiff+searchHeight));
						}else{
							$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
							grid.setGridHeight(PJ.getCenterHeight()-heightDiff);
						}
					});
				}
			}, {
				id:'help',
				icon : 'help',
				text : '帮助',
				click: function(){
					PJ.help("help");
				}
			}   ]
		});

		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/help/list/data',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-heightDiff,
			hideview : true,
			sortname : "helpUuid",
			colNames : [ "帮助主键", "关键字", "标题", "帮助内容"],
			colModel : [ PJ.grid_column("helpUuid", {key:true,sortable:true,hidden:true}),
					PJ.grid_column("code", {sortable:true}),
					PJ.grid_column("title", {sortable:true}),
					PJ.grid_column("content", {hidden:true})]
		});
		
		//搜索条件是日期类型的加入日期控件
		PJ.datepickerAll();
		
		//右上角的帮助按扭float:right
		$("#toolbar > div[toolbarid='help']").addClass("fr");
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/help/list/data');
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
				grid.setGridHeight(PJ.getCenterHeight()-(heightDiff+searchHeight));
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-heightDiff);
			}
		});
	});
</script>
</head>

<body style="padding:3px">
	<div id="layout1">
		<div position="center" title="帮助列表">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="3">
							<form:column>
								<form:left> <p>关键字：</p></form:left>
								<form:right><p><sf:searchForm name="code" prefix="" type="text" oper="eq"/></p></form:right>
							</form:column>
							<form:column>
								<form:left> <p>标题：</p></form:left>
								<form:right> <p><sf:searchForm type="text" prefix="" name="title" oper="cn" /></p></form:right>
							</form:column>
							<form:column>
								<p style="padding-left:10px;"><input class="btn btn_orange" type="button" value="确定" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"></p>
							</form:column>
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