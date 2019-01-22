<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>供应商询价管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />

<script type="text/javascript">
var layout, grid;

$(function(){
	
	layout = $("#layout1").ligerLayout({
		centerBottomHeight: 270,
		onEndResize:function(e){
		  GridResize();
		}
	});
	
	$("#toolbar").ligerToolBar({
		items : [
					{
						id:'add',
						icon : 'add',
						text : '发送邮件',
						click: function(){
							var postData = getData();
							var roleName = "${roleName}"
							var emailIframeId="EmailIframe1";
							if(roleName=="国内采购"){
								PJ.topdialog(emailIframeId, '添加邮件内容', '<%=path%>/purchase/supplierinquiry/toAddEmailText',
										function(item, dialog){
											var email=top.window.frames[emailIframeId].getForm();
											postData.firstText = email.firstText;
											postData.secondText = email.secondText;
											PJ.showLoading("邮件发送中....");
											$.ajax({
												url: '<%=path%>/purchase/supplierinquiry/sendEmail',
												type: "POST",
												data:postData,
												loading: "正在处理...",
												success: function(result){
														PJ.hideLoading();
														if(result.success){
															PJ.success(result.message);
														} else {
															PJ.error(result.message);
														}		
												}
											});
											dialog.close();	
										},function(item,dialog){dialog.close();}, 1100, 500, true,"确定");
								}else{
									PJ.showLoading("邮件发送中....");
									$.ajax({
										url: '<%=path%>/purchase/supplierinquiry/sendEmail',
										type: "POST",
										data:postData,
										loading: "正在处理...",
										success: function(result){
												PJ.hideLoading();
												if(result.success){
													PJ.success(result.message);
												} else {
													PJ.error(result.message);
												}		
										}
									});
								}
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
		rowNum: 50,
		url:'<%=path%>/purchase/supplierinquiry/listData',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight(),
		autoSrcoll:true,
		//shrinkToFit:false,
		sortname : "si.update_timestamp",
		sortorder : "desc",
		inLineEdit: true,
		pager: "#pager1",
		onSelectRow:function(rowid,status,e){
			onSelect();
		},
		multiselect:true,
		editurl:'<%=path%>/purchase/supplierinquiry/edit',
		colNames :["id","供应商","询价单号","供应商询价单号","询价日期","截至日期","报价比例","备注","更新时间","报价单数量","发送状态"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("supplierCode", {sortable:true,width:50,align:'left'}),
		           PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,width:100,align:'left',hidden:true}),
		           PJ.grid_column("supplierInquiryQuoteNumber", {sortable:true,width:90,align:'left'}),
		           PJ.grid_column("supplierInquiryDate", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("supplierDeadline", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("proportion",{sortable:true,width:60,align:'left'}),
		           PJ.grid_column("remark",{sortable:true,width:120,align:'left',editable:true}),
		           PJ.grid_column("updateTimestamp", {sortable:true,width:100,align:'left'}),
		           PJ.grid_column("supplierQuoteCount", {sortable:true,width:60,align:'left'}),
		            PJ.grid_column("emailStatus", {sortable:true,width:60,align:'left',
		            	formatter: statusFormatter})
		           ]
	});
	
	grid2 = PJ.grid("list2", {
		rowNum: 20,
		url:'<%=path%>/purchase/supplierinquiry/ManageElementList?id=0',
		width : PJ.getCenterWidth(),
		height : PJ.getCenterHeight(),
		autoSrcoll:true,
		pager: "#pager2",
		//shrinkToFit:false,
		//sortname : "ci.inquiry_date",
		colNames :["id","序号","件号","另件号","描述","单位","数量","更新时间"],
		colModel :[PJ.grid_column("id", {key:true,hidden:true}),
		           PJ.grid_column("item", {sortable:true,width:60,align:'left'}),
		           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("alterPartNumber", {sortable:true,width:120,align:'left'}),
		           PJ.grid_column("description", {sortable:true,width:200,align:'left'}),
		           PJ.grid_column("unit", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("amount", {sortable:true,width:70,align:'left'}),
		           PJ.grid_column("updateTimestamp",{sortable:true,width:100,align:'left'})
		           ]
	});
	
	function onSelect(){
		var ret = PJ.grid_getSelectedData(grid);
		var id = ret["id"];
		PJ.grid_search(grid2,'<%=path%>/purchase/supplierinquiry/ManageElementList?id='+id);
	};
	
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
					$("#searchForm5").append($option);
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
	    	PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/listManage');
	    }  
	}); 
	
	//搜索
	$("#searchBtn").click(function(){
		PJ.grid_search(grid,'<%=path%>/purchase/supplierinquiry/listManage');
		
	});
	
	//重置条件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	$("#searchForm2").blur(function(){
		var text = $("#searchForm2").val();
		$("#searchForm2").val(trim(text));
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
	
	//获取表单数据
	function getData(){
		var postData = {};
		var ids =  PJ.grid_getMutlSelectedRowkey(grid);
		supplierInquiryIds = ids.join(",");
		if(supplierInquiryIds.length>0){
			postData.supplierInquiryIds = supplierInquiryIds;
		}
		return postData;
	}
	
});

function getData(){
	return postData;
}

function trim(str){
	return $.trim(str);
}

function statusFormatter(cellvalue, options, rowObject){
	switch (cellvalue) {
	case 1:
		return '是';
		break;
		
	case '':
		return '否';
		break;
		
	default: 
		return '否';
		break; 
	}
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
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>邮件状态：</p></form:left>
								<form:right><p><select id="searchForm2" class="text" type="text" name="quoteNumber" alias="si.email_status" oper="cn">
													<option value="0">未发送</option>
													<option value="1">已发送</option>
												</select>
										    </p>
								</form:right>
							</form:column>
							<form:column>
								<form:left>供应商：</form:left>
								<form:right><p>
													<select id="searchForm5" name="inquiryStatusCode" alias="s. CODE" oper="eq">
													<option value="">全部</option>
							            			</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>询价日期：</p></form:left>
								<form:right><p><input id="inquiryStart" class="text" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'inquiryEnd\')||\'new Date()\'}',dateFmt:'yyyy-MM-dd'})" name="inquiryStart" alias="si.inquiry_date" oper="gt"/> </p></form:right>
							</form:column>
							
							<form:column>
								<form:left><p> - </p></form:left>
								<form:right><p><input id="inquiryEnd" class="text" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'inquiryStart\')}',maxDate:new Date(),dateFmt:'yyyy-MM-dd'})" name="inquiryEnd" alias="si.inquiry_date" oper="lt"/> </p></form:right>
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
		<div position="centerbottom" title="">
		<div id="toolbar"></div>
			<table id="list2"></table>
			<div id="pager2"></div>
		</div>
	</div>

</body>
</html>