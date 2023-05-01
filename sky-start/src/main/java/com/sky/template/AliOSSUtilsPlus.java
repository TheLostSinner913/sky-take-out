package com.sky.template;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 工具类
 */
@Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOSSUtilsPlus {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
