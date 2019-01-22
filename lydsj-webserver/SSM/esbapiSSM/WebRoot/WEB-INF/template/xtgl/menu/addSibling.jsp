<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>添加子菜单</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript"> 
        
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
         function getFormData(){
        	 return $("#form").serialize();
         }

    </script> 

</head>
<body style="padding:10px"> 
  <form id="form">
  	<input type="hidden" name="parentMenuId" value="${parentMenuId }"/>
  	<input type="hidden" name="rootMenuId" value="${rootMenuId }"/>
  	<div class="table-box" style="border: none;">
  		<form:row>
  			<form:column>
  				<form:left>菜单名称</form:left>
  				<form:right><ef:editForm name="menuName" type="text" required="true" cssName="input"/></form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>菜单链接</form:left>
  				<form:right><ef:editForm name="menuUrl" type="text" cssName="input"/></form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>排序编码</form:left>
  				<form:right><ef:editForm name="menuOrder" type="text" required="true" cssName="input"/></form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>有效标志</form:left>
  				<form:right><ef:editForm name="yxbz" type="radio" options="{\"url\":\"\",\"data\":[{\"text\":\"有效\",\"value\":\"Y\"},{\"text\":\"无效\",\"value\":\"N\"}]}"/></form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>叶子节点</form:left>
  				<form:right><ef:editForm name="isLeaf" type="yesnoradio" /></form:right>
  			</form:column>
  		</form:row>
  	</div>
  </form> 
</html>