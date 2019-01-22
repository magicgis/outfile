<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新增库存订单明细</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<jsp:include page="/WEB-INF/template/common/resource.jsp" />
<style>
/* .ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-top;
  padding-top:2px;
 } */
</style>

<script type="text/javascript">
	var layout, grid;
	$(function() {
		layout = $("#layout1").ligerLayout();
		
		$("#toolbar").ligerToolBar({
			items : [
			        ]
		});
		
		grid = PJ.grid("list1", {
			rowNum: 20,
			url:'',
			width : PJ.getCenterWidth(),
			height : PJ.getCenterHeight()-242,
			autoSrcoll:true,
			shrinkToFit:false,
			onSelectRow:function(rowid,status,e){
				var ret = PJ.grid_getSelectedData(grid);
				var partNumber =ret["partNumber"];
				var elementId = ret["elementId"];
				var certificationId =ret["certificationId"];
				var conditionId = ret["conditionId"];
				var unit =ret["unit"];
				var price = ret["price"];
				var description = ret["description"];
				$("#partNumber").val(partNumber);
				$("#elementId").val(elementId);
				$("#unit").val(unit);
				$("#price").val(price);
				$("#description").val(description);
				//状态
				$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/purchase/supplierorder/Conditions?conditionId='+conditionId,
					success:function(result){
						var obj = eval(result.message)[0];
						if(result.success){
							$("#conditionId").find("option").remove(); 
							for(var i in obj){
								var $option = $("<option></option>");
								$option.val(obj[i].id).text(obj[i].value);
								$("#conditionId").append($option);
							}
						}else{
							
								PJ.warn(result.msg);
						}
					}
				});
				
				//证书
				$.ajax({
					type: "POST",
					dataType: "json",
					url:'<%=path%>/purchase/supplierorder/Certifications?certificationId='+certificationId,
					success:function(result){
						var obj = eval(result.message)[0];
						if(result.success){
							$("#certificationId").find("option").remove(); 
							for(var i in obj){
								var $option = $("<option></option>");
								$option.val(obj[i].id).text(obj[i].value);
								$("#certificationId").append($option);
							}
						}else{
							
								PJ.warn(result.msg);
						}
					}
				});
			},
			colNames :["id","elementId","证书id","状态id","询价单号","供应商询价单号","件号","报价日期","报价价格","单位","周期","状态","证书","报价描述","备注","更新时间"],
			colModel :[PJ.grid_column("id", {key:true,hidden:true}),
			           PJ.grid_column("elementId", {key:true,hidden:true}),
			           PJ.grid_column("certificationId", {key:true,hidden:true}),
			           PJ.grid_column("conditionId", {key:true,hidden:true}),
			           PJ.grid_column("clientInquiryNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("supplierInquiryNumber", {sortable:true,width:140,align:'left'}),
			           PJ.grid_column("partNumber", {sortable:true,width:120,align:'left'}),
			           PJ.grid_column("quoteDate", {sortable:true,width:100,align:'left'}),
			           PJ.grid_column("price", {sortable:true,width:60,align:'left'}),
			           PJ.grid_column("unit", {sortable:true,width:40,align:'left'}),
			           PJ.grid_column("leadTime", {sortable:true,width:50,align:'left'}),
			           PJ.grid_column("conditionCode", {sortable:true,width:80,align:'left'}),
			           PJ.grid_column("certificationCode", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("description", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("remark", {sortable:true,width:130,align:'left'}),
			           PJ.grid_column("updateTimestamp", {sortable:true,width:100,align:'left'})
			           ]
		});
		
		// 绑定键盘按下事件  
		$(document).keypress(function(e) {
			// 回车键事件  
		    if(e.which == 13) {
		    	var biz = $("#biz").val();
				PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/addStorageElement?biz='+biz+'&supplierId='+${supplierId });
		    }  
		}); 
		
		//搜索
		 $("#searchBtn").click(function(){
			 var biz = $("#biz").val();
			 var part = $("#part").val();
			 PJ.grid_search(grid,'<%=path%>/purchase/supplierorder/addStorageElement?biz='+biz+'&supplierId='+${supplierId }+'&part='+part);
		 });
		
			//状态
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/purchase/supplierorder/Conditions',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].value);
							$("#conditionId").append($option);
						}
					}else{
						
							PJ.warn(result.message);
					}
				}
			});
			
			//证书
			$.ajax({
				type: "POST",
				dataType: "json",
				url:'<%=path%>/purchase/supplierorder/Certifications',
				success:function(result){
					var obj = eval(result.message)[0];
					if(result.success){
						for(var i in obj){
							var $option = $("<option></option>");
							$option.val(obj[i].id).text(obj[i].value);
							$("#certificationId").append($option);
						}
					}else{
						
							PJ.warn(result.message);
					}
				}
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
		
		$("#addBtn").click(function(){
			//var validate = validate();
			var postData = getFormData();
			if(validate){
				$.ajax({
					url: '<%=path%>/purchase/supplierorder/createStorageElement?clientOrderId='+${clientOrderId},
					data: postData,
					type: "POST",
					loading: "正在处理...",
					success: function(result){
							if(result.success){
								PJ.success(result.message);
							} else {
								PJ.error(result.message);
							}		
					}
				});
			}else{
				PJ.warn("请补全数据");
			}
			
		});
		
		//-- 验证
		function validate(){
			return validate2({
				nodeName:"price,amount,unit,leadTime",
				form:"#addForm"
			});
		};
	});
	
	
	
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
		<div position="center" title="新增库存订单明细">
		<div id="searchdiv" style="width: 100%;height: 130px;">
		<form id="addForm" action="">
			<input name="elementId" id="elementId" class="hide"/>
			<input name="biz" id="biz" class="hide" value="${biz }"/>
			<!-- <input name="certificationId" id="certificationId" class="hide"/>
			<input name="conditionId" id="conditionId" class="hide"/> -->
			
			<div class="search-box">
				<form:row columnNum="5">
					<form:column>
						<form:left><p>件号</p></form:left>
						<form:right><p><input id="partNumber" name="partNumber" class="text" /></p></form:right>
					</form:column>
					<form:column>
						<form:left><p>价格</p></form:left>
						<form:right><p><input id="price" name="price" class="text" /></p></form:right>
					</form:column>
					<form:column>
						<form:left><p>数量</p></form:left>
						<form:right><p><input id="amount" name="amount" class="text" /></p></form:right>
					</form:column>
					<form:column>
						<form:left><p>单位</p></form:left>
						<form:right><p><input id="unit" name="unit" class="text" /></p></form:right>
					</form:column>
					<form:column>
						<form:left><p>周期</p></form:left>
						<form:right><p><input id="leadTime" name="leadTime" class="text" /></p></form:right>
					</form:column>
					
				</form:row>
				<form:row columnNum="4">
					<form:column>
						<form:left><p>证书</p></form:left>
						<form:right><p>
											<select id="certificationId" name="certificationId">
											</select>
									</p>
						</form:right>
					</form:column>
					<form:column>
						<form:left><p>状态</p></form:left>
						<form:right><p>
											<select id="conditionId" name="conditionId">
											</select>
									</p>
						</form:right>
					</form:column>
					<form:column>
						<form:left><p>描述</p></form:left>
						<form:right><p><input name="description" id="description" class="text"/></p></form:right>
					</form:column>
					<form:column >
							<p style="padding-left:10px;">
								<input class="btn btn_orange" type="button" value="新增" id="addBtn"/>
							</p>
					</form:column>
				</form:row>
				<form:row columnNum="5">
					
					<form:column>
						<form:left><p>件号</p></form:left>
						<form:right><p><input id="part" name="part" class="text" alias="sqe.PART_NUMBER" oper="cn"/></p></form:right>
					</form:column>
					
					<form:column >
							<p style="padding-left:10px;">
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
	</div>
</body>
</html>