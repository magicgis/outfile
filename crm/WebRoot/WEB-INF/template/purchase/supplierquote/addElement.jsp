<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商明细上传 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">

$(function(){
	$("#submit").click(function(){
		var id =$("#id").val();
		var url = '<%=path%>/supplierquote/uploadExcel?id='+${id}+'&editType='+ getFormData().toString();
		//批量新增来函案源
   	 	$.ajaxFileUpload({  
            url: url,
            secureuri:false,
            type: 'POST',
            fileElementId:'file',
            //evel:'JJSON.parse',
            dataType: "text",
            data: '',
            success: function (data, status) {
            	//var message = decodeURI(data);
            	//var a = decodeURI(data);
            	//var da = jQuery.parseJSON(jQuery(data).text());
            	var da = eval(data)[0];
            	//var falg = data.flag;
            	if(da.flag==true){
            		
	            	PJ.success(da.message);
	            	
            	}
            	else{
            		PJ.error(da.message);
    	            
    	           
            	}
   				
            },  
            error: function (data, status, e) { 
            	PJ.error(data.message);
            }  
        });  
	});		
});

//子页面必须提供表单数据方法
function getFormData(){
	 var data = $("#form").serialize();
	 return data;
}
 
</script>
</head>

<body>

<div class="table-box">
	<form id="form" name="form">
	 	<input type="hidden" name="id" id="id" value="${id}"/>
   		<p>询价单号：${clientInquiryQuoteNumber}</p>
   		<p>供应商询价单号：${supplierInquiryQuoteNumber}</p>
			<form:row columnNum="2">
				<form:column width="120">
					<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
					<form:right>
						<p><input type="file" value="" id="file" name="file"/>&nbsp;
						   <input type="button" id="submit" value="上传"/>
						</p>
					</form:right>
				</form:column>
			</form:row>
	            
	 </form>
	</div>

<%-- <form id="fileUpload" action="<%=path%>/sales/clientinquiry/uploadExcel" enctype="multipart/form-data" method="post">
      <input id="excelFile" name="file" type="file"/>
      <input type="button" value="提交" id="submit"/>
  </form> --%>
	  
</body>
</html>