<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>供应商寄卖</title>

    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <jsp:include page="/WEB-INF/template/common/resource.jsp"/>
    <!-- <script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>  -->
    <style>
        /* .ui-jqgrid tr.jqgrow td {
          white-space: normal !important;
          height:auto;
          vertical-align:text-top;
          padding-top:2px;
         } */
    </style>
    <script type="text/javascript">
        var layout, grid, grid2;
        $(function () {
            //layout = $("#layout1").ligerLayout();
            layout = $("#layout1").ligerLayout({
                centerBottomHeight: 340,
                onEndResize: function (e) {
                    //GridResize();
                }
            });

            $("#toolbar").ligerToolBar({
                items: []
            });

            $("#toolbar2").ligerToolBar({
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
                                    grid.setGridHeight(PJ.getCenterHeight() - 152);
                                } else {
                                    $("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
                                    grid.setGridHeight(PJ.getCenterHeight() - 102);
                                }
                            });
                        }
                    }
                ]
            });

            PJ.ajax({
                url: '<%=path%>/storage/suppliercommissionsale/getCols?id=' + '${id}',
                dataType: 'json',
                loading: "正在加载...",
                success: function (result) {
                    var colNames = ["件号", "日期", "旧件平均价", "新件平均价", "MRO-Reapir","MRO-Overhaul","AR", "SV", "OH", "NE"];
                    var colModel = [PJ.grid_column("partNumber", {sortable: true, width: 130, align: 'left'}),
                        PJ.grid_column("crawlDate", {sortable: true, width: 80, align: 'left', key: true}),
                        PJ.grid_column("oldPrices", {sortable: true, width: 100, align: 'left', key: true}),
                        PJ.grid_column("newPrices", {sortable: true, width: 100, align: 'left', key: true}),
                        // PJ.grid_column("maintenancePrices", {sortable: true, width: 100, align: 'left', key: true}),
                        PJ.grid_column("mroRepair", {sortable: true, width: 100, align: 'left', key: true}),
                        PJ.grid_column("mroOverhual", {sortable: true, width: 100, align: 'left', key: true}),
                        PJ.grid_column("ar", {sortable: true, width: 30, align: 'left'}),
                        PJ.grid_column("sv", {sortable: true, width: 30, align: 'left'}),
                        PJ.grid_column("oh", {sortable: true, width: 30, align: 'left'}),
                        PJ.grid_column("ne", {sortable: true, width: 30, align: 'left'})];
                    for (var index in result.columnDisplayNames) {
                        colNames.push(result.columnDisplayNames[index]);
                        colModel.push(PJ.grid_column(result.columnKeyNames[index], {sortable: false, width: 40}));
                    }
                    //表格
                    grid = PJ.grid("list2", {
                        rowNum: -1,
                        url: '<%=path%>/storage/suppliercommissionsale/countStockMarket?id=' + '${id}',
                        width: PJ.getCenterWidth(),
                        height: PJ.getCenterHeight() - 132,
                        rowList: [10, 20],
                        autoSrcoll: true,
                        shrinkToFit: false,
                        //shrinkToFit:false,
                        colNames: colNames,
                        colModel: colModel

                    });
                    var arr = [];
                    var suppliers = eval(${list})[0];
                    arr[0] = {
                        startColumnName: 'ar',
                        numberOfColumns: 4,
                        titleText: '<div align="center"><span>Total</span></div>'
                    }
                    for (var supplier in suppliers) {
                        //arr[supplier] = "{startColumnName : "+suppliers[supplier]+"_AR"+" ,numberOfColumns : 4,titleText :  '<div align=\"center\"><span>客户询价</span></div>'}";
                        arr[parseInt(supplier) + parseInt(1)] = {
                            startColumnName: suppliers[supplier] + '_AR',
                            numberOfColumns: 4,
                            titleText: '<div align="center"><span>' + suppliers[supplier] + '</span></div>'
                        }
                    }
                    //二级表头
                    $("#list2").jqGrid('setGroupHeaders', {
                        useColSpanStyle: true,  // 没有表头的列是否与表头列位置的空单元格合并
                        groupHeaders: arr
                    });


                }
            });

            function onSelect() {
                var ret = PJ.grid_getSelectedData(grid);
                var id = ret["id"];
                $("#id").val(id);
                PJ.grid_search(grid2, '<%=path%>/storage/suppliercommissionsale/elementList?id=' + id);

            };

            $(window).resize(function () {
                //GridResize();
            });

            /* function GridResize() {
                grid.setGridWidth(PJ.getCenterWidth());
                grid2.setGridWidth(PJ.getCenterBottomWidth());
                grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
                grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom"));
            }
            grid.setGridHeight(PJ.calculateGridHeight(".l-layout-center"));
            grid2.setGridHeight(PJ.calculateGridHeight(".l-layout-centerbottom", "centerbottom")); */

            function statusFormatter(cellvalue, options, rowObject) {
                switch (cellvalue) {
                    case 0:
                        return "无效";
                        break;

                    case 1:
                        return "有效";
                        break;

                    default:
                        return cellvalue;
                        break;
                }
            }

            // 绑定键盘按下事件
            $(document).keypress(function (e) {
                // 回车键事件
                if (e.which == 13) {
                    PJ.grid_search(grid, '<%=path%>/storage/suppliercommissionsale/List');
                }
            });

            //搜索
            $("#searchBtn").click(function () {
                PJ.grid_search(grid, '<%=path%>/storage/suppliercommissionsale/List');

            });

            //供应商代码
            $.ajax({
                type: "POST",
                dataType: "json",
                url: '<%=path%>/storage/suppliercommissionsale/getSupplierList?id=' + '${id}',
                success: function (result) {
                    var obj = eval(result.message)[0];
                    if (result.success) {
                        for (var i in obj) {
                            var $option = $("<option></option>");
                            $option.val(obj[i]).text(obj[i]);
                            $("#suppliers").append($option);
                        }
                    } else {
                        PJ.showWarn(result.msg);
                    }
                }
            });

            //时间
            $.ajax({
                type: "POST",
                dataType: "json",
                url: '<%=path%>/storage/suppliercommissionsale/getTimeList?id=' + '${id}',
                success: function (result) {
                    var obj = eval(result.message)[0];
                    if (result.success) {
                        for (var i in obj) {
                            var $option = $("<option></option>");
                            $option.val(obj[i]).text(obj[i]);
                            $("#times").append($option);
                        }
                        loadPie();
                    } else {
                        PJ.showWarn(result.msg);
                    }
                }
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
                    grid.setGridHeight(PJ.getCenterHeight() - 102);
                } else {
                    grid.setGridHeight(PJ.getCenterHeight() - 102);
                }
            });

            function getFormData() {
                var postData = {};
                postData.data = $("#form").serialize();
                postData.id = $("#id").val();
                return postData;
            };

        });
    </script>
