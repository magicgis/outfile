<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>增加明细 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style type="text/css">

table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#FFFFFF;
}
.evenrowcolor{
	background-color:#E0EEEE;
}
</style>
<script type="text/javascript">
	var layout, grid;
	var check = 0;
	$(function() {
		$("#toolbar").ligerToolBar({
			items : [
					   {  icon : "add",
						  text : "增加行",
						  click : function(){
								  	var $tbody = $("#elementTable>tbody");//-- 容器
									var $item = $($("script[type='tetx/template']").html()).clone();//-- 复制模版
						
									$tbody.append($item);
								}
						},
						{ icon : "delete",
						  text : "删除行",
						  click : function(){
									var $checkedItems = $("#elementTable>tbody>tr").find("input:checkbox");
									$checkedItems.each(function(i){
								    if($(this).prop("checked")){
											$(this).parent().parent().remove();
										} 
									});
											
							}
						}
			        ]
		});
		
		//状态信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/staticprice/findConditionForSupplier',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $items = $("#elementTable>tbody").find("tr");
						$items.each(function(){
							var $inputs = $(this).find("select");
							$inputs.each(function(){
								if($(this).attr("name") == "conditionId"){
									var $option = $("<option></option>");
									$option.val(obj[i].id).text(obj[i].code);
									$(this).append($option);
								}
							});
						});				
					}
				}else{
					
						PJ.warn("操作失误!");
				}
			}
		});
		
		//证书信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/staticprice/findCertificationForSupplier',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $items = $("#elementTable>tbody").find("tr");
						$items.each(function(){
							var $inputs = $(this).find("select");
							$inputs.each(function(){
								if($(this).attr("name") == "certificationId"){
									var $option = $("<option></option>");
									$option.val(obj[i].id).text(obj[i].code);
									$(this).append($option);
								}
							});
						});						
					}
				}else{
					
						PJ.warn("操作失误!");
				}
			}
		});
		
		$("#submit").click(function(){
			var id =$("#id").val();
			<%--var url = '<%=path%>/storage/suppliercommissionsale/uploadExcel?id='+getUploadData().id+'&editType='+ getUploadData().toString();--%>
            var url = '<%=path%>/storage/suppliercommissionsale/uploadExcel?id='+getUploadData().id;
			//批量新增来函案源
			PJ.showLoading("上传中....");
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	PJ.hideLoading();
	            	var da = eval(data)[0];
	            	if(da.flag==true){
		            	PJ.success("上传成功,已新增"+da.message+"条数据！");
		            	$("#uploadBox").toggle(function(){
							$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
							grid2.setGridHeight(PJ.getCenterHeight()-260);
		            	});
	            	}else{
	            		var message = da.message.split(",");
	            		if(message[0] == "condition"){
	            			PJ.error("第"+message[1]+"行状态不存在！")
	            		}else if(message[0] == "certification"){
	            			PJ.error("第"+message[1]+"行证书不存在！")
	            		}else if(message[0] == "certification"){
	            			PJ.error("第"+message[1]+"行出现异常！")
	            		}else{
	            			PJ.error("生成供应商报价异常！");
	            		}
	            	}
	            	
	            	/* if(da.flag==true){
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	}
	            	else{
	            		PJ.error(da.message);
	            		$("#uploadBox").toggle(function(){
							var display = $("#uploadBox").css("display");
							$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-142);
		            	});
	            	} */
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常");
	            }
	        });
			
		 });
		
	});
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
			var $isnull= $(this).find("input[class='isnull']");
			$isnull.each(function(){
				if($(this).val()!=""){
					$inputs.each(function(){
						obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
					});
					index++;
				}
			});
		});
		
		return obj;
	}
	
	function getFormData(){
		var postData = getTableDate();
		postData.check = check
		postData.token='${token}';
		check = 1;
		return postData;
	}
	
	function altRows(id){
		if(document.getElementsByTagName){  
			
			var table = document.getElementById(id);  
			var rows = table.getElementsByTagName("tr"); 
			 
			for(i = 0; i < rows.length; i++){          
				if(i % 2 == 0){
					rows[i].className = "evenrowcolor";
				}else{
					rows[i].className = "oddrowcolor";
				}      
			}
		}
	}

	window.onload=function(){
		altRows('elementTable');
	}
	
	function getUploadData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
