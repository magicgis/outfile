package com.naswork.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@MapperScan("com.naswork.backend.mapper.*")
//public class BackendApplication extends SpringBootServletInitializer{
public class BackendApplication  {
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}



