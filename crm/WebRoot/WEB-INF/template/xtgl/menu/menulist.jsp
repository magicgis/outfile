<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>菜单管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var tree;
	$(function() {
		$("#layout1").ligerLayout();

		$("#toptoolbar").ligerToolBar({
			items : [ {
				text : '增加同级菜单',
				click : function(item) {
					var node = tree.getSelected();
					if(node){
						var pid = node.data.pid;
						var text = node.data.text;
						var iframeId = 'addSibling_'+pid;
						PJ.dialog(iframeId,'增加['+text+']的同级菜单','<%=path%>/xtgl/menu/addSibling/'+pid,
									function(item,dialog){
										//调用子页面的校验方法
										var flag = window.frames[iframeId].validate();
										if(flag){
											dialog.hide();//先不关闭,否则子窗口会被销毁
											PJ.ajax({
												url:'<%=path%>/xtgl/menu/addMenu',
												//调用子页面的数据获取方法
												data:window.frames[iframeId].getFormData(),
												loading:"正在保存...",
												success:function(result){
													if(result.success){
														PJ.success(result.message,function(){tree.reload();});
													}else{
														PJ.error(result.message);
													}
													dialog.close();
												}
											});
										}
									},function(item,dialog){
										dialog.close();
									},500,320);
					}else{
						PJ.warn("请选择一个菜单节点");
					}
				},
				icon:'add'
			}, {
				line : true
			},{
				text : '增加子菜单',
				click : function(item) {
					var node = tree.getSelected();
					if(node){
						var id = node.data.id;
						var text = node.data.text;
						var iframeId = 'addSub_'+id;
						PJ.dialog(iframeId,'增加['+text+']的子菜单','<%=path%>/xtgl/menu/addSub/'+id,
								function(item,dialog){
									//调用子页面的校验方法
									var flag = window.frames[iframeId].validate();
									if(flag){
										dialog.hide();//先不关闭,否则子窗口会被销毁
										PJ.ajax({
											url:'<%=path%>/xtgl/menu/addMenu',
											//调用子页面的数据获取方法
											data:window.frames[iframeId].getFormData(),
											loading:"正在保存...",
											success:function(result){
												if(result.success){
													PJ.success(result.message,function(){tree.reload();});
												}else{
													PJ.error(result.message);
												}
												dialog.close();
											}
										});
									}
								},function(item,dialog){
									dialog.close();
								},500,320);
					}else{
						PJ.warn("请选择一个菜单节点");
					}
				},
				icon : 'add'
			}, {
				line : true
			}, {
				text : '修改选中菜单',
				click : function(item) {
					var node = tree.getSelected();
					if(node){
						var id = node.data.id;
						var text = node.data.text;
						var iframeId = 'modify_'+id;
						PJ.dialog(iframeId,'修改['+text+']','<%=path%>/xtgl/menu/modify/'+id,
								function(item,dialog){
									//调用子页面的校验方法
									var flag = window.frames[iframeId].validate();
									if(flag){
										dialog.hide();//先不关闭,否则子窗口会被销毁
										PJ.ajax({
											url:'<%=path%>/xtgl/menu/modifyMenu',
											//调用子页面的数据获取方法
											data:window.frames[iframeId].getFormData(),
											loading:"正在提交修改...",
											success:function(result){
												if(result.success){
													PJ.tip(result.message,function(){tree.reload();});
												}else{
													PJ.error(result.message);
												}
												dialog.close();
											}
										});
									}
								},function(item,dialog){
									dialog.close();
								},500,320);
					}else{
						PJ.warn("请选择一个菜单节点");
					}
				},
				icon: 'modify'
			}, {
				line : true
			}, {
				text : '删除选中菜单',
				click : function(item) {
					var node = tree.getSelected();
					if(node){
						var id = node.data.id;
						var text = node.data.text;
						PJ.confirm("确定要删除["+text+"]吗？",function(yes){
							if(yes){
								var children = node.data.children;
								if(children){
									PJ.confirm("["+text+"]含有子菜单,删除操作将同时删除子菜单,是否确定删除？", function(yes){
										if(yes){
											PJ.ajax({
												url:'<%=path%>/xtgl/menu/deleteMenu',
												data:'id='+id,
												success:function(result){
													if(result.success){
														PJ.tip(result.message,function(){tree.reload();});
													}else{
														PJ.error(result.message);
													}
												}
											});
										}
									});
								}else{
									PJ.ajax({
										url:'<%=path%>/xtgl/menu/deleteMenu',
										data:'id='+id,
										success:function(result){
											if(result.success){
												PJ.tip(result.message,function(){tree.reload();});
											}else{
												PJ.error(result.message);
											}
										}
									});
								}
							}
						})
					}else{
						PJ.warn("请选择一个菜单节点");
					}
				},
				icon:'delete'
			}, {
				line : true
			}, {
				text : '全部收起',
				click :  function(){
					tree.collapseAll();
				},
				icon:'communication'
			}, {
				text : '全部展开',
				click :  function(){
					tree.expandAll();
				},
				icon:'communication'
			}, {
				line : true
			}, {
				text : '重新加载',
				click :  function(){
					tree.reload();
				},
				icon:'refresh'
			}]
		});
		
		//树
		tree = PJ.pidTree('tree1','<%=path%>/xtgl/menu/fullmenu',function(node){
		},{isExpand:3,
			onSuccess:function(data){
				$("#tree1").css("width",PJ.getCenterWidth());
			 }
		});
		$("#tree1").css("height",PJ.getCenterHeight()-70);
	});
</script>
</head>
<body style="padding:5px">
	<div id="layout1">
		<div position="center" title="菜单管理">
			<div id="toptoolbar"></div>
			<ul id="tree1" style="margin-top:3px; float: left; overflow: auto;">
		</div>
	</div>
</body>
</html>
