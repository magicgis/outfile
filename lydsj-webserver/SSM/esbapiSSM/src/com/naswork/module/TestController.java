package com.naswork.module;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;

@RestController  
@RequestMapping("/test") 
public class TestController extends BaseController {
	

	@RequestMapping("/{id}")
	public String GETUSER(@PathVariable Integer id) {
		return String.valueOf(id);
	}
	
	
	
}
