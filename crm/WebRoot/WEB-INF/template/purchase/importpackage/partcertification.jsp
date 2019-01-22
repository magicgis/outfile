<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>选中件号生成合格证</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>
<script type="text/javascript">
	var layout, grid;
	$(function() {
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/clientquote/searchclient',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].client_code).text(obj[i].client_code);
						$("#client_code").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		}); 
		
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [{
				icon : 'view',
				text : 'Excel管理',
				click : function(){
					Excel();
						}
					}, {
						id:'search1',
						icon : 'search',
						text : '展开搜索',
						click: function(){
							$("#searchdiv").toggle(function(){
							var display = $("#searchdiv").css("display");
							if(display=="block"){
								$("#toolbar > div[toolbarid='search1'] > span").html("收起搜索");
								grid.setGridHeight(PJ.getCenterHeight()-202);
							}else{
								$("#toolbar > div[toolbarid='search1'] > span").html("展开搜索");
								grid.setGridHeight(PJ.getCenterHeight()-142);
							}
						});
					}
				}   ]
		});

		grid = PJ.grid("list", {
			rowNum: 20,
			autowidth:true,
			url:'<%=path%>/importpackage/importpackageelementdate?id='+'${id}',
			width : PJ.getCenterWidth(),
			autoSrcoll:true,
			shrinkToFit:false,
			autowidth:true,
			rowList:[10,20,50],
			multiselect:true,
			height : PJ.getCenterHeight()-142,
			sortname : "ip.import_date",
			colNames : ["id","importpackageid","客户", "件号","描述","状态","证书", "单位","数量","单价"
			            , "位置","重量","溯源号","系列号","合格证日期", "订单号","客户订单号","询价单号","更新时间","客户id","客户订单号"],
			colModel : [PJ.grid_column("id", {sortable:false,hidden:true,align:'left'}),
			            PJ.grid_column("importPackageId", {sortable:false,hidden:true,align:'left'}),
			            PJ.grid_column("clientCode", {sortable:false,width:30,align:'left'}),
			            PJ.grid_column("partNumber", {sortable:false,align:'left'}),
			            PJ.grid_column("description", {sortable:false,align:'left'}),
			            PJ.grid_column("conditionCode", {sortable:false,width:60,align:'left'}),
			            PJ.grid_column("certificationCode", {sortable:false,width:80,align:'left'}),
						PJ.grid_column("unit", {sortable:false,width:40,align:'left'}),
						PJ.grid_column("amount", {sortable:false,width:40,align:'left'}),
						PJ.grid_column("price", {sortable:false,align:'left',width:60}),
			            PJ.grid_column("location", {sortable:false,align:'left',width:60}),
			            PJ.grid_column("boxWeight", {sortable:false,width:60,formatter:function(value){
							if(value){
								return value+"g";
							}
						},align:'left'}),
			            PJ.grid_column("originalNumber", {sortable:false,align:'left',width:60}),
			            PJ.grid_column("serialNumber", {sortable:false,width:60,align:'left'}),
			            PJ.grid_column("certificationDate", {sortable:false,width:80,align:'left'}),
						PJ.grid_column("orderNumber", {sortable:false,width:100,align:'left'}),
						PJ.grid_column("sourceOrderNumber", {sortable:false,width:80,align:'left'}),
						PJ.grid_column("quoteNumber", {sortable:false,width:100,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:false,width:120,align:'left'}),
						PJ.grid_column("clientId", {sortable:false,width:180,hidden:true,align:'left'}),
						PJ.grid_column("clientOrderNumber", {sortable:false,width:120,align:'left'}),
						],
						
		});
		
		//搜索条件是日期类型的加入日期控件
		PJ.datepickerAll();
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/importpackage/importpackageelementdate?id='+'${id}');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/importpackage/importpackageelementdate?id='+'${id}');
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
		$("#test4_dropdown").hide();
	});
	
	function Excel(){
		 var rowKey = grid.getGridParam("selarrrow");
		 var ids;
		 if(rowKey!= ""){
			 var id =  PJ.grid_getMutlSelectedRowkey(grid);
			 ids = id.join(",");
		 }
		 if(rowKey== ""){
				PJ.warn("请选择需要操作的数据");
				return false;
		 }
		 var ret = PJ.grid_getSelectedData(grid);
		 var clientCode = ret["clientCode"];
		 var result=ids.split(",");
		 var length=result.length;
		 if(length>4){
			 PJ.warn("最多只能选中四条数据");
				return false;
		 }
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var ipid=${id}+"-"+ids
			var businessKey = 'import_package_element.id.'+ipid+'.CertificationExcel.'+clientCode
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
			});
	}
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
</style>
<style>
	#cb_list{
		margin:0
	}
</style>
</head>

<body style="padding:3px">
	<div id="layout1">
		<div position="center" title="入库单号：${importNumber}   供应商：${supplierCode}">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 50px;display: none;">
				<form id="searchForm">
				<div class="search-box">
							<form:row columnNum="5">
							<form:column>
							<form:left>客户</form:left>
								<form:right><p><select  id="client_code" name="client_code" alias=" c.code" oper="eq">
							            	<option value="">全部</option>
							            	</select>
							              </p>
								</form:right>
							</form:column>
						<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
						</p>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>
			<table id="list"></table>
			<div id="pager1"></div>
		
		</div>
	</div>
</body>
</html>
