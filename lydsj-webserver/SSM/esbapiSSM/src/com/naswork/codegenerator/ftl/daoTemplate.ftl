package ${rootPackage}.dao;

import java.util.List;

import com.everygold.model.${className};
import com.everygold.vo.PageModel;


/**
 * @since ${date}
 * @author ${author}
 * @version v1.0
 */
public interface ${className}Dao {

	
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
	 * 查询分页
	 * @param ${pkMap.name}
	 * @return
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public List<${className}> findPage(PageModel<${className}> page);
	
	/**
	 * 新增
	 * @param ${className?cap_first}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public int insert(${className} ${className?uncap_first});
	
	/**
	 * 更新
	 * @param ${className?uncap_first}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	public int update(${className} ${className?uncap_first});
	
	/**
	 * 删除
	 * @param ${pkMap.name}
	 * @since ${date}
	 * @author ${author}
	 * @version v1.0
	 */
	int delete(String ${pkMap.name});

}