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
			$("#toolbar").ligerToolBar({
				items : [
						   {  icon : "add",
							  text : "增加行",
							  click : function(){
									  	var $tbody = $("#elementTable>tbody");//-- 容器
										var $item = $($("script[type='tetx/template']").html()).clone();//-- 复制模版
										$tbody.append($item);
									}
							},
							{ icon : "delete",
							  text : "删除行",
							  click : function(){
										var $checkedItems = $("#elementTable>tbody>tr").find("input:checkbox");
										$checkedItems.each(function(i){
									    if($(this).prop("checked")){
												$(this).parent().parent().remove();
											} 
										});
												
								}
							}
				        ]
			});
		
		//状态信息
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/system/staticprice/findConditionForSupplier',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $items = $("#elementTable>tbody").find("tr");
						$items.each(function(){
							var $inputs = $(this).find("select");
							$inputs.each(function(){
								if($(this).attr("name") == "conditionId"){
									var $option = $("<option></option>");
									$option.val(obj[i].id).text(obj[i].code);
									$(this).append($option);
								}
							});
						});
						
					}
				}else{
					
						PJ.warn("操作失误!");
				}
			}
		});
		
	});
	
	/* function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input,textarea,select");
			$inputs.each(function(){
				obj["list["+index+"]."+$(this).attr("name")] = $(this).val();
			});
			index++;
		});
		
		return obj;
	} */
	
	/* function getTableDate(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		var $heads = $($items[0]).find("input,textarea,select");
		for(var i=1;i<$items.length;i++){
			var $inputs = $($items[i]).find("input,textarea,select");
			for(var j=0;j<$inputs.length;j++){
				obj["list["+index+"]."+$($heads[j]).val()] = $($inputs[j]).val();
			};
			index++;
		};
		
		return obj;
	} */
	
	function getTableDate(){
		var obj = new Array();
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		var $heads = $($items[0]).find("input,textarea,select");
		for(var i=1;i<$items.length;i++){
			var dirt = {}
			var $inputs = $($items[i]).find("input,textarea,select");
			for(var j=0;j<$inputs.length;j++){
				dirt[$($heads[j]).val()] = $($inputs[j]).val()
				//obj[index]+"["+$($heads[j]).val()+"]" = $($inputs[j]).val();
			};
			obj[index] = dirt;
			index++;
		};
		
		return obj;
	}
	
	function getFormData(){
		var postData = getTableDate();
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
	
	$(document).ready(function() {
        $("input").bind({
            copy : function(){
                $('#message').text('copy behaviour detected!');
            },
            paste : function(e){
           		var $tbody = $("#elementTable>tbody");
           		var   obj=this.parentNode.parentNode.parentNode;
                var clipboardData = e.originalEvent.clipboardData;
                e.originalEvent.clipboardData.clear;
                e.originalEvent.clipboardData.getData('');
                var webData=new  Array(clipboardData.getData('text').split('\n').length-1);
                for(var i=0;i<clipboardData.getData('text').split('\n').length-1;i++){
		            webData[i]=clipboardData.getData('text').split('\n')[i].split('\t')
				}
 				var tb = $("#elementTable");
		        for(var j=0;j<webData.length;j++){
			       	var $tr = $("<tr></tr>");
			       	var $checkbox = $("<td><input type=\"checkbox\" class=\"form-checkbox\" name=\"dataItem\" /></td>");
			        for(var n=0;n<webData[j].length;n++){
				              /*if(obj.rows[this.parentNode.parentNode.rowIndex+j].cells[this.parentNode.cellIndex+n]!=null)
				              {
				              	obj.rows[this.parentNode.parentNode.rowIndex+j].cells[this.parentNode.cellIndex+n].childNodes[0].value=webData[j][n];
				              }else{
				              		
				              }*/
		   				var $td = $("<td><input  type=\"text\" style=\"width:100%;\" value=\""+webData[j][n]+"\" size=\"10\"></td>");
		   				$tr.append($td);
		        	}
			        tb.append($tr);
	        	}
            },
            cut : function(){
                $('#message').text('cut behaviour detected!');
            }
        });
        $("#ipt-test").keyup(function(){
            console.log("aaaaaaaaaaaaaaaaaa");
        });
    });
	
</script>

</head>

<body>
	<div id="toolbar"></div>
	<div id="tablediv">
		<div>
			输入源：<input  type="text" style="width:100%;" value="0_0" >
		</div>
		<div style="margin-top: 10px">
			table:<br />
			<table id="elementTable" border="1px" >
			<input type="text" name="token" id="token" value="${token}" class="hide"/>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
</body>

</html>