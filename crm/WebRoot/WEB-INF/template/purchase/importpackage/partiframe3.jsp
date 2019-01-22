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
         $("#bcTarget2").empty().barcode($("#src").val(), "code128",{barWidth:1, barHeight:35,showHRI:true});
    }
	
</script>
<style type="">

</style>
</head>
	
<body>
 <c:if test="${ empty sn }">
<div style="position:absolute;right:0;  bottom: 0px;  width:50px;border:0px solid red;z-index:100; ">
 <table>
	<tr><td colspan="4" style="text-align: right;">1/1</td></tr>
	</table>
</div>
</c:if>
 <c:if test="${not empty sn }">
<div style="position:absolute;right:0;  bottom: 0px;  width:50px;border:0px solid red;z-index:100; ">
 <table>
	<tr><td colspan="4" style="text-align: right;">1/2</td></tr>
	</table>
</div>
</c:if>
<div class="my_show" id="my_show" style="position:relative;overflow:hidden;border:0px solid red;">
 <table>
	 <tr><td colspan="4" align="left"><div><img style="width: 200px;border:0px solid #000000;padding:0;" src="<%=path%>/resources/images/newlogo.png"/></div></td></tr>
 		<tr><td width="100px"></td><td width="70px"></td><td width="70px"></td><td></td></tr>
   		<tr><td colspan="4" style="padding: 0 0 1px 0"><font size="1.5">ORDER NO:</font><font size="1.5">${data.clientOrderSourceNumber}</font></td></tr>
   		<tr><td colspan="2" style="padding: 0 0 1px 0"><font size="1.5">Ind Ser.No: </font><font size="1.5">${data.csn}</font></td><td colspan="2" style="padding: 0 0 1px 0"><font size="1.5">SL: </font><font size="1.5">${data.location}</font></td></tr>
 		<tr><td colspan="4" style="padding: 0 0 1px 0"><font size="1.5">PN:</font><font size="1.5">${data.quotePartNumber}</font></td></tr>
 		<tr><td colspan="4" style="padding: 0 0 1px 0"><font size="1.5">DESC:</font><font size="1.5">${data.quoteDescription}</font></td> </tr>
 		<tr><td colspan="2" style="padding: 0 0 1px 0"><font size="1.5">CERT:</font><font size="1.5">${data.certificationCode}</font></td><td colspan="2"><font size="1.5">QTY:</font><font size="1.5"><fmt:formatNumber type="number" maxFractionDigits="3" value="${data.quoteAmount}"/> ${data.quoteUnit}</font></td></tr> 
 		<tr><c:if test="${hide  == 'onehide'}"><td colspan="4" style="padding: 0 0 1px 0"><font size="1.5">SN:</font><font size="1.5">${data.serialNumber}</font></td></c:if></tr>
 		<c:if test="${ empty haveResume }">
 		<tr><td colspan="2" style="padding: 0 0 1px 0;letter-spacing: -0.5px"><font size="0">Manufacture Date:</font><font size="0">${data.manufactureDate}</font></td>
 		    <c:if test="${data.expiryDate  != 'hide'}"><td colspan="2" style="padding: 0 0 1px 0;letter-spacing: -0.5px"><font size="0">Expiry Date:</font><font size="0">${data.expiryDate}</font></td></c:if></tr>
 		</c:if>
 		<c:if test="${not empty haveResume }">
 		<tr><td colspan="4" style="padding: 0 0 1px 0"><font size="1.5">Manufacture Date:</font><font size="1.5">${haveResume}</font></td></tr>
 		 </c:if>
 		<tr><td colspan="4" ><div  id="bcTarget" class="barcodeImg"></div></td></tr>
 		
    </table>
    
</div>
 <c:if test="${not empty sn }">

<div style="position:relative;overflow:hidden;border:0px solid red;height: 190px">
<table>
 <tr><td colspan="4" align="left"><div><img style="width: 200px;border:0px solid #000000;padding:0;" src="<%=path%>/resources/images/newlogo.png"/></div></td></tr>
<tr><td width="100px"></td><td width="70px"></td><td width="70px"></td><td></td></tr>
   <tr><td colspan="4" style="padding: 15px 0 5px 0"><font size="2">SN:</font><font size="1.5">${sn}</font></td></tr>
   <tr><td colspan="4" ><div  id="bcTarget2" class="barcodeImg"></div></td></tr>
    </table>
    <div style="position:absolute;right:0;  bottom: 0px;  width:50px;border:0px solid red;z-index:100; ">
 <table>
 <tr><td width="100px"></td><td width="70px"></td><td width="70px"></td><td></td></tr>
	<tr><td colspan="4" style="text-align: right;">2/2</td></tr>
	</table>
</div>
</div>
</c:if>
 
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