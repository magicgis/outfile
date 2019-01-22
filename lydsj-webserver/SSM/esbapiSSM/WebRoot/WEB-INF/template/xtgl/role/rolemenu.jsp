<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var roleId = ${roleId};
$(function(){
	$("#layout1").ligerLayout();
	$("#toolbar").ligerToolBar({
		items : [{icon : "save",text : "保存",
				click : function(){
						saveForm();
						}
				}] 
	});

	//树
	
	tree = PJ.pidTree('tree1','<%=path%>/xtgl/role/displayRoleMenu?roleId='+roleId,function(node){
	},{isExpand:3,
		checkbox:true,
		onSuccess:function(data){
			$("#tree1").css("width",PJ.getCenterWidth());
		 }
	});
	$("#tree1").css("height",PJ.getCenterHeight()-70);


});
 //子页面表单验证必须实现的验证方法
 function validate() { 
	var v = $("#form").validate({
		errorPlacement : function(error, element) {
			var elem = $(element);
			var err = $(error);
			elem.attr("title", err.html())
		},
		success : function(element) {
			element.removeClass("error");
			element.removeAttr("title");
		}
	});
	if(v.form()){
		return true;
	}else{
		v.defaultShowErrors();
		return false;
	}
} 
 
 //子页面必须提供表单数据方法
	//子页面必须提供表单数据方法
	function getFormData(){
	 	var selectedRoles = tree.getCheckedData();
	 	var menuIdList=[];
	 	for(roleindex in selectedRoles){
	 		menuIdList.push(selectedRoles[roleindex].id);
	 	}
		var postData = {"menuIdList":menuIdList.join(",")};
		var $input = $("#form").find("input");
		
		$input.each(function(i){
			if( !$(this).attr("name") )return;//-- name为空也不取值
			else{
				postData[$(this).attr("name")] = $(this).val();	
			} 
				
		});
		return postData;
	}
	function saveForm()
	{
		PJ.ajax({
			url:'<%=path%>/xtgl/role/updateRoleMenu',
			data: getFormData(),
			loading: "正在保存...",
			success: function(result){
				if(result.success){
            		PJ.success("保存成功！");
            		
            	} else {
					PJ.error(result.message);
				}
			}
		})
	}

</script>
</head>
<body>
<div class="topClass" id="toolbar"></div>
<div class="wenshu-box">
	<br/>
	<div style = "text-align:center">
		<P class="font-first font-34">更改角色${roleName}的权限</P>
	</div>
	<div id="layout1">
		<div position="center" title="菜单管理">
			<ul id="tree1" />
		</div>
	</div>
  <form id="form" name="form">
    <input type="hidden" name="roleId" id="roleId" value="${roleId}"/>
  </form>
	
</body>
  </html>
