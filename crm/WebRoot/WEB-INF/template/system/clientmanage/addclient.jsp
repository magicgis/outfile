<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增客户</title>

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
		//币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/currencyList?edit=0',
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
			url:'<%=path%>/system/clientmanage/classifyList?id='+'${data.id}',
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
			url:'<%=path%>/system/clientmanage/certificationList?edit=0',
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
		
		//用户列表
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/userList',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					var chkhtml = [];
					for(var i in obj){
						 chkhtml.push('<input type="checkbox"  value="'+obj[i].userId+'"/><label>'+obj[i].loginName+'</label>&nbsp;&nbsp;');
					}
					$("#users").html(chkhtml.join(''));
				}else{
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//拥有人
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/Owners?edit=0',
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
		
		//箱单模板
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/shipTemplet?edit=0',
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
		
		//客户状态
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/statusList?edit=0',
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
			url:'<%=path%>/system/clientmanage/levelList?edit=0',
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
		//发货方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/shipWay?edit=0',
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
		
		//交付方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/delivery?edit=0',
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
		
		//客户方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/clientType?edit=0',
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
			url:'<%=path%>/system/clientmanage/invoiceTemplet?edit=0',
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
		
		//校验code是否已存在
		$("#code").blur(function(){
			var code = $("#code").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/system/clientmanage/testCode?code='+code,
				success:function(result){
					if(result.success){
						$("#msg").html("代码已存在!");
					}else{
						$("#msg").html(" ");
					}
				}
			});
		});
		
		$("#url").blur(function(){
			var url = $("#url").val();
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/system/clientmanage/checkUrl?url='+url,
				success:function(result){
					if(result.success){
						$("#urlmsg").html("主页网址已存在！请查看是否已存在该客户");
					}else{
						$("#urlmsg").html(" ");
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
			var check=$("input[type='checkbox']:checked");
			var userIds="";
			$.each(check,function(i){
				userIds=userIds+$(this).val()+",";
			});
			postData.userIds = userIds;
			var check=$("input[type='radio']:checked").val();
			postData.clientType=check;
			return postData;
		}
	}
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"clientShipWay,owner,code,name,prepayRate,shipPayRate,shipPayPeriod,receivePayRate,receivePayPeriod,profitMargin",
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
<div position="center" title="新增客户">
	<form id="addForm" action="">
	<table>
		<tr>
			<td>编号</td>
			<td><input name="code" id="code" class="text"/><span id="msg"></span></td>
		</tr>
		<tr>
			<td>拥有人</td>
			<td><select id="owner" name="owner">
				</select>
			</td>
		</tr>
		<tr>
			<td>开放人员</td>
			<td><div id="users">
				</div>
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
			<td><input name="clientShortName" id="clientShortName" class="text"/></td>
		</tr>
		<tr>
			<td>客户全称</td>
			<td><input name="name" id="name" class="text"/></td>
		</tr>
		<tr>
			<td>Location</td>
			<td><input name="location" id="location" class="text"/></td>
		</tr>
		<tr>
			<td>佣金</td>
			<td><input name="fixedCost" id="fixedCost" class="text"/></td>
		</tr>
		<tr>
			<td>利润率</td>
			<td><input name="profitMargin" id="profitMargin" class="text"/>%</td>
		</tr>
		<tr>
			<td>运费</td>
			<td><input name="freight" id="freight" class="text"/></td>
		</tr>
		<tr>
			<td>最低运费</td>
			<td><input name="lowestFreight" id="lowestFreight" class="text"/></td>
		</tr>
		<tr>
			<td>银行费用</td>
			<td><input name="bankCost" id="bankCost" class="text"/></td>
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
			<td><input name="postCode" id="postCode" class="text"/></td>
		</tr>
		<tr>
			<td>地址</td>
			<td><input name="address" id="address" class="text"/></td>
		</tr>
		<tr>
			<td>交付方式</td>
			<td><select name="termsOfDelivery" id="termsOfDelivery">
				</select>
			</td>
		</tr>
		<tr>
			<td>快递地址</td>
			<td><input name="shipAddress" id="shipAddress" class="text"/></td>
		</tr>
		<tr>
			<td>发货方式</td>
			<td><select name="clientShipWay" id="clientShipWay">
				</select>
			</td>
		</tr>
		<tr id="checkbox">
		<td>客户归类</td>
			<td id="checkbox"></td>
		</tr>
		<tr>
			<td>是否可退税</td>
			<td><input name="taxReturn" id="taxReturn" type="checkbox"/></td>
		</tr>
		<tr>
			<td>预付比例</td>
			<td><input type="text" name="prepayRate" id="prepayRate"/>%</td>
		</tr>

		<tr>
			<td>发货时支付比例</td>
			<td><input type="text" name="shipPayRate" id="shipPayRate"/>%</td>
		</tr>
		<tr>
			<td>发货帐期</td>
			<td><input type="text" name="shipPayPeriod" id="shipPayPeriod"/></td>
		</tr>
		<tr>
			<td>验货时支付比例</td>
			<td><input type="text" name="receivePayRate" id="receivePayRate"/>%</td>
		</tr>
		<tr>
			<td>验货账期</td>
			<td><input type="text" name="receivePayPeriod" id="receivePayPeriod"/></td>
		</tr>
		<tr>
			<td>电话</td>
			<td><input name="phone" id="phone" class="text"/></td>
		</tr>
		<tr>
			<td>传真</td>
			<td><input name="fax" id="fax" class="text"/></td>
		</tr>
		<tr>
			<td>主页网址</td>
			<td><input name="url" id="url" class="text"/><span id="urlmsg"></span></td>
		</tr>
		<tr>
			<td>邮箱</td>
			<td><input name="email" id="email" class="text"/></td>
		</tr>
		<tr>
			<td>客户等级</td>
			<td><select id="clientLevelId" name="clientLevelId">
				</select>
			</td>
		</tr>
		<tr>
			<td>客户阶段</td>
			<td><input name="clientStage" id="clientStage" class="text"/></td>
		</tr>
		<tr>
			<td>客户来源</td>
			<td><input name="clientSource" id="clientSource" class="text"/></td>
		</tr>
		<tr>
			<td>能力范围</td>
			<td><textarea rows="7" cols="90" name="clientAbility" id="clientAbility"></textarea></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="7" cols="90" name="remark" id="remark"></textarea></td>
		</tr>
		
	</table>
	
	</form>
</div>	
</body>
</html>