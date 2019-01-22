<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-新增供应商订单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid,grid2;
	var con,cert,repairType
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 300,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [
						{
							id:'add',
							icon : 'add',
							text : '新增',
							click: function(){
								var iframeId="idealframe2";
								PJ.topdialog(iframeId, '新增入库 ', '<%=path%>/storage/exchangeimport/toAdd',
											function(item,dialog){
												var postData = top.window.frames[iframeId].getFormData();
												var va = top.window.frames[iframeId].validate();
												if(va){
													$.ajax({
														type: "POST",
														dataType: "json",
														data: postData,
														url:'<%=path%>/storage/exchangeimport/saveAdd',
														success:function(result){
															if(result.success){
																PJ.success(result.message);
																PJ.grid_reload(grid);
															}else{
																PJ.warn(result.message);
															}
															dialog.close();
														}
													});
												}else{
													PJ.warn("请填写完整！");
												}
											}
											,function(item,dialog){dialog.close();}, 1300, 700, true,"新增");
								
							}
						},
						{
							id:'add',
							icon : 'add',
							text : '新增出库',
							click: function(){
								var iframeId="idealframe3";
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								PJ.topdialog(iframeId, '新增出库 ', '<%=path%>/storage/exchangeimport/toAddExport?id='+id,
											function(item,dialog){
												var postData = top.window.frames[iframeId].getFormData();
												var va = top.window.frames[iframeId].validate();
												if(va){
													$.ajax({
														type: "POST",
														dataType: "json",
														data: postData,
														url:'<%=path%>/storage/exchangeimport/saveAddExport',
														success:function(result){
															if(result.success){
																PJ.success(result.message);
																PJ.grid_reload(grid);
															}else{
																PJ.warn(result.message);
															}
															dialog.close();
														}
													});
												}else{
													PJ.warn("请填写完整！");
												}
											}
											,function(item,dialog){dialog.close();}, 1200, 600, true,"新增");
								
							}
						},
						{
							id:'view',
							icon : 'view',
							text : '文件管理',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var iframeId="ideaIframe3";
								PJ.topdialog(iframeId, '文件管理 ', '<%=path%>/storage/exchangeimport/fileUpload?id='+id,
										function(item, dialog){
										dialog.close();
										},function(item,dialog){PJ.hideLoading();dialog.close();}, 1000, 500, true);
							}
					 	},
					 	{
							id:'view',
							icon : 'view',
							text : '入库完成',
							click: function(){
								PJ.showLoading("上传中....");
								$.ajax({
									type: "POST",
									dataType: "json",
									url:'<%=path%>/storage/exchangeimport/exchangeImportEmail',
									success:function(result){
										if(result.success){
											PJ.hideLoading();
											PJ.success(result.message);
											PJ.grid_reload(grid);
										}else{
											PJ.hideLoading();
											PJ.warn(result.message);
										}
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
										grid.setGridHeight(PJ.getCenterHeight()-232);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
										grid.setGridHeight(PJ.getCenterHeight()-142);
									}
								});
							}
						}
			        ]
		});
		
		$("#toolbar2").ligerToolBar({
			items : [
						{
							id:'delete',
							icon : 'delete',
							text : '删除',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid2);
								var id = ret["id"];
								PJ.confirm("确定删除所选附件?", function(yes){
									if(yes){
										$.ajax({
											type: "POST",
											dataType: "json",
											url:'<%=path%>/storage/exchangeimport/delExport?id='+id,
											success:function(result){
												if(result.success){
													PJ.success(result.message);
													PJ.grid_reload(grid);
													PJ.grid_reload(grid2);
												}else{
													PJ.warn(result.message);
												}
												dialog.close();
											}
										});
									}
								});	
							}
						}
					]
		});
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exchangeimport/importList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "co.update_timestamp",
			sortorder : "desc",
			pager: "#pager1",
			inLineEdit: true,
			editurl:'<%=path%>/storage/exchangeimport/editImport',
			onSelectRow:function(rowid,status,e){
				onSelect();
				var ret = PJ.grid_getSelectedData(grid);
				var id = ret["id"];
				$.ajax({
					url: '<%=path%>/storage/exchangeimport/Certifications?id='+id,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								cert = result.message;
							}
					}
				});
				
				$.ajax({
					url: '<%=path%>/storage/exchangeimport/Conditions?id='+id,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								con = result.message;
							}
					}
				});
				$.ajax({
					url: '<%=path%>/storage/exchangeimport/repairType?id='+id,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								repairType = result.message;
							}
					}
				});
			},
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			colNames :["id","客户","件号","数量","订单号","描述","入库时间","位置","类型","状态","证书","商业模式","运单号","SN","状态","备注","更新时间"],
			colModel :[PJ.grid_column("id", {hidden:true,editable:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:30,align:'left',editable:true}),
			           PJ.grid_column("orderNumber", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left',editable:true}),
			           PJ.grid_column("importDate",{sortable:true,width:70,align:'left',editable:true,
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   }
			           }),
			           PJ.grid_column("location",{sortable:true,width:40,align:'left',editable:true}),
			           PJ.grid_column("repairTypeValue",{sortable:true,width:40,align:'left',editable:true,
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return repairType;        			   
		        		   		}
							}   
			           }),
			           PJ.grid_column("conValue",{sortable:true,width:70,align:'left',editable:true,
			        	   edittype:"select",formatter:"",
			        		   editoptions:{value:
									function(){
											return con;        			   
			        		   		}
								}
			           }),
			           PJ.grid_column("cerValue",{sortable:true,width:70,align:'left',editable:true,
		        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return cert;        			   
		        		   		}
							}
			           }),
			           PJ.grid_column("bizValue", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("shipNumber", {sortable:true,width:80,align:'left',editable:true}),
			           PJ.grid_column("sn", {sortable:true,width:80,align:'left',editable:true}),
			           PJ.grid_column("complete", {sortable:true,width:40,align:'left',
			        	   formatter: function(value){
								if (value==1) {
									return "完成";
								} else {
									return "";
								}
							}
			           }),
			           PJ.grid_column("remark", {sortable:true,width:150,align:'left',editable:true}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:120,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "co.update_timestamp",
			sortorder : "desc",
			pager: "#pager2",
			inLineEdit: true,
			editurl:'<%=path%>/storage/exchangeimport/editExport',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			colNames :["id","供应商","出库时间","数量","运输单号","备注","更新时间"],
			colModel :[PJ.grid_column("id", {hidden:true}),
			           PJ.grid_column("supplierCode", {sortable:true,width:70,align:'left'}),
			           PJ.grid_column("exportDate", {sortable:true,width:150,align:'left',editable:true,
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   }
			           }),
			           PJ.grid_column("amount", {sortable:true,width:70,align:'left',editable:true}),
			           PJ.grid_column("shipNumber", {sortable:true,width:150,align:'left',editable:true}),
			           PJ.grid_column("remark", {sortable:true,width:250,align:'left',editable:true}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			$("#id").val(id);
			PJ.grid_search(grid2,'<%=path%>/storage/exchangeimport/exportList?id='+id);
		};

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
		    	PJ.grid_search(grid,'<%=path%>/storage/exchangeimport/importList');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/exchangeimport/importList');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
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
	});
	
	function trim(str){
		return $.trim(str);
	}
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="维修入库">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 90px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="orderNumber" alias="cie.part_number" oper="cn"/> </p></form:right>
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
								<form:left><p>入库日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="eip.import_date" oper="gt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="eip.import_date" oper="lt"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>运单号：</p></form:left>
								<form:right><p><input id="shipNumber" class="text" type="text" name="shipNumber" alias="eip.SHIP_NUMBER or eep.SHIP_NUMBER" oper="cn"/></p></form:right>
							</form:column>
							
						</form:row>
						<form:row columnNum="5">
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
							</form:column>
							<form:column>
								<form:left><p></p></form:left>
								<form:right><p></p></form:right>
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
		<div position="centerbottom" title="维修出库">
			<div id="toolbar2"></div>
			<table id="list2" style=""></table>
			<div id="pager2"></div>
  		</div>
	</div>
</body>
</html>