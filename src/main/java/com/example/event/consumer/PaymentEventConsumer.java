package com.example.event.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.constant.KafkaTopicsAndConsumerGroups;
import com.example.event.BaseEvent;
import com.example.event.PaymentEvent;

@Service
public class PaymentEventConsumer {

    @KafkaListener(topics = KafkaTopicsAndConsumerGroups.PAYMENT_EVENTS_TOPIC, groupId = KafkaTopicsAndConsumerGroups.PAYMENT_EVENTS_GROUP_ID, containerFactory = "paymentKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, BaseEvent<PaymentEvent>> kafkaRecord) {
        BaseEvent<PaymentEvent> event = kafkaRecord.value();
        System.out.println("Received Order Event: " + event.getData());
        System.out.println("Partition: " + kafkaRecord.partition());
        System.out.println("Offset: " + kafkaRecord.offset());
    }
}

