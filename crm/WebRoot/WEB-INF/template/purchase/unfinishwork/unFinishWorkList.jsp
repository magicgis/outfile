<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>未收货清单 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />

<script type="text/javascript">
var layout, grid;

$(function(){
	//$("#jqgh_list1_cb").css("style","padding-top: 0px")
	layout = $("#layout1").ligerLayout();
	
	$("#toolbar").ligerToolBar({
		items : [ {	icon : 'logout',
					text : '导出excel',
					click : function() {
							PJ.grid_export("list1");
							}
				  },
				  {
						id:'down',
						icon : 'down',
						text : '下载',
						click: function(){
							    //var ret = PJ.grid_getSelectedData(grid);
					 		    var id = getData();
								//根据具体业务提供相关的title
								var title = 'excel管理';
								//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
								//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
								var businessKey = 'lot_supplier_unfinish_element.id.'+id+'.LotSupplierUnfinishElementExcel';
								PJ.excelDiaglog({
									data:'businessKey='+businessKey,
									title: title,
									add:true,
									remove:true,
									download:true
								});
						}
					
				 },
		          {
						id:'search',
						icon : 'search',
						text : '展开搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
								var display = $("#searchdiv").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
									grid.setGridHeight(PJ.getCenterHeight()-202);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
									grid.setGridHeight(PJ.getCenterHeight()-102);
								}
							});
						}
				 }
				]
	});
	
	$("#exact").change(function(){
		if($("#exact").prop("checked")){
			$("#partNumber").attr("oper","eq");
		}else if(!$("#exact").prop("checked")){
			$("#partNumber").attr("oper","cn");
		}
	});
	
	//客户代码来源
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
					$("#searchForm1").append($option);
				}
			}else{
				
					PJ.showWarn(result.msg);
			}
		}
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
					$("#searchForm5").append($option);
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
	
	grid = PJ.grid("list1", {
		rowNum: 60,
		url:'<%=path%>/purchase/supplierorder/unFinish',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-102,
		autoSrcoll:true,
		shrinkToFit:true,
		sortname : "soe.deadline",
		sortorder : "desc",
		multiselect:true,
		colNames :["id","客户","订单号","供应商","供应商订单号","件号","描述","单位","未到货数量","周期","单价","总价","合同时间","截至日期","备注"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("clientCode", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("clientOrderNumber", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("supplierCode", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("supplierOrderNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("quotePartNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("quoteDescription", {sortable:true,width:150,align:'left'}),
		           PJ.grid_column("quoteUnit", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("amount", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("leadTime", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("supplierOrderPrice", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("supplierOrderTotalPrice", {sortable:true,width:80,align:'left'}),
		           PJ.grid_column("orderDate", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("deadline", {sortable:true,width:100,
		        	   editoptions:{ 
		        		   dataInit:function(el){ 
		        		     $(el).click(function(){ 
		        		       WdatePicker(); 
		        		     }); 
		        		   } 
		        	   }    
		           ,align:'left'}),
		           PJ.grid_column("remark", {sortable:true,width:150,align:'left'})
		           ]
	});
	
	function CountFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '0';
			break;
			
		default: 
			return cellvalue;
			break; 
	}
	}
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	var client = $("#searchForm1").val();
			var supplier = $("#searchForm5").val();
	    	PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/unFinish');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		var client = $("#searchForm1").val();
		var supplier = $("#searchForm5").val();
		let partNumber = $("#partNumber").val();
		console.log(partNumber);
		//if(client!=""||supplier!=""){
			PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/unFinish?partNumber='+partNumber);
		//}else{
			//PJ.warn("必须选择一个客户或供应商！");
		//}
		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	$("#searchForm2").blur(function(){
		var text = $("#searchForm2").val();
		$("#searchForm2").val(trim(text));
	});
	
	
});

function trim(str){
	return $.trim(str);
}

function OrderFormatter(cellvalue, options, rowObject){
	var orderAmount = rowObject["supplierOrderAmount"];
	var importAmount = rowObject["importAmount"];
	var amount = orderAmount.parseInt()-importAmount.parseInt();
	return amount;
}

function getData(){
	var postData = {};
	var inquiryId =  PJ.grid_getMutlSelectedRowkey(grid);
	ids = inquiryId.join(",");
	/* if(ids.length>0){
		postData.ids = ids;
	} */
	return ids;
 }

</script>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>
<style>
	div#jqgh_list1_cb {style:padding-top: 0px;}
</style>
<body>
<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="5">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" alias="co.order_number or so.order_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="c.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>供应商：</form:left>
								<form:right><p>
													<select id="searchForm5" name="inquiryStatusCode" alias="s. CODE" oper="eq">
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
							
							<form:column>
								<form:left>商业类型：</form:left>
								<form:right><p>
													<select id="searchForm4" name="bizTypeCode" alias="bt.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							
							
						</form:row>
						<form:row columnNum="5">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" /> </p></form:right>
							</form:column>
							<form:column>
								<form:left>状态：</form:left>
								<form:right><p>
													<select id="searchForm5" name="inquiryStatusCode" alias="sta.code" oper="eq">
							            			<option value="">全部</option>
							            			<option value="CANCEL">作废</option>
							            			<option value="NO_INQUIRY">未询价</option>
							            			<option value="QUOTED">已报</option>
							            			<option value="REFUSE">拒报</option>
							            			<option value="SEND_INQUIRY">已询价</option>
							            			<option value="SUPPLIER_QUOTE">供应商已报</option>
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
							<form:column >
							<p style="padding-left:30px;">
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