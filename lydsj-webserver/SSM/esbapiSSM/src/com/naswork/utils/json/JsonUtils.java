/**
 * 
 */
package com.naswork.utils.json;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONObject;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @since 2015-2-8 上午9:58:44
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class JsonUtils {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String toJson(Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 对日期进行处理,正常如果是对对象进行日期格式化，可以直接在对象的属性上加上@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	 * @param value
	 * @param dataFormat
	 * @return
	 * @since 2016年4月12日 下午3:09:10
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static String toJson(Object value,String dataFormat) {
		try {
			objectMapper.setDateFormat(new SimpleDateFormat(dataFormat));
			objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			return objectMapper.writeValueAsString(value);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String json, Class<T> valueType) {
		Assert.hasText(json);
		Assert.notNull(valueType);
		try {
			return objectMapper.readValue(json, valueType);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}


	public static <T> T toObject(String json, JavaType javaType) {
		Assert.hasText(json);
		Assert.notNull(javaType);
		try {
			return objectMapper.readValue(json, javaType);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	/**
	 * json2map
	 * @param jsonString
	 * @return
	 * @since 2016年4月6日 下午4:35:51
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static Map<String, String> jsonToMap(String jsonString) {
		//JSONObject必须以"{"开头  
        JSONObject jsonObject = JSONObject.fromObject(jsonString); 
        Map<String, String> resultMap = new HashMap<String, String>();  
        Iterator<String> iter = jsonObject.keys();  
        String key=null;  
        String value=null;  
        while (iter.hasNext()) {  
            key=iter.next();  
            value=jsonObject.getString(key);  
            resultMap.put(key, value);  
        }  
        return resultMap;  
	}

	public static Object urlParam2Json(String data) {
		String[] dataarr = data.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String pv : dataarr) {
			String[] pvarr = pv.split("=");
			map.put(pvarr[0], pvarr[1]);
		}
		return toJson(map);
	}
	
}
