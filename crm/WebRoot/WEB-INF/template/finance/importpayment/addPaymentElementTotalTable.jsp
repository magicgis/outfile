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
		
		
		$("#select_all").click(function () {//当点击全选框时 
			var flag = $("#select_all").prop("checked");//判断全选按钮的状态 
			$("[id$='Item']").each(function () {//查找每一个Id以Item结尾的checkbox 
			$(this).prop("checked", flag);//选中或者取消选中 
			});
		});
		
		
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input");
			var shouldPay = 0;
			var payed = 0;
			$inputs.each(function(){
				if($(this).attr("name") == "shouldPay"){
					shouldPay = $(this).val();
				}else if($(this).attr("name") == "payed"){
					payed = $(this).val();
				}else if($(this).attr("name") == "paymentSum"){
					$(this).val((shouldPay-payed).toFixed(2));
				}
			});
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
	
	/* $("#select_all").click(function() {
		var checkboxes = document.getElementsByName('select_item');
        for (var i = 0; i < checkboxes.length; i++) {
            var checkbox = checkboxes[i];
            if (!$(this).get(0).checked) {
                checkbox.checked = false;
            } else {
                checkbox.checked = true;
            }
        }
    }); */

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
				<th><input name="select_all" id="select_all" type="checkbox" /></th>
				<th style="text-align: center;">件号</th>
				<th style="text-align: center;">数量</th>
				<th style="text-align: center;">应付</th>
				<th style="text-align: center;">已付金额</th>
				<th style="text-align: center;">付款金额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="message" items="${list}" >
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" id="dataItem"/></td>
				<td><input type="text" name="partNumber" readonly="readonly" value="${message.partNumber}"/></td>
				<td><input style="width: 80px" type="text" readonly="readonly" name="amount" value="${message.amount}"/></td>
				<td><input style="width: 80px" type="text" readonly="readonly"  name="shouldPay" value="${message.shouldPay}"/></td>
				<td><input style="width: 80px" type="text" readonly="readonly"  name="payed" value="${message.paymentSum}"/></td>
				<td><input style="width: 80px" type="text"  name="paymentSum" id="paymentSum"/></td>
				<td><input type="text" name="id" class="hide" readonly="readonly" value="${message.id}"/></td>
				<td><input type="text" name="supplierOrderElementId" class="hide" readonly="readonly" value="${message.supplierOrderElementId}"/></td>
				<td><input type="text" name="importPackagePaymentId" class="hide" readonly="readonly" value="${message.importPackagePaymentId}"/></td>
			</tr>
			</c:forEach>
		 	
		</tbody>
	</table>
	</div>
	
</body>

</html>