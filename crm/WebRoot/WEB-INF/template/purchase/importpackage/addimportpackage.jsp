<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增入库单</title>

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
		
	/* 	$("#btnPrint").printPage(); */
		
		PJ.datepicker('importDate');
		var $option = $("<option></option>");
		$option.val(0).text("未完成");
		$("#importStatus").append($option);	
		var $option = $("<option></option>");
		$option.val(1).text("完成");
		$("#importStatus").append($option);
		var $option = $("<option></option>");
		$option.val(2).text("预入库");
		$("#importStatus").append($option);
		
	
	
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/importpackage/findsid',
			success:function(result){
				var obj = eval("("+result.message+")")[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#supplierId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		//币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchcurrency',
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
		
		//费用币种
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchcurrency',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].currency_id).text(obj[i].currency_value);
						$("#feeCurrencyId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//物流方式
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/importpackage/logisticsWay',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#logisticsWay").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		 $("#supplierOrderNumber").blur(function(){
			var supplierOrderNumber=$("#supplierOrderNumber").val();
			//订单查供应商
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/importpackage/findSupplierByNumber?supplierOrderNumber='+supplierOrderNumber,
				success:function(result){
					var obj = eval("("+result.message+")")[0];
					if(result.success){
						$("#supplierId").empty();
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].code);
							$("#supplierId").append($option);
						}
						$.ajax({
							type: "POST",
							dataType: "json",
							url:'<%=path%>/importpackage/getShipWay?orderNumber='+supplierOrderNumber,
							success:function(result){
								var obj = eval(result.message)[0];
								if(result.success){
									$("#logisticsWay").empty();
									for(var i in obj){
										var $option = $("<option></option>");
										$option.val(obj[i].id).text(obj[i].value);
										$("#logisticsWay").append($option);
									}
								}
							}
						});
						$.ajax({
							type: "POST",
							dataType: "json",
							url:'<%=path%>/importpackage/getCurrency?orderNumber='+supplierOrderNumber,
							success:function(result){
								var obj = eval(result.message)[0];
								if(result.success){
									$("#currencyId").empty();
									for(var i in obj){
										var $option = $("<option></option>");
										$option.val(obj[i].id).text(obj[i].value);
										$("#currencyId").append($option);
									}
								}
							}
						});
						$("#msg").html("");
					}else{
						$("#msg").html("合同号不存在");
					}
				}
			});
		 });
		 
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#addForm")[0].reset();
		 });
		 
		/*  $("#print").click(function(){
			 code128();
				$("#my_show").jqprint({
					    debug: false, //如果是true则可以显示iframe查看效果（iframe默认高和宽都很小，可以再源码中调大），默认是false
					     importCSS: true, //true表示引进原来的页面的css，默认是true。（如果是true，先会找$("link[media=print]")，若没有会去找$("link")中的css文件）
					     printContainer: true, //表示如果原来选择的对象必须被纳入打印（注意：设置为false可能会打破你的CSS规则）。
					     operaSupport: true//表示如果插件也必须支持歌opera浏览器，在这种情况下，它提供了建立一个临时的打印选项卡。默认是true
			}
				);
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
			return postData;
	 }	
	
	/*  function code11(){
		 $("#bcTarget").empty().barcode($("#src").val(), "code11",{barWidth:2, barHeight:30,showHRI:true});
	}
	 function code39(){
         $("#bcTarget").empty().barcode($("#src").val(), "code39",{barWidth:2, barHeight:30,showHRI:true});
    }
	 function code93(){
         $("#bcTarget").empty().barcode($("#src").val(), "code93",{barWidth:2, barHeight:30,showHRI:true});
    } */
	/*  function code128(){
         $("#bcTarget").empty().barcode($("#src").val(), "code128",{barWidth:1, barHeight:30,showHRI:true});
    } */
	/*  function ean8(){
         $("#bcTarget").empty().barcode($("#src").val(), "ean8",{barWidth:2, barHeight:30,showHRI:true});
    }
	 function ean13(){
         $("#bcTarget").empty().barcode($("#src").val(), "ean13",{barWidth:2, barHeight:30,showHRI:true});
    }
	 function std25(){
         $("#bcTarget").empty().barcode($("#src").val(), "std25",{barWidth:2, barHeight:30,showHRI:true});
    }
	 function int25(){
         $("#bcTarget").empty().barcode($("#src").val(), "int25",{barWidth:2, barHeight:30,showHRI:true});
    }
	 function msi(){
         $("#bcTarget").empty().barcode($("#src").val(), "msi",{barWidth:2, barHeight:30,showHRI:true});
    } */
