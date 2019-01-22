<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增客户订单</title>

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
		nodeName:"currencyId,sourceNumber,prepayRate,shipPayRate,shipPayPeriod,receivePayRate,receivePayPeriod,orderDate",
		form:"#addForm"
	});
 } 

function validate2(opt){
	var def = {nodeName:"",form: ""};
	for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
	var $items = $(def.form).find("select,input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
	$items.each(function(i){
		var name = $(this).attr("name");
		if(!name)return;
		for(var k in nodes){
			if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
			if(!flag){
                $(this).addClass("input-error");
				tip = $(this).attr("data-tip");
				return false;
			}
			else $(this).removeClass("input-error");
		}
		 if($("#currencyId").val()==""||$("#currencyId").text()=="请选择"){
			$("#currencyId").addClass("input-error");
				tip = $("#currencyId").attr("data-tip");
				flag=false;
				return false;
		}
	});
	return flag;
};

	$(function(){
		PJ.datepicker('orderDate');
		
		//币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchcurrency',
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
		
		//紧急状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/market/clientweatherorder/urgentList',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#urgentLevelId").append($option);
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
	<%-- 	 $("#addBtn").click(function(){
			 $.ajax({
					url: '<%=path%>/clientquote/save',
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
			
			var check=$("input[type='checkbox']:checked");//.prop("checked");
			var certification="";
			$.each(check,function(i){
				certification=certification+$(this).val()+",";
			});
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
				
				postData[$(this).attr("name")] = $(this).val();
			});
			postData.certification=certification;
			var check =validate();
			if(check){ 
			return postData;
		}}
 }		
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="新增客户订单">
	<form id="addForm" action="">
	<input type="text" class="hide" name="clientQuoteId" value="${client_quote_id}" />
	<input type="text" class="hide" name="clientinquiryquotenumber" value="${client_inquiry_quote_number}" />
	<table>
	<tr>
			<td>询价单号</td>
			<td> ${client_inquiry_quote_number}</td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId">
						<option value="" >请选择</option>
				</select>
			</td>
		</tr>
		
		<!-- <tr>
			<td>汇率</td>
			<td><input name="exchangeRate" id="exchangeRate"  class="tc" /></td>
		</tr> -->
		<tr>
			<td>客户订单号</td>
			<td><input name="sourceNumber" id="sourceNumber"  class="tc" /></td>
		</tr>
		<tr>
			<td>紧急程度</td>
			<td>
				<select name="urgentLevelId" type="text" id="urgentLevelId">
				</select>
			</td>
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
		<!-- <tr>
			<td>运费</td>
			<td><input id="freight" name="freight"/></td>
		</tr> -->
		<tr>
			<td>订单日期</td>
			<td><input name="orderDate" id="orderDate" name="orderDate" class="tc" /></td>
		</tr>
		<tr>
			<td>L/C</td>
			<td><input name="lc" id="lc"/></td>
		</tr>
		<tr>
			<td>IMPORTERS REGISTRATION</td>
			<td><input name="importersRegistration" id="importersRegistration" /></td>
		</tr>
		<tr>
		<td>证书</td>
			<td>
			<input type="checkbox" value="OEM COC" name="certification" id="CERT80"/>OEM COC
			<input type="checkbox" value="FAA 8130-3" name="certification" id="CERT81"/>FAA 8130-3
			<input type="checkbox" value="EASA Form One" name="certification" id="CERT82"/>EASA Form One
			<input type="checkbox" value="Vendor COC" name="certification" id="CERT83"/>Vendor COC
			<input type="checkbox" value="MFR CERT" name="certification" id="CERT84"/>MFR CERT<br></br>
			<input type="checkbox" value="CAAC" name="certification" id="CERT85"/>CAAC
			<input type="checkbox" value="FAA+CAAC" name="certification" id="CERT86"/>FAA+CAAC
			<input type="checkbox" value="EASA+CAAC" name="certification" id="CERT87"/>EASA+CAAC
			<input type="checkbox" value="TCCA Form one" name="certification" id="CERT88"/>TCCA Form one
			<input type="checkbox" value="Other" name="certification" id="CERT89"/>Other
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark"></textarea></td>
		</tr>
		<tr>
			<td colspan="2">
			<!-- 	<input class="btn btn_orange" type="button" value="新增" id="addBtn"/>
				<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/> -->
			</td>
			<td></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>