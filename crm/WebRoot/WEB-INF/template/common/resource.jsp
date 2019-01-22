<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<%
String path = request.getContextPath();
%>
<!-- 样式 -->
<link href="<%=path%>/resources/js/ligerui/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/resources/js/ligerui/ligerUI/skins/Everygold/css/all.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/resources/js/ligerui/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=path %>/resources/js/jqGrid/css/ui.jqgrid_naswork.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=path %>/resources/js/jqGrid/themes/naswork/jquery-ui-naswork.css" /> 
<link href="<%=path%>/resources/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/resources/css/reset.css" rel="stylesheet" type="text/css" />
<!-- echart -->
<script src="<%=path%>/resources/js/echart/echarts-all.js" type="text/javascript"></script>
<!-- jquery -->
<script src="<%=path%>/resources/js/jquery/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="<%=path%>/resources/js/jquery/ajaxfileupload.js" type="text/javascript"></script>
<!-- ligerui插件包 -->
<script src="<%=path%>/resources/js/ligerui/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
<script src="<%=path%>/resources/js/jquery-barcode.js"></script>
 <script src="<%=path%>/resources/js/jquery.printPage.js"></script>
<!-- jqgrid-->
<script src="<%=path %>/resources/js/jqGrid/js/i18n/grid.locale-cn.js" type="text/javascript"></script> 
<script src="<%=path %>/resources/js/jqGrid/js/jquery.jqGrid.src.js" type="text/javascript"></script>
<%-- <script src="<%=path %>/resources/js/jqGrid/js/grid.subgrid.js" type="text/javascript"></script> --%>
<script src="<%=path %>/resources/js/jqGrid/plugins/jquery.contextmenu.js" type="text/javascript"></script>
<script src="<%=path %>/resources/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<%=path %>/resources/js/jquery.jclock.js" type="text/javascript"></script>
<script src="<%=path %>/resources/js/jqGrid/js/jquery.searchableSelect.js" type="text/javascript"></script>
<link href="<%=path%>/resources/js/jqGrid/css/jquery.searchableSelect.css" rel="stylesheet" type="text/css" />
<!-- bootstrap -->
<%-- <script src="<%=path %>/resources/js/bootstrap/bootstrap-select.js" type="text/javascript"></script>
<script src="<%=path %>/resources/js/bootstrap/bootstrap.js" type="text/javascript"></script>
<script src="<%=path %>/resources/js/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<link href="<%=path%>/resources/css/bootstrap-select.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/resources/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css" /> --%>
<!-- 工具包 -->
<script src="<%=path%>/resources/js/PJ.js" type="text/javascript"></script>
<!-- cookie -->
<script src="<%=path %>/resources/js/ligerui/jquery.cookie.js"></script>
<!-- json工具类 -->
<script src="<%=path %>/resources/js/ligerui/json2.js"></script>
<!-- 表单校验 -->
<script src="<%=path %>/resources/js/jquery.validation/jquery.validate.js"></script>
<script src="<%=path %>/resources/js/jquery.validation/messages_cn.js" type="text/javascript"></script>
<!-- 扩展的检验方法 -->
<script src="<%=path %>/resources/js/jquery.validation/additional-methods.js" type="text/javascript"></script>
<!-- multiselect -->
<script type="text/javascript" src="<%=path %>/resources/js/jquery-ui/js/jquery-ui.custom.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/multiselect/demos/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/multiselect/src/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/multiselect/demos/assets/prettify.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/multiselect/src/jquery.multiselect.filter.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/form-field-tooltip.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/rounded-corners.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/FlexPaper/flexpaper_flash.js"></script>
<script type="text/javascript" src="<%=path %>/resources/js/FlexPaper/flexpaper_flash_debug.js"></script>
<script type="text/javascript">
var basePath = '<%=path%>';
</script>