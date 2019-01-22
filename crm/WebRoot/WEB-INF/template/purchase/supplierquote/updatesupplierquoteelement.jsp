<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改供应商报价明细</title>

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
		PJ.datepicker('quoteDate');
		PJ.datepicker('validity');
		//状态
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findcond?conditionId='+"${supplierQuoteElement.conditionId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#conditionId").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
	 	//证书
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findcert?certificationId='+"${supplierQuoteElement.certificationId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#certificationId").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
	 	
	 	//状态
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findsqstauts?supplierQuoteStatusId='+"${supplierQuoteElement.supplierQuoteStatusId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#supplierQuoteStatusId").append($option);
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
<div position="center" title="修改供应商报价明细">
	<form id="addForm" action="">
	<table>
	<tr>
			<td>询价单号</td>
			<td> ${clientInquiryQuoteNumber}</td>
		</tr>
		<tr>
			<td>供应商询价单号</td>
			<td> ${supplierInquiryQuoteNumber}</td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="quoteAmount"  class="tc" value="${supplierQuoteElement.amount}"/></td>
		</tr>
		<tr>
			<td>MOQ</td>
			<td><input name="moq" id="moq"  class="tc" value="${supplierQuoteElement.moq}"/></td>
		</tr>
		<tr>
			<td>单位</td>
			<td><input name="unit" id="quoteUnit"  class="tc" value="${supplierQuoteElement.unit}"/></td>
		</tr>
		<tr>
			<td>单价</td>
			<td><input name="price" id="price"  class="tc" value="${supplierQuoteElement.price}"/></td>
		</tr>
		<tr>
			<td>描述</td>
			<td><input name="description" id="quoteDescription"  class="tc" value="${supplierQuoteElement.description}"/></td>
		</tr>
		<tr>
			<td>周期</td>
			<td><input name="leadTime" id="leadTime"  class="tc" value="${supplierQuoteElement.leadTime}"/></td>
		</tr>
		<tr>
			<td>Available Qty</td>
			<td><input name="availableQty" id="availableQty"  class="tc" value="${supplierQuoteElement.availableQty}"/></td>
		</tr>
		<tr>
			<td>location</td>
			<td><input name="location" id="location"  class="tc" value="${supplierQuoteElement.location}"/></td>
		</tr>
		<tr>
			<td>状态</td>
			<td><select id="conditionId" name="conditionId" >
						<option value="${supplierQuoteElement.conditionId}">${supplierQuoteElement.conditionCode}</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>有效日期</td>
			<td><input name="validity" id="validity" class="tc" value="<fmt:formatDate value="${supplierQuoteElement.validity}" pattern="yyyy-MM-dd"/>"/><span id="msg"></span></td>
		</tr>
		<tr>
			<td>证书</td>
			<td><select id="certificationId" name="certificationId" >
						<option value="${supplierQuoteElement.certificationId}"> ${supplierQuoteElement.certificationValue}</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>状态</td>
			<td><select id="supplierQuoteStatusId" name="supplierQuoteStatusId" >
						<option value="${supplierQuoteElement.supplierQuoteStatusId}">${supplierQuoteElement.supplierQuoteStatusValue}</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>提货换单费</td>
			<td><input id="feeForExchangeBill" name="feeForExchangeBill" value="${supplierQuoteElement.feeForExchangeBill}"></input> </td>
		</tr>
		<tr>
			<td>银行费用</td>
			<td><input id="bankCost" name="bankCost" value="${supplierQuoteElement.bankCost}"></input> </td>
		</tr>
		<tr>
			<td>HAZMAT FEE</td>
			<td><input id="hazmatFee" name="hazmatFee" value="${supplierQuoteElement.hazmatFee}"></input> </td>
		</tr>
		<tr>
			<td>杂费</td>
			<td><input id="otherFee" name="otherFee" value="${supplierQuoteElement.otherFee}"></input> </td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea id="remark" name="remark" rows="8" cols="50">${supplierQuoteElement.remark}</textarea> </td>
		</tr>
		<tr>
			<td>Core Charge</td>
			<td><input id="coreCharge" name="coreCharge" value="${supplierQuoteElement.coreCharge}"></input> </td>
		</tr>
		<tr>
			<td>warranty</td>
			<td><input id="warranty" name="warranty" class="tc" value="${supplierQuoteElement.warranty}"></input> </td>
		</tr>
		<tr>
			<td>serialNumber</td>
			<td><input id="serialNumber" name="serialNumber"  value="${supplierQuoteElement.serialNumber}"></input> </td>
		</tr>
		<tr>
			<td>tagSrc</td>
			<td><input id="tagSrc" name="tagSrc"  value="${supplierQuoteElement.tagSrc}"></input> </td>
		</tr>
		<tr>
			<td>tagDate</td>
			<td><input id="tagDate" name="tagDate"  value="${supplierQuoteElement.tagDate}"></input> </td>
		</tr>
		<tr>
			<td>trace</td>
			<td><input id="trace" name="trace"  value="${supplierQuoteElement.trace}"></input> </td>
		</tr>
		<tr>
			<td>更新时间</td><td><fmt:formatDate value="${supplierQuoteElement.updateTimestamp}"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
				
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