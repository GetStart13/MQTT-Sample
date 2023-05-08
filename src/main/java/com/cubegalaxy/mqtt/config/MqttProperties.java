package com.cubegalaxy.mqtt.config;

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
     * 服务器地址url
     */
    private String host;

    /**
     * 客户端唯一ID
     */
    private String clientId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 超时时间
     */
    private Integer timeOut;

    /**
     * 保活时间
     */
    private Integer keepAlive;

    /**
     * 是否清除会话
     */
    private boolean clearSession;
}
