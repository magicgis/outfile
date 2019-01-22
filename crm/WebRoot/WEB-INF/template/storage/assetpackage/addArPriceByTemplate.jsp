<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>根据模板上传AR价格</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style type="text/css">

table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#FFFFFF;
}
.evenrowcolor{
	background-color:#E0EEEE;
}
</style>
<script type="text/javascript">
	var layout, grid;
	var check = 0;
	$(function() {
		//excel上传
		$("#submit").click(function(){
			var id =$("#id").val();
			<%--var url = '<%=path%>/storage/assetpackage/uploadExcel?id='+getExcelFormData().id+'&editType='+ getExcelFormData().toString();--%>
            var url = '<%=path%>/storage/assetpackage/uploadArPriceExcel';
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
		            	PJ.success("上传成功,已新增"+da.message+"条数据！");
		            	// PJ.grid_reload(grid);
		            	// PJ.grid_reload(grid2);
		            	// $("#uploadBox").toggle(function(){
						// 	$("#toolbar2 > div[toolbarid='search'] > span").html("展开excel上传");
						// 	grid2.setGridHeight(PJ.getCenterHeight()-260);
		            	// });
	            	}else{
	            		PJ.grid_reload(grid);
	            		// var message = da.message.split(",");
	            		// if(message[0] == "condition"){
	            		// 	PJ.error("第"+message[1]+"行状态不存在！")
	            		// }else if(message[0] == "certification"){
	            		// 	PJ.error("第"+message[1]+"行证书不存在！")
	            		// }else if(message[0] == "all"){
	            		// 	PJ.error("第"+message[1]+"行出现异常！")
	            		// }else{
	            		// 	PJ.error("生成供应商报价异常！");
	            		// }
	            	}
	            },  
	            error: function (data, status, e) { 
	            	var da = eval(eval(data))[0];
	            	// PJ.grid_reload(grid);
	        		// var message = da.message.split(",");
	        		// if(message[0] == "condition"){
	        		// 	PJ.error("第"+message[1]+"行状态不存在！")
	        		// }else if(message[0] == "certification"){
	        		// 	PJ.error("第"+message[1]+"行证书不存在！")
	        		// }else{
	        		// 	PJ.error("第"+message[1]+"行出现异常！")
	        		// }
	            }  
	        });
			
		 });
	});
	
	function getExcelFormData(){
		 var postData = {};
		 postData.data = $("#form").serialize();
		 postData.id = $("#id").val();
		 return postData;
	};
	
	function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
			var $isnull= $(this).find("input[class='isnull']");
			$isnull.each(function(){
				if($(this).val()!=""){
					$inputs.each(function(){
						obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
					});
					index++;
				}
			});
		});
		
		return obj;
	}
	
	function getFormData(){
		var postData = getTableDate();
		postData.check = check
		postData.token='${token}';
		check = 1;
		return postData;
	}
	
	function altRows(id){
		if(document.getElementsByTagName){  
			
			var table = document.getElementById(id);  
			var rows = table.getElementsByTagName("tr"); 
			 
			for(i = 0; i < rows.length; i++){          
				if(i % 2 == 0){
					rows[i].className = "evenrowcolor";
				}else{
					rows[i].className = "oddrowcolor";
				}      
			}
		}
	}

	window.onload=function(){
		altRows('elementTable');
	}
</script>

</head>

<body>

   <div id="text" style="display: block;height: 50px;line-height: 50px;color: red">
	   注：先选择文件之后再进行上传，点击新增无效
   </div>
	<div id="uploadBox" style="display: block;">
		<form id="form" name="form">
		 	<input type="hidden" name="id" id="id" value="${id}"/>
				<form:row columnNum="2">
					<form:column width="200">
						<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
						<form:right>
							<p>
								<input type="file" value="" id="file" name="file"/>
							</p>
							<p style="margin-top: 10px;">
								<input type="button" id="submit" value="上传"/>
							</p>
						</form:right>
					</form:column>
				</form:row>            
		 </form>
	</div>

</body>

</html>