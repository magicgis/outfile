/**
 * 
 */
package com.naswork.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @since 2016年5月5日 下午8:20:28
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ColumnLeft extends TagSupport {
	private static final long serialVersionUID = -6024742546358490362L;
	
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
				html.append("<div class='col-left' style='position:relative;float:left;width:"+width+"px;margin-right:-"+width+"px;height: 38px;font-size: 14px;text-align: right;' >");
				
			}else{
				html.append("<div style='float:left;width:100%;' >");
				html.append("<div style='margin-right:"+(width.intValue()+10)+"px;' >");
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
			}else{
				html.append("</div>");
				html.append("</div>");
			}
			out.write(html.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
