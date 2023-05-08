package com.cubegalaxy.mqtt.java_demo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 订阅端
 */
public class SubscribeSample {

    public static void main(String[] args) {
        //EMQ X 默认端口 1883
        String broker = "tcp://192.168.3.3:1883";
        String topic = "amq.topic";
        int qos = 1;
        String clientId = "subClient";
        String userName = "rabbitmq";
        String passWord = "123456";

        try {
            // host 为主机名，test 为 clientId，即连接 MQTT 的客户端 ID，一般以客户端唯一标识符表示，
            // MemoryPersistence 设置 clientId 的保存形式，默认为以内存保存
            // 不使用 try-with-resource 自动关闭连接
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调函数
            client.setCallback(new MqttCallback() {

                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost");
                }

                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("======监听到来自[" + topic + "]的消息======");
                    System.out.println("message content: " + new String(message.getPayload()));
                    System.out.println("============");
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("交付完成: " + token.isComplete());
                }

            });

            // 建立连接
            System.out.println("连接到 broker: " + broker);
            client.connect(options);

            System.out.println("连接成功.");

            // 订阅消息
            client.subscribe(topic, qos);
            System.out.println("开始监听" + topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}