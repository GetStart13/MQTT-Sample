package com.example.mqtt.client;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mqtt")
public class MqttController {
    private MqttService mqttService;

    @Autowired
    public void setMqttService(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @PostMapping("/publish")
    @ResponseBody
    public synchronized void publishMqttMsg(@RequestBody Map<String, Object> mqttMsg) {
        log.info("receive publish message: {}", mqttMsg);
        int qos = (int) mqttMsg.get("qos");
        boolean retained = (boolean) mqttMsg.get("retained");
        String topic = (String) mqttMsg.get("topic");
        String msg = (String) mqttMsg.get("msg");
        try {
            mqttService.publish(qos, retained, topic, msg);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
