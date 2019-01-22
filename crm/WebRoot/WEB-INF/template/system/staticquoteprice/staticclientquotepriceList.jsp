<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title></title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	
	$(function() {
		layout = $("#layout1").ligerLayout();
		var currency;
		
		$("#toolbar").ligerToolBar({
			items : [{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe5";
							PJ.topdialog(iframeId, '新增', '<%=path%>/system/staticprice/toAddForClient',
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 PJ.ajax({
												url: '<%=path%>/system/staticprice/addWithPageForClient',
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
															dialog.close();
														} else {
															PJ.error(result.message);
														}		
												}
											});
									PJ.grid_reload(grid);
									}
								   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
							id:'delete',
							icon : 'delete',
							text : '删除',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								
								PJ.confirm("确定删除?", function(yes){
									if(yes){
										PJ.showLoading("正在处理...");
										PJ.ajax({
											url: '<%=path%>/system/staticprice/clientDelete?id='+id,
											type: "POST",
											success: function(result){
													PJ.hideLoading();
													if(result.success){
														PJ.success(result.message);
														PJ.grid_reload(grid);
														dialog.close();
													} else {
														PJ.error(result.message);
													}		
											}
										});
									}
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
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/system/staticprice/clientListData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			shrinkToFit:true,
			sortname : "scqp.update_timestamp",
			sortorder : "desc",
			inLineEdit: true,
			editurl:'<%=path%>/system/staticprice/editClientQuote',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			onSelectRow:function(rowid,result){
				var ret = PJ.grid_getSelectedData(grid);
				var id = ret["id"];
				$.ajax({
					url: '<%=path%>/system/staticprice/currencyTypeForClient?id='+id,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							//var obj = eval(result.message)[0];
							if(result.success){
								currency = result.message;
								/* for(var i in obj){
									currency = currency + obj[i].currency_id + ":" + obj[i].currency_value + ";";
								} */
							}
					}
				});
			},
			colNames :["id","客户","件号","价格","币种","年份","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true}),
			           PJ.grid_column("code", {key:true,width:80,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("price", {sortable:true,width:130,align:'left',editable:true}),
			           PJ.grid_column("currencyValue", {sortable:true,width:130,align:'left',editable:true,
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return currency;        			   
		        		   		}
							}   
			           }),
			           
			           PJ.grid_column("year", {sortable:true,width:130,align:'left',editable:true}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'})
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
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/system/staticprice/clientListData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/system/staticprice/clientListData');
			
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
		
		$("#submit").click(function(){
			var url = '<%=path%>/system/staticprice/uploadExcelForClient?editType='+ getFormData().toString();
			PJ.showLoading("上传中....");
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	var da = eval(data)[0];
	            	if(da.flag==true){
	            		PJ.hideLoading();
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
		            	$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-202);
		            	});
	            	}else{
	            		PJ.hideLoading();
	            		$("#uploadBox").toggle(function(){
							$("#toolbar > div[toolbarid='upload'] > span").html("展开excel上传");
							grid.setGridHeight(PJ.getCenterHeight()-202);
		            	});
	            		iframeId = 'errorframe'
	            		PJ.topdialog(iframeId, '错误信息', '<%=path%>/stock/search/toUnknow',
	            				undefined,function(item,dialog){
			            			$.ajax({
										url: '<%=path%>/stock/search/deleteData',
										type: "POST",
										loading: "正在处理...",
										success: function(result){
										}
									});
	            					dialog.close();}, 1000, 500, true);
	            	}
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.hideLoading();
	            	PJ.error("上传异常！");
	            }  
	        });
			
		 });
		
		function getFormData(){
			 var postData = {};
			 postData.data = $("#form").serialize();
			 postData.id = $("#id").val();
			 return postData;
		};
		
	});
	
	function trim(str){
		return $.trim(str);
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
</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" alias="scqp.part_number" oper="cn"/> </p></form:right>
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
								<form:left></form:left>
								<form:right><p></p>
								</form:right>
							</form:column>
							<form:column>
								<form:left></form:left>
								<form:right><p></p>
								</form:right>
							</form:column>
							<form:column >
							<p style="padding-left:30px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
							</form:column>
						</form:row>
						<form:row columnNum="2">
							<form id="form" name="form">
							<form:column width="120">
								<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
								<form:right>
									<p><input type="file" value="" id="file" name="file"/>&nbsp;
									   <input type="button" id="submit" value="新增"/>
									</p>
								</form:right>
							</form:column>
							</form>
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