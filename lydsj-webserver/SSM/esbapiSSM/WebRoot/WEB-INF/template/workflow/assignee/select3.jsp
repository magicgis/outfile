<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>选择任务处理人</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript">
		var swjgTree,gwTree,swryTree;
		var rootswjg = '${pd.rootswjg}';
		var swjgDms = '${pd.swjgDms}';
		var gwDms = '${pd.gwDms}';
		var swjgurl = '<%=path%>/xtgl/qxgl/swjg';
		swjgurl = swjgurl+'?rootswjg='+rootswjg+'&swjgDms='+swjgDms;
		$(function(){
			$("#swryToolbar").ligerToolBar({
				items : [ {
					icon:'right',
					text : '全选',
					click:function(){
						swryTree.checkAllNodes(true);
					}
				},{
					icon:'candle',
					text : '全不选',
					click:function(){
						swryTree.checkAllNodes(false);
					}
				}]
			});
			$("#swjgToolbar").ligerToolBar({
				items : [ {
					icon:'communication',
					text : '展开',
					click:function(){
						swjgTree.expandAll();
					}
				},{
					icon:'collapse',
					text : '收起',
					click:function(){
						swjgTree.collapseAll();
					}
				}]
			});
			swryTree = PJ.pidTree('swry','',function(node){
				//donothing
			},{checkbox :true});
			gwTree = PJ.pidTree('gw','',function(node){
				swryTree._setUrl('<%=path%>/xtgl/qxgl/gwswry/'+node.data.id+'/swjg/'+node.data.url);
			});
			swjgTree = PJ.pidTree('swjg',swjgurl,function(node){
				if(gwDms){
					gwTree._setUrl('<%=path%>/xtgl/qxgl/gw/'+node.data.id+"?gwDms="+gwDms );
				}else{
					gwTree._setUrl('<%=path%>/xtgl/qxgl/gw/'+node.data.id);
				}
				swryTree.clear();
			},{isExpand:2});
		});
		
		function getFormData(){
			return swryTree.getChecked();
		}
		
		function validate() { 
			var checked = swryTree.getChecked();
			if(checked && checked!=""){
				return true;
			}else{
				return false;
			}
		}
	</script>
  </head>
  
  <body>
    	<div style="width:300px; height:290px; margin:5px; float:left; border:1px solid #ccc; overflow-x:auto;overflow-y:auto;">
    		<div id="swjgToolbar"></div>
    		<ul id="swjg"></ul>
    	</div>
    	<div style="width:210px; height:290px; margin:5px 0 5px 0;  float:left; border:1px solid #ccc; overflow-x:auto;overflow-y:auto;">
    		<ul id="gw"></ul>
    	</div>
    	<div style="width:250px; height:290px; margin:5px; float:left;  border:1px solid #ccc; overflow-x:hidden;overflow-y:auto;">
    		<div id="swryToolbar"></div>
    		<ul id="swry"></ul>
    	</div>
  </body>
</html>
