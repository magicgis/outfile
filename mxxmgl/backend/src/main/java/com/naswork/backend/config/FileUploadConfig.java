package com.naswork.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * @Program: FileUploadConfig
 * @Description:
 * @Author: White
 * @DateTime: 2018-12-19 12:40:08
 **/
@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(1048576*10);
        factory.setMaxRequestSize(1048576*100);
        return factory.createMultipartConfig();
    }

}



