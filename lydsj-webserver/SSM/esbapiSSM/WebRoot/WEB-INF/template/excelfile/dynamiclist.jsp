<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>${title}</title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript">
		var grid,toolbar;
		$(function(){
			
			//初始化布局
			$("#layout1").ligerLayout();
			//工具条
			toolbar =$("#toolbar").ligerToolBar({
				items:[{
					id:'add',
					icon:'add',
					text:'生成excel',
					click : function() {
						PJ.ajax({
							url:'<%=path%>/excelfile/generate?${data}',
							loading:'生成文件中...',
							success:function(result, status){								
								PJ.grid_reload(grid);
							}
						});
												
					}
				},{
					id:'remove',
					icon:'delete',
					text:'删除Excel文件',
					click : function() {
						var ids = PJ.grid_getMutlSelectedRowkey(grid);
						if(ids && ids!=""){
							PJ.confirm("确定删除所选Excel?", function(yes){
								if(yes){
									PJ.ajax({
										url:'<%=path%>/excelfile/remove',
										data:'ids='+ids,
										success : function(result){
											if (result.success) {
					                            PJ.success(result.message, function() {
					                            	if(parent.afterRemove){
														parent.afterRemove(ids);
													}
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
					}
				},{
					id:'download',
					icon:'down',
					text:'下载Excel',
					click : function() {
						var ids = PJ.grid_getMutlSelectedRowkey(grid);
						if(ids && ids!=""){
							PJ.showLoading("处理中...");
							window.location.href='<%=path%>/excelfile/download/'+ids;
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
			PJ.ctrlAttachmentButton(toolbar,${pd});
			PJ.ajax({
	            url: '<%=path%>/excelfile/list/dynamicColNames?${data}',
	            dataType:'json',
	            loading: "正在加载...",
	            success: function(result) {
	            	var colNames = [ "Excel文件ID", "文件名称", "文件类型", "文件大小","生成时间","生成者" ];
	            	var colModel = [ PJ.grid_column("excelFileId", {key:true,hidden:true}),
	    	    						PJ.grid_column("excelFileName", {sortable:true}),
	        						PJ.grid_column("excelType", {sortable:true}),
	        						PJ.grid_column("excelFileLength", {sortable:true,formatter:'money'}),
	        						PJ.grid_column("lrsj", {sortable:true}),
	        						PJ.grid_column("userId", {sortable:true})];
					for(var index in result.columnDisplayNames){
						colNames.push(result.columnDisplayNames[index]);
						colModel.push(PJ.grid_column(result.columnKeyNames[index], {sortable:false}));
					}	    			
	    			//表格
	    			grid = PJ.grid("list1", {
	    				rowNum: 3,
	    				url:'<%=path%>/excelfile/list/data?${data}',
	    				width : PJ.getCenterWidth(),
	    				height : PJ.getCenterHeight()-108,
	    				rowList:[10,20],
	    				multiselect:true,
	    				sortname : "lrsj",
	    				sortorder:"desc",
	    				colNames : colNames,
	    				colModel : colModel

	    			});
	            	
	            }
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
