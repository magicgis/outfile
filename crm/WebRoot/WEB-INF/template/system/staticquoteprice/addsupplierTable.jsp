<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title> </title>

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
	var check = 0;
	$(function() {
		$("#toolbar").ligerToolBar({
			items : [
					   {  icon : "add",
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
						}
			        ]
		});
		
	});
	
	//货币信息
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/system/staticprice/currencyTypeForSupplier',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $items = $("#elementTable>tbody").find("tr");
					$items.each(function(){
						var $inputs = $(this).find("select");
						$inputs.each(function(){
							if($(this).attr("name") == "currencyId"){
								var $option = $("<option></option>");
								$option.val(obj[i].currency_id).text(obj[i].currency_value);
								$(this).append($option);
							}
						});
					});
					
				}
			}else{
				
					PJ.warn("操作失误!");
			}
		}
	});
	
	//状态信息
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/system/staticprice/findConditionForSupplier',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $items = $("#elementTable>tbody").find("tr");
					$items.each(function(){
						var $inputs = $(this).find("select");
						$inputs.each(function(){
							if($(this).attr("name") == "conditionId"){
								var $option = $("<option></option>");
								$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
								$(this).append($option);
							}
						});
					});
					
				}
			}else{
				
					PJ.warn("操作失误!");
			}
		}
	});
	
	
	//证书信息
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/system/staticprice/findCertificationForSupplier',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $items = $("#elementTable>tbody").find("tr");
					$items.each(function(){
						var $inputs = $(this).find("select");
						$inputs.each(function(){
							if($(this).attr("name") == "certificationId"){
								var $option = $("<option></option>");
								$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
								$(this).append($option);
							}
						});
					});
					
				}
			}else{
				
					PJ.warn("操作失误!");
			}
		}
	});
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input[type='text'],select[type='text']");
			$inputs.each(function(){
				if($(this).val()!=""){
					obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
				}
				
			});
			index++;
		});
		
		return obj;
	}
	
	function getFormData(){
		var postData = getTableDate();
		postData.check = check
		postData.token='${token}';
		check = 1;
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
	<input type="text" name="token" id="token" value="${token}" class="hide"/>
		<thead>
			<tr>
				<th></th>
				<th>供应商</th>
				<th>件号</th>
				<th>价格</th>
				<th>币种</th>
				<th>年份</th>
				<th>状态</th>
				<th>证书</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="code"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="price"/></td>
				<td><select type="text" name="currencyId">
					</select>
				</td>
				<td><input type="text" name="year"/></td>
				<td><select type="text" name="conditionId">
					</select>
				</td>
				<td><select type="text" name="certificationId">
					</select>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	<script id="table-add" type="tetx/template" >
		<tr>
			<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
			<td><input type="text" name="code"/></td>
			<td><input type="text" name="partNumber"/></td>
			<td><input type="text" name="price"/></td>
			<td><select type="text" name="currencyId">
				</select>
			</td>
			<td><input type="text" name="year"/></td>
			<td><select type="text" name="conditionId">
				</select>
			</td>
			<td><select type="text" name="certificationId">
				</select>
			</td>
		</tr>
	</script>
</body>

</html>