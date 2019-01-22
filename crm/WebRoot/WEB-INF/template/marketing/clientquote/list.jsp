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
			
			//初始化布局
			$("#layout1").ligerLayout();
			//工具条
			var type='${type}';
			var on=type.indexOf("marketing");
			if(on>-1){
			toolbar =$("#toolbar").ligerToolBar({
				items:[{
					id:'download',
					icon:'down',
					text:'下载附件',
					click : function() {
						var ids = PJ.grid_getMutlSelectedRowkey(grid);
						if(ids && ids!=""){
							PJ.showLoading("处理中...");
							window.location.href='<%=path%>/fj/download/'+ids;
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
			}else{
				
				toolbar =$("#toolbar").ligerToolBar({
					items:[{
						id:'add',
						icon:'add',
						text:'上传附件',
						click : function() {
							PJ.uploadAttachment(null,${datajson},function(result){
								if(result && result.id){
									PJ.success("上传成功", function(){
										if(parent.afterUpload){
											parent.afterUpload(result);
										}
										PJ.grid_reload(grid);
									})
								}
							}, {});
						}
					},{
						id:'remove',
						icon:'delete',
						text:'删除附件',
						click : function() {
							var ids = PJ.grid_getMutlSelectedRowkey(grid);
							if(ids && ids!=""){
								PJ.confirm("确定删除所选附件?", function(yes){
									if(yes){
										PJ.ajax({
											url:'<%=path%>/fj/remove',
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
						text:'下载附件',
						click : function() {
							var ids = PJ.grid_getMutlSelectedRowkey(grid);
							if(ids && ids!=""){
								PJ.showLoading("处理中...");
								window.location.href='<%=path%>/fj/download/'+ids;
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
				
			}
			//控制按扭显示与否
			PJ.ctrlAttachmentButton(toolbar,${pd});
			
			
				//表格
				grid = PJ.grid("list1", {
					rowNum: 20,
					url:'<%=path%>/clientquote/data?type='+'${type}'+'&businessKey='+'${data}',
					width : PJ.getCenterWidth(),
					height : PJ.getCenterHeight()-108,
					rowList:[10,20,50],
					multiselect:true,
					sortname : "lrsj",
					sortorder:"desc",
					colNames : [ "附件ID", "附件名称","供应商", "附件类型", "附件大小","上传时间" ],
					colModel : [ PJ.grid_column("fjId", {key:true,hidden:true}),
							PJ.grid_column("fjName", {sortable:true,
								  formatter: function(value){
										if (value) {
											var strs= new Array();
											strs=value.split("FjBy",2);
											return strs[0];
											
										} else {
											return "";
										}
									}}),
							PJ.grid_column("fjName", {sortable:true,
								  formatter: function(value){
										if (value) {
											var strs= new Array();
											strs=value.split("FjBy",2);
											return strs[1];
										} else {
											return "";
										}
									}}),
							PJ.grid_column("fjType", {sortable:true}),
							PJ.grid_column("fjLength", {sortable:true,formatter:'money'}),
							PJ.grid_column("lrsj", {sortable:true})]
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
   		
   		<!-- 附件上传 -->
   		<div class="table-box" style="display: none;" id="uploadDiv">
   			<form:row>
    			<form:column>
    				<form:left>附件名称：</form:left>
    				<form:right><input type="text" name="fjName" id="fjName" class="input"/></form:right>
    			</form:column>
    		</form:row>
    		<form:row>
    			<form:column>
    				<form:left>附件文件：</form:left>
    				<form:right><input type="file" name="file" id="uploadFile"/></form:right>
    			</form:column>
    		</form:row>
    	</form>
   		</div>
  </body>
</html>
