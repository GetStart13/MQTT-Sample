package com.example.mqtt.client;

import com.example.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述: mqtt 客户端
 * <br> 一个 mqtt broker 可以只有一个 client，也可以为每一个主题都建立一个 client
 * <br> 这里仅创建一个 mqtt client
 */
@Slf4j
@Component
public class CustomMqttClient {

    private CustomMqttCallback mqttCallback;

    private MqttProperties mqttProperties;

    @Autowired
    public void setMqttCallback(CustomMqttCallback mqttCallback) {
        this.mqttCallback = mqttCallback;
    }

    @Autowired
    public void setMqttProperties(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    /**
     * 连接属性配置
     */
    private MqttConnectOptions options;

    /**
     * MQTT 异步客户端
     */
    private static MqttAsyncClient client;

    public static MqttAsyncClient getClient() {
        return client;
    }

    /**
     * 功能描述: 客户端连接
     */
    public void connect() throws MqttException {
        if (mqttProperties == null) {
            throw new IllegalArgumentException("【mqtt异常】：连接失败，配置文件缺失。");
        }
        // 设置配置
        if (options == null) {
            setOptions();
        }
        // 创建客户端
        if (client == null) {
            createClient();
        }
        // 当客户端未连接时，建立连接
        IMqttToken token = client.connect(options);
        // 等待连接操作完成
        token.waitForCompletion();
    }

    /**
     * 功能描述: 创建 mqtt 客户端，加同步锁，防止创建多个客户端
     */
    private synchronized void createClient() throws MqttException {
        if (client == null) {
            // host 为主机名，clientId 是连接 MQTT 的客户端 ID，MemoryPersistence 设置 clientId 的保存方式，默认是以内存方式保存
            client = new MqttAsyncClient(mqttProperties.getHost(), mqttProperties.getClientId(), new MemoryPersistence());
            // 设置回调函数
            client.setCallback(mqttCallback);
            log.info("【mqtt】：mqtt 客户端启动成功");
        }
    }

    /**
     * 功能描述: 设置连接属性
     */
    private void setOptions() {
        if (options != null) {
            options = null;
        }
        if (mqttProperties == null) {
            throw new IllegalArgumentException("【mqtt异常】连接失败，失败原因：配置文件缺失。");
        }
        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setConnectionTimeout(mqttProperties.getTimeout());
        options.setKeepAliveInterval(mqttProperties.getKeepAlive());
        // 设置自动重新连接
        options.setAutomaticReconnect(true);
        options.setCleanSession(mqttProperties.isClearSession());
    }

    /**
     * 功能描述: 断开与 mqtt 的连接
     */
    public synchronized void disconnect() throws MqttException {
        // 判断客户端是否 null，是否连接
        if (client != null && client.isConnected()) {
            IMqttToken token = client.disconnect();
            // 等待完成断开连接
            token.waitForCompletion();
        }
        client = null;
    }

    /**
     * 功能描述: 断开连接，重连
     */
    public synchronized void reconnect() throws MqttException {
        disconnect();
        setOptions();
        createClient();
        connect();
    }

    /**
     * mqtt client 是否处于连接状态
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}