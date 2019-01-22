/**
 * 
 */
package com.naswork.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @since 2015-3-20 下午4:37:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class StringUtil extends org.apache.commons.lang.StringUtils{
	public static String delHTMLTag(String htmlStr) {
		if (StringUtils.isEmpty(htmlStr)) {
			htmlStr = "";
		}
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 获取pattern后的字符
	 * @param originalFilename
	 * @param pattern
	 * @return
	 * @since 2015-4-20 上午10:16:53
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static String substringAfter(String originalFilename, String pattern) {
		if (StringUtils.isNotEmpty(originalFilename)) {
			if (originalFilename.indexOf(pattern) != -1) {
				return originalFilename.substring(
						originalFilename.indexOf(pattern) + pattern.length(),
						originalFilename.length());
			}
		}
		return originalFilename;
	}

	/**
	 * 多个字符串连接
	 * 
	 * @param params
	 * @return
	 * @since 2015-4-20 上午10:09:42
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static String join(Object... params) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Object str : params) {
			stringBuffer.append(String.valueOf(str));
		}
		return stringBuffer.toString();
	}
}
