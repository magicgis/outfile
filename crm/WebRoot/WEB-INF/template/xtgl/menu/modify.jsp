<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>修改菜单</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript"> 
        $(function (){ 
            //创建表单结构 
            /*
            PJ.loadFormJson($("#form"),${menu})
            */
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
         /*
         function getFormData(){
        	return $("#form").serialize();
         }
         */

     	function getFormData(){
    		var postData = {};
    		var $input = $("#form").find("input,textarea,select");
    		
    		$input.each(function(i){
    			if( !$(this).attr("name") )return;//-- name为空也不取值
    			else{
    				if($(this).attr("name")=='roleList'){
    					var roleStr=$(this).val().join(',');
    					postData['roleList'] = roleStr;
    				}else if($(this).attr("name")=='yxbz'){
    					var yxbz = $("input[name='yxbz']:checked").attr("value");
    					postData[$(this).attr("name")] = yxbz;
    				}else if($(this).attr("name")=='isLeaf'){
    					var isLeaf = $("input[name='isLeaf']:checked").attr("value");
    					postData[$(this).attr("name")] = isLeaf;
    				}
    				else{
    					postData[$(this).attr("name")] = $(this).val();	
    				}
    				
    			} 
    				
    		});
    		return postData;
    	}

    </script> 
</head>
<body style="padding:10px"> 
  <form id="form">
    <input type="hidden" name="menuId" id="menuId" value="${menu.menuId}"/>
    <input type="hidden" name="parentMenuId" id="parentMenuId" value="${menu.parentMenuId}"/>
    <input type="hidden" name="rootMenuId" id="rootMenuId" value="${menu.rootMenuId}"/>
  	<div class="table-box" style="border: none;">
  		<form:row>
  			<form:column>
  				<form:left>菜单名称</form:left>
  				<form:right>
  				<!--<ef:editForm name="menuName" type="text" required="true" cssName="input"/>  -->
  					
  					<input type="text" id="menuName" class="input-first input-100-l" name="menuName" value="${menu.menuName}"/>
  				
  				</form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>菜单链接</form:left>
  				
  				<form:right>
  					<!-- <ef:editForm name="menuUrl" type="text" cssName="input"/> -->
  					<input type="text" id="menuUrl" class="input-first input-100-l" name="menuUrl" value="${menu.menuUrl}"/>
  				</form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>排序编码</form:left>
  				<form:right>
  				<!-- <ef:editForm name="menuOrder" type="text" required="true" cssName="input"/> 
  				 -->
  					<input type="text" id="menuOrder" class="input-first input-100-l" name="menuOrder" value="${menu.menuOrder}"/>
  				</form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>有效标志</form:left>
  				<form:right>
  				<!--
  				<ef:editForm name="yxbz" type="radio" options="{\"url\":\"\",\"data\":[{\"text\":\"有效\",\"value\":\"Y\"},{\"text\":\"无效\",\"value\":\"N\"}]}"/> 
  				 -->
  					<c:if test="${menu.yxbz eq 'Y'}">
  						<input type="radio" name="yxbz" value="Y" checked="checked"/> 是
  						<input type="radio" name="yxbz" value="N"/> 否
  					</c:if>
  					<c:if test="${menu.yxbz eq 'N'}">
  						<input type="radio" name="yxbz" value="Y"/>是
  						<input type="radio" name="yxbz" value="N" checked="checked"/>否
  					</c:if>
  				</form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>叶子节点</form:left>
  				<form:right>
  				<!--<ef:editForm name="isLeaf" type="yesnoradio" />  -->
  					<c:if test="${menu.isLeaf eq 'Y'}">
  						<input type="radio" name="isLeaf" value="Y" checked="checked"/>是
  						<input type="radio" name="isLeaf" value="N"/>否
  					</c:if>
  					<c:if test="${menu.isLeaf eq 'N'}">
  						<input type="radio" name="isLeaf" value="Y"/>是
  						<input type="radio" name="isLeaf" value="N" checked="checked"/>否
  					</c:if>
  					
  					
  				</form:right>
  			</form:column>
  		</form:row>
  		<form:row>
  			<form:column>
  				<form:left>角色</form:left>
  				<form:right>
		            		<select name="roleList" id="roleList" class="select" multiple="multiple">
		            		<c:forEach items="${menu.roleIdList}" var="roleId">
							        <option value="${roleId}" selected>
							        	${allRoleMap[roleId]}
							        </option>
							</c:forEach>
		            		<c:forEach items="${menu.unRoleIdList}" var="roleId">
							        <option value="${roleId}">
							        	${allRoleMap[roleId]}
							        </option>
							</c:forEach>
							</select>		            	
  				</form:right>
  			</form:column>
  		</form:row>
  	</div>
  </form> 
</html>