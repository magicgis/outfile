<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>组任务</title>
	<style >
	.ui-priority-secondary{
opacity: 1 !important;
}</style>
	<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>  
	<script type="text/javascript">
		var task_grid, times=0,acl;
		$(function(){
			$("#div_main").ligerLayout();
			
			//工具栏
    		$("#task_toolbar").ligerToolBar({
    			items : [{
    				icon : 'refresh',
    				text : '刷新',
    				click : function() {
    					PJ.grid_reload(dcl);
    				}
    			} ,{
    				icon : 'right',
    				text : '接收任务',
    				click : function(rowid, status, e) {
    				//	var ids = PJ.grid_getMutlSelectedRowkey(acl);
    				var ret = PJ.grid_getSelectedData(dcl);
    					var ids=getData();
    					 var id;
						 if(ids==""){
							 id=ret.ids
						 }else{
							 id=ids;
						 }
    					if (ids)
    					{
	    					PJ.ajax({
	    						url: '<%=path%>/home/takeTask',
	    						data:'taskIds=' + ids,
	    						success:function(result){
	    							if(result.success){
	    								PJ.tip(result.message,function(){
	    									PJ.grid_reload(dcl);
	    									window.parent.reloadPersonalTask();
	    								});
	    							}else{
	    								PJ.error(result.message);
	    							}
	    						}
	    					});
    					}
    				}
    			} ]
    		});
			
    		<%-- task_grid = PJ.grid("task_grid", {
				url: '<%=path%>/home/groupTaskData',
				width : PJ.getCenterWidth(),
				height : PJ.getCenterHeight() - 112,
    			pager: '#task_page',
    			multiselect:true,
    			colNames : ["任务ID","所属模块", "任务名称"],
    			colModel : [PJ.grid_column("id", {key:true}),
    			            PJ.grid_column("deploymentName", {}),
    						PJ.grid_column("name", {})],
				gridComplete:function(){
					var num = task_grid.jqGrid('getRowData').length;
					if(num > 0)
					{
						parent.layout.setRightCollapse(false);
					}
				}
    		}); --%>
			
    		dcl = PJ.grid("task_grid", {
    			rowNum: 20,
    			width : PJ.getCenterWidth(),
    			pager:"#task_page",
    			height :PJ.getCenterHeight() - 146,
    			subGrid:true,
    			gridComplete:function(){
					var num = dcl.jqGrid('getRowData').length;
					if(num > 0)
					{
						parent.layout.setRightCollapse(false);
					}else if(num == 0){
						parent.layout.setRightCollapse(true);
					}
				},
    			
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
                         url:"<%=path%>/home/subTask?ywTableId="+dcldata.ywTableId,
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
    			
    			url:'<%=path%>/home/listData',
    			autoSrcoll:true,
    			shrinkToFit:false,
    			colNames : [ "id", "ywTableId","tableName","任务单号","任务名", "任务描述","创建时间","form","ids" ],
    			colModel : [
    						PJ.grid_column("id", {key:true,width:80,hidden:true}),
    						PJ.grid_column("ywTableId", {key:true,hidden:true}),
    						PJ.grid_column("tableName", {width:160,hidden:true}),
    						PJ.grid_column("taskNumber", {width:190}),
    						PJ.grid_column("taskdefname", {width:150}),
    						PJ.grid_column("descr", {width:215}),
    						PJ.grid_column("create", {width:160}),
    						PJ.grid_column("form", {hidden:true}),
    						PJ.grid_column("ids", {hidden:true})
    					 ],
    			ondblClickRow:function(){
    				dcltoolbar.toolBar.find(".l-toolbar-item[toolbarid=clrw]").trigger("click");
    			}
    		}); 
    		
    	<%-- 	ywc = PJ.grid("list2", {
    			rowNum: 20,
    			width : PJ.getCenterWidth(),
    			height : PJ.getCenterHeight()-146,
    			pager:"#pager2",
    			url:'<%=path%>/workflow/myfinished',
    			colNames : [ "任务ID", "所属模块", "任务名" ,"完成时间"],
    			colModel : [
    						PJ.grid_column("id", {key:true}),
    						PJ.grid_column("name", {}),
    						PJ.grid_column("curentName", {}),
    						PJ.grid_column("endTime", {})
    					 ],
    			 ondblClickRow:function(){
    				 ycltoolbar.toolBar.find(".l-toolbar-item[toolbarid=ckrw]").trigger("click");
    			 }
    		}); 
    		ywc = PJ.grid("list2", {
    			rowNum: -1,
    			width : PJ.getCenterWidth(),
    			pager:"#pager2",
    			height :PJ.getCenterHeight() - 146,
    			subGrid:true,
    			gridComplete:function(){
					var num = dcl.jqGrid('getRowData').length;
					if(num > 0)
					{
						parent.layout.setRightCollapse(false);
					}
				},
    			
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
                         url:"<%=path%>/workflow/myFinishedListData?ywTableId="+dcldata.ywTableId,
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
    			
    			url:'<%=path%>/workflow/myFinishedListsUBData',
    			autoSrcoll:true,
    			shrinkToFit:false,
    			colNames : [ "id", "ywTableId","tableName","任务单号","任务名", "所属模块","创建时间","form","ids" ],
    			colModel : [
    						PJ.grid_column("id", {key:true,width:80,hidden:true}),
    						PJ.grid_column("ywTableId", {key:true,hidden:true}),
    						PJ.grid_column("tableName", {width:160}),
    						PJ.grid_column("taskNumber", {width:160}),
    						PJ.grid_column("activityName", {width:215}),
    						PJ.grid_column("name", {width:215}),
    						PJ.grid_column("create", {width:160}),
    						PJ.grid_column("form", {hidden:true}),
    						PJ.grid_column("ids", {hidden:true})
    					 ],
    			ondblClickRow:function(){
    				dcltoolbar.toolBar.find(".l-toolbar-item[toolbarid=clrw]").trigger("click");
    			}
    		}); 
    		--%>
    		//jqGrid 自适应
    	 	$(window).resize(function() {
    			resize();
    		});
    	 	/**/
    		$("#task_page_left").css("display","none");
    		$("#task_page_right").css("display","none");
		});
		function colNames(tableName){
			var names={};
			if(tableName=='IMPORT_PACKAGE_PAYMENT_ELEMENT'){
				names=['id','件号','应付','数量','比例','备注']
			}else if(tableName=='IMPORT_PACKAGE_ELEMENT'){
				names=['id','件号','描述','单位','数量','备注']
			}else{
				names=['id','序号','ITEM','件号','描述','单位','数量']
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
				         {name:'"remark"',index:'cr',sortable:true,width:270,editable:false,align:"left"}
						]
			}else if(tableName=='IMPORT_PACKAGE_ELEMENT'){
				models=[ 
				         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
				         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
				         {name:'description',index:'click',sortable:true,width:275,editable:false,align:"left"},
				         {name:'unit',index:'cr',sortable:true,width:270,editable:false,align:"left"},
				         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"},
				         {name:'"remark"',index:'cr',sortable:true,width:270,editable:false,align:"left"}
						]
			}else{
				models=[ 
				         {name:'id',index:'name',sortable:false,width:296,editable:false,align:"left",key:true,hidden:true},
				         {name:'number',index:'show',sortable:true,width:80,editable:false,align:"left"},
				         {name:'item',index:'show',sortable:true,width:103,editable:false,align:"left"},
				         {name:'partNUmber',index:'show',sortable:true,width:253,editable:false,align:"left"},
				         {name:'description',index:'click',sortable:true,width:275,editable:false,align:"left"},
				         {name:'unit',index:'cr',sortable:true,width:270,editable:false,align:"left"},
				         {name:'amount',index:'cr',sortable:true,width:270,editable:false,align:"left"}
						]
			}
			return models;
		}
		
		function reloadGroupTask()
		{
			PJ.grid_reload(dcl);
		}
		
		
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
	 	function resize(){
	 		dcl.setGridWidth(PJ.getCenterWidth());
	 		dcl.setGridHeight(PJ.getCenterHeight() - 112);
		} /**/
	</script>
	<style>
		#cb_task_grid{
			margin:0
		}
	</style>
</head>
<body style="overflow: hidden;">
	<div id="div_main">
		<div position="center">
			<div id="task_toolbar"></div>
		    <table id="task_grid"></table>
		    <div id="task_page"></div>
		    <table id="list2"></table>
					<div id="pager2"></div>
		</div>
	</div>
</body>
</html>