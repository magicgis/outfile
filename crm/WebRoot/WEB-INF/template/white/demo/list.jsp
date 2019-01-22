<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>white测试页面</title>

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
                        id: 'add',
                        icon: 'add',
                        text: '修改',
                        click: function () {
                            var ret = PJ.grid_getSelectedData(grid);
                            var id = ret["id"];
                            var iframeId = "ideaIframe2";
                            PJ.topdialog(iframeId, '仓储-修改客户箱单', '<%=path%>/storage/clientship/toEdit?id=' + id,
                                function (item, dialog) {
                                    var postData = top.window.frames[iframeId].getFormData();
                                    $.ajax({
                                        url: '<%=path%>/storage/clientship/saveEdit',
                                        data: postData,
                                        type: "POST",
                                        loading: "正在处理...",
                                        success: function (result) {
                                            if (result.success) {
                                                PJ.success(result.message);
                                                PJ.grid_reload(grid);
                                                dialog.close();
                                            } else {
                                                PJ.error(result.message);
                                                dialog.close();
                                            }
                                        }
                                    });
                                }, function (item, dialog) {
                                    dialog.close();
                                }, 1000, 500, true, "修改");

                        }
                    },
                    {
                        id: 'add',
                        icon: 'add',
                        text: '下载',
                        click: function () {
                            var ret = PJ.grid_getSelectedData(grid);
                            var id = ret["id"];
                            var clientId = ret["clientId"];
                            //根据具体业务提供相关的title
                            var title = 'excel管理';
                            //根据具体业务提供如下格式的businessKey：<关联表表名>.<关联表主键列名>.<关联表主键值>.<Excel生成器key>
                            //其中"Excel生成器key"请在对应的ExcelGeneratorMap实体类中找fetchMappingKey方法的实现
                            var businessKey = 'client_ship.id.' + id + '.ClientShipExcel.' + clientId;
                            PJ.excelDiaglog({
                                data: 'businessKey=' + businessKey,
                                title: title,
                                add: true,
                                remove: true,
                                download: true
                            });
                        }
                    },
                    {
                        id: 'down',
                        icon: 'down',
                        text: '批量下载',
                        click: function () {

                            PJ.showLoading("处理中...");
                            var ids = PJ.grid_getMutlSelectedRowkey(grid);
                            for (var i = 0, len = ids.length; i < len; i++) {
                                (function (a) {
                                    setTimeout(function () {
                                        var id = ids[a];
                                        var data = grid.jqGrid("getRowData", id);
                                        var clientId = data.clientId;
                                        var businessKey = 'client_ship.id.' + id + '.ClientShipExcel.' + clientId;
                                        var data = 'businessKey=' + businessKey;
                                        PJ.ajax({
                                            url: '<%=path%>/excelfile/generate?businessKey=' + data,
                                            beforeSend: function () {
                                                PJ.loading = false;
                                            },
                                            complete: function () {
                                                PJ.loading = false;
                                            }
                                        });
                                    }, i * 2000);
                                })(i)
                            }
                            setTimeout(function () {
                                PJ.hideLoading();
                                var businessKey = 'businessKey=' + ids + '.client_ship.id';
                                var iframeId = "downIframe";
                                PJ.topdialog(iframeId, '客户箱单批量下载', '<%=path%>/excelfile/list?data=' + businessKey + '&batch=' + 'yes',
                                    undefined, function (item, dialog) {
                                        dialog.close();
                                        PJ.grid_reload(grid);
                                    }, 1000, 500, true, "修改");
                            }, len * 2000 + 1000);


                        }
                    },
                    {
                        id: 'search',
                        icon: 'search',
                        text: '展开搜索',
                        click: function () {
                            $("#searchdiv").toggle(function () {
                                var display = $("#searchdiv").css("display");
                                if (display == "block") {
                                    $("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
                                    grid.setGridHeight(PJ.getCenterHeight() - 200);
                                } else {
                                    $("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
                                    grid.setGridHeight(PJ.getCenterHeight() - 112);
                                }
                            });
                        }
                    }
                ]
            });

            grid = PJ.grid("list1", {
                rowNum: 20,
                url: '<%=path%>/white/demo/getList',
                width: PJ.getCenterWidth(),
                height: PJ.getCenterHeight() - 102,
                autoSrcoll: true,
                shrinkToFit: false,
                multiselect: true,
                sortname: "t.recorddatetime",
                sortorder: "desc",
                colNames: ["id", "景点", "接待人数", "级别", "更新时间", "环比"],
                colModel: [PJ.grid_column("id", {key: true, hidden: true}),
                    PJ.grid_column("spotname", {sortable: true, width: 150, align: 'left'}),
                    PJ.grid_column("subscribercount", {sortable: true, width: 100, align: 'left'}),
                    PJ.grid_column("level", {sortable: true, width: 80, align: 'left'}),
                    PJ.grid_column("recorddatetime", {sortable: true, width: 130, align: 'left'}),
                    PJ.grid_column("trend", {sortable: true, width: 110, align: 'left'}),
                ]
            });

            //景点名称来源
            $.ajax({
                type: "POST",
                dataType: "json",
                url: '<%=path%>/white/demo/spotnameList',
                success: function (result) {
                    var obj = eval(result.message)[0];
                    if (result.success) {
                        for (var i in obj) {
                            var $option = $("<option></option>");
                            $option.val(obj[i].spotname).text(obj[i].spotname);
                            $("#searchForm1").append($option);
                        }
                    } else {
                        PJ.showWarn(result.msg);
                    }
                }
            });

            // 绑定键盘按下事件
            $(document).keypress(function (e) {
                // 回车键事件
                if (e.which == 13) {
                    PJ.grid_search(grid, '<%=path%>/storage/clientship/ClientShip');
                }
            });

            //搜索
            $("#searchBtn").click(function () {
                PJ.grid_search(grid, '<%=path%>/storage/clientship/ClientShip');

            });

            //重置条件
            $("#resetBtn").click(function () {
                $("#searchForm")[0].reset();
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

        });

        function trim(str) {
            return $.trim(str);
        }
    </script>

    <style>
        #cb_list1 {
            margin: 0
        }
    </style>

