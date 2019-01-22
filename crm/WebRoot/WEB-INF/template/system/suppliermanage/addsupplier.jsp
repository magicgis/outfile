<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增供应商</title>

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
		nodeName:"code,name,currencyId,supplierAbbreviation,address,countryId,prepayRate,shipPayRate,receivePayRate,receivePayPeriod,checkbox",
		form:"#addForm"
	});
}

function checkCaac(){
	var caac = $("#hasCaacAbility").val();
	var $check=$("input[type='checkbox']:checked");//.prop("checked");
	var hasMro = false;
	$check.each(function(index){
		if($(this).val() == 83){
			hasMro = true;
		}
	});
	if(hasMro && caac == ""){
		return false;
	}else{
		return true;
	}
}

function validate2(opt){
	var def = {nodeName:"",form: ""};
	for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
	var $items = $(def.form).find("input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
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
		/* if($("#currencyId").val()==""||$("#currencyId").text()=="请选择"){
			$("#currencyId").addClass("input-error");
				tip = $("#currencyId").attr("data-tip");
				flag=false;
				return false;
		} */
		
	});
	if (flag){
		if($("#countryId").val()==""||$("#countryId").text()=="请选择"){
			$("#countryId").addClass("input-error");
				tip = $("#countryId").attr("data-tip");
				flag=false;
				return false;
		}
		var check=$("input[type='checkbox']:checked");
		if(check.length == 0){
			tip = $("#checkbox").attr("data-tip");
			flag=false;
			return false;
		}
	}
	
	return flag;
};

	$(function(){
		PJ.datepicker('orderDate');
		
		//币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchcurrency?id='+'${data.currencyId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].currency_id).text(obj[i].currency_value);
						$("#currencyId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//供应商状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/statusList?id='+'${data.supplierStatusId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#supplierStatusId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//国家
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/getCountryList?id='+'${supplier.countryId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					if('${supplier.countryId}' == ''){
						var $option = $("<option></option>");
						$option.val("").text("请选择");
						$("#countryId").append($option);
					}
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].name+"-"+obj[i].chineseName);
						$("#countryId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//Mov Per Order币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/currencyList?id='+'${supplier.movPerOrderCurrencyId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					if('${supplier.movPerOrderCurrencyId}' == ''){
						$("#movPerOrderCurrencyId").append($("<option></option>"));
					}
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#movPerOrderCurrencyId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//供应商归类
		 $.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/classifyList?id='+'${data.id}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $checkedItems = $("#addForm>table").find("tr#checkbox>td#checkbox");
						  var checkBox=document.createElement("input");
					        checkBox.setAttribute("type","checkbox");
					        checkBox.setAttribute("id","supplierClassifyId"+obj[i].id);
					        checkBox.setAttribute("name", "supplierClassifyId");
					        checkBox.setAttribute("value", obj[i].id);
					        if(obj[i].check=='checked'){
					        	 checkBox.setAttribute("checked", true);
					        }
					       
					        $checkedItems.append(checkBox);       
					        $checkedItems.append(obj[i].value);
						/* if(obj[i].supplierClassifyId==80){
							$("#supplierClassifyId80").attr("checked", true);  
						}if(obj[i].supplierClassifyId==81){
							$("#supplierClassifyId81").attr("checked", true);  
						}if(obj[i].supplierClassifyId==82){
							$("#supplierClassifyId82").attr("checked", true);  
						}if(obj[i].supplierClassifyId==83){
							$("#supplierClassifyId83").attr("checked", true);  
						}if(obj[i].supplierClassifyId==84){
							$("#supplierClassifyId84").attr("checked", true);  
						}if(obj[i].supplierClassifyId==85){
							$("#supplierClassifyId85").attr("checked", true);  
						}if(obj[i].supplierClassifyId==86){
							$("#supplierClassifyId86").attr("checked", true);  
						}if(obj[i].supplierClassifyId==87){
							$("#supplierClassifyId87").attr("checked", true);  
						}if(obj[i].supplierClassifyId==88){
							$("#supplierClassifyId88").attr("checked", true);  
						}if(obj[i].supplierClassifyId==89){
							$("#supplierClassifyId89").attr("checked", true);  
						} */
					/* 	var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#supplierClassifyId").append($option); */
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); <%----%>
		
		//是否可退税
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/taxReimbursementList?id='+'${data.taxReimbursementId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#taxReimbursementId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//供应商等级
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/gradeList?id='+'${data.supplierGradeId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#supplierGradeId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//拥有人
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/Owners?id='+'${data.owner}'+'&userId='+'${userId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].userId).text(obj[i].userName);
						$("#owner").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//供应商阶段
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/phasesList?id='+'${data.supplierPhasesId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#supplierPhasesId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//代理
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/agentList?id='+'${supplier.isAgentId}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#isAgentId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//是否有caac能力
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/caacAbilityList?id='+'${supplier.hasCaacAbility}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					if(''=='${supplier.hasCaacAbility}'){
						var $option = $("<option></option>");
						$option.val("").text("请选择");
						$("#hasCaacAbility").append($option);
					}
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#hasCaacAbility").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//校验code是否已存在
		$("#code").blur(function(){
			var code = $("#code").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/suppliermanage/testCode?code='+code,
				success:function(result){
					if(result.success){
						$("#msg").html("代码已在供应商或者竞争对手存在!");
					}else{
						$("#msg").html(" ");
					}
				}
			});
		});
		
		//校验名称是否已存在
		$("#name").blur(function(){
			var name = $("#name").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/suppliermanage/testName?name='+name,
				success:function(result){
					if(result.success){
						$("#msg2").html("此供应商已建立!");
					}else{
						$("#msg2").html(" ");
					}
				}
			});
		});
		
		//校验简称是否已存在
		$("#supplierAbbreviation").blur(function(){
			var shortName = $("#supplierAbbreviation").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/suppliermanage/testShortName?shortName='+shortName,
				success:function(result){
					if(result.success){
						$("#msg3").html("此供应商已建立!");
					}else{
						$("#msg3").html(" ");
					}
				}
			});
		});
		
		//校验网址是否已存在
		$("#url").blur(function(){
			var url = $("#url").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/suppliermanage/checkUrl?url='+url,
				success:function(result){
					if(result.success){
						$("#msg4").html("网址已存在!");
					}else{
						$("#msg4").html(" ");
					}
				}
			});
		});
		
		//校验邮箱域名是否已存在
		$("#email").blur(function(){
			var email = $("#email").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/suppliermanage/checkEmail?email='+email,
				success:function(result){
					if(result.success){
						$("#msg5").html("邮箱域名已存在!请检查是否已存在该供应商");
					}else{
						$("#msg5").html(" ");
					}
				}
			});
		});
		
		 
	});
	
	
	
	//获取表单数据
	 function getFormData(){
			var $input = $("#addForm").find("input,textarea,select");
			var postData = {};
			var prepayRate = $("#prepayRate").val();
			var shipPayRate = $("#shipPayRate").val();
			var receivePayRate = $("#receivePayRate").val();
			var total = parseInt(prepayRate)+parseInt(shipPayRate)+parseInt(receivePayRate);
			var owner=$("#owner").val();
			if(''!='${userId}'&&'${userId}'!=owner){
				PJ.warn("请选择拥有人！");
				return false;
			}
			if(total!=100){
				PJ.warn("预付比例有误！请核实！");
				return false;
			}
			var check=$("input[type='checkbox']:checked");//.prop("checked");
			var supplierClassifyId="";
			$.each(check,function(i){
				supplierClassifyId=supplierClassifyId+$(this).val()+",";
			});
			
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
				postData[$(this).attr("name")] = $(this).val();
			});
			postData.supplierClassifyId=supplierClassifyId;
			return postData;
	 }	
	
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="新增供应商">
	<form id="addForm" action="">
		<input name="id" id="id"  class="tc" type="hidden" value="${supplier.id}"/>
	<table>
		<tr>
			<td><span>*</span>代码</td>
			<td><input name="code" id="code"  class="tc" value="${supplier.code}"/><span id="msg"></span></td>
		</tr>
		<c:if test="${edit eq 1}">
		<tr>
			<td>旧系统代码</td>
			<td><input name="oldCode" id="oldCode"  class="tc" value="${supplier.oldCode}"/></td>
		</tr>
		</c:if>
		<tr>
			<td><span>*</span>供应商全称</td>
			<td><input name="name" id="name"  class="tc" value="${supplier.name}" style="width:200px"/><span id="msg2"></td>
		</tr>
		
		<tr>
			<td><span>*</span>供应商简称</td>
			<td><input name="supplierAbbreviation" id="supplierAbbreviation"  class="tc" value="${supplier.supplierAbbreviation}" style="width:200px"/><span id="msg3"></td>
		</tr>
		
		<tr>
			<td>Location</td>
			<td><input name="location" id="location"  class="text" value="${supplier.location}" style="width:200px"/><span id="msg3"></td>
		</tr>
		
		<tr>
			<td>Name In Stockmarket</td>
			<td><input name="nameInStockmarket" id="nameInStockmarket"  class="tc" value="${supplier.nameInStockmarket}" style="width:200px"/></td>
		</tr>
		
		<tr>
			<td>代理</td>
			<td>
				<select name="isAgentId" id="isAgentId"  class="text"  style="width:200px">
				</select>
			</td>
		</tr>
		
		<tr>
			<td><span>*</span>币种</td>
			<td><select id="currencyId" name="currencyId">
				<c:if test="${not empty data.currencyId}"><option value="${data.currencyId}" >${data.currencyValue}</option></c:if>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>联系人</td>
			<td><input name="contactName" id="contactName"  class="tc" value="${supplier.contactName}"/></td>
		</tr>
		
	<%-- 	<tr>
			<td>邮编</td>
			<td><input name="postCode" id="postCode"  class="tc" value="${supplier.postCode}"/></td>
		</tr>
		 --%>
		<tr>
			<td><span>*</span>地址</td>
			<td><textarea rows="5" cols="90" name="address">${supplier.address}</textarea></td>
		</tr>
		
		<tr>
			<td><span>*</span>国家</td>
			<td>
				<select name="countryId" id="countryId"  class="tc">
				</select>
			</td>
		</tr>
		
		<tr>
			<td>电话</td>
			<td><input name="phone" id="phone"  class="tc" value="${supplier.phone}"/></td>
		</tr>
		
		<tr>
			<td>手机</td>
			<td><input name="mobile" id="mobile"  class="tc" value="${supplier.mobile}" style="width:200px"/></td>
		</tr>
		
		<tr>
			<td>传真</td>
			<td><input name="fax" id="fax"  class="tc" value="${supplier.fax}" style="width:200px"/></td>
		</tr>
		
		<tr>
			<td>网址</td>
			<td><input name="url" id="url" value="${supplier.url}" style="width:200px"/><span id="msg4"></span></td>
		</tr>
		
		<tr>
			<td>电子邮箱</td>
			<td><input name="email" id="email"  class="tc" value="${supplier.email}" style="width:200px"/><span id="msg5"></span></td>
		</tr>
		
	<%-- 	<tr>
			<td>供应商编号</td>
			<td><input name="supplerSerialNumber" id="supplerSerialNumber"  class="tc" value="${supplier.supplerSerialNumber}" style="width:200px"/></td>
		</tr> --%>
		
		<tr>
			<td>拥有人</td>
			<td><select id="owner" name="owner">
			<c:if test="${not empty data.owner }"><option value="${data.owner}" >${data.userName}</option></c:if>
						<c:if test="${not empty userId }"><option value="${userId}" >${userName}</option></c:if>
				</select>
			</td><%-- <input name="owner" id="owner"  class="tc" value="${supplier.owner}" style="width:200px"/> --%>
		</tr>
		
			<tr>
			<td>供应商状态</td>
			<td><select id="supplierStatusId" name="supplierStatusId">
						<c:if test="${not empty data.supplierStatusId}"><option value="${data.supplierStatusId}" >${data.supplierStatusValue}</option></c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td>MOV PER LINE</td>
			<td><input type="text" name="movPerLine" id="movPerLine"  value="${supplier.movPerLine}"/></td>
		</tr>
		<tr>
			<td>MOV PER ORDER</td>
			<td><input type="text" name="movPerOrder" id="movPerOrder"  value="${supplier.movPerOrder}"/></td>
		</tr>
		<tr>
			<td>MOV币种</td>
			<td><select type="text" name="movPerOrderCurrencyId" id="movPerOrderCurrencyId">
				</select>
			</td>
		</tr>
		<tr>
			<td>银行手续费</td>
			<td><input type="text" name="counterFee" id="counterFee"  value="${data.counterFee}"/></td>
		</tr>
		<tr>
			<td>信用卡手续费</td>
			<td><input type="text" name="creditFee" id="creditFee"  value="${data.creditFee}"/>%</td>
		</tr>
		<tr>
			<td><span>*</span>预付比例</td>
			<td><input type="text" name="prepayRate" id="prepayRate" value="<fmt:formatNumber value="${data.prepayRate}" pattern="0"/>"/>%</td>
		</tr>
		<tr>
			<td><span>*</span>发货前付款比例</td>
			<td><input type="text" name="shipPayRate" id="shipPayRate" value="<fmt:formatNumber value="${data.shipPayRate}" pattern="0"/>"/>%</td>
		</tr>
		<tr>
			<td><span>*</span>验收完限期内付款比例</td>
			<td><input type="text" name="receivePayRate" id="receivePayRate" value="<fmt:formatNumber value="${data.receivePayRate}" pattern="0"/>"/>%</td>
		</tr>
		<tr>
			<td><span>*</span>验收完限期内付款账期</td>
			<td><input type="text" name="receivePayPeriod" id="receivePayPeriod" value="<fmt:formatNumber value="${data.receivePayPeriod}" pattern="0"/>"/></td>
		</tr>
		<tr>
			<td>Credit Limit</td>
			<td><input type="text" name="creditLimit" id="creditLimit" value="${supplier.creditLimit}"/></td>
		</tr>
		<tr id="checkbox">
		<td><span>*</span>供应商归类</td>
			<td id="checkbox">
			<!-- <input type="checkbox" value="80" name="supplierClassifyId" id="supplierClassifyId80"/>OEM
			<input type="checkbox" value="81" name="supplierClassifyId" id="supplierClassifyId81"/>分销商
			<input type="checkbox" value="82" name="supplierClassifyId" id="supplierClassifyId82"/>中间商
			<input type="checkbox" value="83" name="supplierClassifyId" id="supplierClassifyId83"/>MRO
			<input type="checkbox" value="84" name="supplierClassifyId" id="supplierClassifyId84"/>旧件
			<input type="checkbox" value="85" name="supplierClassifyId" id="supplierClassifyId85"/>6
			<input type="checkbox" value="86" name="supplierClassifyId" id="supplierClassifyId86"/>7
			<input type="checkbox" value="87" name="supplierClassifyId" id="supplierClassifyId87"/>8
			<input type="checkbox" value="88" name="supplierClassifyId" id="supplierClassifyId88"/>9
			<input type="checkbox" value="89" name="supplierClassifyId" id="supplierClassifyId89"/>10 -->
			</td>
		</tr>
		<tr>
			<td>CAAC能力</td>
			<td><select id="hasCaacAbility" name="hasCaacAbility">
			    </select>
			</td>
		</tr>
		<tr>
		<td>是否可退税</td>
			<td><select id="taxReimbursementId" name="taxReimbursementId">
						<c:if test="${not empty data.taxReimbursementId}"><option value="${data.taxReimbursementId}" >${data.taxReimbursementValue}</option></c:if>
				</select>
			</td>
		</tr>
		
		<%-- <tr>
		<td>供应商等级</td>
			<td><select id="supplierGradeId" name="supplierGradeId">
						<c:if test="${not empty data.supplierGradeId}"><option value="${data.supplierGradeId}" >${data.supplierGradeValue}</option></c:if>
				</select>
			</td>
		</tr>
		
		<tr>
		<td>供应商阶段</td>
			<td><select id="supplierPhasesId" name="supplierPhasesId">
					<c:if test="${not empty data.supplierPhasesId}"><option value="${data.supplierPhasesId}" >${data.supplierPhasesValue}</option></c:if>
				</select>
			</td>
		</tr>
		
		<tr>
			<td>供应商来源</td>
			<td><input name="supplierSource" id="supplierSource"  class="tc" value="${supplier.supplierSource}" style="width:200px"/></td>
		</tr> --%>
		
		<tr>
			<td>能力范围</td>
			<td><input name="competenceScope" id="competenceScope"  class="tc" value="${supplier.competenceScope}" style="width:200px"/></td>
		</tr>
		
		<tr>
			<td>付款规则</td>
			<td><input name="paymentRule" id="paymentRule"  class="tc" value="${supplier.paymentRule}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>开户行 Bank Name</td>
			<td><input name="bank" id="bank"  class="tc" value="${supplier.bank}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>IBAN</td>
			<td><input name="iban" id="iban"  class="tc" value="${supplier.iban}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>Account Name</td>
			<td><input name="accountName" id="accountName"  class="tc" value="${supplier.accountName}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>银行账号</td>
			<td><input name="bankAccountNumber" id="bankAccountNumber"  class="tc" value="${supplier.bankAccountNumber}"  style="width:200px"/></td>
		</tr>
		<tr>
			<td>Swift Code</td>
			<td><input name="swiftCode" id="swiftCode"  class="tc" value="${supplier.swiftCode}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>ROUTING/ABA TRANSIT NUMBER</td>
			<td><input name="routing" id="routing"  class="tc" value="${supplier.routing}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>银行电话</td>
			<td><input name="bankPhone" id="bankPhone"  class="tc" value="${supplier.bankPhone}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>银行地址</td>
			<td><input name="bankAddress" id="bankAddress"  class="tc" value="${supplier.bankAddress}" style="width:200px"/></td>
		</tr>
		<tr>
			<td>纳税号</td>
			<td><input name="taxPayerNumber" id="taxPayerNumber"  class="tc" value="${supplier.taxPayerNumber}" style="width:200px"/></td>
		</tr>
		
		<tr>
			<td>备注</td>
			<td><textarea rows="5" cols="90" name="remark">${supplier.remark}</textarea></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>