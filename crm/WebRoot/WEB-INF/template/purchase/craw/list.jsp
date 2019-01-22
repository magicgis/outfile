<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户询价单统计</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	var data = [];
	$(function() {
		layout = $("#layout1").ligerLayout();
		$("#toolbar").ligerToolBar({
			items : [
					{
						id:'view',
						icon : 'view',
						text : '大于5000报价',
						click: function(){
							var iframeId="ideaIframe";
								PJ.topdialog(iframeId, '大于5000爬虫报价 ', '<%=path%>/supplierquote/toHighPricePage',
										undefined,function(item,dialog){dialog.close();}, 1100, 500, true);
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
		
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/supplierquote/crawerList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "a.update_datetime",
			sortorder : "desc",
			datatype:"local",
			data:data,
			colNames :["供应商","件号","溯源件号","描述","价格","币种","数量","周期","证书","询价单号","备注","更新时间"],
			colModel :[
			           PJ.grid_column("supplierCode", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("enterPartnumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("unitPrice", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("currency", {sortable:true,width:30,align:'left'}),
			           PJ.grid_column("moq", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("leadTime", {sortable:true,width:20,align:'left'}),
			           PJ.grid_column("certification", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("updateDatetime", {sortable:true,width:80,align:'left'})
			           ]
		});

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
						$("#clientCode").append($option);
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
		PJ.showLoading("处理中...");
		$.ajax({
			url: '<%=path%>/supplierquote/crawerListOnce',
			type: "POST",
			loading: "正在处理...",
			dataType: "json",
			success: function(result){
					if(result.success){
						PJ.hideLoading();
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
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	var quoteStatus = $("#quoteStatus").val()
				if(quoteStatus == "0"){
					quoteStatus = "a.unit_price is null"
				}else if(quoteStatus == "1"){
					quoteStatus = "a.unit_price is not null"
				}else{
					quoteStatus = ""
				}
				<%-- PJ.grid_search(grid,'<%=path%>/supplierquote/crawerList?quoteStatus='+quoteStatus); --%>
		    	var postData = {};
				 var searchString = getSearchString();
				 postData.searchString = searchString;
				 PJ.ajax({
						url: '<%=path%>/supplierquote/crawerListOnce?quoteStatus='+quoteStatus,
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
								}	
						}
				});
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var quoteStatus = $("#quoteStatus").val()
			 if(quoteStatus == "0"){
				 quoteStatus = "a.unit_price is null"
			 }else if(quoteStatus == "1"){
				 quoteStatus = "a.unit_price is not null"
			 }else{
				 quoteStatus = ""
			 }
			 <%-- PJ.grid_search(grid,'<%=path%>/supplierquote/crawerList?quoteStatus='+quoteStatus); --%>
			 var postData = {};
			 var searchString = getSearchString();
			 postData.searchString = searchString;
			 PJ.ajax({
					url: '<%=path%>/supplierquote/crawerListOnce?quoteStatus='+quoteStatus,
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
							}	
					}
			});
		 });
		
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
		
		
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-202);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
	});
	
	function trim(str){
		return $.trim(str);
	}
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left>询价单号：</form:left>
								<form:right><input id="searchForm2" class="text" type="text" name="quoteNumber" alias="a.quote_number" oper="cn"/>
								</form:right>
							</form:column>
							<form:column>
								<form:left>件号：</form:left>
								<form:right><input id="searchForm2" class="text" type="text" name="partNumber" alias="PART_NUMBER or a.ENTER_PARTNUMBER" oper="cn"/>
								</form:right>
							</form:column>
							
							<form:column>
								<form:left>报价情况：</form:left>
								<form:right><p>
													<select name="quoteStatus" id="quoteStatus">
							            			<option value="">全部</option>
							            			<option value="1">有报价</option>
							            			<option value="0">无报价</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>供应商</form:left>
								<form:right><input id="searchForm8" class="text" type="text" name="supplierCode" alias="a.supplier_code" oper="eq"/></form:right>
							</form:column>
							<form:column >
							<p style="padding-left:65px;">
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