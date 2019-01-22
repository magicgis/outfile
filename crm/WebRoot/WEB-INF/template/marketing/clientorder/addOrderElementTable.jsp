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
			var $inputs = $(this).find("input[alia='read'],select[alia='read']");
			var have = true;
			$inputs.each(function(){
				if($(this).attr("name")=="price"&&$(this).val()==""&&have){
					have = false;
					return false;
				}
				else{
					have = true;
					obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
				}
				
			});
			if(have){
				index++;
			}
			
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
				<th style="text-align: center;">ITEM</th>
				<th style="text-align: center;">CSN</th>
				<th style="text-align: center;">件号</th>
				<th style="text-align: center;">单价</th>
				<th style="text-align: center;">数量</th>
				<th style="text-align: center;">周期</th>
				<th style="text-align: center;">备注</th>
				<th style="text-align: center;">佣金</th>
				<th style="display:none"></th>
				<th style="text-align: center;">证书</th>
			
			</tr>
		</thead>
		<tbody>
		<c:forEach var="message" items="${list}" >
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="itemitem" style="width: 40px" readonly="readonly"value="${message.item}"/></td>
				<td><input type="text" name="itemitem" style="width: 40px" readonly="readonly"value="${message.csn}"/></td>
				<td><input type="text" name="quotePartNumber" readonly="readonly"value="${message.quotePartNumber}"/></td>
				<td><input style="width: 80px" type="text" alia="read" name="price"/></td>
				<td><input style="width: 80px" type="text" alia="read" name="amount"/></td>
				<td><input style="width: 80px" type="text" alia="read" name="leadTime" pattern="0"/></td>
				<td><input style="width: 200px" type="text" alia="read" name="remark" value="${message.clientQuoteRemark}"/></td>
				<td><input type="text" name="fixedCost" alia="read"value="${message.fixedCost}"/></td>
				<td style="display:none"><input type="text" name="clientQuoteElementId" class="hide" alia="read" value="${message.id}"/></td>
				<td>
				<select name="certificationId" alia="read">
				<option value=""></option>
				<option value="50">OEM COC-原厂合格证</option>
				<option value="51">FAA 8130-3</option>
				<option value="52">EASA Form One</option>
				<option value="53">Vendor COC</option>
				<option value="55">MFR CERT</option>
				<option value="56">CAAC</option>
				<option value="57">FAA+CAAC</option>
				<option value="58">EASA+CAAC</option>
				<option value="59">TCCA Form one</option>
				<option value="54">Other</option>
				</select>
				</td>
			
			
			</tr>
		</c:forEach>
		 	
		</tbody>
	</table>
	</div>
	
</body>

</html>