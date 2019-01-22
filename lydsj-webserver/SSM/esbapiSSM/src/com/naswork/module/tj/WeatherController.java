package com.naswork.module.tj;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;

@RestController
@RequestMapping("/v1/weather")
public class WeatherController extends BaseController {
	
	@Autowired
	private ServletContext application;
	
	@RequestMapping("/{id}")
	public Map<String, Object> weather(@PathVariable Integer id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("weather", application.getAttribute("heweatherWea_"+id));
		resultMap.put("air", application.getAttribute("heweatherAir_1000"));
		return resultMap;
	}
}
