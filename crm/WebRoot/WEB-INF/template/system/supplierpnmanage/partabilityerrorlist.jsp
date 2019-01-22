<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>错误信息</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
		});
		
		grid = PJ.grid("list1", {
			rowNum: '${count}',
			url:'<%=path%>/supplierpnmanage/partAbilityErrorList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-102,
			multiselect:true,
			autoSrcoll:true,
			shrinkToFit:false,
			pager: "#pager2",
			/* datatype:"local",
			data:data, */
			<%-- inLineEdit: true,
			editurl:'<%=path%>/supplierpnmanage/updateSupplierPn', --%>
			/* sortname : "s.code",
			sortorder : "asc", */
			gridComplete: function() {           
				 var rowIds = jQuery("#list1").jqGrid('getDataIDs');
	             for(var k=0; k<rowIds.length; k++) {
	                var curRowData = jQuery("#list1").jqGrid('getRowData', rowIds[k]);
	                if(curRowData.id== null || curRowData.id== undefined || curRowData.id== ''){
	                	 $("#list1").find("input[id='jqg_" + rowIds[k] + "']").attr("disabled", true);
	                }else{
	                	var curChk = $("#"+rowIds[k]+"").find(":checkbox");
	                    curChk.attr('name', 'checkboxname');
	                    curChk.attr('value', curRowData['id']);
	                    curChk.attr('title', curRowData['supplierId'] );
	                }
	                var a = curRowData.ifMatch;
	                if(curRowData.ifMatch=="是"){
	                	$("#list1").jqGrid('setSelection',rowIds[k]);
	                }
	             }
	             /* $("#list1").jqGrid('clearGridData');
				 $("#list1").jqGrid('setGridParam',{  // 重新加载数据
					     datatype:'local',
					     data : data,   //  newdata 是符合格式要求的需要重新加载的数据 
					     rowNum: '${count}',
					     page:1
				}).trigger("reloadGrid"); */
			},
			colNames :["id","bsn", "件号","描述","商品库件号","商品库描述","msn","厂商名称","是否匹配"],
			colModel :[PJ.grid_column("id", {hidden:true,editable:true}),
			           PJ.grid_column("bsn", {hidden:true,key:true}),
			           PJ.grid_column("consultPartNum", {sortable:true,width:180}),
			           PJ.grid_column("consultPartName", {sortable:true,width:180}),
			           PJ.grid_column("partNum", {sortable:true,width:180}),
			           PJ.grid_column("partName", {sortable:true,width:180}),
			           PJ.grid_column("msn", {sortable:true,width:180}),
			           PJ.grid_column("manName", {sortable:true,width:180}),
			           PJ.grid_column("ifMatch", {sortable:true,width:50,align:'left',
			        	   formatter: function(value){
			        		   	switch (value) {
				   				case 1:
				   					return "是";
				   					break;
				   				case 0:
				   					return "否";
				   					break;	
				   				default: 
				   					return value;
				   					break; 
				   				}
							}      
			           })
			           ]
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
		
		$("#searchForm2").change(function(){
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/sales/clientinquiry/test',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						
					}
				}
			});
		});
		
	});
	
	function getData(){
		var postData = {};
		var id = new Array();
		var msn = new Array();
		var bsn = new Array();
		var key =  PJ.grid_getMutlSelectedRowkey(grid);
		for(var k=0; k<key.length; k++) {
	         var curRowData = jQuery("#list1").jqGrid('getRowData', key[k]);
	         id.push(curRowData['id']);
	         msn.push(curRowData['msn']);
	         bsn.push(curRowData['bsn']);
		}
			
		postData.msn = msn.join(",");
		postData.bsn = bsn.join(",");
		postData.id = id.join(",");
		return postData;
	}
	
	function trim(str){
		return $.trim(str);
	}
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>
</head>

<body>
	<div id="layout1">
		<div position="center" title="错误信息">
		<div id="toolbar"></div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>