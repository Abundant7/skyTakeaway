package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 功能：
 * 日期：2023/10/13 10:42
 */

@Component
@ConfigurationProperties(prefix = "sky.redis")
@Data
public class RedisProperties {

    private String host;
    private String port;
    private String password;
    private String database;

}