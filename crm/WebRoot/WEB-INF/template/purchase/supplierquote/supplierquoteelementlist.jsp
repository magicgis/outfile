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
		PJ.datepicker('validity');

		layout = $("#layout_main").ligerLayout();
		
		$("#submit").click(function(){
			var Id =$("#id").val();
			var url = '<%=path%>/supplierquote/uploadExcel?id='+${Id}+'&editType='+ getFormData().toString();
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'file',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	var da = eval(data)[0];
	            	if(da.flag==true){
		            	PJ.success(da.message);
		            	closeoropen();
		            	PJ.grid_reload(grid);
	            	}
                    // else{
                    //     var msg = da.message;
                    //     console.log(msg);
                    //     if(msg== 'type1'){
                    //         console.log(msg);
                    //         PJ.alert("请填好询价单号再上传");
                    //     }else if(msg == 'type2'){
                    //         console.log(msg);
                    //         PJ.alert("询价单号不一致");
                    //     }
	            	else{
	            		iframeId = 'errorframe'
	                		PJ.topdialog(iframeId, '错误信息', '<%=path%>/supplierquote/toUnknow',
	                				undefined,function(item,dialog){
	    		            			$.ajax({
	    									url: '<%=path%>/supplierquote/deleteData',
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
		$("#pdfsubmit").click(function(){
			var ret = PJ.grid_getSelectedData(grid);
			var id =ret['id'];
			var url = '<%=path%>/supplierquote/uploadPdf?id='+id;
			//批量新增来函案源
	   	 	$.ajaxFileUpload({  
	            url: url,
	            secureuri:false,
	            type: 'POST',
	            fileElementId:'pdffile',
	            dataType: "text",
	            data: '',
	            success: function (data, status) {
	            	var da = eval(data)[0];
	            	if(da.flag==true){
		            	PJ.success(da.message);
		            	closeoropen();
		            	PJ.grid_reload(grid);
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
				id:'add',
				icon : 'add',
				text : '新增',
				click: function(){
					var iframeId="ideaIframe5";
					PJ.topdialog(iframeId, '新增供应商报价明细', '<%=path%>/supplierquote/toAddElement?id='+'${Id}+'+'&supplierInquiryId='+'${supplierInquiryId}',
							function(item,dialog){
								 var postData=top.window.frames[iframeId].getFormData();	 							
								 $.ajax({
										url: '<%=path%>/supplierquote/addElement?id='+'${Id}',
										data: postData,
										type: "POST",
										loading: "正在处理...",
										success: function(result){
												if(result.success){
													PJ.success(result.message);
													dialog.close();
													PJ.grid_reload(grid);
												} else {
													PJ.error(result.message);
													dialog.close();
												}		
										}
									});
							PJ.grid_reload(grid);}
						   ,function(item,dialog){PJ.grid_reload(grid);dialog.close();}, 1000, 500, true,"新增");
				}
			 },
			 {
				icon : 'process',
				text : '修改',
				click : function(){
					var ret = PJ.grid_getSelectedData(grid);
					var iframeId = 'supplierquoteelementFrame';
					var clientInquiryQuoteNumber = ret["clientInquiryQuoteNumber"];
					var supplierQuoteStatusValue = ret["supplierQuoteStatusValue"];
					var conditionCode = ret["conditionCode"];
					var certificationCode = ret["certificationCode"]; 
					var conditionValue = ret["conditionValue"];
					var certificationValue = ret["certificationValue"]; 
					var id = ret["id"]; 
						 	PJ.topdialog(iframeId, '采购 - 修改供应商报价明细','<%=path%>/supplierquote/editsupplierquoteelementdate?id='+id
						 			+'&supplierInquiryQuoteNumber='+'${supplierInquiryQuoteNumber}'+'&clientInquiryQuoteNumber='+clientInquiryQuoteNumber
						 			+'&supplierQuoteStatusValue='+supplierQuoteStatusValue+'&conditionCode='+conditionCode+'&certificationCode='+certificationCode
						 			+'&conditionValue='+conditionValue+'&certificationValue='+certificationValue,
								function(item, dialog){
										 var postData=top.window.frames[iframeId].getFormData();	 							
										 $.ajax({
											    url: '<%=path%>/supplierquote/editsupplierquoteelement?id='+id,
												data: postData,
												type: "POST",
												loading: "正在处理...",
												success: function(result){
														if(result.success){
															PJ.success(result.message);
															PJ.grid_reload(grid);
															dialog.close();
														} else {
															PJ.error(result.message);
															dialog.close();
														}		
												}
											});
								},function(item,dialog){dialog.close();}, 1000, 500, true,'修改');
		  			}
				},
				{
					id:'search',
					icon : 'search',
					text : '展开文件上传',
					click: function(){
						closeoropen();
					
					}
				},{
					id:'up',
					icon : 'up',
					text : '件号附件上传',
					click: function(){
						var ret = PJ.grid_getSelectedData(grid);
						var quotePartNumber=ret['quotePartNumber'];
						var clientInquiryId=ret['clientInquiryId'];
						$("#fjName").val(quotePartNumber);
						$("#supplierCode").val("${supplierCode}");
						var businessKey={};
						businessKey['businessKey'] ='client_quote_view'+'.id.'+clientInquiryId,
						PJ.uploadAttachment(null,businessKey,function(result){
							if(result && result.id){
								PJ.success("上传成功", function(){
									if(parent.afterUpload){
										parent.afterUpload(result);
									}
								})
							}
						}, {});
					}
				}
			 ]
		});
		grid = PJ.grid("list", {
			rowNum: 20,
			url: "<%=path%>/supplierquote/querysupplierquoteelement?Id="+"${Id}",
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight() - 182,
			aftersavefunc:function(rowid,result){
				PJ.grid_reload(grid);
			},
			onSelectRow:function(rowid,result){
				var ret = PJ.grid_getSelectedData(grid);
				var conditionCode = ret["conditionCode"];
				var certificationCode= ret["certificationCode"];
				var supplierQuoteStatusValue = ret["supplierQuoteStatusValue"];
				var conditionId = ret["conditionId"];
				var certificationId= ret["certificationId"];
				var supplierQuoteStatusId = ret["supplierQuoteStatusId"];
				var conditionValue = ret["conditionValue"];
				$("#conditionCode").val(conditionCode);
				$("#certificationCode").val(certificationCode);
				$("#supplierQuoteStatusValue").val(supplierQuoteStatusValue);
				var postData = {};
				postData.id = rowid;
				//证书
			 	$.ajax({
					type: "POST",
					data: postData,
					url:'<%=path%>/supplierquote/findcert?certificationId='+certificationId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							cert =certificationId+":"+certificationCode+";"+ result.message;
						}
					}
				}); 
				
			 	//状态
			 	$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/supplierquote/findcond?conditionId='+conditionId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							cond =conditionId+":"+conditionCode+";"+ result.message;
						}
					}
				}); 
			 	
			 	//状态
			 	$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/supplierquote/findsqstauts?supplierQuoteStatusId='+supplierQuoteStatusId+'&type='+"onlineedit",
					success:function(result){
						if(result.success){
							status =supplierQuoteStatusId+":"+supplierQuoteStatusValue+";"+ result.message;
						}
					}
				}); 
				
			},
			autoSrcoll:true,
			shrinkToFit:false,
			inLineEdit: true,
			editurl:'<%=path%>/supplierquote/updatesupplierquoteelement',
			sortname : "cie.item",
			sortorder : "asc",
			colNames : [ "clientInquiryId","id","状态id","证书id","状态id","状态value","证书value","状态value",
			             "询价id","序号","csn","主件号", "件号", "描述", "单位","数量","MOQ", "单价","年内最低报价","周期",
			             "状态", "证书", "报价状态","有效期","Location","备注","Warranty","SN","TagSrc","TagDate","trace","更新时间"],
			colModel : [PJ.grid_column("clientInquiryId", {sortable:true,hidden:true}),
			            PJ.grid_column("id", {sortable:true,hidden:true,editable:true,key:true}),
			            PJ.grid_column("conditionId", {sortable:true,hidden:true}),
			            PJ.grid_column("certificationId", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierQuoteStatusId", {sortable:true,hidden:true}),
			            PJ.grid_column("conditionValue", {sortable:true,hidden:true}),
			            PJ.grid_column("certificationValue", {sortable:true,hidden:true}),
			            PJ.grid_column("supplierQuoteStatusValue", {sortable:true,hidden:true}),
			            PJ.grid_column("clientInquiryQuoteNumber", {sortable:true,hidden:true}),
			            PJ.grid_column("item", {sortable:true,width:50,key:true,align:'left'}),
			            PJ.grid_column("csn", {sortable:true,width:50,key:true,align:'left'}),
			            PJ.grid_column("mainPartNumber",{sortable:true,width:150,align:'left'}),
			            PJ.grid_column("quotePartNumber", {sortable:true,width:150,align:'left'}),
						PJ.grid_column("quoteDescription", {sortable:true,width:250,editable:true,align:'left'}),
						PJ.grid_column("quoteUnit", {sortable:true,width:50,editable:true,align:'left'}),
						PJ.grid_column("quoteAmount", {sortable:true,width:50,editable:true,align:'left'}),
						PJ.grid_column("moq", {sortable:true,width:50,editable:true,align:'left'}),
						PJ.grid_column("price", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("latestPrice", {sortable:true,width:90,editable:true,align:'left'}),
						PJ.grid_column("leadTime", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("conditionCode", {sortable:true,width:50,editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								return cond;
							}
								/* "40:FN-全新;41:NE-新品;42:NS-库存品;43:SV-可用件;44:OH-大修件;45:AR-拆机件;46:RE-修理" */}
						,align:'left'}),
						PJ.grid_column("certificationCode", {sortable:true,width:120,editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								return cert;
								
							}
								/* "50:OEM COC-原厂合格证;51:FAA 8130-3-FAA 8130-3;52:EASA Form One-EASA Form One;53:Vendor COC-Vendor COC;54:Other-Other" */}
						,align:'left'}),
						PJ.grid_column("supplierQuoteStatusValue", {sortable:true,width:80,editable:true,
							edittype:"select",formatter:"",
							editoptions:{value:
								function(){
								return status;
							}
								/* "70:有效;71:失效;72:错误" */}
						,align:'left'}),
						PJ.grid_column("validity", {sortable:true,width:100,align:'left',editable:false,
				        	   editoptions:{ 
				        		   dataInit:function(el){ 
				        		     $(el).click(function(){ 
				        		       WdatePicker(); 
				        		     }); 
				        		   } 
				        	   } }),
						PJ.grid_column("location", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("quoteRemark", {sortable:true,width:200,editable:true,align:'left'}),
						PJ.grid_column("warranty", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("serialNumber", {sortable:true,width:100,editable:true,align:'left'}),
						PJ.grid_column("tagSrc", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("tagDate", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("trace", {sortable:true,width:80,editable:true,align:'left'}),
						PJ.grid_column("updateTimestamp", {sortable:true,width:200,align:'left'})
						]
		});
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#uploadBox").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-192);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
	});
	
	function closeoropen(){
	$("#uploadBox").toggle(function(){
		var display = $("#uploadBox").css("display");
		if(display=="block"){
			$("#toolbar > div[toolbarid='search'] > span").html("收起文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-182);
		}else{
			$("#toolbar > div[toolbarid='search'] > span").html("展开文件上传");
			grid.setGridHeight(PJ.getCenterHeight()-142);
		}
	});
	}
	function add(){
		/* var ret = PJ.grid_getSelectedData(grid);
		var iframeId = 'clientquoteFrame';
		var clientInquiryElementId = ret["clientInquiryElementId"];
		var clientQuoteId = ret["clientQuoteId"];
		var inquiryPartNumber = ret["partNumber"];
		var inquiryDescription = ret["description"];
		var inquiryAmount = ret["amount"];
		var inquiryUnit = ret["unit"];
		var inquiryRemark = ret["inquiryRemark"];
		var supplierCode = ret["supplierCode"];	  */
	 	PJ.topdialog(iframeId, '采购 - 供应商报价明细文件上传','<%=path%>/clientquote/addquoteelement', 			
	 	function(item,dialog){dialog.close();},function(item,dialog){dialog.close();}, 1000, 500, true);
		}
	
	function getFormData(){
		 var data = $("#form").serialize();
		 return data;
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
<input type="text" class="hide" name="conditionCode" id="conditionCode" value="">
<input type="text" class="hide" name="certificationCode" id="certificationCode" value="">
<input type="text" class="hide" name="supplierQuoteStatusValue" id="supplierQuoteStatusValue" value="">
<input type="text" class="hide" name="supplierInquiryId" id="supplierInquiryId" value="${supplierInquiryId}">
	<div id="layout_main">
		<div position="center" title="供应商询价单号:   ${supplierInquiryQuoteNumber}    币种: ${supplierQuoteElement.value} ">
		<div id="toolbar"></div>
			<div id="uploadBox" >
	<form id="form" name="form">
	 	<input type="hidden" name="id" id="id" value="${id}"/>
			<form:row columnNum="2">
				<form:column width="120">
					<form:left><p style="line-height: 30px;">excel批量导入</p></form:left>
					<form:right>
						<p><input type="file" value="" id="file" name="file"/>&nbsp;
						   <input type="button" id="submit" value="上传"/>
						</p>
					</form:right>
				</form:column>
				<%-- <form:column width="120">
					<form:left><p style="line-height: 30px;">PDF上传</p></form:left>
					<form:right>
						<p><input type="file" value="" id="pdffile" name="pdffile"/>&nbsp;
						   <input type="button" id="pdfsubmit" value="上传"/>
						</p>
					</form:right>
				</form:column> --%>
			</form:row>
	            
	 </form>
	</div>
		
			<table id="list"></table>
			<div id="pager1"></div>
		</div>
	</div>
    <div class="hide" id="tmpBox">
    </div>
    	<div class="table-box" style="display: none;width:330px" id="uploadDiv" >
    	<input type="text" name="supplierCode" id="supplierCode" class="hide" value="${supplierCode}"/>
   			<form:row>
    			<form:column>
    				<form:left>附件名称：</form:left>
    				<form:right><input type="text" name="fjName" id="fjName" class="input" style="width: 150px"/></form:right>
    			</form:column>
    		</form:row>
    		<form:row>
    			<form:column>
    				<form:left>附件文件：</form:left>
    				<form:right><input type="file" name="file" id="uploadFile"style="width: 150px" /></form:right>
    			</form:column>
    		</form:row>
   		</div>
</body>
</html>
