package com.example.mqtt.client;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class MqttService {


    /**
     * 功能描述: 发布 mqtt 消息
     *
     * @param qos         消息机制，消息质量
     * @param retained    是否保留消息
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) throws MqttException {
        log.info("【mqtt】发布主题：{}", topic);
        log.info("【mqtt】发布消息：{}", pushMessage);
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());

        IMqttDeliveryToken token = CustomMqttClient.getClient().publish(topic, message);
        token.waitForCompletion();
    }

    /**
     * 功能描述: 订阅某个主题
     *
     * @param topic 主题
     * @param qos   消息质量
     *              Qos1：消息发送一次，不确保
     *              Qos2：至少分发一次，服务器确保接收消息进行确认
     *              Qos3：只分发一次，确保消息送达且只传递一次
     */
    public void subscribe(String topic, int qos) throws MqttException {
        log.info("【mqtt】：订阅了主题 topic: {}, qos: {}", topic, qos);
        IMqttToken token = CustomMqttClient.getClient().subscribe(topic, qos);
        token.waitForCompletion();
    }

    /**
     * 功能描述: 订阅某些主题
     *
     * @param topics 主题
     * @param qos    消息质量
     *               Qos1：消息发送一次，不确保
     *               Qos2：至少分发一次，服务器确保接收消息进行确认
     *               Qos3：只分发一次，确保消息送达和只传递一次
     */
    public void subscribe(String[] topics, int[] qos) throws MqttException {
        log.info("【mqtt】：订阅了主题 topics: {}", Arrays.toString(topics));
        log.info("【mqtt】qos: {}", Arrays.toString(qos));
        IMqttToken token = CustomMqttClient.getClient().subscribe(topics, qos);
        token.waitForCompletion();
    }
}


