package com.naswork.task;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.naswork.dao.HeweatherDao;
import com.naswork.model.HeweatherAir;
import com.naswork.model.HeweatherWea;
import com.naswork.utils.HttpRequestUtil;
import com.naswork.utils.StringUtil;


/**
 * 
 * @ClassName:  WeatherTask   
 * @Description:天气数据获取
 * @author: Li Zhenning 
 * @date:   2018-5-19 下午2:10:13   
 *
 */
@Component("weatherTask")
@Lazy(false)
public class WeatherTask {
	
	private final static Logger logger = LoggerFactory.getLogger(WeatherTask.class);
	
	@Autowired
	private ServletContext application;
	
	@Resource
	private HeweatherDao heweatherDao;
	
	private final static int[] AREA_ID = {1000,1001,1002,1003,1004,1005,1006,1007,1008};
	private final static String[] AREA_NAME = {"梅州市","梅县区","梅江区","平远县","丰顺县","兴宁市","大埔县","五华县","蕉岭县"};
	
	/**
	 * @throws UnsupportedEncodingException 
	 * 
	 * @Title: run   
	 * @Description: 每半个小时获取一次数据
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	@Scheduled(fixedDelay=30*60*1000)
	public void run() throws UnsupportedEncodingException {
		
		for (int i = 0; i < AREA_ID.length; i++) {
			logger.debug("开始获取天气数据");
			String weaStr = HttpRequestUtil.get("https://free-api.heweather.com/s6/weather/now?location="+URLEncoder.encode(AREA_NAME[i], "UTF-8")+"&key=2a6d836ff5054b1c8d5cd14e3b8b2658");
			if(StringUtil.isEmpty(weaStr)){
				continue;
			}
			//由于本环境为GBK,将编码转换为UTF-8
			//String weaStrUtf8 = new String(weaStr.getBytes("GBK"),"UTF-8");
			JSONObject wea = JSONObject.fromObject(weaStr);
			if(wea == null){
				continue;
			}
			JSONObject weaNow = wea.getJSONArray("HeWeather6").getJSONObject(0).getJSONObject("now");
			if(weaNow == null){
				continue;
			}
			HeweatherWea heweatherWea = (HeweatherWea) JSONObject.toBean(weaNow,HeweatherWea.class);
			if(heweatherWea == null){
				continue;
			}
			heweatherWea.setArea_id(AREA_ID[i]);
			heweatherDao.insertWea(heweatherWea);
			application.setAttribute("heweatherWea_"+AREA_ID[i], heweatherWea);
		}
		
		logger.debug("开始获取空气数据");
		String airStr = HttpRequestUtil.get("https://free-api.heweather.com/s6/air/now?location=%E6%A2%85%E5%B7%9E&key=2a6d836ff5054b1c8d5cd14e3b8b2658");
		if(StringUtil.isEmpty(airStr)){
			return;
		}
		//由于本环境为GBK,将编码转换为UTF-8
		//String airStrUtf8 = new String(airStr.getBytes("GBK"),"UTF-8");
		JSONObject air = JSONObject.fromObject(airStr);
		if(air == null){
			return;
		}
		JSONObject airNow = air.getJSONArray("HeWeather6").getJSONObject(0).getJSONObject("air_now_city");
		if(airNow == null){
			return;
		}
		HeweatherAir heweatherAir = (HeweatherAir) JSONObject.toBean(airNow,HeweatherAir.class);
		if(heweatherAir == null){
			return;
		}
		heweatherAir.setArea_id(1000);
		heweatherDao.insertAir(heweatherAir);
		application.setAttribute("heweatherAir_1000", heweatherAir);
		
	}
}
