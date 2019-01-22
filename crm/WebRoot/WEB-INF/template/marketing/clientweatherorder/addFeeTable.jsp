<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>添加费用 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style type="text/css">

table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#FFFFFF;
}
.evenrowcolor{
	background-color:#E0EEEE;
}
</style>

<script type="text/javascript">
	var layout, grid;
	$(function() {
		PJ.datepickerclass('validity');
		
		$("#pdfsubmit").click(function(){
		
			var url = '<%=path%>/supplierquote/uploadPdf';
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'pdffile',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	var obj = eval(data)[0];
	            	if(obj.flag==true){
	            		for(var i in obj){
							var $items = $("#elementTable>tbody").find("tr");
							$items.each(function(){
								var check=false;
								var $inputs = $(this).find("input");
								$inputs.each(function(){
									if($(this).attr("name") == "dataItem"){
										if($(this).prop("checked")){
											check=true;
										}
									}
									if(check){
										if(i!="flag"){
										if($(this).attr("name") == "leadTime"){
											$(this).val(obj[i].leadTime);
										}
										if($(this).attr("name") == "serialNumber"){
											$(this).val(obj[i].serialNumber);
										}
										if($(this).attr("name") == "tagSrc"){
											$(this).val(obj[i].tagSrc);
										}
										if($(this).attr("name") == "tagDate"){
											$(this).val(obj[i].tagDate);
										}
										if($(this).attr("name") == "trace"){
											$(this).val(obj[i].trace);
											check=false;
										}
										
										}
									}
								});
							});
							
						}
	            	}
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            }  
	        });  
		});	
		
		$("#toolbar").ligerToolBar({
			items : [
					   
			        ]
		});
		
	});
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
			var $isnull= $(this).find("input[class='isnull']");
			$inputs.each(function(){
				obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
			});
			index++;
		});
		
		return obj;
	}
	
	function getFormData(){
		var postData = getTableDate();
		return postData;
	}
	function altRows(id){
		if(document.getElementsByTagName){  
			
			var table = document.getElementById(id);  
			var rows = table.getElementsByTagName("tr"); 
			 
			for(i = 0; i < rows.length; i++){          
				if(i % 2 == 0){
					rows[i].className = "evenrowcolor";
				}else{
					rows[i].className = "oddrowcolor";
				}      
			}
		}
	}

	window.onload=function(){
		altRows('elementTable');
	}
</script>

</head>

<body>
	<div>
	<table id="elementTable" border="1px">
		<thead>
			<tr>
				<th style="text-align: center;">供应商</th>
				<th style="text-align: center;">银行费用</th>
				<th style="text-align: center;">提货换单费</th>
				<th style="text-align: center;">杂费</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="message" items="${list}" >
			<tr>
				<td><input style="width: 80px" type="text" name="supplierCode" value="${message.supplierCode}"/><input style="width: 80px" class="hide" name="supplierId" value="${message.supplierId}"/><input style="width: 80px" class="hide" name="clientWeatherOrderId" value="${message.clientWeatherOrderId}"/></td>
				<td><input type="text" name="bankCost" value="${message.bankCost}"/></td>
				<td><input type="text" name="feeForExchangeBill" value="${message.feeForExchangeBill}"/></td>
				<td><input style="width: 80px" type="text" name="otherFee"value="${message.otherFee}"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
	<script id="table-add" type="tetx/template" >
		<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input style="width: 80px" type="text" name="item"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 80px" type="text" name="unit"/></td>
				<td><input style="width: 80px" type="text" name="amount"/></td>
				<td><input style="width: 80px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 80px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
			
				</select>
				</td>
				<td>
				<select name="certificationId">
				
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" name="feeForExchangeBill"/></td>
				<td><input type="text" name="bankCost"/></td>
					<td><input type="text" name="serialNumber" id="serialNumber"/></td>
					<td><input type="text" name="tagSrc" id="tagSrc"/></td>
					<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
					<td><input type="text" name="trace" id="trace"/></td>
					<td><input type="text" name="warranty" id="warranty"/></td>
					<td><input type="text" name="validity" id="" class="validity"/></td>
			</tr>
	</script>
</body>

</html>