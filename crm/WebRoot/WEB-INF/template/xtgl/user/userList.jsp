<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function() {
	
	//获取数据列表
	//数据区的工具栏
	if('${type}'=='all'){
	$("#toolbar").ligerToolBar({
		items:[
				{icon : 'add',text : '新增用户',id:'add', click:addUser},
				//{icon : 'edit',text : '修改用户',id:'edit', click:modifyUser},
				{icon : 'add',text : '供应商与客户管理',id:'add', click:powerManage},
				{icon : 'delete',text : '删除用户',id:'edit', click:deleteUser},
				{icon : 'refresh',text : '刷新',id:'edit', click:function(){PJ.grid_reload(grid);}},
				{icon : 'add',text : '增加供应商',id:'add', click:addSupplier},
				{icon : 'add',text : '层级管理',id:'add', click:relationManage},
				{
				    id:'down',
					icon : 'down',
					text : '下载',
					click: function(){
						    /* var ret = PJ.grid_getSelectedData(grid);
				 		    var id = ret["id"]+"-"+${userId};
				 		    var currencyId = ret["currencyId"]; */
							//根据具体业务提供相关的title
							var title = 'excel管理';
							//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
							//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
							var businessKey = 'part.id.'+1+'.PartExcel.'+1;
							PJ.excelDiaglog({
								data:'businessKey='+businessKey,
								title: title,
								add:true,
								remove:true,
								download:true
							});
					}
			 	}
		       ]
	});}
	
	layout = $("#layout1").ligerLayout();

	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/xtgl/user/userdatalist?type='+'${type}',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-102,
		autoSrcoll:true,
		shrinkToFit:false,
		inLineEdit: true,
		editurl:'<%=path%>/xtgl/user/editUser',
		//caption:"案件列表",
		sortname : "userId",
		colNames : [ "用户编号", "用户姓名","用户登录名","邮件使用名","角色","信息","邮箱","邮箱密码","传真","电话","手机"],
		colModel : [ 	PJ.grid_column("userId", {sortable:true,hidden:true,frozen:true,editable:true}),
						PJ.grid_column("userName", {sortable:true,width:150,editable:true,align:'left'}),
						PJ.grid_column("loginName", {sortable:true,width:150,editable:true,align:'left'}),
						PJ.grid_column("fullName", {sortable:true,width:150,editable:true,align:'left'}),
						PJ.grid_column("roleDisplay", {sortable:true,width:130,align:'left'}),
						PJ.grid_column("information", {sortable:true,width:180,editable:true,align:'left'}),
						PJ.grid_column("email", {sortable:true,width:200,editable:true,align:'left'}),
						PJ.grid_column("emailPassword", {sortable:true,width:130,editable:true,align:'left'}),
						PJ.grid_column("fax", {sortable:true,width:150,editable:true,align:'left'}),
						PJ.grid_column("phone", {sortable:true,width:150,editable:true,align:'left'}),
						PJ.grid_column("mobile", {sortable:true,width:150,editable:true,align:'left'})
						]
				
	});
	grid.jqGrid('setFrozenColumns');
		//改变窗口大小自适应

	
});

function addUser(item){
	var iframeId="addUser";
	PJ.topdialog(iframeId,'增加用户','<%=path%>/xtgl/user/toAddUser',
			function(item, dialog){
		var postData = top.window.frames[iframeId].getFormData();
		var validate=top.window.frames[iframeId].validate();
		if(validate){
 		PJ.ajax({
            url: '<%=path%>/xtgl/user/saveAddUser',
            data: postData,
            loading: "正在保存中...",
            type: "POST",
            success: function(result){
            	if(result.success){
            		PJ.success(result.message);
            	} else {
                    PJ.error(result.message);
                }
            	PJ.grid_reload(grid);
                dialog.close();
            }
        });
		}
	},function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true,"新增");
}

function modifyUser(item){
	var iframeId='modifyUser';
	var ret = PJ.grid_getSelectedData(grid); //获取选中行的数据
	var userId = ret["userId"];
	PJ.topdialog(iframeId,'修改用户','<%=path%>/xtgl/user/toSaveUser?userId='+userId,
			function(item, dialog){
		
 		PJ.ajax({
            url: '<%=path%>/xtgl/user/saveUser',
            data: top.window.frames[iframeId].getFormData(),
            loading: "正在保存中...",
            success: function(result){
            	if(result.success){
            		PJ.success("保存成功！");
            	} else {
                    PJ.error(result.message);
                }
            	PJ.grid_reload(grid);
                dialog.close();
            }
        });
	},function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
}
function deleteUser(item){
	var ret = PJ.grid_getSelectedData(grid); //获取选中行的数据
	var userId = ret["userId"];
	PJ.confirm("确定要删除吗？", function(yes){
		if(yes){
			PJ.ajax({
				url:'<%=path%>/xtgl/user/deleteUser?userId='+userId,
				data:'',
				success:function(result){
					if(result.success){
						PJ.tip(result.message,function(){
							PJ.grid_reload(grid);
						});
					}else{
						PJ.error(result.message);
					}
				}
			});
		}
	});
}

function powerManage(){
	var ret = PJ.grid_getSelectedData(grid);
	var id = ret["userId"];
	var iframeId="powerframe";
	PJ.topdialog(iframeId, ' ', '<%=path%>/xtgl/user/toPower?id='+id,
			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
}

function addSupplier(){
	var ret = PJ.grid_getSelectedData(grid); //获取选中行的数据
	var userId = ret["userId"];
	var iframeId="ideaIframe6";
	PJ.topdialog(iframeId, '新增供应商', '<%=path%>/xtgl/user/toAddSupplier',
			function(item,dialog){
				 var postData=top.window.frames[iframeId].getData();
					 $.ajax({
							url: '<%=path%>/xtgl/user/addSupplier?id='+userId,
							data: postData,
							type: "POST",
							loading: "正在处理...",
							success: function(result){
									if(result.success){
										PJ.success(result.message);
										PJ.grid_reload(grid);
									} else {
										PJ.error(result.message);
									}		
							}
					}); 
				 
			dialog.close();
			PJ.grid_reload(grid);}
		   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
}

function relationManage(item){
	var iframeId='relationManage';
	var ret = PJ.grid_getSelectedData(grid); //获取选中行的数据
	var userId = ret["userId"];
	PJ.topdialog(iframeId,'层级关系','<%=path%>/xtgl/user/toHierarchicalRelation?userId='+userId,
			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true);
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