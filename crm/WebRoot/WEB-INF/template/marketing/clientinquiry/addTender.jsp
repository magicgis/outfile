<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>竞争对手报价管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">

$(function(){
	layout = $("#layout1").ligerLayout();
	
	$("#submit").click(function(){
		var url = '<%=path%>/sales/clientinquiry/uploadCompetitor?id='+${id}+'&editType='+ getFormData().toString();
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
	            	$("#uploadBox").toggle(function(){
						var display = $("#uploadBox").css("display");
						$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
						grid.setGridHeight(PJ.getCenterHeight()-142);
						window.location=window.location;
	            	});
            	}
            	else{
            		PJ.grid_reload(grid);
            		PJ.error(da.message);
            		$("#uploadBox").toggle(function(){
						var display = $("#uploadBox").css("display");
						$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
						grid.setGridHeight(PJ.getCenterHeight()-142);
						window.location=window.location;
	            	});
            	}
   				
            },  
            error: function (data, status, e) { 
            	PJ.error("上传时出现异常！");
            }  
        });  
	});		
	
	$("#toolbar").ligerToolBar({
		items : [	{
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe12";
							PJ.topdialog(iframeId, '销售-新增竞争对手', '<%=path%>/sales/clientinquiry/addTenderPrice?id='+${id},
									function(item, dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
												url: '<%=path%>/sales/clientinquiry/saveTender?id='+${id},
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
															window.location=window.location;
															dialog.close();
														} else {
															PJ.error(result.message);
															dialog.close();
														}		
												}
											});
										 PJ.grid_reload(grid);
									},function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
						}
					},
					{
						id:'search',
						icon : 'search',
						text : '展开文件上传',
						click: function(){
							$("#uploadBox").toggle(function(){
								var display = $("#uploadBox").css("display");
								if(display=="block"){
									$("#toolbar > div[toolbarid='search'] > span").html("收起文件上传");
									grid.setGridHeight(PJ.getCenterHeight()-182);
								}else{
									$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
									grid.setGridHeight(PJ.getCenterHeight()-142);
								}
							});
						}
					}
				]
	});
	
	
	PJ.ajax({
        url: '<%=path%>/sales/clientinquiry/getCols?id='+${id},
        dataType:'json',
        loading: "正在加载...",
        success: function(result) {
        	var colNames = [ "id", "序号", "件号", "描述","单位","数量","备注" ];
        	var colModel = [ PJ.grid_column("id", {key:true,hidden:true,align:'left'}),
	    					 PJ.grid_column("item", {sortable:true,width:30,align:'left'}),
    						 PJ.grid_column("partNumber", {sortable:true,width:130,align:'left'}),
    						 PJ.grid_column("description", {sortable:true,width:150,align:'left'}),
    						 PJ.grid_column("unit", {sortable:true,width:30,align:'left'}),
    						 PJ.grid_column("amount", {sortable:true,width:30,align:'left'}),
    						 PJ.grid_column("remark", {sortable:true,width:150,align:'left'})];
			for(var index in result.columnDisplayNames){
				colNames.push(result.columnDisplayNames[index]);
				colModel.push(PJ.grid_column(result.columnKeyNames[index], {sortable:false,width:90}));
			}	    			
			//表格
			grid = PJ.grid("list1", {
				rowNum: 10,
				url:'<%=path%>/sales/clientinquiry/competitorData?id='+${id},
				width : PJ.getCenterWidth(),
				height : PJ.getCenterHeight()-182,
				rowList:[10,20],
				//shrinkToFit:false,
				colNames : colNames,
				colModel : colModel

			});
			
        }
	});
	
	//改变窗口大小自适应
	$(window).resize(function() {
		grid.setGridWidth(PJ.getCenterWidth());
		var display = $("#uploadBox").css("display");
		if(display=="block"){
			grid.setGridHeight(PJ.getCenterHeight()-182);
		}else{
			grid.setGridHeight(PJ.getCenterHeight()-142);
		}
	});
	
});

//子页面必须提供表单数据方法
function getFormData(){
	 var data = $("#form").serialize();
	 return data;
}
 
</script>
</head>

<body>
<div id="layout1">
<div position="center" title="竞争对手报价管理">
		<div id="toolbar"></div>
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
		<table id="list1"></table>
		<div id="pager1"></div>
</div>
</div>	  
</body>
</html>