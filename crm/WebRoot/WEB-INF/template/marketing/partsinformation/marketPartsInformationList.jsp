<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>销售-部件资料</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		
		
		$("#toolbar").ligerToolBar({
			items : [
					{
					    id:'down',
						icon : 'down',
						text : '下载',
						click: function(){
					 		    var searchString = getSearchString().replace(/'/g, "/");
					 		    while(searchString.indexOf(".")>0){
					 		    	searchString = searchString.replace(".", ":");
					 		    }
					 		   while(searchString.indexOf("%")>0){
					 		    	searchString = searchString.replace("%", "~");
					 		    }
					 		    var userId = ${userId};
					 		    var id = userId +","+searchString;
								//根据具体业务提供相关的title
								var title = 'excel管理';
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var businessKey = 'part_information.id.'+id+'.PartInformationExcel';
								PJ.excelDiaglog({
									data:'businessKey='+businessKey,
									title: title,
									add:true,
									remove:true,
									download:true
								});
						}
					},
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
								var display = $("#searchdiv").css("play");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid.setGridHeight(PJ.getCenterHeight()-242);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-142);
								}
							});
						}
					} */
					 
					]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 100,
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-232,
			autoSrcoll:true,
			shrinkToFit:false,
			sortname : "coe.id desc,sqe.id",
			sortorder : "desc",
			colNames :["id","item","csn","件号","描述","客户","询价单号","询价日期","单位","数量","件号","描述","供应商","供应商询价单号","报价日期","单位","数量","币种","价格","状态","备注","报价状态","客户报价日期","客户数量","客户价格","订单号","订单日期","数量","价格","状态","备注","竞争对手报价"],
			colModel :[PJ.grid_column("id", {key:true,width:30,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("inquiryPartNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("inquiryDescription", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("inquiryDate", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("inquiryUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("inquiryAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quotePartNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("quoteDescription", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("supplierInquiryQuoteNumber", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("supplierQuoteDate", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("quoteUnit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("quoteAmount", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("supplierCurrencyValue", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("supplierBasePrice", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("quoteRemark", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("clientQuoteDate", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("clientQuoteAmount", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("clientBasePrice", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:130,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("clientOrderDate", {sortable:true,width:80,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("clientOrderAmout", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("clientOrderBasePrice", {sortable:true,width:70,align:'left',
			        	   formatter: OrderFormatter   
			           }),
			           PJ.grid_column("orderStatus", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("orderRemark", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("competitorPrice", {sortable:true,width:90,align:'left'})
			           ]
		});
		
		//客户代码来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/clientCode?all=1',
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
						$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
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
		    	if($("#partNumber").val()!=""||$("#inquiryNumber").val()!=""||$("#client").val()!=""
					 ||$("#searchForm3").val()!=""||$("#searchForm4").val()!=""||$("#inquiryStart").val()!=""
						 ||$("#inquiryEnd").val()!=""){
					 PJ.grid_search(grid,'<%=path%>/market/partsinformation/marketinformation');
				}
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 if($("#partNumber").val()!=""||$("#inquiryNumber").val()!=""||$("#client").val()!=""
				 ||$("#searchForm3").val()!=""||$("#searchForm4").val()!=""||$("#inquiryStart").val()!=""
					 ||$("#inquiryEnd").val()!="" || $("#description").val() != ""){
				 PJ.grid_search(grid,'<%=path%>/market/partsinformation/marketinformation');
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
									numberOfColumns : 12,  // 跨越的列数
									titleText :  '<div align="center"><span>供应商报价</span></div>'
							 },
							 {
								startColumnName :  'clientQuoteDate' ,  // 对应colModel中的name
								numberOfColumns : 3,  // 跨越的列数
								titleText :  '<div align="center"><span>客户报价</span></div>'
							 },
							 {
								startColumnName :  'clientOrderNumber' ,  // 对应colModel中的name
								numberOfColumns : 6,  // 跨越的列数
								titleText :  '<div align="center"><span>客户订单</span></div>'
							 }
							]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("play");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-232);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-232);
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
			$("#partNumber").val(trim(text));
		});
		
		$("#inquiryNumber").blur(function(){
			var text = $("#inquiryNumber").val();
			$("#inquiryNumber").val(trim(text));
		});
		
		function getSearchString(){
			var flag = true, searchString = "";
			$(document).find("[alias]").each(function(i, e) {
				if (!$(e).val() || !$.trim($(e).val())){
					return searchString;
				}
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
					
				} 
			});
			return searchString;
		}
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
			case '无订单':
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
			return '无订单';
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
			<div id="searchdiv" style="width: 100%;height: 100px;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input name="partNumber" class="text" id="partNumber" alias="e.PART_NUMBER_CODE" oper="cn"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>精确匹配</p></form:left>
								<form:right><input name="exact" id="exact" type="checkbox"/></form:right>
							</form:column>
							<form:column>
								<form:left><p>单号:</p></form:left>
								<form:right><p><input name="inquiryNumber" class="text" id="inquiryNumber" alias="ci.quote_number" oper="cn"/></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>客户：</p></form:left>
								<form:right><p><select id="client" name="clientCode"  alias="c.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>机型：</form:left>
								<form:right><p>
													<select id="searchForm3" name="airTypeValue" alias="at.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">
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
							<form:left><p>询价日期：</p></form:left>
							<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ci.inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ci.inquiry_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>描述：</p></form:left>
								<form:right><p><input name="description" class="text" id="description" alias="cie.DESCRIPTION" oper="cn"/></p></form:right>
							</form:column>
							<form:column>
							<p style="padding-left:60px;">
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