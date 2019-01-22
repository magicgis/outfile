(function(factory) {
	if (typeof define === "function" && define.amd) {
		define([ "jquery", "./jquery.validate" ], factory);
	} else {
		factory(jQuery);
	}
}(function($) {
	(function() {

		function stripHtml(value) {
			// remove html tags and space chars
			return value.replace(/<.[^<>]*?>/g, " ").replace(/&nbsp;|&#160;/gi,
					" ")
			// remove punctuation
			.replace(/[.(),;:!?%#$'\"_+=\/\-“”’]*/g, "");
		}

		$.validator.addMethod("maxWords", function(value, element, params) {
			return this.optional(element)
					|| stripHtml(value).match(/\b\w+\b/g).length <= params;
		}, $.validator.format("Please enter {0} words or less."));

		$.validator.addMethod("minWords", function(value, element, params) {
			return this.optional(element)
					|| stripHtml(value).match(/\b\w+\b/g).length >= params;
		}, $.validator.format("Please enter at least {0} words."));

		$.validator.addMethod("rangeWords", function(value, element, params) {
			var valueStripped = stripHtml(value), regex = /\b\w+\b/g;
			return this.optional(element)
					|| valueStripped.match(regex).length >= params[0]
					&& valueStripped.match(regex).length <= params[1];
		}, $.validator.format("Please enter between {0} and {1} words."));

		// 验证值小数位数不能超过两位
		$.validator.addMethod("decimal", function(value, element) {
		var decimal = /^-?\d+(\.\d{1,2})?$/;
		return this.optional(element) || (decimal.test(value));
		}, $.validator.format("数值格式有问题！(example: 12.88)"));
	}());

	/**
	 * addMethod 的第一个参数，是添加的验证方法的名字，这时是 af。
	 * addMethod 的第三个参数，是自定义的错误提示，这里的提示为:"必须是一个字母,且a-f"。
	 * addMethod 的第二个参数，是一个函数，这个比较重要，决定了用这个验证方法时的写法。
	 * 如果只有一个参数，直接写，比如 af:"a"，那么 a 就是这个唯一的参数，如果多个参数，则写在 [] 里，用逗号分开。
	 * 
	 * // 邮政编码验证   
	 * jQuery.validator.addMethod("isZipCode", function(value, element) {   
	 *   var tel = /^[0-9]{6}$/;
	 *	    return this.optional(element) || (tel.test(value));
	 *	}, "请正确填写您的邮政编码");
	 */
	
	
}));