<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>采购-供应商订单管理</title>

    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <jsp:include page="/WEB-INF/template/common/resource.jsp"/>
    <script type="text/javascript">
        var layout, grid;
        $(function () {
            layout = $("#layout1").ligerLayout();

            $("#toolbar").ligerToolBar({
                items: [
	                    {
	                        id: 'search',
	                        icon: 'search',
	                        text: '展开搜索',
	                        click: function () {
	                            $("#searchdiv").toggle(function () {
	                                var display = $("#searchdiv").css("display");
	                                if (display == "block") {
	                                    $("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
	                                    grid.setGridHeight(PJ.getCenterHeight() - 192);
	                                } else {
	                                    $("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
	                                    grid.setGridHeight(PJ.getCenterHeight() - 142);
	                                }
	                            });
	                        }
	                    },
	                    {
	                        id: 'view',
	                        icon: 'view',
	                        text: '已读',
	                        click: function () {
	                        	var ids =  PJ.grid_getMutlSelectedRowkeyNotValidate(grid);
	                        	var editIds = "";
	                			if (ids != ""){
	                				editIds = ids.join(",");
	                			}
                                PJ.ajax({
                                    url: '<%=path%>/purchase/deploymentSendBack/changeReadStatus?ids=' + editIds,
                                    type: "POST",
                                    loading: "正在处理...",
                                    success: function (result) {
                                        if (result.success) {
                                            PJ.success(result.message);
                                            PJ.grid_reload(grid);
                                        } else {
                                            PJ.error(result.message);
                                        }
                                    }
                                });
	                        }
	                                    
	                    }
                	   ]
            });

            grid = PJ.grid("list1", {
                rowNum: 20,
                url: '<%=path%>/purchase/deploymentSendBack/listData',
                width: PJ.getCenterWidth(),
                height: PJ.getCenterHeight() - 142,
                autoSrcoll: true,
                shrinkToFit: true,
                sortname: "sbfm.update_timestamp",
                sortorder: "desc",
                multiselect: true,
                colNames: ["id", "件号", "订单", "退回节点", "更新时间"],
                colModel: [
						    PJ.grid_column("id", {key: true, hidden: true}),
						    PJ.grid_column("partNumber", {sortable: true, width: 40, align: 'left'}),
						    PJ.grid_column("orderNumber", {sortable: true, width: 40, align: 'left'}),
						    PJ.grid_column("description", {sortable: true, width: 80, align: 'left'}),
						    PJ.grid_column("updateTimestamp", {sortable: true, width: 130, align: 'left'})
						  ] 
            });

            // 绑定键盘按下事件
            $(document).keypress(function (e) {
                // 回车键事件
                if (e.which == 13) {
                    var partNumber = $("#partNumber").val()
                    PJ.grid_search(grid, '<%=path%>/purchase/deploymentSendBack/listData');
                }
            });

            //搜索
            $("#searchBtn").click(function () {
                var partNumber = $("#partNumber").val()
                PJ.grid_search(grid, '<%=path%>/purchase/deploymentSendBack/listData');
            });

            //重置条件
            $("#resetBtn").click(function () {
                $("#searchForm")[0].reset();
            });

            $("#partNumber").blur(function () {
                var text = $("#partNumber").val();
                text = trim(text);
                $("#partNumber").val(text.replace(" ", ""));
            });


            //改变窗口大小自适应
            $(window).resize(function () {
                grid.setGridWidth(PJ.getCenterWidth());
                var display = $("#searchdiv").css("display");
                if (display == "block") {
                    grid.setGridHeight(PJ.getCenterHeight() - 242);
                } else {
                    grid.setGridHeight(PJ.getCenterHeight() - 142);
                }
            });

            $("#searchForm2").blur(function () {
                var text = $("#searchForm2").val();
                $("#searchForm2").val(trim(text));
            });

            function getData(options) {
                var a = options;
            }

        });

        function trim(str) {
            return $.trim(str);
        }

        function getData(options) {
            var a = options;
        }

    </script>
    <style>
        /* .ui-jqgrid tr.jqgrow td {
          white-space: normal !important;
          height:auto;
          line-height:20px;
          vertical-align:text-bottom;
          padding-top:10px;
         } */
        /*  th.ui-th-column div
        {
            white-space:normal !important;
            height:auto !important;
            padding:0px;
        } */
    </style>
</head>
<style>
    #cb_list1 {
        margin: 0
    }
</style>
<body>
<div id="layout1">
    <div position="center" title="">
        <div id="toolbar"></div>
        <div id="searchdiv" style="width: 100%;height: 50px;display: none;">
            <form id="searchForm">
                <div class="search-box">
                    <form:row columnNum="5">
                        <form:column>
                            <form:left><p>件号：</p></form:left>
                            <form:right><p><input id="partNumber" class="text" type="text" alias="cie.PART_NUMBER" oper="eq"/></p></form:right>
                        </form:column>
                        <form:column>
                            <form:left><p>订单：</p></form:left>
                            <form:right><p><input id="partNumber" class="text" type="text" alias="cwo.ORDER_NUMBER" oper="eq"/></p></form:right>
                        </form:column>
                        <form:column>
                            <p style="padding-left:30px;">
                                <input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
                                <input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
                            </p>
                        </form:column>
                    </form:row>
                </div>
            </form>
        </div>
        <table id="list1"></table>
        <div id="pager1"></div>
    </div>
</div>
</body>
</html>