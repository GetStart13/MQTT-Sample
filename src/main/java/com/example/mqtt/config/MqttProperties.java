package com.example.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    /**
     * 服务器地址 url
     */
    private String host;

    /**
     * 客户端唯一 ID
     */
    private String clientId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间 (s)
     */
    private Integer timeout;

    /**
     * 保持存活时间间隔 (s)
     */
    private Integer keepAlive;

    /**
     * 是否清除会话
     */
    private boolean clearSession;
}
