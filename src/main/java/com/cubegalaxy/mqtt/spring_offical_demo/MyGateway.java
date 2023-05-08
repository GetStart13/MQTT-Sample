package com.cubegalaxy.mqtt.spring_offical_demo;

import org.springframework.integration.annotation.MessagingGateway;

//@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MyGateway {
    void sendToMqtt(String data);
}
