<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>选择供应商</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style> 
table 
  {   border-collapse:   separate;   border-spacing:   10px;   } 
</style> 
<script type="text/javascript">
	$(function(){
		$("#toolbar").ligerToolBar({
			items : [
						{
						    id:'edit',
							icon : 'edit',
							text : '打印库位标签',
							click: function(){
									var clientCode = '${clientCode}';
									var clientOrderElementId = '${clientOrderElementId}';
									var location = $("#location").val();
									var $a = $("<a href='locationiframes?location="+location+"&clientCode=+"+clientCode+"&clientOrderElementId=+"+clientOrderElementId+"'></a>");
			 						$a.printPage();
			 						$a.trigger("click");
			 						if(window.closed){
										top.window.frames[iframeId].onfous();
									}
							}
						}
			        ]
		});
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/importpackage/location?clientId='+${clientId},
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					$("#location2").empty();
					for(var i in obj){
						var $option = $("<option></option>");
						if(""==obj[i].code){
							 $option.val(obj[i].location).text(obj[i].location+" - "+"空闲");
						}
						else{
							 $option.val(obj[i].location).text(obj[i].location+" - "+obj[i].code);
						}
						 $("#location2").append($option); 
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
				
			}
		});
		
		$("#location2").click(function(){
	 		$("#location").val($(this).val());
	 	});
	})
	
	//获取表单数据
	function getFormData(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};

		$input.each(function(index){
			if(!$(this).val()) {
				//PJ.tip("必填数据项没有填写完整");
				//flag = true;
				return;
			}
				
			postData[$(this).attr("name")] = $(this).val();
		});
		return postData;
	}
	
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="选择位置">
	<div id="toolbar"></div>
	<form id="addForm" action="">
	<table>
		<tr>
			<td>位置</td>
			<td><input name="location" id="location" class="text" class="tc" value="${location}"/></td>
			<td>
				<select id="location2" name="location2">
				</select>
			</td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>