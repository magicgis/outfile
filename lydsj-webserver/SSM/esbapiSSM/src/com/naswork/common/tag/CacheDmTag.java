/**
 * 
 */
package com.naswork.common.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.naswork.service.CacheService;
import com.naswork.utils.SpringUtils;


/**
 * @since 2015年10月8日 下午3:57:12
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class CacheDmTag extends TagSupport {
	private static final long serialVersionUID = -2164097413313883986L;
	CacheService cacheService =  (CacheService)SpringUtils.getBean("cacheService");
	private String cacheId;//如税务机关相关的是SWJG_DM,行业代码相关的是HY_DM,身份相关的是SWRYSF_DM
	private String name;//元素名称和id
	private String value;//值
	private String style;

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			StringBuffer html = new StringBuffer();
			String mc;
			if (value.indexOf(",") > 0) {
				String[] values = value.split(",");
				String mcTemp = "";
				for (int i = 0; i < values.length; i++) {
					mcTemp += cacheService.getDmMc(cacheId, values[i].trim())+",";
				}
				mc = mcTemp.substring(0, mcTemp.length()-1);
			}else {
				mc = cacheService.getDmMc(cacheId, value);
			}
			
			String stylestr = "";
			if(StringUtils.isEmpty(mc)){
				mc = "";
			}
			if(StringUtils.isNotBlank(this.style)){
				stylestr = "style=\""+this.style+"\"";
			}
			html.append("<input type=\"text\" "+stylestr+" name=\""+this.name+"_mc\" id=\""+this.name+"_mc\" value=\""+mc+"\" readonly=\"readonly\"/>");
			html.append("<input type=\"hidden\" name=\""+this.name+"\" id=\""+this.name+"\" value=\""+this.value+"\"/>");
			
			out.write(html.toString());
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	public String getCacheId() {
		return cacheId;
	}

	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
