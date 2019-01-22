/**
 * auth: giam
 * 2015/05/09 15:33
 */
var ComFun = {
	//-- 拼装时间
	assembleDate:function(form,name){
		var arr = [];
		$("#"+form).find("input").filter("input[name='"+name+"']").each(function(i){
			arr[i] =  $(this).val();
		});
		return arr.join("-");
	},
	//-- 时间输入框绑定时间插件,依赖WdatePicker
	bindDate:function(){
		$(".form-input-time").on("click",function(){WdatePicker()});
		$(".form-input-year").on("click",function(){WdatePicker({dateFmt:"yyyy"})});
		$(".form-input-month").on("click",function(){WdatePicker({dateFmt:"MM"})});
		$(".form-input-day").on("click",function(){WdatePicker({dateFmt:"dd"})});
		$(".form-input-hour").on("click",function(){WdatePicker({dateFmt:"HH"})});		
	},
	//-- 监听同字段输入框，补值
	listenInput:function(name){
		$("input[name='"+name+"']").on("input propertychange",function(){
			var val = $(this).val();
			$("input[name='sub_"+name+"']").each(function(){
				$(this).val(val);
			});
		})
	},
	//-- 文书表格排序
	xhSort:function($tbody){
		var $items = $tbody.find("tr");
		$items.each(function(i){
			$(this).find("input[name='xh']").val(++i);
		});
	}
};