<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增供应商报价</title>

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
		
		
		var today =$("#quoteDate").val();
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
 		/* var strs= new Array();
		strs=today.split("-",3);
		 
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
		var time = year+'-'+month+'-'+days; 
		$("#validity").val(time); */
		
	 	$("#quoteDate").blur(function(){
	 		var today =$("#quoteDate").val();
	 		/* var strs= new Array();
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
			var time = year+'-'+month+'-'+days; 
			$("#validity").val(time); */
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
		}); /**/
		
		//货币信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/purchase/supplierinquiry/Currency?id='+${manageVo.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#currencyId").append($option);
						/* if(obj[i].currency_id==11){
							$("#exchangeRate").val(obj[i].exchangeRate);
						} */
						
					}
				}else{
					
						PJ.warn("操作失误!");
				}
			}
		});
		
		
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
<div position="center" title="新增供应商报价">
	<form id="addForm" action="">
	<input type="text" class="hide" name="supplierInquiryId" value="${manageVo.id }"/>
	<table style="border-collapse:separate; border-spacing:20px;">
		<tr>
			<td>询价单号</td>
			<td>${manageVo.clientInquiryQuoteNumber }</td>
		</tr>
		<tr>
			<td>供应商询价单号</td>
			<td>${manageVo.supplierInquiryQuoteNumber }</td>
		</tr>
		<tr>
			<td>供应商报价单号</td>
			<td><input name="sourceNumber" id="sourceNumber" /></td>
		</tr>
		<tr>
			<td>币种</td>
			<td>
				<select name="currencyId" id="currencyId">
				</select>
			</td>
		</tr>
		<tr>
			<td>报价日期</td>
			<td><input name="quoteDate" id="quoteDate" class="tc" value="<fmt:formatDate value="${today }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>有效日期</td>
			<td><input name="validity" id="validity" class="tc" value=""/><span id="msg"></span></td>
		</tr>
		<tr>
			<td>银行费用</td>
			<td><input name="bankCost" id="bankCost" value="${fee }"/></span></td>
		</tr>
		<tr>
			<td>提货换单费</td>
			<td><input name="feeForExchangeBill" id="feeForExchangeBill" value=""/></span></td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>