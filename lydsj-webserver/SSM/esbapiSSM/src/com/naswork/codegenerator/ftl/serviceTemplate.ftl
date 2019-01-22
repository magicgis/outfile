package ${rootPackage}.service;

import com.everygold.model.${className};

/**
 * @since ${date}
 * @author ${author}
 * @version v1.0
 */
public interface ${className}Service {

	
	 /**
	 * 根据主键查询对象
	 * @param ${pkMap.name}
	 * @return
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public ${className} findById(String ${pkMap.name});
	
	/**
	 * 新增
	 * @param ${className?cap_first}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public void add(${className} ${className?cap_first});
	
	/**
	 * 更新
	 * @param ${className?cap_first}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public void modify(${className} ${className?cap_first});
	
	/**
	 * 删除
	 * @param ${pkMap.name}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public void remove(String ${pkMap.name});

}