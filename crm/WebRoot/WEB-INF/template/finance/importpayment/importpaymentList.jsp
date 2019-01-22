<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>财务-付款管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	var paymentStatus;
	var paymentType = "0:预付款;1:发货付款;2:验收付款";
	
	$(function() {
		layout = $("#layout1").ligerLayout({
			centerBottomHeight: 170,
			onEndResize:function(e){
			  GridResize();
			}
		});
		
		$("#toolbar").ligerToolBar({
			items : [
					 {
						 	id:'add',
							icon : 'add',
							text : '新增付款',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var importPackageId = ret["importPackageId"];
								var iframeId="incomeIframe";
								PJ.topdialog(iframeId, '新增付款', '<%=path%>/importpackage/toPaymentElement?id='+id,
										function(item,dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 $.ajax({
													url: '<%=path%>/importpackage/AddPaymentElementTotal',
													data: postData,
													type: "POST",
													loading: "正在处理...",
													success: function(result){
															if(result.success){
																PJ.success(result.message);
																PJ.grid_reload(grid);
																PJ.grid_reload(grid2);
															} else {
																PJ.error(result.message);
															}		
													}
												});
										PJ.grid_reload(grid);
										PJ.grid_reload(grid2);
										dialog.close();}
									   ,function(item,dialog){PJ.grid_reload(grid);PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
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
		$("#toolbar2").ligerToolBar({
			items : [
					 
					]
		});
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/finance/importpackagepayment/List',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
				var postData = {};
				postData.id = rowid;
				$.ajax({
					url: '<%=path%>/finance/importpackagepayment/getPaymentStatus',
					data: postData,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								paymentStatus = result.message;
							}
					}
				});
			},
			editurl:'<%=path%>/importpackage/editPayment',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
			},
			sortname : "ipp.update_timestamp",
			sortorder : "desc",
			colNames :["id","入库id","供应商","付款单号","付款日期","应付金额","已付金额","审批状态","类型","状态","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("importPackageId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("code", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("paymentNumber", {key:true,width:120,editable:true,align:'left'}),
			           PJ.grid_column("paymentDate", {sortable:true,width:100,editable:true,align:'left',
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   } 
			           }),
			           PJ.grid_column("shouldPay", {sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("paymentTotal", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("spzt",{sortable:true,align:'left',formatter: function(value){
							if (value==232) {
								return "审批中";
							} else if (value==235||value==233) {
								return "审批结束";
							}if (value==234) {
								return "未发起审批";
							}
						}
						}),
					   PJ.grid_column("paymentType", {sortable:true,width:90,align:'left',editable:true,
						   edittype:"select",formatter:typeFormatter,
		        		   editoptions:{value:
								function(){
										return paymentType;        			   
		        		   		}
							}
				       }),	
			           PJ.grid_column("paymentStatusValue", {sortable:true,width:80,editable:true,align:'left',
			        	   edittype:"select",formatter:"",
		        		   editoptions:{value:
								function(){
										return paymentStatus;        			   
		        		   		}
							}   
			           }),
			           PJ.grid_column("remark", {sortable:true,width:200,editable:true,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager2",
			editurl:'<%=path%>/importpackage/editPaymentElement',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
				PJ.grid_reload(grid);
			},
			sortname : "ippe.update_timestamp",
			colNames :["id","付款id","订单明细id","件号","数量","比例","应付","已付","审批状态","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("importPackagePaymentId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("supplierOrderElementId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("paymentPercentage", {sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("shouldPay", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("paymentSum", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("spzt",{
							sortable:true,
							formatter : function(value){
								if (value==232) {
									return "审批中";
								} else if (value==235) {
									return "审批通过";
								}if (value==234) {
									return "未发起审批";
								}else if(value==233){
									return "审批不通过";
								}
							}
						}),
			           PJ.grid_column("remark", {sortable:true,width:300,editable:true,align:'left'})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			PJ.grid_search(grid2,'<%=path%>/importpackage/paymentElementList?importPackagePaymentId='+id);
		};
		
		 
		//改变窗口大小自适应
		/* $(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-182);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		}); */
		$(window).resize(function() {
			GridResize();
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/List');
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/finance/importpackagepayment/List');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
		 $("#searchForm2").blur(function(){
				var text = $("#searchForm2").val();
				$("#searchForm2").val(trim(text));
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
		 
		function GridResize() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid2.setGridWidth(PJ.getCenterBottomWidth());
			grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
			grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		}
		grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
		grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
		
	});
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
	function typeFormatter(cellvalue, options, rowObject){
		var type = rowObject["paymentType"];
		
		switch (type) {
			case 0:
				return "预付款";
				break;
			case 1:
				return "发货付款";
				break;
			case 2:
				return "验收付款";
				break;	
				
			default: 
				return cellvalue;
				break; 
		}
	}
	
	function trim(str){
		return $.trim(str);
	}
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>单号：</p></form:left>
								<form:right><p><input id="Number" class="text" type="text" name="Number" alias="ipp.PAYMENT_NUMBER or so.ORDER_NUMBER or ip.IMPORT_NUMBER" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>供应商：</form:left>
								<form:right><p>
												<select id="supplierCode" name="supplierCode" alias="s.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>类型：</p></form:left>
								<form:right><p>
												<select id="searchForm1" name="supplierCode" alias="ipp.PAYMENT_TYPE" oper="eq">
												<option value="">全部</option>
							        			<option value="0">预付款</option>
							        			<option value="1">发货付款</option>
							        			<option value="2">尾款</option>
							            		</select>
								</p></form:right>
							</form:column>
							<form:column>
								<form:left><p>付款日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="ipp.PAYMENT_DATE" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="ipp.PAYMENT_DATE" oper="lt"/> </p></form:right>
							</form:column>
						</form:row>
						
						<form:row columnNum="5">
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
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
			<table id="list2" style=""></table>
			<div id="pager2"></div>
  		</div>
	</div>
</body>
</html>