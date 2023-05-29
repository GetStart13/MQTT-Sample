package com.cubegalaxy.mqtt.client;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
public class MqttService {

    /**
     * 发布 mqtt 消息，service 定制版
     *
     * @param mqttMsg mqtt 消息
     */
    public void publish(Map<String, Object> mqttMsg) {
        int qos = (int) mqttMsg.get("qos");
        boolean retained = (boolean) mqttMsg.get("retained");
        String topic = (String) mqttMsg.get("topic");
        String msg = (String) mqttMsg.get("msg");
        publish(qos, retained, topic, msg);
    }

    /**
     * 功能描述: 发布 mqtt 消息
     *
     * @param qos         消息机制，消息质量
     * @param retained    是否保留消息
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        log.info("【mqtt】发布主题：" + topic);
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());

        try {
            IMqttDeliveryToken token = CustomMqttClient.getClient().publish(topic, message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            log.error("【mqtt异常】：mqtt 持久化异常，topic = {}, message = {}", topic, e.getMessage(), e);
        } catch (MqttException e) {
            log.error("【mqtt异常】：发布主题时发生错误，topic = {}, message = {}", topic, e.getMessage(), e);
        }
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
    public void subscribe(String topic, int qos) {
        log.info("【mqtt】：订阅了主题 topic: {}", topic);
        try {
            IMqttToken token = CustomMqttClient.getClient().subscribe(topic, qos);
            token.waitForCompletion();
        } catch (MqttException e) {
            log.error("【mqtt异常】：订阅主题 topic = {}，失败原因 = {}", topic, e.getMessage(), e);
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
        log.info("【mqtt】：订阅了主题 topic: {}", Arrays.toString(topic));
        try {
            IMqttToken token = CustomMqttClient.getClient().subscribe(topic, qos);
            token.waitForCompletion();
        } catch (MqttException e) {
            log.error("【mqtt异常】：订阅主题 topic = {}，失败原因 = {}", topic, e.getMessage(), e);
        }
    }
}


