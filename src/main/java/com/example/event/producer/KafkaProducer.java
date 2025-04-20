package com.example.event.producer;

import java.util.List;

import com.example.event.BaseEvent;

public interface KafkaProducer<T> {

    void send(BaseEvent<T> event);

    void send(BaseEvent<T> event, String key);

    void sendInBatch(BaseEvent<List<T>> events, int batchSize);

    void sendInBatch(BaseEvent<List<T>> events, int batchSize, String key);
}
