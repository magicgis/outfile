<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改客户报价</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
</style> 
<script type="text/javascript">

function validate(){ 
	return validate2({
		nodeName:"profitMargin,prepayRate,shipPayRate,shipPayPeriod,receivePayRate,receivePayPeriod,quoteDate",
		form:"#addForm"
	});
 } 

function validate2(opt){
	var def = {nodeName:"",form: ""};
	for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
	var $items = $(def.form).find("input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
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
	});
	return flag;
};

	$(function(){
		PJ.datepicker('quoteDate');
		
		//币种
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchcurrency?id='+"${record.currencyId}",
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
		
	 	//交付方式
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchDelivery?id='+"${record.termsOfDelivery}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#termsOfDelivery").append($option);
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
	
	 function getFormData(){
			var $input = $("#addForm").find("input,textarea,select");
			var postData = {};
			var prepayRate = $("#prepayRate").val();
			var shipPayRate = $("#shipPayRate").val();
			var receivePayRate = $("#receivePayRate").val();
			var total = parseInt(prepayRate)+parseInt(shipPayRate)+parseInt(receivePayRate);
			if(total!=100){
				PJ.warn("预付比例有误！请核实！");
			}else{
				$input.each(function(index){
					if(!$(this).val()) {
						//PJ.tip("必填数据项没有填写完整");
						//flag = true;
						return;
					}
					
					postData[$(this).attr("name")] = $(this).val();
				});
				var check =validate();
				if(check){ 
				return postData;
			}}
	 }		
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="修改客户报价">
	<form id="addForm" action="">
	<input type="text" class="hide" name="id" value="${client_quote_id}" />
	<table>
	<tr>
			<td>询价单号</td>
			<td> ${client_inquiry_quote_number}</td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId" >
						<option value="${record.currencyId}">${currencyvalue}</option>
				</select>
			</td>
		</tr>
		
		<%-- <tr>
			<td>汇率</td>
			<td><input name="exchangeRate" id="exchangeRate"  class="tc" value="${record.exchangeRate}"/></td>
		</tr> --%>
		<tr>
			<td>交付方式</td>
			<td><select type="text" name="termsOfDelivery" id="termsOfDelivery">
			<option value="${record.termsOfDelivery}">${record.termsOfDeliveryValue}</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>佣金</td>
			<td><input type="text" name="fixedCost" id="fixedCost" value="${record.fixedCost}"/></td>
		</tr>
		<tr>
			<td>利润率</td>
			<td>
			
			<input name="profitMargin" id="profitMargin"  class="tc" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${profitmargin}" />"/>%</td>
		</tr>
		<tr>
			<td>预付比例</td>
			<td><input name="prepayRate" id="prepayRate"  class="tc" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${record.prepayRate}" />"/>%</td>
		</tr>
		<tr>
			<td>发货时支付比例</td>
			<td><input name="shipPayRate" id="shipPayRate"  class="tc" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${record.shipPayRate}" />"/>%</td>
		</tr>
		<tr>
			<td>发货账期</td>
			<td><input name="shipPayPeriod" id="shipPayPeriod"  class="tc" value="${record.shipPayPeriod}"/></td>
		</tr>
		<tr>
			<td>验货时支付比例</td>
			<td><input name="receivePayRate" id="receivePayRate"  class="tc" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${record.receivePayRate}" />"/>%</td>
		</tr>
		<tr>
			<td>验货账期</td>
			<td><input name="receivePayPeriod" id="receivePayPeriod"  class="tc" value="${record.receivePayPeriod}"/></td>
		</tr>
		<tr>
			<td>报价日期</td>
			<td><input name="quoteDate" id="quoteDate" name="quoteDate" class="tc" value="<fmt:formatDate value="${record.quoteDate}"  pattern="yyyy-MM-dd"/>"></td>
		</tr>
		<tr>
			<td>最低运费</td>
			<td><input type="text" name="lowestFreight" id="lowestFreight" value="<fmt:formatNumber value="${record.lowestFreight}" pattern="0"/>"/></td>
		</tr>
		<tr>
			<td>运费</td>
			<td><input type="text" name="freight" id="freight"  value="<fmt:formatNumber value="${record.freight}" pattern="0"/>"/></td>
		</tr>
		<tr>
			<td>银行费用</td>
			<td><input type="text" name="bankCost" id="bankCost" value="${record.bankCost}"/></td>
		</tr>
		<tr>
			<td>更新时间</td><td><fmt:formatDate value="${record.updateTimestamp}"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
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