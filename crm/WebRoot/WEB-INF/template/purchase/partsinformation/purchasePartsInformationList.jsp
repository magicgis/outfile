<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-部件资料</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		var data = [];
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [
				{	icon : 'logout',
					text : '导出excel',
					click : function() {
							PJ.grid_export("list1");
							}
				 }
					/* {
						id:'search',
						icon : 'search',
						text : '展开搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
								var display = $("#searchdiv").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid.setGridHeight(PJ.getCenterHeight()-262);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-162);
								}
							});
						}
					} */
					]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 100,
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-262,
			autoSrcoll:true,
			shrinkToFit:false,
			sortname : "so.order_date desc,sq.quote_date desc,sqe.supplier_quote_status_id",
			//sortorder : "desc",
			pager: "#pager1",
			colNames :["id","item","csn","件号","描述","客户","询价单号","询价日期","单位","数量","件号","描述","创建人","供应商","价格","单位","数量","币种","证书","状态","周期","location",
			          "备注","报价状态","供应商询价单号","报价日期","Core Charge", "warranty","serialNumber","tagSrc","tagDate","trace","供应商","订单号","订单日期","截至日期","数量","单价","总价"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("inquiryPartNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("inquiryDescription", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,width:135,align:'left'}),
			           PJ.grid_column("inquiryDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("inquiryUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("inquiryAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("quoteDescription", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("userName", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("supplierBasePrice", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("quoteUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quoteAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("supplierCurrencyValue", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("leadTime", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("quoteRemark", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("supplierInquiryQuoteNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("supplierQuoteDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("coreCharge", {sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("warranty", {sortable:true,width:80,editable:true,align:'left'}),
					   PJ.grid_column("serialNumber", {sortable:true,width:100,editable:true,align:'left'}),
					   PJ.grid_column("tagSrc", {sortable:true,width:80,editable:true,align:'left'}),
					   PJ.grid_column("tagDate", {sortable:true,width:80,editable:true,align:'left'}),
					   PJ.grid_column("trace", {sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("supplierOrderCode", {sortable:true,width:50,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderNumber", {sortable:true,width:120,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderDate", {sortable:true,width:80,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderDeadline", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("supplierOrderAmout", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("supplierOrderBasePrice", {sortable:true,width:70,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("supplierOrderTotalPrice", {sortable:true,width:80,align:'left'}),
			           ]
		});
		
		//供应商代码
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/purchase/supplierinquiry/getSuppliers',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#supplier").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});//客户代码来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/clientCode',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#client").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		//机型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/airType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
						$("#searchForm3").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//商业类型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/bizType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm4").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	if($("#partNumber").val()!=""||$("#inquiryNumber").val()!=""||$("#supplier").val()!=""
					 ||$("#client").val()!=""||$("#searchForm3").val()!=""||$("#searchForm4").val()!=""
						 ||$("#orderStart").val()!=""||$("#orderEnd").val()!=""){
		    		 var partNumber = $("#partNumber").val();
		    		 var way = 'like';
		    		 if($("#exact").prop("checked")){
		    			 way = 'equal'
		 			}
					 PJ.grid_search(grid,'<%=path%>/market/partsinformation/purchaseinformation?partNumber='+partNumber+'&way='+way);
				 }
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 if($("#partNumber").val()!=""||$("#inquiryNumber").val()!=""||$("#supplier").val()!=""
				 ||$("#client").val()!=""||$("#searchForm3").val()!=""||$("#searchForm4").val()!=""
					 ||$("#orderStart").val()!=""||$("#orderEnd").val()!=""){
				 var partNumber = $("#partNumber").val();
				 var way = 'like';
	    		 if($("#exact").prop("checked")){
	    			 way = 'equal'
	 			 }
				 PJ.grid_search(grid,'<%=path%>/market/partsinformation/purchaseinformation?partNumber='+partNumber+'&way='+way);
			 }
			 
		 });
		
		//搜索
		 <%-- $("#searchBtn").click(function(){
			 if($("#partNumber").val()!=""||$("#inquiryNumber").val()!=""||$("#supplier").val()!=""
				 ||$("#client").val()!=""||$("#searchForm3").val()!=""||$("#searchForm4").val()!=""
					 ||$("#orderStart").val()!=""||$("#orderEnd").val()!=""){
				 var partNumber = $("#partNumber").val();
				 var way = 'like';
	    		 if($("#exact").prop("checked")){
	    			 way = 'equal'
	 			 }
				 var postData = {};
				 var searchString = getSearchString();
				 postData.searchString = searchString;
				 PJ.ajax({
						url: '<%=path%>/market/partsinformation/purchaseinformationOnce?partNumber='+partNumber+'&way='+way,
						type: "POST",
						loading: "正在处理...",
						data: postData,
						dataType: "json",
						success: function(result){
								if(result.success){
									data = eval(result.message)[0];
									//PJ.grid_reload(grid);
									$("#list1").jqGrid('clearGridData');
									$("#list1").jqGrid('setGridParam',{  // 重新加载数据
									      datatype:'local',
									      data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
									      page:1
									}).trigger("reloadGrid");
								}else{
									setTimeout(function(){
										PJ.hideLoading();
									}, 5000);
								}	
						},
						error: function (data, status, e) { 
						    PJ.error("上传异常！");
						}
				});
			 }
		 }); --%>
		
		 function getSearchString() {
				var flag = true, searchString = "";
				$(document).find("[alias]").each(function(i, e) {
					if (!$(e).val() || !$.trim($(e).val()))
						return true;
					
					var result = checkRule(e);
					flag = result[0];
					if (flag) {
						var value = $(e).val();
						//var name = $(e).attr("prefix");
						//if (name) {
							//name += ".";
						//}else{
							//name="";
						//}
						var alias = $(e).attr("alias").split(" or ");
						if(alias.length>=2){
							if(!searchString){
								searchString += " ( ";
							}else{
								searchString += " and ( ";
							}
							
							$(alias).each(function(index){
								//name += alias[index];
								var str = getRuleString(alias[index], result[1], value, result[2]);
								if (searchString && str){
									if(searchString.substring(searchString.length-2, searchString.length) != "( "){
										searchString += " OR ";
									}
								}
								/*if(!searchString){
									searchString += " ( ";
								}*/
								searchString += str;
							});
							searchString += " ) ";
						}else{
							//name += $(e).attr("alias");
							var str = getRuleString($(e).attr("alias"), result[1], value, result[2]);
							if (searchString && str){
								searchString += " AND ";
							}	
								searchString += str;
						}
						
					} else {
						return false;
					}
				});
				return searchString;
			};
		
		
		//供应商能力清单
		 $("#ability").click(function(){
			 if($("#partNumber").val()!=""||$("#inquiryNumber").val()!=""||$("#supplier").val()!=""
				 ||$("#client").val()!=""||$("#searchForm3").val()!=""||$("#searchForm4").val()!=""
					 ||$("#orderStart").val()!=""||$("#orderEnd").val()!=""){
				 	var flag = true, searchString = "";
					$("#searchForm").find("[alias]").each(function(i, e) {
						if (!$(e).val() || !$.trim($(e).val()))
							return true;
						
						var result = checkRule(e);
						flag = result[0];
						if (flag) {
							var value = $(e).val();
							var alias = $(e).attr("alias").split(" or ");
							if(alias.length>1){
								$(alias).each(function(index){
									//name += alias[index];
									var str = getRuleString(alias[index], result[1], value, result[2]);
									if (searchString && str){
										searchString += " or ";
									}
									if(!searchString){
										searchString += " ( ";
									}
									searchString += str;
								});
								searchString += " ) ";
							}else{
								//name += $(e).attr("alias");
								var str = getRuleString($(e).attr("alias"), result[1], value, result[2]);
								if (searchString && str){
									searchString += " AND ";
								}	
									searchString += str;
							}
							
						} else {
							return false;
						}
					});
					var iframeId="abilityIframe";
					PJ.topdialog(iframeId, '供应商能力清单', '<%=path%>/market/partsinformation/toAbility?searchString='+encodeURI(searchString),
							 undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
			 }
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
		
		
		//二级表头
		$( "#list1" ).jqGrid( 'setGroupHeaders' , {
			useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
			groupHeaders : [ {
								startColumnName :  'item' ,  // 对应colModel中的name
								numberOfColumns : 9,  // 跨越的列数
								titleText :  '<div align="center"><span>客户询价</span></div>'
							 },
			                 {
								startColumnName :  'quotePartNumber' ,  // 对应colModel中的name
								numberOfColumns : 22,  // 跨越的列数
								titleText :  '<div align="center"><span>供应商报价</span></div>'
							 },
							 {
								startColumnName :  'supplierOrderCode' ,  // 对应colModel中的name
								numberOfColumns : 7,  // 跨越的列数
								titleText :  '<div align="center"><span>供应商订单</span></div>'
							 }
							]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-302);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-302);
			}
		});
		
		$("#exact").change(function(){
			if($("#exact").prop("checked")){
				$("#partNumber").attr("oper","eq");
			}else if(!$("#exact").prop("checked")){
				$("#partNumber").attr("oper","cn");
			}
		});
		
		$("#partNumber").blur(function(){
			var text = $("#partNumber").val();
			text = trim(text);
			$("#partNumber").val(text.replace(" ", ""));
		});
		
		$("#inquiryNumber").blur(function(){
			var text = $("#inquiryNumber").val();
			$("#inquiryNumber").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var data = $("#form").serialize();
		 return data;
	}
	
	function OrderFormatter(cellvalue, options, rowObject){
		var amount = rowObject["clientOrderAmount"];
		
		switch (amount) {
			case '':
				return "无订单";
				break;
				
			default: 
				return cellvalue;
				break; 
		}
	}
	
	function CountFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '0';
			break;
			
		case '':
			return "无订单";
			break;
			
		default: 
			return cellvalue;
			break; 
		}
	}
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 130px;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<%-- <form:right><p><input name="partNumber" class="text" id="partNumber" /></p></form:right> --%>
								<form:right><p><input name="partNumber" class="text" id="partNumber"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>精确匹配</p></form:left>
								<form:right><p><input name="exact" id="exact" type="checkbox"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>单号:</p></form:left>
								<form:right><p><input name="inquiryNumber" class="text" id="inquiryNumber" alias="ci.quote_number or so.order_number" oper="cn"/></p></form:right>
							</form:column>
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="client" name="clientCode" alias="c.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>供应商：</p></form:left>
								<form:right><p><select id="supplier" name="supplierCode" alias="s. code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
						</form:row>	
						<form:row columnNum="5">
							<form:column>
								<form:left>机型：</form:left>
								<form:right><p>
													<select id="searchForm3" name="airTypeValue" alias="at.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>商业类型：</form:left>
								<form:right><p>
													<select id="searchForm4" name="bizTypeCode" alias="bt.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="orderStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'orderEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="orderStart" alias="so.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="orderEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="orderEnd" alias="so.order_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<p style="padding-left:45px;">
									<input class="btn btn_orange" type="button" value="供应商能力清单" id="ability"/>
								</p>	
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
						
							<form:column>
							<p style="padding-left:45px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
							</form:column>
							
						</form:row>	
					</div>
				</form>
			</div>		
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>