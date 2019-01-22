<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增竞争对手管理 </title>

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
	//数据集合
	var postData = {};
	$(function() {
		$("#toolbar").ligerToolBar({
			items : [
					   {  icon : "add",
						  text : "增加列",
						  click : function(){
								  	var $tr1 = $("#elementTable>thead>tr");//-- 容器
									var $th = $($("script[type='tetx/template1']").html()).clone();//-- 复制模版
									$tr1.append($th);
									
									var $tr2 = $("#elementTable>tbody>tr");//-- 容器
									var $td1 = $($("script[type='tetx/template2']").html()).clone();//-- 复制模版
									$tr2.append($td1);
									
									var $tr3 = $("#elementTable>tfoot>tr");//-- 容器
									var $td2 = $($("script[type='tetx/template3']").html()).clone();//-- 复制模版
									$tr3.append($td2);
								}
						}
			        ]
		});
		
	});
	
	/* function getTableDate1(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>thead").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input[type='text']");
			$inputs.each(function(){
				obj["competitorList["+index+"]."+$(this).attr("name")] = $(this).val();
			});
			index++;
		});
		
		return obj;
	}
	
	function getTableDate2(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input[type='text']");
			$inputs.each(function(){
				obj["competitorQuoteElementList["+index+"]."+$(this).attr("name")] = $(this).val();
			});
			index++;
		});
		
		return obj;
	}
	 */
	 function getTableDate1(){
			var obj = {};
			var index = 1;
			var $items = $("#elementTable>thead").find("tr");
			$items.each(function(){
				var $inputs = $(this).find("input[type='text']");
				$inputs.each(function(){
					postData["competitorList["+index+"]."+$(this).attr("name")] = $(this).val();
					index++;
				});
				
			});
			
	}
	 
	 function getTableDate2(){
			var obj = {};
			var index = 0;
			var $items = $("#elementTable>tbody").find("tr");
			$items.each(function(){
				var $inputs = $(this).find("input[type='text']");
				$inputs.each(function(){
					postData["competitorQuoteElementList["+index+"]."+$(this).attr("name")] = $(this).val();
					index++;
				});
				
			});
			
	}
	 
	 function getTableDate3(){
			var obj = {};
			var index = 0;
			var $items = $("#elementTable>tfoot").find("tr");
			$items.each(function(){
				var $inputs = $(this).find("input[type='text']");
				$inputs.each(function(){
					postData["competitorQuoteList["+index+"]."+$(this).attr("name")] = $(this).val();
					index++;
				});
				
			});
			
	}
	/* function getTableDate1(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>thead").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input[type='text']");
			var list = {};
			var i = 0;
			$inputs.each(function(){
				list["list["+i+"]."+$(this).attr("name")] = $(this).val();
				i++;
			});
			postData["competitorList["+index+"]"] = list;
			index++;
		});
		
		return obj;
	}
	
	function getTableDate2(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tbody").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input[type='text']");
			var list = {};
			var i = 0;
			$inputs.each(function(){
				list["list["+i+"]."+$(this).attr("name")] = $(this).val();
				i++;
			});
			postData["competitorQuoteElementList["+index+"]"] = list;
			index++;
		});
		
		return obj;
	}
	
	function getTableDate3(){
		var obj = {};
		var index = 0;
		var $items = $("#elementTable>tfoot").find("tr");
		$items.each(function(){
			var $inputs = $(this).find("input[type='text']");
			var list = {};
			var i = 0;
			$inputs.each(function(){
				list["list["+i+"]."+$(this).attr("name")] = $(this).val();
				i++;
			});
			postData["competitorQuoteList["+index+"]"] = list;
			index++;
		});
		
		return obj;
	} */
	
	function getFormData(){
		
		/* postData.competitorList = getTableDate1();
		postData.competitorQuoteElementList = getTableDate2();
		postData.competitorQuoteList = getTableDate3(); */
		getTableDate1();
		getTableDate2();
		getTableDate3();
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
	<div id="toolbar"></div>
	<div>
	<table id="elementTable" class="altrowstable">
		<thead>
			<tr>
				<th>序号</th>
				<th>件号</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="l" varStatus="list">
			 	<tr>
			 		<td>
			 		${l.item}
			 		<input value="${l.id}" type="text" name="clientInquiryElementId" class="hide"/>
			 		</td>
			 		<td>
			 		${l.partNumber}
			 		</td>
			 		
			 	</tr>
			 </c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<th>FREIGHT</th>
				<td></td>
			</tr>
		</tfoot>
	</table>
	</div>
	<script id="table-add1" type="tetx/template1" >
			<th><input type="text" name="code" /></th>
	</script>
	
	<script id="table-add2" type="tetx/template2" >
			<td><input type="text" name="price" /></td>
	</script>
	
	<script id="table-add3" type="tetx/template3" >
			<td><input type="text" name="freight" /></td>
	</script>
</body>

</html>