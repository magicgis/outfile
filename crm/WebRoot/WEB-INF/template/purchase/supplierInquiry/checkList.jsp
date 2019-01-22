<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-新增供应商询价 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
var layout, grid;
$(function(){
	layout = $("#layout1").ligerLayout();
	$("#toolbar").ligerToolBar({
		items : [
					{
						id:'modify',
						icon : 'modify',
						text : '保存',
						click: function(){
							 var postData=getData();
							 if(postData.bsn != ""){
								 PJ.showLoading("修改中....");
								 PJ.ajax({
										url: '<%=path%>/purchase/supplierinquiry/saveBsnInClientInuquiryElement',
										data: postData,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													PJ.hideLoading();
													PJ.grid_reload(grid);
													PJ.success(result.message);
													dialog.close();
												} else {
													PJ.hideLoading();
													PJ.grid_reload(grid);
													PJ.error(result.message);
													dialog.close();
												}		
										}
								});
							 }
						}
					}
		         ]
	});
	
	grid = PJ.grid("list1", {
		rowNum: ${size},
		url:'<%=path%>/purchase/supplierinquiry/autoList?id='+${id},
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight()-182,
		autoSrcoll:true,
		multiselect:true,
		inLineEdit: true,
		editurl:'<%=path%>/stock/search/addPartByMatch',
		aftersavefunc:function(rowid,result){
			PJ.grid_reload(grid);
		},
		//multiboxonly: true,
		//shrinkToFit:false,
		//sortname : "ci.update_timestamp",
		//sortorder : "desc",
		colNames :["key","bsn","id","询价件号","询价描述","商品库件号","参考描述","MSN","厂家名","是否匹配"],
		colModel :[PJ.grid_column("key", {key:true,hidden:true}),
		           PJ.grid_column("bsn", {hidden:true}),
		           PJ.grid_column("id", {hidden:true,editable:true}),
		           PJ.grid_column("consultPartNum", {sortable:true,width:130,align:'left'}),
		           PJ.grid_column("consultPartName", {sortable:true,width:170,align:'left'}),
		           PJ.grid_column("partNum", {sortable:true,width:130,align:'left'}),
		           PJ.grid_column("partName", {sortable:true,width:250,align:'left'}),
		           PJ.grid_column("msn", {sortable:true,width:130,align:'left',editable:true}),
		           PJ.grid_column("manName", {sortable:true,width:170,align:'left'}),
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
		           ],
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
		}
	});
	
	// 绑定键盘按下事件  
	$(document).keypress(function(e) {
		// 回车键事件  
	    if(e.which == 13) {
	    	PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/autoList?id='+${id}+'&msnFlag='+$("#msnFlag").val());
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/autoList?id='+${id}+'&msnFlag='+$("#msnFlag").val());
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
	
	/* function(){
		
	} */
	
	
});

function getData(){
	var postData = {};
	var clientInquiryElementIds = new Array();
	var msn = new Array();
	var bsn = new Array();
	var key =  PJ.grid_getMutlSelectedRowkey(grid);
	for(var k=0; k<key.length; k++) {
         var curRowData = jQuery("#list1").jqGrid('getRowData', key[k]);
         clientInquiryElementIds.push(curRowData['id']);
         msn.push(curRowData['msn']);
         bsn.push(curRowData['bsn']);
	}
		
	postData.msn = msn.join(",");
	postData.bsn = bsn.join(",");
	postData.clientInquiryElementIds = clientInquiryElementIds.join(",");
	return postData;
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
	<div position="center" title="采购-新增供应商询价">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;">
			<form id="searchForm">
				<div class="search-box">
					<form:row columnNum="3">
						<form:column>
							<form:left><p>件号：</p></form:left>
							<form:right><p><input id="searchForm2" class="text" type="text" name="quote_number" alias="part_number" oper="cn"/> </p></form:right>
						</form:column>
						<form:column>
							<form:left><p>标志位：</p></form:left>
							<form:right><p><select id="msnFlag" class="text" type="text" name="msnFlag" >
												<option value="">全部</option>
												<option value="0">国外</option>
												<option value="6">国内</option>
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