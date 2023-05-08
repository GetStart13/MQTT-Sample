package com.cubegalaxy.mqtt.java_demo;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 发布端
 */
public class PublishSample {
    public static void main(String[] args) {

        String topic = "amq.topic";
        String content = "测试消息发布";
        int qos = 1;
        String broker = "tcp://192.168.3.3:1883";
        String userName = "rabbitmq";
        String password = "123456";
        String clientId = "pubClient";
        // 内存存储
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            // 创建客户端
            try (MqttClient sampleClient = new MqttClient(broker, clientId, persistence)) {
                // 创建链接参数
                MqttConnectOptions connOpts = new MqttConnectOptions();
                // 在重新启动和重新连接时记住状态
                connOpts.setCleanSession(false);
                // 设置连接的用户名
                connOpts.setUserName(userName);
                connOpts.setPassword(password.toCharArray());
                // 建立连接
                System.out.println("连接到 broker: " + broker);
                sampleClient.connect(connOpts);
                System.out.println("连接成功.");
                // 创建消息
                MqttMessage message = new MqttMessage(content.getBytes());
                // 设置消息的服务质量
                message.setQos(qos);
                // 发布消息
                System.out.println("向[" + topic + "]发送消息:" + message);
                sampleClient.publish(topic, message);
                // 断开连接
                sampleClient.disconnect();
            }
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("exception " + me);
            me.printStackTrace();
        }
    }
}
