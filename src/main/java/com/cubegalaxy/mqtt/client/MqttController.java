package com.cubegalaxy.mqtt.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mqtt")
public class MqttController {
    @Autowired
    private MqttService mqttService;

    @Value("${mqtt.default-topic:default.topic}")
    private String topic;

    @PostMapping("/publish")
    @ResponseBody
    public synchronized void publishMqttMsg(@RequestBody Map<String, Object> mqttMsg) {
        System.out.println(mqttMsg);
        long productId = (Integer) mqttMsg.get("productId");
        String deviceNum = (String) mqttMsg.get("deviceNum");
        mqttService.publishInfo(productId, deviceNum);
    }
}
