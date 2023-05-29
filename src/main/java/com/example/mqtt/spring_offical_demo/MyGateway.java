package com.example.mqtt.spring_offical_demo;

//@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MyGateway {
    void sendToMqtt(String data);
}
