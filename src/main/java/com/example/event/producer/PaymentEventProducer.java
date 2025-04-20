package com.example.event.producer;

import org.springframework.kafka.core.KafkaTemplate;

import com.example.event.BaseEvent;
import com.example.event.PaymentEvent;

public class PaymentEventProducer extends AbstractGenericKafkaProducer<PaymentEvent> {

    public PaymentEventProducer(
    		KafkaTemplate<String, BaseEvent<PaymentEvent>> kafkaTemplate,
    		String topic,
    		Class<PaymentEvent> eventTypeClass
    	) {
		super(kafkaTemplate, topic, eventTypeClass);
    }

    @Override
    public Class<PaymentEvent> getEventTypeClass() {
        return PaymentEvent.class;
    }
}

