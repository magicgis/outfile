<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
 %>
<html>
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	window.location.href='<%=path%>/fj/viewPdf?ids='+'${ids}'
</script>
<head>
</head>
<body>
</body>
</html>
