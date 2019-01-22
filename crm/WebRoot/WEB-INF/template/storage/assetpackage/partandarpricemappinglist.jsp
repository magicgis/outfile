<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>资产包--AR预测价列表</title>

    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <jsp:include page="/WEB-INF/template/common/resource.jsp"/>
    <script type="text/javascript">
        var layout, grid;
        $(function () {
            layout = $("#layout1").ligerLayout();

            layout = $("#layout1").ligerLayout({
               centerBottomHeight:500,
                // onEndResize:function (e) {
                //     GridResize();
                // }
            });

            $("#toolbar").ligerToolBar({
                items: [
                    {
                        id:'uploadArPrice',
                        icon:'up',
                        text:'根据模板上传AR价格',
                        click: function () {
                            var iframeId = "ideaIframe5";
                            // var ret = PJ.grid_getSelectedData(grid);
                            // var id = ret["id"];
                            PJ.topdialog(iframeId, '上传AR价格', '<%=path%>/storage/assetpackage/toAddArPriceTable',
                                function (item, dialog) {
                                    var postData = top.window.frames[iframeId].getFormData();
                                    PJ.grid_reload(grid);
                                    dialog.close();
                                },
                                function (item, dialog) {
                                    PJ.grid_reload(grid);
                                    dialog.close();
                                }, 600, 200, true, "新增");
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
                                    grid.setGridHeight(PJ.getCenterHeight() - 192);
                                } else {
                                    $("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
                                    grid.setGridHeight(PJ.getCenterHeight() - 142);
                                }
                            });
                        }
                    },
                    {
                        id:'down',
                        icon:'down',
                        text:'下载模板',
                        click:function () {
                            PJ.showLoading("处理中...");
                            window.location.href = '<%=path%>/storage/assetpackage/downloadArTemplate';
                            setTimeout(function () {
                                PJ.hideLoading();
                            }, 5000);
                        }
                    }
                ]
            });

            grid = PJ.grid("list1", {
                rowNum: 20,
                url: '<%=path%>/storage/assetpackage/getARPriceData',
                width: PJ.getCenterWidth(),
                height: PJ.getCenterHeight() - 142,
                autoSrcoll: true,
                shrinkToFit: true,
                sortname: "a.update_timestamp",
                sortorder: "desc",
//                multiselect: true,
                onSelectRow: function (rowid, status, e) {
                    onSelect();
                },
                onSelectAll: function (rowids, statue, e) {
                    onSelect();
                },
                colNames: ["件号id", "件号","DESCRIPTION","DOM","AR评估价格","AR出售价价格", "创建人", "更新时间"],
                colModel: [
                    PJ.grid_column("id", {key: true, hidden: true}),
                    PJ.grid_column("partNumber", {sortable: false, width: 40, align: 'left'
                    }),
                    PJ.grid_column("description",{sortable: false, width: 40, align: 'left'}),
                    PJ.grid_column("dom",{sortable: false, width: 40, align: 'left'}),
                    PJ.grid_column("arPrice", {sortable: true, width: 40, align: 'left'}),
                    PJ.grid_column("arSalePrice", {sortable: true, width: 40, align: 'left'}),
                    PJ.grid_column("username", {sortable: true, width: 40, align: 'left'}),
                    PJ.grid_column("updateTimestamp", {sortable: true, width: 50, align: 'left'})
                ]
//			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
//			           PJ.grid_column("currencyId", {key:true,hidden:true}),
//			           PJ.grid_column("supplierCode", {sortable:true,width:40,align:'left'}),
//			           PJ.grid_column("clientOrderNumber", {sortable:true,width:150,align:'left'}),
//			           PJ.grid_column("supplierOrderNumber", {sortable:true,width:100,align:'left',
//			        	   formatter:function(cellvalue, options, rowObj){
//			                    return "<span id='"+rowObj.id+"' class='xmidData' style='display:block; width:100%; cursor:pointer;'>"+cellvalue+"</span>";
//			        	   }
//			           }),
//			           PJ.grid_column("orderType", {sortable:true,width:60,editable:true,align:'left',
//			        		formatter:function(value){
//			        			if(value==1){
//			        				return '<span style="color:red">'+'是'+'<span>';
//			        			}
//			        		}
//			        	   }),
//			           PJ.grid_column("orderDate", {sortable:true,width:80,align:'left'}),
//			           PJ.grid_column("currencyValue",{sortable:true,width:60,align:'left'}),
//			           PJ.grid_column("exchangeRate", {sortable:true,width:40,align:'left'}),
//			           PJ.grid_column("total", {sortable:true,width:70,align:'left'}),
//			           PJ.grid_column("urgentLevelValue", {sortable:true,width:60,align:'left'}),
//			           PJ.grid_column("prepayRate", {sortable:true,width:80,align:'left',
//			        	   formatter: function(value){
//								if (value) {
//									return value+"%";
//								} else {
//									return '0'+"%";
//								}
//							}}),
//			           PJ.grid_column("shipPayRate", {sortable:true,width:80,align:'left',
//			        	   formatter: function(value){
//								if (value) {
//									return value+"%";
//								} else {
//									return '0'+"%";
//								}
//							}}),
//			           PJ.grid_column("receivePayRate", {sortable:true,width:80,align:'left',
//			        	   formatter: function(value){
//								if (value) {
//									return value+"%";
//								} else {
//									return '0'+"%";
//								}
//							}}),
//			           PJ.grid_column("receivePayPeriod", {sortable:true,width:80,align:'left'}),
//			           PJ.grid_column("amountPercent", {sortable:true,width:90,align:'left'}),
//			           PJ.grid_column("totalPercent", {sortable:true,width:90,align:'left'}),
//			           PJ.grid_column("orderStatusValue",{sortable:true,width:80,align:'left'}),
//			           PJ.grid_column("userName",{sortable:true,width:80,align:'left'}),
//			           PJ.grid_column("updateTimestamp",{sortable:true,width:130,align:'left'})
//			           ]
            });

            //选中行之后进行获取数据id与件号并调用loadLineData
            function onSelect() {
                var ret = PJ.grid_getSelectedData(grid);
                var id = ret["id"];
                var partNumber = ret["partNumber"];
                loadLineData(id);
            };

            // 绑定键盘按下事件
            $(document).keypress(function (e) {
                // 回车键事件
                if (e.which == 13) {
                    var partNumber = $("#partNumber").val()
                    PJ.grid_search(grid, '<%=path%>/purchase/supplierorder/SupplierOrderData?partNumber=' + partNumber);
                }
            });

            //搜索
            $("#searchBtn").click(function () {
                var partNumber = $("#partNumber").val()
                PJ.grid_search(grid, '<%=path%>/storage/assetpackage/getARPriceData?partNumber=' + partNumber);
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
    <div position="center" title="<span id='titleId'>总金额：0</spam>">
        <div id="toolbar"></div>
        <div id="searchdiv" style="width: 100%;height: 50px;display: none;">
            <form id="searchForm">
                <div class="search-box">
                    <form:row columnNum="5">
                        <form:column>
                            <form:left><p>件号：</p></form:left>
                            <form:right><p><input id="partNumber" class="text" type="text" alias="a.part_number" oper="eq"/></p></form:right>
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

    <div position="centerbottom" style="height: 500px;">
        <div id="box" style="height: 300px;width: 800px;margin: auto">
        </div>
    </div>

</div>
</body>
</html>

<script type="text/javascript">

        function loadLineData(id){
            $.ajax({
                type: "POST",
                dataType: "json",
                url: '<%=path%>/storage/assetpackage/getArPriceListById?id=' + id,
                success:function (result) {
                    var date = result.data.dateList;
                    var value = result.data.valueList;
                    var myChart = echarts.init(document.getElementById("box"));
                    var option = {
                        // 标题
                        title: {
                            text: 'AR评估价格曲线图',
                            subtext: '数据来源：AR预测价',
                            left:'100',
                            textStyle: {
                                align: 'center',
                                fontSize: 16,
                                color: "black",
                                rich: {
                                    a: {}
                                }
                            }
                        },
                        //图例名
                        legend: {
                            data: ['ArPrice'],
                            show:false
                        },
                        grid: {
                            left: '3%',   //图表距边框的距离
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        tooltip: {
                            trigger: 'axis',
                            // axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            //     // type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            // }
                        },
                        yAxis:
                            {
                                type: 'value',
                                name:'AR评估价格',
                                show:true,
                                scale: true,
                                // nameLocation:'center',
                                splitLine:{
                                    show:true
                                },
                                nameTextStyle:{
                                    color:'black'
                                }
                            },
                        xAxis:{
                            type:'category',
                            name:'价格更新日期',
                            // nameLocation:'center',
                            nameTextStyle:{
                                color:'black'
                            },
                            splitLine:{
                                show:true
                            },
                            data:date,
                            //x轴文字旋转
                            axisLabel: {
                                rotate: -45,
                                interval: 0
                            },
                        },
                        series:[
                            {
                                name:'ArPrice',
                                type:'line',
                                data:value
                            }
                        ]
                    };
                    myChart.setOption(option);
                }
            });
        }

</script>







