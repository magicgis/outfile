<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>付款 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
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
							text : '新增明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var supplierId = ret["supplierId"];
								var iframeId="incomeIframe";
								PJ.topdialog(iframeId, '新增付款明细', '<%=path%>/importpackage/toAddPaymentElement?supplierId='+supplierId,
										function(item,dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 $.ajax({
													url: '<%=path%>/importpackage/AddPaymentElement?id='+id,
													data: postData,
													type: "POST",
													loading: "正在处理...",
													success: function(result){
															if(result.success){
																PJ.success(result.message);
																PJ.grid_reload(grid2);
																PJ.grid_reload(grid);
															} else {
																PJ.error(result.message);
															}		
													}
												});
										PJ.grid_reload(grid);
										dialog.close();}
									   ,function(item,dialog){PJ.grid_reload(grid2);dialog.close();}, 1000, 500, true,"新增");
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
			url:'<%=path%>/importpackage/paymentList?supplierId='+${supplierId },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			editurl:'<%=path%>/importpackage/editPayment',
			sortname : "ipp.payment_date",
			colNames :["id","供应商id","付款单号","付款日期","付款比例","应付","已付","状态","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("supplierId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("paymentNumber", {key:true,editable:true}),
			           PJ.grid_column("paymentDate", {sortable:true,width:170,editable:true,align:'left',
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   } 
			           }),
			           PJ.grid_column("paymentPercentage", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("shouldPay", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("paymentTotal", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("paymentStatusValue", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:300,editable:true,align:'left'})
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
			colNames :["id","付款id","订单明细id","件号","数量","应付","已付","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("importPackagePaymentId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("supplierOrderElementId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("shouldPay", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("paymentSum", {sortable:true,width:170,editable:true,align:'left'}),
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
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="">
		<div id="toolbar"></div>
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