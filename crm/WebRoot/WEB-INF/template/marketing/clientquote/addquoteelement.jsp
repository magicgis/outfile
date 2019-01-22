<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增报价明细</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>

<script type="text/javascript">
 	//-- Set Attribute
 	var optType ='${optType}';
	var layout, grid, grid;
	
	function validate(){ 
		return validate2({
			nodeName:"leadTime,amount,price",
			form:"#Form"
		});
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
                    $(this).addClass("text-error");
					tip = $(this).attr("data-tip");
					return false;
				}else $(this).removeClass("text-error");
			}
		});
		return flag;
	};
	
	$(function() {
		layout = $("#layout_main").ligerLayout();

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ 
			]
		});
		
		$("#addBtn").click(function(){
			var check =validate();
			var profitMargin=$("#profitMargin").val();
			//if(!profitMargin.indexOf("-")==0){
			if(check){ 
			 $.ajax({
					url: '<%=path%>/clientquote/creatclientquote',
					data: getFormData(),
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
			}
			//}else{
			//	PJ.error("利润率为负数");
			//}
		 }); 
		
		$("#editBtn").click(function(){
			var check =validate();
			var profitMargin=$("#profitMargin").val();
			//if(!profitMargin.indexOf("-")==0){
			if(check){ 
			 $.ajax({
					url: '<%=path%>/clientquote/editclientquoteelement',
					data: getFormData(),
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								$("#type").val("edit");
								PJ.success(result.message,function(item, dialog){
									dialog.close();
								});
								PJ.grid_reload(grid);
							} else {
								PJ.error(result.message);
							}
						
					}
				});
			}
			//}else{
			//	PJ.error("利润率为负数");
			//}
		 }); 
		
		grid = PJ.grid("list", {
			rowNum:20,
			url: "<%=path%>/clientquote/quotedate?clientQuoteId="+"${clientQuoteId}"
					+"&clientInquiryElementId="+"${clientInquiryElement.id}",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 240,
			onSelectRow:function(rowid,status,e){
				if($("#type").val()!='edit'){
				var ret = PJ.grid_getSelectedData(grid);
				var supplierQuoteStatusValue=ret["supplierQuoteStatusValue"];
				if(supplierQuoteStatusValue=="失效"){
					PJ.error("价格失效不能使用");
					PJ.grid_reload(grid);
					return;
				}
				
				var iframeId = 'clientquoteFrame';
				var compareRate = ret["compareRate"];
				var averagePrice = ret["averagePrice"];
				var lowestPrice = ret["lowestPrice"];
				var profitMargin = ret["profitMargin"];
				
				
				var quoteAmount = ret["quoteAmount"];
				$("#amount").val(quoteAmount);
				var quoteRemark = ret["quoteRemark"];
				var quotePrice = ret["quotePrice"];
				var profitMargin = ret["profitMargin"];
				var supplierQuoteElementId = ret["id"];
				var location= ret["location"];
				var freight=ret["freight"];
				var counterFee=ret["counterFee"];
				var hazmatFee=ret["hazmatFee"];
				var feeForExchangeBill = ret["feeForExchangeBill"];
				var otherFee = ret["otherFee"];
				var price=ret["price"];
				var moq = ret["moq"];
				
				if(moq != ""){
					$("#moq").val(moq);
					if(parseFloat(moq) > parseFloat(quoteAmount)){
						quoteAmount = moq;
					}
				}else{
					$("#moq").val("");
				}
				if(""==freight){
					freight=0;
				}
				if(""==counterFee||counterFee<1){
					counterFee=0;
				}/* else if(counterFee<1){
					counterFee=counterFee*parseFloat(price);
				} */
				if(""==feeForExchangeBill){
					feeForExchangeBill=0;
				}
				if(""==otherFee){
					otherFee=0;
				}
				if(""==hazmatFee){
					hazmatFee=0;
				}
				
				var profitmargin=ret["profitmargin"];
				var fixedCost=$("#fixedCost").val();
				
				var bankCharges=$("#bankCharges").val();
				var bankChargesFee = 0;
				if(""==bankCharges){
					bankCharges=0;
				}else if(parseFloat(bankCharges)>=1) {
					bankChargesFee = bankCharges
					bankCharges = 0
				}
				
				var fixedCostFee = 0;
				if(null==fixedCost||""==fixedCost){
					fixedCost=0;
				}else if(parseFloat(fixedCost)>=1) {
					fixedCostFee = fixedCost
					fixedCost = 0
				}
				counterFee = counterFee/quoteAmount
				feeForExchangeBill = feeForExchangeBill/quoteAmount
				//hazmatFee = hazmatFee/quoteAmount
				otherFee = otherFee/quoteAmount
				profitmargin=1-(1/parseFloat(profitmargin)).toFixed(2);
				quotePrice=(((parseFloat(price))/(1-parseFloat(profitmargin))+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
				priceArray = quotePrice.split(".")
				if(parseFloat(priceArray[0]) > parseFloat(0)){
					quotePrice = Math.ceil(quotePrice)
				}
				//profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
				profitMargin=((quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
								(quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)

				var relativeRate = ret["relativeRate"];
				$("#relativeRate").val(relativeRate);
				$("#location").val(location);
				$("#remark").val(quoteRemark);
				$("#price").val(quotePrice);
				$("#supplier_quote_price").val(price);
				$("#profitMargin").val(profitMargin+"%");
				$("#compareRate").val(compareRate+'%');
				$("#averagePrice").val(averagePrice);
				$("#lowestPrice").val(lowestPrice);
				$("#supplierQuoteElementId").val(supplierQuoteElementId);
				$("#freight").val(freight);
				$("#counterFee").val(counterFee);
				$("#feeForExchangeBill").val(feeForExchangeBill);
				$("#hazmatFee").val(hazmatFee);
				}
				$("#type").val("");
				
			},
			gridComplete:function(){
				var supplierQuoteElementId=$("#supplierQuoteElementId").val();
				var obj1 = jQuery("#list").jqGrid('setSelection',supplierQuoteElementId);  
				var obj = $("#list").jqGrid("getRowData");
				jQuery(obj).each(function(){
					if(this.id==supplierQuoteElementId){
					 if($("#price").val()==""){
							$("#price").val(this.clientQuotePrice);
					 }
					 $("#freight").val(this.freight);
					 $("#counterFee").val(this.counterFee);
					 $("#supplier_quote_price").val(this.price);
					  $("#averagePrice").val(this.averagePrice);
				        $("#compareRate").val(this.compareRate+'%');
				        $("#lowestPrice").val(this.lowestPrice);
					}else if(supplierQuoteElementId==""){
						 $("#averagePrice").val(this.averagePrice);
					        $("#compareRate").val(this.compareRate+'%');
					        $("#lowestPrice").val(this.lowestPrice);
					}
			    });
				if(${optType eq 'edit'}){
					onPriceChange();
				}
			},
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/clientquote/updatesupplierquotefreight',
			aftersavefunc:function(rowid,result){
				var responseJson=result.responseJSON;
				if(responseJson.success==true){
					var rowData=grid.getRowData(rowid);
					var freight=rowData.freight;
					var counterFee=rowData.counterFee;
					var price=rowData.price;
					if(""==counterFee||counterFee>1){
						counterFee=0;
					}else if(counterFee<1){
						counterFee=counterFee*parseFloat(price);
					}
					if(""==freight){
						freight=0;
					}
					var profitmargin=rowData.profitmargin;
				
					var quotePrice=rowData.quotePrice;
					var quoteAmount=rowData.quoteAmount;
					var fixedCost=$("#fixedCost").val();
					rowData.countprice=(quoteAmount*(parseFloat(price)+parseFloat(counterFee)))+parseFloat(freight);
					
					 var bankCharges=$("#bankCharges").val();
					 if(""==bankCharges||parseFloat(bankCharges)>1){
						 bankCharges=0;
						}
					
					 if(null==fixedCost||""==fixedCost||parseFloat(fixedCost)>1){
							fixedCost=0;
					}/* else{
						rowData.quotePrice=((parseFloat(price)+parseFloat(freight))*parseFloat(profitmargin)).toFixed(2);
						rowData.profitMargin=((((parseFloat(price)+parseFloat(freight))*parseFloat(profitmargin)).toFixed(2) - (parseFloat(price)+parseFloat(freight))) / ((parseFloat(price)+parseFloat(freight))*parseFloat(profitmargin)).toFixed(2) * 100).toFixed(1);
						 $("#price").val(((parseFloat(price)+parseFloat(freight))*parseFloat(profitmargin)).toFixed(2));
					} */
					
					profitmargin=1-(1/parseFloat(profitmargin));
					rowData.quotePrice=((parseFloat(price)+parseFloat(freight)+parseFloat(counterFee))/(1-parseFloat(profitmargin))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
					rowData.profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
					 $("#price").val(((parseFloat(price)+parseFloat(freight)+parseFloat(counterFee))/(1-parseFloat(profitmargin))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4));
					
					grid.setRowData(rowid,rowData);
					 $("#supplier_quote_price").val(price);
					 $("#freight").val(freight);
					 $("#counterFee").val(counterFee);
					 onPriceChange();
				}
			},
			sortname : "sq.quote_date",
			sortorder : "desc",
			sortname : "",
			colNames : [ "客户报价","比较比率","平均价格","最低价格","件号",  "描述","数量","单位","MOQ","供应商","报价日期","竞争对手", "单价","状态","Hazmat Fee","手续费","提货换单费","杂费",
			             "总价","状态","证书","周期", "Available Qty","Location"/* ,"mov" */,"mov per line","mov per order"/* ,"币种" */,"备注","Core Charge","warranty","Serial Number","TagSrc","TagDate","Trace","询价单号", "供应商询价单号","有效日期", "黑名单",
			             "供应商报价明细id","运费", "客户报价日期","客户报价", "订单日期","客户订单价格","利润率百分数","利润率小数","竞争对手报价","当前规避风险汇率"],
			colModel : [ 
			             PJ.grid_column("quotePrice", {hidden:true}),
			             PJ.grid_column("compareRate", {hidden:true}),
			             PJ.grid_column("averagePrice", {hidden:true}),
			             PJ.grid_column("lowestPrice", {hidden:true}),
			         	 PJ.grid_column("quotePartNumber", {width:100,align:'left'}),
			        	 PJ.grid_column("quoteDescription", {width:120,align:'left'}),
			        	 PJ.grid_column("quoteAmount", {sortable:true,width:30,align:'left'}),
			        	 PJ.grid_column("quoteUnit", {width:30,align:'left'}),
			        	 PJ.grid_column("moq", {sortable:true,width:30,align:'left'}),
			             PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left'}),
			             PJ.grid_column("quoteDate", {sortable:true,width:60,align:'left'}),
			             PJ.grid_column("ifCompetitor", {sortable:true,width:60,align:'left',
			            	 formatter: function(value){
									if (value==1||value=="是") {
										return '<span style="color:red">'+"是"+'<span>';
									} else {
										return "";
									}
								}	 
			             }),
			             PJ.grid_column("price", {sortable:true,width:50,align:'left'}),
			             PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:40,align:'left'}),
			             PJ.grid_column("hazmatFee", {sortable:true,width:60,align:'left'}),
			             PJ.grid_column("counterFee", {sortable:true,width:40,align:'left'}),
			             PJ.grid_column("feeForExchangeBill", {sortable:true,width:70,align:'left'}),
			             PJ.grid_column("otherFee", {sortable:true,width:40,align:'left'}),
			         	 PJ.grid_column("countprice", {width:60,align:'left',hidden:true,
							formatter:function(value,options,data){
								var supplierCode=data['supplierCode'];
								if(supplierCode=="历史供应商报价"){
									return
								}
								var countprice=data['countprice'];
								var freight=data['freight'];
								var counterFee=data['counterFee'];
								var price=data['price'];
								if(""==freight){
									freight=0;
								}
								if(""==counterFee||counterFee>1){
									counterFee=0;
								}else if(counterFee<1){
									counterFee=counterFee*parseFloat(price);
								}
							
								var quoteAmount=data['quoteAmount'];
								countprice= (quoteAmount*(parseFloat(price)+parseFloat(counterFee)))+parseFloat(freight);
									return countprice.toFixed(2);
							}	
							}),
							PJ.grid_column("conditionCode", {sortable:true,width:50,align:'left'}),
							PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
							PJ.grid_column("leadTime", {sortable:true,width:40,align:'left'}),
							PJ.grid_column("availableQty", {sortable:true,width:40,align:'left'}),
							PJ.grid_column("location", {sortable:true,width:80,align:'left'}),
							/* PJ.grid_column("mov", {width:60,align:'left'}), */
							PJ.grid_column("movPerLine", {sortable:true,width:80,align:'left'}),
							PJ.grid_column("movPerOrder", {sortable:true,width:80,align:'left'}),
							/* PJ.grid_column("movPerOrderCurrencyValue", {sortable:true,width:80,align:'left'}), */
							PJ.grid_column("quoteRemark", {width:400,align:'left'}),
							PJ.grid_column("coreCharge", {width:100,align:'left'}),
							PJ.grid_column("warranty", {sortable:true,width:80,align:'left'}),
							PJ.grid_column("serialNumber", {sortable:true,width:100,align:'left'}),
							PJ.grid_column("tagSrc", {sortable:true,width:80,align:'left'}),
							PJ.grid_column("tagDate", {sortable:true,width:80,align:'left'}),
							PJ.grid_column("trace", {sortable:true,width:80,align:'left'}),
			             PJ.grid_column("clientInquiryQuoteNumber", {width:160,align:'left'}),
						PJ.grid_column("supplierInquiryQuoteNumber", {width:150,align:'left'}),
						PJ.grid_column("validity", {sortable:true,width:100,align:'left'}),
						 PJ.grid_column("isBlacklist",{sortable:true,width:50,align:'left',
				        	   formatter: function(value){
									if (value==1||value=="是") {
										return '<span style="color:red">'+"是"+'<span>';
									} else if(value==0||value=="否"){
										return "否";							
									}else {
										return "";
									}
								}
				           }),
					
						PJ.grid_column("id", {key:true,hidden:true,editable:true,align:'left'}),
						PJ.grid_column("freight", {sortable:true,width:60,editable:true,align:'left'}),
						PJ.grid_column("clientQuoteDate", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("clientQuotePrice", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("clientOrderDate", {sortable:true,width:100,align:'left'}),
						PJ.grid_column("clientOrderPrice", {sortable:true,width:60,align:'left'}),
						PJ.grid_column("profitMargin", {width:80,hidden:true,align:'left'
							 ,formatter:function(value){
								if(value){
									return value+"%";
								}
								else{
									return value;
								}
							} 		
						}),
						PJ.grid_column("profitmargin", {sortable:true,width:50,hidden:true,align:'left'}),
						PJ.grid_column("competitor", {width:400,align:'left'}),
						PJ.grid_column("relativeRate", {hidden:true})
						]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid.setGridHeight(PJ.getCenterHeight()-242);
		});
		
		/* $("#amount").blur(function(){
			var amount = $("#amount").val();
			var ret = PJ.grid_getSelectedData(grid);
			var profitmargin=ret["profitmargin"];
			var quoteAmount = ret["quoteAmount"];
			var quoteRemark = ret["quoteRemark"];
			var quotePrice = ret["quotePrice"];
			var profitMargin = ret["profitMargin"];
			var supplierQuoteElementId = ret["id"];
			var location= ret["location"];
			var freight=ret["freight"];
			var counterFee=ret["counterFee"];
			var hazmatFee=ret["hazmatFee"];
			var feeForExchangeBill = ret["feeForExchangeBill"];
			var price=ret["price"];
			var fixedCost=$("#fixedCost").val();
			var bankCharges=$("#bankCharges").val();
			var bankChargesFee = 0;
			if(""==bankCharges){
				bankCharges=0;
			}else if(parseFloat(bankCharges)>=1) {
				bankChargesFee = bankCharges
				bankCharges = 0
			}
			if(""==freight){
				freight=0;
			}
			if(""==counterFee||counterFee<1){
				counterFee=0;
			}
			if(""==feeForExchangeBill||feeForExchangeBill<1){
				feeForExchangeBill=0;
			}
			if(""==hazmatFee||hazmatFee<1){
				hazmatFee=0;
			}
			if(null==fixedCost||""==fixedCost||parseFloat(fixedCost)>1){
				fixedCost=0;
			}
			counterFee = counterFee/amount
			feeForExchangeBill = feeForExchangeBill/amount
			hazmatFee = hazmatFee/amount
			profitmargin=1-(1/parseFloat(profitmargin));
			quotePrice=((parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(bankChargesFee))/(1-parseFloat(profitmargin))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
			profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(bankChargesFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
			$("#profitMargin").val(profitMargin+"%");
			$("#price").val(quotePrice);
		}); */
		
		
	});
	
	/* function onPriceChange() {
		var sqPrice = $("#supplier_quote_price").val();
		var price = $("#price").val();
		var freight=$("#freight").val();
		var counterFee=$("#counterFee").val();
		var fixedCost=$("#fixedCost").val();
		var bankCharges=$("#bankCharges").val();
		var feeForExchangeBill = $("#feeForExchangeBill").val();
		var hazmatFee = $("#hazmatFee").val();
		 if(""==bankCharges||parseFloat(bankCharges)>1){
			 bankCharges=0;
			}
		 if(""==freight){
				freight=0;
			}
		 if(""==counterFee){
				counterFee=0;
			} *//* else if(counterFee<1){
				counterFee=counterFee*parseFloat(sqPrice);
			} */
			
		 /*
		if(parseFloat(fixedCost)<1){
			fixedCost=parseFloat(price)*parseFloat(fixedCost);
		}else if(parseFloat(fixedCost)>1){
			fixedCost=0;
		}  */
		
		/* if(""==feeForExchangeBill){
			feeForExchangeBill=0;
		}
		if(""==hazmatFee){
			hazmatFee=0;
		}
		var amount = $("#amount").val(); */
		/* counterFee = counterFee/amount
		feeForExchangeBill = feeForExchangeBill/amount
		hazmatFee = hazmatFee/amount */
		/* if(null==price||""==price){
			$("#profitMargin").val("");
		}else{
			
			if(null==fixedCost||""==fixedCost||parseFloat(fixedCost)>1){
				fixedCost=0;
			}
			$("#profitMargin").val(((1-(parseFloat(sqPrice)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee))/(price*(1-parseFloat(fixedCost))))*100).toFixed(1)+'%'); */
			/* else{
				bankCharges=price*bankCharges;
				$("#profitMargin").val(((parseFloat(price)-parseFloat(bankCharges)- (parseFloat(sqPrice)+parseFloat(freight))) / parseFloat(price) * 100).toFixed(1)+'%');
			} */
	/* 	if(""!=freight){
		$("#profitMargin").val(((parseFloat(price) - fixedCost- (parseFloat(sqPrice)+parseFloat(freight))) / price * 100).toFixed(1)+'%');
		}else{
			$("#profitMargin").val(((parseFloat(price) - fixedCost- parseFloat(sqPrice)) / parseFloat(price) * 100).toFixed(1)+'%');
		} */
		
		//if(""!=freight){
		//	$("#profitMargin").val(((parseFloat(price) - fixedCost- (parseFloat(sqPrice)+parseFloat(freight))) / price * 100).toFixed(1)+'%');
		//}else{
		//	$("#profitMargin").val(((parseFloat(price) - parseFloat(sqPrice)) / parseFloat(price) * 100).toFixed(1)+'%');
		//}
		/* }
		
		
	var averagePrice = $("#average_price").val();
	} */
	
	function onPriceChange() {
		var amount = $("#amount").val();
		var moq = $("#moq").val();
		if(moq != ""){
			if(parseFloat(moq) > parseFloat(amount)){
				amount = moq;
			}
		}
		var ret = PJ.grid_getSelectedData(grid);
		if(ret != null){
			var profitmargin=ret["profitmargin"];
			var quoteAmount = ret["quoteAmount"];
			var quoteRemark = ret["quoteRemark"];
			var quotePrice = ret["quotePrice"];
			var profitMargin = ret["profitMargin"];
			var supplierQuoteElementId = ret["id"];
			var location= ret["location"];
			var freight=ret["freight"];
			var counterFee=ret["counterFee"];
			var hazmatFee=ret["hazmatFee"];
			var otherFee = ret["otherFee"];
			var feeForExchangeBill = ret["feeForExchangeBill"];
			var price=ret["price"];
			var clientQuotePrice = $("#price").val();
			var fixedCost=$("#fixedCost").val();
			var bankCharges=$("#bankCharges").val();
			var bankChargesFee = 0;
			if(""==bankCharges){
				bankCharges=0;
			}else if(parseFloat(bankCharges)>=1) {
				bankChargesFee = bankCharges
				bankCharges = 0
			}
			if(""==freight){
				freight=0;
			}
			if(""==counterFee){
				counterFee=0;
			}
			if(""==feeForExchangeBill){
				feeForExchangeBill=0;
			}
			if(""==hazmatFee){
				hazmatFee=0;
			}
			if(""==otherFee){
				otherFee=0;
			}
			var fixedCostFee = 0;
			if(null==fixedCost||""==fixedCost){
				fixedCost=0;
			}else if(parseFloat(fixedCost)>=1) {
				fixedCostFee = fixedCost
				fixedCost = 0
			}
			counterFee = counterFee/amount
			feeForExchangeBill = feeForExchangeBill/amount
			//hazmatFee = hazmatFee/amount
			otherFee = otherFee/amount
			profitmargin=1-(1/parseFloat(profitmargin));
			//profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
			//profitMargin=((1-price/((parseFloat(clientQuotePrice)-parseFloat(hazmatFee)-parseFloat(feeForExchangeBill)-parseFloat(otherFee)-parseFloat(counterFee)-parseFloat(bankChargesFee)-parseFloat(fixedCostFee)-parseFloat(clientQuotePrice)*(parseFloat(fixedCost)+parseFloat(bankCharges)))))*100).toFixed(1)
			var p = clientQuotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - clientQuotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges));
			profitMargin=((clientQuotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - clientQuotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
									(clientQuotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - clientQuotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)
			if(p < 0){
				profitMargin = -profitMargin;
			}
			$("#profitMargin").val(profitMargin+"%");
		}
	}
	
	function change(){
		var amount = $("#amount").val();
		var moq = $("#moq").val();
		if(moq != ""){
			if(parseFloat(moq) > parseFloat(amount)){
				amount = moq;
			}
		}
		var ret = PJ.grid_getSelectedData(grid);
		var profitmargin=ret["profitmargin"];
		var quoteAmount = ret["quoteAmount"];
		var quoteRemark = ret["quoteRemark"];
		var quotePrice = ret["quotePrice"];
		var profitMargin = ret["profitMargin"];
		var supplierQuoteElementId = ret["id"];
		var location= ret["location"];
		var freight=ret["freight"];
		var counterFee=ret["counterFee"];
		var hazmatFee=ret["hazmatFee"];
		var otherFee = ret["otherFee"];
		var feeForExchangeBill = ret["feeForExchangeBill"];
		var price=ret["price"];
		var fixedCost=$("#fixedCost").val();
		var bankCharges=$("#bankCharges").val();
		var bankChargesFee = 0;
		if(""==bankCharges){
			bankCharges=0;
		}else if(parseFloat(bankCharges)>=1) {
			bankChargesFee = bankCharges
			bankCharges = 0
		}
		if(""==freight){
			freight=0;
		}
		if(""==counterFee){
			counterFee=0;
		}
		if(""==feeForExchangeBill){
			feeForExchangeBill=0;
		}
		if(""==hazmatFee){
			hazmatFee=0;
		}
		if(""==otherFee){
			otherFee=0;
		}
		var fixedCostFee = 0;
		if(null==fixedCost||""==fixedCost){
			fixedCost=0;
		}else if(parseFloat(fixedCost)>=1) {
			fixedCostFee = fixedCost
			fixedCost = 0
		}
		counterFee = counterFee/amount
		feeForExchangeBill = feeForExchangeBill/amount
		//hazmatFee = hazmatFee/amount
		otherFee = otherFee/amount
		profitmargin=1-(1/parseFloat(profitmargin));
		quotePrice=(((parseFloat(price))/(1-parseFloat(profitmargin))+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
		//profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
		profitMargin=((quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
								(quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee)- quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)
		var p = quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee)- quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))
		if(p < 0){
			profitMargin = -profitMargin;
		}
		$("#profitMargin").val(profitMargin+"%");
		$("#price").val(quotePrice);
	}
	
	function profitMarginChange(){
		var amount = $("#amount").val();
		var moq = $("#moq").val();
		if(moq != ""){
			if(parseFloat(moq) > parseFloat(amount)){
				amount = moq;
			}
		}
		var ret = PJ.grid_getSelectedData(grid);
		var profitmargin=$("#profitMargin").val();
		var quoteAmount = ret["quoteAmount"];
		var quoteRemark = ret["quoteRemark"];
		var quotePrice = ret["quotePrice"];
		var profitMargin = ret["profitMargin"];
		var supplierQuoteElementId = ret["id"];
		var location= ret["location"];
		var freight=ret["freight"];
		var counterFee=ret["counterFee"];
		var hazmatFee=ret["hazmatFee"];
		var otherFee = ret["otherFee"];
		var feeForExchangeBill = ret["feeForExchangeBill"];
		var price=ret["price"];
		var fixedCost=$("#fixedCost").val();
		var bankCharges=$("#bankCharges").val();
		var bankChargesFee = 0;
		if(""==bankCharges){
			bankCharges=0;
		}else if(parseFloat(bankCharges)>=1) {
			bankChargesFee = bankCharges
			bankCharges = 0
		}
		if(""==freight){
			freight=0;
		}
		if(""==counterFee){
			counterFee=0;
		}
		if(""==feeForExchangeBill){
			feeForExchangeBill=0;
		}
		if(""==hazmatFee){
			hazmatFee=0;
		}
		if(""==otherFee){
			otherFee=0;
		}
		var fixedCostFee = 0;
		if(null==fixedCost||""==fixedCost){
			fixedCost=0;
		}else if(parseFloat(fixedCost)>=1) {
			fixedCostFee = fixedCost
			fixedCost = 0
		}
		counterFee = counterFee/amount
		feeForExchangeBill = feeForExchangeBill/amount
		//hazmatFee = hazmatFee/amount
		otherFee = otherFee/amount
		//profitmargin=1-(1/parseFloat(profitmargin));
		quotePrice=(((parseFloat(price))/(1-parseFloat(profitmargin/100))+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(1-parseFloat(fixedCost)-parseFloat(bankCharges))).toFixed(4);
		//profitMargin=((1-(parseFloat(price)+parseFloat(freight)+parseFloat(counterFee)+parseFloat(feeForExchangeBill)+parseFloat(hazmatFee)+parseFloat(otherFee)+parseFloat(bankChargesFee)+parseFloat(fixedCostFee))/(quotePrice*(1-parseFloat(fixedCost)-parseFloat(bankCharges))))*100).toFixed(1);
		profitMargin=((quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee) - parseFloat(price) - quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges)))/
								(quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee)- quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))) * 100).toFixed(1)
		var p = quotePrice - parseFloat(freight) - parseFloat(counterFee) - parseFloat(feeForExchangeBill) - parseFloat(hazmatFee) - parseFloat(otherFee) - parseFloat(bankChargesFee) - parseFloat(fixedCostFee)- quotePrice*(parseFloat(fixedCost)+parseFloat(bankCharges))
		if(p < 0){
			profitMargin = -profitMargin;
		}
		$("#profitMargin").val(profitMargin+"%");
		$("#price").val(quotePrice);
	}
	
	//获取表单数据
	 function getFormData(){
			var $input = $("#Form").find("input[type=text]");
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
	
	 function onlyNum()
	 {
		 var number=$("#leadTime").val();
	 if(isNaN(number)){
		 $("#msg").html("周期必须是数字!");
	 }else if(!isNaN(number)){
		 $("#msg").html("");
	 }
	 }
	
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:20px;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
/* th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
}  */
</style>
</head>

<body style="padding:3px">
<input type="text" class="hide" name="client_inquiry_quote_number" value="${client_inquiry_quote_number}">
	<div id="layout_main">
		<div position="center" title="询价单号:  ${clientInquiryQuoteNumber}   件号：${clientInquiryElement.partNumber}    描述：${clientInquiryElement.description}     数量：${clientInquiryElement.amount}    单位：${clientInquiryElement.unit}    备注：${clientInquiryElement.remark}">
		<form id="Form">
		<input type="text" class="hide" name="supplier_quote_price" id="supplier_quote_price" value="">
		<input type="text" class="hide" name="relativeRate" id="relativeRate" value="${clientQuoteElement.relativeRate}">
		<input type="text" class="hide" name="clientQuoteId" id="clientQuoteId" value="${clientQuoteId}">
		<input type="text" class="hide" name="clientInquiryElementId" id="clientInquiryElementId" value="${clientInquiryElement.id}">
		<input type="text" class="hide" name="supplierQuoteElementId" id="supplierQuoteElementId" value="${supplierQuoteElementId}">
		<%-- <input type="text" class="hide" name="fixedCost" id="fixedCost" value="${fixedCost}"> --%>
		<input type="text" class="hide" name="id" id="id" value="${id}">
		<input type="text" class="hide" name="type" id="type" value="${optType}">
		<input type="text" class="hide" name="freight" id="freight" value="${freight}">
		<input type="text" class="hide" name="counterFee" id="counterFee" value="${counterFee}">
		<input type="text" class="hide" name="feeForExchangeBill" id="feeForExchangeBill" value="${feeForExchangeBill}">
		<input type="text" class="hide" name="hazmatFee" id="hazmatFee" value="${hazmatFee}">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
							       <form:left><p>MOQ<input id="moq"  type="text"  prefix="" name="moq" class="text" style="width: 60px" value="${clientQuoteElement.moq}" onBlur="change()"/></p></form:left>
							       <form:right><p>数量<input id="amount" type="text"  prefix="" name="amount" class="text" style="width: 60px"  value="${clientQuoteElement.amount}" onBlur="change()"/></p></form:right>
							   	</form:column>
							<form:column>
							       <form:left><p>单价</p></form:left>
							      <form:right><p> <input id="price" type="text"  prefix="" name="price" class="text"   value="${clientQuoteElement.price}" onchange="onPriceChange()"/></p></form:right>
							 	</form:column>
							<form:column>
							       <form:left><p>利润率</p></form:left>
							      <form:right><p> <input id="profitMargin"  prefix="" name="profitMargin" class="text"  value="" onchange="profitMarginChange()"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>平均价格</p></form:left>
								<form:right><p><input id="averagePrice" type="text"  prefix="" name="averagePrice" class="text" disabled="disabled" value="${averagePrice}"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>最低报价：</p></form:left>
								<form:right><p><input id="lowestPrice" type="text"  prefix="" name="bottomPrice" class="text" disabled="disabled" value="${lowestPrice}"/></p></form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column>
							       <form:left><p>比较比率：</p></form:left>
							      <form:right><p> <input id="compareRate" type="text"  prefix="" name="compareRate" class="text" disabled="disabled" value="${compareRate}"/></p></form:right>
							</form:column>
							<form:column>
							       <form:left><p>备注：</p></form:left>
							           <form:right><p><input id="remark" type="text"  prefix="" name="remark" class="text"  value="${clientQuoteElement.remark}"/></p></form:right>
							</form:column>
							<form:column>
							       <form:left><p>到货周期<input id="leadTime" onkeyup="onlyNum()" type="text"  prefix="" name="leadTime" class="text" style="width: 30px" value="${clientQuoteElement.leadTime}"/>天<span id="msg" style="color: red;"></span></p></form:left>
							       <form:right>
							           	银行费用<input id="bankCharges"  type="text"  prefix="" name="bankCharges" class="text" style="width: 40px" value="${bankCharges}" onBlur="change()"/>
							       </form:right>
								  <%-- <form:left><p>
								  				到货周期
								  			</p>
								  </form:left>
								  <form:right>
								  			<input id="leadTime" onkeyup="onlyNum()" type="text"  prefix="" name="leadTime" class="text" style="width: 30px" value="${clientQuoteElement.leadTime}"/>天<span id="msg" style="color: red;"></span>
								  </form:right> --%>
									
							</form:column>
								<form:column>
							       <form:left><p>location：</p></form:left>
							           <form:right><p><input id="location" type="text"  prefix="" name="location" class="text"  value="${clientQuoteElement.location}"/></p></form:right>
							</form:column>
							<form:column >
								<form:left><p>佣金：<input type="text" class="text"   style="width:45px;" name="fixedCost" id="fixedCost" value="${fixedCost}" onBlur="change()"/></p></form:left>
								<form:right>
									<p style="padding-left:40px;">
									       <input class="btn btn_orange <c:if test="${optType eq 'edit'}">hide</c:if>" type="button" value="新增" id="addBtn"/>		
									         <input class="btn btn_orange <c:if test="${optType eq 'add'}">hide</c:if>" type="button" value="修改" id="editBtn" />		
									</p>
								</form:right>
							</form:column>
						</form:row>
					
					</div>
				</form>
		<div id="toolbar"></div>
		
			<table id="list"></table>
			<div id="pager1"></div> 
			<!-- <table id="list2"></table>
			 <div id="pager1"></div>  -->
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
