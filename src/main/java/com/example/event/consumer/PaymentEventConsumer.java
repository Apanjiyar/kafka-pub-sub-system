package com.example.event.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.constant.KafkaTopicsAndConsumerGroups;
import com.example.event.BaseEvent;
import com.example.event.PaymentEvent;

@Service
public class PaymentEventConsumer {

    @KafkaListener(topics = KafkaTopicsAndConsumerGroups.PAYMENT_EVENTS_TOPIC, groupId = KafkaTopicsAndConsumerGroups.PAYMENT_EVENTS_GROUP_ID, containerFactory = "paymentKafkaListenerContainerFactory")
    public void listen(BaseEvent<PaymentEvent> event) {
        System.out.println("Received Payment Event: " + event.getData().getPaymentId());
    }
}

