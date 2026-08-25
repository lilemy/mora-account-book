package com.lilemy.moraaccountbook.manager.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 对象存储配置属性类
 *
 * @author lilemy
 * @date 2026-08-25
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss.s3")
public class OssProperties {

    /**
     * 对象存储地址
     */
    private String endpoint;

    /**
     * 对象存储区域
     */
    private String region;

    /**
     * 对象存储访问密钥
     */
    private String accessKey;

    /**
     * 对象存储密钥
     */
    private String secretKey;

    /**
     * 对象存储存储桶名称
     */
    private String bucketName;
}
