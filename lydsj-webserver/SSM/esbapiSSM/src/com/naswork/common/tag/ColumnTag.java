/**
 * 
 */
package com.naswork.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @since 2016年5月5日 下午8:19:31
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ColumnTag extends TagSupport {
	private static final long serialVersionUID = 6186356471349742361L;
	private Boolean frozenLeft = true;//是否左边定宽
	private Integer width = 100;//宽度
	private String cssName;

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			//取得父标签
			if(getParent() instanceof RowTag){
				RowTag rowTag = (RowTag) getParent();
				Boolean sameWidth = rowTag.getSameWidth();
				Integer columnNum = rowTag.getColumnNum();
				if(sameWidth){
					this.cssName = "col-"+12/columnNum;
				}
			}else if(getParent() instanceof ColumnRight){
				this.cssName = "col-r-box";
			}
			StringBuffer html = new StringBuffer();
			html.append("<div class=\""+this.cssName+"\">");
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

	public Boolean isFrozenLeft() {
		return frozenLeft;
	}

	public void setFrozenLeft(Boolean frozenLeft) {
		this.frozenLeft = frozenLeft;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getCssName() {
		return cssName;
	}

	public void setCssName(String cssName) {
		this.cssName = cssName;
	}
}
