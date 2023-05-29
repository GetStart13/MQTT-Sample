package com.cubegalaxy.mqtt.client;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;


/**
 * 功能描述: 消息回调
 * <br> 一个 mqtt 客户端只有一个回调函数，因此也可以做为一个 bean 注入
 */
@Slf4j
@Component
public class CustomMqttCallback implements MqttCallbackExtended {

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("【mqtt异常】:断开连接....", throwable);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.info("接收的主题是: " + topic);
        log.info("接收消息QoS: " + message.getQos());
        log.info("接收消息体: " + new String(message.getPayload()));
    }

    /**
     * 功能描述: 发布消息后，到达MQTT服务器，服务器回调消息接收
     *
     * @param token Mqtt传递令牌
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("【mqtt】交付完成: {}", token.isComplete());
    }

    /**
     * 功能描述: 连接完成后执行
     *
     * @param reconnect 是否重连
     * @param serverUrl 服务地址
     */
    @Override
    public void connectComplete(boolean reconnect, String serverUrl) {
        log.info("mqtt 已经连接");
        log.info("是否重连: " + reconnect);
        log.info("服务端 url: " + serverUrl);

    }
}

