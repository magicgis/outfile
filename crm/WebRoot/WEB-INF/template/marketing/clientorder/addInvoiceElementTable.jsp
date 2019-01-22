<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>明细批量新增</title>

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
		$("#toolbar").ligerToolBar({
			items : [
					  
			        ]
		});
		
	});
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		var $checkedItems = $("#elementTable>tbody>tr").find("input:checkbox");
		$checkedItems.each(function(i){
	    if($(this).prop("checked")){
				$(this).val("check");
			} 
		});
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
			$inputs.each(function(){
				obj["voList["+index+"]."+$(this).attr("name")] = $(this).val();
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
				<th></th>
				<th style="text-align: center;">件号</th>
				<th style="text-align: center;">单价</th>
				<th style="text-align: center;">开票数量</th>
				<th style="text-align: center;">开票比例(%)</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="message" items="${eleList}" >
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="quotePartNumber" readonly="readonly"value="${message.quotePartNumber}"/></td>
				<td><input style="width: 80px" type="text"readonly="readonly" name="clientOrderPrice"value="${message.clientOrderPrice}"/></td>
				<td><input style="width: 80px" type="text" name="invoiceAmount"   value="<fmt:formatNumber value="${message.invoiceAmount}" pattern="0"/>"/></td>
				<td><input style="width: 80px" type="text" name="elementTerms" value="<fmt:formatNumber value="${message.elementTerms}" pattern="0"/>"/></td>
				<td><input type="text" name="clientOrderElementId"class="hide" value="${message.clientOrderElementId}"/></td>
			</tr>
			</c:forEach>
		 	
		</tbody>
	</table>
	</div>
	
</body>

</html>