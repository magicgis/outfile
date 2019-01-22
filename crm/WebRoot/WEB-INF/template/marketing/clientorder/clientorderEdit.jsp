<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改客户订单</title>

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
		PJ.datepicker('orderDate');
		
		/* var rate = $("#exchangeRate").val().substr(0, 3);
		$("#exchangeRate").val(rate); */
		//证书
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientorder/certification?certification='+'${clientOrderVo.certification}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						//2017-01-20 修改复选框有部分没有勾选上
						var val=obj[i].code;
						if(obj[i].code=='FAA CAAC'){
							val="FAA+CAAC";
						}if(obj[i].code=='EASA CAAC'){
							val="EASA+CAAC";
						}
						$("input[name='certification'][value='"+val+"']").attr("checked", true);  
						/* if(obj[i].code=='OEM COC'){
							$("#OEMCOC").attr("checked", true);  
						}if(obj[i].code=='FAA 8130-3'){
							$("#FAA8130-3").attr("checked", true);  
						}if(obj[i].code=='EASA Form One'){
							$("#EASAFormOne").attr("checked", true);  
						}if(obj[i].code=='Vendor COC'){
							$("#VendorCOC").attr("checked", true);  
						}if(obj[i].code=='MFR CERT'){
							$("#MFRCERT").attr("checked", true);  
						}if(obj[i].code=='CAAC'){
							$("#CAAC").attr("checked", true);  
						}if(obj[i].code=='FAA+CAAC'){
							$("#FAA+CAAC").attr("checked", true);  
						}if(obj[i].code=='EASA+CAAC'){
							$("#EASA+CAAC").attr("checked", true);  
						}if(obj[i].code=='TCCA Form one'){
							$("#TCCAFormone").attr("checked", true);  
						}if(obj[i].code=='Other'){
							$("#Other").attr("checked", true);  
						} */
					/* 	var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#code").append($option); */
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientorder/findCurrency?id='+${clientOrderVo.id},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#bz").append($option);
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
			url:'<%=path%>/sales/clientorder/zt?id='+${clientOrderVo.id},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#zt").append($option);
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
			url:'<%=path%>/market/clientweatherorder/urgentList?id='+'${clientOrderVo.urgentLevelId}',
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
			return postData;
		} 
	}	
	
	 
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="新增客户询价">
	<form id="addForm" action="">
	<input name="id" id="id" value="${clientOrderVo.id }" class="hide"/>
	<table>
		<tr>
			<td>订单号</td>
			<td>${clientOrderVo.orderNumber }</td>
		</tr>
		<tr>
			<td>币种</td>
			<td>
				<select id="bz" name="currencyId" >
				</select>
			</td>
		</tr>
		<%-- <tr>
			<td>汇率</td>
			<td><input type="text" name="exchangeRate" id="exchangeRate" value="${clientOrderVo.exchangeRate }" /></td>
		</tr> --%>
		<tr>
			<td>订单日期</td>
			<td><input name="orderDate" type="text" class="tc" id="orderDate" value="<fmt:formatDate value="${clientOrderVo.orderDate }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>客户订单号</td>
			<td><input name="sourceNumber" type="text" id="sourceNumber" value="${clientOrderVo.sourceOrderNumber }"/></td>
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
			<td><input name="prepayRate" type="text" id="prepayRate" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${clientOrderVo.prepayRate }"/>"/>%</td>
		</tr>
		<tr>
			<td>发货时支付比例</td>
			<td><input type="text" name="shipPayRate" id="shipPayRate" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${clientOrderVo.shipPayRate }"/>"/>%</td>
		</tr>
		<tr>
			<td>发货帐期</td>
			<td><input type="text" name="shipPayPeriod" id="shipPayPeriod" value="${clientOrderVo.shipPayPeriod }"/></td>
		</tr>
		<tr>
			<td>验货时支付比例</td>
			<td><input type="text" name="receivePayRate" id="receivePayRate" value="<fmt:formatNumber type="number" 
            maxFractionDigits="3" value="${clientOrderVo.receivePayRate }"/>"/>%</td>
		</tr>
		<tr>
			<td>验货账期</td>
			<td><input type="text" name="receivePayPeriod" id="receivePayPeriod" value="${clientOrderVo.receivePayPeriod }"/></td>
		</tr>
		<tr>
			<td>邮费</td>
			<td><input type="text" name="freight" id="freight" value="${clientOrderVo.freight }"/></td>
		</tr>
		<tr>
			<td>L/C</td>
			<td><input name="lc" id="lc" value="${clientOrderVo.lc }"/></td>
		</tr>
		<tr>
			<td>IMPORTERS REGISTRATION</td>
			<td><input name="importersRegistration" id="importersRegistration"  value="${clientOrderVo.importersRegistration }"/></td>
		</tr>
		<tr>
		<td>证书</td>
			<td>
			<input type="checkbox" value="OEM COC" name="certification" id="OEMCOC"/>OEM COC
			<input type="checkbox" value="FAA 8130-3" name="certification" id="FAA8130-3"/>FAA 8130-3
			<input type="checkbox" value="EASA Form One" name="certification" id="EASAFormOne"/>EASA Form One
			<input type="checkbox" value="Vendor COC" name="certification" id="VendorCOC"/>Vendor COC
			<input type="checkbox" value="MFR CERT" name="certification" id="MFRCERT"/>MFR CERT<br></br>
			<input type="checkbox" value="CAAC" name="certification" id="CAAC"/>CAAC
			<input type="checkbox" value="FAA+CAAC" name="certification" id="FAA+CAAC"/>FAA+CAAC
			<input type="checkbox" value="EASA+CAAC" name="certification" id="EASA+CAAC"/>EASA+CAAC
			<input type="checkbox" value="TCCA Form one" name="certification" id="TCCAFormone"/>TCCA Form one
			<input type="checkbox" value="Other" name="certification" id="Other"/>Other
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10"  cols="90" name="remark">${clientOrderVo.remark }</textarea></td>
		</tr>
		<tr>
			<td>状态</td>
			<td>
				<select id="zt" name="orderStatusId" >
				</select>
			</td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td><fmt:formatDate value="${clientOrderVo.updateTimestamp }"  pattern="yyyy-MM-dd hh:mm:ss"/></td>
		</tr>
		<!-- <tr>
			<td colspan="2">
				<input class="btn btn_orange" type="button" value="新增" id="addBtn" name="addBtn"/>
				<input class="btn btn_blue" type="button" value="重置" id="resetBtn" name="resetBtn"/>
			</td>
			<td></td>
		</tr> -->
		
	</table>
	</form>
</div>	
</body>
</html>