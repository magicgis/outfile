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
	
	$(function(){
		$("#location2").click(function(){
	 		$("#location").val($(this).val());
	 	});
	});
	
	//位置
	$.ajax({
		type: "POST",
		dataType: "json",
		url:'<%=path%>/importpackage/getAllLocation',
		success:function(result){
			var obj = eval(result.message)[0];
			if(result.success){
				$("#location2").empty();
				for(var i in obj){
					var $option = $("<option></option>");
					if(""==obj[i].code){
						 $option.val(obj[i].location).text(obj[i].location+" - "+"空闲");
					}
					else{
						 $option.val(obj[i].location).text(obj[i].location+" - "+obj[i].code);
					}
					 $("#location2").append($option); 
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
	
	function validate(){ 
		return validate2({
			nodeName:"location",
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
	
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="选择库位">
	<form id="addForm" action="">
	<table>
		<tr>
			<td>库位</td>
			<td>
				<input id="location" name="location"/>
			</td>
			<td>
				<select id="location2" name="location2">
				</select>
			</td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>