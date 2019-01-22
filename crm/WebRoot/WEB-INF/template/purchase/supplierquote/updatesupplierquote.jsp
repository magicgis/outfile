<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改供应商报价</title>

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
		
	 	$("#quoteDate").blur(function(){
	 		var today =$("#quoteDate").val();
	 	/* 	var strs= new Array();
			strs=today.split("-",3);
			 
			//var today = new Date();
			var year = strs[0];
			var month = parseInt(strs[1]);
			var days = parseInt(30);
			var dayCount = days + parseInt(strs[2]);
			if(dayCount>30){
				month= parseInt(month)+1;
				days=dayCount-30;
			}else{
				days=dayCount;
			}
			if(month>12){
				month=month-12;
				year= parseInt(year)+1;
			}
			var time = year+'-'+month+'-'+days;  */
			 $.ajax({
					type: "POST",
					dataType: "",
					data: '',
					url:'<%=path%>/supplierquote/validity?validity='+today,
					success:function(result){
						if(result.success){
							$("#validity").val(result.message);
						}else{
							PJ.warn(result.message);
						}
					}
			});
		}); 
		
		//币种
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchcurrency?id='+"${supplierQuote.currencyId}",
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
		
	 	//状态
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findstatus?quoteStatusId='+"${supplierQuote.quoteStatusId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#quoteStatusId").append($option);
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
<div position="center" title="修改供应商报价">
	<form id="addForm" action="">
	<input type="text" class="hide" name="id" value="${supplierQuote.id}" />
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
			<td>供应商报价单号</td>
			<td> <input name="sourceNumber" id="sourceNumber" value="${sourceNumber}"/></td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId" >
						<option value="${supplierQuote.currencyId}">${supplierQuote.value}</option>
				</select>
			</td>
		</tr>
		
		<%-- <tr>
			<td>汇率</td>
			<td><input name="exchangeRate" id="exchangeRate"  class="tc" value="${exchangeRate}"/></td>
		</tr> --%>
		<tr>
			<td>报价日期</td>
			<td><input name="quoteDate" id="quoteDate" name="quoteDate" class="tc" value="<fmt:formatDate value="${supplierQuote.quoteDate}"  pattern="yyyy-MM-dd"/>"></td>
		</tr>
		<tr>
			<td>有效日期</td>
			<td><input name="validity" id="validity" class="tc" value="<fmt:formatDate value="${supplierQuote.validity}" pattern="yyyy-MM-dd"/>"/><span id="msg"></span></td>
		</tr>
		<%-- <tr>
			<td>银行费用</td>
			<td><input name="bankCost" id="bankCost" value="${supplierQuote.bankCost}"/></span></td>
		</tr>
		<tr>
			<td>提货换单费</td>
			<td><input name="feeForExchangeBill" id="feeForExchangeBill" value="${supplierQuote.feeForExchangeBill}"/></span></td>
		</tr> --%>
		<tr>
			<td>状态</td>
			<td><select id="quoteStatusId" name="quoteStatusId" >
						<option value="${supplierQuote.quoteStatusId}">${supplierQuote.quoteStatusValue}</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>更新时间</td><td>${updateTimestamp}</td>
			
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