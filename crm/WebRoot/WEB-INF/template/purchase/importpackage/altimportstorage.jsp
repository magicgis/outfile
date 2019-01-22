<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>入库转库存</title>

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
		nodeName:"amount,location",
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
		PJ.datepicker('certificationDate');
		
		$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/importpackage/location?clientId='
						+'${clientId}',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						$("#location2").empty();
						for(var i in obj){
							var $option = $("<option></option>");
							if(""==obj[i].code){
								 $option.val(obj[i].location).text(obj[i].location+" - "+"空闲");
							}
						//	 else if("新建"==obj[i].code){
						//		 $option.val(obj[i].location).text(obj[i].location+" - "+obj[i].code);
						//	} 
							else{
								 $option.val(obj[i].location).text(obj[i].location+" - "+obj[i].code);
							}
							 $("#location2").append($option); 
							
							$("#location").val("");
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
			url:'<%=path%>/supplierquote/findcond?conditionId='+"${importPackageElement.conditionId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code+" - "+obj[i].value);
						$("#conditionId").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
	 	//证书
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/supplierquote/findcert?certificationId='+"${importPackageElement.certificationId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code+" - "+obj[i].value);
						$("#certificationId").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
	 	
		
	 	//符合性证明
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/importpackage/complianceCertificate?id='+"${importPackageElement.complianceCertificate}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#complianceCertificate").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		}); 
	 	
	 	//是否完成符合性证明
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/taxReimbursementList?id='+'${importPackageElement.completeComplianceCertificate}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#completeComplianceCertificate").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
	 	
		$("#location2").click(function(){
	 		$("#location").val($(this).val());
	 	});
	 	
	 	
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#addForm")[0].reset();
		 });
		 
	});
	
	//获取表单数据
	 function getData(){
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
<div position="center" title="">
	<form id="addForm" action="">
	<input type="text" class="hide" name="supplierOrderElementId" id="supplierOrderElementId" value="">
	<input type="text" class="hide" name="id" id="id" value="${importPackageElement.id}">
	<input type="text" class="hide" name="type" id="type" value="${type}">
	<table>
	<tr>
	<td>
	关联订单号：</td><td>${supplierOrderNumber}</td>
	
	</tr>
	<tr>
	<td>
	关联件号：</td><td>${quotePartNumber}
	</td>
	</tr>
	<tr>
			<td>入库单号</td>
			<td>${importPackageElement.importNumber}</td>
		</tr>
		
		<tr>
			<td>件号</td>
			<td><input name="partNumber" id="quotePartNumber" class="text"  class="tc"  value="<c:out value='${importPackageElement.partNumber}'/>"<c:if test="${not empty type}">disabled="disabled"</c:if>/><span id="partmsg"></span></td>
		</tr>
		<tr>
			<td>描述</td>
			<td><input name="description" id="quoteDescription" class="text"  class="tc" value="${importPackageElement.description}"<c:if test="${not empty type}">disabled="disabled"</c:if>/></td>
		</tr>
		<tr>
			<td>单位</td>
			<td><input name="unit" id="quoteUnit"  class="text" class="tc" value="${importPackageElement.unit}"/></td>
		</tr>
		<tr>
			<td>数量</td>
			<td><input name="amount" id="supplierOrderAmount"  class="tc" value="<fmt:formatNumber type="number" maxFractionDigits="3" value="${importPackageElement.amount}"/>"/></td>
		</tr>
		<tr>
			<td>单价</td>
			<td><input name="price" id="supplierOrderPrice" class="text"  class="tc" value="${importPackageElement.price}"/></td>
		</tr>
		<tr>
			<td>状态</td>
			<td><select id="conditionId" name="conditionId" class="text" >
				<c:if test="${not empty importPackageElement.conditionId}"><option value="${importPackageElement.conditionId}" >${importPackageElement.conditionCode} - ${importPackageElement.conditionValue}</option></c:if>
				</select></td>
		</tr>
		<tr>
			<td>证书</td>
			<td><select id="certificationId" name="certificationId" class="text" >
				<c:if test="${not empty importPackageElement.certificationId}"><option value="${importPackageElement.certificationId}" >${importPackageElement.certificationCode} - ${importPackageElement.certificationValue}</option></c:if>
				</select></td>
		</tr>
		<tr>
			<td>符合性证明</td>
			<td><select id="complianceCertificate" name="complianceCertificate" class="text" >
				</select>
			</td>
		</tr>
		<tr>
			<td>是否完成符合性证明</td>
			<td><select id="completeComplianceCertificate" name="completeComplianceCertificate" class="text" >
				<c:if test="${not empty importPackageElement.completeComplianceCertificateValue}"><option value="${importPackageElement.completeComplianceCertificate}" >${importPackageElement.completeComplianceCertificateValue}</option></c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td>位置</td>
			<td><input name="location" id="location" class="text" class="tc" value="${importPackageElement.location}"/>
			 <select id="location2" name="location2"  class="text">
			<c:if test="${not empty importPackageElement.lLocation}"><option value="${importPackageElement.lLocation}" >${importPackageElement.lLocation} </option></c:if>
			</select>
			<span id="locationmsg"></span><%-- --%>
			</td>
			</tr>
			<tr>
			<td>重量</td>
			<td><input name="boxWeight" id="boxWeight"  class="text" class="tc" value="${importPackageElement.boxWeight}"/>g</td>
		</tr>
		<tr>
			<td>溯源号</td>
			<td><input name="originalNumber" id="originalNumber"  class="text" class="tc" value="${importPackageElement.originalNumber}"/></td>
		</tr>
		<tr>
			<td>批次号</td>
			<td><input name="batchNumber" id="batchNumber"  class="text" class="tc" value="${importPackageElement.batchNumber}"/></td>
		</tr>
		<tr>
			<td>系列号</td>
			<td><input name="serialNumber" id="serialNumber"  class="text" class="tc" value="${importPackageElement.serialNumber}"/></td>
		</tr>
		<tr>
			<td>生产日期</td>
			<td><input name="manufactureDate" id="manufactureDate"  class="text" class="tc" value="<fmt:formatDate value="${importPackageElement.manufactureDate}"  pattern="yyyy-MM-dd"/>"></td>
		</tr>
		<tr>
			<td>检验日期</td>
			 <td><input name="inspectionDate" id="inspectionDate"  class="text" class="tc" value="<fmt:formatDate value="${importPackageElement.inspectionDate}"  pattern="yyyy-MM-dd"/>"></td>
			</tr>
		<tr>
			<td>备注</td>
			<td><textarea id="remark" name="remark" rows="8" cols="50"  class="text">${importPackageElement.remark}</textarea> </td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>