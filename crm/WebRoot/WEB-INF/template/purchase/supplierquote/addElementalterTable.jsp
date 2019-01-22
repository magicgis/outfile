<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商报价明细管理 </title>

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
						}/* ,
						{ icon : "add",
							  text : "数据带出",
							  click : function(){
								  
								}
							}  */
			        ]
		});
		
		$("#submit").click(function(){
			var url = '<%=path%>/supplierquote/uploadExcel?id='+${id};
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
		            	var iframeId2 = "checkFrame";
		            	PJ.topdialog(iframeId2, '填写金额', '<%=path%>/market/clientweatherorder/toCheckTotal',
								function(item, dialog){
									var postData=top.window.frames[iframeId2].getFormData();
									var validate = top.window.frames[iframeId2].validate();
									if(validate){
										$.ajax({
											url: '<%=path%>/supplierquote/checkTotalByQuote?id='+${id},
											data: postData,
											type: "POST",
											loading: "正在处理...",
											success: function(result){
													if(result.success){
														PJ.success(result.message);
														dialog.close();
													} else {
														PJ.error(result.message);
														dialog.close();
													}		
											}
										});
									}else{
										PJ.warn("请填写完数据");
									}
							},function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 250, 110, true,"确定");
	            	}
	            	else {
                        var msg = da.message;
                        if (msg == 'type1') {
                            PJ.error("请填好Excel询价单号再上传");
                        } else if (msg == 'type2') {
                            PJ.error("请检查Excel表格询价单号再上传");
                        } else {
                            iframeId = 'errorframe'
                            PJ.topdialog(iframeId, '错误信息', '<%=path%>/supplierquote/toUnknow',
                                undefined, function (item, dialog) {
                                    $.ajax({
                                        url: '<%=path%>/supplierquote/deleteData',
                                        type: "POST",
                                        loading: "正在处理...",
                                        success: function (result) {
                                        }
                                    });
                                    dialog.close();
                                }, 1000, 500, true);

                        }
                    }
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            	closeoropen();
	            }  
	        });  
		});	
		setTimeout(timeout,500);
	});
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
			var $isnull= $(this).find("input[class='isnull']");
			$isnull.each(function(){
				if($(this).val()!=""){
			$inputs.each(function(){
				obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
			});
			index++;
				}
			});
		});
		
		return obj;
	}
	
	function timeout(){
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
	<div id="uploadBox" >
	<form id="form" name="form">
	 	<input type="hidden" name="id" id="id" value="${id}"/>
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
				<th style="text-align: center;">序号</th>
				<th style="text-align: center;">件号</th>
				<th style="text-align: center;">描述</th>
				<th style="text-align: center;">单位</th>
				<th style="text-align: center;">数量</th>
				<th style="text-align: center;">MOQ</th>
				<th style="text-align: center;">单价</th>
				<th style="text-align: center;">Available Qty</th>
				<th style="text-align: center;">location</th>
				<th style="text-align: center;">周期</th>
				<th style="text-align: center;">状态</th>
				<th style="text-align: center;">证书</th>
				<th style="text-align: center;">备注</th>
				<th style="text-align: center;">提货换单费</th>
				<th style="text-align: center;">银行费用</th>
				<th style="text-align: center;">HAZMAT FEE</th>
				<th style="text-align: center;">杂费</th>
				<th style="text-align: center;">Core Charge</th>
				<th style="text-align: center;">Serial Number</th>
				<th style="text-align: center;">Tag Src</th>
				<th style="text-align: center;">Tag Date</th>
				<th style="text-align: center;">Trace</th>
				<th style="text-align: center;">Warranty</th>
				<th style="text-align: center;">有效期</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="message" items="${list}" >
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input style="width: 30px" type="text" name="item" value="${message.item}"/></td>
				<td><input type="text" name="partNumber" value="${message.partNumber}"/></td>
				<td><input type="text" name="description" value="${message.description}"/></td>
				<td><input style="width: 50px" type="text" name="unit" value="${message.unit}"/></td>
				<td><input style="width: 50px" type="text" name="amount" value="${message.amount}"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 100px" type="text" name="availableQty"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark" value="${message.remark}"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill" value="${message.feeForExchangeBill}"/></td>
				<td><input type="text" style="width: 50px" name="bankCost" value="${message.bankCost}"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee"/></td>
				<td><input type="text" style="width: 50px" name="otherFee" value=""/></td>
				<td><input type="text" name="coreCharge"/></td>
					<td><input type="text" name="serialNumber"/></td>
					<td><input type="text" name="tagSrc"/></td>
					<td><input type="text" name="tagDate" class="tagDate"/></td>
					<td><input type="text" name="trace"/></td>	
					<td><input type="text" name="warranty"/></td>
					<td><input type="text" style="width: 90px" name="validity" class="validity" value="<fmt:formatDate value="${message.validity}" pattern="yyyy-MM-dd"/>" /></td>
			</tr>
			</c:forEach>
		 	<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input style="width: 30px" type="text" name="item"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 100px" type="text" name="availableQty"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee"/></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
					<td><input type="text" name="serialNumber"/></td>
					<td><input type="text" name="tagSrc"/></td>
					<td><input type="text" name="tagDate" class="tagDate"/></td>
					<td><input type="text" name="trace"/></td>
					<td><input type="text" name="warranty"/></td>
					<td><input type="text" style="width: 90px" name="validity" id="" class="validity"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input style="width: 30px" type="text" name="item"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 100px" type="text" name="availableQty"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee"/></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text"  name="coreCharge"/></td>
					<td><input type="text" name="serialNumber"/></td>
					<td><input type="text" name="tagSrc"/></td>
					<td><input type="text" name="tagDate" class="tagDate"/></td>
					<td><input type="text" name="trace"/></td>
					<td><input type="text" name="warranty"/></td>
							<td><input type="text" style="width: 90px" name="validity" id="" class="validity"/></td>
			</tr>
		</tbody>
	</table>
	</div>
	<script id="table-add" type="tetx/template" >
		<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input style="width: 50px" type="text" name="item"/></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 100px" type="text" name="availableQty"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 50px" name="hazmatFee"/></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
					<td><input type="text" name="serialNumber"/></td>
					<td><input type="text" name="tagSrc"/></td>
					<td><input type="text" name="tagDate" class="tagDate"/></td>
					<td><input type="text" name="trace"/></td>
					<td><input type="text" name="warranty"/></td>
					<td><input type="text" style="width: 90px" name="validity" id="" class="validity"/></td>
			</tr>
	</script>
</body>

</html>