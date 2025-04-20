package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.event.BaseEvent;
import com.example.event.OrderEvent;
import com.example.event.PaymentEvent;
import com.example.event.producer.KafkaProducer;
import com.example.event.producer.KafkaProducerRegistry;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class KafkaTestController {

    private final KafkaProducerRegistry registry;

    @PostMapping("/order")
    public ResponseEntity<String> sendOrderEvent(@RequestBody OrderEvent orderEvent) {
        KafkaProducer<OrderEvent> producer = registry.getProducer(OrderEvent.class);
        BaseEvent<OrderEvent> baseEvent = BaseEvent.of(orderEvent, "order-events", "order-events");
        producer.send(baseEvent, "ABCD1234");
        return ResponseEntity.ok("Order event sent");
    }
    
    @PostMapping("/orders")
    public ResponseEntity<String> sendOrderEvent(@RequestBody List<OrderEvent> orderEvents) {
        KafkaProducer<OrderEvent> producer = registry.getProducer(OrderEvent.class);
        BaseEvent<List<OrderEvent>> baseEvent = BaseEvent.of(orderEvents, "order-events", "order-events");
        producer.sendInBatch(baseEvent, 2,  "ABCD1234");
        return ResponseEntity.ok("Order event sent");
    }
    
    @PostMapping("/payment")
    public ResponseEntity<String> sendOPaymentEvent(@RequestBody PaymentEvent paymentEvent) {
        KafkaProducer<PaymentEvent> producer = registry.getProducer(PaymentEvent.class);
        BaseEvent<PaymentEvent> baseEvent = BaseEvent.of(paymentEvent, "order-events", "order-events");
        producer.send(baseEvent, "ABCD1234");
        return ResponseEntity.ok("Order event sent");
    }
    
    @PostMapping("/payments")
    public ResponseEntity<String> sendPaymentEvent(@RequestBody List<PaymentEvent> paymentEvents) {
        KafkaProducer<PaymentEvent> producer = registry.getProducer(PaymentEvent.class);
        BaseEvent<List<PaymentEvent>> baseEvent = BaseEvent.of(paymentEvents, "order-events", "order-events");
        producer.sendInBatch(baseEvent, 2,  "ABCD1234");
        return ResponseEntity.ok("Order event sent");
    }
}

