/*
package com.sky.config;

import com.sky.properties.AliOssProperties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*/
/**
 * @Description: aliyun
 * @Author: 刘东钦
 * @Date: 2023/4/25 15:26
 *//*

@Configuration
public class Aliyun {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil create(AliOssProperties ap){
        AliOssUtil aliOssUtil = new AliOssUtil();
        aliOssUtil.setEndpoint(ap.getEndpoint());
        aliOssUtil.setAccessKeyId(ap.getAccessKeyId());
        aliOssUtil.setAccessKeySecret(ap.getAccessKeySecret());
        aliOssUtil.setBucketName(ap.getBucketName());
        return aliOssUtil;
    }
}
*/
