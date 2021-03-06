<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>选择客户询价单</title>

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
								    //var ret = PJ.grid_getSelectedData(grid);
						 		    var id = getData();
									//根据具体业务提供相关的title
									var title = 'excel管理';
									//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
									//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
									var businessKey = 'lot_order_element.id.'+id+'.LotOrderElementExcel';
									PJ.excelDiaglog({
										data:'businessKey='+businessKey,
										title: title,
										add:true,
										remove:true,
										download:true
									});
							}
						
					 }
			         ]
		});
		grid = PJ.grid("list1", {
			rowNum: 500,
			url:'<%=path%>/market/clientweatherorder/listData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-202,
			autoSrcoll:true,
			multiselect:true,
			//shrinkToFit:false,
			sortname : "cwo.update_timestamp",
			sortorder : "desc",
			colNames :["id","报价Id","客户ID","订单号","客户订单号","订单日期","预付","发货","帐期","验货","账期","证书","币种","汇率","状态","备注","更新时间"],
			colModel :[PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("clientQuoteId", {hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("orderNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("sourceOrderNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("orderDate", {sortable:true,width:110,sortname : "co.order_number",align:'left'}),
			           PJ.grid_column("prepayRate", {sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           
			           PJ.grid_column("shipPayRate", {sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           PJ.grid_column("shipPayPeriod",{sortable:true,width:80}),
			           PJ.grid_column("receivePayRate", {sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}
			           ,align:'left'}),
			           PJ.grid_column("receivePayPeriod",{sortable:true,width:80}),
			           PJ.grid_column("certification",{sortable:true,width:200}),
			           PJ.grid_column("currencyValue",{sortable:true,width:60}),
			           PJ.grid_column("exchangeRate", {sortable:true,width:50,
			        	   formatter: function(value){
								if (value) {
									return value.substr(0, 4);
								} else {
									return '';							
								}
							},align:'left'}),
			           PJ.grid_column("orderStatusValue",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:250,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'})
			           ]
		});
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/market/clientweatherorder/listData');
		    }  
		}); 
		
		$("#searchBtn").click(function(){
			PJ.grid_search(grid,'<%=path%>/market/clientweatherorder/listData');
			
		});
		//重置条件
		$("#resetBtn").click(function(){
			$("#searchForm")[0].reset();
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
		
	});
	
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
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  line-height:20px;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
/*  th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
} */
</style>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
<input type="text" name="isBlacklist" id="isBlacklist" class="hide"/>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>订单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="orderNumber" alias="cwo.order_number or cwo.source_number" oper="cn"/> </p></form:right>
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
						</form:row>
						<form:row columnNum="5">	
							<form:column>
								<form:left><p>订单日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="cwo.order_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="cwo.order_date" oper="lt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> </p></form:left>
								<form:right><p> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> </p></form:left>
								<form:right><p> </p></form:right>
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