<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增供应商寄卖</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
  
span
  { color:red;}
</style> 
<script type="text/javascript">
	$(function(){
		PJ.datepicker('commissionDate');
		PJ.datepicker('validity');
		if($("#status").val() == "0"){
			var $option = $("<option></option>");
			$option.val("0").text("无效");
			$("#saleStatus").append($option);
			var $option2 = $("<option></option>");
			$option2.val("1").text("有效");
			$("#saleStatus").append($option2);
		}else{
			var $option = $("<option></option>");
			$option.val("1").text("有效");
			$("#saleStatus").append($option);
			var $option2 = $("<option></option>");
			$option2.val("0").text("无效");
			$("#saleStatus").append($option2);
		}
		
		if($("#crawlStatus").val() == "1"){
			var $option = $("<option></option>");
			$option.val("1").text("是");
			$("#crawlStorageStatus").append($option);
			var $option2 = $("<option></option>");
			$option2.val("0").text("否");
			$("#crawlStorageStatus").append($option2);
		}else{
			var $option = $("<option></option>");
			$option.val("0").text("否");
			$("#crawlStorageStatus").append($option);
			var $option2 = $("<option></option>");
			$option2.val("1").text("是");
			$("#crawlStorageStatus").append($option2);
		}
		if($("#id").val() != ""){
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/storage/assetpackage/supplierList?edit='+1+'&id='+$("#id").val(),
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].code);
							$("#supplierId").append($option);
						}
					}else{
						
							PJ.showWarn(result.msg);
					}
				}
			});
			
		}else{
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/storage/assetpackage/supplierList?edit='+0+'&id='+$("#id").val(),
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].code);
							$("#supplierId").append($option);
						}
					}else{
						
							PJ.showWarn(result.msg);
					}
				}
			});
			
		}
		
		$.ajax({
			type: "POST",
			url:'<%=path%>/storage/assetpackage/airType?airTypeId='+'${supplierCommissionForStockmarket.airTypeId }',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					if('${supplierCommissionForStockmarket.airTypeId }' == ''){
						var $option = $("<option></option>");
						$option.val("").text("全部");
						$("#airTypeId").append($option);
					}
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#airTypeId").append($option);
					}
				}
			}
		});
		
	});
	//供应商代码
	
	
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
		if($("#airTypeId").val() == ""){
			return false;
		}else{
			return true;
		}
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
<div position="center" title="新增供应商寄卖">
	<form id="addForm" action="">
	<input name="id" id="id" class="hide" value="${supplierCommissionForStockmarket.id }"/>
	<input name="status" id="status" class="hide" value="${supplierCommissionForStockmarket.saleStatus }"/>
	<input name="crawlStatus" id="crawlStatus" class="hide" value="${supplierCommissionForStockmarket.crawlStorageStatus }"/>
	<table>
		<tr>
			<td>供应商</td>
			<td>
				<select name="supplierId" id="supplierId">
				</select>
			</td>
		</tr>
		<tr>
			<td>机型</td>
			<td><select name="airTypeId" id="airTypeId">
				</select>
			</td>
		</tr>
		<tr>
			<td>Stock Market爬虫</td>
			<td><select name="crawlStorageStatus" id="crawlStorageStatus"></select></td>
		</tr>
		<tr>
			<td>状态</td>
			<td><select name="saleStatus" id="saleStatus"></select></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="7" cols="90" name="remark" id="remark" >${supplierCommissionForStockmarket.remark }</textarea></td>
		</tr>
		
	</table>
	
	</form>
</div>	
</body>
</html>