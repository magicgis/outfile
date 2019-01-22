<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>增加明细 </title>

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
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/sales/clientinquiry/uploadExcel?id='+getUploadData().id+'&editType='+ getUploadData().toString();
			//批量新增来函案源
			PJ.showLoading("上传中....");
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	var da = eval(data)[0];
	            	if(da.flag==true){
	            		PJ.hideLoading();
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	PJ.grid_reload(grid2);
	            	}else{
	            		if(da.message == "only one"){
	            			PJ.success("商业类型为4的询价单明细只允许上传1条！");
	            			PJ.hideLoading();
	            		}else{
	            			PJ.hideLoading();
		            		iframeId = 'errorframe'
		            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/sales/clientinquiry/toErrorMessage',
		            				undefined,function(item,dialog){
				            			$.ajax({
											url: '<%=path%>/sales/clientinquiry/deleteMessage',
											type: "POST",
											loading: "正在处理...",
											success: function(result){
											}
										});
		            					dialog.close();}, 1000, 500, true);
	            		}
	            	}
	            	
	            	/* if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	}
	            	else{
	            		PJ.error(da.message);
	            		$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	} */
	   				
	            },  
	            /* error: function (data, status, e) { 
	            	PJ.hideLoading();
	            	PJ.error("上传异常！");
	            }   */
	        });
			
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
									$option.val(obj[i].id).text(obj[i].code);
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
		
	});
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
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
	
	function getUploadData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
</script>

</head>

<body>
	<div id="toolbar"></div>
	<div id="uploadBox">
			<form id="form" name="form">
			 	<input class="hide" name="id" id="id" value="${id}"/>
		   		
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
	<div>
	<table id="elementTable" border="1px">
		<thead>
			<tr>
				<th></th>
				<th>序号</th>
				<th>CSN</th>
				<th>件号</th>
				<th>另件号</th>
				<th>描述</th>
				<th>单位</th>
				<th>数量</th>
				<th>状态</th>
				<!-- <th>类型代码</th> -->
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<!-- <td><input type="text" name="typeCode"/></td> -->
				<td><input type="text" name="remark"/></td>
			</tr>
		</tbody>
	</table>
	</div>
	<script id="table-add" type="tetx/template" >
		<tr>
			<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
			<td><input type="text" name="item" size="6"/></td>
			<td><input type="text" name="csn" size="6"/></td>
			<td><input type="text" name="partNumber"/></td>
			<td><input type="text" name="alterPartNumber"/></td>
			<td><input type="text" name="description"/></td>
			<td><input type="text" name="unit" size="6"/></td>
			<td><input type="text" name="amount" size="6"/></td>
			<td><select name="conditionId"></select></td>
			<td><input type="text" name="remark"/></td>
		</tr>
	</script>
</body>

</html>