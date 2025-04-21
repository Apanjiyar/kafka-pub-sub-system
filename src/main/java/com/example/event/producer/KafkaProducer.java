package com.example.event.producer;

import com.example.event.BaseEvent;

public interface KafkaProducer<T> {

    void send(BaseEvent<T> event);

    void send(BaseEvent<T> event, String key);

    void sendInBatch(BaseEvent<T> events, int batchSize);

    void sendInBatch(BaseEvent<T> events, int batchSize, String key);
}
