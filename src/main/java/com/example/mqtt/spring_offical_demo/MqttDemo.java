package com.example.mqtt.spring_offical_demo;

import org.springframework.beans.factory.annotation.Autowired;

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