</head>

<body>
<div id="layout1">
    <div position="center">
        <div id="toolbar"></div>
        <div id="searchdiv" style="width: 100%;height: 80px;display: none;">
            <form id="searchForm">
                <div class="search-box">
                    <form:row columnNum="5">
                        <form:column>
                            <form:left><p>景点名称：</p></form:left>
                            <form:right>
                                <select id="searchForm1" name="spotname" alias="t.spotname" oper="eq">
                                    <option value="">全部</option>
                                </select>
                            </form:right>
                        </form:column>
                        <form:column>
                            <form:left><p></p></form:left>
                            <form:right><p>
                            </p></form:right>
                        </form:column>
                        <form:column>
                            <form:left></form:left>
                            <form:right>
                                <p>

                                </p>
                            </form:right>
                        </form:column>
                        <form:column>
                            <form:left><p></p></form:left>
                            <form:right><p></p></form:right>
                        </form:column>
                        <form:column>
                            <form:left><p></p></form:left>
                            <form:right><p></p></form:right>
                        </form:column>
                    </form:row>
                    <form:row columnNum="5">
                        <form:column>
                            <form:left></form:left>
                            <form:right><p></p></form:right>
                        </form:column>
                        <form:column>
                            <form:left></form:left>
                            <form:right><p></p></form:right>
                        </form:column>
                        <form:column>
                            <form:left></form:left>
                            <form:right><p></p></form:right>
                        </form:column>
                        <form:column>
                            <form:left></form:left>
                            <form:right><p></p></form:right>
                        </form:column>
                        <form:column>
                            <form:right>
                                <p style="padding-left:10px;">
                                    <input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
                                    <input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
                                </p>
                            </form:right>
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