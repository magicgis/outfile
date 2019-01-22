<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增竞争对手</title>

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
		//校验code是否已存在
		$("#code").blur(function(){
			var code = $("#code").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/system/competitormanage/check?code='+code,
				success:function(result){
					if(result.success){
						$("#msg").html("代码已存在!");
					}else{
						$("#msg").html(" ");
					}
				}
			});
		});
		
		//货币
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/competitormanage/currency?edit=0',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#currencyId").append($option);
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
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
					
				postData[$(this).attr("name")] = $(this).val();
			});
			if($("#taxReturn").is(':checked')==true){
				postData.taxReturn=1;
			}else{
				postData.taxReturn=0;
			}
			return postData;
	}
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"code,name",
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
<div position="center" title="新增竞争对手">
	<form id="addForm" action="">
	<table>
		<tr>
			<td>代码</td>
			<td><input name="code" id="code" class="text"/><span id="msg"></span></td>
		</tr>
		<tr>
			<td>名称</td>
			<td><input name="name" id="name" class="text"/></td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId">
				</select>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="7" cols="90" name="remark" id="remark"></textarea></td>
		</tr>
		
	</table>
	
	</form>
</div>	
</body>
</html>