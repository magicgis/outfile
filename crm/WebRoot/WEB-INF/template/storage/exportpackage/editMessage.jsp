<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改出库单</title>

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
		PJ.datepicker('exportDate');
		PJ.datepicker('realExportDate');
		
		//货币信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exportpackage/findCurrency?id='+${exportPackage.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#currencyId").append($option);
						
					}
				}else{
					
						PJ.warn("操作失误!");
				}
			}
		});
		
		//费用币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exportpackage/urgentList?id='+"${exportPackage.feeCurrencyId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					if('${exportPackage.feeCurrencyId}' == '' || '${exportPackage.feeCurrencyId}' == 'undefined'){
						var $option = $("<option></option>");
						$option.text("请选择");
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
		
		//发货方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exportpackage/findLogistics?id='+${exportPackage.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#logisticsWay").append($option);
						
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
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"exportDate",
			form:"#addForm"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,select");var nodes = def.nodeName.split(",");var flag = true;var tip;
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
			if($("#clientId").val()==""||$("#clientId").text()=="请选择"){
				$("#clientId").addClass("input-error");
					tip = $("#clientId").attr("data-tip");
					flag=false;
					return false;
			}
			if($("#currencyId").val()==""||$("#currencyId").text()=="请选择"){
				$("#currencyId").addClass("input-error");
					tip = $("#currencyId").attr("data-tip");
					flag=false;
					return false;
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="修改出库单">
	<form id="addForm" action="">
	<input name="id" id="id" value="${exportPackage.id }" class="hide"/>
	<table>
		<tr>
			<td>出库单号</td>
			<td><input name="exportNumber" id="exportNumber" class="text" value="${exportPackage.exportNumber }" disabled="disabled"/></td>
		</tr>
		<tr>
			<td>出库指令</td>
			<td><input name="exportPackageInstructionsNumber" id="exportPackageInstructionsNumber" readonly="readonly" value="${exportPackage.exportPackageInstructionsNumber }"/></td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId">
				</select>
			</td>
		</tr>
		<tr>
			<td>尺寸</td>
			<td><input name="size" id="size" value="${exportPackage.size }"/>CM</td>
		</tr>
		<tr>
			<td>重量</td>
			<td><input name="weight" id="weight" value="${exportPackage.weight }"/>KG</td>
		</tr>
		<tr>
			<td>出库费</td>
			<td><input name="exportFee" id="exportFee" value="${exportPackage.exportFee }"/></td>
		</tr>
		<tr>
			<td>运费</td>
			<td><input name="freight" id="freight" value="${exportPackage.freight }"/></td>
		</tr>
		<tr>
			<td>费用币种</td>
			<td><select name="feeCurrencyId" id="feeCurrencyId"  class="tc">
				</select>
			</td>
		</tr>
		<tr>
			<td>运输方式</td>
			<td><select id="logisticsWay" name="logisticsWay">
				</select>
			</td>
		</tr>
		<tr>
			<td>运输单号</td>
			<td><input name="awb" id="awb" value="${exportPackage.awb }"/></td>
		</tr>
		<tr>
			<td>出库日期</td>
			<td><input name="exportDate" id="exportDate" class="text" class="tc" value="<fmt:formatDate value="${exportPackage.exportDate }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>实际出库日期</td>
			<td><input name="realExportDate" id="realExportDate" class="text" class="tc" value="<fmt:formatDate value="${exportPackage.realExportDate }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark">${exportPackage.remark }</textarea></td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td><fmt:formatDate value="${exportPackage.updateTimestamp }"  pattern="yyyy-MM-dd"/></td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>