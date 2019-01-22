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

    $(function (){
		$("#testBtn").click(function(){
			PJ.ajax({
				url:'<%=path%>/task/testTask/startTask',
				data:'id=123&taskId=1',
				success:function(result){
					if(result.success){
						PJ.tip(result.message);
					}else{
						PJ.error(result.message);
					}
				}
			});
		});
    });
    </script> 
</head>
<body>
	<a href="javascript:;" id="testBtn" >发起流程</a>
</body>
</html>