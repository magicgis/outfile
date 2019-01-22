<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>StockMarket</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	var conditions;
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 400,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		var clientInquiryId = 0;
		
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [
						{
							id:'search',
							icon : 'search',
							text : '展开搜索',
							click: function(){
								$("#searchdiv").toggle(function(){
									var display = $("#searchdiv").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid.setGridHeight(PJ.getCenterHeight()-162);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-102);
									}
								});
							}
						}
			        ]
		});
		
		$("#toolbar2").ligerToolBar({
			items : [
					 {	icon : 'logout',
						text : '导出excel',
						click : function() {
									PJ.grid_export("list2");
								}
					 },
					 {
						icon : 'view',
						text : '统计数据下载',
						click : function(){
                            var ret = PJ.grid_getSelectedData(grid);
                            var percent = ret["percent"];
                            if(percent == "0%" || percent == 0){
								PJ.alert("该条记录没有数据！请重新选择");
							}else{
                                Excel();
							}
						}
					 },
					 {
							id:'search',
							icon : 'search',
							text : '展开搜索',
							click: function(){
								$("#searchdiv2").toggle(function(){
									var display = $("#searchdiv2").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
										grid2.setGridHeight(PJ.getCenterHeight()-92);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid2.setGridHeight(PJ.getCenterHeight()-32);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/suppliercommissionsale/stockCrawlList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "smc.update_timestamp",
			sortorder : "desc",
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			colNames :["id","供应商","资产包号","爬虫时间","状态","进度","crawlPercent"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("supplierCode", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("crawlDate", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("complete", {sortable:true,width:170,align:'left',
			        	   formatter: function(value){
								if (value==1||value=="是") {
									return "已完成";
								} else if(value==0||value=="否"){
									return "未完成";							
								}else {
									return "";
								}
						   }   
			           }),
			           PJ.grid_column("percent", {sortable:true,width:130,align:'left',
			        	   formatter: percentFormatter
			           }),
			           PJ.grid_column("crawlPercent", {hidden:true})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			shrinkToFit:false,
			pager: "#pager2",
			sortname : "smcm.update_timestamp",
			sortorder : "desc",
			colNames :["件号","数量","状态","供应商","爬取周次"],
			colModel :[
			           PJ.grid_column("partNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:50,editable:true,align:'left'}),
			           PJ.grid_column("conditionValue", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("supplierCode", {sortable:true,width:200,editable:true,align:'left'}),
			           PJ.grid_column("crawlDate", {sortable:true,width:100,editable:true,align:'left'})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			$("#id").val(id);
			PJ.grid_search(grid2,'<%=path%>/storage/suppliercommissionsale/stockCrawlElementList?id='+id);
		};
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	var ret = PJ.grid_getSelectedData(grid);
				 var id = ret["id"];
				 PJ.grid_search(grid2,'<%=path%>/storage/suppliercommissionsale/stockCrawlElementList?id='+id);
		    }  
		}); 
		
		//搜索
		$("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/suppliercommissionsale/stockCrawlList');
		});
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
		//搜索
		$("#searchBtn2").click(function(){
			 var ret = PJ.grid_getSelectedData(grid);
			 var id = ret["id"];
			 PJ.grid_search(grid2,'<%=path%>/storage/suppliercommissionsale/stockCrawlElementList?id='+id);
		});
		
		 //重置条件
		 $("#resetBtn2").click(function(){
			 $("#searchForm2")[0].reset();
		 });
		 
		//改变窗口大小自适应
		/* $(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		}); */
		$(window).resize(function() {
			GridResize();
		});
		
		function GridResize() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid2.setGridWidth(PJ.getCenterBottomWidth());
			grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
			grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		}
		grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
		grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		$("#searchForm2").change(function(){
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/sales/clientinquiry/test',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						
					}
				}
			});
		});
		
		function getFormData(){
			 var postData = {};
			 postData.data = $("#form").serialize();
			 postData.id = $("#id").val();
			 return postData;
		};
		
		function Excel(){
	 		var ret = PJ.grid_getSelectedData(grid);
	 		var id = ret["id"];
			//根据具体业务提供相关的title
			var title = 'excel管理';
			//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
			//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
			var businessKey='stock_market_crawl.id.'+id+'.StockMarketCrawlExcel';
			PJ.excelDiaglog({
				data:'businessKey='+businessKey,
				title: title,
				add:true,
				remove:true,
				download:true
			});
		};
		
		//百分比
        function percentFormatter(cellvalue, options, rowObject) {
            var crawlPercent = rowObject["crawlPercent"];
            var complete = rowObject["complete"];
            if(complete == 1 || complete == "已完成"){
            	return "100.0%"
            }else{
            	return crawlPercent
            }
        }
		
	});
	
	function trim(str){
		return $.trim(str);
	}
</script>
<style>
/* th.ui-th-column div  
{  
    white-space:normal !important;  
    height:auto !important;  
    padding:0px;  
}  */
</style>
</head>

<body>
	<div id="layout1">
		<div position="center">
			<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="4">
							<form:column>
								<form:left><p>号码：</p></form:left>
								<form:right><p><input id="number" class="text" type="text" name="number" alias="scs.NUMBER" oper="cn"/> </p></form:right>
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
		<div position="centerbottom" >
			<div id="toolbar2"></div>
			<div id="searchdiv2" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm2">
					<div class="search-box">
						<form:row columnNum="4">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" name="partNumber" alias="smcm.part_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>供应商：</p></form:left>
								<form:right><p><input id="supplier" class="text" type="text" name="supplier" alias="smcm.supplier_code" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>状态：</p></form:left>
								<form:right><p><input id="condition" class="text" type="text" name="condition" alias="smcm.condition_value" oper="cn"/> </p></form:right>
							</form:column>
							<form:column >
								<p style="padding-left:10px;">
									<input class="btn btn_orange" type="button" value="搜索" id="searchBtn2"/>
									<input class="btn btn_blue" type="button" value="重置" id="resetBtn2"/>
								</p>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>
			<table id="list2" style=""></table>
			<div id="pager2"></div>
  		</div>
	</div>
</body>
</html>