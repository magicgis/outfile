<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改客户询价</title>

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
						$option.val(obj[i].id).text(obj[i].code);
						$("#biztype").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/zt?id='+${clientInquiry.id},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#zt").append($option);
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
			url:'<%=path%>/sales/clientinquiry/contacts?clientContactId='+${clientInquiry.clientContactId}+'&clientId='+${clientInquiry.clientId}
			+'&flag='+'edit',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].name);
						$("#contacts").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//商业模式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/changeBiz?bizTypeId='+${clientInquiry.bizTypeId}
			+'&flag='+'edit',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#bizTypeId").append($option);
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
							} else {
								PJ.error(result.message);
							}
						
					}
				});
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
<div position="center" title="新增客户询价">
	<form id="addForm" action="">
	<input name="id" id="id" value="${clientInquiry.id }" class="hide"/>
	<table>
		<tr>
			<td>询价单号</td>
			<td>${clientInquiry.quoteNumber }</td>
		</tr>
		<tr>
			<td>状态</td>
			<td>
				<select id="zt" name="inquiryStatusId" >
				</select>
			</td>
		</tr>
		<tr>
			<td>联系人</td>
			<td><select id="contacts" name="clientContactId" >	
				</select>
			</td>
		</tr>
		<tr>
			<td>商业模式</td>
			<td><select id="bizTypeId" name="bizTypeId" >	
				</select>
			</td>
		</tr>
		<tr>
			<td>客户询价单号</td>
			<td><input name="sourceNumber" type="text" id="sourceNumber" value="${clientInquiry.sourceNumber }"/></td>
		</tr>
		<tr>
			<td>询价日期</td>
			<td><input name="inquiryDate" type="text" id="inquiryDate" class="text" class="tc" value="<fmt:formatDate value="${clientInquiry.inquiryDate }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>截至日期</td>
			<td><input name="deadline" type="text" id="deadline" class="text" class="tc" value="<fmt:formatDate value="${clientInquiry.deadline }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<tr>
			<td>截至日期</td>
			<td><input name="realDeadline" type="text" id="realDeadline" class="text" class="tc" value="<fmt:formatDate value="${clientInquiry.realDeadline }"  pattern="yyyy-MM-dd"/>"/></td>
		</tr>
		<%-- <tr>
			<td>Terms</td>
			<td><input name="terms" type="text" id="terms" value="${clientInquiry.terms }"/></td>
		</tr> --%>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark">${clientInquiry.remark }</textarea></td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td><fmt:formatDate value="${clientInquiry.updateTimestamp }"  pattern="yyyy-MM-dd hh:mm:ss"/></td>
		</tr>
		<!-- <tr>
			<td colspan="2">
				<input class="btn btn_orange" type="button" value="新增" id="addBtn" name="addBtn"/>
				<input class="btn btn_blue" type="button" value="重置" id="resetBtn" name="resetBtn"/>
			</td>
			<td></td>
		</tr> -->
		
	</table>
	</form>
</div>	
</body>
</html>