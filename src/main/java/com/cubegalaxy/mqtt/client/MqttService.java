package com.cubegalaxy.mqtt.client;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MqttService {

    @Autowired
    private MqttCustomerClient mqttCustomerClient;

    /**
     * 功能描述: 订阅主题
     *
     * @param client MQTT异步客户端
     */
    public void subscribe(MqttAsyncClient client) throws MqttException {
        //获取主题
        log.info("client: " + client);

        // TODO 写死的主题
        List<String> cacheList = new ArrayList<>();
        cacheList.add("amq.topic");

        if (CollectionUtils.isEmpty(cacheList)) {
            log.error("【mqtt异常】:redis缓存中，无法获取主题相关信息！");
            return;
        }
        String[] topicFilters = cacheList.toArray(new String[0]);
        int[] qos = new int[cacheList.size()];
        for (int i = 0; i < cacheList.size(); i++) {
            qos[i] = 1;
        }
        // 订阅
        client.subscribe(topicFilters, qos);
        log.info("mqtt订阅了设备信息和物模型主题");
    }

    /**
     * 功能描述: 消息回调方法
     *
     * @param topic       主题
     * @param mqttMessage 消息体
     */
    @Async
    public void subscribeCallback(String topic, MqttMessage mqttMessage) throws InterruptedException {
        /**测试线程池使用*/
        log.info("====>>>>线程名--{}", Thread.currentThread().getName());
        /**模拟耗时操作*/
        // Thread.sleep(1000);
        // subscribe后得到的消息会执行到这里面
        String message = new String(mqttMessage.getPayload());
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + mqttMessage.getQos());
        log.info("接收消息内容 : " + message);
        //TODO 这里使用通过数据取到相应的bean 动态去调用接口解析数据
    }

    /**
     * 功能描述: 发布 设备状态
     *
     * @param productId    产品id
     * @param deviceNum    设备编号
     * @param deviceStatus 设备状态
     * @param isShadow     影子模式
     * @param rssi         编号
     */
    public void publishStatus(Long productId, String deviceNum, int deviceStatus, int isShadow, int rssi) {
        String message = "{\"status\":" + deviceStatus + ",\"isShadow\":" + isShadow + ",\"rssi\":" + rssi + "}";
        mqttCustomerClient.publish(1, false, "/" + productId + "/" + deviceNum + "", message);
    }

    /**
     * 功能描述: 发布 设备状态
     *
     * @param productId 产品id
     * @param deviceNum 设备编号
     */
    public void publishInfo(Long productId, String deviceNum) {
        mqttCustomerClient.publish(1, false, "amq.topic", "/" + productId + "/" + deviceNum + "");
    }

    /**
     * 功能描述: 发布 设备状态
     *
     * @param productId 产品id
     * @param deviceNum 设备编号
     */
    public void publishFunction(Long productId, String deviceNum, List<String> thingsList) {
        if (thingsList == null) {
            mqttCustomerClient.publish(1, true, "/" + productId + "/" + deviceNum + "", "");
        } else {
            mqttCustomerClient.publish(1, true, "/" + productId + "/" + deviceNum + "", JSON.toJSONString(thingsList));
        }

    }
}


