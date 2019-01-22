<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>流程部署</title>
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import> 
	<script type="text/javascript"> 
         //子页面表单验证必须实现的验证方法
		 function validate() { 
		 	var v = $("#form").validate();
        	if(v.form()){
        		return true;
        	}else{
        		v.defaultShowErrors();
        		return false;
        	}
        } 
         
         //上传
         function upload()
         {
        	 var deploymentName = $.trim($('#deploymentName').val());
        	 $.ajaxFileUpload({  
                 url:'<%=path%>/workflow/deploymentUpload?deploymentName=' + deploymentName,  
                 secureuri:false,  
                 fileElementId:'file',
                 dataType: 'text',
                 data: '',
                 success: function (data, status) {
               	 	if(data == '0'){
	               	 	parent.PJ.success("流程部署成功",function(){
		               	 	parent.PJ.hideLoading();
		               	 	parent.window.location.reload(true);
	        			});
					}else{
						parent.PJ.error("流程部署失败",function(){
							parent.PJ.hideLoading();
		               	 	parent.window.location.reload(true);
						});
					}
                 },  
                 error: function (data, status, e) { 
                 }  
             });  
         }
    </script> 
</head>
<body>
	<form name="form" id="form" enctype="multipart/form-data" action="<%=path%>/workflow/deploymentUpload" method="post">
		<div class="table-box">
		<form:row>
			<form:column>
				<form:left>流程名称:</form:left>
				<form:right><ef:editForm name="deploymentName" type="text" required="true" cssName="input"/></form:right>
			</form:column>
		</form:row>
		<form:row>
			<form:column>
				<form:left>流程文件:</form:left>
				<form:right><input type="file" name="file" class="input required" id="file"/></form:right>
			</form:column>
		</form:row>
		</div>
  	</form>
</body>
</html>