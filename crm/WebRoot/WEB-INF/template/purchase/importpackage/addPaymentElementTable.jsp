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
		
		$("input").blur(function () { 
			
			var $items = $("#elementTable>tbody").find("tr");
			var total = 0;
			$items.each(function(){
				var $inputs = $(this).find("input");
				var paymentPercentage = 0;
				var AllowPaymentPercentage = 0;
				var amount = 0;
				var price = 0;
				var orderAmount = 0;
				var addFlag = false;
				var paymentTotal = 0;
				$inputs.each(function(){
					if($(this).attr("name") == "paymentPrice"){
						paymentTotal = (paymentPercentage * amount * price / 100).toFixed(2);
						$(this).val(paymentTotal);
					}else if($(this).attr("name") == "paymentPercentage"){
						paymentPercentage = $(this).val();
						if(parseFloat(paymentPercentage) > parseFloat(AllowPaymentPercentage)){
							alert("不可大于可开比例！");
						}
					}else if($(this).attr("name") == "AllowPaymentPercentage"){
						AllowPaymentPercentage = $(this).val();
					}else if($(this).attr("name") == "amount"){
						amount = $(this).val();
						if(parseFloat(amount) > parseFloat(orderAmount)){
							alert("不可大于可开数量！");
						}
					}else if($(this).attr("name") == "price"){
						price = $(this).val();
					}else if($(this).attr("name") == "orderAmount"){
						orderAmount = $(this).val();
					}else if($(this).attr("name") == "dataItem"){
						if($(this).prop("checked")){
							addFlag = true;
						}
					}else if(addFlag){
						total = parseFloat(paymentTotal) + parseFloat(total);
					}
				});
			});
			$("#total").val(total);
		});
		
		$("input[type='checkbox']").click(function () {
			var $items = $("#elementTable>tbody").find("tr");
			var total = 0;
			$items.each(function(){
				var $inputs = $(this).find("input");
				var paymentPercentage = 0;
				var AllowPaymentPercentage = 0;
				var amount = 0;
				var price = 0;
				var orderAmount = 0;
				var addFlag = false;
				var paymentTotal = 0;
				$inputs.each(function(){
					if($(this).attr("name") == "paymentPrice"){
						paymentTotal = paymentPercentage * amount * price / 100;
						$(this).val(paymentTotal);
					}else if($(this).attr("name") == "paymentPercentage"){
						paymentPercentage = $(this).val();
						if(parseFloat(paymentPercentage) > parseFloat(AllowPaymentPercentage)){
							alert("不可大于可开比例！");
						}
					}else if($(this).attr("name") == "AllowPaymentPercentage"){
						AllowPaymentPercentage = $(this).val();
					}else if($(this).attr("name") == "amount"){
						amount = $(this).val();
						if(parseFloat(amount) > parseFloat(orderAmount)){
							alert("不可大于可开数量！");
						}
					}else if($(this).attr("name") == "price"){
						price = $(this).val();
					}else if($(this).attr("name") == "orderAmount"){
						orderAmount = $(this).val();
					}else if($(this).attr("name") == "dataItem"){
						if($(this).prop("checked")){
							addFlag = true;
						}
					}else if(addFlag){
						total = parseFloat(paymentTotal) + parseFloat(total);
					}
				});
			});
			$("#total").val(total);
		});
		
		var $items = $("#elementTable>tbody").find("tr");
		var total = 0;
		$items.each(function(){
			var $inputs = $(this).find("input");
			var paymentPercentage = 0;
			var AllowPaymentPercentage = 0;
			var amount = 0;
			var price = 0;
			var orderAmount = 0;
			var addFlag = false;
			var paymentTotal = 0;
			$inputs.each(function(){
				if($(this).attr("name") == "paymentPrice"){
					paymentTotal = paymentPercentage * amount * price / 100;
					$(this).val(paymentTotal);
				}else if($(this).attr("name") == "paymentPercentage"){
					paymentPercentage = $(this).val();
				}else if($(this).attr("name") == "AllowPaymentPercentage"){
					AllowPaymentPercentage = $(this).val();
				}else if($(this).attr("name") == "amount"){
					amount = $(this).val();
				}else if($(this).attr("name") == "price"){
					price = $(this).val();
				}else if($(this).attr("name") == "orderAmount"){
					orderAmount = $(this).val();
				}else if($(this).attr("name") == "dataItem"){
					if($(this).prop("checked")){
						addFlag = true;
					}
				}else if(addFlag){
					total = parseFloat(paymentTotal) + parseFloat(total);
				}
			});
		});
		$("#total").val(total);
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
				<th style="text-align: center;">订单号</th>
				<th style="text-align: center;">单价</th>
				<th style="text-align: center;">可开数量</th>
				<th style="text-align: center;">可开比例</th>
				<th style="text-align: center;">总价</th>
				<th style="text-align: center;">比例</th>
				<th style="text-align: center;">数量</th>
				<th style="text-align: center;">付款金额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="message" items="${list}" >
			<tr>
				<td><input type="checkbox" class="form-checkbox" id="checkboxItem" name="dataItem" /></td>
				<td><input style="width: 120px" type="text" name="quotePartNumber" readonly="readonly" value="${message.partNumber}"/></td>
				<td><input style="width: 120px" type="text"readonly="readonly" name="orderNumber"value="${message.orderNumber}"/></td>
				<td><input style="width: 80px" type="text" readonly="readonly" name="price" id="price" value="${message.price}"/></td>
				<td><input style="width: 80px" type="text" readonly="readonly" name="orderAmount" value="${message.amount}"/></td>
				<td><input style="width: 80px" type="text" name="AllowPaymentPercentage" readonly="readonly" id="AllowPaymentPercentage" value="${message.paymentPercentage}"/></td>
				<td><input style="width: 80px" type="text" readonly="readonly" name="totalPrice" value="${message.totalPrice}"/></td>
				<td><input style="width: 80px" type="text" name="paymentPercentage" id="paymentPercentage" value="${message.paymentPercentage}"/></td>
				<td><input style="width: 80px" type="text" name="amount" id="amount" value="${message.amount}"/></td>
				<td><input style="width: 80px" type="text" name="paymentPrice" id="paymentPrice"/></td>
				<td><input style="width: 80px" type="text" class="hide" name="supplierOrderElementId" value="${message.supplierOrderElementId}"/></td>
			</tr>
		</c:forEach>
		 	<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td>合计:</td>
				<td><input style="width: 80px" type="text" readonly="readonly" name="total" id="total"/></td>
			</tr>
		</tbody>
	</table>
	</div>
	
</body>

</html>