<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增用户</title>

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
		//客户代码来源
		$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/sales/clientinquiry/clientCode',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].code);
							$("#clientId").append($option);
						}
					}else{
						
							PJ.showWarn(result.msg);
					}
				}
			});
		});
	
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
					$option.val(obj[i].id).text(obj[i].code);
					$("#supplierId").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
	});
	
	//机型代码
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/sales/clientinquiry/airType',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
					$("#airTypeId").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
	});
	
	//机型代码
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/system/storehouseaddress/getAll',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				for(var i in obj){
					var $option = $("<option></option>");
					$option.val(obj[i].id).text(obj[i].name);
					$("#storehouseAddressId").append($option);
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
		return (validate2({
			nodeName:"supplierId",
			form:"#addForm"
		}) || validate2({
			nodeName:"userId",
			form:"#addForm"
		}));
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
<div position="center" title="新增权限">
	<form id="addForm" action="">
	<input class="hide" id="userId" name="userId" value="${id }"/>
	<table>
		<tr>
			<td>供应商</td>
			<td>
				<select id="supplierId" name="supplierId">
					<option value="">无</option>
				</select>
			</td>
		</tr>
		<tr>	
			<td>客户</td>
			<td>
				<select id="clientId" name="clientId">
					<option value="">无</option>
				</select>
			</td>
		</tr>
		<tr>	
			<td>机型</td>
			<td>
				<select id="airTypeId" name="airTypeId">
					<option value="">无</option>
				</select>
			</td>
		</tr>
		<tr>	
			<td>仓库</td>
			<td>
				<select id="storehouseAddressId" name="storehouseAddressId">
					<option value="">无</option>
				</select>
			</td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>