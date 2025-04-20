package com.example.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import com.example.event.BaseEvent;
import com.example.event.OrderEvent;
import com.example.event.PaymentEvent;
import com.fasterxml.jackson.core.type.TypeReference;

@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServer;

    @Bean
    public Map<String, Object> consumerConfigs() {
    	Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // optional
        return props;
    }

    // OrderEvent Consumer Configuration
    
    @Bean
    public JsonDeserializer<BaseEvent<OrderEvent>> orderEventJsonDeserializer() {
        JsonDeserializer<BaseEvent<OrderEvent>> deserializer = new JsonDeserializer<>(new TypeReference<BaseEvent<OrderEvent>>() {});
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(false);
        return deserializer;
    }

    @Bean
    public ConsumerFactory<String, BaseEvent<OrderEvent>> orderConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(), orderEventJsonDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BaseEvent<OrderEvent>> orderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BaseEvent<OrderEvent>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        factory.getContainerProperties().setAckMode(AckMode.RECORD);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(5000L, 3)));
        return factory;
    }
    
    // PaymentEvent Consumer Configuration
    
    @Bean
    public JsonDeserializer<BaseEvent<PaymentEvent>> paymentEventJsonDeserializer() {
        JsonDeserializer<BaseEvent<PaymentEvent>> deserializer = new JsonDeserializer<>(new TypeReference<BaseEvent<PaymentEvent>>() {});
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(false);
        return deserializer;
    }

    @Bean
    public ConsumerFactory<String, BaseEvent<PaymentEvent>> paymentConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(), paymentEventJsonDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BaseEvent<PaymentEvent>> paymentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BaseEvent<PaymentEvent>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        factory.getContainerProperties().setAckMode(AckMode.RECORD);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(5000L, 3)));
        return factory;
    }
}
