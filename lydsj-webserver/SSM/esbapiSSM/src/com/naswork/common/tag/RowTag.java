/**
 * 
 */
package com.naswork.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 行
 * 
 * @since 2016年5月5日 下午8:08:49
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class RowTag extends TagSupport {
	private static final long serialVersionUID = 2327716125934722538L;
	private Boolean sameWidth = true;// 等分
	private Integer columnNum = 1;// 如果等分,分成多少个,默认1个
	private String cssName;

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			StringBuffer html = new StringBuffer();
			html.append("<div class=\"row");
			if (StringUtils.isNotEmpty(cssName)) {
				html.append(" ").append(cssName);
			}
			html.append("\">");
			out.write(html.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			StringBuffer html = new StringBuffer();
			html.append("</div>");
			out.write(html.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public Boolean getSameWidth() {
		return sameWidth;
	}

	public void setSameWidth(Boolean sameWidth) {
		this.sameWidth = sameWidth;
	}

	public Integer getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(Integer columnNum) {
		this.columnNum = columnNum;
	}

	public String getCssName() {
		return cssName;
	}

	public void setCssName(String cssName) {
		this.cssName = cssName;
	}
}
