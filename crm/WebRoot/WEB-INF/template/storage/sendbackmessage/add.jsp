<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>采购-新增供应商订单</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<script type="text/javascript">
	
	var layout, grid;
	$(function() {
		PJ.datepicker('importDate');
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [	
			        ]
		});
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'<%=path%>/storage/sendbackmessage/exportList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-202,
			autoSrcoll:true,
			sortname : "co.order_date",
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var count = ret["amount"];
				var exportPackageElementId = ret["id"];
				var serialNumber = ret["serialNumber"];
				$("#amount").val(count);
				$("#exportPackageElementId").val(exportPackageElementId);
				$("#serialNumber").val(serialNumber);
			},
			colNames :["id","客户","件号","描述","数量","客户订单号","出库单号","供应商订单号","入库单号","SN"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("clientCode", {key:true,width:40,align:'left'}),
			           PJ.grid_column("partNumber", {key:true,width:100,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("amount", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("clientOrderNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("exportNumber", {sortable:true,width:80,editable:true,align:'left'}),
			           PJ.grid_column("supplierOrderNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("importNumber",{sortable:true,width:120,align:'left'}),
			           PJ.grid_column("serialNumber",{sortable:true,width:80,align:'left'})
			          ]
		});

		$.ajax({
			url:'<%=path%>/storage/sendbackmessage/statusList',
			type: "POST",
			dataType: "json",
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].value);
						$("#manageStatus").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		})
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/sendbackmessage/exportList');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/sendbackmessage/exportList');
			
		 });
		
		 //重置条件
		 $("#resetBtn").click(function(){
			 $("#searchForm")[0].reset();
		 });
		 
		//改变窗口大小自适应
		$(window).resize(function() {
			grid.setGridWidth(PJ.getCenterWidth());
			var display = $("#searchdiv").css("display");
			if(display=="block"){
				grid.setGridHeight(PJ.getCenterHeight()-242);
			}else{
				grid.setGridHeight(PJ.getCenterHeight()-142);
			}
		});
		
		//所有输入框的前后空格去掉
		$("input").blur(function(){
			var text = this.value;
			var id = "#"+this.id;
			$(id).val(trim(text));
		});
		
		
	});
	
	function trim(str){
		return $.trim(str);
	}
	
	//获取表单数据
	function getFormData(){
		var $input = $("#addForm").find("input,textarea,select");
		var postData = {};
			$input.each(function(index){
				if(!$(this).val()) {
					//PJ.tip("必填数据项没有填写完整");
					//flag = true;
					return;
				}
				postData[$(this).attr("name")] = $(this).val();
			});
			return postData;
	}
	
	//-- 验证
	function validate(){
		return validate2({
			nodeName:"location,description",
			form:"#addForm"
		});
	};
	
	//-- 验证，默认通过true,有空未填则返回false
	function validate2(opt){
		var def = {nodeName:"",form: ""};
		for(var k in opt) if(opt.hasOwnProperty(k) && def.hasOwnProperty(k)) def[k] = opt[k];
		var $items = $(def.form).find("input,select");var nodes = def.nodeName.split(",");var flag = true;var tip;
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
		<div position="center">
		<div id="toolbar"></div>
			<div id="searchdiv" style="width: 100%;height: 100px;">
				<form id="addForm">
					<input id="exportPackageElementId" class="hide" type="text" name="exportPackageElementId" />
					<div class="search-box">
						<form:row columnNum="5">
							<form:column>
								<form:left><p>数量：</p></form:left>
								<form:right><p><input id="amount" class="text" type="text" name="amount" /> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>状态：</p></form:left>
								<form:right>
											<p>
												<select id="manageStatus" class="text" type="text" name="manageStatus">
												</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
									<form:left><p>备注：</p></form:left>
									<form:right><p><textarea id="remark" type="text" name="remark" ></textarea> </p></form:right>
							</form:column>
							<form:column >
								<form:left><p>SN:</p></form:left>
								<form:right><p><input name="serialNumber" id="serialNumber" class="text" type="text"/> </p></form:right>
							</form:column>
							<form:column >
								<form:left><p>位置:</p></form:left>
								<form:right><p><input name="location" id="location" class="text" type="text"/> </p></form:right>
							</form:column>
						</form:row>
						<form:row columnNum="4">
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" name="partNumber" alias="ipe.part_number" oper="cn"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>订单：</p></form:left>
								<form:right><input id="orderNumber" class="text" type="text" name="orderNumber" alias="co.order_number or so.ORDER_NUMBER" oper="cn"/></form:right>
							</form:column>
							<form:column>
							</form:column>
							<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
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