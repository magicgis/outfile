<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function() {
	
	$("#toolbar").ligerToolBar({
		items:[
				{icon : 'edit',text : '权限设置',id:'edit', click:configRole},
				{icon : 'add',text : '添加人员',id:'add', click:addPeople},
				{icon : 'add',text : '添加角色',id:'add', click:addRole},
		       ]
	});
	
	layout = $("#layout1").ligerLayout();

	grid = PJ.grid("list1", {
		rowNum: 20,
		url:'<%=path%>/xtgl/role/rolelistdata',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-142,
		autoSrcoll:true,
		shrinkToFit:false,
		//caption:"案件列表",
		sortname : "roleId",
		colNames : [ "角色编号", "角色名称","备注"],
		colModel : [ PJ.grid_column("roleId", {sortable:true,hidden:true,frozen:true}),
						PJ.grid_column("roleName", {sortable:true,width:170}),
						PJ.grid_column("roleComment", {sortable:true,width:220})
						]
				
	});
	grid.jqGrid('setFrozenColumns');

});
function configRole(item){
	var iframeId='configRole';
	var ret = PJ.grid_getSelectedData(grid); //获取选中行的数据
	var roleId = ret["roleId"];
	PJ.dialog(iframeId,'设置权限','<%=path%>/xtgl/role/toUpdateRoleMenu?roleId='+roleId,
			function(item, dialog){
		
 		PJ.ajax({
            url: '<%=path%>/xtgl/role/updateRoleMenu',
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

function addPeople(){
	var ret = PJ.grid_getSelectedData(grid);
	var id = ret["roleId"];
	var iframeId="powerframe";
	PJ.topdialog(iframeId, ' ', '<%=path%>/xtgl/role/toPeople?id='+id,
			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
}
function addRole(item){
	var iframeId="addUser";
	PJ.topdialog(iframeId,'增加角色','<%=path%>/xtgl/role/toAddRole',
			function(item, dialog){
		var postData = top.window.frames[iframeId].getFormData();
 		PJ.ajax({
            url: '<%=path%>/xtgl/role/addRole',
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
	},function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1000, 500, true,"新增");
}

</script>
</head>
<body>
	<div id="layout1">
		<div position="center" title="角色管理">
			<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>

</body>
</html>