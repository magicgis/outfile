<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增付款</title>

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
		nodeName:"payDate,paySum",
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
		PJ.datepicker('payDate');
		
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
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#addForm")[0].reset();
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
<div position="center" title="新增付款">
	<form id="addForm" action="">
	<input type="text" class="hide" name="importPackageId" value="${id}" />
	<input type="text" class="hide" name="clientinquiryquotenumber" value="${client_inquiry_quote_number}" />
	<table>
	<tr>
			<td>入库单号</td>
			<td>${importNumber}</td>
		</tr>
	
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId">
						<option value="" >请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>付款日期</td>
			<td><input name="payDate" id="payDate"  class="tc" ></td>
		</tr>
		
		<tr>
			<td>付款额</td>
			<td><input name="paySum" id="paySum"  class="tc" /></td>
		</tr>
		
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark"></textarea></td>
		</tr>
		<tr>
			<td colspan="2">
			</td>
			<td></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>