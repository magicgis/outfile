/**
 * 
 */
package com.naswork.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.naswork.utils.StringPrintWriter;

/**
 * 统一错误处理
 * @since 2016年3月3日 下午3:13:05
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ExceptionHandler implements HandlerExceptionResolver {
	private static Log logger = LogFactory.getLog(ExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		System.out.println("==============异常开始=============");
		logger.error("Catch Exception: ", ex);// 把漏网的异常信息记入日志
		System.out.println("==============异常结束=============");
		request.setAttribute("message", "出错了!<br/><br/><br/>"+ ex.getMessage());
		StringPrintWriter strintPrintWriter = new StringPrintWriter();
		ex.printStackTrace(strintPrintWriter);
		request.setAttribute("errorMsg", strintPrintWriter.getString());
		return new ModelAndView("/common/error");
	}

}
