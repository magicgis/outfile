<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增出库单</title>

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
function validate(){ 
	return validate2({
		nodeName:"ecportpackageInstructionsNumber",
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
	});
	return flag;
};

	$(function(){

		//校验code是否已存在
		$("#ecportpackageInstructionsNumber").blur(function(){
			var code = $("#ecportpackageInstructionsNumber").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/storage/exportpackage/testCode?code='+code,
				success:function(result){
					if(result.success){
						$("#msg").html("指令号已存在!");
					}else{
						$("#msg").html(" ");
					}
				}
			});
		});
	});
	
	//获取表单数据
	function getFormData(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};

		if($("#msg").html()=='指令号已存在!'){
			PJ.error('请输入正确的指令号');
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
<div position="center" title="新增客户询价">
	<form id="addForm" action="">
	<input type="text" class="hide" id="clientCode" name="clientCode" value="${clientCode}"/>
	<input type="text" class="hide" id="tax" name="tax" value="${tax}"/>
	<input type="text" class="hide" id="supplierCode" name="supplierCode" value="${supplierCode}"/>
	<input type="text" class="hide" id="location" name="location" value="${location}"/>
	<input type="text" class="hide" id="partNumber" name="partNumber" value="${partNumber}"/>
	<input type="text" class="hide" id="importDateStart" name="importDateStart" value="${importDateStart}"/>
	<input type="text" class="hide" id="importDateEnd" name="importDateEnd" value="${importDateEnd}"/>
	<input type="text" class="hide" id="complianceCertificate" name="complianceCertificate" value="${complianceCertificate}"/>
	<input type="text" class="hide" id="completeComplianceCertificate" name="completeComplianceCertificate" value="${completeComplianceCertificate}"/>
	<input type="text" class="hide" id="exportpackage" name="exportpackage" value="${exportpackage}"/>
	<table>
		<tr>
			<td>客户</td>
			<td>
				<input type="text" class="clientCode" id="clientCode" name="clientCode" value="${clientCode}" readonly="readonly"/>
			</span></td>
		</tr>
		<tr>
			<td>出库指令单号</td>
			<td>
				<input type="text" class="ecportpackageInstructionsNumber" id="ecportpackageInstructionsNumber" name="ecportpackageInstructionsNumber" value="${ecportpackageInstructionsNumber}" />
			<span id="msg"></span></td>
		</tr>
		<tr>
			<td>生成时间</td>
			<td><input type="text" class="createDate" id="createDate" readonly="readonly" value="<fmt:formatDate value="${createDate}"  pattern="yyyy-MM-dd"/>" name="createDate"/>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark"></textarea></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>