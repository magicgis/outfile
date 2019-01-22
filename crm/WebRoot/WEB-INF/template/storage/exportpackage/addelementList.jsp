<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增出库明细管理 </title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId },
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-200,
			autoSrcoll:true,
			shrinkToFit:false,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var count = ret["storageAmount"];
				var importPackageElementId = ret["importPackageElementId"];
				$("#amount").val(count);
				$("#importPackageElementId").val(importPackageElementId);
				
			},
			colNames :["id","入库明细id","件号","描述","状态","证书","单位","数量","人民币价格","总价","位置","订单号","入库日期","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("importPackageElementId", {key:true,hidden:true}),
			           PJ.grid_column("partNumber", {sortable:true,width:150,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("certificationCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("storageAmount", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("basePrice",{sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}   
			           ,align:'left'}),
			           PJ.grid_column("totalBasePrice", {sortable:true,width:80,
			        	   formatter: function(value){
								if (value) {
									return value.toFixed(2);
								}
							}   
			           ,align:'left'}),
			           PJ.grid_column("location", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importDate", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("updateTimestamp",{sortable:true,width:150,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId });
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/exportpackage/addElementList?clientId='+${clientId });
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			grid.setGridHeight(PJ.getCenterHeight()-202);
		});
		
		$("#addBtn").click(function(){
			 var postData = getFormData();
			 var validate = getValidate();
			 if(validate){
				 $.ajax({
						type: "POST",
						dataType: "json",
						data: postData,
						url:'<%=path%>/storage/exportpackage/saveAddElement',
						success:function(result){
							if(result.success){
								PJ.success(result.message);
							}else{
								PJ.warn(result.message);
							}
						}
				});
			 }else{
				 PJ.warn("数据没有填写完整！");
			 }
		});
		
		$("#searchForm2").blur(function(){
			var text = $("#searchForm2").val();
			$("#searchForm2").val(trim(text));
		});
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	//获取表单数据
	function getFormData(){
		var $input = $("#form").find("input,textarea,select");
		var postData = {};
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
					
				postData[$(this).attr("name")] = $(this).val();
			});
			if($("#taxReturn").is(':checked')==true){
				postData.taxReturn=1;
			}else{
				postData.taxReturn=0;
			}
			return postData;
	}
	
	//-- 验证
	function getValidate(){
		return validate2({
			nodeName:"amount",
			form:"#form"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,textarea,span");var nodes = def.nodeName.split(",");var flag = true;var tip;
		$items.each(function(i){
			var name = $(this).attr("name");
			if(!name)return;
			for(var k in nodes){
				if(name === nodes[k]) flag = !!$(this).val() || !!$(this).text();//-- 空则返回true，不通过
				if(!flag){
	                $(this).addClass("input-error");
					tip = $(this).attr("data-tip");
					return false;
				}else $(this).removeClass("input-error");
			}
		});
			//if(!flag) PJ.tip("未填写"+tip);
		return flag;
	};
	
</script>

</head>

<body>
	<div id="layout1">
		<div position="center" title="新增出库单明细 ">
		<div id="toolbar"></div>
		<div id="uploadBox" >
			<form action="" id="form">
				<input class="hide" name="importPackageElementId" id="importPackageElementId"/>
				<input class="hide" name="exportPackageId" id="exportPackageId" value="${id }"/>
				<table style=" border-collapse:   separate;   border-spacing:   10px;">
					<tr>
						<td>出库单号</td>
						<td>${exportNumber }</td>
						<td>数量</td>
						<td><input class="text" name="amount" id="amount"/></td>
						<td><input class="btn btn_orange" type="button" value="新增" id="addBtn"/></td>
						<td></td>
					</tr>
				</table>
			</form>	
			<form id="searchForm">
				<form:row columnNum="3">
					<form:column>
						<form:left><p>件号：</p></form:left>
						<form:right><p><input id="searchForm2" class="text" type="text" name="partNumber" alias="ipe.part_number" oper="cn"/> </p></form:right>
					</form:column>
					<form:column >
						<p style="padding-left:10px;">
						 <input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
						 <input class="btn btn_blue" type="button" value="重置" id="resetBtn"/>
						</p>
					</form:column>
				</form:row>
				
			</form>
		</div>
			<table id="list1"></table>
			<div id="pager1"></div>
		</div>
	</div>
</body>
</html>