<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-客户管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-top;
  padding-top:2px;
 } */
</style>

<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [{
						id:'add',
						icon : 'add',
						text : '新增客户',
						click: function(){
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '系统配置-新增客户', '<%=path%>/system/clientmanage/toAddClient',
										function(item, dialog){
											
											var nullAble=top.window.frames[iframeId].validate();
											if(nullAble){
												var postData=top.window.frames[iframeId].getFormData();
												if(postData!=null){
													$.ajax({
														url: '<%=path%>/system/clientmanage/saveClient',
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
																	dialog.close();
																}		
														}
													});
												}	
											}else{
												PJ.warn("数据还没有填写完整！");
											}
										},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
							id:'modify',
							icon : 'modify',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '系统配置-修改客户', '<%=path%>/system/clientmanage/toClientEdit?id='+id,
											function(item, dialog){
												var postData=top.window.frames[iframeId].getFormData();
												if(postData!=null){
													 $.ajax({
															url: '<%=path%>/system/clientmanage/saveEdit',
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
																		dialog.close();
																	}		
															}
														});
												}
											},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
								
							}
					 },
					 {
							id:'view',
							icon : 'view',
							text : '客户联系人管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe3";
									PJ.topdialog(iframeId, '系统配置-客户联系人管理', '<%=path%>/system/clientmanage/toContactsList?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
							}
					 },
					 {
							id:'view',
							icon : 'view',
							text : '客户银行管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe4";
									PJ.topdialog(iframeId, '系统配置-客户银行管理', '<%=path%>/system/clientmanage/toBankList?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
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
			url:'<%=path%>/system/clientmanage/clientList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			shrinkToFit:false,
			sortname : "c.update_timestamp",
			sortorder : "desc",
			colNames :["id","客户编号","拥有人","全称","简称","Location","利润率","是否需要合格证","货币","邮编","地址","交付方式","客户类型","发票模板","箱单模板","快递地址","发货方式","佣金","运费","最低运费","银行费用","客户归类","是否可退税","预付比例","发货时支付比例","发货帐期","验货时支付比例","验货帐期","电话","传真","主页网址","邮箱","客户等级","客户状态","客户阶段","客户来源","能力范围","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("code", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("ownerName", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("name", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("clientShortName", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("profitMargin", {sortable:true,width:60,align:'left',
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2)+"%";
								} else {
									return value;							
								}
							}
			        	   
			           }),
			           PJ.grid_column("certification", {sortable:true,width:90,align:'left',
			        	   formatter: function(value){
								if (value==521) {
									return "否";
								} else {
									return "是";							
								}
							}}),
			           PJ.grid_column("value", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("postCode", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("address",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("delivery",{sortable:true,width:60,align:'left'}),
			           PJ.grid_column("clientTemplateTypeValue", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("invoiceTempletValue", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("shipTempletValue", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("shipAddress", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("shipWayName", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("fixedCost", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("freight", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("lowestFreight", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("bankCost", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("clientTypeValue", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("taxReturn", {sortable:true,width:70,
			        	   formatter: function(value){
								if (value==1) {
									return "是";
								} else if (value==0){
									return "否";							
								}
							}
			           ,align:'left'}),
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
			           PJ.grid_column("shipPayPeriod", {sortable:true,width:80}),
			           PJ.grid_column("receivePayRate", {sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value+"%";
								} else {
									return '0'+"%";							
								}
							}   
			           ,align:'left'}),
			           PJ.grid_column("receivePayPeriod", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("phone",{sortable:true,width:130,align:'left'}),
			           PJ.grid_column("fax",{sortable:true,width:130,align:'left'}),
			           PJ.grid_column("url",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("email",{sortable:true,width:150,align:'left'}),
			           PJ.grid_column("clientLevelValue",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("clientStatusValue",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("clientStage",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("clientSource",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("clientAbility",{sortable:true,width:70,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:150,align:'left'}),
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
		
		//客户归类
		 $.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/clientmanage/classifyList',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#clientType").append($option);
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
		    	PJ.grid_search(grid,'<%=path%>/system/clientmanage/clientList');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/system/clientmanage/clientList');
			
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
	
	function trim(str){
		return $.trim(str);
	}
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 50px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>代码：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="quoteNumber" alias="c.code" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>名字：</form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="quoteNumber" alias="c.name" oper="cn"/></p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>客户归类：</form:left>
								<form:right><p><select id="clientType"  name="clientType" alias="c.client_type" oper="eq">
									<option value="">全部</option>
								</select> </p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>Location：</form:left>
								<form:right><p><input id="searchForm3" class="text" type="text" name="location" alias="c.location" oper="cn"/></p>
								</form:right>
							</form:column>
							<form:column >
							<p style="padding-left:66px;">
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