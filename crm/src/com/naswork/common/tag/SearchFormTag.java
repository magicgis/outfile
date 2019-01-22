/**
 * 
 */
package com.naswork.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.naswork.filter.ContextHolder;

/**
 * 搜索表单元素标签
 * 
 * @since 2015年8月27日 上午11:16:32
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class SearchFormTag extends TagSupport {
	private static final long serialVersionUID = 2636177550081624250L;
	private String prefix;
	private String name;
	private String oper = "eq";
	private String type = "text";//select,year,date,selecttree,swjg
	private String data = null;
	private String url = null;
	private int selectBoxWidth = 0;
	private boolean hideOnLoseFocus = true;
	private boolean multiSelect = false;
	private String rootswjg = "14401000000";
	private String style;

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;// 让服务器继续执行页面
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			StringBuffer html = new StringBuffer();
			if (type.equals("text")) {
				html.append(createTextHtml());
			} else if (type.equals("select")) {
				html.append(createSelectHtml());
			} else if (type.equals("year")) {
				html.append(createYearHtml());
			}else if (type.equals("selecttree")) {
				html.append(createSelectTreeHtml());
			}else if (type.equals("swjg")) {
				html.append(createSwjgHtml());
			} else if (type.equals("date")) {
				html.append(createDateHtml());
			}else if(type.equals("multiselect")){
				html.append(createMultiSelect());
			}
			out.write(html.toString());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;// 告诉服务器接下来直接执行doEndTag()方法
	}
	
	/**
	 * 下拉
	 * @return
	 * @since 2015年9月18日 下午5:14:35
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	private Object createSelectHtml() {
		StringBuffer html = new StringBuffer();
		html.append("<input type=\"text\" id=\""+this.name+"_dropdown\"/> ");
		html.append("<script type=\"text/javascript\">");
		html.append("$(function(){");
		html.append("$(\"#"+this.name+"_dropdown\").ligerComboBox({");
		if(this.selectBoxWidth>0){
			html.append("selectBoxWidth:"+this.selectBoxWidth+",");
		}
		if(this.multiSelect){
			html.append("isShowCheckBox: true, isMultiSelect: true,");
		}
		html.append("split:\",\", ");
		html.append("valueFieldID: \""+this.name+"\",");
		//处理url和data,优先data
		if(StringUtils.isNotEmpty(this.data)){
			html.append("data:").append(this.data);
		}else if(StringUtils.isNotEmpty(this.url)){
			html.append("url:'").append(this.url).append("'");
		}
		html.append("});");
		html.append("$(\"#"+this.name+"\").attr(\"prefix\",\""+this.prefix+"\");");
		html.append("$(\"#"+this.name+"\").attr(\"alias\",\""+this.name+"\");");
		if(this.multiSelect){
			html.append("$(\"#"+this.name+"\").attr(\"oper\",\"sqlin\");");
		}else{
			html.append("$(\"#"+this.name+"\").attr(\"oper\",\""+this.oper+"\");");
		}
		html.append("});");
		html.append("</script>");
		
		return html.toString();
	}
	
	public Object createMultiSelect(){
		StringBuffer html = new StringBuffer();
		html.append("<select id='"+this.name+"'  multiple=\"multiple\" size=\"5\"></select>");
		html.append("<script type=\"text/javascript\">");
		html.append("$(function(){");
		html.append("$(\"#"+this.name+"\").multiselect({async:true,type:'tree',");
		html.append("checkAllText: \"全选\", uncheckAllText: '全不选', ");
		html.append("url:\""+this.url+"\"");
		html.append("}).multiselectfilter();");
		html.append("});");
		html.append("</script>");
		return html.toString();
	}

	/**
	 * 文本
	 * @return
	 * @since 2015年9月18日 下午5:13:11
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	private Object createTextHtml() {
		StringBuffer html = new StringBuffer();
		html.append("<input class=\"text\" type=\"" + this.type + "\" prefix=\""
				+ this.prefix
				+ "\" alias=\""+this.name+"\" id=\""+this.name+"\" oper=\""+this.oper+"\"");
		if(StringUtils.isNotEmpty(style)){
			html.append("style=\""+this.style+"\" ");
		}
		html.append("/>");
		return html.toString();
	}
	
	/**
	 * 日期区间
	 * @return
	 * @since 2015年9月18日 下午5:13:18
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	private Object createDateHtml() {
		StringBuffer html = new StringBuffer();
		html.append("<input type=\"text\" prefix=\""+this.prefix+"\" alias=\""+this.name+"\" name=\""+this.name+"_BEGIN\" elemType=\"date\" lt=\""+this.name+"_END\" style=\"width: 41%\"/>")
		.append(" 至 <input type=\"text\" prefix=\""+this.prefix+"\" alias=\""+this.name+"\" name=\""+this.name+"_END\" elemType=\"date\" gt=\""+this.name+"_BEGIN\" style=\"width: 41%\"/>");
		return html.toString();
	}

	/**
	 * 年度区间
	 * @return
	 * @since 2015年9月18日 下午5:13:24
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	private Object createYearHtml() {
		StringBuffer html = new StringBuffer();
		html.append("<input type=\"text\" alias=\""+this.name+"\" elemType=\"year\" prefix=\""+this.prefix+"\" oper=\"gt\" style=\"width:80px \"/> 至 <input type=\"text\" alias=\""+this.name+"\" elemType=\"year\" prefix=\""+this.prefix+"\" oper=\"lt\" style=\"width:80px \"/>");
		return html.toString();
	}

	/**
	 * 下拉树
	 * @return
	 * @since 2015年9月18日 下午5:13:30
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	private String createSelectTreeHtml(){
		StringBuffer html = new StringBuffer();
		html.append("<input type=\"text\" id=\""+this.name+"_dropdown\"/> ");
		html.append("<script type=\"text/javascript\">");
		html.append("$(function(){");
		html.append("$(\"#"+this.name+"_dropdown\").ligerComboBox({");
		if(this.selectBoxWidth>0){
			html.append("selectBoxWidth:"+this.selectBoxWidth+",");
		}else{
			html.append("selectBoxWidth:350,");
		}
		if(!this.hideOnLoseFocus){
			html.append("hideOnLoseFocus:false,");
		}
		html.append("split:\",\", ");
		html.append("valueFieldID: \""+this.name+"\",");
		html.append("tree:{");
		if(this.multiSelect){
			html.append("checkbox:true,");
		}else{
			html.append("checkbox:false,");
		}
		html.append("url:'"+this.url+"',");
		html.append("nodeWidth:200,");
		html.append("idFieldName :'id',");
		html.append("parentIDFieldName :'pid',");
		html.append("isExpand:2");
		html.append("}");
		html.append("});");
		html.append("$(\"#"+this.name+"\").attr(\"prefix\",\""+this.prefix+"\");");
		html.append("$(\"#"+this.name+"\").attr(\"alias\",\""+this.name+"\");");
		if(this.multiSelect){
			html.append("$(\"#"+this.name+"\").attr(\"oper\",\"sqlin\");");
		}else{
			html.append("$(\"#"+this.name+"\").attr(\"oper\",\""+this.oper+"\");");
		}
		html.append("});");
		html.append("</script>");
		
		return html.toString();
	}
	
	/**
	 * 税务机关
	 * @return
	 * @since 2015年9月18日 下午5:13:40
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	private String createSwjgHtml(){
		StringBuffer html = new StringBuffer();
		html.append("<input type=\"text\" id=\""+this.name+"_dropdown\"/> ");
		html.append("<script type=\"text/javascript\">");
		html.append("$(function(){");
		html.append("$(\"#"+this.name+"_dropdown\").ligerComboBox({");
		if(this.selectBoxWidth>0){
			html.append("selectBoxWidth:"+this.selectBoxWidth+",");
		}else{
			html.append("selectBoxWidth:400,");
		}
		if(!this.hideOnLoseFocus){
			html.append("hideOnLoseFocus:false,");
		}
		html.append("split:\",\", ");
		html.append("valueFieldID: \""+this.name+"\",");
		html.append("tree:{");
		if(this.multiSelect){
			html.append("checkbox:true,");
		}else{
			html.append("checkbox:false,");
		}
		html.append("url:'"+ContextHolder.getRequest().getContextPath()+"/xtgl/qxgl/swjg?swjgdm="+this.rootswjg+"',");
		html.append("nodeWidth:200,");
		html.append("idFieldName :'id',");
		html.append("parentIDFieldName :'pid',");
		html.append("isExpand:2");
		html.append("}");
		html.append("});");
		html.append("$(\"#"+this.name+"\").attr(\"prefix\",\""+this.prefix+"\");");
		html.append("$(\"#"+this.name+"\").attr(\"alias\",\""+this.name+"\");");
		if(this.multiSelect){
			html.append("$(\"#"+this.name+"\").attr(\"oper\",\"sqlin\");");
		}else{
			html.append("$(\"#"+this.name+"\").attr(\"oper\",\""+this.oper+"\");");
		}
		html.append("});");
		html.append("</script>");
		
		return html.toString();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSelectBoxWidth() {
		return selectBoxWidth;
	}

	public void setSelectBoxWidth(int selectBoxWidth) {
		this.selectBoxWidth = selectBoxWidth;
	}

	public boolean isHideOnLoseFocus() {
		return hideOnLoseFocus;
	}

	public void setHideOnLoseFocus(boolean hideOnLoseFocus) {
		this.hideOnLoseFocus = hideOnLoseFocus;
	}

	public boolean isMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getRootswjg() {
		return rootswjg;
	}

	public void setRootswjg(String rootswjg) {
		this.rootswjg = rootswjg;
	}

}