</script>

</head>

<body>
	<div id="toolbar"></div>
	<div id="uploadBox">
			<form id="form" name="form">
			 	<input class="hide" name="id" id="id" value="${id}"/>
		   		
					<form:row columnNum="2">
						<form:column width="120">
							<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
							<form:right>
								<p><input type="file" value="" id="file" name="file"/>&nbsp;
								   <input type="button" id="submit" value="上传"/>
								</p>
							</form:right>
						</form:column>
					</form:row>            
			 </form>
	</div>
	<div>
	<table id="elementTable" border="1px">
		<thead>
			<tr>
				<th></th>
				<th style="text-align: center;">件号</th>
				<th style="text-align: center;">描述</th>
				<th style="text-align: center;">单位</th>
				<th style="text-align: center;">数量</th>
				<th style="text-align: center;">MOQ</th>
				<th style="text-align: center;">单价</th>
				<th style="text-align: center;">location</th>
				<th style="text-align: center;">周期</th>
				<th style="text-align: center;">状态</th>
				<th style="text-align: center;">证书</th>
				<th style="text-align: center;">备注</th>
				<th style="text-align: center;">提货换单费</th>
				<th style="text-align: center;">银行费用</th>
				<th style="text-align: center;">HAZMAT FEE</th>
				<th style="text-align: center;">杂费</th>
				<th style="text-align: center;">Core Charge</th>
				<th style="text-align: center;">Serial Number</th>
				<th style="text-align: center;">Tag Src</th>
				<th style="text-align: center;">Tag Date</th>
				<th style="text-align: center;">Trace</th>
				<th style="text-align: center;">Warranty</th>
				<th style="text-align: center;">CSN</th>
				<th style="text-align: center;">TSN</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
			<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
			</tr>
		</tbody>
	</table>
	</div>
	<script id="table-add" type="tetx/template" >
		<tr>
				<td><input type="checkbox" class="form-checkbox" name="dataItem" /></td>
				<td><input type="text" name="partNumber"/></td>
				<td><input type="text" name="description"/></td>
				<td><input style="width: 50px" type="text" name="unit"/></td>
				<td><input style="width: 50px" type="text" name="amount"/></td>
				<td><input style="width: 50px" type="text" name="moq"/></td>
				<td><input style="width: 80px" type="text" class="isnull" name="price"/></td>
				<td><input style="width: 80px" type="text" name="location"/></td>
				<td><input style="width: 50px" type="text" name="leadTime"/></td>
				<td>
				<select name="conditionId">
				</select>
				</td>
				<td>
				<select name="certificationId">
				</select>
				</td>
				<td><input type="text" name="remark"/></td>
				<td><input type="text" style="width: 50px" name="feeForExchangeBill"/></td>
				<td><input type="text" style="width: 50px" name="bankCost"/></td>
				<td><input type="text" style="width: 60px" name="hazmatFee" /></td>
				<td><input type="text" style="width: 50px" name="otherFee"/></td>
				<td><input type="text" name="coreCharge"/></td>
				<td><input type="text" name="serialNumber" id="serialNumber"/></td>
				<td><input type="text" name="tagSrc" id="tagSrc"/></td>
				<td><input type="text" name="tagDate" class="tagDate" id="tagDate"/></td>
				<td><input type="text" name="trace" id="trace"/></td>
				<td><input type="text" name="warranty" id="warranty"/></td>
				<td><input type="text" style="width: 90px" name="CSN" id="" class="CSN"/></td>
				<td><input type="text" style="width: 90px" name="TSN" id="" class="TSN"/></td>
		</tr>
	</script>
</body>

</html>