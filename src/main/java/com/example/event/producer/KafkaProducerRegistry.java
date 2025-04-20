package com.example.event.producer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class KafkaProducerRegistry {

    private final Map<Class<?>, KafkaProducer<?>> producerMap = new HashMap<>();

    public KafkaProducerRegistry(List<AbstractGenericKafkaProducer<?>> producers) {
        for (AbstractGenericKafkaProducer<?> producer : producers) {
            producerMap.put(producer.getEventTypeClass(), producer);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> KafkaProducer<T> getProducer(Class<T> eventClass) {
        KafkaProducer<T> producer = (KafkaProducer<T>) producerMap.get(eventClass);
        if (producer == null) {
            throw new IllegalArgumentException("No Kafka producer found for class: " + eventClass.getName());
        }
        return producer;
    }
}