</script>
<style type="">
.barcodeImg{margin:10px 0px}
</style>
</head>
	
<body style="line-height:10px;">
<!-- <div class="my_show" id="my_show" >
 <table>
   		<tr><td>ORDER NO:</td></tr>
 		<tr><td>DSN:</td></tr>
 		<tr><td>PART NO:</td></tr>
 		<tr><td>DESC:</td></tr>
 		<tr><td>UNIT:</td></tr>
 		<tr><td>CERT:</td></tr>
 		
   <tr><td><div id="bcTarget" class="barcodeImg" ></div></td></tr>
      
        <tr>
        <td></td>
        </tr>
        <tr>
    	<td>
    	
    	</td>
        </tr>
    </table>
</div> -->
 
<!-- <div class="my_hidden">
<input id="src" value="1214124"></input>
<input type="button" onclick='code11()' value="code11"/>     
    	  <input type="button" onclick='code39()' value="code39"/> 
    	  <input type="button" onclick='code93()' value="code93"/>
    	  <input type="button" onclick='code128()' value="生成条形码"/><br />
    	  <input type="button" onclick='ean8()' value="ean8"/>   
    	  <input type="button" onclick='ean13()' value="ean13"/>
    	  <input type="button" onclick='ean13()' value="std25"/>
    	  <input type="button" onclick='int25()' value="int25"/>
    	   <input type="button" onclick='msi()' value="msi"/>
    	   <input type="button" id="print"  value="打印"></input>
    	   <p><a id="btnPrint" href='iframes'>Print!</a></p>
</div> -->



<div position="center" title="新增入库单">
	<form id="addForm" action="">
	<input type="text" class="hide" name="clientQuoteId" value="${client_quote_id}" />
	<input type="text" class="hide" name="clientinquiryquotenumber" value="${client_inquiry_quote_number}" />
	<table>
	<tr>
			<td>供应商</td>
			<td><select id="supplierId" name="supplierId">
						<option value="" >请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>供应商合同号</td>
			<td><input name="supplierOrderNumber" id="supplierOrderNumber"  class="tc" /><span id="msg"></span></td>
		</tr>
		<tr>
			<td>币种</td>
			<td><select id="currencyId" name="currencyId">
						<option value="" >请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>物流方式</td>
			<td><select id="logisticsWay" name="logisticsWay">
						<option value="" >请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>运输单号</td>
			<td><input name="logisticsNo" id="logisticsNo"  class="tc" /></td>
		</tr>
		<!-- 
		<tr>
			<td>汇率</td>
			<td><input name="exchangeRate" id="exchangeRate"  class="tc" /></td>
		</tr> -->
		<tr>
			<td>入库日期</td>
			<td><input name="importDate" id="importDate"  class="tc" value="<fmt:formatDate value="${importDate}"  pattern="yyyy-MM-dd"/>"></td>
		</tr>
		<tr>
			<td>状态</td>
			<td>
					<select id="importStatus" name="importStatus">
					
					</select>			
			</td>
		</tr>
		<tr>
			<td>重量</td>
			<td><input name="weight" id="weight"  class="tc" />kg</td>
		</tr>
		<tr>
			<td>入库费</td>
			<td><input name="importFee" id="importFee" /></td>
		</tr>
		<tr>
			<td>运费</td>
			<td><input name="freight" id="freight" /></td>
		</tr>
		<tr>
			<td>费用币种</td>
			<td><select name="feeCurrencyId" id="feeCurrencyId"  class="tc">
					<option value="" >请选择</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td><textarea rows="10" cols="90" name="remark"></textarea></td>
		</tr>
		<tr>
			<td colspan="2">
			<!-- 	<input class="btn btn_orange" type="button" value="新增" id="addBtn"/>
				<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/> -->
			</td>
			<td></td>
		</tr>
		
	</table>
	</form>
</div>	
</body>
</html>