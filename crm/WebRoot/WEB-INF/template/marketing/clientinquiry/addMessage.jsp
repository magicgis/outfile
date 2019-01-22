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
		PJ.datepicker('deadline');
		PJ.datepicker('inquiryDate');
		PJ.datepicker('realDeadline');
		
		//机型
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
						$("#airtype").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//商业类型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/bizType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
						$("#biztype").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//客户及联系人
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/contacts?flag='+'add',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id+","+obj[i].clientId).text(obj[i].code+"-"+obj[i].name);
						$("#contacts").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
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
		 
		/*  $(".text").blur(function(){
			 $("#test").focus();
		 });
		 
		 $("#test").keydown(function() {
             if (event.keyCode == "13") {
            	 setTimeout(test,500);
             }
             
         }); */
	});
	
	function test(){
		var test = $("#test").val();
		var index = parseInt(test.indexOf("）"));
		test = test.substring(4,index);
		alert(test);
	}
	
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
			if($("#contacts").val()==""||$("#contacts").text()=="请选择"){
				$("#contacts").addClass("input-error");
					tip = $("#contacts").attr("data-tip");
					flag=false;
					return false;
			}
			if($("#biztype").val()==""||$("#biztype").text()=="请选择"){
				$("#biztype").addClass("input-error");
					tip = $("#biztype").attr("data-tip");
					flag=false;
					return false;
			}
			if($("#airtype").val()==""||$("#airtype").text()=="请选择"){
				$("#airtype").addClass("input-error");
					tip = $("#airtype").attr("data-tip");
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
<div position="center" title="新增客户询价">
	<form id="addForm" action="">
	<input class="hide" name="token" id="token" value="${token}"/>
	<table>
		<tr>
			<td>客户及联系人</td>
			<td><select id="contacts" name="mixId" class="text">
						<option value="">请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>商业类型</td>
			<td><select id="biztype" name="bizTypeId" class="text">
						<option value="">请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>机型</td>
			<td><select id="airtype" name="airTypeId" class="text">
						<option value="">请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>客户询价单号</td>
			<td><input name="sourceNumber" id="sourceNumber" class="text"/></td>
		</tr>
		<tr>
			<td>询价日期</td>
			<td><input name="inquiryDate" id="inquiryDate" class="text" class="tc" value="<fmt:formatDate value="${today }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>截至日期</td>
			<td><input name="deadline" id="deadline" class="text" class="tc"/></td>
		</tr>
		<tr>
			<td>实际截标时间</td>
			<td><input name="realDeadline" id="realDeadline" class="text" class="tc"/></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark"></textarea></td>
		</tr>
		<!-- <tr>
			<td><input name="deadline" id="test"/></td>
		</tr> -->
		
	</table>
	</form>
</div>	
</body>
</html>