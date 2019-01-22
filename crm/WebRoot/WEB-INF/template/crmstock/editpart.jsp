<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>修改商品库</title>

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
		var msnFlag = ${tPart.msnFlag };
		if(msnFlag==0){
			//alert(msnFlag);
			var $option = $("<option></option>");
			$option.val(0).text("否");
			var $option2 = $("<option></option>");
			$option2.val(1).text("是");
			$("#msnFlag").append($option);
			$("#msnFlag").append($option2);
		}else{
			//alert(msnFlag);
			var $option = $("<option></option>");
			$option.val(1).text("是");
			var $option2 = $("<option></option>");
			$option2.val(0).text("否");
			$("#msnFlag").append($option);
			$("#msnFlag").append($option2);
		}
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
		
		//件号类型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/parttype/partType?bsn='+"${tPart.bsn }",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code+"-"+obj[i].value);
						$("#partType").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//重量单位
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/stock/search/findWeightUnit?id='+"${tPart.weightUnitId }",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#weightUnitId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
	 	
	 	//尺寸单位
	 	$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/stock/search/findDimentionsUnit?id='+"${tPart.dimentionsUnitId }",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#dimentionsUnitId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		/* $("#email").blur(function(){
			var code = $("#email").val();
			var reg = /^(\w+((-\w+)|(\.\w+))*)\+\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			var flag = reg.test(code);
			if(flag==true){
				$("#emailmsg").html(" ");
			}else {
				$("#emailmsg").html("邮箱格式不正确！");
			}
		}); */
		
		
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
			if($("#taxReturn").is(':checked')==true){
				postData.taxReturn=1;
			}else{
				postData.taxReturn=0;
			}
			return postData;
	}
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"msn,msnFlag,partNum,partName,cageCode,nsn",
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
<div position="center" title="修改商品库">
	<form id="addForm" action="">
	<input name="bsn" id="bsn" class="hide"  value="${tPart.bsn }"/>
	<table>
		<tr>
			<td>Part Number</td>
			<td><input id="partNum" name="partNum" class="text"  value="${tPart.partNum }" disabled="disabled"/>
			</td>
		</tr>
		<tr>
			<td>Description</td>
			<td><textarea rows="7" cols="90" name="partName" id="partName">${tPart.partName }</textarea></td>
		</tr>
		<tr>
			<td>Correlation BSN</td>
			<td><input id="correlationBsn" name="correlationBsn" class="text"  value=""/></td>
		</tr>
		<tr>
			<td>MSN Flag</td>
			<td><input name=msnFlag id="msnFlag" class="text"  value="${tPart.msnFlag }"/></td>
		</tr>
		<tr>
			<td>Cage Code</td>
			<td><input name="cageCode" id="cageCode" class="text"  value="${tPart.cageCode }"/></td>
		</tr>
		<tr>
			<td>NSN</td>
			<td><input name="nsn" id="nsn" class="text"  value="${tPart.nsn }"/></td>
		</tr>
		<tr>
			<td>Part Type</td>
			<td><select id="partType" name="partType" >
					<option value="">无</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Blacklist</td>
			<td><select id="isBlacklist" name="isBlacklist" >
			<c:if test="${  tPart.isBlacklist=='1'}">
				<option value="1">是</option>
				<option value="0">否</option>
				</c:if>
				<c:if test="${ empty tPart.isBlacklist ||tPart.isBlacklist=='0'}">
				<option value="0">否</option>
				<option value="1">是</option>
				</c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td>Replaced NSN</td>
			<td><input id="replacedNsn" name="replacedNsn" class="text"  value="${tPart.replacedNsn }"/>
			</td>
		</tr>
		<tr>
			<td>Weight</td>
			<td>
				<input name="weight" id="weight" class="text"  value="${tPart.weight }"/>
				<select name="weightUnitId" id="weightUnitId">
				</select>
			</td>
		</tr>
		<tr>
			<td>Dimentions</td>
			<td>
				<input name="dimentions" id="dimentions" class="text"  value="${tPart.dimentions }"/>
				<select name="dimentionsUnitId" id="dimentionsUnitId">
				</select>
			</td>
		</tr>
		<tr>
			<td>Country Of Origin</td>
			<td><input name="countryOfOrigin" id="countryOfOrigin" class="text" value="${tPart.countryOfOrigin }"/>
			</td>
		</tr>
		<tr>
			<td>ECCN</td>
			<td><input name="eccn" id="eccn" class="text" value="${tPart.eccn }"/></td>
		</tr>
		<tr>
			<td>Schedule B Code</td>
			<td><input name="scheduleBCode" id="scheduleBCode" class="text" value="${tPart.scheduleBCode }"/>
			</td>
		</tr>
		<tr>
			<td>Shelf Life</td>
			<td><input name="shelfLife" id="shelfLife" class="text" value="${tPart.shelfLife }"/>天</td>
		</tr>
		<tr>
			<td>ATA Chapter Section</td>
			<td><input type="text" name="ataChapterSection" id="ataChapterSection" value="${tPart.ataChapterSection }"/></td>
		</tr>
		<tr>
			<td>Category No</td>
			<td><input type="text" name="categoryNo" id="categoryNo" value="${tPart.categoryNo }"/></td>
		</tr>

		<tr>
			<td>Usml</td>
			<td><input type="text" name="usml" id="usml" value="${tPart.usml }"/></td>
		</tr>
		<tr>
			<td>Hazmat_code</td>
			<td><input type="text" name="hazmatCode" id="hazmatCode" value="${tPart.hazmatCode }"/></td>
		</tr>
		<tr>
			<td>Img Path</td>
			<td><input name="imgPath" id="imgPath" class="text" value="${tPart.imgPath }"/>
			</td>
		</tr>
		<tr>
			<td>HS Code</td>
			<td><input name="hsCode" id="hsCode" class="text" value="${tPart.hsCode }"/>
			</td>
		</tr>
		<tr>
			<td>Remark</td>
			<td><textarea rows="10" cols="90" name="remark">${tPart.remark }</textarea>
			</td>
		</tr>
		
	</table>
	
	</form>
</div>	
</body>
</html>