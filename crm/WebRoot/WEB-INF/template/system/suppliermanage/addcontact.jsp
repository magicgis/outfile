<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增供应商联系人信息</title>

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
		PJ.datepicker('birthday');
		PJ.datepicker('creationDate');
	});
	
	//供应商归类
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/suppliermanage/sex?id='+'${data.sexId}',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].value);
					$("#sexId").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
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
			nodeName:"name",
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
<div position="center" title="供应商信息管理">
	<form id="addForm" action="">
	<input name="id" id="id" class="hide" value="${data.id}"/>
	<input name="supplierId" id="supplierId" class="hide" value="${supplierId}"/>
	<table>
		
		<tr>
			<td>全称</td>
			<td><input name="fullName" id="fullName" class="text" value="${data.fullName}"/></td>
		</tr>
		
		<tr>
			<td>姓</td>
			<td><input name="surName" id="surName" class="text" value="${data.surName}"/></td>
		</tr>
		
		<tr>
			<td>名</td>
			<td><input name="name" id="name" class="text" value="${data.name}"/></td>
		</tr>
		
		<tr>
			<td>称谓</td>
			<td><input name="appellation" id="appellation" class="text"value="${data.appellation}"/></td>
		</tr>
		
		<tr>
			<td>性别</td>
			<td><select id="sexId" name="sexId" >
				<c:if test="${not empty data.sexId}"><option value="${data.sexId}" >${data.sexValue}</option></c:if>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>职位</td>
			<td><input name="position" id="position" class="text"value="${data.position}"/></td>
		</tr>
		
		<tr>
			<td>生日</td>
			<td><input name="birthday" id="birthday"  class="tc" value="<fmt:formatDate value="${data.birthday}"  pattern="yyyy-MM-dd"/>"></td>
				</tr>
		
		<tr>
			<td>所属部门</td>
			<td><input name="department" id="department" class="text"value="${data.department}"/></td>
		</tr>
		
		<tr>
			<td>电话</td>
			<td><input name="phone" id="phone" class="text"value="${data.phone}"/></td>
		</tr>
		
		<tr>
			<td>手机</td>
			<td><input name="mobile" id="mobile" class="text"value="${data.mobile}"/></td>
		</tr>
		
		<tr>
			<td>传真</td>
			<td><input name="fax" id="fax" class="text"value="${data.fax}"/></td>
		</tr>
		
		<tr>
			<td>邮箱</td>
			<td><input name="email" id="email" class="text"value="${data.email}"/></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="7" cols="90" name="remark" id="remark">${data.remark}</textarea></td>
		</tr>
		
	</table>
	
	</form>
</div>	
</body>
</html>