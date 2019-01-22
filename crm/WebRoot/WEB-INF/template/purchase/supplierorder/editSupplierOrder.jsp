<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改供应商订单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   15px;   } 
</style> 
<script type="text/javascript">
	$(function(){
		PJ.datepicker('orderDate');
		
		//状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/purchase/supplierorder/status?id='+${supplierOrderManageVo.id},
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
		
		//货币信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/purchase/supplierorder/findCurrency?id='+${supplierOrderManageVo.id},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#currency").append($option);
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
			url:'<%=path%>/market/clientweatherorder/urgentList?id='+'${supplierOrderManageVo.urgentLevelId}',
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
		var $input = $("#editForm").find("input,textarea,select");
		var postData = {};
		var prepayRate = $("#prepayRate").val();
		var shipPayRate = $("#shipPayRate").val();
		var receivePayRate = $("#receivePayRate").val();
		var total = parseInt(prepayRate)+parseInt(shipPayRate)+parseInt(receivePayRate);
		if(total!=100){
			PJ.warn("预付比例有误！请核实！");
			return false;
		}
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
<div position="center" title="修改供应商订单">
	<form id="editForm" action="">
	<input name="id" id="id" value="${supplierOrderManageVo.id }" class="hide"/>
	<table>
		<tr>
			<td>订单号</td>
			<td>${supplierOrderManageVo.clientOrderNumber }</td>
		</tr>
		<tr>
			<td>供应商订单号</td>
			<td>${supplierOrderManageVo.supplierOrderNumber }</td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currency" name="currencyId" >	
				</select>
			</td>
		</tr>
		<%-- <tr>
			<td>汇率</td>
			<td><input name="exchangeRate" type="text" id="exchangeRate" value="${supplierOrderManageVo.exchangeRate }"/></td>
		</tr> --%>
		<tr>
			<td>订单日期</td>
			<td><input name="orderDate" type="text" id="orderDate" class="tc" value="<fmt:formatDate value="${supplierOrderManageVo.orderDate }"  pattern="yyyy-MM-dd"/>"/></td>
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
			<td><input type="text" name="prepayRate" id="prepayRate" value="<fmt:formatNumber value="${supplierOrderManageVo.prepayRate}" pattern="0"/>"/>%</td>
		</tr>
		<tr>
			<td>发货前付款比例</td>
			<td><input type="text" name="shipPayRate" id="shipPayRate" value="<fmt:formatNumber value="${supplierOrderManageVo.shipPayRate}" pattern="0"/>"/>%</td>
		</tr>
		<tr>
			<td>验收完限期内付款比例</td>
			<td><input type="text" name="receivePayRate" id="receivePayRate" value="<fmt:formatNumber value="${supplierOrderManageVo.receivePayRate}" pattern="0"/>"/>%</td>
		</tr>
		<tr>
			<td>验收完限期内付款账期</td>
			<td><input type="text" name="receivePayPeriod" id="receivePayPeriod" value="<fmt:formatNumber value="${supplierOrderManageVo.receivePayPeriod}" pattern="0"/>"/></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark">${supplierOrderManageVo.remark }</textarea></td>
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
			<td><fmt:formatDate value="${supplierOrderManageVo.updateTimestamp }"  pattern="yyyy-MM-dd hh:mm:ss"/></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>