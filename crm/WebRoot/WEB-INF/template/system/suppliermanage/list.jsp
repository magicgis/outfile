<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>系统配置-供应商管理</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();  

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
		
		//供应商归类
		 $.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/classifyList?id='+'${data.id}',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option=$("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#supplierClassify").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//是否有caac能力
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/suppliermanage/caacAbilityList',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].value);
							$("#hasCaacAbility").append($option);
						}
					}else{
						
							PJ.showWarn(result.msg);
					}
				}
			});
		
		//国家
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/suppliermanage/getCountryList',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].name+"-"+obj[i].chineseName);
						$("#countryId").append($option);
					}
					//2 重新生成一个div 
		            $('#countryId').searchableSelect({
		                afterSelectItem: function(){//此方法是点击后激活事件 :关闭下拉框
		                $('#countryId').addClass('searchable-select-hide');
		            }}
		            );
		            //3 生成的同时打开下拉框
		            $('#countryId').removeClass('searchable-select-hide');
		            $(".searchable-select").css("z-index",1);
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe1";
								PJ.topdialog(iframeId, '新增供应商', '<%=path%>/suppliermanage/addSupplier',
										function(item, dialog){
											var postData=top.window.frames[iframeId].getFormData();
											var nullAble=top.window.frames[iframeId].validate();
											var checkCaac = top.window.frames[iframeId].checkCaac();
											if(nullAble&&postData){
												if(checkCaac){
													$.ajax({
														url: '<%=path%>/suppliermanage/save',
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
												}else{
													PJ.warn("供应商归类为包含MRO的，必须填写CAAC能力");
												}
											}else{
												PJ.warn("还有数据没有填写！");
											}
										},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '修改',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe2";
									PJ.topdialog(iframeId, '修改供应商', '<%=path%>/suppliermanage/editSupplier?id='+id,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();	
														var nullAble=top.window.frames[iframeId].validate();
														var checkCaac = top.window.frames[iframeId].checkCaac();
														if(nullAble&&postData){
															if(checkCaac){
															 	$.ajax({
																	url: '<%=path%>/suppliermanage/update',
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
															}else{
																PJ.warn("供应商归类为包含MRO的，必须填写CAAC能力");
															}
														}else{
															PJ.warn("还有数据没有填写！");
														}
											},function(item,dialog){dialog.close();}, 1000, 500, true,"修改");
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '供应商联系人管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="supplierIframe";
									PJ.topdialog(iframeId, '系统配置-供应商联系人管理', '<%=path%>/suppliermanage/supplierContact?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '供应商机型管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="supplierAirIframe";
									PJ.topdialog(iframeId, '系统配置-供应商机型管理', '<%=path%>/suppliermanage/supplierAir?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '生产商',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="makerIframe";
									PJ.topdialog(iframeId, '系统配置-生产商管理', '<%=path%>/suppliermanage/tofactoryList?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '供应商资质管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="aptitudeIframe";
									PJ.topdialog(iframeId, '系统配置-供应商资质管理', '<%=path%>/suppliermanage/toAptitude?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
							}
					 },
					 {
							id:'add',
							icon : 'add',
							text : '件号管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="partIframe";
									PJ.topdialog(iframeId, '系统配置-件号管理', '<%=path%>/suppliermanage/topartList?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
								
							}
					 },	 {
							id:'add',
							icon : 'add',
							text : '年度报价管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="annuaOfferIframe";
									PJ.topdialog(iframeId, '系统配置-年度报价管理', '<%=path%>/suppliermanage/toAnnualOffer?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
								
							}
					 },
					 {
							id:'view',
							icon : 'view',
							text : '文件管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe5";
								PJ.topdialog(iframeId, ' 文件管理 ', '<%=path%>/suppliermanage/file?id='+id,
										undefined,function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
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
										grid.setGridHeight(PJ.getCenterHeight()-212);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-112);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/suppliermanage/listData',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-112,
			autoSrcoll:true,
			shrinkToFit:false,
			/* sortname : "s.code",
			sortorder : "asc", */
			colNames :["id", "代码","旧代码","供应商名称","供应商简称","Location","Name In Stockmarket","附件","代理","币种","联系人","国家","地址","电话","手机","传真","网址","电子邮箱",
			          "拥有人","供应商状态","MOV PER LINE","MOV PER ORDER","银行手续费","信用卡手续费","预付比例","发货前付款比例","验收完限期内付款比例","验收完限期内付款账期","CREDIT LIMIT","供应商归类","CAAC能力","退税","能力范围",
			          "创建日期","开户行 Bank Name","IBAN","银行账号","Account Name","银行电话","Swift Code","ROUTING/ABA TRANSIT NUMBER","银行地址","纳税号","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,align:'left'}),
			           PJ.grid_column("code", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("oldCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("name", {width:150,align:'left'}),
			           PJ.grid_column("supplierAbbreviation", {width:120,align:'left'}),
			           PJ.grid_column("location", {width:100,align:'left'}),
			           PJ.grid_column("nameInStockmarket", {width:120,align:'left'}),
			           PJ.grid_column("haveAttachment", {width:40,align:'left',
			        	   formatter: function(value){
								if (value==1) {
									return "有";
								} else {
									return "";
								}
							}   
			           }),
			           PJ.grid_column("isAgentValue", {width:40,align:'left'}),
			           PJ.grid_column("currencyValue", {width:50,align:'left'}),
			           PJ.grid_column("contactName", {width:90,align:'left'}),
			           PJ.grid_column("country", {width:100,align:'left'}),
			           PJ.grid_column("address", {width:320,align:'left'}),
			           PJ.grid_column("phone",{width:100,align:'left'}),
			           PJ.grid_column("mobile",{width:100,align:'left'}),
			           PJ.grid_column("fax",{width:120,align:'left'}),
			           PJ.grid_column("url", {width:130,align:'left'}),
			           PJ.grid_column("email",{width:220,align:'left'}),
			           PJ.grid_column("ownerName", {width:60,align:'left'}),
			           PJ.grid_column("supplierStatusValue", {width:60,align:'left'}),
			           PJ.grid_column("movPerLine", {width:90,align:'left'}),
			           PJ.grid_column("movPerOrder", {width:90,align:'left'}),
			           PJ.grid_column("counterFee", {width:70,align:'left'}),
			           PJ.grid_column("creditFee", {width:70,align:'left',
			        	   formatter:function(value){
								if (value) {
									return value+"%";
								}
						    }   
			           }),
			           PJ.grid_column("prepayRate", {width:60,align:'left',
			        	   formatter:function(value){
								if (value) {
									return value+"%";
								}
						    }   
			           }),
			           PJ.grid_column("shipPayRate", {width:80,align:'left',
			        	   formatter:function(value){
								if (value) {
									return value+"%";
								}
						    }   
			           }),
			           PJ.grid_column("receivePayRate", {width:110,align:'left',
			        	   formatter:function(value){
								if (value) {
									return value+"%";
								}
						    }   
			           }),
			           PJ.grid_column("receivePayPeriod", {width:110,align:'left'}),
			           PJ.grid_column("creditLimit", {width:120,align:'left'}),
			           PJ.grid_column("supplierClassifyValue", {width:120,align:'left'}),
			           PJ.grid_column("caacAbilityValue", {width:60,align:'left'}),
			           PJ.grid_column("taxReimbursementValue", {width:40,align:'left'}),
			           PJ.grid_column("competenceScope", {width:150,align:'left'}),
			           PJ.grid_column("dateCreated",{sortable:true,width:110,align:'left'}),
			           PJ.grid_column("bank",{width:150,align:'left'}),
			           PJ.grid_column("iban",{width:130,align:'left'}),
			           PJ.grid_column("bankAccountNumber",{width:150,align:'left'}),
			           PJ.grid_column("accountName", {width:170,align:'left'}),
			           PJ.grid_column("bankPhone",{width:130,align:'left'}),
			           PJ.grid_column("swiftCode",{width:130,align:'left'}),
			           PJ.grid_column("routing",{width:130,align:'left'}),
			           PJ.grid_column("bankAddress", {width:170,align:'left'}),
			           PJ.grid_column("taxPayerNumber",{width:150,align:'left'}),
			           PJ.grid_column("remark",{width:300,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:110,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	var cageCode = $("#cageCode").val();
		    	PJ.grid_search(grid,'<%=path%>/suppliermanage/listData?cageCode='+cageCode);
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var cageCode = $("#cageCode").val();
			 PJ.grid_search(grid,'<%=path%>/suppliermanage/listData?cageCode='+cageCode);
			
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
		
		$("#searchForm1").blur(function(){
			var text = $("#searchForm1").val();
			$("#searchForm1").val(trim(text));
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			var a= text.indexOf('\'');
			$("#searchForm2").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
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
								<form:left><p>代码：</p></form:left>
								<form:right><p><input id="searchForm1" class="text" type="code" name="code" alias="s.code" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>名称或简称：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="name" name="name" alias="s.name or s.supplier_abbreviation" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>国家：</form:left>
								<form:right><p>
												<select id="countryId" name="countryId" alias="s.country_id" oper="eq">
						            			<option value="">全部</option>
						            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>类型：</form:left>
								<form:right><p>
													<select id="searchForm3" name="airTypeValue" alias="at.code" oper="eq">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>供应商归类：</form:left>
								<form:right><p>
													<select id="supplierClassify" name="supplierClassify" alias="s.supplier_classify_id" oper="cn">
							            			<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column>
								<form:left><p>cage code：</p></form:left>
								<form:right><p><input id="cageCode" class="text" type="text" name="cageCode" /></p></form:right>
							</form:column>
							<form:column>
								<form:left><p>CAAC能力</p></form:left>
								<form:right><p>
												<select id="hasCaacAbility" name="hasCaacAbility" alias="s.has_caac_ability" oper="eq">
													<option value="">请选择</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
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