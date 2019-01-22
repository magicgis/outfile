<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>上传附件</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
  </head>
  <body style="margin: 20px;">
  <script type="text/javascript">
  var tableName = '${tableName}';
    	PJ.attachmentFrame({
    		data:'businessKey='+tableName+'.id.'+'${id}',
    		 url: '<%=path%>/importpackage/list?type='+'${type}',
    		add:true,
			remove:true,
			download:true,
			width:950
    	});
    </script>
   		<!-- 附件上传 -->
   		<div class="table-box" style="display: none;width:700px" id="uploadDiv" >
   			<form:row>
    			<form:column>
    				<form:left>附件名称：</form:left>
    				<form:right><input type="text" name="fjName" id="fjName" class="input"/></form:right>
    			</form:column>
    		</form:row>
    		<form:row>
    			<form:column>
    				<form:left>附件文件：</form:left>
    				<form:right><input type="file" name="file" id="uploadFile"/></form:right>
    			</form:column>
    		</form:row>
   		</div>
  
  </body>
</html>