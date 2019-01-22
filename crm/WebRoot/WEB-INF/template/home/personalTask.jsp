<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">  
<title>个人任务</title>
<style>
.ui-priority-secondary{
opacity: 1 !important;
}
table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
</style>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import> 
<script type="text/javascript">
    var dcl,ywc,dcltoolbar,ycltoolbar,acl,scl,height=175;
	$(function(){
		if(${role eq '管理员'}){
			height=230;
		}
		
		$("#layout1").ligerLayout();
 		$("#framecenter").ligerTab({
 			 contextmenu:false//不要右键菜单
 		});
 		//getMessage();
 		setInterval(getQuoteMessage,1800*1000);
 		setInterval(getCrawlQuoteMessage,900*1000);
 		//爬取供应商的报价--对应采购的爬虫情况
 		setInterval(createSatairQuoteMessage,700*1000);
 		setInterval(updateStorageRestLife,800*1000);
 		setInterval(sendStorageToOrderEmail,600*1000);
 		//对应于采购StockMarket爬虫
 		setInterval(crawlStockMarket,550*1000);
        //dasi爬虫邮件
 		setInterval(dasiEmail,500*1000);
 		getNotImportMessage();
 		getOverTimeSupplierMessage();
 		//数据区的工具栏
		dcltoolbar = $("#toolbardcl").ligerToolBar({
			items : [ 
			{
					icon : 'refresh',
					text : '刷新',
					click: function(){
						PJ.grid_reload(dcl);
						window.parent.reloadGroupTask();
					}
				},
				 {
					id:'clrw',
					icon : 'process',
					text : '处理任务',
					click: function(){
						 var ret = PJ.grid_getSelectedData(dcl);
						 var user=ret["userName"];
						 var userId='${userId}';
						 if(user==userId){
							 var ids=getData();
							 var id;
							 /* if(ids==""){
								 id=ret.id
							 }else{
								 id=ids;
							 } */
							 id=ret.id
							 PJ.addTab(ret.name, ret.form + '?history=true&&taskId=' +id  );
						 }else{
							 PJ.warn("只能处理自己的任务！");
						 }
						
					}
				},
		         {
					id:'search',
					icon : 'search',
					text : '展开搜索',
					click: function(){
						$("#searchdiv").toggle(function(){
							var display = $("#searchdiv").css("display");
							var blockheight=250;
							var noneheight=180;
							if(${role eq '管理员'}){
								blockheight=300;
								noneheight=240;
							}
							if(display=="block"){
								$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
								dcl.setGridHeight(PJ.getCenterHeight()-blockheight);
							}else{
								$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
								dcl.setGridHeight(PJ.getCenterHeight()-noneheight);
							}
						});
					}
			}
			  ]
			});
			 
		 //数据区的工具栏
		ycltoolbar = $("#toolbarywc").ligerToolBar({
			items : [ 
			{
					icon : 'refresh',
					text : '刷新',
					click: function(){
						PJ.grid_reload(ywc);
					}
				},{
					id:'ckrw',
					icon : 'view',
					text : '查看',
					click: function(){
						 var ret1 = PJ.grid_getSelectedData(scl);
						 var ret2 = PJ.grid_getSelectedData(ywc);
						 if(ret1.userNmae == '流程结束'){
							 PJ.warn("已结束流程不再查看,记录一不存在！");
						 }else{
							 PJ.addTab(ret2.name+'_历史', basePath +'/workflow/historyTask/' + ret1.id);
						 }
					}
				} , {
					id:'search',
					icon : 'search',
					text : '展开搜索',
					click: function(){
						$("#ywcsearchdiv").toggle(function(){
							var display = $("#ywcsearchdiv").css("display");
							if(display=="block"){
								$("#toolbar > div[toolbarid='search'] > span").html("收起搜索");
								ywc.setGridHeight(PJ.getCenterHeight()-202);
							}else{
								$("#toolbar > div[toolbarid='search'] > span").html("展开搜索");
								ywc.setGridHeight(PJ.getCenterHeight()-142);
							}
						});
					}
				} 
			  ]
		});	
		
		dcl = PJ.grid("list1", {
			rowNum: 20,
			width : PJ.getCenterWidth(),
			pager:"#pager1",
			height :PJ.getCenterHeight() - height,
			subGrid:true,
			
			/* gridComplete: function() {
                var timeOut = 10;
                var rowIds = $("#list1").getDataIDs();
                $.each(rowIds, function (index, rowId) {
                    setTimeout(function() {
                        $("#list1").expandSubGridRow(rowId);
                    }, timeOut);
                    timeOut = timeOut + 10;
                });
            }, */
			<%-- jsonReader:{  
			    repeatitems : true,
			    subgrid: {
			      root:"subgridchid", //子级的内容 Action类中必须有与之匹配的属性
			      repeatitems: false  //false之后 subGridModel的mapping才起作用
			   }            
			},
			subGridUrl:"<%=path%>/workflow/subTask",
			subGridModel:[
				{name:["任务ID", "所属模块","任务名","任务名", "任务描述", "创建时间","form",""],
				width:[120,100,100,100,100,100,100,100],
			    align: ['center','center','center','center','center','center','center','center']}
			],
			subGridOptions:{expandOnLoad:true}, --%>
			   subGridOptions: {  
	               plusicon: 'ui-icon-plus',  
	               minusicon: 'ui-icon-minus',  
	               openicon: 'ui-icon-carat-1-sw',  
	               expandOnLoad: false,  
	               selectOnExpand: true,  
	               reloadOnExpand: true  
	           },
	           
		    subGridRowExpanded:function(subgrid_id, row_id){
                var subgrid_table_id;
                 subgrid_table_id = subgrid_id+"_t";
                 var dcldata = dcl.jqGrid("getRowData",row_id);
                 jQuery("#"+subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table>"); 
               	 acl=jQuery("#"+subgrid_table_id).jqGrid({
                     <%-- url:"<%=path%>/workflow/subTask?ids="+dcldata.ids, --%>
                     url:"<%=path%>/workflow/subTask?id="+dcldata.id,
                     datatype: "json", 
                     mtype: 'POST',
                     colNames:colNames(dcldata.tableName),
                     colModel: colModel(dcldata.tableName),
                    sortname: '1',
                    sortorder: "desc",
                    altRows:true, 
                    height:"100%", 
                    caption: "" ,
                    shrinkToFit:true,
                    autowidth:true,
                    multiselect:true,
                    rowNum: -1,
                    gridComplete:function(){
                    	 var rowData=dcl.getRowData(row_id);
                    	 var subObj = $("#"+subgrid_table_id).jqGrid("getRowData");
                    	 var ids="";
            			 for(var i=0, len=subObj.length; i<len; i++) {
            				 ids += subObj[i].id+",";
                         }
            			 if(rowData.ids==""){
                   		  $("#list1").collapseSubGridRow(row_id);
                   	 	}else{
                   	 	dcl.setSelection(row_id);
                   	 	}
                    	//rowData.ids=ids;
                    	//dcl.setRowData(row_id,rowData);
                    	
                      

                        //隐藏标题栏
                      //  var p= $("#"+subgrid_table_id).parent().parent().parent()
                     //   p=$(p).find("table").first().hide(); 
                        //获得所有行数据
                       /*  var subObj = $("#"+subgrid_table_id).jqGrid("getRowData");
                        var show = 0;
                        var click = 0;
                        var cr;
                        
                         for(var i=0, len=subObj.length; i<len; i++) {
                             show += parseInt(subObj[i].show.replace(",", ""));
                             click += parseInt(subObj[i].click.replace(",", ""));
                         }
                         var rowData = $("#list1").jqGrid("getRowData",row_id);   //获得指定row_id的行数据
                         if(parseInt(rowData.show.replace(",", "")) != show) {
                             if(show != 0) {
                                 cr = (click*100/show).toFixed(2) + "%"; 
                             } else {
                                 cr = "0%";
                             }

                              //设置行数据
                             $("#area_grid").setRowData( row_id, {show:formatNum(show.toString()), click:formatNum(click.toString()), cr:cr});
                         } */
                   }
                 });
               	 
		    },
			
			url:'<%=path%>/workflow/listData',
			colNames : [ "id", "ywTableId","tableName","任务单号","任务名", "任务描述","所属模块","审批意见","创建时间","任务处理人","form","ids" ],
			colModel : [
						PJ.grid_column("id", {key:true,width:80,hidden:true}),
						PJ.grid_column("ywTableId", {key:true,hidden:true}),
						PJ.grid_column("tableName", {hidden:true}),
						PJ.grid_column("taskNumber", {}),
						PJ.grid_column("name", {}),
						PJ.grid_column("descr", {}),
						PJ.grid_column("deploymentName", {}),
						PJ.grid_column("message", {}),
						PJ.grid_column("create", {width:80}),
						PJ.grid_column("userName", {width:60}),
						PJ.grid_column("form", {hidden:true}),
						PJ.grid_column("ids", {hidden:true})
					 ],
			ondblClickRow:function(){
				 var ret = PJ.grid_getSelectedData(dcl);
				 var user=ret["userName"];
				 var userId='${userId}';
				 if(user==userId){
					 dcltoolbar.toolBar.find(".l-toolbar-item[toolbarid=clrw]").trigger("click");
				 }else{
					 PJ.warn("只能处理自己的任务！");
				 }
			}
		}); 
		
		ywc = PJ.grid("list2", {
			rowNum: 20,
			width : PJ.getCenterWidth(),
			pager:"#pager2",
			height :PJ.getCenterHeight() - 146,
			subGrid:true,
			
			   subGridOptions: {  
	               plusicon: 'ui-icon-plus',  
	               minusicon: 'ui-icon-minus',  
	               openicon: 'ui-icon-carat-1-sw',  
	               expandOnLoad: false,  
	               selectOnExpand: true,  
	               reloadOnExpand: true  
	           },
	           
		    subGridRowExpanded:function(subgrid_id, row_id){
                var subgrid_table_id;
                 subgrid_table_id = subgrid_id+"_t";
                 var ywcdata = ywc.jqGrid("getRowData",row_id);
                 jQuery("#"+subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table>"); 
                 postData = {};
                 postData.ywTableId = ywcdata.ywTableId;
                 postData.activityName = ywcdata.activityName;
                 postData.name = ywcdata.name;
               	 scl=jQuery("#"+subgrid_table_id).jqGrid({
                     url:"<%=path%>/workflow/myFinishedListsUBData?ywTableId="+ywcdata.ywTableId+'&activityName='+ywcdata.activityName+'&name='+ywcdata.name,
                     datatype: "json", 
                     data:postData,
                     mtype: 'POST',
                     colNames:ywccolNames(ywcdata.tableName),
                     colModel: ywccolModel(ywcdata.tableName),
                    sortname: '1',
                    sortorder: "desc",
                    altRows:true, 
                    height:"100%", 
                    caption: "" ,
                    shrinkToFit:true,
                    autowidth:true,
                    //multiselect:true,
                    rowNum: -1,
                    gridComplete:function(){
                    	 var rowData=dcl.getRowData(row_id);
                    	 var subObj = $("#"+subgrid_table_id).jqGrid("getRowData");
                    	 var ids="";
            			 for(var i=0, len=subObj.length; i<len; i++) {
            				 ids += subObj[i].id+",";
                         }
            			 if(rowData.ids==""){
                   		  $("#list1").collapseSubGridRow(row_id);
                   	 	}else{
                   	 	dcl.setSelection(row_id);
                   	 	}
                    	//rowData.ids=ids;
                    	//dcl.setRowData(row_id,rowData);
                    	
                      

                        //隐藏标题栏
                      //  var p= $("#"+subgrid_table_id).parent().parent().parent()
                     //   p=$(p).find("table").first().hide(); 
                        //获得所有行数据
                       /*  var subObj = $("#"+subgrid_table_id).jqGrid("getRowData");
                        var show = 0;
                        var click = 0;
                        var cr;
                        
                         for(var i=0, len=subObj.length; i<len; i++) {
                             show += parseInt(subObj[i].show.replace(",", ""));
                             click += parseInt(subObj[i].click.replace(",", ""));
                         }
                         var rowData = $("#list1").jqGrid("getRowData",row_id);   //获得指定row_id的行数据
                         if(parseInt(rowData.show.replace(",", "")) != show) {
                             if(show != 0) {
                                 cr = (click*100/show).toFixed(2) + "%"; 
                             } else {
                                 cr = "0%";
                             }

                              //设置行数据
                             $("#area_grid").setRowData( row_id, {show:formatNum(show.toString()), click:formatNum(click.toString()), cr:cr});
                         } */
                   }
                 });
               	 
		    },
			
			url:'<%=path%>/workflow/myFinishedListData',
			autoSrcoll:true,
			shrinkToFit:true,
			colNames : [ "id", "ywTableId","tableName","任务单号","任务名", "所属模块","审批意见","创建时间","form","ids" ],
			colModel : [
						PJ.grid_column("id", {key:true,width:80,hidden:true}),
						PJ.grid_column("ywTableId", {key:true,hidden:true}),
						PJ.grid_column("tableName", {width:160,hidden:true}),
						PJ.grid_column("taskNumber", {width:160}),
						PJ.grid_column("activityName", {width:215}),
						PJ.grid_column("name", {width:215}),
						PJ.grid_column("message", {}),
						PJ.grid_column("create", {width:160}),
						PJ.grid_column("form", {hidden:true}),
						PJ.grid_column("ids", {hidden:true})
					 ],
			ondblClickRow:function(){
				dcltoolbar.toolBar.find(".l-toolbar-item[toolbarid=clrw]").trigger("click");
			}
		}); 
		
		/* //二级表头
		$( "#list1" ).jqGrid( 'setGroupHeaders' , {
			useColSpanStyle :  true ,  // 没有表头的列是否与表头列位置的空单元格合并
			groupHeaders : [ {
								startColumnName :  'id' ,  // 对应colModel中的name
								numberOfColumns : 20,  // 跨越的列数
								titleText :  '客户订单'
							 }
							]
		}); */
		
	 	//搜索框 收缩/展开
		$(".searchtitle .togglebtn").live('click', function() {
			var searchbox = $(this).parent().nextAll("div.searchbox:first");
			toggle(searchbox);
		}); 

		//边框去除
		$(".l-panel").css("border", "none");
		
	<%-- 	 //搜索
		 $("#searchBtn").click(function(){
		     var endTime = $("#endTime").val();
		     var param = "?endTime="+$("#endTime").val()
		     			+"&endTimeTo="+$("#endTimeTo").val()
		     			+"&modelName="+$("#modelName").val();
			 PJ.grid_search(ywc,'<%=path%>/workflow/getAlreadyProcessed');
		 }); 
		 
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 }); --%>
		 
		//jqGrid 自适应
		$(window).resize(function() {
			dcl.setGridWidth(PJ.getCenterWidth());
			dcl.setGridHeight(PJ.getCenterHeight()-146);
			ywc.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				ywc.setGridHeight(PJ.getCenterHeight()-206);
			}else{
				ywc.setGridHeight(PJ.getCenterHeight()-146);
			}
		});
		//客户询价单到期提醒
		 $.ajax({
			url: '<%=path%>/dueremind/getInquiryWarnCount?type='+'marketing&warningDate=3',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#clientInquiryWarn").text(result.message);
				}
			}
		});
		
		//供应商询价单到期提醒
		$.ajax({
			url: '<%=path%>/dueremind/getInquiryWarnCount?type='+'purchase&warningDate=3',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#supplierInquiryWarn").text(result.message);
				}
			}
		});
		
		//流程退回
		$.ajax({
			url: '<%=path%>/purchase/deploymentSendBack/getListDataCount',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#deploymentSendBack").text(result.message);
				}
			}
		});
		
		//客户订单到期提醒
		$.ajax({
			url: '<%=path%>/dueremind/getOrderWarnCount',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#clientOrderWarn").text(result.message);
				}
			}
		});
		
		//订单货款到期提醒
		$.ajax({
			url: '<%=path%>/sales/clientorder/getDeadlineOrderCount',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#receiveWarn").text(result.message);
				}
			}
		});
		
		//供应商订单到期提醒
		$.ajax({
			url: '<%=path%>/dueremind/getWarnSupplierOrderCount',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#supplierOrderWarn").text(result.message);
				}
			}
		});
		
		//付款提醒
		$.ajax({
			url: '<%=path%>/finance/importpackagepayment/getPaymentWarnCount',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#paymentWarn").text(result.message);
				}
			}
		});
		
		//换库位提醒
		$.ajax({
			url: '<%=path%>/importpackage/unchangeLocationCount',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				if(result.success){
					$("#unchangeLocation").text(result.message);
				}
			}
		}); <%--  --%>
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(dcl,'<%=path%>/workflow/listData');
		    }  
		});
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(dcl,'<%=path%>/workflow/listData');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		// 绑定键盘按下事件  
			$(document).keypress(function(e) {
				// 回车键事件  
			    if(e.which == 13) {
			    	PJ.grid_search(ywc,'<%=path%>/workflow/myFinishedListData');
			    }  
			});
		 
		//搜索
		 $("#ywcsearchBtn").click(function(){
			 PJ.grid_search(ywc,'<%=path%>/workflow/myFinishedListData');
			
		 });
		
		 //重置条件
		 $("#ywcresetBtn").click(function(){
			 $("#ywcsearchForm")[0].reset();
		 });
	});
	
	function colNames(tableName){
		var names={};
		if(tableName=='IMPORT_PACKAGE_PAYMENT_ELEMENT'){
			names=['id','件号','应付','数量','比例','备注']
		}else if(tableName=='IMPORT_PACKAGE_ELEMENT'){
			names=['id','件号','描述','单位','数量','备注']
		}else{
			names=['id','序号','ITEM','件号','描述','单位','数量','备注']
		}
		return names;
	}
	
	function colModel(tableName){
		var models={};
		if(tableName=='IMPORT_PACKAGE_PAYMENT_ELEMENT'){
			models=[ 
			         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
			         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
			         {name:'price',index:'click',sortable:true,width:275,editable:false,align:"left"},
			         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'paymentPercentage',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'remark',index:'cr',sortable:true,width:270,editable:false,align:"left"}
					]
		}else if(tableName=='IMPORT_PACKAGE_ELEMENT'){
			models=[ 
			         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
			         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
			         {name:'description',index:'click',sortable:true,width:275,editable:false,align:"left"},
			         {name:'unit',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'remark',index:'cr',sortable:true,width:270,editable:false,align:"left"}
					]
		}else{
			models=[ 
			         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
			         {name:'number',index:'show',sortable:true,width:103,editable:false,align:"left"},
			         {name:'item',index:'show',sortable:true,width:103,editable:false,align:"left"},
			         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
			         {name:'description',index:'click',sortable:true,width:275,editable:false,align:"left"},
			         {name:'unit',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'remark',index:'cr',sortable:true,width:270,editable:false,align:"left"}
					]
		}
		return models;
	}
	
	
	function ywccolNames(tableName){
		var names={};
		if(tableName=='IMPORT_PACKAGE_PAYMENT_ELEMENT'){
			names=['id','件号','应付','数量','比例','备注','当前处理人']
		}else if(tableName=='IMPORT_PACKAGE_ELEMENT'){
			names=['id','件号','描述','单位','数量','备注','当前处理人']
		}else{
			names=['id','序号','ITEM','件号','描述','单位','数量','当前处理人']
		}
		return names;
	}
	
	function ywccolModel(tableName){
		var models={};
		if(tableName=='IMPORT_PACKAGE_PAYMENT_ELEMENT'){
			models=[ 
			         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
			         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
			         {name:'price',index:'click',sortable:true,width:275,editable:false,align:"left"},
			         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'paymentPercentage',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'"remark"',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'userNmae',index:'cr',sortable:true,width:270,editable:false,align:"left"}
					]
		}else if(tableName=='IMPORT_PACKAGE_ELEMENT'){
			models=[ 
			         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
			         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
			         {name:'description',index:'click',sortable:true,width:275,editable:false,align:"left"},
			         {name:'unit',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'"remark"',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'userNmae',index:'cr',sortable:true,width:270,editable:false,align:"left"}
					]
		}else{
			models=[ 
			         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
			         {name:'number',index:'show',sortable:true,width:103,editable:false,align:"left"},
			         {name:'item',index:'show',sortable:true,width:103,editable:false,align:"left"},
			         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
			         {name:'description',index:'click',sortable:true,width:275,editable:false,align:"left"},
			         {name:'unit',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"},
			         {name:'userNmae',index:'cr',sortable:true,width:270,editable:false,align:"left"}
					]
		}
		return models;
	}
	
	//重新加载数据
	function reloadPersonalTask()
	{
		PJ.grid_reload(dcl);
	}
	
	//读取报价提示消息
	function getQuoteMessage(){
		$.ajax({
			url: '<%=path%>/supplierquote/warnMessage',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						PJ.alert(obj[i].partNumber,callBack(obj[i].clientInquiryId));
					}
				}
			}
		});
		//setTimeout(getMessage(),10000000);
	}
	
	//读取报价大于5000的爬虫消息
	function getCrawlQuoteMessage(){
		$.ajax({
			url: '<%=path%>/supplierquote/highPriceMessage',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						PJ.alert(obj[i].partNumber,PriceCallBack(obj[i].clientInquiryId));
					}
				}
			}
		});
		//setTimeout(getMessage(),10000000);
	}
	
	//生成爬虫报价
	function createSatairQuoteMessage(){
		$.ajax({
			url: '<%=path%>/supplierquote/createSatairQuote',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
				/* var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						PJ.alert(obj[i].partNumber,callBack(obj[i].clientInquiryId));
					}
				} */
			}
		});
		//setTimeout(getMessage(),10000000);
	}
	
	//更新库存剩余生命，并发送邮件
	function updateStorageRestLife(){
		$.ajax({
			url: '<%=path%>/importpackage/shilfLifeDealLine',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//库存转订单邮件发送
	function sendStorageToOrderEmail(){
		$.ajax({
			url: '<%=path%>/market/clientweatherorder/storageToOrderEmail',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//stockmarket爬虫
	function crawlStockMarket(){
		$.ajax({
			url: '<%=path%>/storage/assetpackage/checkStockMarketCrawl',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//dasi爬虫邮件
	function dasiEmail(){
		$.ajax({
			url: '<%=path%>/supplierquote/checkDasiCrawlMessage',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//读取到期未到货提醒消息
	function getNotImportMessage(){
		$.ajax({
			url: '<%=path%>/importpackage/checkImportLeadTime',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//提醒资质即将过期的供应商
	function getOverTimeSupplierMessage(){
		$.ajax({
			url: '<%=path%>/suppliermanage/checkSupplierAptitude',
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//更新提醒报价的状态
	function callBack(id){
		$.ajax({
			url: '<%=path%>/supplierquote/updateStatus?id='+id,
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//更新报价大于5000的记录的状态
	function PriceCallBack(id){
		$.ajax({
			url: '<%=path%>/supplierquote/updateHighPriceStatus?id='+id,
			type: "POST",
			loading: "正在处理...",
			success:function(result){
			}
		});
	}
	
	//获取表单数据
	function getData(){
		/*  var postData = {}; */
		 var ret = PJ.grid_getSelectedData(dcl);
		var ids="";
		 var rowKey ="";
		if(acl){
			  rowKey = acl.getGridParam("selarrrow");
		}
		 if(rowKey!= ""){
			 var id =  PJ.grid_getMutlSelectedRowkey(acl);
			 ids = id.join(",");
			/*  if(ids.length>0){
					postData.ids = ids;
				} */
		 }
		 if(rowKey== ""){
				 ids += ret["ids"];
		 }
		 return ids;
	 }
	
	function skipTab(text,url){
		PJ.addTab(text, url);
	}
	
</script>
<style>
	#cb_list1{
		margin:0
	}
</style>

</head>
	<body style="overflow: hidden;">
	   	<div id="layout1">
			<div position="center" id="framecenter">
				<!-- <div tabid="txsw" title="提醒事务" >
			
				</div> -->
				<div tabid="dcl" title="待处理任务" >
					 	<table border="2px" width="100%"  style="font-size:15px;" class="l-toolbar">
						<c:if test="${role eq '管理员'}">
							<tr  style="height:30px;">
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('客户询价单到期提醒','/dueremind/viewListByInquiry?type=marketing')">客户询价单到期提醒:</a><span id="clientInquiryWarn" readonly="readonly"  style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('客户订单到期提醒','/dueremind/viewListByClientOrder')">客户订单到期提醒:</a><span id="clientOrderWarn" readonly="readonly" style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('订单货款到期提醒','/sales/clientorder/toDeadlineOrder')">订单货款到期提醒:</a><span id="receiveWarn" readonly="readonly" style="color:red; "/><br /></td>
							</tr>
							<tr  style="height:30px;">
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('供应商询价单到期提醒','/dueremind/viewListByInquiry?type=purchase')">供应商询价单到期提醒:</a><span id="supplierInquiryWarn" readonly="readonly" style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('供应商订单到期提醒','/dueremind/viewListBySupplierOrder')">供应商订单到期提醒:</a><span id="supplierOrderWarn" readonly="readonly" style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('付款提醒','/finance/importpackagepayment/toPaymentWarn')">付款提醒:</a><span id="paymentWarn" readonly="readonly" style="color:red; "/><br /></td>
							</tr>
							<tr  style="height:30px;">
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('转换库位提醒','/importpackage/toUnchangeLocationPage')">转换库位提醒:</a><span id="unchangeLocation" readonly="readonly" style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('流程退回列表','/purchase/deploymentSendBack/toListData')">流程退回:</a><span id="deploymentSendBack" readonly="readonly" style="color:red; "/><br /></td>
							</tr>
						</c:if>
						<c:if test="${role eq '销售'}">
						<tr  style="height:30px;">
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('客户询价单到期提醒','/dueremind/viewListByInquiry?type=marketing')">客户询价单到期提醒:</a><span id="clientInquiryWarn" readonly="readonly"  style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('客户订单到期提醒','/dueremind/viewListByClientOrder')">客户订单到期提醒:</a><span id="clientOrderWarn" readonly="readonly" style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('订单货款到期提醒','/sales/clientorder/toDeadlineOrder')">订单货款到期提醒:</a><span id="receiveWarn" readonly="readonly" style="color:red; "/><br /></td>
							</tr>
						</c:if>
						<c:if test="${role eq '国外采购' || role eq '国内采购'}">
							<tr  style="height:30px;">
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('供应商询价单到期提醒','/dueremind/viewListByInquiry?type=purchase')">供应商询价单到期提醒:</a><span id="supplierInquiryWarn" readonly="readonly" style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('供应商订单到期提醒','/dueremind/viewListBySupplierOrder')">供应商订单到期提醒:</a><span id="supplierOrderWarn" readonly="readonly" style="color:red; "/><br /></td>
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('付款提醒','/finance/importpackagepayment/toPaymentWarn')">付款提醒:</a><span id="paymentWarn" readonly="readonly" style="color:red; "/><br /></td>
							</tr>
							<c:if test="${isLeader eq true}">
								<tr  style="height:30px;">
									<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('流程退回列表','/purchase/deploymentSendBack/toListData')">流程退回:</a><span id="deploymentSendBack" readonly="readonly" style="color:red; "/><br /></td>
								</tr>
							</c:if>
						</c:if>
						<c:if test="${role eq '物流'}">
						<tr  style="height:30px;">
								<td style="vertical-align:middle; text-align:center;"><a onclick="skipTab('转换库位提醒','/importpackage/toUnchangeLocationPage')">转换库位提醒:</a><span id="unchangeLocation" readonly="readonly" style="color:red; "/><br /></td>
							</tr>
						</c:if>
					</table>  <%----%>
					<div id="toolbardcl"></div>
					<div id="searchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>任务单号：</p></form:left>
								<form:right><p><input id="searchForm2" class="text" type="text" name="taskNumber" alias="IFNULL(IFNULL(ip.IMPORT_NUMBER,ipp.PAYMENT_NUMBER),cwo.ORDER_NUMBER)" oper="cn"/> </p></form:right>
							</form:column>
								<form:column>
								<form:left><p>任务名：</p></form:left>
								<form:right><p><input id="searchForm3" class="text" type="text" name="name" alias="jt.name_" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>所属模块：</form:left>
								<form:right><p>
												<select id="searchForm1" name="clientCode" alias="jdt.name_" oper="eq">
							        			<option value="">全部</option>
							        			<option value="合同评审">合同评审</option>
							        			<option value="付款申请">付款申请</option>
							        			<option value="异常件审核">异常件审核</option>
							            		</select>
											</p>
								</form:right>
							</form:column>	
							<form:column >
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
				<div tabid="ywc" title="已完成任务" >
					<div id="toolbarywc"></div>
						<div id="ywcsearchdiv" style="width: 100%;height: 60px;display: none;">
				<form id="ywcsearchForm">
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>任务单号：</p></form:left>
								<form:right><p><input id="searchForm4" class="text" type="text" name="taskNumber" alias="IFNULL(IFNULL(ip.IMPORT_NUMBER,ipp.PAYMENT_NUMBER),cwo.ORDER_NUMBER)" oper="cn"/> </p></form:right>
							</form:column>
								<form:column>
								<form:left><p>任务名：</p></form:left>
								<form:right><p><input id="searchForm5" class="text" type="text" name="name" alias="jha.activity_name_" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left>所属模块：</form:left>
								<form:right><p>
												<select id="searchForm6" name="clientCode" alias="jdnt.name_" oper="eq">
							        			<option value="">全部</option>
							        			<option value="合同评审">合同评审</option>
							        			<option value="付款申请">付款申请</option>
							        			<option value="异常件审核">异常件审核</option>
							            		</select>
											</p>
								</form:right>
							</form:column>	
							<form:column >
								<form:right>
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="ywcsearchBtn"/>
								<input class="btn btn_blue" type="button" value="重置" id="ywcresetBtn"/>
							</p>
							</form:right>
							</form:column>
						</form:row>
					</div>
				</form>
			</div>
					<table id="list2"></table>
					<div id="pager2"></div>
				</div>
				
			</div>
		</div>
	</body>
</html>