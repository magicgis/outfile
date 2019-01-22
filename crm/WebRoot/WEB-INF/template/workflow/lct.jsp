<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程图</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<style type="text/css">
.current {
	border: 1px solid red;
	background: red;
}

.history {
	border: 1px solid green;
	background: green;
}
.current,.history {
	-moz-border-radius: 15px;
	-webkit-border-radius: 15px;
	border-radius: 15px;
	position: absolute;
	filter: alpha(opacity = 40);
	-moz-opacity: 0.4;
	-khtml-opacity: 0.4;
	opacity: 0.4;
}
</style>
<script type="text/javascript">
	function resizeFrame(width,height){
	   try{
		   frameElement.width = width;
		    frameElement.height= height;
	   }catch (e) {}
	}
</script>
</head>
<body>
	<img id="imageFlow" alt="" src="<%=path %>${pic }" style="position: absolute; left: 0px; top: 0px;">
	<c:if test="${coordinates!=null }">
		<div class="current" style="top:${coordinates.y+6 }px;left: ${coordinates.x+6 }px;width: ${coordinates.width-13 }px;height:${coordinates.height-13 }px;"></div>
	</c:if>
	<c:if test="${hisCoordinates!=null }">
		<c:forEach items="${hisCoordinates}" var="hc">
			<div class="history" style="top:${hc.y+6 }px;left: ${hc.x+6 }px;width: ${hc.width-13 }px;height:${hc.height-13 }px;"></div>
		</c:forEach>
	</c:if>
	<script type="text/javascript">
		resizeFrame('${width }','${height}');
	</script>
</body>
</html>