/**
 * 
 */
package com.naswork.common.tag;

import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.tag.TagOptions;
import com.naswork.vo.tag.TextValueVo;

/**
 * 编辑表单元素标签
 * 
 * @since 2015年8月28日 上午10:53:58
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class EditFormTag extends TagSupport {
	private static final long serialVersionUID = 6601535420769588083L;

	private String name;
	private String type;// text,select,radio,yesnoselect,yesnoradio
	private String options;// data:[],url:''
	private boolean date = false;
	private boolean readOnly = false;
	private boolean required = false;
	private String defaultValue = "";
	private String style = "";
	private String cssName;

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			StringBuffer html = new StringBuffer();
			if (type.equals("text")) {
				html.append("<input id=\"" + this.name + "\" type=\"text\" name=\"" + this.name + "\" ");
				if(readOnly){
					html.append(" readonly=\"readonly\" ");
				}
				if(required){
					html.append(" class=\""+this.cssName+" required\" ");
				}else{
					html.append(" class=\""+this.cssName+"\" ");
				}
				if(StringUtils.isNotBlank(this.defaultValue)){
					html.append(" value=\"").append(defaultValue).append("\" ");
				}
				if(StringUtils.isNotBlank(this.style)){
					html.append(" style=\"").append(style).append("\" ");
				}
				html.append("/>");
				if(date){
					//加入日期选择控件
					html.append("<script type=\"text/javascript\">");
					html.append("$(document).ready(function(){");
					html.append("PJ.datepicker(\""+this.name+"\");");
					html.append("$(\"#ui-datepicker-div\").hide();");
					html.append("});");
					html.append("</script>");
				}
			} else if (type.equals("select")) {
				html.append("<select id=\""+this.name+"\" name=\"" + this.name + "\"");
				if(required){
					html.append(" class=\""+this.cssName+" required\" ");
				}else{
					html.append(" class=\""+this.cssName+"\" ");
				}
				if(StringUtils.isNotBlank(this.style)){
					html.append(" style=\"").append(style).append("\" ");
				}
				html.append(">");
				html.append(convertSelectOptions(name));
				html.append("</select>");
			} else if (type.equals("radio")) {
				html.append(convertRadioOptions());
			} else if (type.equals("yesnoradio")) {
				html.append("<input type=\"radio\" name=\"")
					.append(this.name)
					.append("\" value=\"Y\"")
					.append(StringUtils.isEmpty(this.defaultValue) || this.defaultValue.equals("Y") ? " checked = \"checked\"" : "") 
					.append("/>是")
					.append("<input type=\"radio\" name=\"")
					.append(this.name)
					.append("\" value=\"N\"")
					.append(StringUtils.isEmpty(this.defaultValue) && this.defaultValue.equals("N") ? " checked = \"checked\"" : "")
					.append("/>否 ");
			} else if (type.equals("yesnoselect")) {
				html.append("<select name=\"" + this.name + "\">");
				html.append("<option value=\"Y\">是</option>");
				html.append("<option value=\"N\">否</option>");
				html.append("</select>");
			} else if("decimal".equals(type)){
				html.append("<input id=\"" + this.name + "\" type=\"text\" name=\"" + this.name + "\" ");
				if(StringUtils.isNotBlank(this.defaultValue)){
					html.append(" value=\"").append(defaultValue).append("\" ");
				}
				html.append(" class=\"decimal\" ");
				
				html.append("/>");
			}

			out.write(html.toString());
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	private String convertRadioOptions() {
		StringBuffer op = new StringBuffer();
		if(StringUtils.isNotEmpty(options)){
			TagOptions tagOptions = JsonUtils.toObject(this.options, TagOptions.class);
			List<TextValueVo> data = tagOptions.getData();
			if(data==null){
				String url = tagOptions.getUrl();
				if(StringUtils.isNotEmpty(url)){
					op.append("<script type=\"text/javascript\">");
					op.append("$(document).ready(function(){");
					op.append("PJ.ajax({url:\""+url+"\",loading:\"正在加载数据...\",success:function(result){if(result.success){return result.message;}else{PJ.warn(\"加载数据失败\");return '';}}});");
					op.append("});");
					op.append("</script>");
					
				}else{
					return "";
				}
			}else{
				Iterator<TextValueVo> ite = data.iterator();
				int c = 0;
				while(ite.hasNext()){
					String checked = "";
					TextValueVo textValueVo = ite.next();
					if(c==0){
						checked = "checked = \"checked\"";
					}
					op.append("<input type=\"radio\" name=\""+this.name+"\" value=\""+textValueVo.getValue()+"\" "+checked+">"+textValueVo.getText()+" ");
					c++;
				}
			}
			return op.toString();
		}
		return "";
	}

	private String convertSelectOptions(String id) {
		StringBuffer op = new StringBuffer();
		if(StringUtils.isNotEmpty(options)){
			JSONObject obj = JSONObject.fromObject(this.options);
			JSONArray data = (JSONArray) obj.get("data");
			if(data==null){
				String url = obj.getString("url");
				if(StringUtils.isNotEmpty(url)){
					op.append("<script type=\"text/javascript\">");
					op.append("$(document).ready(function(){");
					op.append("PJ.ajax({url:\""+url+"\",beforeSend:function(){},complete:function(){},success:function(result){if(result.success){$(\"#"+id+"\").empty().append(result.message);}else{PJ.warn(\"加载数据失败\");}}});");
					op.append("});");
					op.append("</script>");
					
				}else{
					return "";
				}
			}else{
				Iterator<JSONObject> ite = data.iterator();
				while(ite.hasNext()){
					JSONObject json = ite.next();
					op.append("<option value=\""+json.getString("value")+"\">"+json.getString("text")+"</option>");
				}
			}
			return op.toString();
		}
		return "";
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
	
	public boolean isDate() {
		return date;
	}

	public void setDate(boolean date) {
		this.date = date;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCssName() {
		return cssName;
	}

	public void setCssName(String cssName) {
		this.cssName = cssName;
	}
	
}
