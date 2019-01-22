<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>选择供应商</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
</style> 
<script type="text/javascript">
	
	//供应商代码
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/purchase/supplierinquiry/getSuppliers',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].name);
					$("#supplierId").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
	});
	
	//证书信息
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/system/staticprice/findCertificationForSupplier',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].code);
					$("#certificationId").append($option);
				}
			}else{
				
					PJ.warn("操作失误!");
			}
		}
	});
	
	//状态
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/system/staticprice/findConditionForSupplier',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].code);
					$("#conditionId").append($option);
				}
			}else{
				
					PJ.warn("操作失误!");
			}
		}
	});
	
	//货币信息
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/sales/clientinquiry/currencyType',
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
			nodeName:"partNumber,description,price,leadTime,unit,amount",
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
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
	
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="选择供应商">
	<form id="addForm" action="">
	<table id="messageTab" style=" border-collapse:   separate;   border-spacing:   5px;">
		<tr>
			<td>供应商</td>
			<td><select name="supplierId" id="supplierId" width="50">
				</select>
			</td>
		</tr>
		<tr>
			<td>件号</td><td><input name="partNumber" id="partNumber" value="${clientInquiryElement.partNumber }"/>
			<td>报价单号</td><td><input name="sourceNumber" id="sourceNumber" /></td>
		</tr>
		<tr>
			<td>描述</td>
			<td><input name="description" id="description" size="70" value="${clientInquiryElement.description }"/></td>
		</tr>
		<tr>
			<td>价格</td><td><input name="price" id="price"/></td>
			<td>周期</td><td><input name="leadTime" id="leadTime" /></td>
		</tr>
		<tr>
			<td>币种</td>
			<td>
				<select name="currencyId" id="currencyId">
				</select>
			</td>
			<td>单位</td>
			<td>
				<br /><input name="unit" id="unit" value="${clientInquiryElement.unit }"/>
			</td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="amount" value="${clientInquiryElement.amount }"/></td>
			<td>MOQ</td>
			<td><input name="moq" id="moq" /></td>
		</tr>
		<tr>
			<td>有效期</td>
			<td><input name="validity" id="validity" class="tc" value="<fmt:formatDate value="${validity }"  pattern="yyyy-MM-dd"/>"/></td>
			<td>证书</td>
			<td>
				<select name="certificationId" id="certificationId">
				</select>
				
			</td>
		</tr>
		<tr>
			<td>状态</td>
			<td>
				<select name="conditionId" id="conditionId">
				</select>
			</td>
			<td>WARRANTY</td>
			<td><input name="warranty" id="warranty"/></td>
		</tr>
		<tr>
			<td>提货换单费</td>
			<td><input name="feeForExchangeBill" id="feeForExchangeBill"/></td>
			<td>银行费</td>
			<td><input name="bankCost" id="bankCost"/></td>
		</tr>
			<td>SN</td>
			<td><input name="serialNumber" id="serialNumber"/></td>
			<td>TAG DATE</td>
			<td><input name="tagDate" id="tagDate"/></td>
		</tr>
		</tr>
			<td>TAG SOURCE</td>
			<td><input name="tagSrc" id="tagSrc"/></td>
			<td>CORE C</td>
			<td><input name="coreCharge" id="coreCharge"/></td>
		</tr>
		</tr>
			<td>Location</td>
			<td><input name="location" id="location" /></td>
			<td>trace</td>
			<td><input name="trace" id="trace" /></td>
		</tr>
		</tr>
			<td>备注</td>
			<td><input name="remark" id="remark" size="70"/></td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>