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
			url:'<%=path%>/storage/exchangeimport/warnList',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-242,
			autoSrcoll:true,
			sortname : "co.order_date",
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var count = ret["amount"];
				var clientOrderElementId = ret["clientOrderElementId"];
				var partNumber = ret["partNumber"];
				var description = ret["description"];
				$("#amount").val(count);
				$("#clientOrderElementId").val(clientOrderElementId);
				$("#partNumber").val(partNumber);
				$("#description").val(description);
			},
			colNames :["订单明细id","客户","订单号","件号","描述","订单时间","未收数量"],
			colModel :[PJ.grid_column("clientOrderElementId", {key:true,hidden:true}),
			           PJ.grid_column("clientCode", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("orderNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("orderDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("amount",{sortable:true,width:80,align:'left'})
			          ]
		});

		//证书来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exchangeimport/Certifications',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#cert").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		//状态来源
		$.ajax({
			type: "POST",
			dataType: "json",
			url:'<%=path%>/storage/exchangeimport/Conditions',
			success:function(result){
				var obj = eval(result.message)[0];
				if(result.success){
					for(var i in obj){
						var $option = $("<option></option>");
						$option.val(obj[i].id).text(obj[i].code);
						$("#con").append($option);
					}
				}else{
					
						PJ.showWarn(result.msg);
				}
			}
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	PJ.grid_search(grid,'<%=path%>/storage/exchangeimport/warnList');
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 PJ.grid_search(grid,'<%=path%>/storage/exchangeimport/warnList');
			
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
			if($("#taxReturn").is(':checked')==true){
				postData.taxReturn=1;
			}else{
				postData.taxReturn=0;
			}
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
			<div id="searchdiv" style="width: 100%;height: 140px;">
				<form id="addForm">
					<input id="clientOrderElementId" class="hide" type="text" name="clientOrderElementId" />
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
												<select id="con" class="text" type="text" name="conditionId">
												</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>证书：</p></form:left>
								<form:right>
											<p>
												<select id="cert" class="text" type="text" name="certificationId">
												</select>
											</p>
								</form:right>
							</form:column>
							<form:column>
								<form:left><p>位置：</p></form:left>
								<form:right><p><input id="location" class="text" type="text" name="location" /> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>SN：</p></form:left>
								<form:right><p><input id="sn" class="text" type="text" name="sn" /> </p></form:right>
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column>
								<form:left><p>入库时间：</p></form:left>
								<form:right><p><input id="importDate" type="text" name="importDate"  class="tc" value="<fmt:formatDate value="${today }"  pattern="yyyy-MM-dd"/>"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>描述：</p></form:left>
								<form:right><p><input id="description" type="text" name="description" class="text"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>运单号：</p></form:left>
								<form:right><p><input id="shipNumber" type="text" name="shipNumber" class="text"/> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>件号：</p></form:left>
								<form:right><p><input id="partNumber" class="text" type="text" name="partNumber" alias="cie.PART_NUMBER" oper="cn"/></textarea> </p></form:right>
							</form:column>
							<form:column>
							<p style="padding-left:60px;">
								<input class="btn btn_orange" type="button" value="搜索" id="searchBtn"/>
							</p>
							</form:column>
						</form:row>
						<form:row columnNum="5">
							<form:column>
									<form:left><p>备注：</p></form:left>
									<form:right><p><textarea id="remark" type="text" name="remark" ></textarea> </p></form:right>
							</form:column>
							<form:column>
								<form:left><p>维修类型：</p></form:left>
								<form:right>
											<p>
												<select name="repairType" id="repairType">
													<option value="1000057">维修</option>
													<option value="1000058">交换</option>
												</select>
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