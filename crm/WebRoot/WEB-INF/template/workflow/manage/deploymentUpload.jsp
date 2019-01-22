<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>上传结果</title>
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import> 
	<script type="text/javascript">
		var flag = ${flag};
		if(flag){
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
	</script>
  </head>
  
</html>
