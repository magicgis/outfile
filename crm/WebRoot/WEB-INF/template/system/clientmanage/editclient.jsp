<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改客户</title>

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
		
		//币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/currencyList?edit=1&id='+${client.id},
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
		
		//客户归类
		 $.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/classifyList?id='+'${client.id}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $checkedItems = $("#addForm>table").find("tr#checkbox>td#checkbox");
						  var checkBox=document.createElement("input");
					        checkBox.setAttribute("type","radio");
					        checkBox.setAttribute("id","clientType"+obj[i].id);
					        checkBox.setAttribute("name", "clientType");
					        checkBox.setAttribute("value", obj[i].id);
					        if(obj[i].check=='checked'){
					        	 checkBox.setAttribute("checked", true);
					        }
					       
					        $checkedItems.append(checkBox);       
					        $checkedItems.append(obj[i].value);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/certificationList?edit=1&id='+${client.id},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#certification").append($option);
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
			url:'<%=path%>/system/clientmanage/Owners?edit=1&id='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].userId).text(obj[i].userName);
						$("#owner").append($option);
					}
				}
			}
		});
		
		//客户状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/statusList?edit=1&id='+${client.id},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#clientStatusId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//客户等级
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/levelList?edit=1&id='+${client.id},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#clientLevelId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//交付方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/delivery?edit=1&id='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#termsOfDelivery").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//发货方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/shipWay?edit=1&id='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#clientShipWay").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//客户方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/clientType?edit=1&id='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#clientTemplateType").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//发票模板
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/invoiceTemplet?edit=1&id='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#invoiceTemplet").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//箱单模板
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/shipTemplet?edit=1&id='+${client.id },
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#shipTemplet").append($option);
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
				data: code,
				url:'<%=path%>/system/clientmanage/currencyList',
				success:function(result){
					if(result.success){
						$("#msg").html("代码已存在!");
					}else{
						$("#msg").html(" ");
					}
				}
			});
		});
		
		if(${client.taxReturn }==1){
			$("#taxReturn").attr("checked","checked");
		}
	    
	});
	
	//获取表单数据
	function getFormData(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};
		var prepayRate = $("#prepayRate").val();
		var shipPayRate = $("#shipPayRate").val();
		var receivePayRate = $("#receivePayRate").val();
		var total = parseInt(prepayRate)+parseInt(shipPayRate)+parseInt(receivePayRate);
		if(total!=100){
			PJ.warn("预付比例有误！请核实！");
		}else{
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
			var check=$("input[type='radio']:checked").val();
			postData.clientType=check;
			return postData;
		}
	}	
	
	 
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="新增客户询价">
	<form id="addForm" action="">
	<input name="id" id="id" value="${client.id }" class="hide"/>
	<table>
		<tr>
			<td>编号</td>
			<td><input name="code" id="code" class="text" value="${client.code }"/><span id="msg"></span></td>
		</tr>
		<tr>
			<td>拥有人</td>
			<td><select id="owner" name="owner">
				</select>
			</td>
		</tr>
		<tr>
			<td>客户状态</td>
			<td><select id="clientStatusId" name="clientStatusId">
				</select>
			</td>
		</tr>
		<tr>
			<td>客户类型</td>
			<td><select id="clientTemplateType" name="clientTemplateType">
				</select>
			</td>
		</tr>
		<tr>
			<td>发票模板</td>
			<td><select id="invoiceTemplet" name="invoiceTemplet">
				</select>
			</td>
		</tr>
		<tr>
			<td>箱单模板</td>
			<td><select id="shipTemplet" name="shipTemplet">
				</select>
			</td>
		</tr>
		<tr>
			<td>客户简称</td>
			<td><input name="clientShortName" id="clientShortName" class="text" value="${client.clientShortName }"/></td>
		</tr>
		<tr>
			<td>客户全称</td>
			<td><input name="name" id="name" class="text" value="${client.name }"/></td>
		</tr>
		<tr>
			<td>Location</td>
			<td><input name="location" id="location" class="text" value="${client.location }"/></td>
		</tr>
		<tr>
			<td>固定成本</td>
			<td><input name="fixedCost" id="fixedCost" class="text" value="${client.fixedCost }"/></td>
		</tr>
		<tr>
			<td>利润率</td>
			<td><input name="profitMargin" id="profitMargin" class="text" value="${client.profitMargin }"/>%</td>
		</tr>
		<tr>
			<td>运费</td>
			<td><input name="freight" id="freight" class="text" value="${client.freight }"/></td>
		</tr>
		<tr>
			<td>最低运费</td>
			<td><input name="lowestFreight" id="lowestFreight" class="text" value="${client.lowestFreight }"/></td>
		</tr>
		<tr>
			<td>银行费用</td>
			<td><input name="bankCost" id="bankCost" class="text" value="${client.bankCost }"/></td>
		</tr>
		<tr>
			<td>是否需要合格证</td>
			<td><select id="certification" name="certification">
				</select>
			</td>
		</tr>
		<tr>
			<td>货币</td>
			<td><select id="currencyId" name="currencyId">
				</select>
			</td>
		</tr>
		<tr>
			<td>邮编</td>
			<td><input name="postCode" id="postCode" class="text" value="${client.postCode }"/></td>
		</tr>
		<tr>
			<td>地址</td>
			<td><input name="address" id="address" class="text" value="${client.address }"/></td>
		</tr>
		<tr>
			<td>交付方式</td>
			<td><select name="termsOfDelivery" id="termsOfDelivery">
				</select>
			</td>
		</tr>
		<tr>
			<td>快递地址</td>
			<td><input name="shipAddress" id="shipAddress" class="text" value="${client.shipAddress }"/></td>
		</tr>
		<tr>
			<td>发货方式</td>
			<td><select name="clientShipWay" id="clientShipWay" >
				</select>
			</td>
		</tr>
		<tr id="checkbox">
		<td>客户归类</td>
			<td id="checkbox"></td>
		</tr>
		<tr>
			<td>是否可退税</td>
			<td><input name="taxReturn" id="taxReturn" class="text" type="checkbox"/></td>
		</tr>
		<tr>
			<td>预付比例</td>
			<td><input type="text" name="prepayRate" id="prepayRate" value="${client.prepayRate }"/>%</td>
		</tr>

		<tr>
			<td>发货时支付比例</td>
			<td><input type="text" name="shipPayRate" id="shipPayRate" value="${client.shipPayRate }"/>%</td>
		</tr>
		<tr>
			<td>发货帐期</td>
			<td><input type="text" name="shipPayPeriod" id="shipPayPeriod" value="${client.shipPayPeriod }"/></td>
		</tr>
		<tr>
			<td>验货时支付比例</td>
			<td><input type="text" name="receivePayRate" id="receivePayRate" value="${client.receivePayRate }"/>%</td>
		</tr>
		<tr>
			<td>验货账期</td>
			<td><input type="text" name="receivePayPeriod" id="receivePayPeriod" value="${client.receivePayPeriod }"/></td>
		</tr>
		<tr>
			<td>电话</td>
			<td><input name="phone" id="phone" class="text" value="${client.phone }"/></td>
		</tr>
		<tr>
			<td>传真</td>
			<td><input name="fax" id="fax" class="text" value="${client.fax }"/></td>
		</tr>
		<tr>
			<td>主页网址</td>
			<td><input name="url" id="url" class="text" value="${client.url }"/></td>
		</tr>
		<tr>
			<td>邮箱</td>
			<td><input name="email" id="email" class="text" value="${client.email }"/></td>
		</tr>
		<tr>
			<td>客户等级</td>
			<td><select id="clientLevelId" name="clientLevelId">
				</select>
			</td>
		</tr>
		<tr>
			<td>客户阶段</td>
			<td><input name="clientStage" id="clientStage" class="text" value="${client.clientStage }"/></td>
		</tr>
		<tr>
			<td>客户来源</td>
			<td><input name="clientSource" id="clientSource" class="text" value="${client.clientSource }"/></td>
		</tr>
		<tr>
			<td>能力范围</td>
			<td><textarea rows="7" cols="90" name="clientAbility" id="clientAbility">${client.clientAbility }</textarea></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="7" cols="90" name="remark" id="remark">${client.remark }</textarea></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>