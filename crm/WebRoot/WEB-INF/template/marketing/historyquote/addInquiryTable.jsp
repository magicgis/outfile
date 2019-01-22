<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户询价明细管理 </title>

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
		PJ.datepicker('deadline');
		PJ.datepicker('inquiryDate');
		PJ.datepicker('realDeadline');
			$("#toolbar").ligerToolBar({
				items : [
						   {  icon : "add",
							  text : "增加空白行",
							  click : function(){
									  	var $tbody = $("#elementTable>tbody");//-- 容器
										var $item = $($("script[type='tetx/template']").html()).clone();//-- 复制模版
										$tbody.append($item);
										var $items = $("#elementTable>tbody").find("tr");
										var index = 1;
										$items.each(function(){
											var $inputs = $(this).find("input");
											$inputs.each(function(){
												if($(this).attr("name") == "item" || $(this).attr("name") == "csn"){
													$(this).val(index);
												}
												
											});
											index++;
										});
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
							},
							{ icon : "add",
							  text : "Paste Excel",
							  click : function(){
								  iframeId = "iframeId";
								  PJ.topdialog(iframeId, '粘贴Excel内容', '<%=path%>/sales/clientinquiry/pasteExcel',
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();
													 //var list = postData.list;
													 var tb = $("#elementTable>tbody");
													 for(var i=0;i<postData.length;i++){
														 var $tr = $("<tr></tr>");
														 var check = $("<td><input type=\"checkbox\" class=\"form-checkbox\" name=\"dataItem\" /></td>");
														 $tr.append(check);
														 if(postData[i]["item"] != undefined){
															 var $td1 = $("<td><input type=\"text\" name=\"item\" size=\"6\" value=\""+ postData[i]["item"] +"\"/></td>");
												   			 $tr.append($td1);
														 }else{
															 var item = parseInt(i)+parseInt(1);
															 var $td1 = $("<td><input type=\"text\" name=\"item\" size=\"6\" value=\""+ item +"\"/></td>");
												   			 $tr.append($td1);
														 }
														 if(postData[i]["csn"] != undefined){
															 var $td2 = $("<td><input type=\"text\" name=\"csn\" size=\"6\" value=\""+ postData[i]["csn"] +"\"/></td>");
												   			 $tr.append($td2);
														 }else{
															 var csn = parseInt(i)+parseInt(1);
															 var $td2 = $("<td><input type=\"text\" name=\"csn\" size=\"6\" value=\""+ item +"\"/></td>");
												   			 $tr.append($td2);
														 }
														 if(postData[i]["partNumber"] != undefined){
															 var $td3 = $("<td><input type=\"text\" name=\"partNumber\" value=\""+ postData[i]["partNumber"] +"\"/></td>");
												   			 $tr.append($td3);
														 }else{
															 var $td3 = $("<td><input type=\"text\" name=\"partNumber\" value=\""+ "" +"\"/></td>");
												   			 $tr.append($td3);
														 }
														 if(postData[i]["replacePartNumber"] != undefined){
															 var $td4 = $("<td><input type=\"text\" name=\"alterPartNumber\" value=\""+ postData[i]["replacePartNumber"] +"\"/></td>");
												   			 $tr.append($td4);
														 }else{
															 var $td4 = $("<td><input type=\"text\" name=\"alterPartNumber\" value=\""+ "" +"\"/></td>");
												   			 $tr.append($td4);
														 }
														 if(postData[i]["description"] != undefined){
															 var $td5 = $("<td><input type=\"text\" name=\"description\" value=\""+ postData[i]["description"] +"\"/></td>");
												   			 $tr.append($td5);
														 }else{
															 var $td5 = $("<td><input type=\"text\" name=\"description\" value=\""+ "" +"\"/></td>");
												   			 $tr.append($td5);
														 }
														 if(postData[i]["unit"] != undefined){
															 var $td6 = $("<td><input type=\"text\" name=\"unit\" size=\"6\" value=\""+ postData[i]["unit"] +"\"/></td>");
												   			 $tr.append($td6);
														 }else{
															 var $td6 = $("<td><input type=\"text\" name=\"unit\" size=\"6\" value=\""+ "EA" +"\"/></td>");
												   			 $tr.append($td6);
														 }
														 if(postData[i]["amount"] != undefined){
															 var $td7 = $("<td><input type=\"text\" name=\"amount\" size=\"6\" value=\""+ postData[i]["amount"] +"\"/></td>");
												   			 $tr.append($td7);
														 }else{
															 var $td7 = $("<td><input type=\"text\" name=\"amount\" size=\"6\" value=\""+ "" +"\"/></td>");
												   			 $tr.append($td7);
														 }
														 if(postData[i]["condition"] != undefined){
															 var $td8 = $("<td><input type=\"text\" name=\"condition\" value=\""+ postData[i]["condition"] +"\"/></td>");
												   			 $tr.append($td8);
														 }else{
															 var $td8 = $("<td><input type=\"text\" name=\"condition\" value=\""+ "FN" +"\"/></td>");
												   			 $tr.append($td8);
														 }
														 if(postData[i]["remark"] != undefined){
															 return 
															 var $td9 = $("<td><input type=\"text\" name=\"remark\" value=\""+ postData[i]["remark"] +"\"/></td>");
												   			 $tr.append($td9);
														 }else{
															 var $td9 = $("<td><input type=\"text\" name=\"remark\" value=\""+ "" +"\"/></td>");
												   			 $tr.append($td9);
														 }
											   			 tb.append($tr);
													 }
													 dialog.close();
													 
													<%--  <%
													 	request.setAttribute("elements",%>postData<%) ; 
													 %> --%>
													 
													 <%-- PJ.ajax({
															url: '<%=path%>/sales/clientinquiry/edit',
															data: postData,
															type: "POST",
															loading: "正在处理...",
															success: function(result){
																	if(result.success){
																		PJ.success(result.message);
																		PJ.grid_reload(grid);
																		dialog.close();
																	} else {
																		PJ.error(result.message);
																		dialog.close();
																	}		
															}
														}); --%>
											},function(item,dialog){dialog.close();}, 1000, 500, true,"确定");
												
								}
							},
							{ icon : "add",
							  text : "clone",
							  click : function(){
									var $checkedItems = $("#elementTable>tbody>tr").find("input:checkbox");
									$checkedItems.each(function(i){
								    	if($(this).prop("checked")){
								    		//var $item = $($($(this).parent().parent()).html()).clone();
								    		var $item = $(this).parent().parent().clone();
											$($(this).parent().parent()).after($item);
										} 
									});
							        var $items = $("#elementTable>tbody").find("tr");
									var index = 1;
									$items.each(function(){
										var $inputs = $(this).find("input");
										$inputs.each(function(){
											if($(this).attr("name") == "item" || $(this).attr("name") == "csn"){
												$(this).val(index);
											}
											
										});
										index++;
									});
											
							  }
							}
				        ]
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
		
		//机型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/airType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
						$("#airtype").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//商业类型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/bizType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
						$("#biztype").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//客户及联系人
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/contacts?flag='+'add',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id+","+obj[i].clientId).text(obj[i].code+"-"+obj[i].name);
						$("#contacts").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
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
	
	//获取表单数据
	function getFormData(){
		var postData = getTableDate();
		var $input = $("#addForm").find("input,textarea,select");
		

		$input.each(function(index){
			if(!$(this).val()) {
				//PJ.tip("必填数据项没有填写完整");
				//flag = true;
				return;
			}
				
			postData[$(this).attr("name")] = $(this).val();
		});
		return postData;
	}
	
	/* function getFormData(){
		var postData = getTableDate();
		postData.check = check
		postData.token='${token}';
		check = 1;
		return postData;
	} */
	
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
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"mixId,bizTypeId,airTypeId,sourceNumber,inquiryDate",
			form:"#inquirymessge"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,select");var nodes = def.nodeName.split(",");var flag = true;var tip;
		$items.each(function(i){
			var name = $(this).attr("name");
			if(!name)return;
			for(var k in nodes){
				if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
				if(!flag){
	                $(this).addClass("input-error");
					tip = $(this).attr("data-tip");
					return false;
				}else $(this).removeClass("input-error");
			}
			if($("#contacts").val()==""||$("#contacts").text()=="请选择"){
				$("#contacts").addClass("input-error");
					tip = $("#contacts").attr("data-tip");
					flag=false;
					return false;
			}
			if($("#biztype").val()==""||$("#biztype").text()=="请选择"){
				$("#biztype").addClass("input-error");
					tip = $("#biztype").attr("data-tip");
					flag=false;
					return false;
			}
			if($("#airtype").val()==""||$("#airtype").text()=="请选择"){
				$("#airtype").addClass("input-error");
					tip = $("#airtype").attr("data-tip");
					flag=false;
					return false;
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
	
</script>

</head>
<style>
</style>
<body>
	<div id="toolbar"></div>
	<div id="inquirymessge">
	<form id="addForm">
		<table style=" border-collapse:   separate;   border-spacing:   10px;">
			<tr>
				<td>客户及联系人</td>
				<td><select id="contacts" name="mixId" class="text">
							<option value="">请选择</option>
					</select>
				</td>
				<td>商业类型</td>
				<td><select id="biztype" name="bizTypeId" class="text">
							<option value="">请选择</option>
					</select>
				</td>
				<td>机型</td>
				<td><select id="airtype" name="airTypeId" class="text">
							<option value="">请选择</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>客户询价单号</td>
				<td><input name="sourceNumber" id="sourceNumber" class="text"/></td>
				<td>询价日期</td>
				<td><input name="inquiryDate" id="inquiryDate" class="text" class="tc" value="<fmt:formatDate value="${today }"  pattern="yyyy-MM-dd"/>"/></td>
				<td>截至日期</td>
				<td><input name="deadline" id="deadline" class="text" class="tc"/></td>
			</tr>
			<tr>
				<td>实际截标时间</td>
				<td><input name="realDeadline" id="realDeadline" class="text" class="tc"/></td>
				<td>备注</td>
				<td colspan=3><input name="remark" size="80"/></td>
			</tr>
		</table>
	</form>
	</div>
	<div>
	<table id="elementTable" border="1px">
	<input type="text" name="token" id="token" value="${token}" class="hide"/>
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
			<%-- <c:forEach var="element" items="copyElement" >
				<tr>
					<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
					<td><input type="text" name="item" size="6" value="${elements.item }"/></td>
					<td><input type="text" name="csn" size="6" value="${elements.csn }"/></td>
					<td><input type="text" name="partNumber" value="${elements.partNumber }"/></td>
					<td><input type="text" name="alterPartNumber" value="${elements.alterPartNumber }"/></td>
					<td><input type="text" name="description" value="${elements.description }"/></td>
					<td><input type="text" name="unit" size="6" value="${elements.unit }"/></td>
					<td><input type="text" name="amount" size="6" value="${elements.amount }"/></td>
					<td><input name="conditionId" size="10" value="${elements.conditionId }"></td>
					<td><input type="text" name="typeCode" value="${elements.typeCode }"/></td>
					<td><input type="text" name="remark" value="${elements.remark }"/></td>
				</tr>
			</c:forEach> --%>
			<!-- <tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="item" size="6"/></td>
				<td><input type="text" name="csn" size="6"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="alterPartNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input type="text" name="unit" size="6"/></td>
				<td><input type="text" name="amount" size="6"/></td>
				<td><select name="conditionId"></select></td>
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
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
				<td><input type="text" name="typeCode"/></td>
				<td><input type="text" name="remark"/></td>
			</tr> -->
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
			<td><input type="text" name="unit" size="6" value="EA"/></td>
			<td><input type="text" name="amount" size="6"/></td>
			<td><input name="conditionCode" value="FN"/></td>
			<td><input type="text" name="remark"/></td>
		</tr>
	</script>
</body>

</html>