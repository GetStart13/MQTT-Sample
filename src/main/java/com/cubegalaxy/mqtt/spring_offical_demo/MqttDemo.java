package com.cubegalaxy.mqtt.spring_offical_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class MqttDemo {
    @Autowired
    private MyGateway gateway;

    @PostConstruct
    public void demo() {
        gateway.sendToMqtt("foo");
    }
}
