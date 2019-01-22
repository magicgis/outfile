<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%-- <%  
	String path = request.getContextPath();
%>   --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style type="text/css" media="screen">   
            html, body  { height:100%; }  
            body { margin:0; padding:0; overflow:auto; }     
            #flashContent { display:none; }  
        </style>   
  
<title>文档在线预览系统</title>  
</head>  
<body>   
        <div style="position:absolute;left:50px;top:10px;">  
            <a id="viewerPlaceHolder" style="width:820px;height:650px;display:block"></a>  
              
            <script type="text/javascript">   
            var swfVersionStr = "10.0.0";  
            var xiSwfUrlStr = "";  //值可以任意  
              
            var flashvars = {   
                  SwfFile : escape("FlexPaperViewer.swf"),   //swf文件路径（lucene4.6和页面同一个位置）     
                  Scale : 0.6,   
                  ZoomTransition : "easeOut",  
                  ZoomTime : 0.5,  
                  ZoomInterval : 0.1,  
                  FitPageOnLoad : false,  
                  FitWidthOnLoad : true,  
                  PrintEnabled : true,  
                  FullScreenAsMaxWindow : false,  
                  ProgressiveLoading : true,  
                    
                  PrintToolsVisible : true,  
                  ViewModeToolsVisible : true,  
                  ZoomToolsVisible : true,  
                  FullScreenVisible : true,  
                  NavToolsVisible : true,  
                  CursorToolsVisible : true,  
                  SearchToolsVisible : true,  
                    
                  localeChain: "en_US"  
                  };  
                    
             var params = {  
                  
                }  
            params.quality = "high";  
            params.bgcolor = "#ffffff";  
            params.allowscriptaccess = "sameDomain";  
            params.allowfullscreen = "true";  
            var attributes = {};  
            attributes.id = "FlexPaperViewer";  
            attributes.name = "FlexPaperViewer";  
            swfobject.embedSWF(  
                "FlexPaperViewer.swf", "flashContent",   
                "800", "600",  
                swfVersionStr, xiSwfUrlStr,   
                flashvars, params, attributes);  
            swfobject.createCSS("#flashContent", "display:block;text-align:center;");  
        </script>              
        </div>  
</body>  
