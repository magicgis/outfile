<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增客户询价</title>

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
		
		$("#searchForm3").keydown(function() {
           	setTimeout(validatea,1000);
        });
	});
	
	function test(){
		var test = $("#test").val();
		var index = parseInt(test.indexOf("）"));
		test = test.substring(4,index);
		alert(test);
	}
	
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
	
	//-- 验证
	function validatea(){
		var number = $("#searchForm3").val();
		var index = parseInt(number.indexOf(")"));
		if(index > 0){
			number = number.substring(3,index);
		}
		var start = number.substring(0,3);
		if(start=="040"){
			var checkId = number.substring(18);
			var iframeId = "listByLocation";
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/importpackage/getImportList?locationId='+checkId,
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var amount=obj[i].amount;
							var soeid=obj[i].supplierOrderElementId;
							var id=obj[i].importPackageId;
							var inspectionDate=obj[i].inspectionDateString;
							var manufactureDate=obj[i].manufactureDateString;
							var printPartNumber=obj[i].partNumber;
							var printDescription=obj[i].description;
							var resume=obj[i].completeComplianceCertificate;
							var complianceCertificate=obj[i].complianceCertificate;
							var serialNumber=obj[i].serialNumber;
							var location=obj[i].location;
							var $a = $("<a href='partiframes?idate="+inspectionDate+"&id="+soeid+"&sl="+location+"&resume="+resume
										+"&complianceCertificate="+complianceCertificate
										+"&amount="+amount+"&ipid="+id+"&mdate="+manufactureDate+"&sn="+encodeURIComponent(serialNumber)+
										"&pdesc="+encodeURIComponent(printDescription)+"&ppart="+encodeURIComponent(printPartNumber)+"'></a>");
							$a.printPage();
		 					$a.trigger("click");
		 					if(window.closed){
								top.window.frames[iframeId].onfous();
							}
						}
					}else{
						PJ.warn(result.message);
					}
				}
		});
		}else if (start=="010"){
			var checkId = number.substring(18);
			var searchString = "isll.id = "+checkId;
			//PJ.grid_search(grid2,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId }+'&searchString='+searchString+'&id='+${id });
			
		}
		$("#searchForm3").val("");
	}
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,select");var nodes = def.nodeName.split(",");var flag = true;var tip;
		$items.each(function(i){
			var name = $(this).attr("name");
			if(!name)return;
			for(var k in nodes){
				if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
				if(!flag){
	                $(this).addClass("input-error");
					tip = $(this).attr("data-tip");
					return false;
				}else $(this).removeClass("input-error");
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
</script>
</head>
	
<body style="line-height:10px;">
<div position="center" title="位置更改">
	<form id="addForm" action="">
	<table>
		<tr>
			<td>需要重新打印件号标签的位置条码<br /><br /><span style="color:red"></span></td>
			<td><textarea rows="10" cols="90" name="inputList" id="searchForm3"></textarea></td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>