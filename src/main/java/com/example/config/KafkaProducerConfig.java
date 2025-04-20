package com.example.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.constant.KafkaTopicsAndConsumerGroups;
import com.example.event.BaseEvent;
import com.example.event.OrderEvent;
import com.example.event.PaymentEvent;
import com.example.event.producer.OrderEventProducer;
import com.example.event.producer.PaymentEventProducer;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        // Batching-related configurations
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32_768); // 32 KB
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);      // Wait 10ms before sending
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 67_108_864); // Total memory for buffering (64 MB)
        return props;
    }
    
    // OrderEvent Producer Configuration

    @Bean
    public ProducerFactory<String, BaseEvent<OrderEvent>> orderProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, BaseEvent<OrderEvent>> orderKafkaTemplate() {
        return new KafkaTemplate<>(orderProducerFactory());
    }
    
    @Bean
    public OrderEventProducer orderEventProducer(KafkaTemplate<String, BaseEvent<OrderEvent>> kafkaTemplate) {
        return new OrderEventProducer(kafkaTemplate, KafkaTopicsAndConsumerGroups.ORDER_EVENTS_TOPIC, OrderEvent.class);
    }
    
    // PaymentEvent Producer Configuration
    
    @Bean
    public ProducerFactory<String, BaseEvent<PaymentEvent>> paymentProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, BaseEvent<PaymentEvent>> paymentKafkaTemplate() {
        return new KafkaTemplate<>(paymentProducerFactory());
    }
    
    @Bean
    public PaymentEventProducer paymentEventProducer(KafkaTemplate<String, BaseEvent<PaymentEvent>> kafkaTemplate) {
        return new PaymentEventProducer(kafkaTemplate, KafkaTopicsAndConsumerGroups.PAYMENT_EVENTS_TOPIC, PaymentEvent.class);
    }
}