</head>
<style>
    div {
        float: left;
    }
</style>
<body>
<div id="layout1">
    <div position="center">
        <div id="supplier" style="margin-left:550px;margin-top:10px">
            <span>选择供应商：</span>
            <select name="suppliers" id="suppliers" class="text">
                <option>Total</option>
            </select>
            <span style="margin-left:200px">时间：</span>
            <select name="times" id="times" class="text">
            </select>
        </div>
        <div id="box" style="height:370px;width: 850px;padding: 20px"></div>
        <div id="box2" style="height:370px;width: 500px;padding: 20px"></div>
    </div>
    <div position="centerbottom">
        <table id="list2"></table>
        <div id="pager2"></div>

    </div>
</div>
</body>
</html>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById("box"));
    var option = {
        // 标题
        title: {
            text: '件号数量分布',
            subtext: '数据来源：StockMarket'
        },
        tooltip: {
            trigger: 'axis'
        },
        //图例名
        legend: {
            data: ['AR', 'SV', 'OH', 'NE']
        },
        grid: {
            left: '3%',   //图表距边框的距离
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        //工具框，可以选择
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
                }
            },
            {
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ]
    };
    myChart.setOption(option);


    $.ajax({
        type: "POST",
        dataType: "json",
        url: '<%=path%>/storage/suppliercommissionsale/getEchartData?id=' + '${id}',
        success: function (result) {
            var ar = eval(result.message)[0];
            var sv = eval(result.message)[1];
            var oh = eval(result.message)[2];
            var ne = eval(result.message)[3];
            var date = eval(result.message)[4];
            if (result.success) {
                myChart.setOption({
                    //x轴信息样式
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: date,
                        //坐标轴颜色
                        axisLine: {
                            lineStyle: {
                                color: 'red'
                            }
                        },
                        //x轴文字旋转
                        axisLabel: {
                            rotate: 30,
                            interval: 0
                        },
                    },
                    series: [
                        //虚线
                        {
                            name: 'AR',
                            type: 'line',
                            symbolSize: 4,   //拐点圆的大小
                            color: ['red'],  //折线条的颜色
                            data: ar,
                            yAxisIndex: 1,
                            smooth: false,   //关键点，为true是不支持虚线的，实线就用true
                            itemStyle: {
                                normal: {
                                    lineStyle: {
                                        width: 2,
                                        type: 'dotted'  //'dotted'虚线 'solid'实线
                                    }
                                }
                            }
                        },
                        //实线
                        {
                            name: 'SV',
                            type: 'line',
                            symbol: 'circle',
                            symbolSize: 4,
                            itemStyle: {
                                normal: {
                                    color: 'green',
                                    borderColor: 'green'  //拐点边框颜色
                                }
                            },
                            data: sv
                        },
                        {
                            name: 'OH',
                            type: 'line',
                            symbolSize: 4,
                            color: ['yellow'],
                            smooth: false,   //关键点，为true是不支持虚线的，实线就用true
                            itemStyle: {
                                normal: {
                                    lineStyle: {
                                        width: 2,
                                        type: 'dotted'  //'dotted'虚线 'solid'实线
                                    }
                                }
                            },
                            data: oh
                        },
                        {
                            name: 'NE',
                            type: 'line',
                            symbolSize: 4,
                            color: ['blue'],
                            itemStyle: {
                                normal: {
                                    lineStyle: {
                                        width: 2,
                                        type: 'dotted'  //'dotted'虚线 'solid'实线
                                    }
                                }
                            },
                            data: ne
                        }
                    ]
                })
            } else {

            }
        }
    });

    var myecharts = echarts.init(document.getElementById("box2"));
    var option = ({
        //提示框组件,鼠标移动上去显示的提示内容
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"//模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。
        },
        title: {
            text: '比例分析',//主标题文本，支持\n换行
            subtext: '',//副文本
            sublink: 'http://esa.un.org/wpp/Excel-Data/population.htm',//副文本链接
            left: 'center',//离容器左侧的距离
            top: 'top'//距离容器上测的距离
        },
        backgroundColor: '',//背景颜色
        textStyle: {
            color: 'rgba(255, 255, 255, 0.3)'//文字的颜色
        },

    });
    myecharts.setOption(option);

    function loadPie() {
        var time = $("#times").val();
        $.ajax({
            type: "POST",
            dataType: "json",
            url: '<%=path%>/storage/suppliercommissionsale/getPieData?id=' + '${id}' + '&time=' + time,
            success: function (result) {
                var data = eval(result.message)[0];
                myecharts.setOption({
                    series: [
                        {
                            name: '访问来源',
                            type: 'pie',//每个系列，通过type决定自己的系列型号
                            radius: '60%',
                            data: data,
                            roseType: 'angle',

                            itemStyle: {//图形样式 normal，emphasis
                                normal: {
                                    label: {
                                        show: true,
                                        formatter: '{b} : {c} ({d}%)'   //在饼状图上直接显示百分比
                                    },
                                    labelLine: {show: true}
                                },
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }

                            }
                        }
                    ]
                })
            }
        });
    }

    $("#suppliers").change(function () {
        var supplier = $("#suppliers").val();
        var time = $("#times").val();
        $.ajax({
            type: "POST",
            dataType: "json",
            url: '<%=path%>/storage/suppliercommissionsale/getEchartData?id=' + '${id}' + '&supplier=' + supplier,
            success: function (result) {
                var ar = eval(result.message)[0];
                var sv = eval(result.message)[1];
                var oh = eval(result.message)[2];
                var ne = eval(result.message)[3];
                var date = eval(result.message)[4];
                if (result.success) {
                    myChart.setOption({
                        //x轴信息样式
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            data: date,
                            //坐标轴颜色
                            axisLine: {
                                lineStyle: {
                                    color: 'red'
                                }
                            },
                            //x轴文字旋转
                            axisLabel: {
                                rotate: 30,
                                interval: 0
                            },
                        },
                        series: [
                            //虚线
                            {
                                name: 'AR',
                                type: 'line',
                                symbolSize: 4,   //拐点圆的大小
                                color: ['red'],  //折线条的颜色
                                data: ar,
                                smooth: false,   //关键点，为true是不支持虚线的，实线就用true
                                itemStyle: {
                                    normal: {
                                        lineStyle: {
                                            width: 2,
                                            type: 'dotted'  //'dotted'虚线 'solid'实线
                                        }
                                    }
                                }
                            },
                            //实线
                            {
                                name: 'SV',
                                type: 'line',
                                symbol: 'circle',
                                symbolSize: 4,
                                itemStyle: {
                                    normal: {
                                        color: 'green',
                                        borderColor: 'green'  //拐点边框颜色
                                    }
                                },
                                data: sv
                            },
                            {
                                name: 'OH',
                                type: 'line',
                                symbolSize: 4,
                                color: ['yellow'],
                                smooth: false,   //关键点，为true是不支持虚线的，实线就用true
                                itemStyle: {
                                    normal: {
                                        lineStyle: {
                                            width: 2,
                                            type: 'dotted'  //'dotted'虚线 'solid'实线
                                        }
                                    }
                                },
                                data: oh
                            },
                            {
                                name: 'NE',
                                type: 'line',
                                symbolSize: 4,
                                color: ['blue'],
                                itemStyle: {
                                    normal: {
                                        lineStyle: {
                                            width: 2,
                                            type: 'dotted'  //'dotted'虚线 'solid'实线
                                        }
                                    }
                                },
                                data: ne
                            }
                        ]
                    })
                } else {

                }
            }
        });

        $.ajax({
            type: "POST",
            dataType: "json",
            url: '<%=path%>/storage/suppliercommissionsale/getPieData?id=' + '${id}' + '&supplier=' + supplier + '&time=' + time,
            success: function (result) {
                var data = eval(result.message)[0];
                myecharts.setOption({
                    series: [
                        	{
                            name: '访问来源',
                            type: 'pie',//每个系列，通过type决定自己的系列型号
                            radius: '60%',
                            data: data,
                            roseType: 'angle',

                            itemStyle: {//图形样式 normal，emphasis
                                normal: {
                                    label: {
                                        show: true,
                                        formatter: '{b} : {c} ({d}%)'   //在饼状图上直接显示百分比
                                    },
                                    labelLine: {show: true}
                                },
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }

                            }
                        }
                    ]
                })
            }
        })
    })

    $("#times").change(function () {
        var supplier = $("#suppliers").val();
        var time = $("#times").val();
        $.ajax({
            type: "POST",
            dataType: "json",
            url: '<%=path%>/storage/suppliercommissionsale/getPieData?id=' + '${id}' + '&supplier=' + supplier + '&time=' + time,
            success: function (result) {
                var data = eval(result.message)[0];
                myecharts.setOption({
                    series: [
                        {
                            name: '访问来源',
                            type: 'pie',//每个系列，通过type决定自己的系列型号
                            radius: '60%',
                            data: data,
                            roseType: 'angle',

                            itemStyle: {//图形样式 normal，emphasis
                                normal: {
                                    label: {
                                        show: true,
                                        formatter: '{b} : {c} ({d}%)'   //在饼状图上直接显示百分比
                                    },
                                    labelLine: {show: true}
                                },
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }

                            }
                        }
                    ]
                })
            }
        })
    });
</script>