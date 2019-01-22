<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增客户询价</title>

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
		PJ.datepicker('expireDate1');
		PJ.datepicker('expireDate2');
		PJ.datepicker('expireDate3');
		
		//机型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/getAptitudes',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						
						var $option = $("<option></option>");
						$option.val(obj[i].value).text(obj[i].value);
						$("#aptitude1").append($option);
						var $option2 = $("<option></option>");
						$option2.val(obj[i].value).text(obj[i].value);
						$("#aptitude2").append($option2);
						var $option3 = $("<option></option>");
						$option3.val(obj[i].value).text(obj[i].value);
						$("#aptitude3").append($option3);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		$("#aptitude1").click(function(){
			$("#name1").val($(this).val());
	 	});
		
		$("#aptitude2").click(function(){
	 		$("#name2").val($(this).val());
	 	});
		
		$("#aptitude3").click(function(){
	 		$("#name3").val($(this).val());
	 	});
		
		
		 <%-- //重置条件
		 $("#resetBtn").click(function(){
			 $("#addForm")[0].reset();
		 });
		 
		 //新增按键事件
		 $("#addBtn").click(function(){
    		 var postData=getFormData()	 				
			 $.ajax({
					url: '<%=path%>/sales/clientinquiry/add',
					data: postData,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
								PJ.
							} else {
								PJ.error(result.message);
							}		
					}
				});
		 }); --%>
		 
	});
	
	function test(){
		var test = $("#test").val();
		var index = parseInt(test.indexOf("）"));
		test = test.substring(4,index);
		alert(test);
	}
	
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
			$inputs.each(function(){
				if($(this).val()!=""){
					obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
				}
				
			});
			index++;
		});
		
		return obj;
	}
	
	//获取表单数据
	function getFormData(){
		var postData = getTableDate();
		postData.supplierId = ${supplierId}
		return postData;
	}
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"mixId,bizTypeId,airTypeId,sourceNumber,inquiryDate",
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
<div position="center" title="">
	<form id="addForm" action="">
	<input class="hide" name="supplierId" id="supplierId" value="${supplierId }"/>
	<table id="elementTable">
		<tr>
			<td>资质</td>
			<td><input name="name" id="name1" class="text"/></td>
			<td>资质选择</td>
			<td><select id="aptitude1" name="aptitude1" class="text">
						<option value="">请选择</option>
				</select>
			</td>
			<td>过期日期</td>
			<td><input name="expireDate" id="expireDate1" class="text" class="tc" /></td>
		</tr>
		<tr>
			<td>资质</td>
			<td><input name="name" id="name2" class="text"/></td>
			<td>资质选择</td>
			<td><select id="aptitude2" name="aptitude2" class="text">
						<option value="">请选择</option>
				</select>
			</td>
			<td>过期日期</td>
			<td><input name="expireDate" id="expireDate2" class="text" class="tc" /></td>
		</tr>
		<tr>
			<td>资质</td>
			<td><input name="name" id="name3" class="text"/></td>
			<td>资质选择</td>
			<td><select id="aptitude3" name="aptitude3" class="text">
						<option value="">请选择</option>
				</select>
			</td>
			<td>过期日期</td>
			<td><input name="expireDate" id="expireDate3" class="text" class="tc" /></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>