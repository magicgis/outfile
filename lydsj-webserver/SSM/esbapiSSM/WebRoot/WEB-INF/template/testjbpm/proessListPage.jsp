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
	var ask4levaeGrid;
    $(function (){
    	
    	$("#div_main").ligerLayout();
    	
    	
		grid = PJ.grid("grid", {
			url: '<%=path%>/testJbpm/getNoteList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 142,
			pager: '#Page',
			colNames : ["ID", "信息", "备注"],
			colModel : [PJ.grid_column("id", {key:true}),
					PJ.grid_column("message", {}),
					PJ.grid_column("remark", {})]
		});
		
		
    });
    </script> 
</head>
<body>
	<div id="div_main">
		<div position="center" title="列表">
			<div id="toolbar"></div>
		    <table id="grid"></table>
		    <div id="Page"></div>
		</div>
	</div>
</body>
</html>