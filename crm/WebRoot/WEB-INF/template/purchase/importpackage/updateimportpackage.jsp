<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改入库单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
</style> 
<script type="text/javascript">
	$(function(){
		PJ.datepicker('importDate');
		//var import = ${importPackage.importStatus};
		var importStatus = "${importPackage.importStatus}"
		if("${importPackage.importStatus}"==""){
			var $option = $("<option></option>");
			$option.val(1).text("完成");
			$("#importStatus").append($option);
			var $option = $("<option></option>");
			$option.val(0).text("未完成");
			$("#importStatus").append($option);
			var $option = $("<option></option>");
			$option.val(2).text("预入库");
			$("#importStatus").append($option);
		}else{
			if(importStatus=="0"){
				var $option = $("<option></option>");
				$option.val(0).text("未完成");
				$("#importStatus").append($option);
				var $option = $("<option></option>");
				$option.val(1).text("完成");
				$("#importStatus").append($option);
				var $option = $("<option></option>");
				$option.val(2).text("预入库");
				$("#importStatus").append($option);
			}else if(importStatus=="1"){
				var $option = $("<option></option>");
				$option.val(1).text("完成");
				$("#importStatus").append($option);
				var $option = $("<option></option>");
				$option.val(0).text("未完成");
				$("#importStatus").append($option);
				var $option = $("<option></option>");
				$option.val(2).text("预入库");
				$("#importStatus").append($option);
			}else if(importStatus=="2"){
				var $option = $("<option></option>");
				$option.val(2).text("预入库");
				$("#importStatus").append($option);
				var $option = $("<option></option>");
				$option.val(1).text("完成");
				$("#importStatus").append($option);
				var $option = $("<option></option>");
				$option.val(0).text("未完成");
				$("#importStatus").append($option);
			}
		}
		
		
		//币种
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchcurrency?id='+"${importPackage.currencyId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].currency_id).text(obj[i].currency_value);
						$("#currencyId").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
	 	//费用币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exportpackage/urgentList?id='+"${importPackage.feeCurrencyId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					if('${importPackage.feeCurrencyId}' == '' || '${importPackage.feeCurrencyId}' == 'undefined'){
						var $option = $("<option></option>");
						$option.val("").text("请选择");
						$("#feeCurrencyId").append($option);
					}
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#feeCurrencyId").append($option);
						
					}
				}else{
						PJ.warn("操作失误!");
				}
			}
		});
		
	 	//物流方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/importpackage/logisticsWay?id='+"${importPackage.logisticsWay}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#logisticsWay").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#addForm")[0].reset();
		 });
		 
		 //新增按键事件
		<%--  $("#addBtn").click(function(){
			 $.ajax({
					url: '<%=path%>/clientquote/updateclientquote',
					data: getFormData(),
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}
					}
				});
		 }); --%>
	});
	
	//获取表单数据
	 function getFormData(){
			var $input = $("#addForm").find("input,textarea,select");
			var postData = {};
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
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="修改入库单">
	<form id="addForm" action="">
	<input type="text" class="hide" name="id" value="${id}" />
	<table>
	<tr>
			<td>供应商</td>
			<td> ${importPackage.supplierCode}</td>
		</tr>
		<tr>
			<td>入库单号</td>
			<td><input name="importNumber" id="importNumber"  class="tc" value="${importPackage.importNumber}" disabled="disabled"/></td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId" >
						<option value="${importPackage.currencyId}">${importPackage.currencyValue}</option>
				</select>
			</td>
		</tr>
			<tr>
			<td>物流方式</td>
			<td><select id="logisticsWay" name="logisticsWay">
						<option value="${importPackage.logisticsWay}">${importPackage.logisticsWayValue}</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>运输单号</td>
			<td><input name="logisticsNo" id="logisticsNo"  class="tc" value="${importPackage.logisticsNo}"/></td>
		</tr>
		<%-- <tr>
			<td>汇率</td>
			<td><input name="exchangeRate" id="exchangeRate"  class="tc" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${importPackage.exchangeRate}" />"/></td>
		</tr> --%>
		
		<tr>
			<td>入库日期</td>
			<td><input name="importDate" id="importDate"  class="tc" value="<fmt:formatDate value="${importPackage.importDate}"  pattern="yyyy-MM-dd"/>"></td>
					</tr>
		<tr>
			<td>状态</td>
			<td>
					<select id="importStatus" name="importStatus">
					
					</select>			
			</td>
		</tr>			
		<tr>
			<td>重量</td>
			<td><input name="weight" id="weight"  class="tc" value="${importPackage.weight}" />kg</td>
		</tr>
		<tr>
			<td>入库费</td>
			<td><input name="importFee" id="importFee" value="${importPackage.importFee}"/></td>
		</tr>
		<tr>
			<td>运费</td>
			<td><input name="freight" id="freight" value="${importPackage.freight}"/></td>
		</tr>
		<tr>
			<td>费用币种</td>
			<td><select name="feeCurrencyId" id="feeCurrencyId"  class="tc">
				</select>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea id="remark" name="remark" rows="8" cols="50">${importPackage.remark}</textarea> </td>
		</tr>
		<tr>
			<td>更新时间</td><td><fmt:formatDate value="${importPackage.updateTimestamp}"  pattern="yyyy-MM-dd"/>
			</td>
			
		</tr>
		<tr>
			<td colspan="2">
			<!-- 	<input class="btn btn_orange" type="button" value="修改" id="addBtn"/>
				<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/> -->
			</td>
			<td></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>