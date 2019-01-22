<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>未换位置提醒</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
		items : [ 
					{
						icon : 'process',
						text : '修改',
						click : function(){
							var ret = PJ.grid_getSelectedData(grid);
							var id = ret["id"]; 
							var clientId = ret["clientId"]; 
							var supplierId = ret["supplierId"]; 
							var clientOrderNumber = ret["clientOrderNumber"]; 
							var importPackageId=ret["importPackageId"];
							var url='<%=path%>/importpackage/editimportpackageelement?id='+id
				 			+'&supplierId='+supplierId+'&clientId='+clientId+'&clientOrderNumber='+clientOrderNumber;
							var iframeId = 'importpackageelementFrame';
							var btnArr=[];
							var editCancel={text:"修改",onclick:function(item,dialog){
								 var postData=top.window.frames[iframeId].getFormData();	 							
								 var nullAble=top.window.frames[iframeId].validate();
									if(nullAble){
								 $.ajax({
									    url: '<%=path%>/importpackage/editimportpackageelementdate?importPackageId='+id,
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
							}};
							
							var printCancel1={text:"件号条形码打印",onclick:function(item,dialog){
								 var postData=top.window.frames[iframeId].getFormData();	 
								var elementId=postData.elementId;
								var id=postData.soeid;
								var printAmount;
								if (typeof(postData.printAmount) == "undefined") { 
									printAmount=0; 
									}else{
										printAmount=postData.printAmount;
									}
								var ipeId=postData.id;
								var amount=postData.amount;
								var inspectionDate=postData.inspectionDate;
								var manufactureDate=postData.manufactureDate;
								var printPartNumber=postData.printPartNumber;
								var type="update";
								printPartNumber=printPartNumber.replace(/'/g,"%27");
								var printDescription=postData.printDescription;
								var location=postData.location;
								var $a = $("<a href='partiframes?idate="+inspectionDate+"&id=+"+id+"&pamount=+"+printAmount+"&sl=+"+location
										+"&amount=+"+amount+"&ipid=+"+importPackageId+"&mdate=+"+manufactureDate+"&ipeId=+"+ipeId+"&type=+"+type+
										"&pdesc=+"+encodeURIComponent(printDescription)+"&ppart=+"+printPartNumber+"'></a>");
								$a.printPage();
								$a.trigger("click");
								
								 var ret = PJ.grid_getSelectedData(grid);
								 var supplierQuoteElementId=ret["supplierQuoteElementId"];
								 $.ajax({
									    url: '<%=path%>/importpackage/storage?supplierQuoteElementId='+supplierQuoteElementId,
										data: '',
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													var message=result.message;
													var obj = eval(message);
													 var importPackageId = obj[0].importPackageId;
											 		 var supplierOrderElementId=obj[0].supplierOrderElementId;
											 		 var storageAmount=obj[0].storageAmount;
											 		 var partNumber=obj[0].partNumber;
											 		 var importPackageElementId=obj[0].importPackageElementId;
											 		 var type="update";
													 var $a = $("<a href='<%=path%>/importpackage/partiframes?id="+supplierOrderElementId+"&ipid=+"
															+importPackageId+"&ipeId=+"+importPackageElementId+"&type=+"+type+"&amount=+"+storageAmount+"&ppart=+"+partNumber+"'></a>");
													$a.printPage();
													$a.trigger("click");
												} 	
										}
									});
								
							}};
		 					
		 					var printCancel2={text:"箱子条形码打印",onclick:function(item,dialog){
								 var postData=top.window.frames[iframeId].getFormData();	 
		 						var location=postData.location;
		 						var clientCode=postData.clientCode;
								var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"'></a>");
								$a.printPage();
								$a.trigger("click");
							}};
		 					
		 					var closeCancel={text:"关闭",onclick:function(item,dialog){
		 						PJ.grid_reload(grid);dialog.close();
		 					}};
		 					
		 					btnArr.push(closeCancel);
		 					btnArr.push(printCancel1);
		 					btnArr.push(printCancel2);
		 					btnArr.push(editCancel);
		 					
		 					var opts = {
		 							buttons: btnArr,
		 							width: 1000, 
		 							height: 500,
		 							showMax: true
		 					};
		 					
		 					PJ.openTopDialog(iframeId, "财务 - 修改入库单明细", url, opts);
							
				  			}
				  },
			 	  {	
					icon : 'logout',
					text : '导出excel',
					click : function() {
							PJ.grid_export("list1");
							}
				  },	
				  {
					icon : 'view',
					text : '生成合格证',
					click : function(){
						Excel();
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
								grid.setGridHeight(PJ.getCenterHeight()-62);
							}
						});
					}
				 }
				]
		
	});
		
	grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/importpackage/unchangeLocationPage',
			width : PJ.getCenterWidth(),
			rowList:[10,20,50],
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			colNames :["id","客户","入库单号","件号","数量","库存单位","位置","clientId","supplierId","clientOrderNumber","supplierQuoteElementId","importPackageId"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importAmount", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importUnit", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("supplierId", {hidden:true}),
			           PJ.grid_column("clientOrderNumber", {hidden:true}),
			           PJ.grid_column("supplierQuoteElementId", {hidden:true}),
			           PJ.grid_column("importPackageId", {hidden:true}),
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
						$option.val(obj[i].id).text(obj[i].code);
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
		    	PJ.grid_search(grid,'<%=path%>/importpackage/unchangeLocationPage');
		    }  
		}); 
		
		//搜索
		$("#searchBtn").click(function(){
			PJ.grid_search(grid,'<%=path%>/importpackage/unchangeLocationPage');
			
		});
		
		//重置条件
		$("#resetBtn").click(function(){
			$("#searchForm")[0].reset();
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
		$("#orderNumber").blur(function(){
			var text = $("#orderNumber").val();
			$("#orderNumber").val(trim(text));
		});
		
	});
	
	function Excel(){
		var iframeId = 'clientquoteFrame';
	 	PJ.topdialog(iframeId, '生成合格证', 
	 			'<%=path%>/importpackage/unchangecertification',
	 			undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
	}
	
	function trim(str){
		return $.trim(str);
	}
	
	function CountFormatter(cellvalue, options, rowObject){
		switch (cellvalue) {
		case 0:
			return '0';
			break;
			
		default: 
			return cellvalue;
			break; 
		}
	}
	
	
	//子页面必须提供表单数据方法
	function getFormData(){
		 var data = $("#form").serialize();
		 return data;
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
					<form:row columnNum="3">
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="a.client_id" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
							</form:column>
							<form:column >
								<form:left></form:left>
								<form:right></form:right>
							</form:column>
							<form:column >
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
	</div>
</body>
</html>