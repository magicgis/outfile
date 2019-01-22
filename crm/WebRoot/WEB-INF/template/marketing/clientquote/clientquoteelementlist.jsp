<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>明细列表</title>
<c:import url="/WEB-INF/template/common/resource.jsp"></c:import>

<script type="text/javascript">
 	//-- Set Attribute
 	
	var layout, grid;
	$(function() {
		layout = $("#layout_main").ligerLayout();
		
		$("#submit").click(function(){
			
			var url = '<%=path%>/clientquote/uploadExcel?clientinquiryquotenumber='+'${client_inquiry_quote_number}';
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            //evel:'JJSON.parse',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	//var message = decodeURI(data);
	            	//var a = decodeURI(data);
	            	//var da = jQuery.parseJSON(jQuery(data).text());
	            	var da = eval(data)[0];
	            	//var falg = data.flag;
	            	if(da.flag==true){
	            		console.log(da.message);
		            	PJ.success(da.message);
		            	closeoropen();
		            	PJ.grid_reload(grid);
	            	}
	            	// else{
	            	//     var msg = da.message;
	            	//     console.log(msg);
	            	//     if(msg== 'type1'){
	            	//         console.log(msg);
					// 		PJ.alert("请填好询价单号再上传");
					// 	}else if(msg == 'type2'){
                    //         console.log(msg);
                    //         PJ.alert("询价单号不一致");
						 else{
	            	        // console.log(msg);
                            iframeId = 'errorframe'
                            PJ.topdialog(iframeId, '错误信息', '<%=path%>/clientquote/toUnknow',
                                undefined,function(item,dialog){
                                    $.ajax({
                                        url: '<%=path%>/clientquote/deleteData',
                                        type: "POST",
                                        loading: "正在处理...",
                                        success: function(result){
                                        }
                                    });
                                    dialog.close();}, 1000, 500, true);
                            closeoropen();
						}
	            	},
	            error: function (data, status, e) {
	            	PJ.error("上传异常！");
	            	closeoropen();
	            }  
	        });  
		});	

		//数据区的工具栏
		$("#toolbar").ligerToolBar({
			items : [ 
			{
				icon : 'add',
			    text : '新增',
				click : function(){
				add();
			}
			},        
		 	{
				icon : 'edit',
				text : '修改',
				click : function(){
					edit();
				}
			}, {
				id:'down',
				icon : 'down',
				text : '件号附件下载',
				click: function(){
					var iframeId="uploadframe";
					PJ.topdialog(iframeId, ' 件号附件下载 ', '<%=path%>/clientquote/partfile?id='+'${clientInquiryId}'+'&type='+"marketing",
							undefined,function(item,dialog){dialog.close();}, 1000, 500, true);
				}
		 },
			{
				id:'search',
				icon : 'search',
				text : '收起文件上传',
				click: function(){
					$("#uploadBox").toggle(function(){
						var display1 = $("#uploadBox").css("display");
						var display2 = $("#searchdiv").css("display");
						if(display1=="block"&&display2=="block"){
							$("#toolbar > div[toolbarid='search'] > span").html("收起文件上传");
							grid.setGridHeight(PJ.getCenterHeight()-280);
						}else if(display1=="block"){
							$("#toolbar > div[toolbarid='search'] > span").html("收起文件上传");
							grid.setGridHeight(PJ.getCenterHeight()-220);
						}else if(display1=="none"&&display2=="block"){
							$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
							grid.setGridHeight(PJ.getCenterHeight()-200);
						}
						else{
							$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
							grid.setGridHeight(PJ.getCenterHeight()-200);
						}
					});
				}
			},
		     {
				id:'search1',
				icon : 'search',
				text : '展开搜索',
				click: function(){
					$("#searchdiv").toggle(function(){
						var display1 = $("#uploadBox").css("display");
						var display2 = $("#searchdiv").css("display");
						if(display1=="block"&&display2=="block"){
							$("#toolbar > div[toolbarid='search1'] > span").html("收起搜索");
							grid.setGridHeight(PJ.getCenterHeight()-280);
						}else if(display2=="block"){
							$("#toolbar > div[toolbarid='search1'] > span").html("收起搜索");
							grid.setGridHeight(PJ.getCenterHeight()-230);
						}
						else if(display1=="block"&&display2=="none"){
							$("#toolbar > div[toolbarid='search1'] > span").html("展开搜索");
							grid.setGridHeight(PJ.getCenterHeight()-280);
						}
						else{
							$("#toolbar > div[toolbarid='search1'] > span").html("展开搜索");
							grid.setGridHeight(PJ.getCenterHeight()-200);
						}
					});
			}
		} ]
		});
		grid = PJ.grid("list", {
			rowNum: 20,
			url: "<%=path%>/clientquote/clientquoteelementdate?clientquoteid="+"${clientquoteid}",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 220,
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			sortname : "csn,item",
			sortorder : "asc",
			editurl:'<%=path%>/clientquote/updateclientquoteelement',
			colNames : [ "询价id","报价id","询价备注","明细id","序号", "件号","CSN","是否主件号","主件号","描述","数量", "单位","黑名单",
			             "供应商", "件号", "备注","单位", "数量","MOQ", "单价","手续费","证书","状态","location","运费","到货周期","MOV","warranty","serialNumber","tagSrc","tagDate","trace",
			             "数量","单价", "总价","佣金","银行费用","location","利润率","到货周期","备注","供应商报价明细id","客户报价明细id","更新时间"],
			colModel : [PJ.grid_column("clientInquiryElementId", {hidden:true}),
			            PJ.grid_column("clientQuoteId", {hidden:true}),
			            PJ.grid_column("inquiryRemark", {hidden:true}),
			            PJ.grid_column("elementId", {hidden:true}),
			            PJ.grid_column("item", {sortable:true,width:50,key:true,align:'left'}),
			            PJ.grid_column("partNumber", {width:150,align:'left'}),
			            PJ.grid_column("csn", {width:50,align:'left',sortable:true}),
			            PJ.grid_column("isMain",{sortable:true,width:60,align:'left',
				        	   formatter: function(value){
									if (value==2) {
										return "是";
									} else if(value==1){
										return "否";							
									}else {
										return "";
									}
								}   
				           }),
				           PJ.grid_column("mainPartNumber",{sortable:true,width:150,align:'left'}),
						PJ.grid_column("description", {width:250,align:'left'}),
						PJ.grid_column("amount", {width:50,align:'left'}),
						PJ.grid_column("unit", {width:50,align:'left'}),
						 PJ.grid_column("isBlacklist",{sortable:true,width:50,align:'left',
				        	   formatter: function(value){
									if (value==1||value=="是") {
										return "是";
									} else if(value==0||value=="否"){
										return "否";							
									}else {
										return "";
									}
								}
				           }),
						PJ.grid_column("supplierCode", {width:60,align:'left'}),
						PJ.grid_column("quotePartNumber", {width:200,align:'left'}),
						PJ.grid_column("quoteRemark", {width:450,align:'left'}),
						PJ.grid_column("quoteUnit", {width:50,align:'left'}),
						PJ.grid_column("supplierQuoteAmount", {width:50,align:'left'}),
						PJ.grid_column("moq", {width:50,align:'left'}),
						PJ.grid_column("supplierQuotePrice", {width:80,align:'left'}),
						PJ.grid_column("counterFee", {width:80,align:'left'}),
						PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("conditionCode", {sortable:true,width:50,align:'left'}),
						PJ.grid_column("location", {width:80,align:'left'}),
						PJ.grid_column("freight", {sortable:true,width:60}),
						PJ.grid_column("leadTime", {width:50,align:'left'}),
						PJ.grid_column("mov", {width:100,align:'left'}),
						PJ.grid_column("warranty", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("serialNumber", {sortable:true,width:100,editable:true,align:'left'}),
						PJ.grid_column("tagSrc", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("tagDate", {sortable:true,width:80,align:'left'}),
						PJ.grid_column("trace", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("clientQuoteAmount", {width:50,editable:true,align:'left'}),
						PJ.grid_column("clientQuotePrice", {width:80,editable:true,align:'left'}),
						PJ.grid_column("countprice", {width:80,align:'left'}),
						PJ.grid_column("fixedCost", {width:60,align:'left'}),
						PJ.grid_column("bankCharges", {width:70,align:'left'}),
						PJ.grid_column("quoteLocation", {width:80,editable:true,align:'left'}),
						PJ.grid_column("profitMargin", {width:60
							 ,formatter:function(value){
								if(value){
									return value+"%";
								}
								else{
									return value;
								}
							} 		
						,align:'left'}),
						PJ.grid_column("cqeLeadTime", {width:50,editable:true,align:'left'}),
						PJ.grid_column("clientQuoteRemark", {width:450,editable:true,align:'left'}),
						PJ.grid_column("supplierQuoteElementId", {hidden:true,editable:true,align:'left'}),
					    PJ.grid_column("id", {hidden:true,editable:true,align:'left'}),
						PJ.grid_column("quoteupDatTimestamp", {width:180,align:'left'})
						]
		});
		
		 grid.jqGrid('setGroupHeaders', { 
		 	useColSpanStyle: true, 
		 	groupHeaders:[ 
		 		{startColumnName: 'clientInquiryElementId', numberOfColumns: 13, titleText: '<div align="center"><span>客户询价</span></div>'},
		 		{startColumnName: 'supplierCode', numberOfColumns: 19, titleText: '<div align="center"><span>供应商报价</span></div>'},
		 		{startColumnName: 'clientQuoteAmount', numberOfColumns: 12, titleText: '<div align="center"><span>客户报价</span></div>'}
		 	] 
		}); 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-222);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-182);
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/clientquote/clientquoteelementdate?clientquoteid='+'${clientquoteid}'+'&supplier_quote='+$("#supplier_quote").val());
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/clientquote/clientquoteelementdate?clientquoteid='+'${clientquoteid}'+'&supplier_quote='+$("#supplier_quote").val());
		 });
		 
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		
	});
	
	
	
	function closeoropen(){
	$("#uploadBox").toggle(function(){
		var display = $("#uploadBox").css("display");
		if(display=="block"){
			$("#toolbar > div[toolbarid='search'] > span").html("收起文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-220);
		}else{
			$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-182);
		}
	});
	}
	
	function add(){
		var ret = PJ.grid_getSelectedData(grid);
		var iframeId = 'clientquoteFrame';
		var clientInquiryElementId = ret["clientInquiryElementId"];
		var clientQuoteId = ret["clientQuoteId"];
	//	var fixedCost = ret["fixedCost"];
	/* 	var inquiryPartNumber = ret["partNumber"];
		var inquiryDescription = ret["description"];
		var inquiryAmount = ret["amount"];
		var inquiryUnit = ret["unit"];
		var inquiryRemark = ret["inquiryRemark"]; */
		var supplierCode = ret["supplierCode"];
		 if(supplierCode=="没有报价"){
			PJ.warn('没有报价不能进行新增');
			return;
		}
		if(supplierCode==""||supplierCode==null){
			PJ.warn('不能进行此操作');
			return;
		}
		if(supplierCode=="有供应商报价"||supplierCode=="有历史报价"){
		 
	 	PJ.topdialog(iframeId, '销售 - 新增客户报价明细','<%=path%>/clientquote/addquoteelement?clientQuoteId='+clientQuoteId+ 			
	 			'&client_inquiry_quote_number='+"${client_inquiry_quote_number}"+
	 			'&clientInquiryElementId='+clientInquiryElementId+
	 			'&optType='+"add", 
	 			//'&optType='+"add"+'&fixedCost='+'${fixedCost}',
	 			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);},  1200, 500, true);
		 }else{
			PJ.warn('当前状态仅能做修改操作');
			return;
		} 
		
		}
	
	function edit(){
		var ret = PJ.grid_getSelectedData(grid);
		var iframeId = 'clientquoteFrame';
		var clientInquiryElementId = ret["clientInquiryElementId"];
		//var fixedCost = ret["fixedCost"];
		var clientQuoteElementId = ret["id"];
		var clientQuoteAmount = ret["clientQuoteAmount"];
		var clientQuotePrice = ret["clientQuotePrice"];
		var Remark = ret["clientQuoteRemark"];
		var clientQuoteId = ret["clientQuoteId"];
		/* var inquiryPartNumber = ret["partNumber"];
		var inquiryDescription = ret["description"];
		var inquiryAmount = ret["amount"];
		var inquiryUnit = ret["unit"];
		var inquiryRemark = ret["inquiryRemark"]; */
		var supplierCode = ret["supplierCode"];
		var supplierQuoteElementId = ret["supplierQuoteElementId"];
		var location = ret["location"];
		 if(supplierCode=="没有报价"){
			PJ.warn('没有报价不能进行修改');
			return;
		}
		if(supplierCode==""||supplierCode==null){
			PJ.warn('不能进行此操作');
			return;
		}
		if(supplierCode=="有供应商报价"||supplierCode=="有历史报价"){
			PJ.warn('当前状态仅能做新增操作');
			return;
		 }else{
			 	PJ.topdialog(iframeId, '销售 - 修改客户报价明细','<%=path%>/clientquote/addquoteelement?clientQuoteId='+clientQuoteId+			 			
			 			'&client_inquiry_quote_number='+"${client_inquiry_quote_number}"+
			 			'&clientInquiryElementId='+clientInquiryElementId+
			 			'&optType='+"edit"+'&Price='+clientQuotePrice+
			 			'&id='+clientQuoteElementId+'&Amount='+clientQuoteAmount+
			 			'&supplierQuoteElementId='+supplierQuoteElementId+
			 			'&Remark='+encodeURIComponent(Remark)+
			 			'&location='+location, 	
			 		//	'&location='+location+'&fixedCost='+'${fixedCost}',	 			
			 			undefined,function(item,dialog){dialog.close();PJ.grid_reload(grid);}, 1200, 500, true);
		} 
		
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

<body style="padding:3px">
<input type="text" class="hide" name="client_inquiry_quote_number" value="${client_inquiry_quote_number}">
	<div id="layout_main">
		<div position="center" title="询价单号:  ${client_inquiry_quote_number}">
		<div id="toolbar"></div>
		<div id="searchdiv" style="width: 100%;height: 50px;display: none;">
				<form id="searchForm">
					<div class="search-box">
						<form:row columnNum="3">
							<form:column>
							<form:left>供应商报价状态</form:left>
								<form:right><p><select style="width: 100px"  id="supplier_quote" name="supplier_quote">
							            	<option value="">全部</option>
							            	<option value="0">没有报价</option>
							            	<option value="1">供应商有报价</option>
							            	<option value="2">有历史报价</option>
							            	</select>
							              </p>
								</form:right>
							</form:column>
						<form:column ><form:right>
						<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn">
								<input class="btn btn_blue" type="button" value="重置" id="resetBtn">
							</p></form:right>
							</form:column>
					</form:row>
					</div>
				</form>
			</div>
		<div id="uploadBox" >
	<form id="form" name="form">
			<form:row columnNum="2">
				<form:column width="120">
					<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
					<form:right>
						<p><input type="file" value="" id="file" name="file"/>&nbsp;
						   <input type="button" id="submit" value="上传"/>
						</p>
					</form:right>
				</form:column>
			</form:row>
	            
	 </form>
	</div>
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
</body>
</html>
