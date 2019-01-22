<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>收款管理 </title>

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
						text : '新增收款',
						click: function(){
							var iframeId="incomeIframe";
							PJ.topdialog(iframeId, '新增收款', '<%=path%>/sales/clientorder/toAddIncome?id='+${clientInvoiceId},
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
												url: '<%=path%>/sales/clientorder/saveAdd?InvoiceId='+${clientInvoiceId },
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
															
														} else {
															PJ.error(result.message);
														}		
												}
											});
									PJ.grid_reload(grid);
									dialog.close();}
								   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
						 	id:'add',
							icon : 'add',
							text : '新增明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var clientInvoiceId = ret["clientInvoiceId"];
								var id = ret["id"];
								var iframeId="incomeIframe";
								PJ.topdialog(iframeId, '新增收款', '<%=path%>/sales/clientorder/toAddDetail?clientInvoiceId='+clientInvoiceId,
										function(item,dialog){
											 var postData=top.window.frames[iframeId].getFormData();	 							
											 $.ajax({
													url: '<%=path%>/sales/clientorder/saveAddDetail?id='+id+'&clientInvoiceId='+clientInvoiceId,
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
			url:'<%=path%>/sales/clientorder/IncomeList?clientInvoiceId='+${clientInvoiceId },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager1",
			onSelectRow:function(rowid,status,e){
				onSelect();
			},
			editurl:'<%=path%>/sales/clientorder/saveEdit',
			sortname : "ci.RECEIVE_DATE",
			colNames :["id","发票id","收款日期","收款金额","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("clientInvoiceId", {key:true,editable:true,hidden:true}),
			           PJ.grid_column("receiveDate", {sortable:true,width:170,editable:true,align:'left',
			        	   editoptions:{ 
			        		   dataInit:function(el){ 
			        		     $(el).click(function(){ 
			        		       WdatePicker(); 
			        		     }); 
			        		   } 
			        	   } 
			           }),
			           PJ.grid_column("totalSum", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:300,editable:true,align:'left'})
			           ]
		});
		
		grid2 = PJ.grid("list2", {
			rowNum: 20,
			url:'<%=path%>/sales/clientorder/incomeDetail?id=0',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight(),
			autoSrcoll:true,
			//shrinkToFit:false,
			inLineEdit: true,
			pager: "#pager2",
			editurl:'<%=path%>/sales/clientorder/editIncomeDetail',
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid2);
				PJ.grid_reload(grid);
			},
			sortname : "id.UPDATE_TIMESTAMP",
			colNames :["id","订单明细id","件号","单价","数量","总价","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("clientOrderElementId", {key:true,hidden:true,editable:true,}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("price", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:170,align:'left'}),
			           PJ.grid_column("total", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:300,editable:true,align:'left'})
			           ]
		});
		
		function onSelect(){
			var ret = PJ.grid_getSelectedData(grid);
			var id = ret["id"];
			PJ.grid_search(grid2,'<%=path%>/sales/clientorder/incomeDetail?id='+id);
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
		<div position="center" title="发票单号:  ${clientInvoiceNumber }&nbsp;&nbsp;发票比例: ${term}%">
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