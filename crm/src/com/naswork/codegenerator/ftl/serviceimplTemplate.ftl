package ${rootPackage}.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.everygold.model.${className};
import com.everygold.dao.${className}Dao;
import com.everygold.service.${className}Service;

/**
 * @since ${date}
 * @author ${author}
 * @version v1.0
 */
@Service("${className?uncap_first}Service")
public class ${className}ServiceImpl implements ${className}Service{

	@Resource
	private ${className}Dao ${className?uncap_first}Dao;
	
	 /**
	 * 根据主键查询对象
	 * @param ${pkMap.name}
	 * @return
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public ${className} findById(String ${pkMap.name}){
		return ${className?uncap_first}Dao.findById(${pkMap.name});
	}
	
	/**
	 * 新增
	 * @param ${className?uncap_first}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public void add(${className} ${className?uncap_first}){
		${className?uncap_first}Dao.insert(${className?uncap_first});
		
	}
	
	/**
	 * 更新
	 * @param ${className?uncap_first}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public void modify(${className} ${className?uncap_first}){
		${className?uncap_first}Dao.update(${className?uncap_first});
	}
	
	/**
	 * 删除
	 * @param ${pkMap.name}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public void remove(String ${pkMap.name}){
		${className?uncap_first}Dao.delete(${pkMap.name});
	}

}