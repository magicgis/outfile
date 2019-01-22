<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>流程部署</title>
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>  
	<script type="text/javascript">
		var dep_grid, def_grid;
		
        $(function (){
        	$("#div_main").ligerLayout({
    			centerBottomHeight:150
    		});
        	
        	//流程部署工具栏
    		$("#dep_toolbar").ligerToolBar({
    			items : [ {
    				icon : 'add',
    				text : '流程部署',
    				click : function() {
    					deploymentAdd();
    				}
    			}, {
    				icon : 'delete',
    				text : '删除',
    				click: function(){
    					deploymentDelete();
    				}
    			}, {
    				icon : 'refresh',
    				text : '刷新',
    				click: function(){
    					PJ.grid_reload(dep_grid);
    				}
    			}]
    		});
        	
        	//流程定义工具栏
    		$("#def_toolbar").ligerToolBar({
    			items : [ {
    				icon : 'order',
    				text : '查看流程图',
    				click : function() {
    					viewFlowChart();
    				}
    			}, {
    				icon : 'refresh',
    				text : '刷新',
    				click: function(){
    					PJ.grid_reload(def_grid);
    				}
    			}]
    		});
        	
    		dep_grid = PJ.grid("dep_grid", {
    			url: '<%=path%>/workflow/deploymentData',
    			width : PJ.getCenterWidth(),
    			height : PJ.getCenterHeight() - 142,
    			pager: '#dep_page',
    			colNames : ["部署ID", "部署名称", "部署时间"],
    			colModel : [PJ.grid_column("id", {key:true}),
    					PJ.grid_column("name", {}),
    					PJ.grid_column("time", {})],
				onSelectRow: function(rowid, aData){
					var ret = PJ.grid_getSelectedData(dep_grid);
					var id = ret['id'];
    				var url = "<%=path%>/workflow/definitionData?id=" + id;
					def_grid.jqGrid('setGridParam', {url:url}).trigger('reloadGrid');
    			}
    		});
    		
    		def_grid = PJ.grid("def_grid", {
    			width : PJ.getCenterWidth(),
    			height : 50,
    			pager: '#def_page',
    			colNames : ["ID", "名称", "流程定义key", "版本", "资源图片名称", "部署ID"],
    			colModel : [PJ.grid_column("id", {key:true}),
    					PJ.grid_column("name", {}),
    					PJ.grid_column("key", {}),
    					PJ.grid_column("version", {}),
    					PJ.grid_column("diagramResourceName", {}),
    					PJ.grid_column("deploymentId", {})]
    		});
        	
        	//去外边框
    		$("#gbox_list1").css("border", "none");

    		//jqGrid 自适应
    		$(window).resize(function() {
    			dep_grid.setGridWidth(getWidth());
    			dep_grid.setGridHeight(PJ.getCenterHeight() - 142);
    		});
        });
        
      	//获取布局center高度
    	function getHeight() {
    		return parseInt($("div .l-layout-center").css("height").replace(
    				"px", "")) - 30;
    	}
      	
    	//获取布局center宽度
    	function getWidth() {
    		return parseInt($("div .l-layout-center").css("width").replace(
    				"px", ""));
    	}
    	
    	//流程部署刷新
    	function deploymentRefresh()
    	{
    		dep_grid.reload();
    	}
    	
    	//流程部署
    	function deploymentAdd()
    	{
   			var iframeId = 'newdeployment';
			PJ.dialog(iframeId,'部署流程','<%=path%>/workflow/deploymentAdd', function(item,dialog){
				//调用子页面的校验方法
				var flag = window.frames[iframeId].validate();
				if(flag){
					dialog.hide();//先不关闭,否则子窗口会被销毁
					PJ.showLoading("文件上传中...");
					//window.frames[iframeId].form.submit();
					window.frames[iframeId].upload();
				}
			},function(item, dialog){dialog.close();},500,160);
    	}
    	
    	//流程部署删除
    	function deploymentDelete()
    	{
    		var ret = PJ.grid_getSelectedData(dep_grid);
    		if (ret)
    		{
				var id = ret['id'];
				var name = ret['name'];
				PJ.confirm("确定要删除[" + name + "]吗？", function(yes){
					if(yes){
						PJ.ajax({
							url:'<%=path%>/workflow/deploymentDelete',
							data:'id=' + id,
							success:function(result){
								if(result.success){
									PJ.tip(result.message,function(){
										PJ.grid_reload(dep_grid);
										PJ.grid_reload(def_grid);
									});
								}else{
									PJ.error(result.message);
								}
							}
						});
					}
				});
    		}
    	}
    	
    	//查看流程图
    	function viewFlowChart()
    	{
    		var ret = PJ.grid_getSelectedData(def_grid);
    		if (ret)
    		{
				var id = ret['id'];
				var name = ret['name'];
				var title = name + "的流程图";
				var url = '<%=path%>/workflow/viewlct?processDefinitionId=' + id;
				var iframeId = id + '_workflowPic';
				var width = 720;
				var height = 520;
				PJ.openTopDialog(id, title, url, {
					cancle:function(item,dialog){
						dialog.close();
					},
					width:width,
					height:height,
					showMax:true
				});
    		}
    	}
    </script>

</head>
<body style="padding: 2px;">
	<div id="div_main">
		<div position="center" title="流程部署">
			<div id="dep_toolbar"></div>
		    <table id="dep_grid"></table>
		    <div id="dep_page"></div>
		</div>
		
		<div position="centerbottom" title="流程定义">
			<div id="def_toolbar"></div>
		    <table id="def_grid"></table>
		    <div id="def_page"></div>
		</div>
	</div>
</body>

</html>