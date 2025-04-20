package com.example.event.producer;

import org.springframework.kafka.core.KafkaTemplate;

import com.example.event.BaseEvent;
import com.example.event.OrderEvent;

public class OrderEventProducer extends AbstractGenericKafkaProducer<OrderEvent> {

    public OrderEventProducer(
    		KafkaTemplate<String, BaseEvent<OrderEvent>> kafkaTemplate,
    		String topic,
    		Class<OrderEvent> eventTypeClass
    	) {
		super(kafkaTemplate, topic, eventTypeClass);
    }

    @Override
    public Class<OrderEvent> getEventTypeClass() {
        return OrderEvent.class;
    }
}

