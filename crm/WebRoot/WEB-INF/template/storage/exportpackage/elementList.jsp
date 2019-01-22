<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>出库明细管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		$(document).ready(function(){ 
			$("#search").focus();
		});
		
		layout1 = $("#layout1").ligerLayout({
			centerBottomHeight: 300,
			onEndResize:function(e){
			GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [{
							id:'add',
							icon : 'add',
							text : '新增发货发票',
							click: function(){
								var exportPackageId = ${id };
								var iframeId="invoiceIframe";
									PJ.topdialog(iframeId, '新增发货发票', '<%=path%>/storage/exportpackage/toExportPackInvoice?id='+exportPackageId,
											function(item, dialog){
													 var postData=top.window.frames[iframeId].getFormData();
													 if(postData){
														 $.ajax({
																url: '<%=path%>/storage/exportpackage/addExportpackageInvoice?exportPackageId='+exportPackageId,
																data: postData,
																type: "POST",
																loading: "正在处理...",
																success: function(result){
																		if(result.success){
																			
																			var iframeId="invoiceIframe";
																			PJ.topdialog(iframeId, '新增发货发票', '<%=path%>/finance/invoice/toAddInvoice?id='+result.message+'&type='+'2',
																					function(item, dialog){
																							 var postData=top.window.frames[iframeId].getFormData();
																							 var isNull=top.window.frames[iframeId].validate();
																							 if(isNull&&postData){
																								 $.ajax({
																										url: '<%=path%>/finance/invoice/addInvoice?type='+2,
																										data: postData,
																										type: "POST",
																										loading: "正在处理...",
																										success: function(result){
																												if(result.success){
																													PJ.success(result.message);
																													PJ.grid_reload(grid2);
																													dialog.close();
																												} else {
																													PJ.error(result.message);
																													dialog.close();
																												}		
																										}
																								});
																							 }
																					},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
																			
																			PJ.grid_reload(grid2);
																			dialog.close();
																		}	
																}
														});
													 }
											},function(item,dialog){dialog.close();}, 1000, 500, true,"新增");
								
							}
					},
					{
						id:'delete',
						icon : 'delete',
						text : '删除',
						click: function(){
							var iframeId="deleteIframe";
							var ret = PJ.grid_getSelectedData(grid1);
							var id = ret["id"];
							$.ajax({
									url: '<%=path%>/storage/exportpackage/deleteElement?id='+id,
									data: '',
									type: "POST",
									loading: "正在处理...",
									success: function(result){
											if(result.success){
												PJ.success(result.message);
											} else {
												PJ.error(result.message);
											}		
											PJ.grid_reload(grid1);
											PJ.grid_reload(grid2);
									}
							});
						}
					 },	{
							id:'view',
							icon : 'view',
							text : '箱子重量',
							click: function(){
								var iframeId="boxWeightIframe";
								var id ='${id}';
								PJ.topdialog(iframeId, '箱子重量 ', '<%=path%>/storage/exportpackage/toBoxWeight?id='+id,
										undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
							}
						 }
			        ]
		});
		
		grid1 = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exportpackage/Element?id='+${id },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-142,
			autoSrcoll:true,
			shrinkToFit:false,
			colNames :["id","exportPackageId","件号","描述","状态","证书","单位","数量","单价","入库价","退税","订单号","客户订单号","询价单号","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("exportPackageId", {hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("price",{sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}      
			           ,align:'left'}),
			           PJ.grid_column("importBasePrice", {sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}     
			           ,align:'left'}),
			           PJ.grid_column("taxReturnValue", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("sourceOrderNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("quoteNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&id='+${id },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-200,
			autoSrcoll:true,
			shrinkToFit:false,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid2);
				var count = ret["storageAmount"];
				var importPackageElementId = ret["importPackageElementId"];
				var exportRackageInstructionsElementId = ret["exportRackageInstructionsElementId"];
				$("#amount").val(count);
				$("#importPackageElementId").val(importPackageElementId);
				$("#exportRackageInstructionsElementId").val(exportRackageInstructionsElementId);
			},
			pager: "#pager2",
			colNames :["id","exportRackageInstructionsElementId","入库明细id","件号","描述","状态","证书","单位","数量","人民币价格","总价","位置","订单号","客户订单号","入库日期","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("exportRackageInstructionsElementId", {key:true,hidden:true}),
			           PJ.grid_column("importPackageElementId", {key:true,hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("storageAmount", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("basePrice",{sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}   
			           ,align:'left'}),
			           PJ.grid_column("totalBasePrice", {sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}   
			           ,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("sourceNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importDate", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid2,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&id='+${id });
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid2,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&id='+${id });
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		/*$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});*/
		//改变窗口大小自适应
		$(window).resize(function() {
			GridResize();
		});
			
		function GridResize() {
			grid1.setGridWidth(PJ.getCenterWidth());
			grid2.setGridWidth(PJ.getCenterBottomWidth());
			grid1.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
			grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		}
		grid1.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
		grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));	
		
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		
		$("#addBtn").click(function(){
			 var postData = getFormData2();
			 var validate = getValidate();
			 if(validate){
				 $.ajax({
						type: "POST",
						dataType: "json",
						data: postData,
						url:'<%=path%>/storage/exportpackage/saveAddElement',
						success:function(result){
							if(result.success){
								PJ.success(result.message);
							}else{
								PJ.warn(result.message);
							}
							PJ.grid_reload(grid1);
							PJ.grid_reload(grid2);
						}
				});
				
			 }else{
				 PJ.warn("数据没有填写完整！");
			 }
			 $("#search").focus();
		});
		
		<%-- $("#addAll").click(function(){
			 var postData = {};
			 var rowIds = jQuery("#list2").jqGrid('getDataIDs');
			 for(var i = 0;i<rowIds.length;i++){
				 postData["voList["+i+"].id"] = rowIds[i];
			 }
				 $.ajax({
						type: "POST",
						dataType: "json",
						data: postData,
						url:'<%=path%>/storage/exportpackage/addExportElements?id='+${id },
						success:function(result){
							if(result.success){
								PJ.success(result.message);
							}else{
								PJ.warn(result.message);
							}
							PJ.grid_reload(grid1);
							PJ.grid_reload(grid2);
						}
				});
				
		}); --%>
		
		$(".text").blur(function(){
			 $("#search").focus();
		 });
		 
		 $("#searchForm3").keydown(function() {
            /* if (event.keyCode == "13") { */
           	setTimeout(validatea,1000);
/*             } */
            
        });
		
	});
	
	function validatea(){
		var number = $("#searchForm3").val();
		var index = parseInt(number.indexOf(")"));
		if(index > 0){
			number = number.substring(3,index);
		}
		var start = number.substring(0,3);
		if(start=="040"){
			var checkId = number.substring(18);
			var iframeId = "listByLocation";
			PJ.topdialog(iframeId, '件号列表', '<%=path%>/storage/exportpackage/toPartListByLocation?locationId='+checkId+'&id='+${id },
					undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid1);PJ.grid_reload(grid2);}, 1000, 500, true);
			<%-- var checkId = number.substring(18);
			var searchString = "isll.id = "+checkId;
			var postData = {};
			var iframeId = "errorIframe"
			postData.searchString = searchString;
			$.ajax({
					type: "POST",
					dataType: "json",
					data: postData,
					url:'<%=path%>/storage/exportpackage/BarCodeAdd?id='+${id }+'&checkId='+checkId+'&clientId='+${clientId },
					success:function(result){
						if(result.success){
							PJ.success(result.message);
						}else{
							PJ.topdialog(iframeId, '多出件', '<%=path%>/storage/exportpackage/toErrorTable',
									undefined,function(item,dialog){
																	$.ajax({
																		type: "POST",
																		dataType: "json",
																		url:'<%=path%>/storage/exportpackage/deleteUnexportElement',
																		success:function(result){
																		}
																	});
																	dialog.close();
																	}, 1000, 500, true);
						}
						PJ.grid_reload(grid1);
						PJ.grid_reload(grid2);
					}
			}); --%>
			<%-- PJ.grid_search(grid2,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&searchString='+searchString); --%>
			//$("#search").val("");
			//$("#search").focus();
		}else if(parseInt(start.indexOf("03"))>-1){
			var length = number.length;
			if(length==17){
				var end=start.substring(2,3);
				var importPackageId = number.substring(3,10);
				var clientOrderElementId = number.substring(10,17);
				var postData = {};
				postData.importPackageId = importPackageId;
				postData.clientOrderElementId = clientOrderElementId;
				if(end!=0){
					var sequence = end;
					postData.sequence = sequence;
				}
				postData.id = ${id };
				$.ajax({
					type: "POST",
					dataType: "json",
					data: postData,
					url:'<%=path%>/storage/exportpackage/BarCodeAddByPartNumber?id='+${id }+'&token='+'${token}',
					success:function(result){
						if(result.success){
							PJ.success(result.message);
						}else{
							PJ.warn(result.message);
						}
						PJ.grid_reload(grid1);
						PJ.grid_reload(grid2);
					}
				});
			}else if(length==18){
				var importPackageId = number.substring(4,11);
				var clientOrderElementId = number.substring(11,18);
				var sequence = number.substring(2,4);
				var postData = {};
				postData.importPackageId = importPackageId;
				postData.clientOrderElementId = clientOrderElementId;
				postData.sequence = sequence;
				postData.id = ${id };
				$.ajax({
					type: "POST",
					dataType: "json",
					data: postData,
					url:'<%=path%>/storage/exportpackage/BarCodeAddByPartNumber?id='+${id }+'&token='+'${token}',
					success:function(result){
						if(result.success){
							PJ.success(result.message);
						}else{
							PJ.warn(result.message);
						}
						PJ.grid_reload(grid1);
						PJ.grid_reload(grid2);
					}
				});
			}else if(length==19){
				var importPackageId = number.substring(3,10);
				var clientOrderElementId = number.substring(10,17);
				var sequence = number.substring(17,19);
				var postData = {};
				postData.importPackageId = importPackageId;
				postData.clientOrderElementId = clientOrderElementId;
				postData.sequence = sequence;
				postData.id = ${id };
				$.ajax({
					type: "POST",
					dataType: "json",
					data: postData,
					url:'<%=path%>/storage/exportpackage/BarCodeAddByPartNumber?id='+${id }+'&token='+'${token}',
					success:function(result){
						if(result.success){
							PJ.success(result.message);
						}else{
							PJ.warn(result.message);
						}
						PJ.grid_reload(grid1);
						PJ.grid_reload(grid2);
					}
				});
			}else{
				var checkid = number.substring(15);
				var checkId = number.substring(13);
				var searchString = "(e.id= " + checkId +" or e.id="+ checkid+")";
				PJ.grid_search(grid2,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&searchString='+searchString+'&id='+${id});
			}
			
			//$("#search").val("");
			//$("#search").focus();
		}else if (start=="020"){
			var checkId = number.substring(13);
			var searchString = "e.id= " + checkId;
			PJ.grid_search(grid2,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&searchString='+searchString+'&id='+${id });
			
		}else if (start=="010"){
			var checkId = number.substring(18);
			var searchString = "isll.id = "+checkId;
			PJ.grid_search(grid2,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&searchString='+searchString+'&id='+${id });
			
		}
		$("#searchForm3").val("");
	}
	
	function trim(str){
		return $.trim(str);
	}
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
	//获取表单数据
	function getFormData2(){
		var $input = $("#searchForm").find("input,textarea,select");
		var postData = {};
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
					
				postData[$(this).attr("name")] = $(this).val();
			});
			if($("#taxReturn").is(':checked')==true){
				postData.taxReturn=1;
			}else{
				postData.taxReturn=0;
			}
			return postData;
	}
	
	
	//-- 验证
	function getValidate(){
		return validate2({
			nodeName:"amount",
			form:"#form"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
		$items.each(function(i){
			var name = $(this).attr("name");
			if(!name)return;
			for(var k in nodes){
				if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
				if(!flag){
	                $(this).addClass("input-error");
					tip = $(this).attr("data-tip");
					return false;
				}else $(this).removeClass("input-error");
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="出库单号   ${exportNumber } ">
		<div id="toolbar"></div>
		<div id="uploadBox" >
			<form id="form" name="form">
			 </form>
		</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
		<div position="centerbottom" title="新增出库明细">
			<div id="toolbar"></div>
			<div id="uploadBox" style="width: 100%;height: 70px;">
				<form id="searchForm">
					<input class="hide" name="token" id="token" value="${token}"/>
					<input class="hide" name="importPackageElementId" id="importPackageElementId"/>
					<input class="hide" name="exportPackageId" id="exportPackageId" value="${id }"/>
					<input class="hide" name="exportRackageInstructionsElementId" id="exportRackageInstructionsElementId"/>
					<input name="search" id="search" class="hide"/>
					<form:row columnNum="4">
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="searchForm2" class="text" type="text" name="partNumber" alias="ipe.part_number" oper="cn"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>条码</p></form:left>
							<form:right><p><input id="searchForm3" class="text" type="text" name="code" alias="code" oper="eq"/></p></form:right>
						</form:column>
						<form:column >
							<p style="padding-left:5px;">
							 <input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
							 <input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
							</p>
						</form:column>
					</form:row>
					<form:row columnNum="4">
						<form:column>
							<form:left><p>数量</p></form:left>
							<form:right><p><input class="text" name="amount" id="amount"/></p></form:right>
						</form:column>
						<form:column >
							<p style="padding-left:70px;">
							<!-- <input class="btn btn_orange" type="button" value="全部新增" id="addAll"/> -->
							<input class="btn btn_orange" type="button" value="新增" id="addBtn"/>
							</p>
						</form:column>
					</form:row>
					
				</form>
			</div>
			<table id="list2"></table>
			<div id="pager2"></div>
		</div>
	</div>
</body>
</html>