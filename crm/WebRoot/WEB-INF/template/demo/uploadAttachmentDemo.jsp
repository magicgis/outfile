<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>上传附件</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<jsp:include page="/WEB-INF/template/common/resource.jsp" />
	<script type="text/javascript">
		$(function(){
			$("#viewattach").click(function(){
				PJ.attachmentDialog({
					data:'businessKey=t_testTable.id.2',
					add:true,
					remove:true,
					download:false
				});
			});
		});

		$(function(){
			$("#upload").click(function(){
				var datajson = {businessKey:'t_testTable.id.2'};
				PJ.uploadAttachment(null,datajson,function(result){
					if(result && result.id){
						PJ.success("上传成功", function(){
								afterUpload(result);
						})
					}
				});
			});
		});

		$(function(){
			$("#cusUpload").click(function(){
				var datajson = {businessKey:'t_testTable.id.2'};
				var options ={
						target:"#cusUploadDiv",
						url:"/fj/upload",
						fjName: "#cusFjName",
						fileElementId: "cusUploadFile"
						
				};
				PJ.uploadAttachment(null,datajson,function(result){
					if(result && result.id){
						PJ.success("上传成功", function(){
								afterUpload(result);
						})
					}
				}, options);
			});
		});
		
		//默认回调方法
		function afterUpload(result){
			console.log(result);
		}
		function afterRemove(ids){
			console.log(ids);
		}
	</script>
  </head>
  
  <body style="margin: 20px;">
    <h2>1、只是调用一个button</h2>
    <input type="button" value="上传附件" id="upload"/>
    <input type="button" value="自定义上传附件" id="cusUpload"/>
    <h2>2、通过js调用弹出对话框加载</h2>
    <br/>
    <input type="button" value="查看管理附件" id="viewattach"/>
     <br/> <br/>
     <h2>3、通过js调用在页面上渲染一个iframe</h2>
     <br/>
    <script type="text/javascript">
    	PJ.attachmentFrame({
    		data:'businessKey=t_testTable.id.2',
    		add:true,
			remove:true,
			download:true
    	});
    </script>
   		<!-- 附件上传 -->
   		<div class="table-box" style="display: none;" id="uploadDiv">
   			<form:row>
    			<form:column>
    				<form:left>附件名称：</form:left>
    				<form:right><input type="text" name="fjName" id="fjName" class="input"/></form:right>
    			</form:column>
    		</form:row>
    		<form:row>
    			<form:column>
    				<form:left>附件文件：</form:left>
    				<form:right><input type="file" name="file" id="uploadFile"/></form:right>
    			</form:column>
    		</form:row>
    	</form>
   		</div>
   		<div class="table-box" style="display: none;" id="cusUploadDiv">
   			<form:row>
    			<form:column>
    				<form:left>附件名称：</form:left>
    				<form:right><input type="text" name="cusFjName" id="cusFjName" class="input"/></form:right>
    			</form:column>
    		</form:row>
    		<form:row>
    			<form:column>
    				<form:left>附件文件：</form:left>
    				<form:right><input type="file" name="file" id="cusUploadFile"/></form:right>
    			</form:column>
    		</form:row>
    	</form>
   		</div>

  </body>
</html>
