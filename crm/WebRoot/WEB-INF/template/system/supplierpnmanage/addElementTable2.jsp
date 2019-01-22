<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商件号管理 </title>

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
					   /*{  icon : "add",
						  text : "增加行",
						  click : function(){
								  	var $tbody = $("#elementTable>tbody");//-- 容器
									var $item = $($("script[type='tetx/template']").html()).clone();//-- 复制模版
						
									$tbody.append($item);
								}
						},
						{ icon : "delete",
						  text : "删除行",
						  click : function(){
									var $checkedItems = $("#elementTable>tbody>tr").find("input:checkbox");
									$checkedItems.each(function(i){
								    if($(this).prop("checked")){
											$(this).parent().parent().remove();
										} 
									});
											
							}
						} ,
						{ icon : "add",
							  text : "数据带出",
							  click : function(){
								  
								}
							}  */
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
	<div id="toolbar"></div>
	<div>
	<table id="elementTable" border="1px">
		<thead>
			<tr>
				<th></th>
				<th style="text-align: center;">件号</th>
				<th style="text-align: center;">描述</th>
				<th style="text-align: center;">cage code</th>
				<th style="text-align: center;">生厂商</th>
				<th style="text-align: center;">是否匹配</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="message" items="${list}" >
		<tr>
		<c:if test="${not empty message.supplierId}">
		<td colspan="6">件号：${message.partNum}  描述：${message.partName}</td>
			</c:if>
		</tr>
			<tr>
			<c:if test="${empty message.supplierId}">
				<td><input type="checkbox" class="form-checkbox" name="dataItem"/></td>
				<td style="display: none;"><input readonly="readonly" type="text" name="bsn" value="${message.bsn}"/></td>
				<td><input readonly="readonly" type="text" name="partNumber"value="${message.partNum}"/></td>
				<td><input readonly="readonly" type="text" style="width: 490px" name="description"value="${message.partName}"/></td>
				<td><input readonly="readonly" type="text" style="width: 80px" name="cageCode"value="${message.cageCode}"/></td>
				<td><input readonly="readonly" type="text"  name="manName"value="${message.manName}"/></td>
				<td><input readonly="readonly" type="text" style="width: 80px" name="match"value="${message.match}"/></td>
			</c:if>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	<script id="table-add" type="tetx/template" >
		<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"value="${message.partNum}"/></td>
				<td><input type="text" style="width: 720px" name="description"value="${message.partName}"/></td>
				<td><input readonly="readonly" type="text" style="width: 80px" name="match"value="${message.match}"/></td>
			</tr>
	</script>
</body>

</html>