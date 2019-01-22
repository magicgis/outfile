/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 2016年5月11日 下午3:20:33
 * @author xiaohu
 * @version v1.0
 */
public class ZTreeVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7569001428972797580L;

	private String id;
	
	private String pid;
	
	private String name;
	
	private String value;
	
	private ZTreeVo parent;
	
	private boolean isParent; 
	
	private List<ZTreeVo> children = new ArrayList<ZTreeVo>();
	
	public static void createTree(List<ZTreeVo> childrens)
    {
        if (null == childrens)
        {
            return ;
        }
         
        for (ZTreeVo each : childrens)
        {
            for (ZTreeVo inner : childrens)
            {
                if (each.getId().equals(inner.getPid()))
                {
                    each.getChildren().add(inner);
                    inner.setParent(each);
                }
            }
        }
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

	public List<ZTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<ZTreeVo> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public ZTreeVo getParent() {
		return parent;
	}

	public void setParent(ZTreeVo parent) {
		this.parent = parent;
	}
	
	
	
}
