<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>明细列表</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>

<script type="text/javascript">
 	//-- Set Attribute
 	
	var layout, grid;
	$(function() {
		layout = $("#layout_main").ligerLayout();
		
		$("#submit").click(function(){
			var url = '<%=path%>/supplierquote/quoteUploadExcel?data='+getFormData().ids;
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	//var message = decodeURI(data);
	            	//var a = decodeURI(data);
	            	//var da = jQuery.parseJSON(jQuery(data).text());
	            	var da = eval(data)[0];
	            	//var falg = data.flag;
	            	if(da.flag==true){
	            		
		            	PJ.success(da.message);
		            	PJ.grid_reload(grid);
	            	}
	            	else{
	            		iframeId = 'errorframe'
	                		PJ.topdialog(iframeId, '错误信息', '<%=path%>/supplierquote/toUnknow',
	                				undefined,function(item,dialog){
	    		            			$.ajax({
	    									url: '<%=path%>/supplierquote/deleteData',
	    									type: "POST",
	    									loading: "正在处理...",
	    									success: function(result){
	    									}
	    								});
	                					dialog.close();}, 1000, 500, true);
	            		closeoropen();
	    	           
	            	}
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            	closeoropen();
	            }  
	        });  
		});
		

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ 
			       
			         ]
		});
		
		
		grid = PJ.grid("list", {
			rowNum: -1,
			url: "<%=path%>/system/airmanage/airlistData",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-70,
			autoSrcoll:true,
			shrinkToFit:true,
			multiselect:true,
			sortname : "",
			colNames : ["id", "code","名称"],
			colModel : [ PJ.grid_column("id", {sortable:true,width:100,key:true,hidden:true}),
			            PJ.grid_column("code", {sortable:true,width:100}),
			            PJ.grid_column("name", {sortable:true,width:100})
						]
		});
		
		
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-102);
			}
		});
		
		
	});
	
	
	
	 function getFormData(){
		 var postData = {};
		 var rowKey = grid.getGridParam("selarrrow");
		 if(rowKey!= ""){
			 var air =  PJ.grid_getMutlSelectedRowkey(grid);
			 ids = air.join(",");
			 if(ids.length>0){
					postData.ids = ids;
				}
		 }
		 if(rowKey== ""){
				PJ.warn("请选择需要操作的数据");
				return false;
		 }
			return postData;
	 }	
</script>
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-bottom;
  padding-top:10px;
 } */
 #cb_list1{
		margin:0
	}
</style>
</head>

<body style="padding:3px">
<input type="text" class="hide" name="conditionCode" id="conditionCode" value="">
<input type="text" class="hide" name="certificationCode" id="certificationCode" value="">
<input type="text" class="hide" name="supplierQuoteStatusValue" id="supplierQuoteStatusValue" value="">
<input type="text" class="hide" name="supplierInquiryId" id="supplierInquiryId" value="${supplierInquiryId}">
	<div id="layout_main">
		<div position="center" title="">
		<div id="uploadBox" >
	<form id="form" name="form">
	 	<input type="hidden" name="id" id="id" value="${id}"/>
			<form:row columnNum="2">
				<form:column width="120">
					<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
					<form:right>
						<p><input type="file" value="" id="file" name="file"/>&nbsp;
						   <input type="button" id="submit" value="上传"/>
						</p>
					</form:right>
				</form:column>
			</form:row>
	            
	 </form>
	</div>
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
