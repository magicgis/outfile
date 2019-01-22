<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增供应商订单明细</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />


<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		//alert(${clientOrderElementVo.clientOrderId });
		layout = $("#layout1").ligerLayout();
		PJ.datepicker('deadline');
		$("#toolbar").ligerToolBar({
			items : [{
							id:'add',
							icon : 'add',
							text : '新增报价',
							click: function(){
								var clientOrderElementId = $("#clientOrderElementId").val();
								var iframeId="ideaIframe";
									PJ.topdialog(iframeId, '新增供应商报价 ', '<%=path%>/purchase/supplierorder/toAddQuotePrice?id='+clientOrderElementId+'&type='+'${type}',
											function(item,dialog){
												var postData=top.window.frames[iframeId].getFormData();
												var nullAble=top.window.frames[iframeId].validate();
												if(nullAble){
													$.ajax({
														url: '<%=path%>/purchase/supplierorder/addQuotePrice?clientOrderElementId='+clientOrderElementId+'&type='+'${type}',
														type: "POST",
														loading: "正在处理...",
														data:postData,
														success: function(result){
															if(result.success){
																PJ.grid_reload(grid);
																PJ.success(result.message);
																dialog.close();
															}else{
																PJ.error(result.message);
															}
														}
													});
												}else{
													PJ.warn("数据还没有填写完整！");
												}
											}
											,function(item,dialog){dialog.close();}, 1100, 500, true);
							}
					 }
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/purchase/supplierorder/SupplierList?orderNumber='+'${orderNumber}'+'&clientQuoteElementId='+${clientQuoteElementId}+'&quoteNumber='+'${quoteNumber}',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-272,
			autoSrcoll:true,
			shrinkToFit:false,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var count = ${clientOrderElementVo.clientOrderAmount };
				var price = ret["price"];
				var supplierId = ret["supplierId"];
				var currencyId =ret["currencyId"];
				var exchangeRate = ret["exchangeRate"];
				var id = ret["id"];
				var clientOrderElementId=ret["clientOrderElementId"];
				$("#supplierQuoteElementId").val(id);
				$("#coeId").val(clientOrderElementId);
				$("#amount").val(count);
				$("#price").val(price);
				$("#supplierId").val(supplierId);
				$("#currencyId").val(currencyId);
				$("#exchangeRate").val(exchangeRate);
				$("#taxReimbursementId").empty();
				//退税状态
				$.ajax({
					url: '<%=path%>/purchase/supplierorder/getTaxReimbursement?supplierId='+supplierId,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
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
				
				$.ajax({
					url: '<%=path%>/purchase/supplierorder/checkSupplierAptitude?id='+id,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
						if(result.success){
							PJ.warn(result.message);
						}
					}
				});
			},
			colNames :["id","供应商ID","clientOrderElementId","供应商","询价单号","供应商询价单号","报价日期","件号","描述","数量","库存","在途","单位","单价","周期","状态","证书","备注","warranty","serialNumber","tagSrc","tagDate","trace","更新时间","货币","汇率"],
			colModel :[PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("supplierId", {hidden:true}),
			           PJ.grid_column("clientOrderElementId", {hidden:true}),
			           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("supplierInquiryQuoteNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("quoteDate",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("quotePartNumber",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("quoteDescription",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("quoteAmount",{sortable:true,width:30,align:'left'}),
			           PJ.grid_column("storageAmount",{sortable:true,width:30,align:'left'}),
			           PJ.grid_column("onPassageAmount",{sortable:true,width:30,align:'left'}),
			           PJ.grid_column("quoteUnit",{sortable:true,width:30,align:'left'}),
			           PJ.grid_column("price",{sortable:true,width:50,align:'left'}),
			           PJ.grid_column("leadTime",{sortable:true,width:30,align:'left'}),
			           PJ.grid_column("conditionCode",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("quoteRemark",{sortable:true,width:250,align:'left'}),
			           PJ.grid_column("warranty", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("serialNumber", {sortable:true,width:100,editable:true,align:'left'}),
						PJ.grid_column("tagSrc", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("tagDate", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("trace", {sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("currencyId", {hidden:true}),
			           PJ.grid_column("exchangeRate", {hidden:true})
			           ]
		});
		
		//运输方式
		$.ajax({
			url: '<%=path%>/purchase/supplierorder/getShipWay?IfOrder=1',
			type: "POST",
			loading: "正在处理...",
			success: function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#shipWayId").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//目的地
		$.ajax({
			url: '<%=path%>/purchase/supplierorder/destinationList?IfOrder=1',
			type: "POST",
			loading: "正在处理...",
			success: function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#destination").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		if('${weather}'=='update'){
			$("#saveBtn").val("修改");
		}
		
		//保存按钮
		$("#saveBtn").click(function(){
			 var postData = getFormData();
			 var validate = getValidate();
			 var coeId=$("#coeId").val();
			 var weather='${weather}';
			 if(validate){
				 var url="";
				 if(null!=weather&&""!=weather){
					 if(weather=="add"){
						url='<%=path%>/sales/clientorder/addSupplierWeatherOrder?coeId='+coeId;
					 }else if(weather=="update"){
						 url='<%=path%>/sales/clientorder/updateSupplierWeatherOrder?coeId='+coeId+'&supplierWeatherOrderElementId='+'${supplierWeatherOrderElementId}';
					 }
					 $.ajax({
							type: "POST",
							dataType: "json",
							data: postData,
							url:url,
							success:function(result){
								if(result.success){
									PJ.success(result.message);
									window.close();
								}else{
									PJ.warn(result.message);
								}
							}
					});
				 }else{
					 var clientAmount = $("#clientOrderAmount").val();
					 var supplierAmouont = $("#supplierOrderAmount").val();
					 if(parseFloat(supplierAmouont) >= parseFloat(clientAmount)){
						 PJ.warn("采购数量已满足客户需求，不可再增加！");
					 }else{
						 $.ajax({
								type: "POST",
								dataType: "json",
								data: postData,
								url:'<%=path%>/purchase/supplierorder/saveSupplierOrder?coeId='+coeId,
								success:function(result){
									if(result.success){
										PJ.success(result.message);
										window.close();
									}else{
										PJ.warn(result.message);
									}
								}
						});
						$(".l-dialog-btn-inner").click();
					 }
				 }
			 }else{
				 PJ.warn("数据没有填写完整！");
			 }
		});
		
		$("#leadTime").blur(function(){
			
			var leadTime = $("#leadTime").val();
			if(leadTime != ""){
				var days = parseInt($("#leadTime").val());
				 $.ajax({
						type: "POST",
						dataType: "",
						data: '',
						url:'<%=path%>/purchase/supplierorder/deadline?leadTime='+days,
						success:function(result){
							if(result.success){
								$("#deadline").val(result.message);
							}else{
								PJ.warn(result.message);
							}
						}
				});
			}
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid.setGridHeight(PJ.getCenterHeight()-252);
		});
		
		$("#replenishment").click(function() {
			 $.ajax({
					type: "POST",
					dataType: "",
					data: '',
					url:'<%=path%>/purchase/supplierorder/getSupplierOrderAmount?clientOrderElementId='+${clientOrderElementVo.id },
					success:function(result){
						if(result.success){
							PJ.warn("数量已大于客户订单数量，不可继续追加！");
							$("#replenishment").attr("checked",false);
						}
					}
			});
		});
		
		$("#leadTime").keyup(function(){
			this.value = this.value.replace(/[^\d]/g, '')
		});
		$("#feeForExchangeBill").keyup(function(){
			this.value = this.value.replace(/[^\d]/g, '')
		});
		$("#bankCost").keyup(function(){
			this.value = this.value.replace(/[^\d]/g, '')
		});
		$("#otherFee").keyup(function(){
			this.value = this.value.replace(/[^\d]/g, '')
		});
		
		/* $("#leadTime").blur(function(){
			var leadTime = $("#leadTime").val();
			var re = /^[0-9]+.?[0-9]*$/;     
			if(!re.test(leadTime)){
				PJ.warn("只可以为数字");
			}
		}); */
		
	});
	
	//获取表单数据
	function getFormData(){
			var $input = $("#orderForm").find("input,select");
			var postData = {};
			if($("#replenishment").is(':checked')==true){
				postData.add="on";
			}else{
				postData.add="off";
			}

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
	function getValidate(){
		return validate2({
			nodeName:"amount,price,leadTime,deadline",
			form:"#orderForm"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
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
				}else $(this).removeClass("input-error");
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="新增供应商订单明细">
		<div id="toolbar"></div>
			<form id="orderForm">
			<input type="text" class="hide" name="clientOrderId" value="${clientOrderElementVo.clientOrderId }"/>
			<input type="text" class="hide" name="supplierId" id="supplierId"/>
			<input type="text" class="hide" name="currencyId" id="currencyId" />
			<input type="text" class="hide" name="exchangeRate" id="exchangeRate"/>
			<input type="text" class="hide" name="clientOrderElementId" id="clientOrderElementId" value="${clientOrderElementVo.id }"/>
			<input type="text" class="hide" id="supplierOrderAmount" name="supplierOrderAmount" value="${clientOrderElementVo.supplierOrderAmount }"/>
			<input type="text" class="hide" id="clientOrderAmount" name="clientOrderAmount" value="${clientOrderElementVo.clientOrderAmount }"/>
			<input type="text" class="hide" name="supplierQuoteElementId" id="supplierQuoteElementId"/>
			<input type="text" class="hide" name="coeId" id="coeId"/>
			<div id="searchdiv" style="width: 100%;height: 140px;">
				<table id="messageTab" style=" border-collapse:   separate;   border-spacing:   10px;">
						<tr>
							<td>订单号</td>
							<td>${orderNumber }</td>
							<td>件号</td>
							<td>${clientOrderElementVo.quotePartNumber }</td>
							<td>数量</td>
							<td>${clientOrderElementVo.clientOrderAmount }</td>
							<td>库存数量</td>
							<td>${clientOrderElementVo.storageAmount }</td>
							<td>单位</td>
							<td>${clientOrderElementVo.quoteUnit }</td>
						</tr>
						<tr>
							<td>截至日期</td>
							<td><fmt:formatDate value="${clientOrderElementVo.clientOrderDeadline }"  pattern="yyyy-MM-dd"/></td>
							<td>周期</td>
							<td>${clientOrderElementVo.clientOrderLeadTime }</td>
							<td>收货地址</td>
							<td>
								<select name="destination" id="destination">
									
								</select>
								
							</td>
							<td>是否退税：</td>
							<td><select name="taxReimbursementId" id="taxReimbursementId">
									
								</select>
							</td>
							<td>运输方式</td>
							<td><select name="shipWayId" id="shipWayId"></select></td>
						</tr>
						<tr>
							<td>数量</td>
							<td><input type="text" name="amount" id="amount" size="11"/></td>
							<td>单价</td>
							<td><input type="text" name="price" id="price" size="11"/></td>
							<td>平均价格</td>
							<td><input type="text" name="averagePrice" value="${averagePrice } " disabled="disabled" size="11"/></td>
							<td>最低报价</td>
							<td><input type="text" name="lowPrice" value="${lowPrice }" disabled="disabled" size="11"/></td>
							<td>比较比率</td>
							<td><input type="text" name="compareRate" value="${compareRate }" disabled="disabled" size="11"/>%</td>
							<td>补货<input type="checkbox" id="replenishment"/></td>
						</tr>
						<tr>
							
							<td>周期</td>
							<td><input type="text" name="leadTime" id="leadTime" size="11"/>天</td>
							<td>截至日期</td>
							<td><input name="deadline" type="text" id="deadline" class="tc" size="11"/></td>
							<!-- <td>提货换单费</td>
							<td>$<input type="text" name="feeForExchangeBill" id="feeForExchangeBill" size="11"/></td> -->
							<td>银行手续费</td>
							<td>$<input type="text" name="bankCost" id="bankCost" size="11"/></td>
							<!-- <td>杂费</td>
							<td>$<input type="text" name="otherFee" id="otherFee" size="11"/></td> -->
							<!-- <td>补货&nbsp;&nbsp;&nbsp;<input type="checkbox" id="replenishment"/></td> -->
							<!-- <td><input class="btn btn_orange" type="button" value="新增" id="saveBtn"/></td> -->
							<td><input class="btn btn_orange" type="button" value="新增" id="saveBtn"/></td>
						</tr>

				</table> 
			</div>
			</form>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>