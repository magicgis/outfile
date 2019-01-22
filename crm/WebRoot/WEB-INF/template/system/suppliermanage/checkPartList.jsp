<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>客户询价明细管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#submit").click(function(){
			var id =$("#id").val();
			var url = '<%=path%>/sales/clientinquiry/uploadExcel?id='+getFormData().id+'&editType='+ getFormData().toString();
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
	            	var da = eval(data)[0];
	            	if(da.flag==true){
	            		PJ.topdialog(iframeId, '能力清单', '<%=path%>/sales/clientinquiry/toAddElement?id='+${id },
							 function(item,dialog){
							 var postData=top.window.frames[iframeId].getFormData();	 							
							 $.ajax({
									url: '<%=path%>/sales/clientinquiry/addElement?id='+${id },
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
					   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"确认")
	            	}
	            	else{
	            		PJ.error(da.message);
	            	}
	   				
	            },  
	            error: function (data, status, e) { 
	            	PJ.error("上传异常！");
	            }  
	        });
			
		 });
		
		
		$("#toolbar").ligerToolBar({
			items : [<%-- {
						id:'add',
						icon : 'add',
						text : '批量新增',
						click: function(){
								var iframeId="ideaIframe12";
								PJ.topdialog(iframeId, '新增客户询价明细', '<%=path%>/sales/clientinquiry/addelement?id='+${id },
										function(item,dialog){
											var data = top.window.frames[iframeId].getSubmit();
											if(data.flag==true){
								            	PJ.success(data.message);
								        	}
											if(data.flag==false){
								        		PJ.error(data.message);
								        	}
											if(data.flag=='error'){
								        		PJ.error("上传异常！");
								        	}
											dialog.close();
											PJ.grid_reload(grid);
										},function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"上传");
						}
					 
					 }, --%>
					 {
						id:'add',
						icon : 'add',
						text : '新增',
						click: function(){
							var iframeId="ideaIframe5";
							PJ.topdialog(iframeId, '新增客户询价明细', '<%=path%>/sales/clientinquiry/toAddElement?id='+${id },
									function(item,dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
												url: '<%=path%>/sales/clientinquiry/addElement?id='+${id },
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
									PJ.grid_reload(grid);}
								   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
						}
					 },
					 {
							id:'search',
							icon : 'search',
							text : '收起excel上传',
							click: function(){
								$("#uploadBox").toggle(function(){
									var display = $("#uploadBox").css("display");
									if(display=="block"){
										$("#toolbar > div[toolbarid='search'] > span").html("收起excel上传");
										grid.setGridHeight(PJ.getCenterHeight()-182);
									}else{
										$("#toolbar > div[toolbarid='search'] > span").html("展开excel上传");
										grid.setGridHeight(PJ.getCenterHeight()-142);
									}
								});
							}
					}
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/sales/clientinquiry/elementData?id='+${id },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-182,
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/sales/clientinquiry/editElement',
			sortname : "ci.inquiry_date",
			colNames :["id","序号","件号","CSN","另件号","描述","单位","数量","备注","更新时间","是否主件号","主件号"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("item", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("csn", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("alterPartNumber", {sortable:true,width:170,editable:true,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:200,editable:true,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("amount",{sortable:true,width:60,editable:true,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:240,editable:true,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:170,align:'left'}),
			           PJ.grid_column("isMain",{sortable:true,width:90,align:'left',
			        	   formatter: function(value){
								if (value==2) {
									return "是";
								} else if(value==1){
									return "否";							
								}else {
									return "";
								}
							}   
			           }),
			           PJ.grid_column("mainPartNumber",{sortable:true,width:170,align:'left'})
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
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm1").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//机型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/airType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code+"-"+obj[i].value);
						$("#searchForm3").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//商业类型
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/sales/clientinquiry/bizType',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].code).text(obj[i].code);
						$("#searchForm4").append($option);
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
		    	PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/sales/clientinquiry/listData');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
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
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="询价单号   ${quoteNumber } ">
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