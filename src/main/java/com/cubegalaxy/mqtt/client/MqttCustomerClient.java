package com.cubegalaxy.mqtt.client;

import com.cubegalaxy.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 功能描述: mqtt客户端
 */
@Slf4j
@Component
public class MqttCustomerClient {

    @Resource
    private MqttCallback mqttCallback;

    @Resource
    private MqttProperties mqttProperties;

    /**
     * 连接配置
     */
    private MqttConnectOptions options;

    /**
     * MQTT异步客户端
     */
    public static MqttAsyncClient client;

    /**
     * 功能描述: 客户端连接
     */
    public void connect() {
        if (mqttProperties == null) {
            log.error("【mqtt异常】:连接失败，配置文件缺失。");
            return;
        }
        //设置配置
        if (options == null) {
            setOptions();
        }
        //创建客户端
        if (client == null) {
            createClient();
        }
        while (!client.isConnected()) {
            try {
                IMqttToken token = client.connect(options);
                token.waitForCompletion();
            } catch (Exception e) {
                log.error("【mqtt异常】:mqtt连接失败，message={}", e.getMessage());
            }
        }
    }

    /**
     * 功能描述: 创建客户端
     */
    private void createClient() {
        if (client == null) {
            try {
              /*host为主机名，clientId是连接MQTT的客户端ID，MemoryPersistence设置clientId的保存方式
                默认是以内存方式保存*/
                client = new MqttAsyncClient(mqttProperties.getHost(), mqttProperties.getClientId(), new MemoryPersistence());
                //设置回调函数
                client.setCallback(mqttCallback);
                log.info("【mqtt】:mqtt客户端启动成功");
            } catch (MqttException e) {
                log.error("【mqtt异常】:mqtt客户端连接失败，error={}", e.getMessage());
                e.printStackTrace();
            }
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
            log.error("【mqtt异常】连接失败，失败原因：配置文件缺失。");
            return;
        }
        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(mqttProperties.getUserName());
        options.setPassword(mqttProperties.getPassWord().toCharArray());
        options.setConnectionTimeout(mqttProperties.getTimeOut());
        options.setKeepAliveInterval(mqttProperties.getKeepAlive());
        //设置自动重新连接
        options.setAutomaticReconnect(true);
        options.setCleanSession(mqttProperties.isClearSession());
    }

    /**
     * 功能描述: 断开与mqtt的连接
     */
    public synchronized void disconnect() {
        //判断客户端是否null 是否连接
        if (client != null && client.isConnected()) {
            try {
                IMqttToken token = client.disconnect();
                token.waitForCompletion();
            } catch (MqttException e) {
                log.error("【mqtt异常】: 断开mqtt连接发生错误，message={}", e.getMessage());
            }
        }
        client = null;
    }

    /**
     * 功能描述: 重新连接 MQTT
     */
    public synchronized void reconnect() {
        disconnect();
        setOptions();
        createClient();
        connect();
    }

    /**
     * 功能描述: 发布
     *
     * @param qos         连接方式
     * @param retained    是否保留
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        log.info("【mqtt】发布主题" + topic);
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());

        try {
            IMqttDeliveryToken token = client.publish(topic, message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            log.error("【mqtt异常】: 发布主题时发生错误 topic={},message={}", topic, e.getMessage());
        }
    }

    /**
     * 功能描述: 订阅某个主题
     *
     * @param topic 主题
     * @param qos   消息质量
     *              Qos1：消息发送一次，不确保
     *              Qos2：至少分发一次，服务器确保接收消息进行确认
     *              Qos3：只分发一次，确保消息送达和只传递一次
     */
    public void subscribe(String topic, int qos) {
        log.info("【mqtt】:订阅了主题 topic={}", topic);
        try {
            IMqttToken token = client.subscribe(topic, qos);
            token.waitForCompletion();
        } catch (MqttException e) {
            log.error("【mqtt异常】:订阅主题 topic={} 失败 message={}", topic, e.getMessage());
        }
    }

    /**
     * 功能描述: 订阅某些主题
     *
     * @param topic 主题
     * @param qos   消息质量
     *              Qos1：消息发送一次，不确保
     *              Qos2：至少分发一次，服务器确保接收消息进行确认
     *              Qos3：只分发一次，确保消息送达和只传递一次
     */
    public void subscribe(String[] topic, int[] qos) {
        log.info("【mqtt】:订阅了主题 topic={}", Arrays.toString(topic));
        try {
            IMqttToken token = client.subscribe(topic, qos);
            token.waitForCompletion();
        } catch (MqttException e) {
            log.error("【mqtt异常】:订阅主题 topic={} 失败 message={}", topic, e.getMessage());
        }
    }

    /**
     * 是否处于连接状态
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}