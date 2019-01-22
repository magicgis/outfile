<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增入库单明细</title>

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
		nodeName:"partNumber,unit,amount,price,location,originalNumber",
		form:"#addForm"
	});
}

function validateLife(){
	var expireDate = $("#expireDate").val();
	var manufactureDate = $("#manufactureDate").val();
	var inspectionDate = $("#inspectionDate").val();
	var hasLife = $("#hasLife").val();
	if(hasLife == "1"){
		if(manufactureDate != "" || inspectionDate != ""){
			return true;
		}else{
			return false;
		}
	}
	return true;
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
		PJ.datepicker('manufactureDate');
		PJ.datepicker('inspectionDate');
		PJ.datepicker('expireDate');
		if('${importPackageElement.hasLife}' == ''){
			var $option1 = $("<option></option>");
			$option1.val(0).text("否");
			$("#hasLife").append($option1); 
			var $option2 = $("<option></option>");
			$option2.val(1).text("是");
			$("#hasLife").append($option2); 
		}else{
			if('${importPackageElement.hasLife}' == '1'){
				var $option2 = $("<option></option>");
				$option2.val(1).text("是");
				$("#hasLife").append($option2);
				var $option1 = $("<option></option>");
				$option1.val(0).text("否");
				$("#hasLife").append($option1); 
			}else{
				var $option1 = $("<option></option>");
				$option1.val(0).text("否");
				$("#hasLife").append($option1); 
				var $option2 = $("<option></option>");
				$option2.val(1).text("是");
				$("#hasLife").append($option2); 
			}
		}
			$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/importpackage/location?clientId='
							+'${clientId}'+'&clientOrderNumber='+'${clientOrderNumber}',
					success:function(result){
						var obj = eval(result.message)[0];
						if(result.success){
							$("#location2").empty();
							for(var i in obj){
								var $option = $("<option></option>");
								if(""==obj[i].code){
									 $option.val(obj[i].location).text(obj[i].location+" - "+"空闲");
								}
								 /* else if("新建"==obj[i].code){
									 $option.val(obj[i].location).text(obj[i].location+" - "+obj[i].code);
								}  */
								else{
									 $option.val(obj[i].location).text(obj[i].location+" - "+obj[i].code);
								}
								 $("#location2").append($option); 
								
								/* $("#location").val(obj[0].location); */
							}
						}else{
							
								PJ.showWarn(result.msg);
						}
						
					}
				}); 
		
		
		//位置
	<%--  	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/importpackage/locationList?id='+"${importPackageElement.locationId}",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].location);
						$("#location2").append($option);
					}
				}else{
						PJ.showWarn(result.msg);
				}
			}
		});  --%>
		
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
	 	
	 	$("#location2").click(function(){
	 		$("#location").val($(this).val());
	 	});
	 	
	 	 
	 	
	 	 $("#r1").click(function(){
	 		var url = '<%=path%>/importpackage/supplierorderlement?supplierId='+'${supplierId}';
		
	 		 
	 		var iframeId = 'supplierorderFrame';
	 		
	 		var btnArr=[];
	 		
	 		var rkCancel={text:"入库",onclick:function(item,dialog){
	 			 var postData=top.window.frames[iframeId].getFormData();
 				 var clientOrderNumber=postData.clientOrderNumber;
 				 var clientId=postData.clientId;
 				 var conditionId=postData.conditionId;
 				 var certificationId=postData.certificationId;
 				 $("#elementId").val(postData.elementId);
 				 $("#clientCode").val(postData.clientCode);
 				 $("#clientId").val(postData.clientId);
 				 $("#soeid").val(postData.id);
 				 $("#clientOrderElementId").val(postData.clientOrderElementId);
 				$("#onPassageStatus").val(postData.onPassageStatus);
 				$.ajax({
 					type: "POST",
 					dataType: "json",
 					url:'<%=path%>/importpackage/location?clientId='
 							+clientId+'&clientOrderNumber='+clientOrderNumber,
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
 					url:'<%=path%>/importpackage/findcond?conditionId='+conditionId,
 					success:function(result){
 						var obj = eval(result.message)[0];
 						if(result.success){
 							$("#conditionId").empty();
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
 					url:'<%=path%>/importpackage/findcert?certificationId='+certificationId,
 					success:function(result){
 						var obj = eval(result.message)[0];
 						if(result.success){
 							$("#certificationId").empty();
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
							
						 var th=postData.th;
						 $("#quotePartNumber").val(postData.quotePartNumber);
						 if(postData.orderDescription != ""){
							 $("#quoteDescription").val(postData.orderDescription) ;
							 $("#printDescription").val(postData.orderDescription) ;
						 }else{
							 $("#quoteDescription").val(postData.quoteDescription) ;
							 $("#printDescription").val(postData.quoteDescription) ;
						 }
						 
						 $("#description").val(postData.quoteDescription) ;
						 $("#quoteUnit").val(postData.quoteUnit);
						 $("#shelfLife").val(postData.shelfLife);
						 $("#supplierOrderElementId").val(postData.id);
						 var importAmount=postData.importAmount;
						 var supplierOrderAmount=postData.supplierOrderAmount;
						 var clientOrderAmount=postData.clientOrderAmount;
						 var countImportAmount=postData.countImportAmount;
						 var clientOrderImportAmount = postData.clientOrderImportAmount;
						 if(importAmount==supplierOrderAmount){
							 PJ.warn("入库完毕");
							 return;
						 }else if(parseFloat(countImportAmount)>=parseFloat(clientOrderAmount)){
							 if(parseFloat(clientOrderImportAmount)>=parseFloat(clientOrderAmount)){
								 $("#r2").attr("checked","checked");
								 $("#quoteDescription").val( $("#description").val()) ;
								 $("#printDescription").val( $("#description").val()) ;
								 $("#supplierOrderElementId").val(0);
								 if(postData.surplusAmount==supplierOrderAmount){
									 $("#a").val(postData.orderAmount-countImportAmount);
								 }else{
									 $("#a").val(supplierOrderAmount-importAmount);
								 }
							 }else{
								 $("#a").val(supplierOrderAmount-importAmount);
							 }
							 
						 }
						 else if(parseFloat(clientOrderAmount)==parseFloat(supplierOrderAmount)){
							 $("#a").val(supplierOrderAmount-importAmount);
						 }else if(parseFloat(supplierOrderAmount)>parseFloat(clientOrderAmount)){
							 $("#a").val(clientOrderAmount-countImportAmount);
						 }else if(parseFloat(supplierOrderAmount)<parseFloat(clientOrderAmount)){
								if(postData.surplusAmount==supplierOrderAmount&&parseFloat(postData.orderAmount)>parseFloat(clientOrderAmount)){
									 $("#a").val(clientOrderAmount-countImportAmount);
								}else{
							 		$("#a").val(supplierOrderAmount-importAmount);
								}
						 }
						 
						 $("#supplierOrderPrice").val(postData.supplierOrderPrice);
						
						 
						 $("#supplierOrderNumber").val(postData.supplierOrderNumber);
					
						$("#printAmount").val($("#a").val());
						$("#printPartNumber").val(postData.quotePartNumber);
						 dialog.close();
			}};
	 		
	 		var thCancel={text:"退货",onclick:function(item,dialog){
	 			 var postData=top.window.frames[iframeId].getFormData();
 				 var clientOrderNumber=postData.clientOrderNumber;
 				 var clientId=postData.clientId;
 				 var conditionId=postData.conditionId;
 				 var certificationId=postData.certificationId;
 				 $("#elementId").val(postData.elementId);
 				 $("#clientCode").val(postData.clientCode);
 				 $("#soeid").val(postData.id);
 				 $("#clientOrderElementId").val(postData.clientOrderElementId);
 				$.ajax({
 					type: "POST",
 					dataType: "json",
 					url:'<%=path%>/importpackage/location?clientId='
 							+clientId+'&clientOrderNumber='+clientOrderNumber,
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
 					url:'<%=path%>/importpackage/findcond?conditionId='+conditionId,
 					success:function(result){
 						var obj = eval(result.message)[0];
 						if(result.success){
 							$("#conditionId").empty();
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
 					url:'<%=path%>/importpackage/findcert?certificationId='+certificationId,
 					success:function(result){
 						var obj = eval(result.message)[0];
 						if(result.success){
 							$("#certificationId").empty();
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
							
						 var th=postData.th;
						 $("#quotePartNumber").val(postData.quotePartNumber);
						 $("#quoteDescription").val(postData.orderDescription) ;
						 $("#quoteUnit").val(postData.quoteUnit);
						 var importAmount=postData.importAmount;
						 var supplierOrderAmount=postData.supplierOrderAmount;
						 if(importAmount==""){
							 PJ.warn("入库数量为0不能退货");
							 return;
						 }else{
							 $("#a").val(-importAmount);
						 }
						 
						 $("#supplierOrderPrice").val(postData.supplierOrderPrice);
						
						 
						 $("#supplierOrderNumber").val(postData.supplierOrderNumber);
						 $("#supplierOrderElementId").val(postData.id);
						
						 dialog.close();
			
				}};
	 		
	 		var closeCancel={text:"关闭",onclick:function(item,dialog){
					dialog.close();
				}};
	 		
	 			btnArr.push(closeCancel);
				btnArr.push(thCancel);
				btnArr.push(rkCancel);
				
				var opts = {
							buttons: btnArr,
							width: 1000, 
							height: 500,
							showMax: true
					};
				
				PJ.openTopDialog(iframeId, "财务 - 供应商订单明细", url, opts);
			
  			}); 
	 	 
	 	 $("#r2").click(function(){
	 		 $("#quoteDescription").val( $("#description").val()) ;
			 $("#printDescription").val( $("#description").val()) ;
	 		 $("#supplierOrderElementId").val(0);
	 	});
	 	 
	 	 $("#r3").click(function(){
	 		 $("#supplierOrderElementId").val(-1);
	 	});
	 	
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#addForm")[0].reset();
		 });
		 
		 /* $(".text").blur(function(){
			 $("#number").focus();
		 }); */
		 
		 $("#number").keydown(function() {
             if (event.keyCode == "13") {
            	 setTimeout(validatea,500);
             }
             
         });
		 $("#supplierOrderAmount").blur(function(){
	 		 $("#printAmount").val($("#supplierOrderAmount").val());
	 	 });
		 
		 $("input[type='input']").blur(function(){
			var expireDate = $("#expireDate").val();
			var manufactureDate = $("#manufactureDate").val();
			var inspectionDate = $("#inspectionDate").val();
			var useDate = "";
			if(manufactureDate != "" && inspectionDate != ""){
				if(manufactureDate > inspectionDate){
					useDate = manufactureDate
				}else if(manufactureDate < inspectionDate){
					useDate = inspectionDate
				}
			}else if(manufactureDate != ""){
				useDate = manufactureDate;
			}else if(inspectionDate != ""){
				useDate = inspectionDate;
			}
			var hasLife = $("#hasLife").val();
			var shelfLife = $("#shelfLife").val();
			$("#hasLife").empty();
			var $option2 = $("<option></option>");
			$option2.val(1).text("是");
			$("#hasLife").append($option2);
			var $option1 = $("<option></option>");
			$option1.val(0).text("否");
			$("#hasLife").append($option1);
			if((shelfLife != "") && useDate != "" && hasLife == "1"){
				 $.ajax({
						type: "POST",
						dataType: "JSON",
						url:'<%=path%>/importpackage/getRestLifePercent?manufactureDate='+useDate+'&shelfLife='+shelfLife,
						success:function(result){
							if(result.success){
								var res = result.message.split(",")
								$("#restLife").val(res[0]);
								$("#expireDate").val(res[1]);
							}else{
								PJ.warn(result.message);
							}
						}
				});
			}/*  else{
				PJ.warn("要计算剩余生命请把生产日期、到期日期和是否时寿件填上");
			} */
		 });
		 
		 <%-- $("#inspectionDate").blur(function(){
			 var inspectionDate = $("#inspectionDate").val();
			 var manufactureDate = $("#manufactureDate").val();
			 var soeid = $("#soeid").val();
			 var postData = {};
			 postData.inspectionDate = inspectionDate;
			 postData.manufactureDate = manufactureDate;
			 postData.soeid = soeid;
			 $.ajax({
					type: "POST",
					dataType: "json",
					data:postData,
					url:'<%=path%>/importpackage/checkShelfLife',
					success:function(result){
						if(!result.success){
							PJ.warn(result.message);
						}
					}
				});
		 });
		 
		 $("#manufactureDate").blur(function(){
			 var inspectionDate = $("#inspectionDate").val();
			 var manufactureDate = $("#manufactureDate").val();
			 var soeid = $("#soeid").val();
			 var postData = {};
			 postData.inspectionDate = inspectionDate;
			 postData.manufactureDate = manufactureDate;
			 postData.soeid = soeid;
			 $.ajax({
					type: "POST",
					dataType: "json",
					data:postData,
					url:'<%=path%>/importpackage/checkShelfLife',
					success:function(result){
						if(!result.success){
							PJ.warn(result.message);
						}
					}
				});
		 }); --%>
		 
			function validatea(){
				var number = $("#number").val();
				var index = parseInt(number.indexOf(")"));
				number = number.substring(3,index);
				var start = number.substring(0,3);
				if(start=="040"){
					var checkId = number.substring(18);
					var location = $("#location2").val();
					$.ajax({
							type: "POST",
							dataType: "json",
							url:'<%=path%>/importpackage/check?checkId='+checkId+'',
							success:function(result){
								if(result.success){
										$("#location").val(result.message);
										$("#locationmsg").html("");
										$("#number").val("");
										//$("#number").focus();
								}else{
										$("#locationmsg").html(result.message);
										$("#number").val("");
										//$("#number").focus();
								}
							}
						}); 
					$("#number").val("");
					//$("#number").focus();
				}else if(start=="030"){
					var checkId = $("#importpackageId").val()+$("#clientOrderElementId").val();
					var elementId = number.substring(3,10)+ number.substring(10);
					if(checkId!=elementId){
						$("#partmsg").html("件号不匹配！");
						$("#number").val("");
						//$("#number").focus();
					}else if(checkId==elementId){
						$("#partmsg").html("");
						$("#number").val("");
						//$("#number").focus();
					}
				}
				$("#number").val("");
				//$("#number").focus();
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
			var amount =postData.amount;
			var printAmount=postData.printAmount;
			if(null!=amount&&""!=amount&&typeof(amount) != "undefined"){
				amount=amount.replace(/,/g,"");
			}
			if(null!=printAmount&&""!=printAmount&&typeof(printAmount) != "undefined"){
				printAmount=printAmount.replace(/,/g,"");
			}
			postData.amount=amount;
			postData.printAmount=printAmount;
			return postData;
	 }
	
	function onfous(){
		$("#number").focus();
	}
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="">
	<form id="addForm" action="">
	<input type="text" class="hide" name="supplierOrderElementId" id="supplierOrderElementId" value="">
	<input type="text" class="hide" name="clientOrderElementId" id="clientOrderElementId" value="${importPackageElement.clientOrderElementId}">
	<input type="text" class="hide" name="id" id="id" value="${importPackageElement.id}">
	<input type="text" class="hide" name="type" id="type" value="${type}">
	<input type="text" class="hide" name="importpackageId" id="importpackageId" value="${importPackageElement.importPackageId}">
	<input type="text" class="hide" name="elementId" id="elementId" value="${importPackageElement.elementId}">
	<input type="text" class="hide" name="clientCode" id="clientCode" value="${importPackageElement.clientCode}">
	<input type="text" class="hide" name="clientId" id="clientId" value="">
	<input type="text" class="hide" name="surplusAmount" id="surplusAmount" value="">
	<input type="text" class="hide" name="soeid" id="soeid" value="${importPackageElement.supplierOrderElementId}">
	<input type="text" class="hide" name="importPackageSign" id="importPackageSign" value="${importPackageElement.importPackageSign}">
	<input type="text" class="hide" name="onPassageStatus" id="onPassageStatus">
	<input type="text" class="hide" name="description" id="description"   value="${importPackageElement.description}"/>
	<table>
		<tr>
			<td>条形码</td>
			<td><input type="text" name="number" id="number" value=""/></td>
		</tr>
		<tr>
			<td>入库单号</td>
			<td>${importPackageElement.importNumber}</td>
		</tr>
		<tr>
			<td><input type="radio" name="r1" id="r2" <c:if test="${not empty importPackageElement.check}">checked="checked"</c:if>/> 库存  
			 <input type="radio" name="r1" id="r3" /> 异常 <!----></td>
		</tr>
		<tr>
		<td><input type="radio" name="r1" id="r1" <c:if test="${not empty type}">checked="checked"</c:if>/>供应商订单号 </td>
			<td><input name="supplierOrderNumber" id="supplierOrderNumber" class="text"  class="tc" value="${importPackageElement.orderNumber}" disabled="disabled"/> </td>
		</tr>
		<tr>
			<td>客户订单号</td>
			<td><input name="sourceNumber" id="sourceNumber" class="text"  class="tc" /></td>
		</tr>
		<tr>
			<td>件号</td>
			<td><input name="partNumber" id="quotePartNumber" class="text"  class="tc"  value="<c:out value='${importPackageElement.partNumber}'/>"<c:if test="${not empty type}">disabled="disabled"</c:if>/><span id="partmsg"></span></td>
		</tr>
		<tr>
			<td>打印件号</td>
			<td><input name="printPartNumber" id="printPartNumber" class="text"  class="tc"  value="<c:out value='${importPackageElement.partNumber}'/>"/></td>
		</tr>
		<tr>
			<td>描述</td>
			<td><input name="description" id="quoteDescription" class="text"  class="tc" value="${importPackageElement.description}"<c:if test="${not empty type}">disabled="disabled"</c:if>/></td>
		</tr>
		<tr>
			<td>打印描述</td>
			<td><input name="printDescription" id="printDescription" class="text"  class="tc"  value="${importPackageElement.orderDescription}"/></td>
		</tr>
		<tr>
			<td>单位</td>
			<td><input name="unit" id="quoteUnit"  class="text" class="tc" value="${importPackageElement.unit}"/></td>
		</tr>
		<tr>
			<td>客户数量</td>
			<td><input name="a" id="a" class="text" disabled="disabled"  class="tc" value="<fmt:formatNumber type="number" maxFractionDigits="3" value="${importPackageElement.amount}"/>"/></td>
		</tr>
		<tr>
			<td>入库数量</td>
			<td><input name="amount" id="supplierOrderAmount" class="text"  class="tc"  value="<fmt:formatNumber type="number" maxFractionDigits="3" value="${importPackageElement.amount}"/>"/></td>
		</tr>
		<tr>
			<td>打印数量</td>
			<td><input name="printAmount" id="printAmount" class="text"  class="tc" value="<fmt:formatNumber type="number" maxFractionDigits="3" value="${importPackageElement.amount}"/>"/></td>
		</tr>
		<tr>
			<td>单价</td>
			<td><input name="price" id="supplierOrderPrice" class="text"  class="tc" value="${importPackageElement.price}"/></td>
		</tr>
		<tr>
			<td>寿命</td>
			<td><input name="shelfLife" id="shelfLife" type="input" class="text" value="${importPackageElement.shelfLife}"/></td>
		</tr>
		<tr>
			<td>状态</td>
			<td><select id="conditionId" name="conditionId" class="text" >
				<c:if test="${not empty importPackageElement.conditionId}"><option value="${importPackageElement.conditionId}" >${importPackageElement.conditionCode} - ${importPackageElement.conditionValue}</option></c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td>证书</td>
			<td><select id="certificationId" name="certificationId" class="text" >
				<c:if test="${not empty importPackageElement.certificationId}"><option value="${importPackageElement.certificationId}" >${importPackageElement.certificationCode} - ${importPackageElement.certificationValue}</option></c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td>证书编号</td>
			<td><input id="certificationNumber" name="certificationNumber" class="text" value="${importPackageElement.certificationNumber}"/>
			</td>
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
		<%-- <tr>
			<td>位置</td>
			<td><select id="location" name="location" >
			<c:if test="${not empty importPackageElement.lLocation}"><option value="${importPackageElement.lLocation}" >${importPackageElement.lLocation} </option></c:if>
			</select>
			</td>
		</tr> --%>
		 <tr>
			<td>位置</td>
			<td><input name="location" id="location" class="text" class="tc" value="${importPackageElement.location}"/>
			<select id="location2" name="location2"  class="text">
			<c:if test="${not empty importPackageElement.lLocation}"><option value="${importPackageElement.lLocation}" >${importPackageElement.lLocation} </option></c:if>
			</select>
			<span id="locationmsg"></span>
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
			<td><input name="manufactureDate" id="manufactureDate" type="input" class="text" class="tc" value="<fmt:formatDate value="${importPackageElement.manufactureDate}"  pattern="yyyy-MM-dd"/>"></td>
		</tr>
		<tr>
			<td>检验日期</td>
			 <td><input name="inspectionDate" id="inspectionDate" type="input" class="text" class="tc" value="<fmt:formatDate value="${importPackageElement.inspectionDate}"  pattern="yyyy-MM-dd"/>"><span id="msg"></span></td>
		</tr>
		<tr>
			<td>时寿件</td>
			<td>
				<select name="hasLife" id="hasLife" type="input">
					
				</select>
			</td>
		</tr>
		<tr>
			<td>过期日期</td>
			 <td><input name="expireDate" id="expireDate" type="input"  class="text" class="tc"  value="<fmt:formatDate value="${importPackageElement.expireDate}"  pattern="yyyy-MM-dd"/>"></span></td>
		</tr>
		<tr>
			<td>剩余寿命</td>
			<td><input name="restLife" id="restLife"  class="text" disabled="disabled" value="${importPackageElement.restLife}"/>%</td>
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