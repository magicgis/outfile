<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增客户报价</title>

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
		
		
		//货币信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/currencyType?clientId='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].currency_id).text(obj[i].currency_value);
						$("#currencyId").append($option);
					}
				}else{
					PJ.warn("操作失误!");
				}
			}
		});
		
		//交付方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/delivery?id='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#termsOfDelivery").append($option);
						
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
				return postData;
			}
	}
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"currencyId,profitMargin,prepayRate,shipPayRate,shipPayPeriod,receivePayRate,receivePayPeriod,quoteDate",
			form:"#addForm"
		})
	};
	
	//-- 验证，默认通过true,有空未填则返回false
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
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="新增客户询价">
	<form id="addForm" action="">
	<input type="text" class="hide" name="clientInquiryId" value="${clientInquiry.id }"/>
	<input type="text" class="hide" name="quoteNumber" value="${clientInquiry.quoteNumber }"/>
	<table>
		<tr>
			<td>询价单号</td>
			<td>${clientInquiry.quoteNumber }</td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId">
				</select>
			</td>
		</tr>
		<tr>
			<td>交付方式</td>
			<td><select type="text" name="termsOfDelivery" id="termsOfDelivery">
				</select>
			</td>
		</tr>
		<tr>
			<td>佣金</td>
			<td><input type="text" name="fixedCost" id="fixedCost"/></td>
		</tr>
		<tr>
			<td>利润率</td>
			<td><input type="text" name="profitMargin" id="profitMargin" value="${client.profitMargin }"/>%</td>
		</tr>
		<tr>
			<td>预付比例</td>
			<td><input type="text" name="prepayRate" id="prepayRate" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${client.prepayRate }"/>"/>%</td>
		</tr>

		<tr>
			<td>发货时支付比例</td>
			<td><input type="text" name="shipPayRate" id="shipPayRate" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${client.shipPayRate }"/>"/>%</td>
		</tr>
		<tr>
			<td>发货帐期</td>
			<td><input type="text" name="shipPayPeriod" id="shipPayPeriod" value="${client.shipPayPeriod }"/></td>
		</tr>
		<tr>
			<td>验货时支付比例</td>
			<td><input type="text" name="receivePayRate" id="receivePayRate" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${client.receivePayRate }"/>"/>%</td>
		</tr>
		<tr>
			<td>验货账期</td>
			<td><input type="text" name="receivePayPeriod" id="receivePayPeriod" value="${client.receivePayPeriod }"/></td>
		</tr>
		<tr>
			<td>报价日期</td>
			<td><input name="quoteDate" id="quoteDate" class="tc" value="<fmt:formatDate value="${clientInquiry.inquiryDate }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>最低运费</td>
			<td><input type="text" name="lowestFreight" id="lowestFreight" /></td>
		</tr>
		<tr>
			<td>运费</td>
			<td><input type="text" name="freight" id="freight" /></td>
		</tr>
		<tr>
			<td>银行费用</td>
			<td><input type="text" name="bankCost" id="bankCost" /></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>