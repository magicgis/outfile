<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>出库指令</title>

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
							id:'add',
							icon : 'add',
							text : '明细',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								var code = ret["code"];
								var iframeId="ideaIframe3";
									PJ.topdialog(iframeId, '出库指令明细 ', '<%=path%>/storage/exportpackage/toExportPackageInstructionsElement?id='+id+'&code='+code,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
							}
					 },
					 {
							id:'edit',
							icon : 'edit',
							text : '批量打印标签',
							click: function(){
								var ret = PJ.grid_getSelectedData(grid);
								var id = ret["id"];
								$.ajax({
									type: "POST",
									dataType: "json",
									url:'<%=path%>/storage/exportpackage/getLabelList?id='+id,
									success:function(result){
										var obj = eval(result.message)[0];
										if(result.success){
											for(var i in obj){
												var importPackageElementId=obj[i].importPackageElementId;
												var amount=obj[i].amount;
												var soeid=obj[i].supplierOrderElementId;
												var id=obj[i].importPackageId;
												var inspectionDate=obj[i].inspectionDateString;
												var manufactureDate=obj[i].manufactureDateString;
												var printPartNumber=obj[i].partNumber;
												var printDescription=obj[i].description;
												var resume=obj[i].completeComplianceCertificate;
												var complianceCertificate=obj[i].complianceCertificate;
												var serialNumber=obj[i].serialNumber;
												var location=obj[i].location;
												var $a = $("<a href='partiframes?idate="+inspectionDate+"&id="+soeid+"&sl="+location+"&resume="+resume
															+"&complianceCertificate="+complianceCertificate+"&type=update"+"&ipeId="+importPackageElementId
															+"&amount="+amount+"&ipid="+id+"&mdate="+manufactureDate+"&sn="+encodeURIComponent(serialNumber)+
															"&pdesc="+encodeURIComponent(printDescription)+"&ppart="+encodeURIComponent(printPartNumber)+"'></a>");
												$a.printPage();
							 					$a.trigger("click");
							 					if(window.closed){
													top.window.frames[iframeId].onfous();
												}
											}
										}else{
											PJ.warn(result.message);
										}
									}
							});
							}
					 },
					 {
						    id:'down',
							icon : 'down',
							text : '批量生成合格证',
							click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var iframeId = "excelIframeId";
									PJ.topdialog(iframeId, '位置列表 ', '<%=path%>/storage/exportpackage/toLotsExcel?id='+id,
											undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
							}
					 },
					 {
						    id:'view',
							icon : 'view',
							text : '查看不出库记录',
							click: function(){
									var ret = PJ.grid_getSelectedData(grid);
									var id = ret["id"];
									var clientId = ret["clientId"];
									var iframeId = "elementIframeId";
									PJ.topdialog(iframeId, '位置列表 ', '<%=path%>/storage/exportpackage/toNotInInstruction?id='+id+'&clientId='+clientId,
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
			url:'<%=path%>/storage/exportpackage/exportPackageInstructionsList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			autoSrcoll:true,
			//shrinkToFit:false,
			sortname : "epi.creat_date",
			sortorder : "desc",
			colNames :["id","客户Id","客户","出库指令单号","创建日期","备注"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientId", {hidden:true}),
			           PJ.grid_column("code", {sortable:true,width:90,align:'left'}),
			           PJ.grid_column("exportPackageInstructionsNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("creatDate", {sortable:true,width:110,align:'left'}),
			           PJ.grid_column("remark",{sortable:true,width:270,align:'left'})
			          
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
		    	PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportPackageInstructionsList');
		    }  
		}); 
			
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/exportpackage/exportPackageInstructionsList');
			
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
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							
							<form:column>
								<form:left>客户：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="epi.code" oper="eq">
							        			<option value="">全部</option>
							            		</select>
											</p>
								</form:right>
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