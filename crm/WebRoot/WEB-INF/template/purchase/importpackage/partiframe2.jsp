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

</style>
</head>
	
<body>
<div class="my_show" id="my_show" >
 <table>
 		<tr><td  width="10px" style="width: 10px;border:0px solid #000000;padding: 0px 0 0px 0"></td>
 		<td  width="10px" style="width: 10px;border:0px solid #000000;padding: 0px 0 0px 0"></td>
 		<td  width="50px" style="width: 50px;border:0px solid #000000;padding: 0px 0 0px 0"></td>
 		<td  width="10px" style="width: 10px;border:0px solid #000000;padding: 0px 0 0px 0"></td>
 		<td  width="10px" style="width: 10px;border:0px solid #000000;padding: 0px 0 0px 0"></td>
 		<td  width="10px" style="width: 10px;border:0px solid #000000;padding: 0px 0 0px 0"></td>
 		</tr>
 		<tr><td colspan="4" style="width: 150px;border:1px solid #000000;padding: 3px 0 0px 0"><div  id="bcTarget" class="barcodeImg"></div></td>
 		    <td colspan="2" style="width: 120px;border:1px solid #000000;padding: 0 5px 0px 10px"><div><img style="width: 100px;border:0px solid #000000;padding:1;" src="<%=path%>/resources/images/betterair.png"/></div></td></tr>
   		<tr><td colspan="6" style="width: 100px;border:1px solid #000000;padding: 0 0 5px 0"><font size="2">ORDER NO:</font><font size="1.5">${data.clientOrderSourceNumber}</font></td></tr>
   		<tr><td colspan="3" style="width: 100px;border:1px solid #000000;padding: 0 0 5px 0"><font size="2">Ind Ser.No: </font><font size="1.5">${data.csn}</font></td>
   		    <td colspan="3" style="width: 100px;border:1px solid #000000;padding: 0 0 5px 0"><font size="2">QTY:</font><font size="1.5"><fmt:formatNumber type="number" maxFractionDigits="3" value="${data.quoteAmount}"/> ${data.quoteUnit}</font></td></tr>
 		<tr><td colspan="6" style="width: 100px;border:1px solid #000000;padding: 0 0 5px 0"><font size="2">PN:</font><font size="1.5">${data.quotePartNumber}</font></td></tr>
 		<tr><td colspan="6" style="width: 100px;border:1px solid #000000;padding: 0 0 5px 0"><font size="2">DESC:</font><font size="1.5">${data.quoteDescription}</font></td> </tr>
 		<tr><td colspan="3" style="width: 100px;border:1px solid #000000;padding: 0 0 5px 0"><font size="1.5">Manufacture/Inhibiting Date:</font><font size="1.5">${data.manufactureDate}</font></td>
 		    <td colspan="3" style="width: 100px;border:1px solid #000000;padding: 0 0 20px 0"><font size="1.5">Expiry Date:</font><font size="1.5">${data.expiryDate}</font><br/></td></tr>
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