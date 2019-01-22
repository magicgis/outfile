<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>附件管理</title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript">
		var grid,toolbar;
		$(function(){
			
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/importpackage/fileType',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].value);
							$("#fileType").append($option);
						}
					}else{
						
							PJ.showWarn(result.msg);
					}
				}
			}); 
			
			//初始化布局
			$("#layout1").ligerLayout();
			//工具条
			toolbar =$("#toolbar").ligerToolBar({
				items:[{
					id:'download',
					icon:'down',
					text:'下载附件',
					click : function() {
						var ids = PJ.grid_getMutlSelectedRowkey(grid);
						if(ids && ids!=""){
							PJ.showLoading("处理中...");
							window.location.href='<%=path%>/supplierquote/download/'+ids;
							setTimeout(function(){
								PJ.hideLoading();
							}, 5000);
						}
					}
				},{
					icon:'refresh',
					text:'刷新',
					click:function(){
						PJ.grid_reload(grid);
					}
				}, {
					id:'help',
					icon : 'help',
					text : '帮助',
					click: function(){
						PJ.help("uploadAttachment");
					}
				}]
			});
			
			//控制按扭显示与否
			//PJ.ctrlAttachmentButton(toolbar,${pd});
			
			//表格
			grid = PJ.grid("list1", {
				rowNum: 10,
				url:'<%=path%>/supplierquote/list/data?partNumber='+'${data}',
				width : PJ.getCenterWidth(),
				height : PJ.getCenterHeight()-108,
				rowList:[10,20],
				multiselect:true,
				colNames : [ "附件ID", "附件名称","上传时间" ],
				colModel : [ PJ.grid_column("id", {key:true,hidden:true}),
						PJ.grid_column("fileName", {sortable:true}),
						PJ.grid_column("createTimestamp", {sortable:true})]
			});
			
			//右上角的帮助按扭float:right
			$("#toolbar > div[toolbarid='help']").addClass("fr");
			
			//改变窗口大小自适应
			$(window).resize(function() {
				resize();
			});
		});
		
		function resize(){
			grid.setGridWidth(PJ.getCenterWidth());
			grid.setGridHeight(PJ.getCenterHeight()-108);
		}
	</script>
	<style>
		#cb_list1{
			margin:0
		}
	</style>
  </head>
  
  <body>
   		<div id="layout1">
   			<div position="center">
   				<div id="toolbar"></div>
   				<table id="list1"></table>
				<div id="pager1"></div>
   			</div>
   		</div>
  </body>
</html>
