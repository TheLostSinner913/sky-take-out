package com.sky.template;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 手动装配
 * @Author: 刘东钦
 * @Date: 2023/4/21 19:36
 */
@Configuration
@EnableConfigurationProperties(AliOSSUtilsPlus.class)
public class AutoConfiguration {
    @Bean
    public AliOSSUtils get(AliOSSUtilsPlus ap){
        AliOSSUtils aliOSSUtils = new AliOSSUtils();
        aliOSSUtils.setAliOSSUtilsPlus(ap);
        return aliOSSUtils;
    }
}
