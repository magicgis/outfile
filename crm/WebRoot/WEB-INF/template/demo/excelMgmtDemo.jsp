<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>Excel文件管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript">
		$(function(){
			$("#viewexcels").click(function(){
				//根据具体业务提供相关的title
				var title = '例子excel上传';
				//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
				//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
				var businessKey = 'SampleTableName.id.3.SampleExcel'
				PJ.excelDiaglog({
					data:'businessKey='+businessKey,
					title: title,
					add:true,
					remove:true,
					download:true
				});
			});
			$("#viewdynamic").click(function(){
				//根据具体业务提供相关的title
				var title = '动态列';
				var dynamicColCount = $("#dynamicColCount")[0].value;
				//根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
				//其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
				var businessKey = 'SampleTableName.id.3.SampleExcel.dynamic.'+dynamicColCount;
				PJ.excelDiaglog({
					data:'businessKey='+businessKey,
					title: title,
					add:true,
					remove:true,
					download:true
				});
			});
		});


	</script>
  </head>
  
  <body style="margin: 20px;">
    <h2>1、通过js调用弹出对话框加载</h2>
    <br/>
    <input type="button" value="查看管理附件" id="viewexcels"/>
     <br/> <br/>
    <h2>2、动态列</h2>
    <br/>
    动态列长度<input type="text" id="dynamicColCount"/>
    <input type="button" value="动态列" id="viewdynamic"/>
     <br/> <br/>

  </body>
</html>
