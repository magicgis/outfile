package ${rootPackage}.model;

import java.io.Serializable;
<#if hasDate ==true>
import java.util.Date;
</#if>

/**
 * 
 * @since ${date}
 * @author ${author}
 * @version v1.0
 */
public class ${className} implements Serializable {

	<#list fieldList as var>
	/**
	 * <#if var.comments?has_content>${var.comments}</#if>
	 */
	private ${var.class} ${var.name};
	</#list>
	
	
	<#list fieldList as var>
	/**
	 * <#if var.comments?has_content>${var.comments}</#if>
	 */
	public ${var.class} get${var.name?cap_first}() {
		return ${var.name};
	}

	/**
	 * <#if var.comments?has_content>${var.comments}</#if>
	 */
	public void set${var.name?cap_first}(${var.class} ${var.name}) {
		this.${var.name} = ${var.name};
	}
	</#list>
}