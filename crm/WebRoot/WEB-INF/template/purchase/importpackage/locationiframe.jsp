<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title></title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
 
<script type="text/javascript">
	$(function(){
	code128();
	 });
	
	 function code128(){
         $("#bcTarget").empty().barcode($("#src").val(), "code128",{barWidth:1, barHeight:35,showHRI:true});
    }
	
</script>
<style type="">
.barcodeImg{margin:20px 0px 0px 40px}
</style>
</head>
	
<body>
<div class="my_show" id="my_show" >
 <table>
	 <tr><td colspan="4" align="left"><div><img style="width: 100px;border:0px solid #000000;padding:0;" src="<%=path%>/resources/images/newbetterair.png"/></div></td></tr>
 		<tr><td width="100px"></td><td width="60px"></td><td width="60px"></td><td></td></tr>
 		<c:if test="${not empty sourceNumber }">
   		<tr><td colspan="4" style="padding: 0 0 1px 0"><font size="1.5">ORDER NO:</font><font size="1.5">${sourceNumber}</font></td></tr>
   		</c:if>
   		<tr><td colspan="2" ><font size="2">Cust ID:</font><font size="2">${clientCode}</font></td><td colspan="2"><font size="2">BOX ID:</font><font size="2">${location}</font></td></tr>
 		<tr><td colspan="4" align="center" ><div id="bcTarget" class="barcodeImg" ></div></td></tr>
    </table>
   
</div>
 
<div class="my_hidden">
<input id="src" value="${barCode}" class="hide"></input>
<!-- <input type="button" onclick='code11()' value="code11"/>     
    	  <input type="button" onclick='code39()' value="code39"/> 
    	  <input type="button" onclick='code93()' value="code93"/>
    	  <input type="button" onclick='code128()' value="生成条形码"/><br />
    	   <input type="button" onclick='ean8()' value="ean8"/>   
    	  <input type="button" onclick='ean13()' value="ean13"/>
    	  <input type="button" onclick='ean13()' value="std25"/>
    	  <input type="button" onclick='int25()' value="int25"/>
    	   <input type="button" onclick='msi()' value="msi"/>
    	   <input type="button" id="print"  value="打印"></input>
    	   <p><a id="btnPrint" href='iframes'>Print!</a></p> -->
</div>


</body>
</html>