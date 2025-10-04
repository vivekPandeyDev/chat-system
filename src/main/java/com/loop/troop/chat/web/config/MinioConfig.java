package com.loop.troop.chat.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "minio")
@Configuration
@Getter
@Setter
public class MinioConfig {
    private String url;
    private String accessKey;
    private String secretKey;

}
