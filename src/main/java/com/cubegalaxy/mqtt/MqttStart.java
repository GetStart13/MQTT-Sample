package com.cubegalaxy.mqtt;

import com.cubegalaxy.mqtt.client.CustomMqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 功能描述: spring 容器创建完成之后，开始创建 mqtt 客户端
 */
@Order(value = 1)
@Component
public class MqttStart implements ApplicationRunner {

    private CustomMqttClient mqttCustomerClient;

    @Autowired
    public void setMqttCustomerClient(CustomMqttClient mqttCustomerClient) {
        this.mqttCustomerClient = mqttCustomerClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        mqttCustomerClient.connect();
    }
}


