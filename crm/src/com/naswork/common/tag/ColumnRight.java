/**
 * 
 */
package com.naswork.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @since 2016年5月5日 下午8:21:01
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ColumnRight extends TagSupport {
	private static final long serialVersionUID = 5532428573875143062L;

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			//取得父标签
			ColumnTag columnTag = (ColumnTag) getParent();
			Boolean frozenLeft = columnTag.isFrozenLeft();
			Integer width = columnTag.getWidth();
			
			StringBuffer html = new StringBuffer();
			if(frozenLeft.booleanValue()){
				html.append("<div style='float:right;width:100%;'>");
				html.append("<div style='margin-left:"+(width.intValue()+10)+"px;'>");
			}else{
				html.append("<div class='col-right' style='position:relative;float:right;width:"+width+"px;margin-left:-"+width+"px'>");
			}
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
			//取得父标签
			ColumnTag columnTag = (ColumnTag) getParent();
			Boolean frozenLeft = columnTag.isFrozenLeft();
			
			StringBuffer html = new StringBuffer();
			if(frozenLeft.booleanValue()){
				html.append("</div>");
				html.append("</div>");
			}else{
				html.append("</div>");
			}
			out.write(html.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
