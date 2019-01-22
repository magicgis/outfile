<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>库存明细</title>

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
							id:'search',
							icon : 'search',
							text : '收起搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid.setGridHeight(PJ.getCenterHeight()-210);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-110);
									}
								});
							}
					}	
			        ]
				});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-210,
			autoSrcoll:true,
			shrinkToFit:false,
			gridComplete:function(){
				var location=$("#location").val();
					var obj = $("#list1").jqGrid("getRowData");
					//jQuery(obj).each(function(){
						if(obj.length>0){
							$("#boxWeight").val(obj[0].countWeight);
						}
						
					//});
			},
			multiselect:true,
			sortname : "a.import_date",
			sortorder : "desc",
			colNames :["id","importPackageElementId","客户","客户订单号","供应商","件号","描述","状态","证书","","符合性证明","是否完成符合性证明","单位","数量","人民币价格","总价","位置","入库单号","订单号","入库日期","运输单号","物流方式","重量(g)","总重量","备注"],
			colModel :[PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("importPackageElementId", {hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("completeComplianceCertificateValue",{hidden:true,formatter:function(value){
   	                	$("#completeComplianceCertificateValue").val(value);
	                	return value;
					}}),
			           PJ.grid_column("complianceCertificateValue", {sortable:true,width:80,align:'left',formatter:function(value){
	   	                	var completeComplianceCertificateValue=$("#completeComplianceCertificateValue").val();
	   	                	if(completeComplianceCertificateValue=="否"){
	   	                		return '<span style="color:red">'+value+'<span>';
	   	                	}else{
		                	return value;
	   	                	}
						}}),
			           PJ.grid_column("completeComplianceCertificateValue",{sortable:true,width:130,align:'left',formatter:function(value){
	   	                	if(value=="否"){
	   	                		return '<span style="color:red">'+value+'<span>';
	   	                	}else{
		                	return value;
	   	                	}
						}}),
			           PJ.grid_column("unit",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("storageAmount",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("basePrice",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("totalBasePrice",{sortable:true,width:100,align:'left'}),
			           PJ.grid_column("location",{sortable:true,width:80,align:'left'}),
			           PJ.grid_column("importNumber",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("orderNumber",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importDate",{sortable:true,width:110,align:'left'}),
			           PJ.grid_column("logisticsNo",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("logisticsValue",{sortable:true,width:110,align:'left'}),
			           PJ.grid_column("boxWeight",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("countWeight",{hidden:true}),
			           PJ.grid_column("remark",{sortable:true,width:120,align:'left'})
			           ]
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
						$("#supplierCode").append($option);
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
		    	if('${code}'==$("#searchForm1").val()){
				 	PJ.grid_search(grid,'<%=path%>/storage/detail/StorageDetail?epiId='+'${epiId}');
				 }else{
					PJ.warn("请选择正确的客户");
				 }
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 if('${code}'==$("#searchForm1").val()){
			 PJ.grid_search(grid,'<%=path%>/storage/detail/StorageDetail?epiId='+'${epiId}');
			 }else{
					PJ.warn("请选择正确的客户");
			 }
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
	});
	
	 function getFormData(){
		 var postData = {};
		 var rowKey = grid.getGridParam("selarrrow");
		 if(rowKey!= ""){
			 var ids="";
			 for(var i=0, len=rowKey.length; i<len; i++) {
					var row_id= rowKey[i];
					 var data = grid.jqGrid("getRowData",row_id);
					 ids+=row_id+"-"+data.storageAmount+",";
			     }
			 postData.ids = ids;
			/*  var id =  PJ.grid_getMutlSelectedRowkey(grid);
			 ids = id.join(",");
			 if(ids.length>0){
					postData.ids = ids;
				} */
		 }
		 if(rowKey== ""){
				PJ.warn("请选择需要操作的数据");
				return false;
		 }
			return postData;
	 }	
	
	function trim(str){
		return $.trim(str);
	}
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>

</head>

<body>
	<div id="layout1">
		<div position="center" >
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: block;">
				<form id="searchForm">
				<input type="text" class="hide" id="completeComplianceCertificateValue"/>
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="a.client_code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>供应商</form:left>
								<form:right><p>
												<select id="supplierCode" name="supplierCode" alias="a.supplier_code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>件号</form:left>
								<form:right><input name="partNumber" id="partNumber"  class="text" alias="a.part_number" oper="cn"/></form:right>
							</form:column>
							<form:column>
								<form:left><p>入库日期：</p></form:left>
								<form:right><p><input id="importDateStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'importDateEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="importDateStart" alias="a.import_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="importDateEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'importDateStart\')}',dateFmt:'yyyy-MM-dd'})" name="importDateEnd" alias="a.import_date" oper="lt"/> </p></form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column>
								<form:left>位置</form:left>
								<form:right><input name="location" id="location"  class="text"  alias="a.location" oper="eq"/></form:right>
							</form:column>
							
						
						<form:column>
								<form:left>符合性证明</form:left>
								<form:right><p>
												<select id="complianceCertificate" name="complianceCertificate" alias="a.compliance_certificate" oper="eq">
							        			<option value="">全部</option>
							        			<option value="300">合格证</option>
							        			<option value="301">履历表</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
						
						<form:column>
								<form:left><p>是否完成</p><p>符合性证明</p></form:left>
								<form:right><p>
												<select id="completeComplianceCertificate" name="completeComplianceCertificate" alias="ipe.complete_compliance_certificate" oper="eq">
							        			<option value="">全部</option>
							        			<option value="520">是</option>
							        			<option value="521">否</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							
						
							<form:column >
							<p style="padding-left:10px;">
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