package com.example.event.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.constant.KafkaTopicsAndConsumerGroups;
import com.example.event.BaseEvent;
import com.example.event.OrderEvent;

@Service
public class OrderEventConsumer {

    @KafkaListener(topics = KafkaTopicsAndConsumerGroups.ORDER_EVENTS_TOPIC, groupId = KafkaTopicsAndConsumerGroups.ORDER_EVENTS_GROUP_ID, containerFactory = "orderKafkaListenerContainerFactory")
    public void listen(BaseEvent<OrderEvent> event) {
        System.out.println("Received Order Event: " + event.getData().getOrderId());
    }
}

